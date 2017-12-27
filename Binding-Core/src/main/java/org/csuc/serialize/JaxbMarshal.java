/**
 * 
 */
package org.csuc.serialize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author amartinez
 *
 */
public class JaxbMarshal {

	
	private static Logger logger = LogManager.getLogger(JaxbMarshal.class);

	private Marshaller marshaller;
	private Object object;


	public JaxbMarshal(Object object, Class<?> classType) {
		try {
			JAXBContext oaidcContext = JAXBContext.newInstance(classType);
			this.marshaller = oaidcContext.createMarshaller();
			this.object = object;
		} catch (Exception e) {
			logger.error(e);
		}

	}


	public void marshaller(OutputStream stream) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(object, stream);
	}

	public void marshaller(OutputStream stream, Charset charset, boolean formatted, boolean fragment) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, fragment);
		marshaller.marshal(object, stream);
	}

	public void marshaller(Writer writer) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(object, writer);
	}

	public void marshaller(Node node) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(object, node);
	}
}
