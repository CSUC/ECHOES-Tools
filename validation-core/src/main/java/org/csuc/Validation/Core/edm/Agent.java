package org.csuc.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.*;
import org.EDM.Transformations.formats.utils.TimeUtil;
import org.csuc.Validation.Core.DataType;
import org.csuc.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dfernandez
 */
public class Agent extends DataType implements InterfaceCoreClasses<AgentType> {

    private static Logger logger = LogManager.getLogger(Agent.class);
    private Instant inici = Instant.now();

    public Agent() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public AgentType validate(AgentType data) {
        AgentType agentType = new AgentType();

        if(aboutType(data.getAbout()))  agentType.setAbout(data.getAbout());
        else return null;

        Optional.ofNullable(data.getPrefLabelList()).ifPresent((List<PrefLabel> p) -> agentType.setPrefLabelList(p.stream().filter(this::literalType).collect(Collectors.toList())));
        Optional.ofNullable(data.getAltLabelList()).ifPresent((List<AltLabel> p) -> agentType.setAltLabelList(p.stream().filter(this::literalType).collect(Collectors.toList())));
        Optional.ofNullable(data.getHasMetList()).ifPresent((List<HasMet> p) -> agentType.setHasMetList(p.stream().filter(this::resourceType).collect(Collectors.toList())));
        Optional.ofNullable(data.getIsRelatedToList()).ifPresent((List<IsRelatedTo> p) -> agentType.setIsRelatedToList(p.stream().filter(this::resourceOrLiteralType).collect(Collectors.toList())));

        if(Objects.nonNull(data.getDateOfBirth())){
            if(literalType(data.getDateOfBirth()) && dateType(data.getDateOfBirth())){
                DateOfBirth dateOfBirth = new DateOfBirth();
                dateOfBirth.setString(TimeUtil.format(data.getDateOfDeath().getString()));

                agentType.setDateOfBirth(data.getDateOfBirth());
            }
        }
        if(Objects.nonNull(data.getDateOfDeath())){
            if(literalType(data.getDateOfDeath()) && dateType(data.getDateOfDeath())){
                DateOfDeath dateOfDeath = new DateOfDeath();
                dateOfDeath.setString(TimeUtil.format(TimeUtil.format(data.getDateOfDeath().getString())));

                agentType.setDateOfDeath(dateOfDeath);
            }
        }

        Optional.ofNullable(data.getGender()).filter(this::literalType).ifPresent(agentType::setGender);

        if(Objects.nonNull(data.getPlaceOfBirth()) && resourceOrLiteralType(data.getPlaceOfBirth()))
            agentType.setPlaceOfBirth(data.getPlaceOfBirth());

        Optional.ofNullable(data.getProfessionOrOccupationList()).ifPresent((List<ProfessionOrOccupation> p) -> agentType.setProfessionOrOccupationList(p.stream().filter(this::resourceOrLiteralType).collect(Collectors.toList())));
        
        return agentType;
    }

}
