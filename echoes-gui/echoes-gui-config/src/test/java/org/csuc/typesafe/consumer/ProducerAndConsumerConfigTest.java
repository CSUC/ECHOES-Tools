package org.csuc.typesafe.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProducerAndConsumerConfigTest {

    private static Logger logger = LogManager.getLogger(ProducerAndConsumerConfigTest.class);

    @Test
    public void getRabbitMQConfig() {
        RabbitMQConfig rabbitMQConfig = new ProducerAndConsumerConfig(null).getRabbitMQConfig();

        assertNotNull(rabbitMQConfig);

        assertEquals("localhost", rabbitMQConfig.getHost());
        assertEquals(5672, rabbitMQConfig.getPort());

        assertEquals("guest", rabbitMQConfig.getUsername());
        assertEquals("guest", rabbitMQConfig.getPassword());

        assertEquals(15672, rabbitMQConfig.getPortManagement());

        assertEquals("analyse", rabbitMQConfig.getQueues().getAnalyse());
        assertEquals("recollect", rabbitMQConfig.getQueues().getRecollect());
        assertEquals("validation", rabbitMQConfig.getQueues().getValidation());
        assertEquals("zip", rabbitMQConfig.getQueues().getZip());

        logger.info(rabbitMQConfig.toString());
    }
}