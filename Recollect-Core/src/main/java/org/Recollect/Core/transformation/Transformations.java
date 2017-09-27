/**
 * 
 */
package org.Recollect.Core.transformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author amartinez
 *
 */
public class Transformations {

	private TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	private StreamSource xlsStreamSource;
	
	private OutputStream out;
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
	
	public Transformations(String xslt, OutputStream out) throws Exception {
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
		if(Objects.isNull(out)) throw new Exception("out must not be null");
		
		xlsStreamSource = new StreamSource(Paths
	            .get(xslt)
	            .toAbsolutePath().toFile());
		
		this.out = out;		
	}
	
	
	/**
	 * 
	 * @param sourceID
	 * @param map
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 * @throws IOException
	 */
	public void transformationsFromUrl(URL sourceID) throws TransformerException, TransformerConfigurationException, IOException{    
		Transformer transformer = fact.newTransformer(xlsStreamSource);
	    
		if(Objects.nonNull(parameters)) {
			parameters.forEach((k,v)->{
				 transformer.setParameter(k,v);
			});			
		}
		
		transformer.transform(new StreamSource(sourceID.openStream()), new StreamResult(out));
	}
	
	/**
	 * 
	 * @param content
	 * @param map
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 * @throws IOException
	 */
	public void transformationsFromString(String content) throws TransformerException, TransformerConfigurationException, IOException{		
		Transformer transformer = fact.newTransformer(xlsStreamSource);

		if(Objects.nonNull(parameters)) {
			parameters.forEach((k,v)->{
				 transformer.setParameter(k,v);
			});			
		}
	    
		transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes())), new StreamResult(out));
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}	
}
