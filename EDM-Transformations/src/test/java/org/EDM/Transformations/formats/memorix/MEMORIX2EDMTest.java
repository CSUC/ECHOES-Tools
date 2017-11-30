package org.EDM.Transformations.formats.memorix;

import eu.europeana.corelib.definitions.jibx.RDF;
import junit.framework.TestCase;
import nl.memorix_maior.api.rest._3.Memorix;
import org.EDM.Transformations.deserialize.JaxbUnmarshal;
import org.EDM.Transformations.deserialize.JibxUnMarshall;
import org.EDM.Transformations.serialize.JaxbMarshal;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class MEMORIX2EDMTest extends TestCase {

    /**
     *
     *
     *
     * @throws Exception
     */
    @Test
    public void testTransformationAndValidation() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File xml = new File(classLoader.getResource("memorix/memorix.xml").getFile());
        File xslt = new File(classLoader.getResource("memorix/memorix.xslt").getFile());
        File tempFile = Files.createTempFile("memorix_edm", ".xml").toFile();

        assertTrue(xml.exists());
        assertTrue(xslt.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[] { Memorix.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        new MEMORIX2EDM(UUID.randomUUID().toString(), xslt.getPath(), properties(), new FileOutputStream(tempFile))
            .transformationsFromSource(new StreamSource(new FileInputStream(xml)));

        JibxUnMarshall jibx = new JibxUnMarshall(new FileInputStream(tempFile), StandardCharsets.UTF_8.name(), RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());

        tempFile.deleteOnExit();
    }

    public Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties = new HashMap<String, String>();
        properties.put("edmType", "IMAGE");
        properties.put("provider", "provider");
        properties.put("dataProvider", "dataProvider");
        properties.put("language", "language");
        properties.put("rights", "rights");
        properties.put("set", "set");

        return properties;
    }
}