package org.csuc.format;

import com.mongodb.MongoClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.Quality;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.csuc.step.Schema;
import org.csuc.util.FormatType;
import org.mongodb.morphia.Morphia;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Datastore implements FormatInterface {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    private org.mongodb.morphia.Datastore datastore;
    private QualityDetailsDAO qualityDetailsDAO;

    private Quality quality;
    private Schema schema;

    public Datastore(String host, int port, String database, Quality quality, Schema schema) {
        logger.info("{}", getClass().getSimpleName());

        Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreNulls(false);
        morphia.getMapper().getOptions().setStoreEmpties(false);

        MongoClient mongo = new MongoClient(host, port);

        datastore = morphia.createDatastore(mongo, database);
        datastore.ensureIndexes();

        qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, datastore);

        this.quality = quality;
        this.schema = schema;
    }

    @Override
    public void execute(Path path) throws IOException {
        qualityDetailsDAO.getDatastore().save(quality);

        Files.walk(path)
                .parallel()
                .filter(Files::isRegularFile)
                .filter(f -> FormatType.RDFXML.lang().getFileExtensions().stream().anyMatch(m -> f.toString().endsWith(String.format(".%s", m))))
                .forEach(p -> {
                    try {
                        QualityDetails qualityDetails = schema.quality(p);
                        qualityDetails.setQuality(quality);

                        qualityDetailsDAO.save(qualityDetails);
                    } catch (Exception e) {
                        logger.error(p);
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void execute(URL url) throws Exception {
        qualityDetailsDAO.getDatastore().save(quality);

        QualityDetails qualityDetails = schema.quality(url);
        qualityDetails.setQuality(quality);

        qualityDetailsDAO.save(qualityDetails);
    }

}
