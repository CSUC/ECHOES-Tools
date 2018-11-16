package org.csuc;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.consumer.RabbitMQConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public class Producer extends EndPoint {

    private Logger logger = LogManager.getLogger(Producer.class);

    /**
     *
     * @param endPointName
     * @throws IOException
     * @throws TimeoutException
     */
    public Producer(String endPointName, RabbitMQConfig config) throws IOException, TimeoutException {
        super(endPointName, config);
    }
    /**
     *
     * @param object
     * @throws IOException
     */
    public void sendMessage(Serializable object) throws IOException {
        channel.basicPublish("", endPointName, null, SerializationUtils.serialize(object));
        logger.info("[x] Sent '" +(HashMap<?,?>) object + "'");
    }
}
