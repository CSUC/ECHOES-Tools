package org.csuc.echoes.gui.consumer.recollect;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import io.reactivex.Observable;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.EDM.Transformations.formats.utils.SchemaType;
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
import org.openarchives.oai._2.ListRecordsType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ResumptionTokenType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.recollect.core.Recollect;
import org.recollect.core.client.HttpOAIClient;
import org.recollect.core.client.OAIClient;
import org.recollect.core.download.Download;
import org.recollect.core.download.FactoryDownload;
import org.recollect.core.parameters.ListRecordsParameters;
import org.recollect.core.parameters.Parameters;
import org.recollect.core.util.*;

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
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class RecollectQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(RecollectQueueConsumer.class);

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    private org.csuc.entities.Recollect recollect = null;


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
                AtomicInteger batch = new AtomicInteger(0);
                OAIClient oaiClient = new HttpOAIClient(recollect.getHost());
                Recollect recollectOAI = new Recollect(oaiClient);

                if (recollectOAI.isOAI()) {
                    ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
                    listRecordsParameters.withMetadataPrefix(recollect.getMetadataPrefix());
                    listRecordsParameters.withSetSpec(recollect.getSet());

                    recollect.setSize(size(Parameters.parameters().withVerb(Verb.Type.ListRecords).include(listRecordsParameters).toUrl(oaiClient.getURL())));
                    recollectDAO.insert(recollect);

                    if (Objects.nonNull(recollect.getFrom())) {
                        UTCDateProvider dateProvider = new UTCDateProvider();
                        if (Objects.nonNull(recollect.getGranularity()))
                            listRecordsParameters
                                    .withFrom(dateProvider.parse(recollect.getFrom(), Granularity.fromRepresentation(recollect.getGranularity())));
                        else
                            listRecordsParameters.withFrom(dateProvider.parse(recollect.getFrom()));
                    }
                    if (Objects.nonNull(recollect.getGranularity()))
                        listRecordsParameters.withGranularity(recollect.getGranularity());
                    if (Objects.nonNull(recollect.getUntil())) {
                        UTCDateProvider dateProvider = new UTCDateProvider();
                        if (Objects.nonNull(recollect.getGranularity()))
                            listRecordsParameters
                                    .withUntil(dateProvider.parse(recollect.getUntil(), Granularity.fromRepresentation(recollect.getGranularity())));
                        else
                            listRecordsParameters.withFrom(dateProvider.parse(recollect.getUntil()));
                    }

                    Class<?>[] classType = new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class, Memorix.class};

                    Observable<Download> observable =
                            FactoryDownload.createDownloadIterator(
                                    recollectOAI.listRecords(listRecordsParameters, classType), SchemaType.convert(recollect.getSchema())
                            );
                    Path path = Files.createDirectories(Paths.get(applicationConfig.getRecollectFolder(File.separator + map.get("_id").toString()) + File.separator + listRecordsParameters.getSetSpec()));

                    logger.info(path);

                    observable
                            .doOnNext(i -> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                            .subscribe(
                                    (Download l) -> {
                                        if ((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                        logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                        l.execute(path, recollect.getProperties(), FormatType.convert(recollect.getFormat()));
                                    },
                                    e -> {
                                        logger.error("Error: " + e);
                                        appendError(recollect, e.toString());
                                    },
                                    () -> {
                                        //recollect = recollectDAO.getById(map.get("_id").toString());

                                        //if(!recollect.getStatus().equals(Status.ERROR)){
                                        recollect.setStatus(Status.END);
                                        recollect.setDuration(Time.duration(recollect.getTimestamp(), DateTimeFormatter.ISO_TIME));

                                        RecollectLink recollectLink = new RecollectLink();
                                        recollectLink.setStatusLink(StatusLink.NULL);
                                        recollect.setLink(recollectLink);

                                        recollect.setDownload(
                                                Math.toIntExact(Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s/%s", recollect.get_id(), recollect.getSet()))))
                                                        .filter(Files::isRegularFile)
                                                        .filter(f-> f.toString().endsWith(String.format(".%s", FormatType.convert(recollect.getFormat()).extensions().stream().findFirst().get())))
                                                        .count())
                                        );

                                        recollectDAO.getDatastore().save(recollectLink);
                                        recollectDAO.insert(recollect);
                                        // }

                                        logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
                                    }
                            );
                    Thread.sleep(3000);
                } else {
                    logger.error(recollectOAI.gethandleEventErrors());
                    appendError(recollect, recollectOAI.gethandleEventErrors().toString());
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
                    Math.toIntExact(Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s/%s", recollect.get_id(), recollect.getSet()))))
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
