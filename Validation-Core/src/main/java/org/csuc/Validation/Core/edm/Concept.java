package org.csuc.Validation.Core.edm;

import org.csuc.Validation.Core.DataType;
import org.csuc.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dfernandez
 */
public class Concept extends DataType implements InterfaceCoreClasses<eu.europeana.corelib.definitions.jibx.Concept> {

    private static Logger logger = LogManager.getLogger(Concept.class);

    public Concept() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public eu.europeana.corelib.definitions.jibx.Concept validate(eu.europeana.corelib.definitions.jibx.Concept data) {
        eu.europeana.corelib.definitions.jibx.Concept concept = new eu.europeana.corelib.definitions.jibx.Concept();

        if(aboutType(data.getAbout()))  concept.setAbout(data.getAbout());
        else return null;

        Optional.ofNullable(data.getChoiceList()).ifPresent(present-> concept.setChoiceList(present.stream()
                .filter(f-> (f.ifPrefLabel() && literalType(f.getPrefLabel())) || (f.ifRelated() && resourceType(f.getRelated()))).collect(Collectors.toList())));

        return concept;
    }
}
