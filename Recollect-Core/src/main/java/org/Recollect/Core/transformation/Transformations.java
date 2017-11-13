/**
 * 
 */
package org.Recollect.Core.transformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class Transformations {

	private static Logger logger = LogManager.getLogger(Transformations.class);
	
	private TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	private StreamSource xlsStreamSource;
	
	private OutputStream out;
	private Map<String,String> xsltProperties = new HashMap<String,String>();
	
	
	
	public Transformations(String xslt, OutputStream out) throws Exception {
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
		if(Objects.isNull(out)) throw new Exception("out must not be null");
		
		xlsStreamSource = new StreamSource(Paths
	            .get(xslt)
	            .toAbsolutePath().toFile());
		
		this.out = out;		
	}
	
	
	public Transformations(String xslt, OutputStream out, Map<String,String> xsltProperties) throws Exception {
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
		if(Objects.isNull(out)) throw new Exception("out must not be null");
		if(Objects.isNull(xsltProperties)) throw new Exception("xsltProperties must not be null");
		
		xlsStreamSource = new StreamSource(Paths
	            .get(xslt)
	            .toAbsolutePath().toFile());
		
		this.out = out;
		this.xsltProperties = xsltProperties;
	}
	
	/**
	 * 
	 * @param sourceID
	 */
	public void transformationsFromUrl(URL sourceID) {    
		try {
			Transformer transformer = fact.newTransformer(xlsStreamSource);
		    
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			
			if(Objects.nonNull(xsltProperties)) {
				xsltProperties.forEach((k,v)->{
					 transformer.setParameter(k,v);
				});			
			}
			
			transformer.transform(
					new StreamSource(sourceID.openStream()),
					new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));
			
		} catch (TransformerException | IOException exception) {			
			logger.error(exception);
		}		
	}
	
	/**
	 * 
	 * @param content
	 */
	public void transformationsFromString(String content) {		
		try {
			Transformer transformer = fact.newTransformer(xlsStreamSource);
			
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			
			if(Objects.nonNull(xsltProperties)) {
				xsltProperties.forEach((k,v)->{
					 transformer.setParameter(k,v);
				});			
			}

			transformer.transform(
					new StreamSource(new ByteArrayInputStream(content.getBytes())),
					new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));
		
		} catch (TransformerException | IOException exception) {			
			logger.error(exception);
		}  
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		xsltProperties.put(key, value);
	}
	
}
