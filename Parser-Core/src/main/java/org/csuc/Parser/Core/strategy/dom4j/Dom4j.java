package org.csuc.Parser.Core.strategy.dom4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.util.xml.Result;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class Dom4j implements ParserMethod {

    private Logger logger = LogManager.getLogger(Dom4j.class);
    private SAXReader reader;

    private Map<String, String> values = new HashMap<>();
    private Map<String, AtomicInteger> count = new HashMap<>();
    private Map<String, String> namespace = new HashMap<>();

    private AtomicInteger iterNamespaces = new AtomicInteger(0);

    public Dom4j(){
        logger.info(String.format("Method: %s", getClass().getSimpleName()));
        reader = new SAXReader();
    }

    private void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    /**
     *
     *
     * @param element
     */
    private void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                Element el = (Element) node;
                for (int j = 0, total = el.attributeCount(); j < total; j++) {
                    Attribute attribute = el.attribute(j);
                    values.putIfAbsent(attribute.getPath(), "@"+ attribute.getQualifiedName());
                    count.computeIfAbsent(attribute.getPath(), (t) -> new AtomicInteger()).incrementAndGet();
                }

                values.putIfAbsent(String.format("%s/text()", el.getPath()), el.getQualifiedName());
                count.computeIfAbsent(String.format("%s/text()", el.getPath()), (t) -> new AtomicInteger()).incrementAndGet();
                namespace.putIfAbsent(((Element) node).getNamespaceURI(),
                        ((Element) node).getNamespacePrefix().isEmpty() ? "ns" + iterNamespaces.incrementAndGet() : ((Element) node).getNamespacePrefix());

                treeWalk((Element) node);
            }
        }
    }

    @Override
    public void parser(String fileOrPath) {
        try {
            Document document = reader.read(new FileInputStream(fileOrPath));
            treeWalk(document);
        } catch (DocumentException | FileNotFoundException e) {
            logger.error(e);
        }
    }

    @Override
    public void parser(URL url) {
        try {
            Document document = reader.read(url);
            treeWalk(document);
        } catch (DocumentException e) {
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

    private Result createObject(){
        Result result = new Result();
        values.forEach((String xpath, String value) -> {
            org.csuc.Parser.Core.util.xml.Node node = new org.csuc.Parser.Core.util.xml.Node();

            Integer c = count.get(xpath).get();
            node.setCount(c);
            node.setName(value);
            node.setPath(xpath);

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
