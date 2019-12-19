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
            //skos:relatedMatch
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:relatedMatch\".level")), LevelQuality.OFF)) {
                if (choice.ifRelatedMatch()) {
                    try {
                        resourceType(choice.getRelatedMatch());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_relatedMatch, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:relatedMatch\".level"))));
                    }
                }
            }

            //skos:altLabel
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:altLabel\".level")), LevelQuality.OFF)) {
                if (choice.ifAltLabel()) {
                    try {
                        literalType(choice.getAltLabel());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_altLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:altLabel\".level"))));
                    }
                }
            }

            //skos:exactMatch
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:exactMatch\".level")), LevelQuality.OFF)) {
                if (choice.ifExactMatch()) {
                    try {
                        resourceType(choice.getExactMatch());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_exactMatch, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:exactMatch\".level"))));
                    }
                }
            }

            //skos:broader
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:broader\".level")), LevelQuality.OFF)) {
                if (choice.ifBroader()) {
                    try {
                        resourceType(choice.getBroader());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_broader, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:broader\".level"))));
                    }
                }
            }

            //skos:closeMatch
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:closeMatch\".level")), LevelQuality.OFF)) {
                if (choice.ifCloseMatch()) {
                    try {
                        resourceType(choice.getCloseMatch());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_closeMatch, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:closeMatch\".level"))));
                    }
                }
            }

            //skos:narrower
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:narrower\".level")), LevelQuality.OFF)) {
                if (choice.ifNarrower()) {
                    try {
                        resourceType(choice.getNarrower());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_narrower, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:narrower\".level"))));
                    }
                }
            }

            //skos:note
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:note\".level")), LevelQuality.OFF)) {
                if (choice.ifNote()) {
                    try {
                        literalType(choice.getNote());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_note, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:note\".level"))));
                    }
                }
            }

            //skos:notation
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:notation\".level")), LevelQuality.OFF)) {
                if (choice.ifNotation()) {
                    try {
                        literalType(choice.getNotation());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_notation, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:notation\".level"))));
                    }
                }
            }

            //skos:broadMatch
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:broadMatch\".level")), LevelQuality.OFF)) {
                if (choice.ifBroadMatch()) {
                    try {
                        resourceType(choice.getBroadMatch());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_broadMatch, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:broadMatch\".level"))));
                    }
                }
            }

            //skos:inScheme
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:inScheme\".level")), LevelQuality.OFF)) {
                if (choice.ifInScheme()) {
                    try {
                        resourceType(choice.getInScheme());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_inScheme, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:inScheme\".level"))));
                    }
                }
            }

            //skos:narrowMatch
            if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:narrowMatch\".level")), LevelQuality.OFF)) {
                if (choice.ifNarrowMatch()) {
                    try {
                        resourceType(choice.getNarrowMatch());
                        concept.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        concept.getErrorList().add(new Error(EntityType.Concept, MetadataType.skos_narrowMatch, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:narrowMatch\".level"))));
                    }
                }
            }

        });
        return concept;
    }
}
