package org.csuc.typesafe;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Objects;

public class QualityConfig {

    private static Logger logger = LogManager.getLogger(QualityConfig.class);

    private Path filename;

    public QualityConfig(Path filename) {
        this.filename = Objects.requireNonNull(filename, "Path filename must not be null");
    }

    public Config getQualityConfig() {
        return ConfigFactory.parseFile(filename.toFile()).resolve();
    }
}
