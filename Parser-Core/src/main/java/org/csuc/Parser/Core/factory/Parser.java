package org.csuc.Parser.Core.factory;

import org.csuc.Parser.Core.strategy.XPATH;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amartinez
 */
public interface Parser {


    void execute(String fileOrPath);
    void execute(URL url);

    List<XPATH> getXPATHResult();
    Map<String, String> getNamespaceResult();
}
