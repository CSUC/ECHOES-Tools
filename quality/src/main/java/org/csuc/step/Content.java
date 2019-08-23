package org.csuc.step;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.entity.edm.Edm;
import org.csuc.dao.entity.edm.Place;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.typesafe.QualityConfig;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Content extends QualityConfig implements StepInterface {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    public Content(Path config) {
        super(config);
        logger.info("{}", getClass().getSimpleName());

    }

    @Override
    public QualityDetails quality(Path path) throws Exception {
        JibxUnMarshall jibxUnMarshall = new JibxUnMarshall(new FileInputStream(path.toFile()), StandardCharsets.UTF_8, RDF.class);

        QualityDetails qualityDetails = new QualityDetails(path.getFileName().toString());

        return getQualityDetails(jibxUnMarshall, qualityDetails);
    }

    @Override
    public QualityDetails quality(URL url) throws Exception {
        JibxUnMarshall jibxUnMarshall = new JibxUnMarshall(url.openStream(), StandardCharsets.UTF_8, RDF.class);

        QualityDetails qualityDetails = new QualityDetails(url.toString());

        return getQualityDetails(jibxUnMarshall, qualityDetails);
    }

    private QualityDetails getQualityDetails(JibxUnMarshall jibxUnMarshall, QualityDetails qualityDetails) {
        Set<Place> placeList = new HashSet<>();
        Edm edm = new Edm();

        ((RDF) jibxUnMarshall.getElement()).getChoiceList().forEach(choice -> {
            if (choice.ifPlace()) {
                try {
                    placeList.add(new org.csuc.step.content.Place(getQualityConfig().getConfig("\"edm:Place\"")).quality(choice.getPlace()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });

        edm.setPlace(placeList);
        qualityDetails.setEdm(edm);

        return qualityDetails;
    }
}
