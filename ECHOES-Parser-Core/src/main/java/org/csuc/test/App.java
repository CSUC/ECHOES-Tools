/**
 * 
 */
package org.csuc.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.jaxb.UnmarshalOAIPMH;
import org.csuc.parser.OAIFactory;
import org.csuc.parser.ParserFactory;
import org.csuc.parser.Sax;
import org.csuc.transformation.Transformations;
import org.openarchives.oai._2.OAIPMHtype;

/**
 * @author amartinez
 *
 */
public class App {
	private static Logger logger = LogManager.getLogger(App.class.getName());
	
	private static Set<String> SetSpec = new HashSet<String>();

	/**
	 * @param args
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) {
		if(args[0].equals("OAIDownloadAllSets")){
			OAIDownload(args);
		}if(args[0].equals("OAIDownloadSet")){
			OAIDownloadSet(args);
		}else if(args[0].equals("OAIParser")){
			OAIParser(args);
		}else{
			logger.info("java -jar [OAIDownloadAllSets | OAIDownloadSet | OAIParser]");
		}		
	}

	private static void OAIDownloadSet(String[] args){
		if(args[1].equals("--host") && args[3].equals("--set") && args[5].equals("--xslt")){
			String url_base =  args[2];
			String set = args[4];
			String xslt = args[6];
			
			try {
				download(new URL(url_base + "?verb=ListRecords&metadataPrefix=a2a&set=" + set), set, xslt);
			} catch (MalformedURLException e) {
				logger.error(e);
			}	
		}else if(args[1].equals("--help") || args[1].equals("-h")){				
			logger.info("--host [host] --set [set] --xslt [verb]");
		}
		else{
			logger.info("--host [host] --set [set] --xslt [verb]");
		}
	}
	
	private static void OAIDownload(String[] args){
		try {
			if(args[1].equals("--host") && args[3].equals("--xslt")){
				String url_base =  args[2];
				String xslt = args[4];
				
				OAIPMHtype doc = new UnmarshalOAIPMH(new URL(url_base + "?verb=ListSets")).getOaipmh();
				
				doc.getListSets().getSet().forEach(setType->{SetSpec.add(setType.getSetSpec());});
				
				SetSpec.forEach(set->{
					logger.info(set);
					try {
						download(new URL(url_base + "?verb=ListRecords&metadataPrefix=a2a&set=" + set), set, xslt);
					} catch (MalformedURLException e) {
						logger.error(e);
					}
				});	
			}else if(args[1].equals("--help") || args[1].equals("-h")){
				logger.info("--host [host] --xslt [verb]");
			}
			else{
				logger.info("--host [host] --xslt [verb]");
			}
					
		} catch (MalformedURLException e) {
			logger.error(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void download(URL url, String set, String xslt) {
		logger.info(url);		
		Transformations transformation = new Transformations(xslt);
		
		try {
			//SaxParserFactory
			OAIPMHtype oai = new UnmarshalOAIPMH(url).getOaipmh();

			transformation.transformationsFromString(IOUtils.toString(url), set);
			//transformation.transformationsFromUrl(url, set);
			
			if(oai.getListRecords().getResumptionToken() != null){
				if(!oai.getListRecords().getResumptionToken().getValue().isEmpty()){				
					String token = oai.getListRecords().getResumptionToken().getValue().trim();				
					download(
						new URL(String.format("%s?verb=ListRecords&resumptionToken=%s", StringUtils.replaceAll(url.toString(), "\\?verb.+", ""), token)), set, xslt);
				}			
			}
		} catch (IOException e) {
			logger.error(e);
		} catch (TransformerConfigurationException e) {
			logger.error(e);
		} catch (TransformerException e) {
			logger.error(e);
		}
	}
	
	private static void OAIParser(String[] args){
		if(args[1].equals("--host") && args[3].equals("--verb")
			&& args[5].equals("--metadataPrefix") && args[7].equals("--set")){
			String host = args[2];
			String verb = args[4];
			String metadataPrefix = args[6];
			String set = args[8];
						
			ParserFactory factoryELO = new OAIFactory(new Sax(host, verb, metadataPrefix, set, null));			
			factoryELO.instanceFactory().execute();				
			factoryELO.getMapValues().entrySet().forEach(e->{
				String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
				logger.info(String.format("|%s|%s|%s|", e.getValue(), elementNameCount, e.getKey()));
			});	
			factoryELO.getDuration();
		}else if(args[1].equals("--host") && args[3].equals("--resumptionToken")){
			String host = args[1];			
			String resumptionToken = args[3];
				
			ParserFactory factoryELO = new OAIFactory(new Sax(host, null, null, null, resumptionToken));			
			factoryELO.instanceFactory().execute();				
			factoryELO.getMapValues().entrySet().forEach(e->{
				String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
		        logger.info(String.format("%-25s-----%s", e.getValue() + "(" + elementNameCount +")", e.getKey()));
			});	
			factoryELO.getDuration();		
		}else if(args[1].equals("--help") || args[1].equals("-h")){
			logger.info("--host [host] --verb [verb] --metadataPrefix [metadataPrefix] --set [set]");
			logger.info("--host [host] --verb [verb] --resumptionToken [resumptionToken]");
		}else{
			logger.info("--host [host] --verb [verb] --metadataPrefix [metadataPrefix] --set [set]");
			logger.info("--host [host] --verb [verb] --resumptionToken [resumptionToken]");
		}
	}

}
