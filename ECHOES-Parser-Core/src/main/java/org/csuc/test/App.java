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
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.csuc.parser.OAIFactory;
import org.csuc.parser.ParserFactory;
import org.csuc.parser.Sax;
import org.csuc.transformation.Transformations;
import org.openarchives.oai.x20.OAIPMHDocument;
import org.xml.sax.SAXException;

/**
 * @author amartinez
 *
 */
public class App {
	private static Logger logger = LogManager.getLogger(App.class.getName());
	
	
	private static XmlOptions options;
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
		try {
			if(args[1].equals("--host") && args[3].equals("--set") && args[5].equals("--xslt")){
				String url_base =  args[2];
				String set = args[4];
				String xslt = args[6];
				
				options = new XmlOptions().setLoadUseXMLReader(SAXParserFactory.newInstance().newSAXParser().getXMLReader());			

				try {
					download(new URL(url_base + "?verb=ListRecords&metadataPrefix=a2a&set=" + set), set, xslt);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}	
			}else if(args[1].equals("--help") || args[1].equals("-h")){
				logger.info("--host [host] --set [set] --xslt [verb]");
			}
			else{
				logger.info("--host [host] --set [set] --xslt [verb]");
			}
					
		} catch (SAXException e) {
			e.printStackTrace();
		}catch ( ParserConfigurationException e){
			e.printStackTrace();
		}
	}
	
	private static void OAIDownload(String[] args){
		try {
			if(args[1].equals("--host") && args[3].equals("--xslt")){
				String url_base =  args[2];
				String xslt = args[4];
				
				options = new XmlOptions().setLoadUseXMLReader(SAXParserFactory.newInstance().newSAXParser().getXMLReader());			

				OAIPMHDocument doc = OAIPMHDocument.Factory.parse(new URL(url_base + "?verb=ListSets"), options);
				
				Stream.of(doc.getOAIPMH().getListSets().getSetArray()).forEach(setType->{SetSpec.add(setType.getSetSpec());});
				
				SetSpec.forEach(set->{
					logger.info(set);
					try {
						download(new URL(url_base + "?verb=ListRecords&metadataPrefix=a2a&set=" + set), set, xslt);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				});	
			}else if(args[1].equals("--help") || args[1].equals("-h")){
				logger.info("--host [host] --xslt [verb]");
			}
			else{
				logger.info("--host [host] --xslt [verb]");
			}
					
		} catch (SAXException e) {
			e.printStackTrace();
		}catch ( ParserConfigurationException e){
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void download(URL url, String set, String xslt) {
		logger.info(url);		
		Transformations transformation = new Transformations(xslt);
		
		try {
			//SaxParserFactory
			OAIPMHDocument oai = OAIPMHDocument.Factory.parse(url, options);
	
			transformation.transformationsFromString(oai.xmlText(options), set);
//			transformation.transformationsFromUrl(url, set);
			
			if(oai.getOAIPMH().getListRecords().getResumptionToken() != null){
				if(!oai.getOAIPMH().getListRecords().getResumptionToken().getStringValue().isEmpty()){				
					String token = oai.getOAIPMH().getListRecords().getResumptionToken().getStringValue().trim();				
					download(
						new URL(String.format("%s?verb=ListRecords&resumptionToken=%s", StringUtils.replaceAll(url.toString(), "\\?verb.+", ""), token)), set, xslt);
				}			
			}
		} catch (XmlException | IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
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
