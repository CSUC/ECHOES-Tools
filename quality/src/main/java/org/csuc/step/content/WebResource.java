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

        if (aboutType(webResourceType.getAbout())) webResource.getData().setAbout(webResourceType.getAbout());

        //dc:description
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

        //dc:format
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

        //dcterms:created
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

        //edm:rights
        if(Objects.nonNull(webResourceType.getRights())){
            try {
                resourceType(webResourceType.getRights());
                //UriType()
                webResource.getData().setRights(webResourceType.getRights());
            } catch (Exception e) {
                webResource.getErrorList().add(new Error(EntityType.WebResource, MetadataType.edm_rights, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:rights\".level"))));
            }
        }

        return webResource;
    }
}
