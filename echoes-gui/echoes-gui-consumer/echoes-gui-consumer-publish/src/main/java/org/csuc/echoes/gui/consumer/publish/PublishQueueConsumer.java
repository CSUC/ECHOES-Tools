package org.csuc.echoes.gui.consumer.publish;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.lang.CollectorStreamTriples;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.QualityDAO;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.Quality;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.impl.QualityDAOImpl;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.csuc.dao.impl.loader.LoaderDAOImpl;
import org.csuc.dao.loader.LoaderDAO;
import org.csuc.echoes.gui.consumer.publish.utils.RdfDAO;
import org.csuc.echoes.gui.consumer.publish.utils.Time;
import org.csuc.entities.loader.Loader;
import org.csuc.entities.loader.LoaderDetails;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.util.FormatType;
import org.csuc.utils.Status;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @author amartinez
 */
public class PublishQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(PublishQueueConsumer.class);

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, client.getDatastore());
    private QualityDAO qualityDAO = new QualityDAOImpl(Quality.class, client.getDatastore());

    private Loader loader = null;

    /**
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public PublishQueueConsumer(Config typesafeRabbitMQ) throws IOException, TimeoutException {
        super(typesafeRabbitMQ);
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
        threadPool.submit(() -> {
            try {
                Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);

                logger.info("[x] Received '{}'", map);

                loader = loaderDAO.getById(map.get("_id").toString());
                loader.setStatus(Status.PROGRESS);

                Quality quality = qualityDAO.getById(map.get("uuid").toString());

                loader.setSize(quality.getQualitySize());

                loaderDAO.insert(loader);

                QualityDetailsDAO qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, client.getDatastore());

                Query<QualityDetails> qualityDetailsQuery = qualityDetailsDAO.getValidById(quality.get_id());

                qualityDetailsQuery.fetch(new FindOptions().batchSize(50).noCursorTimeout(true)).forEach(qualityDetails -> {
                    try {
                        CloseableHttpClient httpclient = HttpClients.createDefault();

                        RdfDAO<ByteArrayOutputStream> outputStreamRdfDAO = new RdfDAO<>(qualityDetails, ByteArrayOutputStream::new);

                        if(loader.isReplace()){
                            CollectorStreamTriples inputStream = new CollectorStreamTriples();

                            RDFParser.source(new ByteArrayInputStream(outputStreamRdfDAO.toRDF().toByteArray())).lang(FormatType.convert(loader.getContentType()).lang()).parse(inputStream);

                            inputStream.getCollected().stream().map(Triple::getSubject).map(Node::getURI).collect(Collectors.toSet()).forEach(subject->{
                                try {
                                    HttpDelete httpDelete =
                                            new HttpDelete(String.format("%s?c=%s&s=%s",
                                                    loader.getEndpoint(), URLEncoder.encode(String.format("<%s>", loader.getContextUri()), StandardCharsets.UTF_8.toString()), URLEncoder.encode(String.format("<%s>",subject), StandardCharsets.UTF_8.toString())));

                                    HttpResponse response = httpclient.execute(httpDelete);
                                    HttpEntity entity = response.getEntity();
                                    if (entity != null) logger.info("[DELETE] - {} {}", subject, EntityUtils.toString(entity));
                                } catch (IOException e) {
                                    logger.error(e);
                                }
                            });
                        }

                        HttpPost httppost;
                        if(Objects.nonNull(loader.getContextUri()))
                            httppost = new HttpPost(String.format("%s?context-uri=%s", loader.getEndpoint(), loader.getContextUri()));
                        else
                            httppost = new HttpPost(loader.getEndpoint());

                        httppost.addHeader("content-type", FormatType.convert(loader.getContentType()).lang().getContentType().getContentType());



                        httppost.setEntity(new ByteArrayEntity(outputStreamRdfDAO.toRDF().toByteArray()));

                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity resEntity = response.getEntity();

                        if (resEntity != null) {
                            LoaderDetails loaderDetails = new LoaderDetails();

                            loaderDetails.setValue(FilenameUtils.getName(qualityDetails.getInput()));
                            loaderDetails.setLoader(loader);
                            loaderDetails.setStatus(response.getStatusLine().getStatusCode());
                            loaderDetails.setMessage(EntityUtils.toString(resEntity));

                            loaderDAO.getDatastore().save(loaderDetails);
                            //logger.info(response.getStatusLine().getStatusCode() + " " + EntityUtils.toString(resEntity));
                        }
                        httpclient.close();

                    } catch (Exception e) {
                        logger.error("{}     {}", qualityDetails.get_id(), e);
                        LoaderDetails loaderDetails = new LoaderDetails();
                        loaderDetails.setLoader(loader);
                        loaderDetails.setStatus(-1);
                        loaderDetails.setMessage(e.toString());

                        loaderDAO.getDatastore().save(loaderDetails);
                    }
                });


//                Files.walk(Paths.get(applicationConfig.getQualityFolder(String.format("%s", quality.get_id()))))
//                        .filter(Files::isRegularFile)
//                        .filter(f-> FormatType.convert(quality.getContentType()).lang().getFileExtensions().stream().anyMatch(m->  f.toString().endsWith(String.format(".%s", m))))
//                        .parallel()
//                        .forEach((Path f) -> {
//
//                        });

                loader = loaderDAO.getById(map.get("_id").toString());
                loader.setStatus(Status.END);
                loader.setDuration(Time.duration(loader.getTimestamp(), DateTimeFormatter.ISO_TIME));


                Query<LoaderDetails> loaderDetailsQuery = loaderDAO.getDatastore().createQuery(LoaderDetails.class);

                loaderDetailsQuery.and(
                        loaderDetailsQuery.criteria("loader").equal(loaderDAO.getDatastore().getKey(loader)),
                        loaderDetailsQuery.criteria("status-code").equal(200)
                );

                loader.setTotal(Math.toIntExact(loaderDetailsQuery.count()));

                loaderDAO.insert(loader);

                logger.info(String.format("[x] Consumed '%s\t%s'", map, loader.getDuration()));

                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
                logger.error(e);

                if (Objects.nonNull(loaderDAO)) {
                    loader.setStatus(Status.ERROR);
                    loader.setDuration(Time.duration(loader.getTimestamp(), DateTimeFormatter.ISO_TIME));
                    loaderDAO.save(loader);
                }

                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e1) {
                    logger.error(e);
                }
            }
        });
    }

    @Override
    public void run() {
        try {
            // Add a recoverable listener (when broken connections are recovered).
            // Given the way the RabbitMQ factory is configured, the channel should be "recoverable".
            channel.basicQos(typesafeRabbitMQ.getInt("Qos"), false); // Per consumer limit
            //channel.basicQos(1, true);  // Per channel limit
            channel.basicConsume(endPointName, false, this);
        } catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
            logger.error(e);
        }
    }
}
