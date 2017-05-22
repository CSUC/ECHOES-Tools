/**
 * 
 */
package org.csuc.jaxb;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.OAIPMHtype;

import eu.europeana.schemas.edm.RDF;

/**
 * @author amartinez
 *
 */
public class UnmarshalEDM {
	
	private static Logger logger = LogManager.getLogger(UnmarshalEDM.class.getName());
	
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;
	private JAXBElement<RDF> jaxb;
	
	private EDMValidationEventHandler edmValidationEventHandler;
	
	@SuppressWarnings("unchecked")
	public UnmarshalEDM(URL url) {
		try {
			jaxbContext = JAXBContext.newInstance(RDF.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(edmValidationEventHandler = new EDMValidationEventHandler());
			
	        jaxb = (JAXBElement<RDF>) jaxbUnmarshaller.unmarshal(url);
		} catch (JAXBException e) {
			logger.error(e);			
		}  
	}

	@SuppressWarnings("unchecked")
	public UnmarshalEDM(String file) {
		try {
			jaxbContext = JAXBContext.newInstance(OAIPMHtype.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(edmValidationEventHandler = new EDMValidationEventHandler());
			
			File xml = new File(file);
			
			jaxb = (JAXBElement<RDF>) jaxbUnmarshaller.unmarshal(xml);	        
		} catch (JAXBException e) {
			logger.error(e);			
		} 
	}
	
	public RDF getRDF() {
		return jaxb.getValue();
	}
	
	public List<String> getEdmValidationEventHandler() {
		return edmValidationEventHandler.getValidationEventHandler();
	}
	
	
}
