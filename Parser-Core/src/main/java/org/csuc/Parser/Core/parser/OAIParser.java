/**
 * 
 */
package org.csuc.Parser.Core.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.SAXParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.jaxb.JaxbUnmarshal;
import org.openarchives.oai._2.OAIPMHtype;
import org.xml.sax.SAXException;

/**
 * @author amartinez
 *
 */
public class OAIParser {

	private Logger logger = LogManager.getLogger(OAIParser.class);
	
	private URL url;
	
	private AtomicInteger iter = new AtomicInteger(0);	
	
	
	public OAIParser(String url) throws MalformedURLException {
		logger.info(url);
		this.url = new URL(url);
		
	}
	
	public void parser(SAXParser saxparser, FragmentContentHandler contentHandler){
		OAIPMHtype OAIPMHtype = 
				(OAIPMHtype) new JaxbUnmarshal(this.url, OAIPMHtype.class).getObject();
		try {
			saxparser.parse(getUrl(), contentHandler);        
			if(OAIPMHtype.getListRecords().getResumptionToken() != null){
				if(!OAIPMHtype.getListRecords().getResumptionToken().getValue().isEmpty()){
					logger.info(iter.incrementAndGet() + "\t" + OAIPMHtype.getListRecords().getResumptionToken().getValue());
						
					setUrl(next(getUrl(), OAIPMHtype.getListRecords().getResumptionToken().getValue()));
					parser(saxparser, contentHandler);
				}
			}
		}catch(SAXException e) {
			logger.error(e);
		}catch(IOException e) {
			logger.error(e);
		}
		
	}
	
	
	public String next(String url, String resumptionToken) {
		String baseurl = getUrl().replaceAll("\\?verb=.+", "");
		return String.format("%s?verb=ListRecords&resumptionToken=%s", baseurl, resumptionToken);
	}
	
	public String getUrl() {
		return url.toString();
	}
//	
	public void setUrl(String url) throws MalformedURLException {		
		this.url = new URL(url);
	}
	
}
