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

import static org.csuc.util.DataType.aboutType;
import static org.csuc.util.DataType.literalType;

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

        return timeSpan;
    }
}
