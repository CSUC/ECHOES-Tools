/**
 * 
 */
package org.csuc.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.jaxb.UnmarshalOAIPMH;
import org.openarchives.oai._2.OAIPMHtype;

/**
 * @author amartinez
 *
 */
public class OAIDownloadAllSets {

	private static Logger logger = LogManager.getLogger(OAIDownloadAllSets.class);
	
	private Set<String> SetSpec = new HashSet<String>();
	
	private Instant inici = Instant.now();
	
	public OAIDownloadAllSets() {}
	
	public OAIDownloadAllSets(String[] args) {
		try {
			String url = null;
			String key = null;
			String metadataPrefix = null;
			String xslt = null;
			String sch = null;
			
			for(int i = 0; i < args.length -1; i++){
				if(args[i].equals("--host"))	url = args[i+1];
				if(args[i].equals("--key"))	url = args[i+1];
				if(args[i].equals("--metadataPrefix"))	url = args[i+1];
				if(args[i].equals("--xslt"))	url = args[i+1];
				if(args[i].equals("--sch"))	sch = args[i+1];				
			}
		
			
			if(Objects.nonNull(url) && Objects.nonNull(metadataPrefix) && Objects.nonNull(xslt)){		
				OAIPMHtype doc = new UnmarshalOAIPMH(new URL(url + "?verb=ListSets")).getOaipmh();
				
				doc.getListSets().getSet().forEach(setType->{SetSpec.add(setType.getSetSpec());});
				
				for(String set : SetSpec){
					logger.info(set);
					try {
						if(Objects.nonNull(key)){							
							ECHOESUtils.download(new URL(String.format("%s?verb=ListRecords&key=%s&metadataPrefix=a2a&set=%s", url, key, set)), key, set, xslt);
						}else{
							ECHOESUtils.download(new URL(String.format("%s?verb=ListRecords&metadataPrefix=a2a&set=%s", url, set)), null, set, xslt);
						}						
					} catch (MalformedURLException e) {
						logger.error(e);
					}
				}
				ECHOESUtils.validateAndSchematron(sch, "./");
			}
		} catch (MalformedURLException e) {
			logger.error(e);
		}finally {
			logger.info(String.format("Duration: %s", App.duration(inici)));
		}
	}
	
}
