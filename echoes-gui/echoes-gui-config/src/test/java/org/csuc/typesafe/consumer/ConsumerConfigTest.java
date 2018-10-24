package org.csuc.typesafe.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ConsumerConfigTest {

    private static Logger logger = LogManager.getLogger(ConsumerConfigTest.class);

    @Test
    public void getRabbitMQConfig() {
        RabbitMQConfig rabbitMQConfig = new ConsumerConfig(null).getRabbitMQConfig();

        assertNotNull(rabbitMQConfig);

        assertEquals("localhost", rabbitMQConfig.getHost());
        assertEquals(5672, rabbitMQConfig.getPort());

        assertEquals("guest", rabbitMQConfig.getUsername());
        assertEquals("guest", rabbitMQConfig.getPassword());

        assertEquals(15672, rabbitMQConfig.getPortManagement());

        assertEquals("parser", rabbitMQConfig.getParserQueue());
        assertEquals("recollect", rabbitMQConfig.getRecollectQueue());
        assertEquals("validation", rabbitMQConfig.getValidationQueue());
        assertEquals("zip", rabbitMQConfig.getZipQueue());

        logger.info(rabbitMQConfig.toString());
    }
}