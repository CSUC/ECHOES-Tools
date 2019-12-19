package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.csuc.util.DataType.*;

public class TimeSpan {

    private Config config;

    public TimeSpan(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.TimeSpan quality(TimeSpanType timeSpanType) throws Exception {
        org.csuc.dao.entity.edm.TimeSpan timeSpan = new org.csuc.dao.entity.edm.TimeSpan();

        try{
            aboutType(timeSpanType.getAbout());
            timeSpan.getData().setAbout(timeSpanType.getAbout());
        }catch (Exception e){
            timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        //skos:prefLabel
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:prefLabel\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(timeSpanType.getPrefLabelList()).ifPresent(prefLabels -> {
                timeSpan.getData().setPrefLabelList(
                        prefLabels.stream().map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.skos_prefLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:prefLabel\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //edm:begin
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:begin\".level")), LevelQuality.OFF)) {
            if (Objects.nonNull(timeSpanType.getBegin())) {
                try {
                    literalType(timeSpanType.getBegin());
                    timeSpan.getData().setBegin(timeSpanType.getBegin());
                } catch (Exception e) {
                    timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.edm_begin, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:begin\".level"))));
                }
            }
        }

        //edm:end
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:end\".level")), LevelQuality.OFF)) {
            if (Objects.nonNull(timeSpanType.getEnd())) {
                try {
                    literalType(timeSpanType.getEnd());
                    timeSpan.getData().setEnd(timeSpanType.getEnd());
                } catch (Exception e) {
                    timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.edm_end, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:end\".level"))));
                }
            }
        }

        //skos:altLabel
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:altLabel\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(timeSpanType.getAltLabelList()).ifPresent(altLabels -> {
                timeSpan.getData().setAltLabelList(
                        altLabels.stream().map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.skos_altLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:altLabel\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //skos:note
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:note\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(timeSpanType.getNoteList()).ifPresent(noteList -> {
                timeSpan.getData().setNoteList(
                        noteList.stream().map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.skos_note, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:note\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //edm:isNextInSequence
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level")), LevelQuality.OFF)) {
            if (Objects.nonNull(timeSpanType.getIsNextInSequence())) {
                try {
                    resourceType(timeSpanType.getIsNextInSequence());
                    timeSpan.getData().setIsNextInSequence(timeSpanType.getIsNextInSequence());
                } catch (Exception e) {
                    timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.edm_isNextInSequence, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level"))));
                }
            }
        }

        //dcterms:hasPart
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasPart\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(timeSpanType.getHasPartList()).ifPresent(hasParts -> {
                timeSpan.getData().setHasPartList(
                        hasParts.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.dcterms_hasPart, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasPart\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //owl:sameAs
        if(!Objects.equals(LevelQuality.convert(config.getString("\"owl:sameAs\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(timeSpanType.getSameAList()).ifPresent(sameAs -> {
                timeSpan.getData().setSameAList(
                        sameAs.stream().map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.owl_sameAs, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"owl:sameAs\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:isPartOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(timeSpanType.getIsPartOfList()).ifPresent(isPartOfs -> {
                timeSpan.getData().setIsPartOfList(
                        isPartOfs.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                timeSpan.getErrorList().add(new Error(EntityType.TimeSpan, MetadataType.dcterms_isPartOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        return timeSpan;
    }
}
