/**
 * 
 */
package org.csuc.jaxb;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class Validate {
	
	private static Logger logger = LogManager.getLogger(Validate.class.getName());
	
	private UnmarshalEDM edm;
	
	
	public Validate(URL url) {
		edm = new UnmarshalEDM(url);
	}
	
	public Validate(String file) {
		edm = new UnmarshalEDM(file);
	}

	
	public boolean isValid(){
		return edm.getEdmValidationEventHandler().isEmpty() ? true : false;		
	}
	
	public void showErrors(){
		edm.getEdmValidationEventHandler().forEach(logger::info);
	}
}
