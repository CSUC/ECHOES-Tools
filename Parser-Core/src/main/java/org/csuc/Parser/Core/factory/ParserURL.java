package org.csuc.Parser.Core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.strategy.XPATH;

import java.net.URL;
import java.util.List;
import java.util.Map;

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
    public List<XPATH> getXPATHResult() {
        return method.createXPATHResult();
    }

    @Override
    public Map<String, String> getNamespaceResult() {
        return method.createNamespaceResult();
    }
}
