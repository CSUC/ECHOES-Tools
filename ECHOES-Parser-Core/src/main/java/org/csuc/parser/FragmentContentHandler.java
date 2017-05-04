/**
 * 
 */
package org.csuc.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author amartinez
 *
 */
public class FragmentContentHandler extends DefaultHandler{
	
	private Map<String,String> mapValues = new HashMap<String,String>();
	private Map<String,String> namespaces = new HashMap<String,String>();
	private Map<String, AtomicInteger> elementNameCount = new HashMap<String, AtomicInteger>();
	
	private String resumptionTokenValue = null;
	private String qName = null; 
	
	private AtomicInteger iterNamespaces = new AtomicInteger(0);

	private String xPath = new String();
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
				if(l != attributes.getLength() -1)	attr.append("@"+attributes.getQName(l) + " and ");
		    	else	attr.append("@"+attributes.getQName(l));
			}			
	    }); 
		
		if(attr.length()>0)	qName = qName + "[" + attr.toString()  + "]";
	    
//		Integer count = elementNameCount.get(qName);		
//	    if (null == count)	count = 1;
//	    else	count++;	  
//	    elementNameCount.put(qName, count);
		elementNameCount.putIfAbsent(qName, new AtomicInteger(0));
		elementNameCount.get(qName).incrementAndGet();	

	    String childXPath = xPath + "/" + qName;
	    	
	    FragmentContentHandler child = new FragmentContentHandler(childXPath, xmlReader, this);
	    
	    child.setMapValues(getMapValues());
	    child.setNamespaces(getNamespaces());
	    child.setResumptionTokenValue(getResumptionTokenValue());
	    child.setElementNameCount(getElementNameCount());
	    child.setIterNamespaces(getIterNamespaces());
	    child.setqName(qName);
	    
	    xmlReader.setContentHandler(child);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {		
		//if(!qName.contains(":"))	qName = namespaces.get(uri) + ":" + qName;		
		String value = characters.toString().trim();		
	    if (value.length() > 0){
//	    	if(qName.contains("resumptionToken"))	setResumptionTokenValue(value);	    	
//	    	mapValues.put(xPath, qName);
	    	if(getqName().contains("resumptionToken"))	setResumptionTokenValue(value);	    	
	    	mapValues.put(xPath, getqName());
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
		e.printStackTrace();
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		e.printStackTrace();
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {		
		e.printStackTrace();
	}
	
	public Map<String, String> getMapValues() {
		return mapValues;
	}

	public Map<String, String> getNamespaces() {
		return namespaces;
	}

	public void setMapValues(Map<String, String> mapValues) {
		this.mapValues = mapValues;
	}

	public void setNamespaces(Map<String, String> namespaces) {
		this.namespaces = namespaces;
	}

	public String getResumptionTokenValue() {
		return resumptionTokenValue;
	}

	public void setResumptionTokenValue(String resumptionTokenValue) {
		this.resumptionTokenValue = resumptionTokenValue;
	}
	
	public String getqName() {
		return qName;
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

	public Map<String, AtomicInteger> getElementNameCount() {
		return elementNameCount;
	}

	public void setElementNameCount(Map<String, AtomicInteger> elementNameCount) {
		this.elementNameCount = elementNameCount;
	}

	public AtomicInteger getIterNamespaces() {
		return iterNamespaces;
	}

	public void setIterNamespaces(AtomicInteger iterNamespaces) {
		this.iterNamespaces = iterNamespaces;
	}	
	
	
}
