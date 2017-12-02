package org.csuc.Parser.Core.strategy.sax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.FragmentContentHandler;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.strategy.XPATH;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author amartinez
 */
public class Sax implements ParserMethod {

    private Logger logger = LogManager.getLogger(Sax.class);

    private FragmentContentHandler contentHandler;
    private SAXParser parser;

    public Sax(){
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
        //logger.info(String.format("ParserMethod: %s fileOrPath: %s", getClass().getSimpleName(), fileOrPath));
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
        //logger.info(String.format("ParserMethod: %s url: %s", getClass().getSimpleName(), url));
        try {
            parser.parse(url.toString(), contentHandler);
        } catch (SAXException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public List<XPATH> createXPATHResult() {
       return contentHandler.getXPATH();
    }

    @Override
    public Map<String, String> createNamespaceResult() {
        return contentHandler.getNamespaces();
    }
}
