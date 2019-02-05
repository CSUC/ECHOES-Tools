package org.csuc.typesafe.consumer;

import com.typesafe.config.ConfigValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProducerAndConsumerConfigTest {

    private static Logger logger = LogManager.getLogger(ProducerAndConsumerConfigTest.class);

    @Test
    public void getRabbitMQConfig() {
        Queues rabbitMQConfig = new ProducerAndConsumerConfig(null).getRabbitMQConfig();

        assertNotNull(rabbitMQConfig);

        assertEquals("localhost", rabbitMQConfig.getAnalyse().getString("host"));
        assertEquals(5672, rabbitMQConfig.getAnalyse().getInt("port"));

        assertEquals("analyse", rabbitMQConfig.getAnalyse().getString("endpoint"));
        assertEquals("recollect", rabbitMQConfig.getRecollect().getString("endpoint"));
        assertEquals("validation", rabbitMQConfig.getValidation().getString("endpoint"));
        assertEquals("zip", rabbitMQConfig.getZip().getString("endpoint"));


        System.out.println(rabbitMQConfig.getAnalyse().getIsNull("arguments") ? null : rabbitMQConfig.getAnalyse().getObject("arguments").unwrapped());
    }
}