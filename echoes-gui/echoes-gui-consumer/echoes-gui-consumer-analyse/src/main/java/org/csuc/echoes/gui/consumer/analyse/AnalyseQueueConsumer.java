package org.csuc.echoes.gui.consumer.analyse;

import com.rabbitmq.client.*;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.core.factory.*;
import org.csuc.analyse.core.strategy.dom.Dom;
import org.csuc.analyse.core.strategy.dom4j.Dom4j;
import org.csuc.analyse.core.strategy.sax.Sax;
import org.csuc.analyse.core.strategy.xslt.Xslt;
import org.csuc.client.Client;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.ParserErrorDAO;
import org.csuc.dao.impl.AnalyseDAOImpl;
import org.csuc.dao.impl.AnalyseErrorDAOImpl;
import org.csuc.echoes.gui.consumer.analyse.utils.Time;
import org.csuc.entities.Analyse;
import org.csuc.entities.AnalyseError;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.Status;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public class AnalyseQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(AnalyseQueueConsumer.class);

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private AnalyseDAO analyseDAO = new AnalyseDAOImpl(Analyse.class, client.getDatastore());
    private ParserErrorDAO parserErrorDAO = new AnalyseErrorDAOImpl(AnalyseError.class, client.getDatastore());

    /**
     * @param endpointName
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public AnalyseQueueConsumer(String endpointName, RabbitMQConfig typesafeRabbitMQ) throws IOException, TimeoutException {
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
        Analyse analyse = null;
        try {
            Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);

            logger.info("[x] Received '{}'", map);

            analyse = analyseDAO.getById(map.get("_id").toString());
            analyse.setStatus(Status.PROGRESS);
            analyseDAO.insert(analyse);

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
                    factory.JSON(new FileOutputStream(Paths.get(applicationConfig.getParserFolder(File.separator + map.get("_id").toString()) + File.separator + "result.json").toFile()));
                } else if (Objects.equals(map.get("format"), ParserFormat.XML)) {
                    factory.XML(new FileOutputStream(Paths.get(applicationConfig.getParserFolder(File.separator + map.get("_id").toString()) + File.separator + "result.xml").toFile()));
                } else {
                    factory.XML(new FileOutputStream(Paths.get(applicationConfig.getParserFolder(File.separator + map.get("_id").toString()) + File.separator + "result.json").toFile()));
                }
            }

            analyse = analyseDAO.getById(map.get("_id").toString());
            analyse.setStatus(Status.END);
            analyse.setDuration(Time.duration(LocalDateTime.parse(analyse.getTimestamp()), DateTimeFormatter.ISO_TIME));

            analyseDAO.insert(analyse);

            logger.info(String.format("[x] Consumed '%s\t%s'", map, analyse.getDuration()));

            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error(e);

            if (Objects.nonNull(analyse)) {
                analyse.setStatus(Status.ERROR);
                analyse.setDuration(Time.duration(LocalDateTime.parse(analyse.getTimestamp()), DateTimeFormatter.ISO_TIME));

                AnalyseError analyseError = new AnalyseError();

                analyseError.setException(e.toString());
                analyseError.setAnalyse(analyse);

                analyseDAO.save(analyse);
                parserErrorDAO.save(analyseError);
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
