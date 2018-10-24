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
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * @author amartinez
 *
 */
public class XSLTTransformations {
	
	private TransformerFactory fact = new net.sf.saxon.TransformerFactoryImpl();
	private StreamSource xlsStreamSource;
    private XSLTErrorListener errorListener = new XSLTErrorListener();

	private OutputStream out;
	private Map<String,String> xsltProperties = new HashMap<>();

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
        xsltProperties.values().removeIf(Objects::isNull);
        this.xsltProperties = xsltProperties;
	}

    /**
     *
     * @param sourceID
     * @throws IOException
     *
     */
	public void transformationsFromUrl(URL sourceID) throws IOException {
        Transformer transformer = null;
        try{
            transformer = fact.newTransformer(xlsStreamSource);
            transformer.setErrorListener(errorListener);

            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            if (Objects.nonNull(xsltProperties))
                xsltProperties.forEach(transformer::setParameter);

            transformer.transform(new StreamSource(sourceID.openStream()),
                    new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));
        }catch(TransformerException exception){

        }
	}

    /**
     *
     * @param content
     * @throws IOException
     *
     */
	public void transformationsFromString(String content) throws IOException  {
        Transformer transformer = null;
        try{
            transformer = fact.newTransformer(xlsStreamSource);
            transformer.setErrorListener(errorListener);

            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            if (Objects.nonNull(xsltProperties))
                xsltProperties.forEach(transformer::setParameter);


            transformer.transform(new StreamSource(new ByteArrayInputStream(content.getBytes())),
                    new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));
        }catch(TransformerException exception){}
	}

	/**
	 *
	 *  Ex: Source s = new DOMSource(null);
	 *
	 *
	 * @param source
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void transformationsFromSource(Source source) throws IOException {
        Transformer transformer = null;
	    try{
            transformer = fact.newTransformer(xlsStreamSource);
            transformer.setErrorListener(errorListener);

            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            if (Objects.nonNull(xsltProperties))
                xsltProperties.forEach(transformer::setParameter);

            transformer.transform(source, new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8.name())));
        }catch(TransformerException exception){}
	}

    public XSLTErrorListener getErrorListener() {
        return errorListener;
    }
}