package org.csuc.zip;

import com.rabbitmq.client.*;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.csuc.entities.RecollectLink;
import org.csuc.utils.recollect.StatusLink;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.EndPoint;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.Time;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author amartinez
 */
public class ZipQueueConsumer extends EndPoint implements Runnable, Consumer {

    private Logger logger = LogManager.getLogger(ZipQueueConsumer.class);

    private org.csuc.typesafe.server.Application serverConfig = new ServerConfig(null).getConfig();

    private Client client = new Client("localhost", 27017, "echoes");
    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    /**
     * @param endpointName
     * @param typesafeRabbitMQ
     * @throws IOException
     * @throws TimeoutException
     */
    public ZipQueueConsumer(String endpointName, RabbitMQConfig typesafeRabbitMQ) throws IOException, TimeoutException {
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
        Recollect recollect = null;
        try {
            LocalDateTime inici = LocalDateTime.now();
            Map<?, ?> map = (HashMap<?, ?>) SerializationUtils.deserialize(bytes);
            logger.info("[x] Received '{}'", map);

            recollect = recollectDAO.getById(map.get("_id").toString());

            if(Objects.nonNull(recollect)){
                RecollectLink recollectLink = recollect.getLink();
                recollectLink.setStatusLink(StatusLink.PROGRESS);

                recollect.setLink(recollectLink);

                recollectDAO.getDatastore().save(recollectLink);
                recollectDAO.save(recollect);
            }


            FileOutputStream fos = new FileOutputStream(System.getProperty("java.io.tmpdir") + File.separator + map.get("_id").toString() + ".zip");

            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = Paths.get(serverConfig.getRecollectFolder(map.get("_id").toString()) + File.separator + map.get("set").toString()).toFile();

            zipFolder(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();


            Files.move(
                    Paths.get(System.getProperty("java.io.tmpdir") + File.separator + map.get("_id").toString() + ".zip"),
                    Paths.get(serverConfig.getRecollectFolder(map.get("_id").toString()) + File.separator + map.get("_id").toString() + ".zip"),
                    StandardCopyOption.REPLACE_EXISTING);



            if(Objects.nonNull(recollect)){
                RecollectLink recollectLink = recollect.getLink();
                recollectLink.setStatusLink(StatusLink.GENERATE);

                recollect.setLink(recollectLink);

                recollectDAO.getDatastore().save(recollectLink);
                recollectDAO.save(recollect);
            }

            logger.info(String.format("[x] Consumed '%s\t%s'", map, Time.duration(inici, DateTimeFormatter.ISO_TIME)));

            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error(e);

            if(Objects.nonNull(recollect)){
                RecollectLink recollectLink = recollect.getLink();
                recollectLink.setStatusLink(StatusLink.NULL);

                recollect.setLink(recollectLink);

                recollectDAO.getDatastore().save(recollectLink);
                recollectDAO.save(recollect);
            }

            try {
                channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (IOException e1) {
                logger.error(e);
            }
        }
    }


    /**
     *
     * @param fileToZip
     * @param fileName
     * @param zipOut
     * @throws IOException
     */
    private void zipFolder(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden())   return;
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) zipFolder(childFile, fileName + "/" + childFile.getName(), zipOut);
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) zipOut.write(bytes, 0, length);
        fis.close();
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
