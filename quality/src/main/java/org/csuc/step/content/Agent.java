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

        if (aboutType(agentType.getAbout())) agent.getData().setAbout(agentType.getAbout());

        //skos:prefLabel
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

        //skos:altLabel
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

        //edm:hasMet
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

        //edm:isRelatedTo
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

        //rdaGr2:dateOfBirth
        if(Objects.nonNull(agentType.getDateOfBirth())){
            try {
                literalType(agentType.getDateOfBirth());
                agent.getData().setDateOfBirth(agentType.getDateOfBirth());
            } catch (Exception e) {
                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_dateOfBirth, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:dateOfBirth\".level"))));
            }
        }

        //rdaGr2:dateOfDeath
        if(Objects.nonNull(agentType.getDateOfDeath())){
            try {
                literalType(agentType.getDateOfDeath());
                agent.getData().setDateOfDeath(agentType.getDateOfDeath());
            } catch (Exception e) {
                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_dateOfDeath, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:dateOfDeath\".level"))));
            }
        }

        //rdaGr2:gender
        if(Objects.nonNull(agentType.getGender())){
            try {
                literalType(agentType.getGender());
                agent.getData().setGender(agentType.getGender());
            } catch (Exception e) {
                agent.getErrorList().add(new Error(EntityType.Agent, MetadataType.rdaGr2_gender, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"rdaGr2:gender\".level"))));
            }
        }

        //rdaGr2:placeOfBirth
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

        //rdaGr2:placeOfDeath
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

        //rdaGr2:professionOrOccupation
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

        //rdaGr2:biographicalInformation
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

        return agent;
    }
}
