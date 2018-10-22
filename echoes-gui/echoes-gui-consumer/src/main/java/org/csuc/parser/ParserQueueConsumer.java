package org.csuc.parser;

import com.rabbitmq.client.*;
import org.csuc.client.Client;
import org.csuc.dao.ParserDAO;
import org.csuc.dao.ParserErrorDAO;
import org.csuc.dao.impl.ParserDAOImpl;
import org.csuc.dao.impl.ParserErrorDAOImpl;
import org.csuc.entities.ParserError;
import org.csuc.utils.Status;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.EndPoint;
import org.csuc.Parser.Core.factory.*;
import org.csuc.Parser.Core.strategy.dom.Dom;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.csuc.Parser.Core.strategy.sax.Sax;
import org.csuc.Parser.Core.strategy.xslt.Xslt;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.Time;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public class ParserQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(ParserQueueConsumer.class);

    private Client client = new Client("localhost", 27017, "echoes");

    private ParserDAO parserDAO = new ParserDAOImpl(org.csuc.entities.Parser.class, client.getDatastore());
    private ParserErrorDAO parserErrorDAO = new ParserErrorDAOImpl(org.csuc.entities.ParserError.class, client.getDatastore());

    private org.csuc.typesafe.server.Application serverConfig = new ServerConfig(null).getConfig();


    /**
     * @param endpointName
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public ParserQueueConsumer(String endpointName, RabbitMQConfig typesafeRabbitMQ) throws IOException, TimeoutException {
        super(endpointName, typesafeRabbitMQ);
    }

    @Override
    public void handleConsumeOk(String s) {
        logger.info(String.format("Consumer [ %s ] registered", endPointName));
    }

    @Override
    public void handleCancelOk(String s) {
        logger.info("handleCancelOk:  {}", s);
    }

    @Override
    public void handleCancel(String s) throws IOException {
        logger.info("handleCancel:  {}", s);
    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {
        logger.info("handleShutdownSignal:  {}  {}", s, e);
    }

    @Override
    public void handleRecoverOk(String s) {
        logger.info("handleRecoverOk:  {}", s);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        org.csuc.entities.Parser parser = null;
        try {
            //LocalDateTime inici = LocalDateTime.now();
            Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);

            logger.info("[x] Received '{}'", map);

            parser = parserDAO.getById(map.get("_id").toString());
            parser.setStatus(Status.PROGRESS);
            parserDAO.insert(parser);

            Parser factory = null;

            if (map.get("type").equals(ParserType.OAI)) {
                if (ParserMethod.DOM4J.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserOAI(new Dom4j()));
                }
                if (ParserMethod.SAX.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserOAI(new Sax()));
                }
                if (ParserMethod.DOM.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserOAI(new Dom()));
                }
                if (ParserMethod.XSLT.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserOAI(new Xslt()));
                }
                factory.execute(new URL(map.get("value").toString()));
            } else if (map.get("type").equals(ParserType.URL)) {
                if (ParserMethod.DOM4J.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserURL(new Dom4j()));
                }
                if (ParserMethod.SAX.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserURL(new Sax()));
                }
                if (ParserMethod.DOM.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserURL(new Dom()));
                }
                if (ParserMethod.XSLT.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserURL(new Xslt()));
                }
                factory.execute(new URL(map.get("value").toString()));
            } else if (map.get("type").equals(ParserType.FILE)) {
                if (ParserMethod.DOM4J.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserFILE(new Dom4j()));
                }
                if (ParserMethod.SAX.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserFILE(new Sax()));
                }
                if (ParserMethod.DOM.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserFILE(new Dom()));
                }
                if (ParserMethod.XSLT.equals(map.get("method"))) {
                    factory = FactoryParser.createFactory(new ParserFILE(new Xslt()));
                }
                factory.execute(map.get("value").toString());
            }

            if (Objects.nonNull(factory)) {
                if (Objects.equals(map.get("format"), ParserFormat.JSON)) {
                    factory.JSON(new FileOutputStream(Paths.get(serverConfig.getParserFolder(File.separator + map.get("_id").toString()) + File.separator + "result.json").toFile()));
                } else if (Objects.equals(map.get("format"), ParserFormat.XML)) {
                    factory.XML(new FileOutputStream(Paths.get(serverConfig.getParserFolder(File.separator + map.get("_id").toString()) + File.separator + "result.xml").toFile()));
                } else {
                    factory.XML(new FileOutputStream(Paths.get(serverConfig.getParserFolder(File.separator + map.get("_id").toString()) + File.separator + "result.json").toFile()));
                }
            }

            parser = parserDAO.getById(map.get("_id").toString());
            parser.setStatus(Status.END);
            parser.setDuration(Time.duration(LocalDateTime.parse(parser.getTimestamp()), DateTimeFormatter.ISO_TIME));

            parserDAO.insert(parser);

            logger.info(String.format("[x] Consumed '%s\t%s'", map, parser.getDuration()));

            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error(e);

            if (Objects.nonNull(parser)) {
                parser.setStatus(Status.ERROR);
                parser.setDuration(Time.duration(LocalDateTime.parse(parser.getTimestamp()), DateTimeFormatter.ISO_TIME));

                ParserError parserError = new ParserError();

                parserError.setException(e.toString());
                parserError.setParser(parser);

                parserDAO.save(parser);
                parserErrorDAO.save(parserError);
            }

            try {
                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (IOException e1) {
                logger.error(e);
            }
        }
    }

    @Override
    public void run() {
        try {
            // Add a recoverable listener (when broken connections are recovered).
            // Given the way the RabbitMQ factory is configured, the channel should be "recoverable".
            channel.basicQos(1, false); // Per consumer limit
            //channel.basicQos(1, true);  // Per channel limit
            channel.basicConsume(endPointName, false, this);
        } catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
            logger.error(e);
        }
    }
}
