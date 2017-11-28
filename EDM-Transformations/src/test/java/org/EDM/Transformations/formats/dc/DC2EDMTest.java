/**
 * 
 */
package org.EDM.Transformations.formats.dc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.EDM.Transformations.deserialize.JaxbUnmarshal;
import org.EDM.Transformations.deserialize.JibxUnMarshall;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import junit.framework.TestCase;

import javax.xml.transform.stream.StreamSource;

/**
 * @author amartinez
 *
 */
public class DC2EDMTest extends TestCase {

    private static Logger logger = LogManager.getLogger(DC2EDMTest.class);

    /**
	 * Test method for
	 * {@link org.EDM.Transformations.formats.dc.DC2EDM#marshal(java.nio.charset.Charset, boolean, Writer writer)}.
	 */
	@Test
	public void testMarshalWriter() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("dc/dc.xml").getFile());

		assertTrue(file.exists());
		JaxbUnmarshal jxb = new JaxbUnmarshal(file, new Class[] { OaiDcType.class });
		assertNotNull(jxb.getObject());
		assertTrue(jxb.isValidating());

        StringWriter writer = new StringWriter();
		DC2EDM dc = new DC2EDM(UUID.randomUUID().toString(), (OaiDcType) jxb.getObject(), properties());
        assertNotNull(dc);

		dc.marshal(StandardCharsets.UTF_8, true, writer);
        dc.marshal(StandardCharsets.UTF_8, true, System.out);

        assertNotNull(writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = new JibxUnMarshall(reader, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
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

    private Map<String, String> getParameters(){
        Map<String,String> properties = new HashMap<>();
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
