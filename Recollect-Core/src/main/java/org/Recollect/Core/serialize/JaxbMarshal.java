/**
 * 
 */
package org.Recollect.Core.serialize;

import java.io.OutputStream;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class JaxbMarshal {

	private static Logger logger = LogManager.getLogger(JaxbMarshal.class);
	
	private Marshaller marshaller;
	private JAXBElement<?> jaxbElement;
	private OutputStream stream;
	
	public JaxbMarshal(JAXBElement<?> jaxbElement, Class<?> classType, OutputStream stream) throws Exception {
		if(Objects.isNull(jaxbElement)) throw new Exception("JaxbMarshal requires jaxbElement");
		if(Objects.isNull(classType))	throw new Exception("JaxbMarshal requires classType");
		if(Objects.isNull(stream))	throw new Exception("JaxbMarshal requires OutputStream");
		
		try {			
			JAXBContext oaidcContext = JAXBContext.newInstance(classType);
			this.marshaller = oaidcContext.createMarshaller();
			this.jaxbElement = jaxbElement;
			this.stream = stream;
			
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	
	public void marshal(String schema_location, boolean formatted_output, boolean fragment, String encoding) {		
		try {			
			if(Objects.nonNull(schema_location))	marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schema_location);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted_output);
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        if(Objects.nonNull(encoding))	marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			
	        marshaller.marshal(jaxbElement, stream);
		}catch(Exception e) {
			logger.error(e);
		}
	}
	
	
}
