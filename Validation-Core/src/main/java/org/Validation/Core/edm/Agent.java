package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.*;
import org.Validation.Core.DataTypeAgent;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Optional;

/**
 * @author dfernandez
 */
public class Agent extends DataTypeAgent implements InterfaceCoreClasses<AgentType> {

    private static Logger logger = LogManager.getLogger(Agent.class);
    private Instant inici = Instant.now();

    public Agent() {
        super();
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public AgentType validate(AgentType data) {
       
        AboutType aboutType = new AboutType();
        aboutType.setAbout(data.getAbout());
        aboutType(aboutType);
        
        Optional.ofNullable(data.getPrefLabelList()).ifPresent(p -> p.forEach(prefLabelList -> literalType(prefLabelList)));
        Optional.ofNullable(data.getAltLabelList()).ifPresent(p -> p.forEach(altLabelList -> literalType(altLabelList)));
        Optional.ofNullable(data.getHasMetList()).ifPresent(p -> p.forEach(hasMetList -> resourceType(hasMetList)));
        Optional.ofNullable(data.getIsRelatedToList()).ifPresent(p -> p.forEach(isRelatedTo -> resourceOrLiteralType(isRelatedTo)));
        // FIXME: Should dateOfBirth be a child class of DateType?
        if (data.getDateOfBirth() != null){
            if (dateOfBirthType(data.getDateOfBirth()) || literalType(data.getDateOfBirth())){
                return null;
            }
        }
        // FIXME: Should dateOfDate be a child class of DateType?
        if (data.getDateOfDeath() != null){
            if (dateOfDeathType(data.getDateOfDeath()) || literalType(data.getDateOfDeath())){
                return null;
            }
        }
        Optional.ofNullable(data.getGender()).ifPresent(p -> literalType(p));
        if (data.getPlaceOfBirth() != null){
            if (placeOfBirthType(data.getPlaceOfBirth()) || resourceOrLiteralType(data.getPlaceOfBirth())){
                return null;
            }
        }
        Optional.ofNullable(data.getProfessionOrOccupationList()).ifPresent(p -> p.forEach(professionOrOccupation -> resourceOrLiteralType(professionOrOccupation)));
        
        return null;
    }

}
