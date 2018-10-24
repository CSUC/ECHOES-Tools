package org.csuc.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.Begin;
import eu.europeana.corelib.definitions.jibx.End;
import eu.europeana.corelib.definitions.jibx.PrefLabel;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import org.EDM.Transformations.formats.utils.TimeUtil;
import org.csuc.Validation.Core.DataType;
import org.csuc.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author amartinez
 */
public class TimeSpan extends DataType implements InterfaceCoreClasses<TimeSpanType> {

    private static Logger logger = LogManager.getLogger(TimeSpan.class);

    public TimeSpan() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public TimeSpanType validate(TimeSpanType data) {
        TimeSpanType timeSpanType = new TimeSpanType();

        if(aboutType(data.getAbout()))  timeSpanType.setAbout(data.getAbout());
        else return null;

        Optional.ofNullable(data.getPrefLabelList()).ifPresent((List<PrefLabel> present) -> timeSpanType.setPrefLabelList(present.stream().filter(this::literalType).filter(this::dateType).collect(Collectors.toList())));
        if(Objects.nonNull(data.getBegin())){
            if(literalType(data.getBegin()) && dateType(data.getBegin())){
                Begin begin = new Begin();
                begin.setString(TimeUtil.format(begin.getString()));

                timeSpanType.setBegin(begin);
            }
        }
        if(Objects.nonNull(data.getEnd())){
            if(literalType(data.getEnd()) && dateType(data.getEnd())){
                End end = new End();
                end.setString(data.getEnd().getString());

                timeSpanType.setEnd(end);
            }
        }

        return timeSpanType;
    }
}

