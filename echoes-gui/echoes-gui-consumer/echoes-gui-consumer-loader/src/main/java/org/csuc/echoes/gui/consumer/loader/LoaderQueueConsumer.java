package org.csuc.echoes.gui.consumer.loader;

import com.rabbitmq.client.*;
import com.typesafe.config.Config;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.LoaderDAO;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.LoaderDAOImpl;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.echoes.gui.consumer.loader.utils.Time;
import org.csuc.entities.Loader;
import org.csuc.entities.LoaderDetails;
import org.csuc.entities.Recollect;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.util.FormatType;
import org.csuc.utils.Status;
import org.mongodb.morphia.query.Query;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author amartinez
 */
public class LoaderQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(LoaderQueueConsumer.class);

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, client.getDatastore());
    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    private Loader loader = null;

    /**
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public LoaderQueueConsumer(Config typesafeRabbitMQ) throws IOException, TimeoutException {
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

        try {
            Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);

            logger.info("[x] Received '{}'", map);

            loader = loaderDAO.getById(map.get("_id").toString());
            loader.setStatus(Status.PROGRESS);


            Recollect recollect = recollectDAO.getById(map.get("uuid").toString());

            loader.setSize(
                    Math.toIntExact(Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s/%s", recollect.get_id(), recollect.getSet()))))
                            .filter(Files::isRegularFile).count())
            );

            loaderDAO.insert(loader);

            Files.walk(Paths.get(applicationConfig.getRecollectFolder(String.format("%s/%s", recollect.get_id(), recollect.getSet()))))
                    .filter(Files::isRegularFile)
                    .parallel()
                    .forEach((Path f) -> {
                        CloseableHttpClient httpclient = HttpClients.createDefault();

                        HttpPost httppost;
                        if(Objects.nonNull(loader.getContextUri()))
                            httppost = new HttpPost(String.format("%s?context-uri=%s", loader.getEndpoint(), loader.getContextUri()));
                        else
                            httppost = new HttpPost(loader.getEndpoint());

                        httppost.addHeader("content-type", FormatType.convert(loader.getContentType()).lang().getContentType().getContentType());

                        FileEntity entity = new FileEntity(f.toFile());

                        httppost.setEntity(entity);

                        try {
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity resEntity = response.getEntity();

                            if (resEntity != null) {
                                LoaderDetails loaderDetails = new LoaderDetails(FilenameUtils.getBaseName(f.getFileName().toString()));
                                loaderDetails.setLoader(loader);
                                loaderDetails.setStatus(response.getStatusLine().getStatusCode());
                                loaderDetails.setMessage(EntityUtils.toString(resEntity));

                                loaderDAO.getDatastore().save(loaderDetails);
                               //logger.info(response.getStatusLine().getStatusCode() + " " + EntityUtils.toString(resEntity));
                            }
                            httpclient.close();

                        } catch (IOException e) {
                            //logger.error(String.format("%s %s\n%s", "ERROR", f, e));
                            LoaderDetails loaderDetails = new LoaderDetails();
                            loaderDetails.setLoader(loader);
                            loaderDetails.setStatus(-1);
                            loaderDetails.setMessage(e.toString());

                            loaderDAO.getDatastore().save(loaderDetails);
                        }
                    });

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
