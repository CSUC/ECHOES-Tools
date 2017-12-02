package org.csuc.Parser.Core.strategy.xom;

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
public class Xom implements ParserMethod {

    private Logger logger = LogManager.getLogger(Xom.class);

    public Xom(){

    }

    @Override
    public void parser(String fileOrPath) {

    }

    @Override
    public void parser(URL url) {

    }

    @Override
    public List<XPATH> createXPATHResult() {
       return null;
    }

    @Override
    public Map<String, String> createNamespaceResult() {
        return null;
    }
}
