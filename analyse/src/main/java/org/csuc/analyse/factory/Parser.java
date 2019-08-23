package org.csuc.analyse.factory;

import org.apache.hadoop.fs.FileSystem;
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
    void HDFS_XML(FileSystem fileSystem, Path dest) throws IOException;

    void JSON(OutputStream outs);
    void HDFS_JSON(FileSystem fileSystem, Path dest) throws IOException;
}
