package org.csuc.echoes.gui.consumer.recollect;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.echoes.gui.consumer.recollect.utils.Time;
import org.csuc.entities.RecollectError;
import org.csuc.entities.RecollectLink;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.util.FormatType;
import org.csuc.utils.Status;
import org.csuc.utils.recollect.StatusLink;
import org.csuc.utils.recollect.TransformationType;
import org.openarchives.oai._2.ListRecordsType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ResumptionTokenType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.download.Download;
import org.transformation.factory.*;

import javax.xml.bind.JAXBIntrospector;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public class RecollectQueueConsumer extends EndPoint implements Runnable, Consumer {

    private static Logger logger = LogManager.getLogger(RecollectQueueConsumer.class);

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    private org.csuc.entities.Recollect recollect = null;

    private Transformation transformation = null;

    /**
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public RecollectQueueConsumer(Config typesafeRabbitMQ) throws IOException, TimeoutException {
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
        threadPool.submit(()->{
            try {
                Instant inici = Instant.now();

                Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);

                logger.info("[x] Received '{}'", map);

                recollect = recollectDAO.getById(map.get("_id").toString());

                logger.info(recollect);

                recollect.setStatus(Status.PROGRESS);
                recollectDAO.insert(recollect);

                //Recollect
                Class<?>[] classType = new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class, Memorix.class, Ead.class};

                if(Objects.equals(TransformationType.OAI, recollect.getType())) transformation = new TransformationOai2(new URL(recollect.getInput()), classType);
                if(Objects.equals(TransformationType.URL, recollect.getType()))   transformation = new TransformationUrl(new URL(recollect.getInput()), classType);
                if(Objects.equals(TransformationType.FILE, recollect.getType()))  transformation = new TransformationFile(Paths.get(recollect.getInput()), classType);

                if (Objects.nonNull(transformation)) {
                    Path path =
                            Files.createDirectories(Paths.get(applicationConfig.getRecollectFolder(File.separator + map.get("_id").toString())));

                    new ForkJoinPool().submit(() ->
                    {
                        try {
                            transformation.path(
                                    path,
                                    recollect.getProperties(),
                                    FormatType.convert(recollect.getFormat())
                            );
                        } catch (IOException e) {
                        }

                    }).join();

                    if(Objects.nonNull(transformation.getExceptions()))
                        logger.info(transformation.getExceptions());

                    //recollect.setSize(size(recollect.getInput()));
                    recollect.setStatus(Status.END);
                    recollect.setDuration(Time.duration(recollect.getTimestamp(), DateTimeFormatter.ISO_TIME));

                    RecollectLink recollectLink = new RecollectLink();
                    recollectLink.setStatusLink(StatusLink.NULL);
                    recollect.setLink(recollectLink);

                    recollect.setDownload(
                            Math.toIntExact(Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s", recollect.get_id()))))
                                    .filter(Files::isRegularFile)
                                    .filter(f-> f.toString().endsWith(String.format(".%s", FormatType.convert(recollect.getFormat()).extensions().stream().findFirst().get())))
                                    .count())
                    );

                    recollectDAO.getDatastore().save(recollectLink);
                    recollectDAO.insert(recollect);

                    Thread.sleep(5000);
                }

                logger.info(String.format("[x] Consumed '%s\t%s'", map, recollect.getDuration()));

                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error(e);
                try {
                    appendError(recollect, e.toString());

                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e1) {
                    logger.error(e);
                }
            }
        });
    }

    public static <T> T intenseCalculation(T value, Path dest, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(dest, arguments, formatType);

        return value;
    }

    private void appendError(org.csuc.entities.Recollect recollect, String e) throws IOException {
        if (Objects.nonNull(recollect)) {
            recollect.setStatus(Status.END);
            recollect.setDuration(Time.duration(recollect.getTimestamp(), DateTimeFormatter.ISO_TIME));

            RecollectError recollectError = new RecollectError();

            recollectError.setException(e);

            recollect.setError(recollectError);

            RecollectLink recollectLink = new RecollectLink();
            recollectLink.setStatusLink(StatusLink.NULL);
            recollect.setLink(recollectLink);

            recollect.setDownload(
                    Math.toIntExact(Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s", recollect.get_id()))))
                            .filter(Files::isRegularFile)
                            .filter(f-> f.toString().endsWith(String.format(".%s", FormatType.convert(recollect.getFormat()).extensions().stream().findFirst().get())))
                            .count())
            );

            recollectDAO.getDatastore().save(recollectError);
            recollectDAO.save(recollect);
        }
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

    private int size(String value){
        try {
            OAIPMHtype oaipmHtype  = (OAIPMHtype) JAXBIntrospector.getValue(new JaxbUnmarshal(new URL(value), new Class[]{OAIPMHtype.class}).getObject());

            return
                    Optional.ofNullable(oaipmHtype.getListRecords())
                            .map(ListRecordsType::getResumptionToken)
                            .map(ResumptionTokenType::getCompleteListSize)
                            .map(BigInteger::intValueExact)
                            .orElse(0);
        } catch (MalformedURLException e) {
            return 0;
        }
    }
}
