package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.csuc.util.DataType.*;

public class ProvidedCHO {

    private Config config;

    public ProvidedCHO(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.ProvidedCHO quality(ProvidedCHOType providedCHOType) throws Exception {
        org.csuc.dao.entity.edm.ProvidedCHO provided = new org.csuc.dao.entity.edm.ProvidedCHO();

        if (aboutType(providedCHOType.getAbout())) provided.getData().setAbout(providedCHOType.getAbout());

        provided.getData().setType(providedCHOType.getType());

        providedCHOType.getChoiceList().forEach(choice -> {
            //dc:contributor
            if (choice.ifContributor()) {
                try {
                    resourceOrLiteralType(choice.getContributor());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_contributor, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:contributor\".level"))));
                }
            }
            //dc:coverage
            if (choice.ifCoverage()) {
                try {
                    resourceOrLiteralType(choice.getCoverage());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_coverage, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:coverage\".level"))));
                }
            }
            //dc:creator
            if (choice.ifCreator()) {
                try {
                    resourceOrLiteralType(choice.getCreator());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_creator, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:creator\".level"))));
                }
            }

            //dc:date
            if (choice.ifDate()) {
                try {
                    resourceOrLiteralType(choice.getDate());
                    //dateType()
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_date, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:date\".level"))));
                }
            }

            //dc:description
            if (choice.ifDescription()) {
                try {
                    resourceOrLiteralType(choice.getDescription());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_description, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:description\".level"))));
                }
            }

            //dc:format
            if (choice.ifFormat()) {
                try {
                    resourceOrLiteralType(choice.getFormat());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_format, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:format\".level"))));
                }
            }

            //dcterms:provenance
            if (choice.ifProvenance()) {
                try {
                    resourceOrLiteralType(choice.getProvenance());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_provenance, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:provenance\".level"))));
                }
            }

            //dc:identifier
            if (choice.ifIdentifier()) {
                try {
                    literalType(choice.getIdentifier());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_identifier, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:identifier\".level"))));
                }
            }

            //dc:language
            if (choice.ifLanguage()) {
                try {
                    literalType(choice.getLanguage());
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_language, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:language\".level"))));
                }

                try {
                    if (!languageCode(choice.getLanguage().getString()))
                        throw new Exception(MessageFormat.format("\"{0}\" language not suported", choice.getLanguage().getString()));
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_language, QualityType.LanguageType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:language\".level"))));
                }

                provided.getData().getChoiceList().add(choice);
            }

            //dc:publisher
            if (choice.ifPublisher()) {
                try {
                    resourceOrLiteralType(choice.getPublisher());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_publisher, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:publisher\".level"))));
                }
            }

            //dcterms:spatial
            if (choice.ifSpatial()) {
                try {
                    resourceOrLiteralType(choice.getSpatial());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_spatial, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:spatial\".level"))));
                }
            }

            //dc:relation
            if (choice.ifRelation()) {
                try {
                    resourceOrLiteralType(choice.getRelation());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_relation, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:relation\".level"))));
                }
            }

            //dc:rights
            if (choice.ifRights()) {
                try {
                    resourceOrLiteralType(choice.getRights());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_rights, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:rights\".level"))));
                }
            }

            //dc:source
            if (choice.ifSource()) {
                try {
                    resourceOrLiteralType(choice.getSource());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_source, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:source\".level"))));
                }
            }

            //dcterms:temporal
            if (choice.ifTemporal()) {
                try {
                    resourceOrLiteralType(choice.getTemporal());
                    //dateType()
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_temporal, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:temporal\".level"))));
                }
            }

            //dc:subject
            if (choice.ifSubject()) {
                try {
                    resourceOrLiteralType(choice.getSubject());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_subject, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:subject\".level"))));
                }
            }

            //dc:title
            if (choice.ifTitle()) {
                try {
                    literalType(choice.getTitle());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_title, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:title\".level"))));
                }
            }
            //dc:type
            if (choice.ifType()) {
                try {
                    resourceOrLiteralType(choice.getType());
                    provided.getData().getChoiceList().add(choice);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_type, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:type\".level"))));
                }
            }
        });

        // edm:isRelatedTo
        Optional.ofNullable(providedCHOType.getIsRelatedToList()).ifPresent(relatedTos -> {
            provided.getData().setIsRelatedToList(
                    relatedTos.stream()
                            .map(m -> {
                                try {
                                    resourceType(m);
                                    return m;
                                } catch (Exception e) {
                                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_isRelatedTo, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isRelatedTo\".level"))));
                                    return null;
                                }
                            })
                            .collect(Collectors.toList())
            );
        });

        return provided;
    }

}
