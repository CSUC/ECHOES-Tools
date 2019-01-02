package org.csuc.service.injection;

import org.csuc.typesafe.consumer.ProducerAndConsumerConfig;
import org.csuc.typesafe.consumer.Queues;
import org.glassfish.hk2.api.Factory;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * @author amartinez
 */
public class RabbitMQBinderFactory implements Factory<Queues> {

    private URL rabbitmqResource = getClass().getClassLoader().getResource("rabbitmq.conf");

    @Override
    public Queues provide() {
        return new ProducerAndConsumerConfig((Objects.isNull(rabbitmqResource)) ? null : new File(rabbitmqResource.getFile()).toPath()).getRabbitMQConfig();
    }

    @Override
    public void dispose(Queues application) {

    }
}
