package org.csuc.util.format;

import java.io.IOException;
import java.nio.file.Path;

public interface FormatInterface {

    void DATASTORE(String host, int port, String database) throws IOException;

    void JSON(Path out) throws IOException;

    void XML(Path out) throws IOException;
}
