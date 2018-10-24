package org.csuc.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.*;
import org.csuc.Validation.Core.DataType;
import org.csuc.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dfernandez
 */
public class Place extends DataType implements InterfaceCoreClasses<PlaceType> {

    private static Logger logger = LogManager.getLogger(Place.class);

    public Place() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public PlaceType validate(PlaceType data) {
        PlaceType placeType = new PlaceType();

        if(aboutType(data.getAbout()))  placeType.setAbout(data.getAbout());
        else return null;

        if(Objects.nonNull(data.getLat()) && floatType(data.getLat().toString())) placeType.setLat(data.getLat());
        if(Objects.nonNull(data.getLong()) && floatType(data.getLong().toString())) placeType.setLong(data.getLong());
        Optional.ofNullable(data.getNoteList()).ifPresent((List<Note> present) -> placeType.setNoteList(present.stream().filter(this::literalType).collect(Collectors.toList())));
        Optional.ofNullable(data.getPrefLabelList()).ifPresent((List<PrefLabel> present) -> placeType.setPrefLabelList(present.stream().filter(this::literalType).collect(Collectors.toList())));
        Optional.ofNullable(data.getAltLabelList()).ifPresent((List<AltLabel> present) -> placeType.setAltLabelList(present.stream().filter((AltLabel f) -> placeType(f.getString())).collect(Collectors.toList())));

        return placeType;
    }
}
