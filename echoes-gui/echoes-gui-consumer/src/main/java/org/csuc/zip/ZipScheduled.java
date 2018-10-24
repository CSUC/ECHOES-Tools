package org.csuc.zip;


import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.csuc.entities.RecollectLink;
import org.csuc.utils.recollect.StatusLink;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.server.ServerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author amartinez
 */
public class ZipScheduled extends TimerTask {

    private static Logger logger = LogManager.getLogger(ZipScheduled.class);

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private org.csuc.typesafe.server.Application serverConfig = new ServerConfig(null).getConfig();

    private Client client = new Client("localhost", 27017, "echoes");
    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    @Override
    public void run() {
        try {
            System.out.println("Starting ZipScheduled...");
            ScheduledFuture<?> countdown = scheduler.schedule(() -> {
                try {
                    final long time = new Date().getTime();
                    final long maxdiff = TimeUnit.DAYS.toMillis(1);

                    Files.newDirectoryStream(Paths.get(serverConfig.getFolder() + File.separator + "recollect"), p -> (time - p.toFile().lastModified()) < maxdiff)
                            .forEach(p -> {
                                FileUtils.deleteQuietly(p.toFile());
                                try {
                                    Recollect recollect = recollectDAO.getById(FilenameUtils.getBaseName(p.toString()));

                                    RecollectLink recollectLink = recollect.getLink();
                                    recollectLink.setStatusLink(StatusLink.EXPIRED);

                                    recollect.setLink(recollectLink);

                                    recollectDAO.getDatastore().save(recollectLink);
                                    recollectDAO.save(recollect);
                                } catch (Exception e) {
                                    logger.error(e);
                                }
                            });
                } catch (IOException e) {
                    logger.error(e);
                }
            }, 1, TimeUnit.DAYS);

            while (!countdown.isDone()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
            scheduler.shutdown();
        } catch (Exception ex) {
            logger.error("error running thread " + ex.getMessage());
        }
    }
}
