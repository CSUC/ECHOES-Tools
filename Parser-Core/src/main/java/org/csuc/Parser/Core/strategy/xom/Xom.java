package org.csuc.Parser.Core.strategy.xom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;

import java.io.OutputStream;
import java.net.URL;

/**
 * @author amartinez
 */
public class Xom implements ParserMethod {

    private Logger logger = LogManager.getLogger(Xom.class);

    public Xom(){
        logger.info(String.format("Method: %s", getClass().getSimpleName()));
    }

    @Override
    public void parser(String fileOrPath) {

    }

    @Override
    public void parser(URL url) {

    }

    @Override
    public void createXML(OutputStream outs) {

    }

    @Override
    public void createJSON(OutputStream outs) {

    }


}
