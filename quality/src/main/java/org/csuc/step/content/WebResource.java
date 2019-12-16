package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.WebResourceType;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.csuc.util.DataType.*;

public class WebResource {

    private Config config;

    public WebResource(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.WebResource quality(WebResourceType webResourceType) throws Exception {
        org.csuc.dao.entity.edm.WebResource webResource = new org.csuc.dao.entity.edm.WebResource();

        try{
            aboutType(webResourceType.getAbout());
            webResource.getData().setAbout(webResourceType.getAbout());
        }catch (Exception e){
            webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        //dc:description
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:description\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getDescriptionList()).ifPresent(descriptions -> {
                webResource.getData().setDescriptionList(
                        descriptions.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dc_description, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:description\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dc:format
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:format\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getFormatList()).ifPresent(formats -> {
                webResource.getData().setFormatList(
                        formats.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dc_format, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:format\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:created
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:created\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getCreatedList()).ifPresent(createds -> {
                webResource.getData().setCreatedList(
                        createds.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                //DateType()
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_created, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:created\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //edm:rights
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:rights\".level")), LevelQuality.OFF)) {
            if(Objects.nonNull(webResourceType.getRights())){
                try {
                    resourceType(webResourceType.getRights());
                    //UriType()
                    webResource.getData().setRights(webResourceType.getRights());
                } catch (Exception e) {
                    webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.edm_rights, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:rights\".level"))));
                }
            }
        }

        //dc:creator
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:creator\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getCreatorList()).ifPresent(creators -> {
                webResource.getData().setCreatorList(
                        creators.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dc_creator, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:creator\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:hasPart
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasPart\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getHasPartList()).ifPresent(hasParts -> {
                webResource.getData().setHasPartList(
                        hasParts.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_hasPart, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasPart\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:isFormatOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isFormatOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getIsFormatOfList()).ifPresent(isFormatOfs -> {
                webResource.getData().setIsFormatOfList(
                        isFormatOfs.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_isFormatOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isFormatOf\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:isPartOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getIsPartOfList()).ifPresent(isPartOfs -> {
                webResource.getData().setIsPartOfList(
                        isPartOfs.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_isPartOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dc:rights
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:rights\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getRightList()).ifPresent(rights -> {
                webResource.getData().setRightList(
                        rights.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dc_rights, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:rights\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:isReferencedBy
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isReferencedBy\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getIsReferencedByList()).ifPresent(isReferencedBIES -> {
                webResource.getData().setIsReferencedByList(
                        isReferencedBIES.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_isReferencedBy, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isReferencedBy\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dc:source
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:source\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getSourceList()).ifPresent(sources -> {
                webResource.getData().setSourceList(
                        sources.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dc_source, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:source\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:issued
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:issued\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getIssuedList()).ifPresent(issueds -> {
                webResource.getData().setIssuedList(
                        issueds.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_issued, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:issued\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //dcterms:conformsTo
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:conformsTo\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getConformsToList()).ifPresent(conformsTos -> {
                webResource.getData().setConformsToList(
                        conformsTos.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_conformsTo, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:conformsTo\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //edm:isNextInSequence
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level")), LevelQuality.OFF)) {
            if(Objects.nonNull(webResourceType.getIsNextInSequence())){
                try {
                    resourceType(webResourceType.getIsNextInSequence());
                    //UriType()
                    webResource.getData().setIsNextInSequence(webResourceType.getIsNextInSequence());
                } catch (Exception e) {
                    webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.edm_isNextInSequence, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level"))));
                }
            }
        }

        //dcterms:extent
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:extent\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getExtentList()).ifPresent(extents -> {
                webResource.getData().setExtentList(
                        extents.stream().map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.dcterms_extent, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:extent\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //owl:sameAs
        if(!Objects.equals(LevelQuality.convert(config.getString("\"owl:sameAs\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(webResourceType.getSameAList()).ifPresent(sameAs -> {
                webResource.getData().setSameAList(
                        sameAs.stream().map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.owl_sameAs, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"owl:sameAs\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        return webResource;
    }
}
