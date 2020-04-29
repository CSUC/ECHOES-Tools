package org.csuc.format;

import org.apache.hadoop.fs.FileSystem;

import java.net.URL;
import java.nio.file.Path;

public interface FormatInterface {

    void execute(Path path) throws Exception;

    void execute(URL url) throws Exception;

    void execute(FileSystem fileSystem, org.apache.hadoop.fs.Path path) throws Exception;

}
