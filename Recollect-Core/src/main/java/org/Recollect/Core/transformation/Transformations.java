/**
 * 
 */
package org.Recollect.Core.transformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
	private Path path;
	
	public Transformations(String xslt, Path path) {
		Objects.requireNonNull(xslt, "xslt must not be null");
		Objects.requireNonNull(path, "path must not be null");
		
		xlsStreamSource = new StreamSource(Paths
	            .get(xslt)
	            .toAbsolutePath().toFile());
		
		this.path = path;		
	}
	
	
	public void transformationsFromUrl(URL sourceID, String identifier) throws TransformerException, TransformerConfigurationException, IOException{    
		Transformer transformer = fact.newTransformer(xlsStreamSource);
	    
		transformer.setParameter("identifier", identifier);
		transformer.setParameter("edmType", "IMAGE");
		
	    transformer.transform(new StreamSource(sourceID.openStream()), new StreamResult(Files.newOutputStream(path)));
	}
	
	public void transformationsFromString(String content, Map<String, String> map) throws TransformerException, TransformerConfigurationException, IOException{		
		Transformer transformer = fact.newTransformer(xlsStreamSource);

		if(Objects.nonNull(map)) {
			map.forEach((k,v)->{
				 transformer.setParameter(k,v);
			});			
		}
	    
		transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes())), new StreamResult(Files.newOutputStream(path)));
	}
}
