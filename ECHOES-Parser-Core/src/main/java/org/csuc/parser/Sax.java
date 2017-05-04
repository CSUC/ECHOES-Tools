/**
 * 
 */
package org.csuc.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
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
	
	
	private AtomicInteger iter = new AtomicInteger(0);	
	
	private String url;
	
	private String verb;
	private String host;
	private String metadataPrefix;
	private String set;
	private String resumptionToken;
	
	private Path path;
	
	/**
	 * 
	 * @param host URL host
	 * @param verb Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers o ListRecords
	 * @param metadataPrefix metadata prefix
	 * @param set	set de la col·lecció 
	 * @param resumptionToken resumptionToken si en te
	 */
	public Sax(String host, String verb, String metadataPrefix, String set, String resumptionToken) {	
		logger.info("Sax Stretategy Parser");		
		if(resumptionToken == null){
			Objects.requireNonNull(host, "host must not be null");
			Objects.requireNonNull(verb, "verb must not be null");
			Objects.requireNonNull(metadataPrefix, "metadataPrefix must not be null");
			Objects.requireNonNull(set, "set must not be null");
		}else	Objects.requireNonNull(host, "host must not be null");
		
		this.host = host;
		this.verb = verb;
		this.metadataPrefix = metadataPrefix;
		this.set = set;
		this.resumptionToken = resumptionToken;
		
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        
	        parser = factory.newSAXParser();       
	        
	        XMLReader xr = parser.getXMLReader();
	        
			contentHandler = new FragmentContentHandler(xr);
			
			if(this.resumptionToken == null)					
				setUrl(String.format("%s?verb=%s&metadataPrefix=%s&set=%s", this.host, this.verb, this.metadataPrefix, this.set));
			else	setUrl(String.format("%s?verb=ListRecords&resumptionToken=%s", this.host, this.resumptionToken));
			
			logger.info(String.format("url: %s", getUrl()));
			
		}catch (ParserConfigurationException e){        
        	e.printStackTrace();
        }catch(SAXException e){   
        	e.printStackTrace();
        }
	}
	
	public Sax(String host) {	
		logger.info("Sax Stretategy Parser");		
		Objects.requireNonNull(host, "host must not be null");
		
		this.host = host;
		this.url = host;
		
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        
	        parser = factory.newSAXParser();       
	        
	        XMLReader xr = parser.getXMLReader();
	        
			contentHandler = new FragmentContentHandler(xr);
			
			logger.info(String.format("url: %s", getUrl()));
			
		}catch (ParserConfigurationException e){        
        	e.printStackTrace();
        }catch(SAXException e){   
        	e.printStackTrace();
        }
	}
	
	public Sax(Path path) {	
		logger.info("Sax Stretategy Parser");		
		Objects.requireNonNull(path, "path must not be null");		
		this.path = path;
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        
	        parser = factory.newSAXParser();       
	        
	        XMLReader xr = parser.getXMLReader();
	        
			contentHandler = new FragmentContentHandler(xr);
			
			logger.info(String.format("path: %s", this.path));
			
		}catch (ParserConfigurationException e){        
        	e.printStackTrace();
        }catch(SAXException e){   
        	e.printStackTrace();
        }
	}
	
	
	@Override
	public void execute() {
		//is File&Folder
		if(this.path != null && Files.exists(this.path, LinkOption.NOFOLLOW_LINKS)){			
			try {
				Files.walk(this.path)				
				.filter(Files::isRegularFile)
				.filter(f-> f.toString().endsWith(".xml"))
				.forEach(f->{
					logger.info(String.format("file: %s", f.getFileName()));
					try {						
						parser.parse(f.toFile(), contentHandler);
					} catch (SAXException | IOException e) {
						e.printStackTrace();
					}				
				});
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}else{//URL
			if(contentHandler.getResumptionTokenValue() != null) contentHandler.setResumptionTokenValue(null);
			try{
	            parser.parse(url, contentHandler);
	            
	            if(contentHandler.getResumptionTokenValue() != null){
					logger.info(iter.incrementAndGet() + "\t" + contentHandler.getResumptionTokenValue());
					setUrl(String.format("%s?verb=ListRecords&resumptionToken=%s", this.host, contentHandler.getResumptionTokenValue()));
					execute();
				}
			}catch(SAXException e){   
	        	e.printStackTrace();
	        }catch (IOException e){ 
	        	e.printStackTrace();
	        }
		}		
	}	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
