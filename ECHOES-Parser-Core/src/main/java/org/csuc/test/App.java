/**
 * 
 */
package org.csuc.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlException;

/**
 * @author amartinez
 *
 */
public class App {
	private static Logger logger = LogManager.getLogger(App.class.getName());
		
	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) {
		if(args[0].equals("OAIDownloadAllSets"))	new OAIDownloadAllSets(args);
		else if(args[0].equals("OAIDownloadSet"))	new OAIDownloadSet(args);
		else if(args[0].equals("OAIParser"))	new OAIParser(args);
		else if(args[0].equals("Newspaper"))	new Newspapers(args);
		else	logger.info("java -jar [OAIDownloadAllSets | OAIDownloadSet | OAIParser | Newspaper]");				
	}
		
	public static String duration(Instant inici) {
		long diff = Duration.between(inici, Instant.now()).getSeconds();
	    return String.format("%02d:%02d:%02d", diff / 3600, diff % 3600 / 60, diff % 60);
	}
}
