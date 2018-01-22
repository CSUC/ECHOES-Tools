package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author amartinez
 */
public class TimeSpan extends DataType implements InterfaceCoreClasses<TimeSpanType> {

    private static Logger logger = LogManager.getLogger(TimeSpan.class);

    public TimeSpan(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public TimeSpanType validate(TimeSpanType data) {
        aboutType(data.getAbout());
        Optional.ofNullable(data.getPrefLabelList()).ifPresent(present-> present.forEach(prefLabel -> {
            if(literalType(prefLabel) && dateType(prefLabel));
        }));
        if(Objects.nonNull(data.getBegin())){
            if(literalType(data.getBegin()) && dateType(data.getBegin()));
        }
        if(Objects.nonNull(data.getEnd())){
            if(literalType(data.getEnd()) && dateType(data.getEnd()));
        }

        return data;
    }
}

