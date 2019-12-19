package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.text.MessageFormat;
import java.util.Objects;
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

        try{
            aboutType(providedCHOType.getAbout());
            provided.getData().setAbout(providedCHOType.getAbout());
        }catch (Exception e){
            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        provided.getData().setType(providedCHOType.getType());

        providedCHOType.getChoiceList().forEach(choice -> {
            //dc:contributor
            if (choice.ifContributor()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:contributor\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getContributor());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_contributor, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:contributor\".level"))));
                    }
                }
            }

            //dc:coverage
            if (choice.ifCoverage()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:coverage\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getCoverage());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_coverage, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:coverage\".level"))));
                    }
                }
            }
            //dc:creator
            if (choice.ifCreator()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:creator\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getCreator());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_creator, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:creator\".level"))));
                    }
                }
            }

            //dc:date
            if (choice.ifDate()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:date\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getDate());
                        //dateType()
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_date, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:date\".level"))));
                    }
                }
            }

            //dc:description
            if (choice.ifDescription()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:description\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getDescription());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_description, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:description\".level"))));
                    }
                }
            }

            //dc:format
            if (choice.ifFormat()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:format\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getFormat());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_format, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:format\".level"))));
                    }
                }
            }

            //dcterms:provenance
            if (choice.ifProvenance()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:provenance\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getProvenance());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_provenance, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:provenance\".level"))));
                    }
                }
            }

            //dc:identifier
            if (choice.ifIdentifier()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:identifier\".level")), LevelQuality.OFF)) {
                    try {
                        literalType(choice.getIdentifier());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_identifier, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:identifier\".level"))));
                    }
                }
            }

            //dc:language
            if (choice.ifLanguage()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:language\".level")), LevelQuality.OFF)) {
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
            }

            //dc:publisher
            if (choice.ifPublisher()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:publisher\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getPublisher());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_publisher, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:publisher\".level"))));
                    }
                }
            }

            //dcterms:spatial
            if (choice.ifSpatial()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:spatial\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getSpatial());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_spatial, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:spatial\".level"))));
                    }
                }
            }

            //dc:relation
            if (choice.ifRelation()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:relation\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getRelation());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_relation, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:relation\".level"))));
                    }
                }
            }

            //dc:rights
            if (choice.ifRights()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:rights\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getRights());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_rights, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:rights\".level"))));
                    }
                }
            }

            //dc:source
            if (choice.ifSource()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:source\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getSource());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_source, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:source\".level"))));
                    }
                }
            }

            //dcterms:temporal
            if (choice.ifTemporal()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:temporal\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getTemporal());
                        //dateType()
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_temporal, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:temporal\".level"))));
                    }
                }
            }

            //dc:subject
            if (choice.ifSubject()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:subject\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getSubject());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_subject, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:subject\".level"))));
                    }
                }
            }

            //dc:title
            if (choice.ifTitle()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:title\".level")), LevelQuality.OFF)) {
                    try {
                        literalType(choice.getTitle());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_title, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:title\".level"))));
                    }
                }
            }
            //dc:type
            if (choice.ifType()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:type\".level")), LevelQuality.OFF)) {
                    try {
                        resourceOrLiteralType(choice.getType());
                        provided.getData().getChoiceList().add(choice);
                    } catch (Exception e) {
                        provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dc_type, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:type\".level"))));
                    }
                }
            }

            //dcterms:isReplacedBy
            if (choice.ifSubject()) {
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isReplacedBy\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIsReplacedBy()).ifPresent(isReplacedBy -> {
                        try {
                            resourceOrLiteralType(isReplacedBy);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_isReplacedBy, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isReplacedBy\".level"))));
                        }
                    });
                }
            }

            //dcterms:isRequiredBy
            if(choice.ifIsRequiredBy()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isRequiredBy\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIsRequiredBy()).ifPresent(isRequiredBy -> {
                        try {
                            resourceOrLiteralType(isRequiredBy);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_isRequiredBy, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isRequiredBy\".level"))));
                        }
                    });
                }
            }

            //dcterms:issued
            if(choice.ifIssued()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:issued\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIssued()).ifPresent(issued -> {
                        try {
                            resourceOrLiteralType(issued);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_issued, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:issued\".level"))));
                        }
                    });
                }
            }

            //dcterms:isVersionOf
            if(choice.ifIsVersionOf()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isVersionOf\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIsVersionOf()).ifPresent(isVersionOf -> {
                        try {
                            resourceOrLiteralType(isVersionOf);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_isVersionOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isVersionOf\".level"))));
                        }
                    });
                }
            }

            //dcterms:medium
            if(choice.ifMedium()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:medium\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getMedium()).ifPresent(medium -> {
                        try {
                            resourceOrLiteralType(medium);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_medium, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:medium\".level"))));
                        }
                    });
                }
            }

            //dcterms:references
            if(choice.ifReferences()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:references\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getReferences()).ifPresent(references -> {
                        try {
                            resourceOrLiteralType(references);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_references, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:references\".level"))));
                        }
                    });
                }
            }

            //dcterms:requires
            if(choice.ifRequires()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:requires\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getRequires()).ifPresent(requires -> {
                        try {
                            resourceOrLiteralType(requires);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_requires, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:requires\".level"))));
                        }
                    });
                }
            }

            //dcterms:tableOfContents
            if(choice.ifTableOfContents()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:tableOfContents\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getTableOfContents()).ifPresent(tableOfContents -> {
                        try {
                            resourceOrLiteralType(tableOfContents);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_tableOfContents, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:tableOfContents\".level"))));
                        }
                    });
                }
            }

            //dcterms:alternative
            if(choice.ifAlternative()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:alternative\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getAlternative()).ifPresent(alternative -> {
                        try {
                            literalType(alternative);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_alternative, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:alternative\".level"))));
                        }
                    });
                }
            }

            //dcterms:conformsTo
            if(choice.ifConformsTo()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:conformsTo\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getConformsTo()).ifPresent(conformsTo -> {
                        try {
                            resourceOrLiteralType(conformsTo);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_conformsTo, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:conformsTo\".level"))));
                        }
                    });
                }
            }

            //dcterms:created
            if(choice.ifCreated()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:created\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getCreated()).ifPresent(created -> {
                        try {
                            resourceOrLiteralType(created);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_created, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:created\".level"))));
                        }
                    });
                }
            }

            //dcterms:extent
            if(choice.ifExtent()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:extent\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getExtent()).ifPresent(extent -> {
                        try {
                            resourceOrLiteralType(extent);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_extent, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:extent\".level"))));
                        }
                    });
                }
            }

            //dcterms:hasFormat
            if(choice.ifHasFormat()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasFormat\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getHasFormat()).ifPresent(hasFormat -> {
                        try {
                            resourceOrLiteralType(hasFormat);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_hasFormat, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasFormat\".level"))));
                        }
                    });
                }
            }

            //dcterms:hasPart
            if(choice.ifHasPart()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasPart\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getHasPart()).ifPresent(hasPart -> {
                        try {
                            resourceOrLiteralType(hasPart);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_hasPart, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasPart\".level"))));
                        }
                    });
                }
            }

            //dcterms:hasVersion
            if(choice.ifHasVersion()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasVersion\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getHasVersion()).ifPresent(hasVersion -> {
                        try {
                            resourceOrLiteralType(hasVersion);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_hasVersion, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasVersion\".level"))));
                        }
                    });
                }
            }

            //dcterms:isFormatOf
            if(choice.ifIsFormatOf()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isFormatOf\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIsFormatOf()).ifPresent(isFormatOf -> {
                        try {
                            resourceOrLiteralType(isFormatOf);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_isFormatOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isFormatOf\".level"))));
                        }
                    });
                }
            }

            //dcterms:isPartOf
            if(choice.ifIsPartOf()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIsPartOf()).ifPresent(isFormatOf -> {
                        try {
                            resourceOrLiteralType(isFormatOf);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_isPartOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level"))));
                        }
                    });
                }
            }

            //dcterms:isReferencedBy
            if(choice.ifIsReferencedBy()){
                if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isReferencedBy\".level")), LevelQuality.OFF)) {
                    Optional.ofNullable(choice.getIsReferencedBy()).ifPresent(isReferencedBy -> {
                        try {
                            resourceOrLiteralType(isReferencedBy);
                            provided.getData().getChoiceList().add(choice);
                        } catch (Exception e) {
                            provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.dcterms_isReferencedBy, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isReferencedBy\".level"))));
                        }
                    });
                }
            }
        });

        // edm:isRelatedTo
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isRelatedTo\".level")), LevelQuality.OFF)) {
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
        }

        //edm:currentLocation
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:currentLocation\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getCurrentLocation()).ifPresent(currentLocation -> {
                try {
                    resourceOrLiteralType(currentLocation);
                    provided.getData().setCurrentLocation(currentLocation);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_currentLocation, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:currentLocation\".level"))));
                }
            });
        }

        //edm:hasMet
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:hasMet\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getHasMetList()).ifPresent(hasMets -> {
                provided.getData().setHasMetList(hasMets.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_hasMet, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:hasMet\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:hasType
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:hasType\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getHasTypeList()).ifPresent(hasTypes -> {
                provided.getData().setHasTypeList(hasTypes.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_hasType, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:hasType\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:incorporates
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:incorporates\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getIncorporateList()).ifPresent(incorporates -> {
                provided.getData().setIncorporateList(incorporates.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_incorporates, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:incorporates\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:isDerivativeOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isDerivativeOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getIsDerivativeOfList()).ifPresent(isDerivativeOVES -> {
                provided.getData().setIsDerivativeOfList(isDerivativeOVES.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_isDerivativeOf, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isDerivativeOf\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:isNextInSequence
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getIsNextInSequenceList()).ifPresent(isNextInSequences -> {
                provided.getData().setIsNextInSequenceList(isNextInSequences.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_isNextInSequence, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:isRepresentationOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isRepresentationOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getIsRepresentationOf()).ifPresent(representationOf -> {
                try {
                    resourceType(representationOf);
                    provided.getData().setIsRepresentationOf(representationOf);
                } catch (Exception e) {
                    provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_isRepresentationOf, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level"))));
                }
            });
        }

        //edm:isSimilarTo
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isSimilarTo\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getIsSimilarToList()).ifPresent(isSimilarTos -> {
                provided.getData().setIsSimilarToList(isSimilarTos.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_isSimilarTo, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isSimilarTo\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:isSuccessorOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isSuccessorOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getIsSuccessorOfList()).ifPresent(isSuccessorOfs -> {
                provided.getData().setIsSuccessorOfList(isSuccessorOfs.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_isSuccessorOf, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isSuccessorOf\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:realizes
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:realizes\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getRealizeList()).ifPresent(realizes -> {
                provided.getData().setRealizeList(realizes.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.edm_realizes, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:realizes\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //owl:sameAs
        if(!Objects.equals(LevelQuality.convert(config.getString("\"owl:sameAs\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(providedCHOType.getSameAList()).ifPresent(sameAs -> {
                provided.getData().setSameAList(sameAs.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                provided.getErrorList().add(new Error(EntityType.ProvidedCHO, MetadataType.owl_sameAs, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"owl:sameAs\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        return provided;
    }
}
