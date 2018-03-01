package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.*;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author amartinez
 */
public class ProvidedCHO extends DataType implements InterfaceCoreClasses<ProvidedCHOType> {

    private static Logger logger = LogManager.getLogger(ProvidedCHO.class);
    private Instant inici = Instant.now();


    public ProvidedCHO(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public ProvidedCHOType validate(ProvidedCHOType data) {
        ProvidedCHOType providedCHOType = new ProvidedCHOType();

        if(aboutType(data.getAbout()))  providedCHOType.setAbout(data.getAbout());
        else return null;

        data.getChoiceList().forEach((EuropeanaType.Choice c) -> {
            if (c.ifContributor()) {
                if(resourceOrLiteralType(c.getContributor())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setContributor(c.getContributor());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifCoverage()) {
                if(resourceOrLiteralType(c.getCoverage())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setCoverage(c.getCoverage());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifCreator()) {
                if(resourceOrLiteralType(c.getCreator())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setCreator(c.getCreator());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifDate()) {
                if(resourceOrLiteralType(c.getDate()) && dateType(c.getDate())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setDate(c.getDate());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifDescription()) {
                if(resourceOrLiteralType(c.getDescription())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setDescription(c.getDescription());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifFormat()) {
                if(resourceOrLiteralType(c.getFormat())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setFormat(c.getFormat());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifProvenance()) {
                if(resourceOrLiteralType(c.getProvenance())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setProvenance(c.getProvenance());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifIdentifier()) {
                if(literalType(c.getIdentifier())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setIdentifier(c.getIdentifier());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifLanguage()) {
                if(literalType(c.getLanguage()) && languageCode(c.getLanguage().getString())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setLanguage(c.getLanguage());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifPublisher()) {
                if(resourceOrLiteralType(c.getPublisher())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setPublisher(c.getPublisher());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifSpatial()) {
                if(resourceOrLiteralType(c.getSpatial())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setSpatial(c.getSpatial());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifRelation()) {
                if(resourceOrLiteralType(c.getRelation())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setRelation(c.getRelation());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifRights()) {
                if(resourceOrLiteralType(c.getRights())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setRights(c.getRights());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifSource()) {
                if(resourceOrLiteralType(c.getSource())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setSource(c.getSource());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifTemporal()) {
                if(resourceOrLiteralType(c.getTemporal()) && dateType(c.getTemporal())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setTemporal(c.getTemporal());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifSubject()) {
                if(resourceOrLiteralType(c.getSubject())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setSubject(c.getSubject());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifTitle()) {
                if(literalType(c.getTitle())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setTitle(c.getTitle());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifType()) {
                if(resourceOrLiteralType(c.getType())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setType(c.getType());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifAlternative()) {
                if(literalType(c.getAlternative())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setAlternative(c.getAlternative());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifCreated()) {
                if(resourceOrLiteralType(c.getCreated()) && dateType(c.getCreated())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setCreated(c.getCreated());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifExtent()) {
                if(resourceOrLiteralType(c.getExtent())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setExtent(c.getExtent());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifHasFormat()) {
                if(resourceOrLiteralType(c.getHasFormat())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setHasFormat(c.getHasFormat());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifHasPart()) {
                if(resourceOrLiteralType(c.getHasPart())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setHasPart(c.getHasPart());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifIsPartOf()) {
                if(resourceOrLiteralType(c.getIsPartOf())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setIsPartOf(c.getIsPartOf());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
            if (c.ifIsReferencedBy()) {
                if(resourceOrLiteralType(c.getIsReferencedBy())){
                    EuropeanaType.Choice choice = new EuropeanaType.Choice();
                    choice.setIsReferencedBy(c.getIsReferencedBy());
                    providedCHOType.getChoiceList().add(choice);
                }
            }
        });

        if(edmType(data.getType().getType()))   providedCHOType.setType(data.getType());
        else return null;

        Optional.ofNullable(data.getIsNextInSequenceList()).ifPresent(p -> p.forEach(isNextInSequence -> {
            if(resourceType(isNextInSequence)) providedCHOType.getIsNextInSequenceList().add(isNextInSequence);
        }));
        Optional.ofNullable(data.getIsRelatedToList()).ifPresent(p -> p.forEach(isRelatedTo -> {
            if(resourceOrLiteralType(isRelatedTo))  providedCHOType.getIsRelatedToList().add(isRelatedTo);
        }));

        return data;
    }

}
