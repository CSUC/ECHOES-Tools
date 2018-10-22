package org.csuc.typesafe.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.consumer.ConsumerConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author amartinez
 */
public class ServerConfig extends ConfigBeanFactory {

    private static Logger logger = LogManager.getLogger(ConsumerConfig.class);

    private Path filename;

    public ServerConfig(Path filename) {
        this.filename = filename;
    }

    public Application getConfig(){
        Config defaultConfig = ConfigFactory.parseResources("echoes-gui-server.conf");
        if(Objects.nonNull(filename) && Files.exists(filename)){
            logger.debug("load config {}", filename);
            Config fallbackConfig = ConfigFactory.parseFile(filename.toFile())
                    .withFallback(defaultConfig)
                    .resolve();
            Application config = create(fallbackConfig.getConfig("echoes-gui-server"), Application.class);
            return config;
        }else{
            logger.debug("load config defaults.conf");
            Application config = create(defaultConfig.getConfig("echoes-gui-server"), Application.class);
            return config;
        }
    }

}
