package org.transformation.factory;

import org.csuc.util.FormatType;
import org.edm.transformations.formats.utils.SchemaType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;

public interface Transformation {

    void console(SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException;

    void path(Path out, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException;

    void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException;

}
