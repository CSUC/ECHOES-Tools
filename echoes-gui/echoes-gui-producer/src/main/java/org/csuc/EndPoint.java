package org.csuc;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.producer.RabbitMQConfig;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public abstract class EndPoint{

    private Logger logger = LogManager.getLogger(EndPoint.class);

    protected Channel channel;
    protected Connection connection;
    protected ConnectionFactory factory;
    protected String endPointName;


    /**
     *
     * @param endpointName
     * @throws IOException
     * @throws TimeoutException
     */
    public EndPoint(String endpointName, RabbitMQConfig typesafeRabbitMQ) throws IOException, TimeoutException {
        this.endPointName = endpointName;
        factory = new ConnectionFactory();

        factory.setHost(typesafeRabbitMQ.getHost());
        factory.setPort(typesafeRabbitMQ.getPort());
        factory.setUsername(typesafeRabbitMQ.getUsername());
        factory.setPassword(typesafeRabbitMQ.getPassword());
        factory.setConnectionTimeout(0);
        factory.setRequestedHeartbeat(0);
        // Configure automatic reconnections
        factory.setAutomaticRecoveryEnabled(true);

        // Recovery interval: 10s
        factory.setNetworkRecoveryInterval(10000);

        // Exchanges and so on should be redeclared if necessary
        factory.setTopologyRecoveryEnabled(true);
        ExecutorService es = Executors.newFixedThreadPool(4);
        connection = factory.newConnection(es);

        connection.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                logger.error("shutdown signal received", cause);
            }
        });

        channel = connection.createChannel();

        channel.basicRecover();
        channel.queueDeclare(endpointName, false, false, false, null);

    }


    /**
     * Close channel and connection. Not necessary as it happens implicitly any way.
     * @throws IOException
     * @throws TimeoutException
     */
    public void close() throws IOException, TimeoutException{
        this.channel.close();
        this.connection.close();
    }
}
