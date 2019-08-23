package org.csuc.format;

import java.net.URL;
import java.nio.file.Path;

public interface FormatInterface {

    void execute(Path path) throws Exception;

    void execute(URL url) throws Exception;

}
