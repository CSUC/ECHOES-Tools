package org.transformation.factory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.core.HDFS;
import org.csuc.custom.multirecords.Metadata;
import org.csuc.util.FormatType;
import org.edm.transformations.formats.utils.SchemaType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.download.Download;
import org.transformation.download.Jaxb;
import org.transformation.util.Garbage;
import org.transformation.util.TimeUtils;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.transformation.util.Schema.addSchema;

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
    public void console(Map<String, String> arguments, FormatType formatType) throws IOException {
        AtomicInteger batch = new AtomicInteger(0);

        Observable<Download> observable = Observable.create(emitter -> {
            iterate(input, emitter);

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
    public void path(Path out, Map<String, String> arguments, FormatType formatType) throws IOException {
        AtomicInteger batch = new AtomicInteger(0);

        Observable<Download> observable = Observable.create(emitter -> {
            iterate(input, emitter);

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
    public void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException {
        HDFS hdfs = new HDFS(hdfsuri, hdfuser, hdfshome);
        FileSystem fileSystem = hdfs.getFileSystem();

        String uuid = UUID.randomUUID().toString();

        iterate(input, fileSystem, path, arguments, formatType);
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
     * @throws MalformedURLException
     */
    private void iterate(URL url, ObservableEmitter<Download> emitter) throws MalformedURLException {
        try {
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(url.openStream());

            Schema schema = addSchema(classType);
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) throws SAXException {
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    logger.error("[{}] - {}", url, e);
                    throwables.add(e);
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                }
            });
            validator.validate(new StAXSource(xmlStreamReader));
            xmlStreamReader = inputFactory.createXMLStreamReader(url.openStream());

            final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

            String resumptionToken = null;

            while ((xmlStreamReader.next()) != XMLStreamConstants.END_DOCUMENT) {
                if (xmlStreamReader.isStartElement()) {
                    final String eventType = xmlStreamReader.getLocalName();
                    String uuid = UUID.randomUUID().toString();
                    switch (eventType) {
                        case "A2A":
                            emitter.onNext(new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue()));
                            break;
                        case "dc":
                            emitter.onNext(new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue()));
                            break;
                        case "ead":
                            emitter.onNext(new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, Ead.class).getValue()));
                            break;
                        case "memorix":
                            emitter.onNext(new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, Memorix.class).getValue()));
                            break;
                        case "resumptionToken":
                            resumptionToken = xmlStreamReader.getElementText();
                            break;
                        default:
                            //System.out.println("Unknown");
                            break;
                    }
                }
            }
            xmlStreamReader.close();
            if(Objects.nonNull(resumptionToken)){
                iterate(next(url, resumptionToken), emitter);
            }
        } catch (Exception e) {
            //throwables.add(e);
        }
    }

    private void iterate(URL url,  Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws MalformedURLException, URISyntaxException {
        try {
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(url.openStream());

            Schema schema = addSchema(classType);
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) throws SAXException {
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    logger.error("[{}] - {}", url, e);
                    throwables.add(e);
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                }
            });
            validator.validate(new StAXSource(xmlStreamReader));
            xmlStreamReader = inputFactory.createXMLStreamReader(url.openStream());

            final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

            String resumptionToken = null;

            while ((xmlStreamReader.next()) != XMLStreamConstants.END_DOCUMENT) {
                if (xmlStreamReader.isStartElement()) {
                    final String eventType = xmlStreamReader.getLocalName();
                    String uuid = UUID.randomUUID().toString();

                    switch (eventType) {
                        case "A2A":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue()),
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "dc":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue()),
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "ead":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, Ead.class).getValue()),
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "memorix":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, Memorix.class).getValue()),
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "resumptionToken":
                            resumptionToken = xmlStreamReader.getElementText();
                            break;
                        default:
                            //System.out.println("Unknown");
                            break;
                    }
                }
            }
            xmlStreamReader.close();
            if(Objects.nonNull(resumptionToken)){
                iterate(
                        next(url, resumptionToken),
                        path,
                        schemaType,
                        arguments,
                        formatType);
            }
        } catch (Exception e) {
            //throwables.add(e);
        }
    }

    /**
     * @param url
     * @param fileSystem
     * @param path
     * @param arguments
     * @param formatType
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    private void iterate(URL url, FileSystem fileSystem, org.apache.hadoop.fs.Path path, Map<String, String> arguments, FormatType formatType) throws MalformedURLException, URISyntaxException {
        try {
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(url.openStream());

            Schema schema = addSchema(classType);
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) throws SAXException {
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    logger.error("[{}] - {}", url, e);
                    throwables.add(e);
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                }
            });
            validator.validate(new StAXSource(xmlStreamReader));
            xmlStreamReader = inputFactory.createXMLStreamReader(url.openStream());

            final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

            String resumptionToken = null;

            while ((xmlStreamReader.next()) != XMLStreamConstants.END_DOCUMENT) {
                if (xmlStreamReader.isStartElement()) {
                    final String eventType = xmlStreamReader.getLocalName();
                    String uuid = UUID.randomUUID().toString();

                    switch (eventType) {
                        case "A2A":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue()),
                                    fileSystem,
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "dc":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue()),
                                    fileSystem,
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "ead":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, Ead.class).getValue()),
                                    fileSystem,
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "memorix":
                            intenseCalculation(
                                    new Jaxb(uuid, unmarshaller.unmarshal(xmlStreamReader, Memorix.class).getValue()),
                                    fileSystem,
                                    path,
                                    arguments,
                                    formatType
                            );
                            break;
                        case "resumptionToken":
                            resumptionToken = xmlStreamReader.getElementText();
                            break;
                        default:
                            //System.out.println("Unknown");
                            break;
                    }
                }
            }
            xmlStreamReader.close();
            if(Objects.nonNull(resumptionToken)){
                iterate(
                        next(url, resumptionToken),
                        fileSystem,
                        path,
                        arguments,
                        formatType);
            }
        } catch (Exception e) {
            //throwables.add(e);
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
