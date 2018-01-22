package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @author dfernandez
 */
public class Concept extends DataType implements InterfaceCoreClasses<eu.europeana.corelib.definitions.jibx.Concept> {

    private static Logger logger = LogManager.getLogger(Concept.class);

    public Concept(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public eu.europeana.corelib.definitions.jibx.Concept validate(eu.europeana.corelib.definitions.jibx.Concept data) {
        aboutType(data.getAbout());
        Optional.ofNullable(data.getChoiceList()).ifPresent(present->{
            present.forEach(choice -> {
                if(choice.ifPrefLabel())    literalType(choice.getPrefLabel());
                if(choice.ifRelated())  resourceType(choice.getRelated());
            });
        });
        return data;
    }
}
