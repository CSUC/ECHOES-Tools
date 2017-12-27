package org.EDM.Transformations.formats.memorix;

import eu.europeana.corelib.definitions.jibx.RDF;
import nl.memorix_maior.api.rest._3.Memorix;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
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
    public XSLTTransformations transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        String xsl = getClass().getClassLoader().getResource("memorix/memorix.xslt").toExternalForm();
        return new XSLTTransformations(xsl, out, xsltProperties);
    }

    @Override
    public XSLTTransformations transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        return new XSLTTransformations(xslt, out, xsltProperties);
    }

    @Override
    public XSLTTransformations transformation(String xslt) throws Exception {
        return new XSLTTransformations(xslt, IoBuilder.forLogger(MEMORIX2EDM.class).setLevel(Level.INFO).buildOutputStream(), properties);
    }

    @Override
    public void creation() {
        throw new IllegalArgumentException("creation is not valid for MEMORIX2EDM!");
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs) {
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
}
