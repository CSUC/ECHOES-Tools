package org.edm.transformations.formats.custom;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.commons.io.FilenameUtils;
import org.csuc.custom.multirecords.Metadata;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.serialize.JaxbMarshal;
import org.edm.transformations.formats.EDM;
import org.edm.transformations.formats.a2a.A2A2EDM;
import org.edm.transformations.formats.dc.DC2EDM;
import org.junit.Test;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CUSTOM2EDMTest {

    @Test
    public void test() throws Exception {
        //File xml = new File(getClass().getClassLoader().getResource("custom/custom.xml").getFile());
        //JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[]{Metadata.class});

        Path tmp = Files.createTempDirectory("custom");
        JaxbUnmarshal jxb = new JaxbUnmarshal(new File("/home/amartinez/Baixades/custom2.xml"), new Class[]{Metadata.class});
        System.out.println(jxb.isValidating());
        if (jxb.isValidating()) {
            Metadata metadata = (Metadata) jxb.getObject();
            if (Objects.nonNull(metadata)) {
                EDM custom2EDM = new CUSTOM2EDM(tmp.toString(), metadata, properties());
                if (Objects.nonNull(custom2EDM)) {
                    custom2EDM.creation(StandardCharsets.UTF_8, true, (OutputStream) null);
                }
            }
        } else {
            if (!jxb.getValidationEvent().getEventError().isEmpty()) {
                jxb.getValidationEvent().getEventError().forEach(System.out::println);
            }
        }
    }

    @Test
    public void a2a() throws JAXBException, FileNotFoundException {
        Stream.of(
                "/home/amartinez/Baixades/ECHOES-725/frl_a2a_bs_g-201901.xml",
                "/home/amartinez/Baixades/ECHOES-725/frl_a2a_be_a-202305.xml",
                "/home/amartinez/Baixades/ECHOES-725/frl_a2a_t01_a-202302.xml"
        ).map(m -> Paths.get(m)).forEach(path -> {
            try {
                XMLInputFactory xif = XMLInputFactory.newFactory();
                FileInputStream xml = new FileInputStream(path.toString());

                JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[]{OAIPMHtype.class, A2AType.class});

                Metadata result = new Metadata();
                Metadata.Records records = new Metadata.Records();

                if (jxb.isValidating() && jxb.getValidationEvent().getEventError().isEmpty()) {
                    OAIPMHtype oaipmHtype = (OAIPMHtype) jxb.getObject();
                    oaipmHtype.getListRecords().getRecord().stream()
                            .forEach(record -> records.getRecord().add(((JAXBElement<Metadata.Records.Record>) record.getMetadata().getAny()).getValue()));
                }
                if (!records.getRecord().isEmpty()) {
                    result.setRecords(records);
                    JaxbMarshal jaxbMarshal = new JaxbMarshal(result, new Class[]{Metadata.class, A2AType.class});
                    jaxbMarshal.marshaller(new FileOutputStream(String.format("/tmp/custom_%s", path.getFileName())));
                } else {
                    jxb.getValidationEvent().getEventError().forEach(System.out::println);
                }
            } catch (Exception e) {
                System.err.println(String.format("%s - %s", path.getFileName(), e.getMessage()));

            }
        });
    }

    /**
     * XMLStreamReader
     *
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void stax() throws JAXBException, XMLStreamException, IOException, SAXException {
        Stream.of(
                //VALID
                "/home/amartinez/Baixades/ECHOES-725/frl_a2a_bs_g-201901.xml"
//                "/home/amartinez/Baixades/ECHOES-725/frl_a2a_be_a-202305.xml",
//                "/home/amartinez/Baixades/ECHOES-725/frl_a2a_t01_a-202302.xml"
                //ERROR
                //"/home/amartinez/Baixades/ECHOES-725/error/frl_a2a_dtb_b-202302.xml",
                //"/home/amartinez/Baixades/ECHOES-725/error/frl_a2a_mvs_a-202302.xml",
                //"/home/amartinez/Baixades/ECHOES-725/error/frl_a2a_t05_a-202302.xml",
                //"/home/amartinez/Baixades/ECHOES-725/error/frl_a2a_t07_a-202302.xml",
                //"/home/amartinez/Baixades/ECHOES-725/error/frl_a2a_t15_a-201901.xml",
                //"/home/amartinez/Baixades/ECHOES-725/error/frl_a2a_t16_a-202302.xml",
                //EAD
               // "/home/amartinez/Baixades/ECHOES-725/39933EAD.xml"
                //MEMORIX
//                 "/home/amartinez/projectes/ECHOES/ECHOES-Tools/edm-transformations/src/test/resources/a2a/a2a.xml"
//                "/home/amartinez/projectes/ECHOES/ECHOES-Tools/edm-transformations/src/test/resources/memorix/memorix.xml"
//                "/home/amartinez/Baixades/02gfc7t72.xml"
        ).map(m -> Paths.get(m)).forEach(path -> {
            if (Files.notExists(Paths.get("/home/amartinez/Baixades/ECHOES-725/" + FilenameUtils.removeExtension(path.getFileName().toString())))) {
                new File("/home/amartinez/Baixades/ECHOES-725/" + FilenameUtils.removeExtension(path.getFileName().toString())).mkdirs();
            }
            AtomicInteger total = new AtomicInteger(0);
            try {
                final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
                inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new FileInputStream(path.toFile()));

                Schema schema = addSchema(new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class});
                Validator validator = schema.newValidator();

                validator.setErrorHandler(new ErrorHandler() {
                    @Override
                    public void warning(SAXParseException e) throws SAXException {
                    }

                    @Override
                    public void error(SAXParseException e) throws SAXException {
                        System.out.println(e);
                    }

                    @Override
                    public void fatalError(SAXParseException e) throws SAXException {
                    }
                });
                validator.validate(new StAXSource(xmlStreamReader));
                xmlStreamReader = inputFactory.createXMLStreamReader(new FileInputStream(path.toFile()));

                final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class, Metadata.class).createUnmarshaller();

                while (xmlStreamReader.hasNext()) {
                    xmlStreamReader.next();
                    if (xmlStreamReader.isStartElement()) {
                        final String eventType = xmlStreamReader.getLocalName();
                        switch (eventType) {
                            case "A2A":
                                total.getAndIncrement();
                                A2AType a2AType = unmarshaller.unmarshal(xmlStreamReader, A2AType.class).getValue();
                                EDM a2a = new A2A2EDM(UUID.randomUUID().toString(), a2AType, properties());
                                if (Objects.nonNull(a2a)) {
                                    System.out.println(" After Unmarhsalling : " + a2AType);
                                    a2a.creation(StandardCharsets.UTF_8, true, new FileOutputStream(String.format("/home/amartinez/Baixades/ECHOES-725/%s/%s.rdf", FilenameUtils.removeExtension(path.getFileName().toString()), UUID.randomUUID())));
                                }
                                break;
                            case "dc":
                                total.getAndIncrement();
                                OaiDcType oaiDcType = unmarshaller.unmarshal(xmlStreamReader, OaiDcType.class).getValue();
                                EDM dc = new DC2EDM(UUID.randomUUID().toString(), oaiDcType, properties());
                                if (Objects.nonNull(dc)) {
                                    System.out.println(" After Unmarhsalling : " + oaiDcType);
                                    dc.creation(StandardCharsets.UTF_8, true, new FileOutputStream(String.format("/tmp/%s.rdf", UUID.randomUUID())));
                                }
                                break;
                            default:
                                //System.out.println("Unknown " + eventType);
                                break;

                        }
                        //if (event != null) System.out.println(" After Unmarhsalling : " + event);
                    }
                }
                xmlStreamReader.close();
            } catch (Exception e) {
                System.err.println(e);
            }
            System.out.println(String.format("%s : %s count", path, total.get()));
        });
    }

    @Test
    public void asd() throws XMLStreamException, JAXBException {
        try {
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new URL("https://cdm21056.contentdm.oclc.org/oai/oai.php?verb=ListRecords&metadataPrefix=oai_dc&set=TRLffa").openStream());



            final Unmarshaller unmarshaller = JAXBContext.newInstance(OAIPMHtype.class, A2AType.class, OaiDcType.class, Ead.class, Memorix.class).createUnmarshaller();

            while (xmlStreamReader.hasNext()) {
                xmlStreamReader.next();
                if (xmlStreamReader.isStartElement()) {
                    final String eventType = xmlStreamReader.getLocalName();
                   // System.out.println(eventType);
                    switch (eventType) {
                        case "resumptionToken":
                            System.out.println(xmlStreamReader.getElementText());
                            break;
                        case "header":
                            xmlStreamReader.next();
                            if(xmlStreamReader.getName().getLocalPart().equals("identifier"))
                                System.out.println(xmlStreamReader.getElementText());

                        default:
                            break;
                    }
                }
            }
            xmlStreamReader.close();
        }catch (Exception e){
            System.err.println(e);
        }

    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("edmType", "IMAGE");
        properties.put("provider", "provider");
        properties.put("dataProvider", "dataProvider");
        properties.put("language", "language");
        properties.put("rights", "rights");

        return properties;
    }

    private Schema addSchema(Class[] classType) throws SAXException {
        List<Source> list = new ArrayList<>();

        Stream.of(classType).forEach(type -> {
            if (Objects.equals(type, A2AType.class))
                list.add(new StreamSource(A2AType.class.getClassLoader().getResource("A2AAllInOne_v.1.7.xsd").toExternalForm()));
            if (Objects.equals(type, OaiDcType.class)) {
                list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("oai_dc.xsd").toExternalForm()));
                list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("simpledc20021212.xsd").toExternalForm()));
                list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("xml.xsd").toExternalForm()));
            }
            if (Objects.equals(type, Ead.class))
                list.add(new StreamSource(Ead.class.getClassLoader().getResource("apeEAD.xsd").toExternalForm()));
            if (Objects.equals(type, Memorix.class))
                list.add(new StreamSource(Memorix.class.getClassLoader().getResource("MRX-API-ANY.xsd").toExternalForm()));
            if (Objects.equals(type, OAIPMHtype.class))
                list.add(new StreamSource(OAIPMHtype.class.getClassLoader().getResource("OAI-PMH.xsd").toExternalForm()));
            if (Objects.equals(type, Metadata.class))
                list.add(new StreamSource(Metadata.class.getClassLoader().getResource("multirecord.xsd").toExternalForm()));
        });

        Source[] schemaFiles = list.toArray(new Source[list.size()]);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return sf.newSchema(schemaFiles);
    }

    @Test
    public void walk() throws IOException {
        new ForkJoinPool().submit(() -> {
            try {
                Files.walk(Paths.get("/home/amartinez/Baixades/ECHOES-725/tresoar/a2a"))
                        .parallel()
                        .filter(Files::isRegularFile)
                        .forEach(f -> {
                            System.out.println(String.format("[%s] - %s", Thread.currentThread().getName(), f));
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).join();

    }
}