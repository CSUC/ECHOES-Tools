package org.EDM.Transformations.formats.ead;

import eu.europeana.corelib.definitions.jibx.RDF;
import isbn._1_931666_22_9.Ead;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.FactoryEDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.deserialize.JibxUnMarshall;
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

public class EAD2EDMTest {

    private static Logger logger = LogManager.getLogger(EAD2EDMTest.class);

    private File xml;
    private File xslt;
    private File tmp;

    private EDM ead;

    @Before
    public void setUp() throws Exception {
        xml = new File(getClass().getClassLoader().getResource("ead/ead.xml").getFile());
        xslt = new File(getClass().getClassLoader().getResource("ead/ead2edm.xsl").getFile());
        tmp = Files.createTempFile("ead_edm", ".xml").toFile();

        assertTrue(xml.exists());
        assertTrue(xslt.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[]{Ead.class});
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        ead = FactoryEDM.createFactory(new EAD2EDM(UUID.randomUUID().toString(), (Ead) jxb.getObject(), properties()));
        assertNotNull(ead);

        tmp.deleteOnExit();
    }

    /**
     *
     * {@link EAD2EDM#transformation(String)}.
     *
     * @throws Exception
     */
    @Test
    public void transformation() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));
    }

    /**
     *
     * {@link EAD2EDM#transformation(String, OutputStream, Map)}.
     *
     * @throws Exception
     */
    @Test
    public void transformation1() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));
    }

    /**
     *
     * {@link EAD2EDM#creation()}.
     *
     * @throws Exception
     */
    @Test
    public void creation() throws Exception {
        try{
            ead.creation();
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * {@link EAD2EDM#creation(Charset, boolean, Writer)}.
     *
     * @throws Exception
     */
    @Test
    public void creation1() throws Exception {
        StringWriter writer = new StringWriter();
        try{
            ead.creation(UTF_8, true, writer);
            assertTrue(writer.toString().isEmpty());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * {@link EAD2EDM#creation(Charset, boolean, OutputStream)}.
     *
     * @throws Exception
     */
    @Test
    public void creation2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);

        try{
            ead.creation(UTF_8, true, outs);
            int b  = new FileInputStream(tmp).read();
            assertEquals(-1, b);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * {@link EAD2EDM#validateSchema(Reader, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = ead.validateSchema(new FileReader(tmp), RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link EAD2EDM#validateSchema(Reader, String, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema1() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = ead.validateSchema(new FileReader(tmp), "name", RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link EAD2EDM#validateSchema(InputStream, Charset, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema2() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = ead.validateSchema(new FileInputStream(tmp), UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    /**
     * {@link EAD2EDM#validateSchema(InputStream, String, Charset, Class)}.
     *
     * @throws Exception
     */
    @Test
    public void validateSchema3() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = ead.validateSchema(new FileInputStream(tmp), "name", UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    @Test
    public void modify() throws Exception {
        XSLTTransformations transformations = ead.transformation(xslt.getPath(), new FileOutputStream(tmp), properties());
        assertNotNull(transformations);

        transformations.transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = ead.validateSchema(new FileInputStream(tmp), "name", UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());

        //RDF rdf = (RDF) jibx.getElement();

//        RDF rd2 = Object.c.clone

    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("europeana_provider", "europeana_provider");
        properties.put("europeana_dataprovider", "europeana_dataprovider");
        properties.put("europeana_rights", "europeana_rights");
        properties.put("dc_rights", "dc_rights");
        properties.put("europeana_type", "VIDEO");
        properties.put("idSource", "unitid");
        properties.put("useISODates", "false");
        properties.put("language", "en");
        properties.put("inheritElementsFromFileLevel", Boolean.TRUE.toString());
        properties.put("inheritOrigination", Boolean.TRUE.toString());
        properties.put("inheritUnittitle", Boolean.TRUE.toString());
        properties.put("inheritLanguage", Boolean.TRUE.toString());
        properties.put("inheritRightsInfo", Boolean.TRUE.toString());
        properties.put("useExistingDaoRole", Boolean.TRUE.toString());
        properties.put("useExistingLanguage", Boolean.TRUE.toString());
        properties.put("useExistingRepository", Boolean.TRUE.toString());
        properties.put("useExistingRightsInfo", Boolean.TRUE.toString());
        properties.put("minimalConversion", Boolean.FALSE.toString());
        properties.put("edm_identifier", "edm_identifier");
        properties.put("host", "localhost");
        properties.put("repository_code", "FR-SIAF");
        properties.put("xml_type_name", "fa");
        properties.put("landingPage", "ape");
        properties.put("useArchUnittitle", Boolean.TRUE.toString());
        return properties;
    }
}