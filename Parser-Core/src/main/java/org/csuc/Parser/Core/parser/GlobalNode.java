/**
 * 
 */
package org.csuc.Parser.Core.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * @author amartinez
 *
 */
public class GlobalNode {

	private static Logger logger = LogManager.getLogger(GlobalNode.class.getName());
	
	private AtomicInteger iterNamespaces = new AtomicInteger(0);
	
	private Map<String,String> namespaces = new HashMap<String,String>();
	private Map<String,String> map = new HashMap<String,String>();
	
	private String resumptionToken = null;
	
	
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
		                if (!map.containsKey(parent.getNodeName())) {
		                    if (!parent.getNodeName().contains(":")) {
		                        map.put(path, namespaces.get(parent.getNamespaceURI()) + ":" + parent.getNodeName());
		                    } else {
		                        map.put(path, parent.getNodeName());
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

	public Map<String, String> getNamespaces() {
		return namespaces;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}
}
