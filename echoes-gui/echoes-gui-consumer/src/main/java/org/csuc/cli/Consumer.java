package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.AnalyseQueueConsumer;
import org.csuc.recollect.RecollectQueueConsumer;
import org.csuc.typesafe.consumer.ProducerAndConsumerConfig;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.zip.ZipQueueConsumer;
import org.csuc.zip.ZipScheduled;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
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

        RabbitMQConfig rabbitMQConfig = new ProducerAndConsumerConfig(null).getRabbitMQConfig();
        logger.info(rabbitMQConfig);

        try {
            AnalyseQueueConsumer analyseQueueConsumer = new AnalyseQueueConsumer(rabbitMQConfig.getParserQueue(), rabbitMQConfig);
            RecollectQueueConsumer recollectQueueConsumer = new RecollectQueueConsumer(rabbitMQConfig.getRecollectQueue(), rabbitMQConfig);
            ZipQueueConsumer zipQueueConsumer = new ZipQueueConsumer(rabbitMQConfig.getZipQueue(), rabbitMQConfig);
            ZipScheduled zipScheduled = new ZipScheduled();


            Stream.of(analyseQueueConsumer, recollectQueueConsumer, zipQueueConsumer, zipScheduled).forEach(consumer-> new Thread(consumer).start());
        } catch (IOException | TimeoutException e) {
            logger.error(e);
        }
    }
}
