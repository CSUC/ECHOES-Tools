package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.PlaceType;
import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.WebResourceType;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @author dfernandez
 */
public class Place extends DataType implements InterfaceCoreClasses<PlaceType> {

    private static Logger logger = LogManager.getLogger(Place.class);

    public Place(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public PlaceType validate(PlaceType data) {
        aboutType(data.getAbout());        
        floatType(data.getLat().toString());
        floatType(data.getLong().toString());        
        Optional.ofNullable(data.getNoteList()).ifPresent(present-> present.forEach(note -> literalType(note)));
        Optional.ofNullable(data.getPrefLabelList()).ifPresent(present-> present.forEach(prefLabel -> literalType(prefLabel)));
        Optional.ofNullable(data.getAltLabelList()).ifPresent(present-> present.forEach(altLabel -> placeType(altLabel.getString())));
        return null;
    }
}
