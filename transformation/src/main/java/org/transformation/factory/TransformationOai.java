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
import org.transformation.util.Garbage;
import org.transformation.util.TimeUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private int buffer = 1000;
    private AtomicInteger atomicInteger = new AtomicInteger();

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
    public void console(SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, XMLStreamException {
        try {
            iterate(null, schemaType, arguments, formatType);

            logger.info(String.format("Completed %s %s", input, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
        } catch (Exception e) {
            logger.error(e);
            logger.error("{}", input);
            throwables.add(e);
        }
    }

    @Override
    public void path(Path out, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, XMLStreamException {
        try {
            iterate(out, schemaType, arguments, formatType);

            logger.info(String.format("Completed %s %s", input, TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
        } catch (Exception e) {
            logger.error(e);
            logger.error("{}", input);
            throwables.add(e);
        }
    }

    @Override
    public void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, XMLStreamException {
        HDFS hdfs = new HDFS(hdfsuri, hdfuser, hdfshome);
        FileSystem fileSystem = hdfs.getFileSystem();

        String uuid = UUID.randomUUID().toString();

        try {
            iterate(fileSystem, path, schemaType, arguments, formatType);

        } catch (JAXBException | SAXException e) {
            logger.error(e);
            logger.error("{}", input);
            throwables.add(e);
        }
    }

    @Override
    public List<Throwable> getExceptions() {
        return throwables.isEmpty() ? null : throwables;
    }

    private void iterate(Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, JAXBException, SAXException, XMLStreamException {
        logger.info("#{} - {}", atomicInteger.get(), input);

        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(input.openStream());
        OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(xmlr, classType).getObject();

        if (Objects.nonNull(oaipmHtype)) {
            deserialize(path, schemaType, arguments, formatType, oaipmHtype);

            if (oaipmHtype.getListRecords().getResumptionToken() != null) {
                while (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty()) {
                    input = new URL(String.format("%s?verb=ListRecords&resumptionToken=%s", input.toString().replaceAll("\\?verb=.+", ""), oaipmHtype.getListRecords().getResumptionToken().getValue()));

                    logger.info("#{} - {}", atomicInteger.get(), input);

                    xmlif = XMLInputFactory.newInstance();
                    xmlr = xmlif.createXMLStreamReader(input.openStream());
                    oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(xmlr, classType).getObject();

                    if (Objects.nonNull(oaipmHtype)) {
                        deserialize(path, schemaType, arguments, formatType, oaipmHtype);
                    }
                }
            }
        }
    }

    /**
     * @param path
     * @param schemaType
     * @param arguments
     * @param formatType
     * @param oaipmHtype
     */
    private void deserialize(Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType, OAIPMHtype oaipmHtype) {
        oaipmHtype.getListRecords().getRecord().stream().parallel().forEach(recordType -> {
            if ((atomicInteger.incrementAndGet() % buffer) == 0) Garbage.gc();

            try {
                if (!Objects.equals(StatusType.DELETED, recordType.getHeader().getStatus())) {
                    if (!Objects.equals(schemaType.getType(), JAXBIntrospector.getValue(recordType.getMetadata().getAny()).getClass()))
                        throw new Exception(String.format("recordType \"%s\" is not a %s valid schema", recordType.getHeader().getIdentifier(), schemaType.getType()));

                    if (Objects.isNull(path)) {
                        ((Download) new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType)).execute(arguments, formatType);
                    } else {
                        ((Download) new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType)).execute(path, arguments, formatType);
                    }
                } else
                    logger.debug("{} - {}", recordType.getHeader().getIdentifier(), recordType.getHeader().getStatus());

            } catch (Exception e) {
                logger.error(e);
                logger.error("{}", recordType.getHeader().getIdentifier());
                throwables.add(e);
            }
        });
    }

    /**
     * @param fileSystem
     * @param path
     * @param schemaType
     * @param arguments
     * @param formatType
     * @throws MalformedURLException
     */
    private void iterate(FileSystem fileSystem, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, JAXBException, SAXException, XMLStreamException {
        logger.info("#{} - {}", atomicInteger.get(), input);

        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(input.openStream());
        OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(xmlr, classType).getObject();

        if (Objects.nonNull(oaipmHtype)) {
            deserialize(fileSystem, path, schemaType, arguments, formatType, oaipmHtype);

            if (oaipmHtype.getListRecords().getResumptionToken() != null)
                while (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty()) {
                    input = new URL(String.format("%s?verb=ListRecords&resumptionToken=%s", input.toString().replaceAll("\\?verb=.+", ""), oaipmHtype.getListRecords().getResumptionToken().getValue()));

                    logger.info("#{} - {}", atomicInteger.get(), input);

                    xmlif = XMLInputFactory.newInstance();
                    xmlr = xmlif.createXMLStreamReader(input.openStream());
                    oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(xmlr, classType).getObject();

                    if (Objects.nonNull(oaipmHtype)) {
                        deserialize(fileSystem, path, schemaType, arguments, formatType, oaipmHtype);
                    }
                }
        }
    }

    /**
     * @param fileSystem
     * @param path
     * @param schemaType
     * @param arguments
     * @param formatType
     * @param oaipmHtype
     */
    private void deserialize(FileSystem fileSystem, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType, OAIPMHtype oaipmHtype) {
        oaipmHtype.getListRecords().getRecord().stream().parallel().forEach(recordType -> {
            if ((atomicInteger.incrementAndGet() % buffer) == 0) Garbage.gc();

            try {
                if (!Objects.equals(StatusType.DELETED, recordType.getHeader().getStatus())) {
                    if (!Objects.equals(schemaType.getType(), JAXBIntrospector.getValue(recordType.getMetadata().getAny()).getClass()))
                        throw new Exception(String.format("recordType \"%s\" is not a %s valid schema", recordType.getHeader().getIdentifier(), schemaType.getType()));

                    ((Download) new Jaxb(recordType, JAXBIntrospector.getValue(recordType.getMetadata().getAny()), schemaType)).execute(fileSystem, path, arguments, formatType);

                } else
                    logger.debug("{} - {}", recordType.getHeader().getIdentifier(), recordType.getHeader().getStatus());

            } catch (Exception e) {
                logger.error(e);
                logger.error("{}", recordType.getHeader().getIdentifier());
                throwables.add(e);
            }
        });
    }
}
