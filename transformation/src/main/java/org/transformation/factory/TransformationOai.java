package org.transformation.factory;

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
    public void console(SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException {
        iterate(input,null,schemaType, arguments, formatType);

        logger.info(String.format("Completed %s %s", input, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
    }

    @Override
    public void path(Path out, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException {
        iterate(input,out,schemaType, arguments, formatType);

        logger.info(String.format("Completed %s %s", input, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
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

    private void iterate(URL url,  Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws MalformedURLException, URISyntaxException {
        OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(url, classType).getObject();

        if (Objects.nonNull(oaipmHtype)) {
            oaipmHtype.getListRecords().getRecord().stream().parallel().forEach(recordType -> {
                try {
                    if (!Objects.equals(StatusType.DELETED, recordType.getHeader().getStatus())) {
                        if (!Objects.equals(schemaType.getType(), JAXBIntrospector.getValue(recordType.getMetadata().getAny()).getClass()))
                            throw new Exception(String.format("recordType \"%s\" is not a %s valid schema", recordType.getHeader().getIdentifier(), schemaType.getType()));

                        if(Objects.isNull(path))
                            intenseCalculation(
                                    new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType),
                                    arguments,
                                    formatType
                            );
                        else
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
