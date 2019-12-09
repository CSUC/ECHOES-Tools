package org.csuc.step.content;

import com.typesafe.config.Config;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;

import static org.csuc.util.DataType.*;

public class Concept {

    private Config config;

    public Concept(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.Concept quality(eu.europeana.corelib.definitions.jibx.Concept conceptType) throws Exception {
        org.csuc.dao.entity.edm.Concept concept = new org.csuc.dao.entity.edm.Concept();

        try {
            aboutType(conceptType.getAbout());
            concept.getData().setAbout(conceptType.getAbout());
        } catch (Exception e) {
            concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        conceptType.getChoiceList().forEach(choice -> {

            //skos:prefLabel
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:prefLabel\".level")), LevelQuality.OFF)) {
                if (choice.ifPrefLabel()) {
                    try {
                        literalType(choice.getPrefLabel());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_prefLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:prefLabel\".level"))));
                    }
                }
            }

            //skos:related
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:related\".level")), LevelQuality.OFF)) {
                if (choice.ifRelated()) {
                    try {
                        resourceType(choice.getRelated());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_related, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:related\".level"))));
                    }
                }
            }

        });
        return concept;
    }
}
