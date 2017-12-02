package org.EDM.Transformations.formats.memorix;

import eu.europeana.corelib.definitions.jibx.RDF;
import nl.memorix_maior.api.rest._3.Memorix;
import org.EDM.Transformations.deserialize.JaxbUnmarshal;
import org.EDM.Transformations.deserialize.JibxUnMarshall;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.FactoryEDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class MEMORIX2EDMTest {

    private static Logger logger = LogManager.getLogger(MEMORIX2EDMTest.class);

    private File xml;
    private File xslt;
    private File tmp;

    private EDM memorix;

    @Before
    public void setUp() throws Exception {
        xml = new File(getClass().getClassLoader().getResource("memorix/memorix.xml").getFile());
        xslt = new File(getClass().getClassLoader().getResource("memorix/memorix.xslt").getFile());
        tmp = Files.createTempFile("memorix_edm", ".xml").toFile();

        assertTrue(xml.exists());
        assertTrue(xslt.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[] { Memorix.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        memorix = FactoryEDM.createFactory(new MEMORIX2EDM(UUID.randomUUID().toString(), (Memorix) jxb.getObject(), properties()));
        assertNotNull(memorix);
    }

    /**
     *
     * {@link MEMORIX2EDM#transformation(String)}.
     *
     * @throws Exception
     */
    @Test
    public void transformation() throws Exception {
        XSLTTransformations transformations = memorix.transformation(xslt.getPath());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));
    }

    /**
     *
     * {@link MEMORIX2EDM#transformation(String, OutputStream, Map)}.
     *
     * @throws Exception
     */
    @Test
    public void transformation1() throws Exception {
        XSLTTransformations transformations = memorix.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));
    }

    /**
     *
     * {@link MEMORIX2EDM#creation()}.
     *
     * @throws Exception
     */
    @Test
    public void creation() throws Exception {
        try{
            memorix.creation();
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * {@link MEMORIX2EDM#creation(Charset, boolean, Writer)}.
     *
     * @throws Exception
     */
    @Test
    public void creation1() throws Exception {
        StringWriter writer = new StringWriter();
        try{
            memorix.creation(UTF_8, true, writer);
            assertTrue(writer.toString().isEmpty());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * {@link MEMORIX2EDM#creation(Charset, boolean, OutputStream)}.
     *
     * @throws Exception
     */
    @Test
    public void creation2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);

        try{
            memorix.creation(UTF_8, true, outs);
            int b  = new FileInputStream(tmp).read();
            assertEquals(-1, b);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * {@link MEMORIX2EDM#validateSchema(Reader, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema() throws Exception {
        XSLTTransformations transformations = memorix.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = memorix.validateSchema(new FileReader(tmp), RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link MEMORIX2EDM#validateSchema(Reader, String, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema1() throws Exception {
        XSLTTransformations transformations = memorix.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = memorix.validateSchema(new FileReader(tmp), "name", RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link MEMORIX2EDM#validateSchema(InputStream, Charset, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema2() throws Exception {
        XSLTTransformations transformations = memorix.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = memorix.validateSchema(new FileInputStream(tmp), UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link MEMORIX2EDM#validateSchema(InputStream, String, Charset, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema3() throws Exception {
        XSLTTransformations transformations = memorix.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = memorix.validateSchema(new FileInputStream(tmp), "name", UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    @Test
    public void modify() throws Exception {

    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties = new HashMap<String, String>();
        properties.put("edmType", "IMAGE");
        properties.put("provider", "provider");
        properties.put("dataProvider", "dataProvider");
        properties.put("language", "language");
        properties.put("rights", "rights");
        properties.put("set", "set");
        properties.put("identifier", UUID.randomUUID().toString());

        return properties;
    }
}