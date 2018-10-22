package org.csuc.typesafe.consumer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author amartinez
 */
public class ConsumerConfig extends ConfigBeanFactory {

    private static Logger logger = LogManager.getLogger(ConsumerConfig.class);

    private Path filename;

    public ConsumerConfig(Path filename) {
        this.filename = filename;
    }

    public RabbitMQConfig getRabbitMQConfig(){
        Config defaultConfig = ConfigFactory.parseResources("rabbitmq.defaults.conf");
        if(Objects.nonNull(filename) && Files.exists(filename)){
            logger.debug("load config {}", filename);
            Config fallbackConfig = ConfigFactory.parseFile(filename.toFile())
                    .withFallback(defaultConfig)
                    .resolve();
            RabbitMQConfig config = create(fallbackConfig.getConfig("rabbitmq"), RabbitMQConfig.class);
            return config;
        }else{
            logger.debug("load config defaults.conf");
            RabbitMQConfig config = create(defaultConfig.getConfig("rabbitmq"), RabbitMQConfig.class);
            return config;
        }
    }

}
