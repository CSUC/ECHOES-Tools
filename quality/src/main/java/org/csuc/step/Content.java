package org.csuc.step;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.entity.edm.Edm;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.typesafe.QualityConfig;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Content extends QualityConfig implements StepInterface {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    public Content(Path config) {
        super(config);
        logger.info("{}", getClass().getSimpleName());
    }

    public Content(String config) {
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
        Edm edm = new Edm();

        ((RDF) jibxUnMarshall.getElement()).getChoiceList().forEach(choice -> {
            if (choice.ifProvidedCHO()) {
                try {
                    edm.getProvidedCHO().add(new org.csuc.step.content.ProvidedCHO(getQualityConfig().getConfig("\"edm:ProvidedCHO\"")).quality(choice.getProvidedCHO()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            if (choice.ifPlace()) {
                try {
                    edm.getPlace().add(new org.csuc.step.content.Place(getQualityConfig().getConfig("\"edm:Place\"")).quality(choice.getPlace()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            if (choice.ifConcept()) {
                try {
                    edm.getConcept().add(new org.csuc.step.content.Concept(getQualityConfig().getConfig("\"skos:Concept\"")).quality(choice.getConcept()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            if (choice.ifAgent()) {
                try {
                    edm.getAgent().add(new org.csuc.step.content.Agent(getQualityConfig().getConfig("\"edm:Agent\"")).quality(choice.getAgent()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            if (choice.ifTimeSpan()) {
                try {
                    edm.getTimeSpan().add(new org.csuc.step.content.TimeSpan(getQualityConfig().getConfig("\"edm:TimeSpan\"")).quality(choice.getTimeSpan()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            if (choice.ifAggregation()) {
                try {
                    edm.getAggregation().add(new org.csuc.step.content.Aggregation(getQualityConfig().getConfig("\"ore:Aggregation\"")).quality(choice.getAggregation()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            if (choice.ifWebResource()) {
                try {
                    edm.getWebResource().add(new org.csuc.step.content.WebResource(getQualityConfig().getConfig("\"edm:WebResource\"")).quality(choice.getWebResource()));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });

        qualityDetails.setEdm(edm);

        return qualityDetails;
    }
}
