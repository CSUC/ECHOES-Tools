package org.csuc.Parser.Core.strategy.sax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.util.xml.Result;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author amartinez
 */
public class Sax implements ParserMethod {

    private Logger logger = LogManager.getLogger(Sax.class);

    private FragmentContentHandler contentHandler;
    private SAXParser parser;

    public Sax(){
        logger.info(String.format("Method: %s", getClass().getSimpleName()));
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newSAXParser();

            XMLReader xr = parser.getXMLReader();

            contentHandler = new FragmentContentHandler(xr);

        }catch (ParserConfigurationException e){
            logger.error(e);
        }catch(SAXException e){
            logger.error(e);
        }
    }

    @Override
    public void parser(String fileOrPath) {
        try {
            parser.parse(new FileInputStream(fileOrPath), contentHandler);
        } catch (SAXException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void parser(URL url) {
        try {
            parser.parse(url.toString(), contentHandler);
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

    private Result createObject(){
        Result result = new Result();
        contentHandler.getMapValues().forEach((String xpath, String value) -> {
            org.csuc.Parser.Core.util.xml.Node node = new org.csuc.Parser.Core.util.xml.Node();

            Integer c = contentHandler.getMapCount().get(xpath).get();
            node.setCount(c);
            node.setName(value);
            node.setPath(xpath);

            result.getNodeResult().getNode().add(node);
        });

        contentHandler.getNamespaces().forEach((String uri, String prefix)->{
            org.csuc.Parser.Core.util.xml.Namespace namespace = new org.csuc.Parser.Core.util.xml.Namespace();

            namespace.setUri(uri);
            namespace.setPrefix(prefix);

            result.getNamespaceResult().getNamespaces().add(namespace);
        });
        return result;
    }

}
