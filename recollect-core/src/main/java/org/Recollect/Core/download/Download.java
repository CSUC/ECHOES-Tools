package org.Recollect.Core.download;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author amartinez
 *
 */
public interface Download {

    void execute(Map<String, String> properties) throws Exception;

    void execute(Path outs, Map<String, String> properties) throws Exception;

}
