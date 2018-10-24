package org.csuc.typesafe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.consumer.ConsumerConfig;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsumerConfigTest {

    private static Logger logger = LogManager.getLogger(ConsumerConfigTest.class);

    @Test
    public void getRabbitMQConfig() {
        RabbitMQConfig config = new ConsumerConfig(null).getRabbitMQConfig();

        assertEquals("localhost", config.getHost());
        assertEquals(5672, config.getPort());
        assertEquals("guest", config.getUsername());
        assertEquals("guest", config.getPassword());
        assertEquals(15672, config.getPortManagement());
        assertEquals("parser", config.getParserQueue());
        assertEquals("recollect", config.getRecollectQueue());
        assertEquals("validation", config.getValidationQueue());

        logger.info("host:  {}", config.getHost());
        logger.info("port:  {}", config.getPort());
        logger.info("username:  {}", config.getUsername());
        logger.info("password:  {}", config.getPassword());
        logger.info("portManagement:  {}", config.getPortManagement());
        logger.info("parserQueue:  {}", config.getParserQueue());
        logger.info("recollectQueue:  {}", config.getRecollectQueue());
        logger.info("validationQueue:  {}", config.getValidationQueue());

    }
}