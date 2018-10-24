package org.EDM.Transformations.formats.memorix;

import eu.europeana.corelib.definitions.jibx.RDF;
import nl.memorix_maior.api.rest._3.Memorix;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.FactoryEDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.deserialize.JibxUnMarshall;
import org.junit.After;
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

    @After
    public void tearDown() throws Exception {
        tmp.deleteOnExit();
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



    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<>();
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