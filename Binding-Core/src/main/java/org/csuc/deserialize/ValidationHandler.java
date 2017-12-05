package org.csuc.deserialize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class ValidationHandler implements ValidationEventHandler {

    private static Logger logger = LogManager.getLogger(ValidationHandler.class);

    private boolean isValidating = true;

    @Override
    public boolean handleEvent(ValidationEvent event) {

        if (event.getSeverity() == ValidationEvent.FATAL_ERROR || event .getSeverity() == ValidationEvent.ERROR
                || event.getSeverity() == ValidationEvent.WARNING){

            ValidationEventLocator locator = event.getLocator();

            logger.info(String.format("lineNumber: %s\ncolumnNumber: %s\nmessage: %s",
                    locator.getLineNumber(), locator.getColumnNumber(), event.getMessage()));
            isValidating = false;
        }
        return true;
    }

    public boolean isValidating() {
        return isValidating;
    }
}
