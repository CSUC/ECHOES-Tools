package org.Recollect.Core.download;

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
}
