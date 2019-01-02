package org.csuc.echoes.gui.consumer.zip;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.csuc.entities.RecollectLink;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.recollect.StatusLink;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
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

    private URL applicationResource = getClass().getClassLoader().getResource("echoes-gui-server.conf");
    private Application applicationConfig = new ServerConfig((Objects.isNull(applicationResource)) ? null : new File(applicationResource.getFile()).toPath()).getConfig();

    private Client client = new Client(applicationConfig.getMongoDB().getHost(), applicationConfig.getMongoDB().getPort(), applicationConfig.getMongoDB().getDatabase());

    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    @Override
    public void run() {
        try {
            System.out.println("Starting ZipScheduled...");
            ScheduledFuture<?> countdown = scheduler.schedule(() -> {
                try {
                    final long time = new Date().getTime();
                    final long maxdiff = TimeUnit.DAYS.toMillis(1);

                    Files.newDirectoryStream(Paths.get(applicationConfig.getFolder() + File.separator + "recollect"),
                            p -> p.toString().endsWith(".zip") && ((time - p.toFile().lastModified()) < maxdiff))
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
