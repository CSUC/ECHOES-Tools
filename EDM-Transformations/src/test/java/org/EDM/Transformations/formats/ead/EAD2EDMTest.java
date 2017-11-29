/**
 *
 */
package org.EDM.Transformations.formats.ead;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import isbn._1_931666_22_9.Ead;
import org.EDM.Transformations.deserialize.JaxbUnmarshal;

/**
 * @author amartinez
 *
 */
public class EAD2EDMTest extends TestCase {

    /**
     * Test method for
     * {@link org.EDM.Transformations.formats.ead.EAD2EDM#marshal(java.nio.charset.Charset, boolean)}.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testMarshal() throws FileNotFoundException, Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ead/ead.xml").getFile());
        assertTrue(file.exists());
        JaxbUnmarshal jxb = new JaxbUnmarshal(file, new Class[]{Ead.class});
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());
        
        EAD2EDM edm = new EAD2EDM(classLoader.getResource("ead/ead2edm.xsl").getPath(), new FileInputStream(file), properties(), System.out);

        assertNotNull(edm);
        assertNotNull(edm.getRDF());

        edm.marshal(StandardCharsets.UTF_8, true);
    }

    // TODO: Test delAdditionalhasViewWebResources function with tweaked XSL
    
    
    public Map<String, String> properties() {
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
