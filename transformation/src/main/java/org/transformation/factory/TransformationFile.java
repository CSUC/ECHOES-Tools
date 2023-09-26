package org.transformation.factory;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.core.HDFS;
import org.csuc.custom.multirecords.Metadata;
import org.csuc.util.FormatType;
import org.edm.transformations.formats.a2a.A2A2EDM;
import org.edm.transformations.formats.dc.DC2EDM;
import org.edm.transformations.formats.ead.EAD2EDM;
import org.edm.transformations.formats.memorix.MEMORIX2EDM;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.transformation.util.Schema.addSchema;

public class TransformationFile implements Transformation {

    private Logger logger = LogManager.getLogger(getClass());

    private Class<?>[] classType;
    private Path input;

    private List<Throwable> throwables = new ArrayList<>();

    public TransformationFile(Path input, Class<?>[] classType) {
        this.classType = classType;
        this.input = input;
    }

    @Override
    public void console(Map<String, String> arguments, FormatType formatType) throws IOException {
        Files.walk(input)
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
                        inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                        XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new FileInputStream(f.toFile()));

                        Schema schema = addSchema(classType);
                        Validator validator = schema.newValidator();
                        validator.setErrorHandler(new ErrorHandler() {
                            @Override
                            public void warning(SAXParseException e) throws SAXException {
                            }

                            @Override
                            public void error(SAXParseException e) throws SAXException {
                                logger.error("[{}] - {}", f.getFileName(), e);
                                throwables.add(e);
                            }

                            @Override
                            public void fatalError(SAXParseException e) throws SAXException {
                            }
                        });
                        validator.validate(new StAXSource(xmlStreamReader));
                        xmlStreamReader = inputFactory.createXMLStreamReader(new FileInputStream(f.toFile()));

                        final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

