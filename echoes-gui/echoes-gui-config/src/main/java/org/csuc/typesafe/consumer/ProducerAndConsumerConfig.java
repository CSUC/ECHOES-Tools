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
public class ProducerAndConsumerConfig extends ConfigBeanFactory {

    private static Logger logger = LogManager.getLogger(ProducerAndConsumerConfig.class);

    private Path filename;

    public ProducerAndConsumerConfig(Path filename) {
        this.filename = filename;
    }

    public Queues getRabbitMQConfig(){
        Config defaultConfig = ConfigFactory.parseResources("rabbitmq.defaults.conf");
        if(Objects.nonNull(filename) && Files.exists(filename)){
            logger.debug("load config {}", filename);
            Config fallbackConfig = ConfigFactory.parseFile(filename.toFile())
                    .withFallback(defaultConfig)
                    .resolve();
            return new Queues(fallbackConfig.getConfig("rabbitmq.queues"));
        }else{
            logger.debug("load config defaults.conf");
            return new Queues(defaultConfig.getConfig("rabbitmq.queues"));
        }
    }

}
