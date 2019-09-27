package org.csuc.step.content;

import com.typesafe.config.Config;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.csuc.util.DataType.*;

public class Aggregation {

    private Config config;

    public Aggregation(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.Aggregation quality(eu.europeana.corelib.definitions.jibx.Aggregation aggregationType) throws Exception {
        org.csuc.dao.entity.edm.Aggregation aggregation = new org.csuc.dao.entity.edm.Aggregation();

        if (aboutType(aggregationType.getAbout())) aggregation.getData().setAbout(aggregationType.getAbout());

        //edm:aggregatedCHO
        if(Objects.nonNull(aggregationType.getAggregatedCHO())){
            try {
                resourceType(aggregationType.getAggregatedCHO());
                aggregation.getData().setAggregatedCHO(aggregationType.getAggregatedCHO());
            } catch (Exception e) {
                aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_aggregatedCHO, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:aggregatedCHO\".level"))));
            }
        }

        //edm:dataProvider
        if(Objects.nonNull(aggregationType.getDataProvider())){
            try {
                resourceOrLiteralType(aggregationType.getDataProvider());
                aggregation.getData().setDataProvider(aggregationType.getDataProvider());
            } catch (Exception e) {
                aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_dataProvider, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:dataProvider\".level"))));
            }
        }

        //edm:provider
        try {
            resourceOrLiteralType(aggregationType.getProvider());
            aggregation.getData().setProvider(aggregationType.getProvider());
        } catch (Exception e) {
            aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_provider, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:provider\".level"))));
        }

        //edm:hasView
        Optional.ofNullable(aggregationType.getHasViewList()).ifPresent(hasViews -> {
            aggregation.getData().setHasViewList(hasViews.stream()
                    .map(m -> {
                        try {
                            resourceType(m);
                            return m;
                        } catch (Exception e) {
                            aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_hasView, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:hasView\".level"))));
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        });

        //edm:isShownAt
        if(Objects.nonNull(aggregationType.getIsShownAt())){
            try {
                resourceType(aggregationType.getIsShownAt());
                aggregation.getData().setIsShownAt(aggregationType.getIsShownAt());
            } catch (Exception e) {
                aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_isShownAt, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isShownAt\".level"))));
            }
        }

        //edm:object
        if(Objects.nonNull(aggregationType.getObject())){
            try {
                resourceType(aggregationType.getObject());
                aggregation.getData().setObject(aggregationType.getObject());
            } catch (Exception e) {
                aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_object, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isShownAt\".level"))));
            }
        }

        //edm:rights
        if(Objects.nonNull(aggregationType.getRights())){
            try {
                resourceType(aggregationType.getRights());
                //UriType()
                aggregation.getData().setRights(aggregationType.getRights());
            } catch (Exception e) {
                aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_rights, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:rights\".level"))));
            }
        }

        //edm:intermediateProvider
        Optional.ofNullable(aggregationType.getIntermediateProviderList()).ifPresent(intermediateProviders -> {
            aggregation.getData().setIntermediateProviderList(intermediateProviders.stream()
                    .map(m -> {
                        try {
                            resourceOrLiteralType(m);
                            return m;
                        } catch (Exception e) {
                            aggregation.getErrorList().add(new Error(EntityType.Aggregation, MetadataType.edm_intermediateProvider, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:intermediateProvider\".level"))));
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        });

        return aggregation;
    }
}
