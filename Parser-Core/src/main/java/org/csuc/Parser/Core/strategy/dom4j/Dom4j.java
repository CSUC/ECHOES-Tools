package org.csuc.Parser.Core.strategy.dom4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.strategy.XPATH;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                    values.putIfAbsent(attribute.getPath(), "@"+ attribute.getName());
                    count.computeIfAbsent(attribute.getPath(), (t) -> new AtomicInteger()).incrementAndGet();
                }

                values.putIfAbsent(el.getPath(), el.getName());
                count.computeIfAbsent(el.getPath(), (t) -> new AtomicInteger()).incrementAndGet();
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
    public List<XPATH> createXPATHResult() {
        List<XPATH> list = new ArrayList<>();
        values.forEach((String xpath, String value) -> {
            Integer c = count.get(xpath).get();
            list.add(new XPATH(xpath, value, c));
        });
        return list;
    }

    @Override
    public Map<String, String> createNamespaceResult() {
        return namespace;
    }
}
