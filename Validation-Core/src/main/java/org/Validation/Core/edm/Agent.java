package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.*;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author dfernandez
 */
public class Agent extends DataType implements InterfaceCoreClasses<AgentType> {

    private static Logger logger = LogManager.getLogger(Agent.class);
    private Instant inici = Instant.now();

    public Agent(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public AgentType validate(AgentType data) {
        aboutType(data.getAbout());

        Optional.ofNullable(data.getPrefLabelList()).ifPresent(p -> p.forEach(prefLabelList -> literalType(prefLabelList)));
        Optional.ofNullable(data.getAltLabelList()).ifPresent(p -> p.forEach(altLabelList -> literalType(altLabelList)));
        Optional.ofNullable(data.getHasMetList()).ifPresent(p -> p.forEach(hasMetList -> resourceType(hasMetList)));
        Optional.ofNullable(data.getIsRelatedToList()).ifPresent(p -> p.forEach(isRelatedTo -> resourceOrLiteralType(isRelatedTo)));

        if(Objects.nonNull(data.getDateOfBirth())){
            if(literalType(data.getDateOfBirth()) && dateType(data.getDateOfBirth()));
        }
        if(Objects.nonNull(data.getDateOfDeath())){
            if(literalType(data.getDateOfDeath()) && dateType(data.getDateOfDeath()));
        }

        Optional.ofNullable(data.getGender()).ifPresent(p -> literalType(p));

        if(Objects.nonNull(data.getPlaceOfBirth())) resourceOrLiteralType(data.getPlaceOfBirth());

        Optional.ofNullable(data.getProfessionOrOccupationList()).ifPresent(p -> p.forEach(professionOrOccupation -> resourceOrLiteralType(professionOrOccupation)));
        
        return data;
    }

}
