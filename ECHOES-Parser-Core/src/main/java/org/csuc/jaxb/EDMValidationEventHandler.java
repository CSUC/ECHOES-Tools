/**
 * 
 */
package org.csuc.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;


/**
 * @author amartinez
 *
 */
public class EDMValidationEventHandler implements ValidationEventHandler{

	 
	private List<String> validationEventHandler = new ArrayList<String>();
	
	@Override
	public boolean handleEvent(ValidationEvent event) {
		if (event.getSeverity() == ValidationEvent.FATAL_ERROR || event .getSeverity() == ValidationEvent.ERROR){			
	        ValidationEventLocator  locator = event.getLocator();
	        
	        validationEventHandler.add(
		        	String.format("Line: %s\nColumn: %s\nMessage: %s",
		        			locator.getLineNumber(), locator.getColumnNumber(), event.getMessage()));
	    }
	    return true;
	}
	
	public List<String> getValidationEventHandler() {
		return validationEventHandler;
	}
}
