/**
 * 
 */
package org.Recollect.Core;



import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.Recollect.Core.transformation.Transformations;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;


/**
 * @author amartinez
 *
 */
public class MainTest {
	
	private static Logger logger = LogManager.getLogger(MainTest.class);
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		testRecollect();
//		testTransformation();
	}
	
	private static void testRecollect() {
		String[] args =
			{
				"--host", "https://webservices.picturae.com/a2a/20a181d4-c896-489f-9d16-20a3b7306b15",
				"--verb", "ListRecords",
				"--metadataPrefix", "oai_a2a",
				"--set", "bn_a",
				"--edmType", "IMAGE",
				"--provider", "Erfgoed",
				"--xslt",  "/home/amartinez/Escriptori/oai_a2a_to_edm.xslt",
				"--out", "/tmp/echoes"			
			};
		
		try {
			Main.main(args);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private static void testTransformation() {
		String xslt = "/home/amartinez/Escriptori/ead2edm.xsl";
//		String xslt = "/home/amartinez/Escriptori/ead2ese.xslt";
		
		
		//OutputStream out = IoBuilder.forLogger(MainTest.class).setLevel(Level.INFO).buildOutputStream();		
		try {
			OutputStream out = new FileOutputStream(new File("/home/amartinez/Escriptori/test_ead_to_edm.xml"));
			Transformations tranformation = new Transformations(xslt, out, getParameters());
			
			tranformation.transformationsFromUrl(
					new URL("https://www.erfgoedleiden.nl/collecties/archieven/archievenoverzicht/ead/xml/eadid/1005"));
			
		} catch (Exception e) {
			logger.error(e);;
		}		
	}
	
	private static Map<String, String> getParameters(){
		Map<String, String> properties = new HashMap<String,String>();
        properties.put("europeana_provider", "edm provider");
        properties.put("europeana_dataprovider", "edm data provider");
        properties.put("europeana_rights", "europeana_rights");
        properties.put("dc_rights", "dc_rights");
        properties.put("europeana_type", "IMAGE");
        properties.put("idSource", "unitid");
//        properties.put("useISODates", "false");
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
