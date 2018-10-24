/**
 * 
 */
package org.csuc.Parser.Core.strategy.sax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author amartinez
 *
 */
public class FragmentContentHandler extends DefaultHandler{
	
	private static Logger logger = LogManager.getLogger(FragmentContentHandler.class.getName());
			
	private Map<String,String> mapValues = new HashMap<>();
	private Map<String,String> namespaces = new HashMap<>();
    private Map<String, AtomicInteger> mapCount = new HashMap<>();
	
	private String resumptionTokenValue = null;
	private String qName = null; 
	
	private AtomicInteger iterNamespaces = new AtomicInteger(0);

	private String xPath = "";
	private XMLReader xmlReader;
	private FragmentContentHandler parent;
	private StringBuilder characters = new StringBuilder();

	public FragmentContentHandler(XMLReader xmlReader) {
		this.xmlReader = xmlReader;
	}
	  
	private FragmentContentHandler(String xPath, XMLReader xmlReader, FragmentContentHandler parent) {
		this(xmlReader);
	    this.xPath = xPath;
	    this.parent = parent;
	    
	}
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {		
		return super.resolveEntity(publicId, systemId);
	}

	@Override
	public void notationDecl(String name, String publicId, String systemId) throws SAXException {
		super.notationDecl(name, publicId, systemId);
	}

	@Override
	public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
			throws SAXException {
		super.unparsedEntityDecl(name, publicId, systemId, notationName);
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		super.setDocumentLocator(locator);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
//		System.out.println("end Document\n");
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		if (uri != null && prefix.isEmpty()){
			if (!namespaces.containsKey(uri))	namespaces.put(uri, "ns" + iterNamespaces.incrementAndGet());
		}else	namespaces.put(uri, prefix);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		super.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		final StringBuffer attr = new StringBuffer();
		
		if(!qName.contains(":"))	qName = namespaces.get(uri) + ":" + qName;
		
		IntStream.range(0, attributes.getLength()).forEach(l->{
			if(!attributes.getQName(l).equals("xsi:schemaLocation")){
				if(l != attributes.getLength() -1)	attr.append("@").append(attributes.getQName(l)).append(" and ");
		    	else	attr.append("@").append(attributes.getQName(l));
			}			
	    }); 
		
		if(attr.length()>0)	qName = qName + "[" + attr.toString()  + "]";

	    String childXPath = xPath + "/" + qName;
	    	
	    FragmentContentHandler child = new FragmentContentHandler(childXPath, xmlReader, this);
	    
	    child.setMapValues(getMapValues());
	    child.setNamespaces(getNamespaces());
	    child.setResumptionTokenValue(getResumptionTokenValue());
	    child.setMapCount(getMapCount());
	    child.setIterNamespaces(getIterNamespaces());
	    child.setqName(qName);
	    
	    xmlReader.setContentHandler(child);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {		
		String value = characters.toString().trim();
	    if (value.length() > 0){
	    	if(getqName().contains("resumptionToken"))	setResumptionTokenValue(value);
	    	mapValues.put(xPath, getqName());
            mapCount.computeIfAbsent(xPath, k -> new AtomicInteger()).incrementAndGet();
	    }
	    parent.setResumptionTokenValue(getResumptionTokenValue());
	    parent.setIterNamespaces(getIterNamespaces());
	    xmlReader.setContentHandler(parent);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characters.append(ch, start, length);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		super.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		super.skippedEntity(name);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		logger.error(e);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		logger.error(e);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {		
		logger.error(e);
	}

    public Map<String, AtomicInteger> getMapCount() {
        return mapCount;
    }

    private void setMapCount(Map<String, AtomicInteger> mapCount) {
        this.mapCount = mapCount;
    }

	public Map<String, String> getMapValues() {
		return mapValues;
	}

	public Map<String, String> getNamespaces() {
		return namespaces;
	}

	private void setMapValues(Map<String, String> mapValues) {
		this.mapValues = mapValues;
	}

    private void setNamespaces(Map<String, String> namespaces) {
		this.namespaces = namespaces;
	}

    private String getResumptionTokenValue() {
		return resumptionTokenValue;
	}

    private void setResumptionTokenValue(String resumptionTokenValue) {
		this.resumptionTokenValue = resumptionTokenValue;
	}

    private String getqName() {
		return qName;
	}

    private void setqName(String qName) {
		this.qName = qName;
	}

    private AtomicInteger getIterNamespaces() {
		return iterNamespaces;
	}

	private void setIterNamespaces(AtomicInteger iterNamespaces) {
		this.iterNamespaces = iterNamespaces;
	}

}
