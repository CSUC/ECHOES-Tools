package org.csuc.analyse.core.strategy;

import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @author amartinez
 *
 */
public interface ParserMethod {

    void parser(String fileOrPath) throws Exception;
    void parser(URL url) throws Exception;

    void createXML(OutputStream outs);
    void createHDFS_XML(String uri, String user, String home, Path dest) throws IOException;

    void createJSON(OutputStream outs);
    void createHDFS_JSON(String uri, String user, String home, Path dest) throws IOException;

}
