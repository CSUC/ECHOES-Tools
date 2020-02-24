package org.transformation.factory;

import org.csuc.util.FormatType;
import org.edm.transformations.formats.utils.SchemaType;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Transformation  {

    void console(SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException, XMLStreamException;

    void path(Path out, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException, XMLStreamException;

    void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException, URISyntaxException, XMLStreamException;

    List<Throwable> getExceptions();
}
