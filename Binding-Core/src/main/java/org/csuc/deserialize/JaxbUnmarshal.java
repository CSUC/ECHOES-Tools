/**
 *
 */
package org.csuc.deserialize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;


/**
 * @author amartinez
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JaxbUnmarshal {

    private static Logger logger = LogManager.getLogger(JaxbUnmarshal.class);

    private Object data;
    private ValidationHandler validationEvent = new ValidationHandler();

    public JaxbUnmarshal(File file, Class[] classType)  {
        logger.debug(String.format("read file %s", file));

        try{
            JAXBContext jc = JAXBContext.newInstance(classType);
            Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(validationEvent);

            Object obj = u.unmarshal(file);

            if (obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating()));
        }catch(JAXBException e){
        }
    }

    public JaxbUnmarshal(InputStream inputStream, Class[] classType) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(classType);
        Unmarshaller u = jc.createUnmarshaller();
        u.setEventHandler(validationEvent);

        Object obj = u.unmarshal(inputStream);
        if (obj instanceof JAXBElement)
            data = ((JAXBElement<Object>) obj).getValue();
        else data = obj;

        logger.debug(String.format("isValidating %s", validationEvent.isValidating()));

    }


    public JaxbUnmarshal(URL url, Class[] classType) {
        logger.debug(String.format("read url %s", url));

        try{
            JAXBContext jc = JAXBContext.newInstance(classType);
            Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(validationEvent);

            Object obj = u.unmarshal(url);

            if (obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating()));
        }catch(JAXBException e){
        }
    }

    public JaxbUnmarshal(StringBuffer stringbuffer, Class[] classType) throws JAXBException {
        logger.debug(String.format("read StringBuffer %s", stringbuffer));
        JAXBContext jc = JAXBContext.newInstance(classType);
        Unmarshaller u = jc.createUnmarshaller();
        u.setEventHandler(validationEvent);

        Object obj = u.unmarshal(new StreamSource(new StringReader(stringbuffer.toString())));

        if (obj instanceof JAXBElement)
            data = ((JAXBElement<Object>) obj).getValue();
        else data = obj;

        logger.debug(String.format("isValidating %s", validationEvent.isValidating()));

    }

    public JaxbUnmarshal(Node node, Class<?> classType) throws IOException, JAXBException {
        logger.debug(String.format("read Node %s", node));
        Source xmlSource = new DOMSource(node);
        Unmarshaller u = JAXBContext.newInstance(classType).createUnmarshaller();
        u.setEventHandler(validationEvent);

        Object obj = u.unmarshal(xmlSource);

        if (obj instanceof JAXBElement)
            data = ((JAXBElement<Object>) obj).getValue();
        else data = obj;

        logger.debug(String.format("isValidating %s", validationEvent.isValidating()));

    }

    @SuppressWarnings("deprecation")
    public JaxbUnmarshal(SAXSource saxSource, Class[] classType) throws JAXBException, ParserConfigurationException, SAXException {
        logger.debug(String.format("read SAXSource %s", saxSource));
        // configure a validating SAX2.0 parser (Xerces2)
        final String JAXP_SCHEMA_LANGUAGE =
                "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
        final String JAXP_SCHEMA_LOCATION =
                "http://java.sun.com/xml/jaxp/properties/schemaSource";
        final String W3C_XML_SCHEMA =
                "http://www.w3.org/2001/XMLSchema";

        System.setProperty("javax.xml.parsers.SAXParserFactory",
                "org.apache.xerces.jaxp.SAXParserFactoryImpl");

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(true);
        SAXParser saxParser = spf.newSAXParser();

        saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        saxParser.setProperty(JAXP_SCHEMA_LOCATION, "http://....");


//			XMLReader xmlReader = saxParser.getXMLReader();
//			SAXSource source =
//					new SAXSource( xmlReader, new InputSource( "http://..." ) );

        // Setup JAXB to unmarshal
        JAXBContext jc = JAXBContext.newInstance(classType);
        Unmarshaller u = jc.createUnmarshaller();
        ValidationEventCollector vec = new ValidationEventCollector();
        u.setEventHandler(vec);

        // turn off the JAXB provider's default validation mechanism to
        // avoid duplicate validation
        u.setValidating(false);

        // unmarshal
        Object obj = u.unmarshal(saxSource);

        if (obj instanceof JAXBElement)
            data = ((JAXBElement<Object>) obj).getValue();
        else data = obj;

        logger.debug(String.format("isValidating %s", validationEvent.isValidating()));

        // check for events
        if (vec.hasEvents()) {
            // iterate over events
        }

    }


    public JaxbUnmarshal(XMLStreamReader xmlStreamReader, Class[] classType) throws JAXBException {
        logger.debug(String.format("read XMLStreamReader %s", xmlStreamReader));
        JAXBContext jc = JAXBContext.newInstance(classType);
        Unmarshaller u = jc.createUnmarshaller();

        Object obj = u.unmarshal(xmlStreamReader);

        if (obj instanceof JAXBElement)
            data = ((JAXBElement<Object>) obj).getValue();
        else data = obj;

        logger.debug(String.format("isValidating %s", validationEvent.isValidating()));

    }

    public JaxbUnmarshal(XMLEventReader xmlEventReader, Class[] classType) throws JAXBException {
        logger.debug(String.format("read XMLEventReader %s", xmlEventReader));
        JAXBContext jc = JAXBContext.newInstance(classType);
        Unmarshaller u = jc.createUnmarshaller();

        Object obj = u.unmarshal(xmlEventReader);

        if (obj instanceof JAXBElement)
            data = ((JAXBElement<Object>) obj).getValue();
        else data = obj;

        logger.debug(String.format("isValidating %s", validationEvent.isValidating()));

    }

    public Object getObject() {
        return data;
    }

    public boolean isValidating() {
        return validationEvent.isValidating();
    }


    public ValidationHandler getValidationEvent() {
        return validationEvent;
    }
}
