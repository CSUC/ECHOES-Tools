package org.csuc.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author amartinez
 */
public class FileUtils {

    private static Logger logger = LogManager.getLogger(FileUtils.class);

    /**
     * @param source
     * @param target
     * @throws IOException
     */
    public static void move(Path source, Path target) throws IOException {
        Files.move(source, Paths.get(target + File.separator + source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        logger.debug("Files move from {} to {}", source, Paths.get(target + File.separator + source.getFileName()));
    }

    /**
     * @param source
     * @param target
     * @throws IOException
     */
    public static void copy(Path source, Path target) throws IOException {
        Files.copy(source, Paths.get(target + File.separator + source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        logger.debug("Files move from {} to {}", source, Paths.get(target + File.separator + source.getFileName()));
    }
}
