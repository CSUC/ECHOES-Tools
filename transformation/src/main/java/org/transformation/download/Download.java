package org.transformation.download;

import org.apache.hadoop.fs.FileSystem;
import org.csuc.util.FormatType;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author amartinez
 *
 */
public interface Download {

    void execute(Map<String, String> properties) throws Exception;

    void execute(Map<String, String> properties, FormatType formatType) throws Exception;

    void execute(Path outs, Map<String, String> properties) throws Exception;

    void execute(Path outs, Map<String, String> properties, FormatType formatType) throws Exception;

    // Hadoop FS
    void execute(FileSystem fileSystem, org.apache.hadoop.fs.Path dest, Map<String, String> properties) throws Exception;

    void execute(FileSystem fileSystem, org.apache.hadoop.fs.Path dest, Map<String, String> properties, FormatType formatType) throws Exception;
}
