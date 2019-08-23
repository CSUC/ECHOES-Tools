package org.csuc.step;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.deserialize.JibxUnMarshall;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class Schema implements StepInterface<QualityDetails> {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());
    private Schematron schematron;

    public Schema(Schematron schematron) {
        logger.info("{}", getClass().getSimpleName());

        this.schematron = schematron;
    }

    @Override
    public QualityDetails quality(Path path) throws Exception {
        JibxUnMarshall jibxUnMarshall = new JibxUnMarshall(new FileInputStream(path.toFile()), StandardCharsets.UTF_8, RDF.class);

        QualityDetails qualityDetailsSchema = new QualityDetails(path.getFileName().toString());

        if (!Objects.isNull(jibxUnMarshall.getError())) {
            qualityDetailsSchema.setSchema(new org.csuc.dao.entity.Schema(jibxUnMarshall.getError().getMessage()));
        } else {
            qualityDetailsSchema.setValidSchema(true);

            if (Objects.nonNull(schematron)) {
                QualityDetails q = schematron.quality(path);

                qualityDetailsSchema.setSchematron(q.getSchematron());
                qualityDetailsSchema.setValidSchematron(q.isValidSchematron());

                qualityDetailsSchema.setEdm(q.getEdm());
                qualityDetailsSchema.setValidContent(q.isValidContent());
            }
        }

        return qualityDetailsSchema;
    }

    @Override
    public QualityDetails quality(URL url) throws Exception {
        JibxUnMarshall jibxUnMarshall = new JibxUnMarshall(url.openStream(), StandardCharsets.UTF_8, RDF.class);

        QualityDetails qualityDetailsSchema = new QualityDetails(url.toString());

        if (!Objects.isNull(jibxUnMarshall.getError())) {
            qualityDetailsSchema.setSchema(new org.csuc.dao.entity.Schema(jibxUnMarshall.getError().getMessage()));
        } else {
            qualityDetailsSchema.setValidSchema(true);

            if (Objects.nonNull(schematron)) {
                QualityDetails q = schematron.quality(url);

                qualityDetailsSchema.setSchematron(q.getSchematron());
                qualityDetailsSchema.setValidSchematron(q.isValidSchematron());

                qualityDetailsSchema.setEdm(q.getEdm());
                qualityDetailsSchema.setValidContent(q.isValidContent());
            }
        }

        return qualityDetailsSchema;
    }
}
