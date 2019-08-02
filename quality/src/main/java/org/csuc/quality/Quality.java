package org.csuc.quality;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.format.FormatInterface;

public class Quality {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    private FormatInterface formatInterface;

    public Quality(FormatInterface formatInterface) {
        this.formatInterface = formatInterface;
    }

    public FormatInterface getFormatInterface() {
        return formatInterface;
    }

    public void setFormatInterface(FormatInterface formatInterface) {
        this.formatInterface = formatInterface;
    }
}
