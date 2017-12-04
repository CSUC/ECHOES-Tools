package org.csuc.Parser.Core.strategy;

import java.io.OutputStream;
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

    void parser(String fileOrPath);
    void parser(URL url);

    void createXML(OutputStream outs);
    void createJSON(OutputStream outs);
}
