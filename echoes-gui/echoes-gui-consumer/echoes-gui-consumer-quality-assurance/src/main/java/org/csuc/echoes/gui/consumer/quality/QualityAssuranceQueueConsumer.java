package org.csuc.echoes.gui.consumer.quality;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.RDF;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Format;
import org.csuc.client.Client;
import org.csuc.dao.impl.quality.QualityDAOImpl;
import org.csuc.dao.impl.quality.QualityDetailsDAOImpl;
import org.csuc.dao.quality.QualityDAO;
import org.csuc.dao.quality.QualityDetailsDAO;
import org.csuc.echoes.gui.consumer.quality.schematron.Schematron;
import org.csuc.echoes.gui.consumer.quality.utils.FileUtils;
import org.csuc.echoes.gui.consumer.quality.utils.Time;
import org.csuc.echoes.gui.consumer.quality.schema.Schema;
import org.csuc.entities.quality.Quality;
import org.csuc.entities.quality.QualityDetails;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.util.FormatType;
import org.csuc.utils.Status;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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

                Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s", quality.getData()))))
                        .filter(Files::isRegularFile)
                        .filter(f-> FormatType.convert(quality.getContentType()).lang().getFileExtensions().stream().anyMatch(m->  f.toString().endsWith(String.format(".%s", m))))
                        .parallel()
                        .forEach((Path f) -> {
                            logger.debug(f);
                            try {
                                Schema schema;
                                Path temporal = null;

                                if(!Objects.equals(FormatType.RDFXML, FormatType.convert(quality.getContentType()))){
                                    temporal  = Files.createTempFile("quality_", ".rdf");

                                    Format.format(f.toFile(), FormatType.RDFXML, new FileOutputStream(temporal.toFile()));

                                    schema = new Schema(new FileInputStream(temporal.toFile()), RDF.class);
                                }else
                                    schema = new Schema(new FileInputStream(f.toFile()), RDF.class);

                                logger.debug("{}:    schema:     {}", f.getFileName(), schema.isValid());

                                QualityDetails qualityDetails = new QualityDetails();

                                qualityDetails.setValue(FilenameUtils.getName(f.getFileName().toString()));

                                if (!schema.isValid()){
                                    logger.debug("\tMessage:  {}", schema.getError().getMessage());

                                    qualityDetails.setQuality(quality);
                                    qualityDetails.setSchema(new org.csuc.entities.quality.Schema(schema.getError().getMessage()));

                                }else {
                                    qualityDetails.setValidSchema(true);

                                    if(Schematron.isValid(f.toFile())) {
                                        FileUtils.copy(f, Paths.get(applicationConfig.getQualityFolder((String) map.get("_id"))));
                                        qualityDetails.setValidSchematron(true);
                                    }else {
                                        qualityDetails.setSchematron(
                                                Schematron.getSVRLFailedAssert(f.toFile())
                                                        .stream()
                                                        .map(m-> new org.csuc.entities.quality.Schematron(m.getTest(), m.getText()))
                                                        .collect(Collectors.toList())
                                        );
                                        qualityDetails.setQuality(quality);
                                    }
                                }
                                qualityDAO.getDatastore().save(qualityDetails);

                                if(Objects.nonNull(temporal))   temporal.toFile().delete();
                            } catch (Exception e) {
                                logger.error(e);
                            }
                        });

                quality.setQualitySize(
                        Math.toIntExact(
                                Files.walk(Paths.get(applicationConfig.getQualityFolder(String.format("%s", quality.get_id()))))
                                .filter(f-> FormatType.convert(quality.getContentType()).lang().getFileExtensions().stream().anyMatch(m->  f.toString().endsWith(String.format(".%s", m))))
                                .filter(Files::isRegularFile).count()));

                quality.setErrorSize(
                        Math.toIntExact(
                            qualityDetailsDAO.countErrorsById(quality.get_id())));

                quality.setStatus(Status.END);
                quality.setDuration(Time.duration(quality.getTimestamp(), DateTimeFormatter.ISO_TIME));

                qualityDAO.save(quality);

                logger.info(String.format("[x] Consumed '%s\t%s'", map, quality.getDuration()));

                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error(e);
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);

                    quality.setException(e.getMessage());
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
