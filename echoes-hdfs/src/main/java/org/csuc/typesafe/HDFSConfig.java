package org.csuc.typesafe;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author amartinez
 */
public class HDFSConfig {

    private Config config;

    private HDFSConfig(Path filename) {
        Config defaultConfig = ConfigFactory.parseResources("echoes-hdfs-defaults.conf");
        if (Objects.nonNull(filename) && Files.exists(filename)) {
            Config fallbackConfig = ConfigFactory.parseFile(filename.toFile())
                    .withFallback(defaultConfig)
                    .resolve();

            this.config = fallbackConfig;
        } else  this.config = defaultConfig.resolve();

    }

    public static HDFSConfig config(Path filename){
        return new HDFSConfig(filename);
    }

    public String getUri() {
        return config.getString("hdfs.uri");
    }

    public String getUser() {
        return config.getString("hdfs.user");
    }

    public String getHome() {
        return config.getString("hdfs.home");
    }

    public Object get(String key){
        return config.getString(key);
    }
}
