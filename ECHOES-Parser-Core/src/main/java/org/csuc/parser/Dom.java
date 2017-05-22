/**
 * 
 */
package org.csuc.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author amartinez
 *
 */
public class Dom implements ParserStrategy {
	
	static final Logger logger = LogManager.getLogger(Dom.class.getName());
	static final AtomicInteger iter = new AtomicInteger(0);
	
	private Map<String,String> mapValues = new HashMap<String,String>();
	private Map<String,String> namespaces = new HashMap<String,String>();	
	
	private AtomicInteger iterNamespaces = new AtomicInteger(0);
	
	
	private String url;
	private String content;
	
	private String resumptionToken = null;
	
	private DocumentBuilder documentBuilder;
	
	public Dom(String uri) {
		this.url = uri;
		
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}
	}
	
	
	
	@Override
	public void execute() {
		logger.info("Dom Stretategy Parser");
//		traverseLevel2(walker);
		test();		
	}
	
	private void test() {		
		try {
			this.content = urlToString(url);
			if(documentBuilder == null)		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			if(content != null){
				InputStream source = new ByteArrayInputStream(content.getBytes());
				Document domTree = documentBuilder.parse(source);	
				
		        Node rootNode = domTree.getDocumentElement();
				rootNode.normalize();
				
				parseXML(rootNode, null);

				if(resumptionToken != null){
					logger.info(iter.incrementAndGet() + "\t" + resumptionToken);
					this.url = String.format(
									"http://api.memorix-maior.nl/collectiebeheer/oai-pmh/key/7f040f84-cd4e-11df-a055-9b2671262fcb/tenant/lei?verb=ListRecords&resumptionToken=%s", resumptionToken);
//					documentBuilder = null;
//					test();
				}	
			}else{
				System.out.println("Null");
			}
		} catch (Exception e) {			
			logger.error(e);
		}
	}
	
	private String urlToString(String url){
		try {			
			return IOUtils.toString(new URL(url), "UTF-8");
		} catch (IOException e) {
			logger.error(e);
			return null;
		}        
	}
	
	/**
	 * 
	 * @param node
	 * @param parent
	 */
	public void parseXML(Node node, Node parent) {		
		try {
		    if (node.hasChildNodes()) {
		        NodeList childrens = node.getChildNodes();
		        for (int i = 0; i < childrens.getLength(); i++) {
		            if (parent != null) {
		                if (parent.getNamespaceURI() != null && parent.getPrefix() == null) {
		                    if (!namespaces.containsKey(parent.getNamespaceURI()))
		                        namespaces.put(parent.getNamespaceURI(), "ns" + iterNamespaces.incrementAndGet());
		                } else namespaces.put(parent.getNamespaceURI(), parent.getPrefix());

		            }
		            parseXML(childrens.item(i), node);
		        } //for
		    } //fi:root_childrens
		    else {
		        if (node.getNodeValue() != null) {
		            if (parent.getNodeName().equals("resumptionToken"))	resumptionToken = parent.getTextContent();		            
		            String nodeValue = node.getNodeValue().trim();
		            if (nodeValue.length() > 0) {
		                String path = getPath(node);
		                if (!mapValues.containsKey(parent.getNodeName())) {
		                    if (!parent.getNodeName().contains(":")) {
		                    	mapValues.put(path, namespaces.get(parent.getNamespaceURI()) + ":" + parent.getNodeName());
		                    } else {
		                    	mapValues.put(path, parent.getNodeName());
		                    }
		                }
		            }
		        }
		    }
		} catch (Exception e) {
		    logger.error(e);
		}   
	}
	
	/**
	 * 
	 * @param root
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getPath(Node root) {
		Node current = root;
        String output = "";
        while (current.getParentNode() != null) {
        	Node parent = current.getParentNode();
            if (parent != null && parent.getChildNodes().getLength() > 1) {                
				int nthChild = 1;
                Node siblingSearch = current;
                while ((siblingSearch = siblingSearch.getPreviousSibling()) != null) {
                    // only count siblings of same type
                    if (siblingSearch.getNodeName().equals(current.getNodeName())) {
                        nthChild++;
                    }
                }
                if(!current.getNodeName().contains(":"))	
                	output = "/" + namespaces.get(current.getNamespaceURI()) + ":" + current.getNodeName() + output;
                else	
                	output = "/" + current.getNodeName() + output;
            } else {
           		output = "/" + namespaces.get(current.getNamespaceURI()) + ":" + current.getNodeName() + output;
            }
            current = current.getParentNode();
        }
        return output.replace("null:#text","text()");
    }



	@Override
	public Map<String, String> getMapValues() {
		// TODO Auto-generated method stub
		return mapValues;
	}



	@Override
	public Map<String, String> getNamespaces() {
		return namespaces;
	}



	@Override
	public Map<String, AtomicInteger> getElementNameCount() {
		// TODO Auto-generated method stub
		return null;
	}
}
