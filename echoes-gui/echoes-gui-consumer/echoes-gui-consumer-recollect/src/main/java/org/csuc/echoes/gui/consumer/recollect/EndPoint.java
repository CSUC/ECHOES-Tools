package org.csuc.echoes.gui.consumer.recollect;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public abstract class EndPoint {

    private Logger logger = LogManager.getLogger(EndPoint.class);

    protected Channel channel;
    protected Connection connection;
    protected ConnectionFactory factory;
    protected String endPointName;

    protected Config typesafeRabbitMQ;

    protected ExecutorService threadPool;

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public EndPoint(Config typesafeRabbitMQ) throws IOException, TimeoutException {
        this.typesafeRabbitMQ = typesafeRabbitMQ;
        this.endPointName = typesafeRabbitMQ.getString("endpoint");
        factory = new ConnectionFactory();

        factory.setHost(typesafeRabbitMQ.getString("host"));
        factory.setPort(typesafeRabbitMQ.getInt("port"));
        factory.setUsername(typesafeRabbitMQ.getString("username"));
        factory.setPassword(typesafeRabbitMQ.getString("password"));
        factory.setConnectionTimeout(0);
        factory.setRequestedHeartbeat(0);
        // Configure automatic reconnections
        factory.setAutomaticRecoveryEnabled(true);

        // Recovery interval: 10s
        factory.setNetworkRecoveryInterval(10000);

        // Exchanges and so on should be redeclared if necessary
        factory.setTopologyRecoveryEnabled(true);
        threadPool = Executors.newFixedThreadPool(typesafeRabbitMQ.getInt("Qos"));
        connection = factory.newConnection(threadPool);

        connection.addShutdownListener(cause -> logger.error("shutdown signal received", cause));

        channel = connection.createChannel();

        channel.basicRecover();
        channel.queueDeclare(endPointName,
                typesafeRabbitMQ.getBoolean("durable"),
                typesafeRabbitMQ.getBoolean("exclusive"),
                typesafeRabbitMQ.getBoolean("autoDelete"),
                typesafeRabbitMQ.getIsNull("arguments") ? null : typesafeRabbitMQ.getObject("arguments").unwrapped());
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
