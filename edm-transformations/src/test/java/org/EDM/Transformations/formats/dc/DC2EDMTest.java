package org.EDM.Transformations.formats.dc;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.FactoryEDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.deserialize.JibxUnMarshall;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class DC2EDMTest {

    private File xml;
    private File tmp;

    private EDM dc;

    @Before
    public void setUp() throws Exception {
        xml = new File(getClass().getClassLoader().getResource("dc/dc.xml").getFile());
        tmp = Files.createTempFile("dc_edm", ".xml").toFile();
        assertTrue(xml.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[] { OaiDcType.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        dc = FactoryEDM.createFactory(new DC2EDM(UUID.randomUUID().toString(), (OaiDcType) jxb.getObject(), properties()));
        assertNotNull(dc);

        tmp.deleteOnExit();
    }

    /**
     *
     * {@link DC2EDM#transformation(String)}.
     *
     * @throws Exception
     */
    @Test
    public void transformation() throws Exception {
        XSLTTransformations transformations = null;
        try{
            transformations = dc.transformation(null);
            assertNull(transformations);
        }catch(Exception e){}
    }

    /**
     *
     * {@link DC2EDM#transformation(String, OutputStream, Map)}.
     *
     * @throws Exception
     */
    @Test
    public void transformation1() throws Exception {
        XSLTTransformations transformations = null;
        try{
            transformations = dc.transformation(null, null, null);
            assertNull(transformations);
        }catch(Exception e){}
    }

    /**
     *
     * {@link DC2EDM#creation()}.
     *
     * @throws Exception
     */
    @Test
    public void creation() throws Exception {
        dc.creation();
    }

    /**
     * {@link DC2EDM#creation(Charset, boolean, Writer)}.
     *
     * @throws Exception
     */
    @Test
    public void creation1() throws Exception {
        StringWriter writer = new StringWriter();
        dc.creation(UTF_8, true, writer);
        assertTrue(!writer.toString().isEmpty());
    }

    /**
     * {@link DC2EDM#creation(Charset, boolean, OutputStream)}.
     *
     * @throws Exception
     */
    @Test
    public void creation2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        dc.creation(UTF_8, true, outs);

        int b  = new FileInputStream(tmp).read();
        assertNotEquals(-1, b);
    }

    /**
     * {@link DC2EDM#validateSchema(Reader, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema() throws Exception {
        StringWriter writer = new StringWriter();
        dc.creation(UTF_8, true, writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = dc.validateSchema(reader, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link DC2EDM#validateSchema(Reader, String, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema1() throws Exception {
        StringWriter writer = new StringWriter();
        dc.creation(UTF_8, true, writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = dc.validateSchema(reader, "name", RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link DC2EDM#validateSchema(InputStream, Charset, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        dc.creation(UTF_8, true, outs);

        JibxUnMarshall jibx = dc.validateSchema(new FileInputStream(tmp), UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link DC2EDM#validateSchema(InputStream, String, Charset, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema3() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        dc.creation(UTF_8, true, outs);

        JibxUnMarshall jibx = dc.validateSchema(new FileInputStream(tmp), "name", UTF_8, RDF.class);

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