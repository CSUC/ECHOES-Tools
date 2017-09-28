/**
 * 
 */
package org.csuc.Parser.Core.parser;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author amartinez
 *
 */
public class Sax implements ParserStrategy{
	
	static final Logger logger = LogManager.getLogger(Sax.class.getName());
	
	//SAX
	private FragmentContentHandler contentHandler;	
	private SAXParser parser;
	
	//OAIDocument
//	OAIPMHDocument oaipmh;
//	OAIPMHtype oaipmh;
	
//	private AtomicInteger iter = new AtomicInteger(0);	
	
//	private String url;
	
//	private String verb;
//	private String key;
//	private String host;
//	private String metadataPrefix;
//	private String set;
//	private String resumptionToken;
//	
//	private Path path;
	
	
	private OAIParser oaiparser;
	private URLParser urlparser;
	private FileParser fileparser;
	
	
	/**
	 * 
	 */
	public Sax(OAIParser oaiparser) {
		logger.info("Sax Stretategy OAI Parser");
		this.oaiparser = oaiparser;
		
		initialize();
		
		
		
	}
	
	public Sax(URLParser urlparser) {
		logger.info("Sax Stretategy URL Parser");
		this.urlparser = urlparser;
		
		initialize();
		
	}
	
	public Sax(FileParser fileparser) {		
		logger.info("Sax Stretategy File Parser");
		this.fileparser = fileparser;
		
		initialize();
		
	}
	
	private void initialize() {
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
		        
		    parser = factory.newSAXParser();       
		        
		    XMLReader xr = parser.getXMLReader();
		        
			contentHandler = new FragmentContentHandler(xr);
		
		}catch (ParserConfigurationException e){        
			logger.error(e);
		}catch(SAXException e){   
			logger.error(e);
		}		
	}	
	
	@Override
	public void execute() {
		if(Objects.nonNull(oaiparser))	oaiparser.parser(parser, contentHandler);		
		if(Objects.nonNull(urlparser))	urlparser.parser(parser, contentHandler);
		if(Objects.nonNull(fileparser)) 	fileparser.parser(parser, contentHandler);
	}
	
	@Override
	public Map<String, String> getMapValues() {
		return contentHandler.getMapValues();
	}

	@Override
	public Map<String, String> getNamespaces() {		
		return contentHandler.getNamespaces();
	}

	@Override
	public Map<String, AtomicInteger> getElementNameCount() {
		return contentHandler.getElementNameCount();
	}		
}
