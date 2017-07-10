/**
 * 
 */
package org.csuc.transformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

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

	private String folder = "./tmp/";
	
	private TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	private StreamSource xlsStreamSource;
	
	public Transformations(String xslt) {
		Objects.requireNonNull(xslt, "xslt must not be null");
		xlsStreamSource = new StreamSource(Paths
	            .get(xslt)
	            .toAbsolutePath().toFile());
	}
	
	
	public void transformationsFromUrl(URL sourceID, String set) throws TransformerException, TransformerConfigurationException, IOException{
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
	    
		Transformer transformer = fact.newTransformer(xlsStreamSource);
	    
		transformer.setParameter("year", ldt.getYear());
		transformer.setParameter("month", ldt.getMonthValue());
		transformer.setParameter("day", ldt.getDayOfMonth());		 
	    transformer.setParameter("collectionSet", set);
	    transformer.setParameter("folder", folder);
	    transformer.setParameter("dir", UUID.randomUUID().toString().replaceAll("-", ""));
	    
	    transformer.transform(new StreamSource(sourceID.openStream()), new StreamResult(System.out));
	}
	
	public void transformationsFromString(String content, String set) throws TransformerException, TransformerConfigurationException{
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		
		Transformer transformer = fact.newTransformer(xlsStreamSource);

	    transformer.setParameter("year", ldt.getYear());
		transformer.setParameter("month", ldt.getMonthValue());
		transformer.setParameter("day", ldt.getDayOfMonth());	
	    transformer.setParameter("collectionSet", set);
	    transformer.setParameter("folder", folder);
	    transformer.setParameter("dir", UUID.randomUUID().toString().replaceAll("-", ""));
	    
	    
	    transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes())), new StreamResult(System.out));
	}
}
