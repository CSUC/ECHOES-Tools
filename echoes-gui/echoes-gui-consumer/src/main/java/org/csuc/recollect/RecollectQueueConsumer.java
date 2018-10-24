package org.csuc.recollect;

import com.rabbitmq.client.*;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import nl.mindbus.a2a.A2AType;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.RecollectError;
import org.csuc.entities.RecollectLink;
import org.csuc.utils.Status;
import org.csuc.utils.recollect.StatusLink;
import org.Recollect.Core.Recollect;
import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.download.Download;
import org.Recollect.Core.download.FactoryDownload;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.util.Granularity;
import org.Recollect.Core.util.TimeUtils;
import org.Recollect.Core.util.UTCDateProvider;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.EndPoint;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.Time;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class RecollectQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(RecollectQueueConsumer.class);

    private Client client = new Client("localhost", 27017, "echoes");

    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    private org.csuc.typesafe.server.Application serverConfig = new ServerConfig(null).getConfig();

    private org.csuc.entities.Recollect recollect = null;


    /**
     * @param endpointName
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public RecollectQueueConsumer(String endpointName, RabbitMQConfig typesafeRabbitMQ) throws IOException, TimeoutException {
        super(endpointName, typesafeRabbitMQ);
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

                Iterator iteratorRecordType = recollectOAI.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class});

                Observable<Download> observable = FactoryDownload.createDownloadIterator(iteratorRecordType, null);

                Path path = Files.createDirectories(Paths.get(serverConfig.getRecollectFolder(File.separator + map.get("_id").toString()) + File.separator + listRecordsParameters.getSetSpec()));

                org.csuc.entities.Recollect finalRecollect = recollect;
                org.csuc.entities.Recollect finalRecollect1 = recollect;

                observable
                        .doOnNext(i -> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                        .groupBy(i -> batch.getAndIncrement() % 4)
                        .flatMap(g -> g.observeOn(Schedulers.io()))
                        .observeOn(Schedulers.io())
                        .subscribe(
                                (Download l) -> {
                                    logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                    l.execute(path, finalRecollect.getProperties());
                                },
                                e -> {
                                    appendError(finalRecollect1, e.toString());
                                    logger.error("Error: " + e);
                                },
                                () -> {
                                    recollect = recollectDAO.getById(map.get("_id").toString());
                                    recollect.setStatus(Status.END);
                                    recollect.setDuration(Time.duration(LocalDateTime.parse(recollect.getTimestamp()), DateTimeFormatter.ISO_TIME));

                                    RecollectLink recollectLink = new RecollectLink();
                                    recollectLink.setStatusLink(StatusLink.NULL);
                                    recollect.setLink(recollectLink);

                                    recollectDAO.getDatastore().save(recollectLink);
                                    recollectDAO.insert(recollect);

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
            appendError(recollect, e.toString());
            try {
                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (IOException e1) {
                logger.error(e);
            }
        }
    }

    private void appendError(org.csuc.entities.Recollect recollect, String e) {
        if (Objects.nonNull(recollect)) {
            recollect.setStatus(Status.ERROR);
            recollect.setDuration(Time.duration(LocalDateTime.parse(recollect.getTimestamp()), DateTimeFormatter.ISO_TIME));

            RecollectError recollectError = new RecollectError();

            recollectError.setException(e);

            recollect.setError(recollectError);

            recollectDAO.getDatastore().save(recollectError);
            recollectDAO.save(recollect);
        }
    }

    @Override
    public void run() {
        try {
            // Add a recoverable listener (when broken connections are recovered).
            // Given the way the RabbitMQ factory is configured, the channel should be "recoverable".
            channel.basicQos(1, false); // Per consumer limit
            //channel.basicQos(1, true);  // Per channel limit
            channel.basicConsume(endPointName, false, this);
        } catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
            logger.error(e);
        }
    }
}
