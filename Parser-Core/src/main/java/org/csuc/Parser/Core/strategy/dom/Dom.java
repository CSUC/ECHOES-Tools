package org.csuc.Parser.Core.strategy.dom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.util.xml.Result;
import org.w3c.dom.*;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class Dom implements ParserMethod {

    private Logger logger = LogManager.getLogger(Dom.class);

    private DocumentBuilder documentBuilder;

    private Map<String, String> values = new HashMap<>();
    private Map<String, AtomicInteger> count = new HashMap<>();
    private Map<String, String> namespace = new HashMap<>();

    public Dom() throws ParserConfigurationException {
        logger.info(String.format("Method: %s", getClass().getSimpleName()));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        documentBuilder = dbf.newDocumentBuilder();
    }

    @Override
    public void parser(String fileOrPath) {
        try {
            Document document = documentBuilder.parse(new FileInputStream(fileOrPath));


        } catch (SAXException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void parser(URL url) {
        try {
            Document document = documentBuilder.parse(url.toString());

            traversal(document);
//            parseXML(document,null);
        } catch (SAXException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void createXML(OutputStream outs) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Result.class);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createObject(), outs);
        } catch (JAXBException e) {
            logger.error(e);
        }
    }

    @Override
    public void createJSON(OutputStream outs) {
        try {
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(outs, createObject());
        } catch (IOException e) {
            logger.error(e);
        }
    }


    /**
     *
     * @param document
     */
    private void traversal(Document document){
        DocumentTraversal traversal = (DocumentTraversal) document;

        NodeIterator iterator = traversal.createNodeIterator(
                document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

        for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
            String tagname = ((Element) n).getTagName();
            namespace.putIfAbsent(n.getNamespaceURI(), n.getPrefix());
            NamedNodeMap map = ((Element)n).getAttributes();
            if(map.getLength() > 0) {
                for(int i=0; i<map.getLength(); i++) {
                    Node node = map.item(i);
                    String path = String.format("%s[@%s]", getXPath(n), node.getNodeName());
                    values.putIfAbsent(path, "@"+ node.getNodeName());
                    count.computeIfAbsent(path, (t) -> new AtomicInteger()).incrementAndGet();
                }
            }
            else {
                String path = String.format("%s/text()", getXPath(n));
                values.putIfAbsent(path, tagname);
                count.computeIfAbsent(path, (t) -> new AtomicInteger()).incrementAndGet();
            }
        }
    }

    /**
     *
     * @param node
     * @return
     */
    private String getXPath(Node node){
        Node parent = node.getParentNode();
        return Objects.isNull(parent) ? "" : String.format("%s/%s", getXPath(parent), node.getNodeName());
    }

    private Result createObject(){
        Result result = new Result();
        values.forEach((String xpath, String value) -> {
            org.csuc.Parser.Core.util.xml.Node node = new org.csuc.Parser.Core.util.xml.Node();

            Integer c = count.get(xpath).get();
            node.setCount(c);
            node.setName(value);
            node.setPath(xpath);
//
            result.getNodeResult().getNode().add(node);
        });

        namespace.forEach((String uri, String prefix)->{
            org.csuc.Parser.Core.util.xml.Namespace namespace = new org.csuc.Parser.Core.util.xml.Namespace();

            namespace.setUri(uri);
            namespace.setPrefix(prefix);

            result.getNamespaceResult().getNamespaces().add(namespace);
        });
        return result;
    }

}
