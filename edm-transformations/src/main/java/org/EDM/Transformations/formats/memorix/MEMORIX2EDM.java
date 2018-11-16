package org.EDM.Transformations.formats.memorix;

import eu.europeana.corelib.definitions.jibx.RDF;
import nl.memorix_maior.api.rest._3.Memorix;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.utils.FormatType;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.net.ssl.TrustStoreConfigurationException;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import sun.applet.Main;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author amartinez
 */
public class MEMORIX2EDM implements EDM {

    private static Logger logger = LogManager.getLogger(MEMORIX2EDM.class);

    private Memorix type;
    private String identifier;

    private Map<String, String> properties;

    public MEMORIX2EDM(String identifier, Memorix type, Map<String, String> properties) {
        this.identifier = identifier;
        this.type = type;
        this.properties = properties;
    }

    @Override
    public void transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        InputStream xsl = getClass().getClassLoader().getResourceAsStream("memorix/memorix.xslt");

        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Memorix.class);
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://api.memorix-maior.nl/REST/3.0/ http://api.memorix-maior.nl/REST/3.0/MRX-API-ANY.xsd");
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        m.marshal(type, sw);

        System.out.println(sw.toString());
        new XSLTTransformations(xsl, out, xsltProperties)
                .transformationsFromSource(new StreamSource(new StringReader(sw.toString())));
    }

    @Override
    public void transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Memorix.class);
        Marshaller m = context.createMarshaller();

        m.marshal(type, sw);

        new XSLTTransformations(xslt, out, xsltProperties)
                .transformationsFromSource(new DOMSource(getDocument(type)));
    }

    @Override
    public void transformation(String xslt) throws Exception {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Memorix.class);
        Marshaller m = context.createMarshaller();

        m.marshal(type, sw);

        new XSLTTransformations(xslt, IoBuilder.forLogger(MEMORIX2EDM.class).setLevel(Level.INFO).buildOutputStream(), properties)
                .transformationsFromSource(new DOMSource(getDocument(type)));
    }

    @Override
    public void creation() {
        throw new IllegalArgumentException("creation is not valid for MEMORIX2EDM!");
    }

    @Override
    public void creation(FormatType formatType) throws IllegalArgumentException {
        throw new IllegalArgumentException("creation is not valid for MEMORIX2EDM!");
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs) {
        throw new IllegalArgumentException("creation is not valid for MEMORIX2EDM!");
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs, FormatType formatType) throws Exception {
        throw new IllegalArgumentException("creation is not valid for MEMORIX2EDM!");
    }

    @Override
    public void creation(Charset encoding, boolean alone, Writer writer) {
        throw new IllegalArgumentException("creation is not valid for MEMORIX2EDM!");
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, String name, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, name, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, Class<?> classType) {
        return new JibxUnMarshall(rdr, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, String name, Class<?> classType) {
        return new JibxUnMarshall(rdr, name, classType);
    }

    @Override
    public void modify(RDF rdf) {

    }

    public static Document getDocument(Object jaxb) throws JAXBException, ParserConfigurationException {
        DOMResult res = new DOMResult();
        JAXBContext context = JAXBContext.newInstance(Memorix.class);
        context.createMarshaller().marshal(jaxb, res);
        Document doc = (Document) res.getNode();
        System.out.println(doc.toString());
        return doc;
    }
}
