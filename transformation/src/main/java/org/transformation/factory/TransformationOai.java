package org.transformation.factory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.core.HDFS;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.util.FormatType;
import org.edm.transformations.formats.utils.SchemaType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.StatusType;
import org.transformation.download.Download;
import org.transformation.download.Jaxb;
import org.transformation.util.Garbage;
import org.transformation.util.TimeUtils;

import javax.xml.bind.JAXBIntrospector;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TransformationOai implements Transformation {

    private Logger logger = LogManager.getLogger(this.getClass());

    private Instant inici = Instant.now();

    private Class<?>[] classType;
    private URL input;
    private int threads = Runtime.getRuntime().availableProcessors();
    private int buffer = 15000;

    private List<Throwable> throwables = new ArrayList<>();

    public TransformationOai(URL input, Class<?>[] classType) {
        this.classType = classType;
        this.input = input;
    }

    public TransformationOai(URL input, Class<?>[] classType, int threads, int buffer) {
        this.classType = classType;
        this.input = input;
        this.threads = threads;
        this.buffer = buffer;
    }

    @Override
    public void console(SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException {
        AtomicInteger batch = new AtomicInteger(0);

        Observable<Download> observable = Observable.create(emitter -> {
            iterate(input, emitter, schemaType);

            emitter.onComplete();
        });

        AtomicInteger assigner = new AtomicInteger(0);

        observable
                .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                .groupBy(i -> assigner.incrementAndGet() % 4)
                .flatMap(grp -> grp.observeOn(Schedulers.io())
                        .map(i2 -> intenseCalculation(i2, arguments, formatType))
                )
                .subscribe(
                        (Download l) -> {
                            if ((batch.incrementAndGet() % buffer) == 0) Garbage.gc();
                            logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                        },
                        e -> {
                            logger.error("Error: " + e.getMessage());
                            throwables.add(e);
                        },
                        () -> logger.info(String.format("Completed %s %s", input, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)))
                );
    }

    @Override
    public void path(Path out, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException {
        AtomicInteger batch = new AtomicInteger(0);

        Observable<Download> observable = Observable.create(emitter -> {
            iterate(input, emitter, schemaType);

            emitter.onComplete();
        });

        AtomicInteger assigner = new AtomicInteger(0);

        observable
                .doOnNext(i -> logger.info("Emiting  {} in {}", i, Thread.currentThread().getName()))
                .groupBy(i -> assigner.incrementAndGet() % threads)
                .flatMap(grp -> grp.observeOn(Schedulers.io())
                        .map(i2 -> intenseCalculation(i2, out, arguments, formatType))
                        .filter(Objects::nonNull)
                )
                .subscribe(
                        (Download l) -> {
                            if ((batch.incrementAndGet() % buffer) == 0) Garbage.gc();
                            logger.info("Received in {} value {}", Thread.currentThread().getName(), l);
                        },
                        e -> {
                            logger.error("Error: " + e);
                            throwables.add(e);
                        },
                        () -> logger.info(String.format("Completed %s %s", input, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)))
                );

    }

    @Override
    public void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException {
        HDFS hdfs = new HDFS(hdfsuri, hdfuser, hdfshome);
        FileSystem fileSystem = hdfs.getFileSystem();

        String uuid = UUID.randomUUID().toString();

        iterate(input, fileSystem, path, schemaType, arguments, formatType);
    }

    @Override
    public List<Throwable> getExceptions() {
        return throwables.isEmpty() ? null : throwables;
    }

    /**
     * @param value
     * @param fileSystem
     * @param dest
     * @param arguments
     * @param formatType
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T intenseCalculation(T value, FileSystem fileSystem, org.apache.hadoop.fs.Path dest, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(fileSystem, dest, arguments, formatType);

        return value;
    }

    /**
     * @param value
     * @param arguments
     * @param formatType
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T intenseCalculation(T value, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(arguments, formatType);

        return value;
    }

    /**
     * @param value
     * @param dest
     * @param arguments
     * @param formatType
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T intenseCalculation(T value, Path dest, Map<String, String> arguments, FormatType formatType) throws Exception {
        logger.info("Calculating in {} value {}", Thread.currentThread().getName(), value);

        ((Download) value).execute(dest, arguments, formatType);

        return value;
    }

    /**
     * @param url
     * @param emitter
     * @param schemaType
     * @throws MalformedURLException
     */
    private void iterate(URL url, ObservableEmitter<Download> emitter, SchemaType schemaType) throws MalformedURLException {
        OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(url, classType).getObject();

        if (Objects.nonNull(oaipmHtype)) {
            oaipmHtype.getListRecords().getRecord().stream().parallel().forEach(recordType -> {
                try {
                    if (!Objects.equals(StatusType.DELETED, recordType.getHeader().getStatus())) {
                        if (!Objects.equals(schemaType.getType(), JAXBIntrospector.getValue(recordType.getMetadata().getAny()).getClass()))
                            throw new Exception(String.format("recordType \"%s\" is not a %s valid schema", recordType.getHeader().getIdentifier(), schemaType.getType()));

                        emitter.onNext(new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType));
                    } else
                        logger.debug("{} - {}", recordType.getHeader().getIdentifier(), recordType.getHeader().getStatus());

                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throwables.add(e);
                }
            });

            if (oaipmHtype.getListRecords().getResumptionToken() != null)
                if (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty())
                    iterate(next(url, oaipmHtype.getListRecords().getResumptionToken().getValue()), emitter, schemaType);
        }
    }

    private void iterate(URL url,  Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws MalformedURLException, URISyntaxException {
        OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(url, classType).getObject();

        if (Objects.nonNull(oaipmHtype)) {
            oaipmHtype.getListRecords().getRecord().stream().parallel().forEach(recordType -> {
                try {
                    if (!Objects.equals(StatusType.DELETED, recordType.getHeader().getStatus())) {
                        if (!Objects.equals(schemaType.getType(), JAXBIntrospector.getValue(recordType.getMetadata().getAny()).getClass()))
                            throw new Exception(String.format("recordType \"%s\" is not a %s valid schema", recordType.getHeader().getIdentifier(), schemaType.getType()));

                        intenseCalculation(
                                new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType),
                                path,
                                arguments,
                                formatType
                        );
                    } else
                        logger.debug("{} - {}", recordType.getHeader().getIdentifier(), recordType.getHeader().getStatus());

                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throwables.add(e);
                }

            });

            if (oaipmHtype.getListRecords().getResumptionToken() != null)
                if (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty())
                    iterate(
                            next(url, oaipmHtype.getListRecords().getResumptionToken().getValue()),
                            path,
                            schemaType,
                            arguments,
                            formatType);
        }
    }

    /**
     * @param url
     * @param fileSystem
     * @param path
     * @param schemaType
     * @param arguments
     * @param formatType
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    private void iterate(URL url, FileSystem fileSystem, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws MalformedURLException, URISyntaxException {
        OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(url, classType).getObject();

        if (Objects.nonNull(oaipmHtype)) {
            oaipmHtype.getListRecords().getRecord().stream().parallel().forEach(recordType -> {
                try {
                    if (!Objects.equals(StatusType.DELETED, recordType.getHeader().getStatus())) {
                        if (!Objects.equals(schemaType.getType(), JAXBIntrospector.getValue(recordType.getMetadata().getAny()).getClass()))
                            throw new Exception(String.format("recordType \"%s\" is not a %s valid schema", recordType.getHeader().getIdentifier(), schemaType.getType()));

                        intenseCalculation(
                                new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType),
                                fileSystem,
                                path,
                                arguments,
                                formatType
                        );
                    } else
                        logger.debug("{} - {}", recordType.getHeader().getIdentifier(), recordType.getHeader().getStatus());

                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throwables.add(e);
                }
            });

            if (oaipmHtype.getListRecords().getResumptionToken() != null)
                if (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty())
                    iterate(
                            next(url, oaipmHtype.getListRecords().getResumptionToken().getValue()),
                            fileSystem,
                            path,
                            schemaType,
                            arguments,
                            formatType);
        }
    }

    /**
     * @param url
     * @param resumptionToken
     * @return
     * @throws MalformedURLException
     */
    private URL next(URL url, String resumptionToken) throws MalformedURLException {
        return new URL(String.format("%s?verb=ListRecords&resumptionToken=%s",
                url.toString().replaceAll("\\?verb=.+", ""), resumptionToken));
    }
}
