/**
 * 
 */
package org.EDM.Transformations.formats.xslt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * @author amartinez
 *
 */
public class XSLTTransformations {
	
	private TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	private StreamSource xlsStreamSource;
	
	private OutputStream out;
	private Map<String,String> xsltProperties = new HashMap<String,String>();
	
	
	
	public XSLTTransformations(String xslt, OutputStream out) throws Exception {
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
		if(Objects.isNull(out)) throw new Exception("out must not be null");
		
		xlsStreamSource = new StreamSource(Paths
	            .get(xslt)
	            .toAbsolutePath().toFile());
		
		this.out = out;		
	}
	
	
	public XSLTTransformations(String xslt, OutputStream out, Map<String,String> xsltProperties) throws Exception {
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
	 * @throws IOException 
	 * @throws TransformerException 
	 * @throws UnsupportedEncodingException 
	 */
	public void transformationsFromUrl(URL sourceID) throws UnsupportedEncodingException, TransformerException, IOException {		
		Transformer transformer = fact.newTransformer(xlsStreamSource);

		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		if (Objects.nonNull(xsltProperties)) {
			xsltProperties.forEach((k, v) -> {
				transformer.setParameter(k, v);
			});
		}

		transformer.transform(new StreamSource(sourceID.openStream()),
				new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));		
	}
	
	/**
	 * 
	 * @param content
	 * @throws TransformerException 
	 * @throws UnsupportedEncodingException 
	 */
	public void transformationsFromString(String content) throws UnsupportedEncodingException, TransformerException {		
		Transformer transformer = fact.newTransformer(xlsStreamSource);

		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		if (Objects.nonNull(xsltProperties)) {
			xsltProperties.forEach((k, v) -> {
				transformer.setParameter(k, v);
			});
		}

		transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes())),
				new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));		 
	}

	/**
	 *
	 *  Ex: Source s = new DOMSource(null);
	 *
	 *
	 * @param source
	 * @throws UnsupportedEncodingException
	 * @throws TransformerException
	 * @throws IOException
	 */
	public void transformationsFromSource(Source source) throws UnsupportedEncodingException, TransformerException, IOException {
		Transformer transformer = fact.newTransformer(xlsStreamSource);

		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		if (Objects.nonNull(xsltProperties)) {
			xsltProperties.forEach((k, v) -> {
				transformer.setParameter(k, v);
			});
		}

		transformer.transform(source, new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));
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