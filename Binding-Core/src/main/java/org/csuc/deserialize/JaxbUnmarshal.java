/**
 * 
 */
package org.csuc.deserialize;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

import javax.xml.XMLConstants;
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
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * @author amartinez
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class JaxbUnmarshal {

	private static Logger logger = LogManager.getLogger(JaxbUnmarshal.class);
	
	private Object data;
    private ValidationHandler validationEvent = new ValidationHandler();

	public JaxbUnmarshal(File file, Class[] classType) {
		logger.debug(String.format("read file %s", file));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
			Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(validationEvent);
			u.setSchema(addSchema(classType));

			Object obj = u.unmarshal(file);

			if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
			else    data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		} catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal file %s", e));
		}
	}
	
	public JaxbUnmarshal(InputStream inputStream, Class[] classType) {
		try {
		    JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(validationEvent);
			u.setSchema(addSchema(classType));

            Object obj = u.unmarshal(inputStream);
            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else   data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		} catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal InputStream %s", e));
		}
	}
	
	
	public JaxbUnmarshal(URL url, Class[] classType) {
		logger.debug(String.format("read url %s", url));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(validationEvent);
			u.setSchema(addSchema(classType));

            Object obj = u.unmarshal(url);

            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		} catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal URL %s", e));
		}
	}
	
	public JaxbUnmarshal(StringBuffer stringbuffer, Class[] classType) {
		logger.debug(String.format("read StringBuffer %s", stringbuffer));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
            u.setEventHandler(validationEvent);
			u.setSchema(addSchema(classType));

            Object obj = u.unmarshal(new StreamSource(new StringReader(stringbuffer.toString())));

            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		} catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal StringBuffer %s", e));
		}	
	}
	
	public JaxbUnmarshal(Node node, Class[] classType) throws IOException {
		logger.debug(String.format("read Node %s", node));
		try {			
		    Source xmlSource = new DOMSource(node);
		    Unmarshaller u = JAXBContext.newInstance(classType).createUnmarshaller();
            u.setEventHandler(validationEvent);
			u.setSchema(addSchema(classType));

            Object obj = u.unmarshal(xmlSource);

            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		}catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal Node %s", e));
		}			
	}
	
	@SuppressWarnings("deprecation")
	public JaxbUnmarshal(SAXSource saxSource, Class[] classType) {
		logger.debug(String.format("read SAXSource %s", saxSource));
		try {			
			// configure a validating SAX2.0 parser (Xerces2)
			final String JAXP_SCHEMA_LANGUAGE =
	           "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
			final String JAXP_SCHEMA_LOCATION =
	           "http://java.sun.com/xml/jaxp/properties/schemaSource";
			final String W3C_XML_SCHEMA =
	           "http://www.w3.org/2001/XMLSchema";

			System.setProperty( "javax.xml.parsers.SAXParserFactory",
	                           "org.apache.xerces.jaxp.SAXParserFactoryImpl" );

			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			spf.setValidating(true);
			SAXParser saxParser = spf.newSAXParser();

			try {
				saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
				saxParser.setProperty(JAXP_SCHEMA_LOCATION, "http://....");
			} catch (SAXNotRecognizedException x) {
	           // exception handling omitted
			}

//			XMLReader xmlReader = saxParser.getXMLReader();
//			SAXSource source =
//					new SAXSource( xmlReader, new InputSource( "http://..." ) );

			// Setup JAXB to unmarshal
			JAXBContext jc = JAXBContext.newInstance(classType);
			Unmarshaller u = jc.createUnmarshaller();
			ValidationEventCollector vec = new ValidationEventCollector();
			u.setEventHandler( vec );
			u.setSchema(addSchema(classType));

			// turn off the JAXB provider's default validation mechanism to
			// avoid duplicate validation
			u.setValidating( false );

			// unmarshal
            Object obj = u.unmarshal(saxSource);

            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
			else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));

			// check for events
			if( vec.hasEvents() ) {
				// iterate over events
			}
		}catch (JAXBException | ParserConfigurationException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal SAXSource %s", e));
		}		
	}
	
	
	public JaxbUnmarshal(XMLStreamReader xmlStreamReader, Class[] classType) {
		logger.debug(String.format("read XMLStreamReader %s", xmlStreamReader));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
			u.setSchema(addSchema(classType));

            Object obj = u.unmarshal(xmlStreamReader);

            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		} catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal XMLStreamReader %s", e));
		}		
	}

	public JaxbUnmarshal(XMLEventReader xmlEventReader, Class[] classType) {
		logger.debug(String.format("read XMLEventReader %s", xmlEventReader));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
			u.setSchema(addSchema(classType));

            Object obj = u.unmarshal(xmlEventReader);

            if(obj instanceof JAXBElement)
                data = ((JAXBElement<Object>) obj).getValue();
            else data = obj;

            logger.debug(String.format("isValidating %s", validationEvent.isValidating() ));
		}catch (JAXBException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal XMLEventReader %s", e));
		}			
	}
	
	public Object getObject() {
		return data;
	}

    public boolean isValidating() {
        return validationEvent.isValidating();
    }

    private Schema addSchema(Class[] classType) throws SAXException {
		List<Source> list = new ArrayList<>();

		Stream.of(classType).forEach(type ->{
			if(Objects.equals(type, A2AType.class))	list.add(new StreamSource(A2AType.class.getClassLoader().getResource("A2AAllInOne_v.1.7.xsd").toExternalForm()));
			if(Objects.equals(type, OaiDcType.class)) list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("oai_dc.xsd").toExternalForm()));
			if(Objects.equals(type, Ead.class)) list.add(new StreamSource(Ead.class.getClassLoader().getResource("apeEAD.xsd").toExternalForm()));
			if(Objects.equals(type, Memorix.class)) list.add(new StreamSource(Memorix.class.getClassLoader().getResource("MRX-API-ANY.xsd").toExternalForm()));
			if(Objects.equals(type, OAIPMHtype.class)) list.add(new StreamSource(OAIPMHtype.class.getClassLoader().getResource("OAI-PMH.xsd").toExternalForm()));
		});

		Source[] schemaFiles = list.toArray(new Source[list.size()]);
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		return sf.newSchema( schemaFiles );
	}
}
