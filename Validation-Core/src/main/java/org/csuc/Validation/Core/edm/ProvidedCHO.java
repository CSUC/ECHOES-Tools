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
 * @author amartinez
 */
public class ProvidedCHO extends DataType implements InterfaceCoreClasses<ProvidedCHOType> {

    private static Logger logger = LogManager.getLogger(ProvidedCHO.class);
    private Instant inici = Instant.now();


    public ProvidedCHO() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public ProvidedCHOType validate(ProvidedCHOType data) {
        ProvidedCHOType providedCHOType = new ProvidedCHOType();

        if(aboutType(data.getAbout()))  providedCHOType.setAbout(data.getAbout());
        else return null;

        data.getChoiceList().forEach((EuropeanaType.Choice c) -> {
            if (c.ifContributor()) {
                if(resourceOrLiteralType(c.getContributor())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifCoverage()) {
                if(resourceOrLiteralType(c.getCoverage())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifCreator()) {
                if(resourceOrLiteralType(c.getCreator())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifDate()) {
                if(resourceOrLiteralType(c.getDate())) {
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    Date date = new Date();
                    date.setResource(c.getDate().getResource());
                    if(!c.getDate().getString().isEmpty() && dateType(c.getDate()))    date.setString(TimeUtil.format(c.getDate().getString()));
                    else    date.setString("");

                    choice.setDate(date);

                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifDescription()) {
                if(resourceOrLiteralType(c.getDescription())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifFormat()) {
                if(resourceOrLiteralType(c.getFormat())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifProvenance()) {
                if(resourceOrLiteralType(c.getProvenance())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifIdentifier()) {
                if(literalType(c.getIdentifier())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifLanguage()) {
                if(literalType(c.getLanguage()) && languageCode(c.getLanguage().getString()))
                    providedCHOType.getChoiceList().add(c);
            }
            if (c.ifPublisher()) {
                if(resourceOrLiteralType(c.getPublisher())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifSpatial()) {
                if(resourceOrLiteralType(c.getSpatial())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifRelation()) {
                if(resourceOrLiteralType(c.getRelation())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifRights()) {
                if(resourceOrLiteralType(c.getRights())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifSource()) {
                if(resourceOrLiteralType(c.getSource())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifTemporal()) {
                if(resourceOrLiteralType(c.getTemporal())) {
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    Temporal temporal = new Temporal();

                    temporal.setResource(c.getTemporal().getResource());
                    if (!c.getTemporal().getString().isEmpty() && dateType(c.getTemporal()))   temporal.setString(TimeUtil.format(c.getTemporal().getString()));
                    else temporal.setString("");

                    choice.setTemporal(temporal);

                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifSubject()) {
                if(resourceOrLiteralType(c.getSubject())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifTitle()) {
                if(literalType(c.getTitle())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifType()) {
                if(resourceOrLiteralType(c.getType())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifAlternative()) {
                if(literalType(c.getAlternative())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifCreated()) {
                if(resourceOrLiteralType(c.getCreated())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    Created created = new Created();

                    created.setResource(c.getCreated().getResource());
                    if (!c.getCreated().getString().isEmpty() && dateType(c.getCreated()))   created.setString(TimeUtil.format(c.getCreated().getString()));
                    else     created.setString("");

                    choice.setCreated(created);

                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifExtent()) {
                if(resourceOrLiteralType(c.getExtent())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifHasFormat()) {
                if(resourceOrLiteralType(c.getHasFormat())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifHasPart()) {
                if(resourceOrLiteralType(c.getHasPart())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifIsPartOf()) {
                if(resourceOrLiteralType(c.getIsPartOf())) providedCHOType.getChoiceList().add(c);
            }
            if (c.ifIsReferencedBy()) {
                if(resourceOrLiteralType(c.getIsReferencedBy())) providedCHOType.getChoiceList().add(c);
            }
        });

        if(edmType(data.getType().getType()))   providedCHOType.setType(data.getType());
        else return null;

        Optional.ofNullable(data.getIsNextInSequenceList()).ifPresent((List<IsNextInSequence> p) -> providedCHOType.setIsNextInSequenceList(p.stream().filter(this::resourceType).collect(Collectors.toList())));
        Optional.ofNullable(data.getIsRelatedToList()).ifPresent((List<IsRelatedTo> p) -> providedCHOType.setIsRelatedToList(p.stream().filter(this::resourceOrLiteralType).collect(Collectors.toList())));

        return providedCHOType;
    }

}
