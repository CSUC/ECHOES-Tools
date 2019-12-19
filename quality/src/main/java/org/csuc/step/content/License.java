package org.csuc.step.content;

import com.typesafe.config.Config;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;
import java.util.Optional;

import static org.csuc.util.DataType.*;

public class License {

    private Config config;

    public License(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.License quality(eu.europeana.corelib.definitions.jibx.License licenseType) throws Exception {
        org.csuc.dao.entity.edm.License license = new org.csuc.dao.entity.edm.License();

        try{
            aboutType(licenseType.getAbout());
            license.getData().setAbout(licenseType.getAbout());
        }catch (Exception e){
            license.getErrorList().add(new Error(EntityType.License, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        //odrl:inheritFrom
        if (!Objects.equals(LevelQuality.convert(config.getString("\"odrl:inheritFrom\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(licenseType.getInheritFrom()).ifPresent(inheritFrom -> {
                try {
                    resourceType(inheritFrom);
                    license.getData().setInheritFrom(inheritFrom);
                } catch (Exception e) {
                    license.getErrorList().add(new Error(EntityType.License, MetadataType.odrl_inheritFrom, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"odrl:inheritFrom\".level"))));
                }
            });
        }

        //cc:deprecatedOn
        if (!Objects.equals(LevelQuality.convert(config.getString("\"cc:deprecatedOn\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(licenseType.getDeprecatedOn()).ifPresent( dateType-> {
                try {
                    //literalType(dateType);
                    //dateType()
                    license.getData().setDeprecatedOn(dateType);
                } catch (Exception e) {
                    license.getErrorList().add(new Error(EntityType.License, MetadataType.cc_deprecatedOn, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"cc:deprecatedOn\".level"))));
                }
            });
        }

        return license;
    }



}
