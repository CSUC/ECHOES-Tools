package org.csuc.echoes.gui.consumer.quality;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.QualityDAO;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.Quality;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.entity.Status;
import org.csuc.dao.impl.QualityDAOImpl;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.csuc.echoes.gui.consumer.quality.utils.Time;
import org.csuc.format.Datastore;
import org.csuc.step.Content;
import org.csuc.step.Schema;
import org.csuc.step.Schematron;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.util.FormatType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public class QualityAssuranceQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(QualityAssuranceQueueConsumer.class);

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private QualityDAO qualityDAO = new QualityDAOImpl(Quality.class, client.getDatastore());
    private QualityDetailsDAO qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, client.getDatastore());

    private Quality quality = null;

    /**
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public QualityAssuranceQueueConsumer(Config typesafeRabbitMQ) throws IOException, TimeoutException {
        super(typesafeRabbitMQ);
    }

    @Override
    public void handleConsumeOk(String s) {
        logger.info(String.format("Consumer [ %s ] registered", endPointName));
    }

    @Override
    public void handleCancelOk(String s) {
        logger.info("handleCancelOk:  {}", s);
    }

    @Override
    public void handleCancel(String s) throws IOException {
        logger.info("handleCancel:  {}", s);
    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {
        logger.info("handleShutdownSignal:  {}  {}", s, e);
    }

    @Override
    public void handleRecoverOk(String s) {
        logger.info("handleRecoverOk:  {}", s);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        threadPool.submit(() -> {
            try {
                Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);

                logger.info("[x] Received '{}'", map);

                quality = qualityDAO.getById(map.get("_id").toString());

                quality.setDatasetSize(
                        Math.toIntExact(
                                Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s", quality.getData()))))
                                .filter(Files::isRegularFile)
                                .filter(f-> FormatType.convert(quality.getContentType()).lang().getFileExtensions().stream().anyMatch(m->  f.toString().endsWith(String.format(".%s", m))))
                                .count()));

                qualityDAO.save(quality);

                org.csuc.quality.Quality q =
                        new org.csuc.quality.Quality(
                                new Datastore(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase(), quality,
                                        new Schema(new Schematron(new Content(quality.getQualityConfig())))));

                q.getFormatInterface().execute(Paths.get(applicationConfig.getRecollectFolder(String.format("%s", quality.getData()))));

                quality.setErrorSize(
                        Math.toIntExact(
                            qualityDetailsDAO.countErrorsById(quality.get_id())));

                quality.setQualitySize(quality.getDatasetSize() - quality.getErrorSize());

                quality.setStatus(Status.END);
                quality.setDuration(Time.duration(quality.getTimestamp(), DateTimeFormatter.ISO_TIME));

                qualityDAO.save(quality);

                logger.info(String.format("[x] Consumed '%s\t%s'", map, quality.getDuration()));

                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error(e);
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);

                    //quality.setException(e.getMessage());
                    quality.setStatus(Status.END);
                    quality.setDuration(Time.duration(quality.getTimestamp(), DateTimeFormatter.ISO_TIME));

                    qualityDAO.save(quality);
                } catch (IOException e1) {
                    logger.error(e);
                }
            }
        });
    }

    @Override
    public void run() {
        try {
            // Add a recoverable listener (when broken connections are recovered).
            // Given the way the RabbitMQ factory is configured, the channel should be "recoverable".
            channel.basicQos(typesafeRabbitMQ.getInt("Qos"), false); // Per consumer limit
            //channel.basicQos(1, true);  // Per channel limit
            channel.basicConsume(endPointName, false, this);
        } catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
            logger.error(e);
        }
    }
}
