package org.csuc.echoes.gui.consumer.loader.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.echoes.gui.consumer.loader.LoaderQueueConsumer;
import org.csuc.typesafe.consumer.ProducerAndConsumerConfig;
import org.csuc.typesafe.consumer.Queues;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class Consumer {

    private static Logger logger = LogManager.getLogger(Consumer.class);

    private static ArgsBean bean;

    public static void main(String[] args) {
        bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        URL applicationResource = Consumer.class.getClassLoader().getResource("rabbitmq.conf");
        Queues rabbitMQConfig = new ProducerAndConsumerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getRabbitMQConfig();

        logger.info(rabbitMQConfig);

        try {
            LoaderQueueConsumer analyseQueueConsumer = new LoaderQueueConsumer(rabbitMQConfig.getLoader());
            Stream.of(analyseQueueConsumer).forEach(consumer-> new Thread(consumer).start());
        } catch (IOException | TimeoutException e) {
            logger.error(e);
        }
    }
}
