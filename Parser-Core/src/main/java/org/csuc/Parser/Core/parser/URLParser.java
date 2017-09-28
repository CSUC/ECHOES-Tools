package org.csuc.Parser.Core.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * 
 * @author amartinez
 *
 */
public class URLParser {

private Logger logger = LogManager.getLogger(URLParser.class);
	
	private URL url;
	
	public URLParser(String url) throws MalformedURLException {
		logger.info(url);
		this.url = new URL(url);
		
	}
	
	public void parser(SAXParser saxparser, FragmentContentHandler contentHandler){
		try {
			saxparser.parse(getUrl(), contentHandler);			
		}catch(SAXException e) {
			logger.error(e);
		}catch(IOException e) {
			logger.error(e);
		}
		
	}
	
	private String getUrl() {
		return url.toString();
	}
	
	
}
