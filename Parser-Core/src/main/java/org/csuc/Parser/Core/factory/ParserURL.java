package org.csuc.Parser.Core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;

import java.io.OutputStream;
import java.net.URL;

/**
 * @author amartinez
 */
public class ParserURL implements Parser {

    private static Logger logger = LogManager.getLogger(ParserURL.class);

    private ParserMethod method;

    public ParserURL(ParserMethod method){
        logger.info(String.format("Parser: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public void execute(String fileOrPath) {
        throw new IllegalArgumentException("execute fileOrPath is not valid for ParserURL!");
    }

    @Override
    public void execute(URL url) {
        method.parser(url);
    }

    @Override
    public void XML(OutputStream outs) {
        method.createXML(outs);
    }

    @Override
    public void JSON(OutputStream outs) {
        method.createJSON(outs);
    }

}
