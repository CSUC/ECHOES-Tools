package org.EDM.Transformations.formats.a2a;

import eu.europeana.corelib.definitions.jibx.RDF;
import nl.mindbus.a2a.A2AType;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.FactoryEDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.deserialize.JibxUnMarshall;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.Assert.*;

public class A2A2EDMTest {

    private File xml;
    private File tmp;

    private EDM a2a;

    @Before
    public void setUp() throws Exception {
        xml = new File(getClass().getClassLoader().getResource("a2a/a2a.xml").getFile());
        tmp = Files.createTempFile("a2a_edm", ".xml").toFile();

        assertTrue(xml.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[] { A2AType.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        a2a = FactoryEDM.createFactory(new A2A2EDM(UUID.randomUUID().toString(), (A2AType) jxb.getObject(), properties()));
        assertNotNull(a2a);

        tmp.deleteOnExit();
    }


    @Test
    public void transformation() throws Exception {
        XSLTTransformations transformations = null;
        try{
            transformations = a2a.transformation(null);
            assertNull(transformations);
        }catch(Exception e){}
    }


    @Test
    public void transformation1() throws Exception {
        XSLTTransformations transformations = null;
        try{
            transformations = a2a.transformation(null, null, null);
            assertNull(transformations);
        }catch(Exception e){}
    }


    @Test
    public void creation() throws Exception {
        a2a.creation();
    }


    @Test
    public void creation1() throws Exception {
        StringWriter writer = new StringWriter();
        a2a.creation(UTF_8, true, writer);
        assertTrue(!writer.toString().isEmpty());
    }


    @Test
    public void creation2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        a2a.creation(UTF_8, true, outs);

        int b  = new FileInputStream(tmp).read();
        assertNotEquals(-1, b);
    }


    @Test
    public void validateSchema() throws Exception {
        StringWriter writer = new StringWriter();
        a2a.creation(UTF_8, true, writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = a2a.validateSchema(reader, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }


    @Test
    public void validateSchema1() throws Exception {
        StringWriter writer = new StringWriter();
        a2a.creation(UTF_8, true, writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = a2a.validateSchema(reader, "name", RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }


    @Test
    public void validateSchema2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        a2a.creation(UTF_8, true, outs);

        JibxUnMarshall jibx = a2a.validateSchema(new FileInputStream(tmp), UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }


    @Test
    public void validateSchema3() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        a2a.creation(UTF_8, true, outs);

        JibxUnMarshall jibx = a2a.validateSchema(new FileInputStream(tmp), "name", UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    @Test
    public void modify() throws Exception {

    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("edmType", "IMAGE");
        properties.put("provider", "provider");
        properties.put("dataProvider", "dataProvider");
        properties.put("language", "language");
        properties.put("rights", "rights");
        properties.put("set", "set");

        return properties;
    }
}