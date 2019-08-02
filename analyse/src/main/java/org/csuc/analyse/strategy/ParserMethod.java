package org.csuc.analyse.strategy;

import org.apache.hadoop.fs.FileSystem;
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
    void createHDFS_XML(FileSystem fileSystem, Path dest) throws IOException;

    void createJSON(OutputStream outs);
    void createHDFS_JSON(FileSystem fileSystem, Path dest) throws IOException;

}
