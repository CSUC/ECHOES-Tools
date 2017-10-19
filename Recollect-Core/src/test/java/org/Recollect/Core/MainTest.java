/**
 * 
 */
package org.Recollect.Core;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

/**
 * @author amartinez
 *
 */
public class MainTest extends TestCase {
	
	private Logger logger = LogManager.getLogger(MainTest.class);
	
	/**
	 * Test method for {@link org.Recollect.Core.Main#main(java.lang.String[])}.
	 */
	public void testMain() {
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

}
