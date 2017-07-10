/**
 * 
 */
package org.csuc.jaxb;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openarchives.oai._2.OAIPMHtype;

/**
 * @author amartinez
 *
 */
public class UnmarshalOAIPMH {

	private static Logger logger = LogManager.getLogger(UnmarshalOAIPMH.class.getName());
	
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;
	private JAXBElement<OAIPMHtype> jaxb;
	
	
	@SuppressWarnings("unchecked")
	public UnmarshalOAIPMH(URL url) {		
		try {
			jaxbContext = JAXBContext.newInstance(OAIPMHtype.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	        
	        jaxb = (JAXBElement<OAIPMHtype>) jaxbUnmarshaller.unmarshal(url);
		} catch (JAXBException e) {
			logger.error(e);			
		}        
	}
	
	@SuppressWarnings("unchecked")
	public UnmarshalOAIPMH(String file) {
		try {
			jaxbContext = JAXBContext.newInstance(OAIPMHtype.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			File xml = new File(file);
			
			jaxb = (JAXBElement<OAIPMHtype>) jaxbUnmarshaller.unmarshal(xml);	        
		} catch (JAXBException e) {
			logger.error(e);			
		} 
	}
	
	public OAIPMHtype getOaipmh() {
		return (jaxb != null) ? jaxb.getValue() : null;
	}
	
}
