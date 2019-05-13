package org.transformation.cli;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.core.HDFS;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.util.FormatType;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.openarchives.oai._2.*;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.Recollect;
import org.transformation.client.HttpOAIClient;
import org.transformation.client.OAIClient;
import org.transformation.download.Download;
import org.transformation.download.FactoryDownload;
import org.transformation.parameters.GetRecordParameters;
import org.transformation.parameters.ListIdentifiersParameters;
import org.transformation.parameters.ListRecordsParameters;
import org.transformation.parameters.Parameters;
import org.transformation.util.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author amartinez
 *
 *
 *
 */
public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    private static Instant inici = Instant.now();

    private static Path pathWithSetSpec;

    private static UTCDateProvider dateProvider = new UTCDateProvider();

    private static ArgsBean bean;

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }

        if(bean.isListRecords())    ListRecords();
        if(bean.isIdentify())   Identify();
        if(bean.isListMetadataFormats())    ListMetadataFormats();
        if(bean.isListSets())   ListSets();
        if(bean.isGetRecord())  GetRecord();
        if(bean.isListIdentifiers())    ListIdentifiers();

        Thread.sleep(3000);
        logger.info(String.format("End Recollect %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
    }

    /**
     * @throws Exception
     *
     */
    private static void ListRecords() throws Exception {
        if (Objects.isNull(bean.getSet())) {
            OAIClient oaiClient = new HttpOAIClient(bean.getHost());
            Recollect recollect = new Recollect(oaiClient);

            recollect.listSets().forEachRemaining((SetType setType) -> {
                try {
                    downloadRecords(setType.getSetSpec());
                } catch (IOException e) {
                    logger.error(e);
                } catch (NoSuchAlgorithmException e) {
                    logger.error(e);
                } catch (KeyStoreException e) {
                    logger.error(e);
                } catch (KeyManagementException e) {
                    logger.error(e);
                } catch (Exception e) {
                    logger.error(e);
                }
                Garbage.gc();
            });
        } else {
            downloadRecords(bean.getSet());
        }
    }

    /**
     *
     */
    private static void Identify() throws Exception {
        OAIClient oaiClient = new HttpOAIClient(bean.getHost());
        Recollect recollect = new Recollect(oaiClient);

        IdentifyType identify = recollect.identify();

        logger.info("RepositoryName		: {}", identify.getRepositoryName());
        logger.info("BaseURL			: {}", identify.getBaseURL());
        logger.info("ProtocolVersion	: {}", identify.getProtocolVersion());
        logger.info("AdminEmail			: {}", identify.getAdminEmail());
        logger.info("EarliestDatestamp	: {}", identify.getEarliestDatestamp());
        logger.info("DeletedRecord		: {}", identify.getDeletedRecord());
        logger.info("Granularity		: {}", identify.getGranularity());
        logger.info("getCompression		: {}", identify.getCompression());
        logger.info("Description		: {}\n", identify.getDescription());
    }

    /**
     *
     */
    private static void ListMetadataFormats() throws Exception {
        OAIClient oaiClient = new HttpOAIClient(bean.getHost());
        Recollect recollect = new Recollect(oaiClient);

        recollect.listMetadataFormats().forEachRemaining(metadataFormatType ->{
            logger.info("MetadataPrefix     : {}", metadataFormatType.getMetadataPrefix());
            logger.info("Schema             : {}", metadataFormatType.getSchema());
            logger.info("MetadataNamespace  : {}\n", metadataFormatType.getMetadataNamespace());
        });
    }

    /**
     * @return
     * @throws Exception
     *
     */
    private static void ListSets() throws Exception {
        OAIClient oaiClient = new HttpOAIClient(bean.getHost());
        Recollect recollect = new Recollect(oaiClient);

        Iterator<SetType> sets = recollect.listSets();
        sets.forEachRemaining(s->{
            logger.info("name                : {}", s.getSetName());
            logger.info("spec                : {}", s.getSetSpec());
            logger.info("description         : {}", s.getSetDescription());

            if(Objects.nonNull(bean.getMetadataPrefix())){
                try {
                    ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
                    listRecordsParameters.withSetSpec(s.getSetSpec());
                    listRecordsParameters.withMetadataPrefix(bean.getMetadataPrefix());

                    OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(new URL(Parameters.parameters().withVerb(Verb.Type.ListRecords).include(listRecordsParameters).toUrl(oaiClient.getURL())), new Class[]{OAIPMHtype.class}).getObject();
                    logger.info("completeListSize    : {}\n", oaipmHtype.getListRecords().getResumptionToken().getCompleteListSize());
                } catch (MalformedURLException e) {
                    logger.error(e);
                }
            }else logger.info("\n");
        });
    }

    /**
     *
     * @throws Exception
     */
    private static void GetRecord() throws Exception {
        String uuid = UUID.randomUUID().toString();

        AtomicInteger batch = new AtomicInteger(0);
        Instant timeRecord = Instant.now();

        try{
            OAIClient oaiClient = new HttpOAIClient(bean.getHost());
            Recollect recollect = new Recollect(oaiClient);

            GetRecordParameters getRecordParameters = new GetRecordParameters();
            getRecordParameters.withIdentifier(bean.getIdentifier());
            getRecordParameters.withMetadataFormatPrefix(bean.getMetadataPrefix());

            Class<?>[] classType = new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class, Memorix.class};

            Observable<Download> observable =
                    FactoryDownload.createDownloadRecordType(
                            recollect.getRecord(getRecordParameters, classType),
                            bean.getSchema()
                    );

            if(bean.isHdfs()){
                HDFS hdfs = new HDFS(bean.getHdfsuri(), bean.getHdfsuser(), bean.getHdfshome());

                AtomicInteger assigner = new AtomicInteger(0);

                observable
                        .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                        .groupBy(i -> assigner.incrementAndGet() % bean.getThreads())
                        .flatMap(grp -> grp.observeOn(Schedulers.io())
                                .map(i2 -> intenseCalculation(i2, hdfs.getFileSystem(), new org.apache.hadoop.fs.Path(MessageFormat.format("recollect/{0}/{1}", uuid, bean.getIdentifier())), bean.getArguments(), bean.getFormat()))
                        )
                        .subscribe(
                                (Download l) -> {
                                    if((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                    logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                                },
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", timeRecord, TimeUtils.duration(timeRecord, DateTimeFormatter.ISO_TIME)))
                        );
            }else{
                if (Objects.nonNull(bean.getOut())) {
                    pathWithSetSpec = Files.createDirectories(Paths.get(MessageFormat.format("{0}/{1}", bean.getOut(), uuid)));

                    AtomicInteger assigner = new AtomicInteger(0);

                    observable
                            .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                            .groupBy(i -> assigner.incrementAndGet() % bean.getThreads())
                            .flatMap(grp -> grp.observeOn(Schedulers.io())
                                    .map(i2 -> intenseCalculation(i2, pathWithSetSpec, bean.getArguments(), bean.getFormat()))
                            )
                            .subscribe(
                                    (Download l) -> {
                                        if((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                        logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                                    },
                                    e -> logger.error("Error: " + e),
                                    () -> logger.info(String.format("Completed %s: %s", timeRecord, TimeUtils.duration(timeRecord, DateTimeFormatter.ISO_TIME)))
                            );
                } else {

                    AtomicInteger assigner = new AtomicInteger(0);

                    observable
                            .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                            .groupBy(i -> assigner.incrementAndGet() % bean.getThreads())
                            .flatMap(grp -> grp.observeOn(Schedulers.io())
                                    .map(i2 -> intenseCalculation(i2, bean.getArguments(), bean.getFormat()))
                            )
                            .subscribe(
                                    (Download l) -> {
                                        if((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                        logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                                    },
                                    e -> logger.error("Error: " + e),
                                    () -> logger.info(String.format("Completed %s: %s", timeRecord, TimeUtils.duration(timeRecord, DateTimeFormatter.ISO_TIME)))
                            );
                }
            }
        }catch(Exception e) {
            logger.error(e);
        }
    }

    /**
     *
     * @throws Exception
     */
    private static void ListIdentifiers() throws Exception {
        OAIClient oaiClient = new HttpOAIClient(bean.getHost());
        Recollect recollect = new Recollect(oaiClient);

        ListIdentifiersParameters listIdentifiersParameters = new ListIdentifiersParameters();
        listIdentifiersParameters.withMetadataPrefix(bean.getMetadataPrefix());

        Iterator<HeaderType> record = recollect.listIdentifiers(listIdentifiersParameters);

        try {
            record.forEachRemaining((HeaderType headerType) ->{
                logger.info("Identifier : {}", headerType.getIdentifier());
                logger.info("Datestamp  : {}", headerType.getDatestamp());
                logger.info("SetSpec    : {}", headerType.getSetSpec());
                logger.info("Status     : {}\n", headerType.getStatus());
            });
        } catch (Exception e) {
            logger.error(e);
        }

    }

    /**
     *
     * @param s
     */
    private static void downloadRecords(String s) throws Exception {
        String uuid = UUID.randomUUID().toString();

        AtomicInteger batch = new AtomicInteger(0);
        Instant timeSet = Instant.now();
        bean.getArguments().put("set", s);

        OAIClient oaiClient = new HttpOAIClient(bean.getHost());
        Recollect recollect = new Recollect(oaiClient);

        try {
            ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
            listRecordsParameters.withMetadataPrefix(bean.getMetadataPrefix());
            listRecordsParameters.withSetSpec(s);

            if (Objects.nonNull(bean.getResumptionToken()))
                listRecordsParameters.withgetResumptionToken(bean.getResumptionToken());

            if (Objects.nonNull(bean.getFrom())) {
                if (Objects.nonNull(bean.getGranularity()))
                    listRecordsParameters
                            .withFrom(dateProvider.parse(bean.getFrom(), Granularity.fromRepresentation(bean.getGranularity())));
                else
                    listRecordsParameters.withFrom(dateProvider.parse(bean.getFrom()));
            }
            if (Objects.nonNull(bean.getGranularity()))
                listRecordsParameters.withGranularity(bean.getGranularity());
            if (Objects.nonNull(bean.getUntil())) {
                if (Objects.nonNull(bean.getGranularity()))
                    listRecordsParameters
                            .withUntil(dateProvider.parse(bean.getUntil(), Granularity.fromRepresentation(bean.getGranularity())));
                else
                    listRecordsParameters.withFrom(dateProvider.parse(bean.getUntil()));
            }

            Class<?>[] classType = new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class, Memorix.class};

            Observable<Download> observable =
                    FactoryDownload.createDownloadIterator(
                        recollect.listRecords(listRecordsParameters, classType),
                        bean.getSchema()
                    );

            if(bean.isHdfs()){
                HDFS hdfs = new HDFS(bean.getHdfsuri(), bean.getHdfsuser(), bean.getHdfshome());

                AtomicInteger assigner = new AtomicInteger(0);

                observable
                        .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                        .groupBy(i -> assigner.incrementAndGet() % bean.getThreads())
                        .flatMap(grp -> grp.observeOn(Schedulers.io())
                                .map(i2 -> intenseCalculation(i2, hdfs.getFileSystem(), new org.apache.hadoop.fs.Path(MessageFormat.format("recollect/{0}/{1}", uuid, s)), bean.getArguments(), bean.getFormat()))
                        )
                        .subscribe(
                                (Download l) -> {
                                    if((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                    logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                                },
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(timeSet, DateTimeFormatter.ISO_TIME)))
                        );
            }else{
                if (Objects.nonNull(bean.getOut())) {
                    pathWithSetSpec = Files.createDirectories(Paths.get(MessageFormat.format("{0}/{1}/{2}", bean.getOut(), uuid, listRecordsParameters.getSetSpec())));

                    AtomicInteger assigner = new AtomicInteger(0);

                    observable
                            .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                            .groupBy(i -> assigner.incrementAndGet() % bean.getThreads())
                            .flatMap(grp -> grp.observeOn(Schedulers.io())
                                    .map(i2 -> intenseCalculation(i2, pathWithSetSpec, bean.getArguments(), bean.getFormat()))
                            )
                            .subscribe(
                                    (Download l) -> {
                                        if((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                        logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                                    },
                                    e -> logger.error("Error: " + e),
                                    () -> logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(timeSet, DateTimeFormatter.ISO_TIME)))
                            );
                } else {
                    AtomicInteger assigner = new AtomicInteger(0);

                    observable
                            .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                            .groupBy(i -> assigner.incrementAndGet() % bean.getThreads())
                            .flatMap(grp -> grp.observeOn(Schedulers.io())
                                    .map(i2 -> intenseCalculation(i2, bean.getArguments(), bean.getFormat()))
                            )
                            .subscribe(
                                    (Download l) -> {
                                        if((batch.incrementAndGet() % 25000) == 0) Garbage.gc();
                                        logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                                    },
                                    e -> logger.error("Error: " + e),
                                    () -> logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(timeSet, DateTimeFormatter.ISO_TIME)))
                            );

                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        //Garbage.gc();
    }

    public static <T> T intenseCalculation(T value, FileSystem fileSystem, org.apache.hadoop.fs.Path dest, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(fileSystem, dest, arguments, formatType);

        return value;
    }

    public static <T> T intenseCalculation(T value, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(arguments, formatType);

        return value;
    }

    public static <T> T intenseCalculation(T value, Path dest, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(dest, arguments, formatType);

        return value;
    }
}