                        while ((xmlStreamReader.next()) != XMLStreamConstants.END_DOCUMENT) {
                            if (xmlStreamReader.isStartElement()) {
                                final String eventType = xmlStreamReader.getLocalName();
                                final String uuid = UUID.randomUUID().toString();
                                switch (eventType) {
                                    case "A2A":
                                        A2AType a2AType = unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue();
                                        new A2A2EDM(uuid, a2AType, arguments)
                                                .creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), formatType);
                                        logger.info("{}", a2AType);
                                        break;
                                    case "dc":
                                        OaiDcType oaiDcType = unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue();
                                        new DC2EDM(uuid, oaiDcType, arguments)
                                                .creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), formatType);
                                        logger.info("{}", oaiDcType);
                                        break;
                                    case "ead":
                                        Ead eadType = unmarshaller.unmarshal(xmlStreamReader, Ead.class).getValue();
                                        new EAD2EDM(uuid, eadType, arguments)
                                                .transformation(IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), arguments);
                                        logger.info("{}", eadType);
                                        break;
                                    case "memorix":
                                        Memorix memorixType = unmarshaller.unmarshal(xmlStreamReader, Memorix.class).getValue();
                                        new MEMORIX2EDM(uuid, memorixType, arguments)
                                            .transformation(IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), arguments);
                                        logger.info("{}", memorixType);
                                        break;
                                    default:
                                        //System.out.println("Unknown");
                                        break;
                                }
                            }
                        }
                        xmlStreamReader.close();
                    } catch (Exception e) {
                        //throwables.add(e);
                    }
                });
    }

    @Override
    public void path(Path out, Map<String, String> arguments, FormatType formatType) throws IOException {
        Files.walk(input)
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
                        inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                        XMLStreamReader xmlStreamReaderSchema = inputFactory.createXMLStreamReader(new FileInputStream(f.toFile()));

                        Schema schema = addSchema(classType);
                        Validator validator = schema.newValidator();
                        validator.setErrorHandler(new ErrorHandler() {
                            @Override
                            public void warning(SAXParseException e) throws SAXException {
                            }

                            @Override
                            public void error(SAXParseException e) throws SAXException {
                                logger.error("[{}] - {}", f.getFileName(), e);
                                throwables.add(e);
                            }

                            @Override
                            public void fatalError(SAXParseException e) throws SAXException {
                            }
                        });
                        validator.validate(new StAXSource(xmlStreamReaderSchema));

                        xmlStreamReaderSchema.close();
                        XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new FileInputStream(f.toFile()));

                        final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

                        while ((xmlStreamReader.next()) != XMLStreamConstants.END_DOCUMENT) {
                            if (xmlStreamReader.isStartElement()) {
                                final String eventType = xmlStreamReader.getLocalName();
                                String uuid = UUID.randomUUID().toString();
                                switch (eventType) {
                                    case "A2A":
                                        A2AType a2AType = unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue();
                                        new A2A2EDM(uuid, a2AType, arguments)
                                                .creation(StandardCharsets.UTF_8, true,
                                                        Files.newOutputStream(new File(String.format("%s/%s.%s", out, uuid, formatType.extensions().stream().findFirst().get())).toPath()), formatType);
                                        logger.info("{}", a2AType);
                                        break;
                                    case "dc":
                                        OaiDcType oaiDcType = unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue();
                                        new DC2EDM(uuid, oaiDcType, arguments)
                                                .creation(StandardCharsets.UTF_8, true,
                                                        Files.newOutputStream(new File(String.format("%s/%s.%s", out, uuid, formatType.extensions().stream().findFirst().get())).toPath()), formatType);
                                        logger.info("{}", oaiDcType);
                                        break;
                                    case "ead":
                                        Ead eadType = unmarshaller.unmarshal(xmlStreamReader, Ead.class).getValue();
                                        new EAD2EDM(uuid, eadType, arguments)
                                                .transformation(Files.newOutputStream(new File(String.format("%s/%s.%s", out, uuid, formatType.extensions().stream().findFirst().get())).toPath()), arguments);
                                        logger.info("{}", eadType);
                                        break;
                                    case "memorix":
                                        Memorix memorixType = unmarshaller.unmarshal(xmlStreamReader, Memorix.class).getValue();
                                        new MEMORIX2EDM(uuid, memorixType, arguments)
                                                .transformation(Files.newOutputStream(new File(String.format("%s/%s.%s", out, uuid, formatType.extensions().stream().findFirst().get())).toPath()), arguments);
                                        logger.info("{}", memorixType);
                                        break;
                                    default:
                                        //System.out.println("Unknown");
                                        break;
                                }
                            }
                        }
                        xmlStreamReader.close();
                    } catch (Exception e) {
                        //throwables.add(e);
                    }
                });
    }

    @Override
    public void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, Map<String, String> arguments, FormatType formatType) throws IOException {
        HDFS hdfs = new HDFS(hdfsuri, hdfuser, hdfshome);

        Files.walk(input)
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
                        inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                        XMLStreamReader xmlStreamReaderSchema = inputFactory.createXMLStreamReader(new FileInputStream(f.toFile()));

                        Schema schema = addSchema(classType);
                        Validator validator = schema.newValidator();
                        validator.setErrorHandler(new ErrorHandler() {
                            @Override
                            public void warning(SAXParseException e) throws SAXException {
                            }

                            @Override
                            public void error(SAXParseException e) throws SAXException {
                                logger.error("[{}] - {}", f.getFileName(), e);
                                throwables.add(e);
                            }

                            @Override
                            public void fatalError(SAXParseException e) throws SAXException {
                            }
                        });
                        validator.validate(new StAXSource(xmlStreamReaderSchema));

                        xmlStreamReaderSchema.close();
                        XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new FileInputStream(f.toFile()));

                        final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

                        while ((xmlStreamReader.next()) != XMLStreamConstants.END_DOCUMENT) {
                            if (xmlStreamReader.isStartElement()) {
                                final String eventType = xmlStreamReader.getLocalName();
                                String uuid = UUID.randomUUID().toString();

                                String filename = String.format("%s.%s", uuid, formatType.extensions().stream().findFirst().get());
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                                switch (eventType) {
                                    case "A2A":
                                        A2AType a2AType = unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue();
                                        new A2A2EDM(uuid, a2AType, arguments).creation(StandardCharsets.UTF_8, true,byteArrayOutputStream, formatType);
                                        logger.info("{}", a2AType);
                                        break;
                                    case "dc":
                                        OaiDcType oaiDcType = unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue();
                                        new DC2EDM(uuid, oaiDcType, arguments).creation(StandardCharsets.UTF_8, true, byteArrayOutputStream, formatType);
                                        logger.info("{}", oaiDcType);
                                        break;
                                    case "ead":
                                        Ead eadType = unmarshaller.unmarshal(xmlStreamReader, Ead.class).getValue();
                                        new EAD2EDM(uuid, eadType, arguments).transformation(byteArrayOutputStream, arguments);
                                        logger.info("{}", eadType);
                                        break;
                                    case "memorix":
                                        Memorix memorixType = unmarshaller.unmarshal(xmlStreamReader, Memorix.class).getValue();
                                        new MEMORIX2EDM(uuid, memorixType, arguments).transformation(byteArrayOutputStream, arguments);
                                        logger.info("{}", memorixType);
                                        break;
                                    default:
                                        //System.out.println("Unknown");
                                        break;
                                }
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                InputStream inputStream = new ByteArrayInputStream(bytes);
                                HDFS.write(hdfs.getFileSystem(), new org.apache.hadoop.fs.Path(path, filename), inputStream, true);
                            }
                        }
                        xmlStreamReader.close();
                    } catch (Exception e) {
                        //throwables.add(e);
                    }
                });
    }

    @Override
    public List<Throwable> getExceptions() {
        return throwables.isEmpty() ? null : throwables;
    }
}
