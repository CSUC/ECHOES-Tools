package org.csuc.Parser.Core.strategy;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amartinez
 *
 */
public interface ParserMethod {

    Map<String,String> values = new HashMap<>();
    Map<String,String> namespaces = new HashMap<>();

    void parser(String fileOrPath);
    void parser(URL url);

    List<XPATH> createXPATHResult();
    Map<String, String> createNamespaceResult();
}
