package org.EDM.Transformations.formats.memorix;

import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author amartinez
 */
public class MEMORIX2EDM extends XSLTTransformations {

    private static Logger logger = LogManager.getLogger(MEMORIX2EDM.class);

    private String identifier;
    private Map<String, String> properties = new HashMap<>();


    public MEMORIX2EDM(String identifier, String xslt, Map<String, String> properties, OutputStream outs) throws Exception {
        super(xslt, outs, properties);
        this.identifier = identifier;
        this.properties = properties;
    }


}
