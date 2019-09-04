package org.csuc.step.content;

import com.typesafe.config.Config;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import static org.csuc.util.DataType.*;

public class Concept {

    private Config config;

    public Concept(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.Concept quality(eu.europeana.corelib.definitions.jibx.Concept conceptType) throws Exception {
        org.csuc.dao.entity.edm.Concept concept = new org.csuc.dao.entity.edm.Concept();

        if (aboutType(conceptType.getAbout())) concept.getData().setAbout(conceptType.getAbout());

        conceptType.getChoiceList().forEach(choice -> {
            //skos:prefLabel
            if (choice.ifPrefLabel()) {
                try {
                    literalType(choice.getPrefLabel());
                    concept.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_prefLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:prefLabel\".level"))));
                }
            }

            //skos:related
            if (choice.ifRelated()) {
                try {
                    resourceType(choice.getRelated());
                    concept.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_related, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:related\".level"))));
                }
            }
        });
        return concept;
    }
}
