package org.Recollect.Core.cli;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.Recollect.Core.Recollect;
import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.download.Download;
import org.Recollect.Core.download.FactoryDownload;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.util.Garbage;
import org.Recollect.Core.util.Granularity;
import org.Recollect.Core.util.TimeUtils;
import org.Recollect.Core.util.UTCDateProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.openarchives.oai._2.*;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;

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

        if (bean.getVerb().equals(VerbType.LIST_RECORDS))
            ListRecords();
        else if (bean.getVerb().equals(VerbType.IDENTIFY))
            Identify();
        else if (bean.getVerb().equals(VerbType.LIST_METADATA_FORMATS))
            ListMetadataFormats();
        else if (bean.getVerb().equals(VerbType.LIST_SETS))
            ListSets();
        else if (bean.getVerb().equals(VerbType.GET_RECORD))
            GetRecord();
        else if (bean.getVerb().equals(VerbType.LIST_IDENTIFIERS))
            ListIdentifiers();

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
                downloadRecords(setType.getSetSpec());
                Garbage.gc();
            });
        } else {
            downloadRecords(bean.getSet());
        }
    }

    /**
     *
     */
    private static void Identify() {
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
    private static void ListMetadataFormats() {
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
            logger.info("name           : {}", s.getSetName());
            logger.info("spec           : {}", s.getSetSpec());
            logger.info("description    : {}\n", s.getSetDescription());
        });
    }

    /**
     *
     * @throws Exception
     */
    private static void GetRecord() throws Exception {
        AtomicInteger batch = new AtomicInteger(0);
        Instant timeRecord = Instant.now();

        try{
            OAIClient oaiClient = new HttpOAIClient(bean.getHost());
            Recollect recollect = new Recollect(oaiClient);

            GetRecordParameters getRecordParameters = new GetRecordParameters();
            getRecordParameters.withIdentifier(bean.getIdentifier());
            getRecordParameters.withMetadataFormatPrefix(bean.getMetadataPrefix());

            if (Objects.nonNull(bean.getOut()))
                pathWithSetSpec = Files.createDirectories(bean.getOut());

            //FactoryDownload.createDownloadRecordType(recollect.getRecord(getRecordParameters, new Class[] { OAIPMHtype.class, A2AType.class, OaiDcType.class }),xslt);

            Observable<Download> observable;

            if(Objects.isNull(bean.getXslt())) {
                observable = FactoryDownload.createDownloadRecordType(recollect.getRecord(getRecordParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}), bean.getXslt());

                observable
                        .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                        .groupBy(i -> batch.getAndIncrement() % bean.getThreads())
                        .flatMap(g -> g.observeOn(Schedulers.io()))
                        .observeOn(Schedulers.io())
                        .subscribe(
                                (Download l) ->{
                                    logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                    l.execute(pathWithSetSpec, bean.getArguments() );
                                } ,
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", getRecordParameters.getIdentifier(), TimeUtils.duration(timeRecord, DateTimeFormatter.ISO_TIME)))
                        );
                Thread.sleep(3000);
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
    private static void downloadRecords(String s) {
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

            Observable<Download> observable;

            if(Objects.isNull(bean.getXslt()))
                observable = FactoryDownload.createDownloadIterator(recollect.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class }), bean.getXslt());
            else observable = FactoryDownload.createDownloadIterator(recollect.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class}), bean.getXslt());

            if (Objects.nonNull(bean.getOut())) {
                pathWithSetSpec = Files.createDirectories(Paths.get(bean.getOut() + File.separator + listRecordsParameters.getSetSpec()));

                observable
                        .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                        .groupBy(i -> batch.getAndIncrement() % bean.getThreads())
                        .flatMap(g -> g.observeOn(Schedulers.io()))
                        .observeOn(Schedulers.io())
                        .subscribe(
                                (Download l) ->{
                                    logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                    l.execute(pathWithSetSpec, bean.getArguments() );
                                } ,
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(timeSet, DateTimeFormatter.ISO_TIME)))
                        );
                Thread.sleep(3000);
            }else{
                observable
                        .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                        .observeOn(Schedulers.io())
                        .subscribe(
                                (Download l) ->{
                                    logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                    l.execute(bean.getArguments() );
                                } ,
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(timeSet, DateTimeFormatter.ISO_TIME)))
                        );
                Thread.sleep(3000);
            }


        } catch (Exception e) {
            logger.error(e);
        }
        //Garbage.gc();
    }

}
