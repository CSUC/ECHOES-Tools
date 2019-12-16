package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.AgentType;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.csuc.util.DataType.*;

public class Agent {

    private Config config;

    public Agent(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.Agent quality(AgentType agentType) throws Exception {
        org.csuc.dao.entity.edm.Agent agent = new org.csuc.dao.entity.edm.Agent();

        try{
            aboutType(agentType.getAbout());
            agent.getData().setAbout(agentType.getAbout());
        }catch (Exception e) {
            agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        //skos:prefLabel
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:prefLabel\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getPrefLabelList()).ifPresent(prefLabels -> {
                agent.getData().setPrefLabelList(
                        prefLabels.stream().map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.skos_prefLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:prefLabel\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //skos:altLabel
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:altLabel\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getAltLabelList()).ifPresent(altLabels -> {
                agent.getData().setAltLabelList(altLabels.stream()
                        .map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.skos_altLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:altLabel\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:hasMet
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:hasMet\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getHasMetList()).ifPresent(hasMets -> {
                agent.getData().setHasMetList(hasMets.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.edm_hasMet, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:hasMet\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:isRelatedTo
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isRelatedTo\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getIsRelatedToList()).ifPresent(relatedTos -> {
                agent.getData().setIsRelatedToList(relatedTos.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.edm_isRelatedTo, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isRelatedTo\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //rdaGr2:dateOfBirth
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:dateOfBirth\".level")), LevelQuality.OFF)) {
            if(Objects.nonNull(agentType.getDateOfBirth())){
                try {
                    literalType(agentType.getDateOfBirth());
                    agent.getData().setDateOfBirth(agentType.getDateOfBirth());
                } catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_dateOfBirth, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:dateOfBirth\".level"))));
                }
            }
        }

        //rdaGr2:dateOfDeath
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:dateOfDeath\".level")), LevelQuality.OFF)) {
            if(Objects.nonNull(agentType.getDateOfDeath())){
                try {
                    literalType(agentType.getDateOfDeath());
                    agent.getData().setDateOfDeath(agentType.getDateOfDeath());
                } catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_dateOfDeath, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:dateOfDeath\".level"))));
                }
            }
        }

        //rdaGr2:gender
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:gender\".level")), LevelQuality.OFF)) {
            if(Objects.nonNull(agentType.getGender())){
                try {
                    literalType(agentType.getGender());
                    agent.getData().setGender(agentType.getGender());
                } catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_gender, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:gender\".level"))));
                }
            }
        }

        //rdaGr2:placeOfBirth
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:placeOfBirth\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getPlaceOfBirthList()).ifPresent(placeOfBirths -> {
                agent.getData().setPlaceOfBirthList(placeOfBirths.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_placeOfBirth, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:placeOfBirth\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //rdaGr2:placeOfDeath
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:placeOfDeath\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getPlaceOfDeathList()).ifPresent(placeOfDeaths -> {
                agent.getData().setPlaceOfDeathList(placeOfDeaths.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_placeOfDeath, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:placeOfDeath\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //rdaGr2:professionOrOccupation
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:professionOrOccupation\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getProfessionOrOccupationList()).ifPresent(professionOrOccupations -> {
                agent.getData().setProfessionOrOccupationList(professionOrOccupations.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_professionOrOccupation, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:professionOrOccupation\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //rdaGr2:biographicalInformation
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:biographicalInformation\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getBiographicalInformationList()).ifPresent(biographicalInformations -> {
                agent.getData().setBiographicalInformationList(biographicalInformations.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_biographicalInformation, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:biographicalInformation\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //foaf:name
        if(!Objects.equals(LevelQuality.convert(config.getString("\"foaf:name\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getNameList()).ifPresent(nameList -> {
                agent.getData().setNameList(nameList.stream()
                        .map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.foaf_name, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"foaf:name\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //skos:note
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:note\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getNoteList()).ifPresent(noteList -> {
                agent.getData().setNoteList(noteList.stream()
                        .map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.skos_note, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:note\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //dc:date
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:date\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getDateList()).ifPresent(dateList -> {
                agent.getData().setDateList(dateList.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.dc_date, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:date\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //dc:identifier
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dc:identifier\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getIdentifierList()).ifPresent(identifiers -> {
                agent.getData().setIdentifierList(identifiers.stream()
                        .map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.dc_identifier, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dc:identifier\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //rdaGr2:dateOfEstablishment
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:dateOfEstablishment\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getDateOfEstablishment()).ifPresent(dateOfEstablishment -> {
                try{
                    literalType(dateOfEstablishment);
                    agent.getData().setDateOfEstablishment(dateOfEstablishment);
                }catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_dateOfEstablishment, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:dateOfEstablishment\".level"))));
                }
            });
        }

        //dcterms:hasPart
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasPart\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getHasPartList()).ifPresent(hasParts -> {
                agent.getData().setHasPartList(hasParts.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.dcterms_hasPart, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasPart\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //rdaGr2:dateOfTermination
        if(!Objects.equals(LevelQuality.convert(config.getString("\"rdaGr2:dateOfTermination\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getDateOfTermination()).ifPresent(dateOfTermination -> {
                try{
                    literalType(dateOfTermination);
                    agent.getData().setDateOfTermination(dateOfTermination);
                }catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_dateOfTermination, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:dateOfTermination\".level"))));
                }
            });
        }

        //dcterms:isPartOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getIsPartOfList()).ifPresent(isPartOfs -> {
                agent.getData().setIsPartOfList(isPartOfs.stream()
                        .map(m -> {
                            try {
                                resourceOrLiteralType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.dcterms_isPartOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //edm:begin
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:begin\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getBegin()).ifPresent(begin -> {
                try{
                    literalType(begin);
                    agent.getData().setBegin(begin);
                }catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.edm_begin, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:begin\".level"))));
                }
            });
        }

        //edm:end
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:end\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getEnd()).ifPresent(end -> {
                try{
                    literalType(end);
                    agent.getData().setEnd(end);
                }catch (Exception e) {
                    agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.edm_end, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:end\".level"))));
                }
            });
        }

        //owl:sameAs
        if(!Objects.equals(LevelQuality.convert(config.getString("\"owl:sameAs\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(agentType.getSameAList()).ifPresent(sameAs -> {
                agent.getData().setSameAList(sameAs.stream()
                        .map(m -> {
                            try {
                                resourceType(m);
                                return m;
                            } catch (Exception e) {
                                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.owl_sameAs, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"owl:sameAs\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        return agent;
    }
}
