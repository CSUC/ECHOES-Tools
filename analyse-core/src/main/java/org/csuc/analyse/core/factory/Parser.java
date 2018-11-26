package org.csuc.analyse.core.factory;

import org.apache.hadoop.fs.Path;

import java.io.IOException;
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
    void HDFS_XML(String uri, String user, String home, Path dest) throws IOException;

    void JSON(OutputStream outs);
    void HDFS_JSON(String uri, String user, String home, Path dest) throws IOException;
}
