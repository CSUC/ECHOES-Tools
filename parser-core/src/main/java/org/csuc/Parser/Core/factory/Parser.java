package org.csuc.Parser.Core.factory;

import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @author amartinez
 */
public interface Parser {


    void execute(String fileOrPath) throws Exception;
    void execute(URL url)  throws Exception;

    void XML(OutputStream outs);
    void JSON(OutputStream outs);
}
