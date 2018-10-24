package org.EDM.Transformations.formats.xslt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amartinez
 */
public class XSLTErrorListener implements ErrorListener {

    private List<TransformerException> errors = new ArrayList<>();

    private static Logger logger = LogManager.getLogger(XSLTErrorListener.class);

    @Override
    public void warning(TransformerException e) throws TransformerException {
        logger.error(e.getMessageAndLocation());
        errors.add(e);
    }

    @Override
    public void error(TransformerException e) throws TransformerException {
        logger.error(e.getMessageAndLocation());
        errors.add(e);
    }

    @Override
    public void fatalError(TransformerException e) throws TransformerException {
        logger.error(e.getMessageAndLocation());
        errors.add(e);
    }

    public List<TransformerException> getErrors() {
        return !errors.isEmpty() ? errors : null;
    }
}
