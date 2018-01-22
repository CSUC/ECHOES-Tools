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
        data.getAbout();

        aboutType(data.getAbout());

        data.getChoiceList().forEach((EuropeanaType.Choice c) -> {
            if (c.ifContributor()) {
                resourceOrLiteralType(c.getContributor());
            }
            if (c.ifCoverage()) {
                resourceOrLiteralType(c.getCoverage());
            }
            if (c.ifCreator()) {
                resourceOrLiteralType(c.getCreator());
            }
            if (c.ifDate()) {
                if(resourceOrLiteralType(c.getDate()) && dateType(c.getDate()));

            }
            if (c.ifDescription()) {
                resourceOrLiteralType(c.getDescription());
            }
            if (c.ifFormat()) {
                resourceOrLiteralType(c.getFormat());
            }
            if (c.ifProvenance()) {
                resourceOrLiteralType(c.getProvenance());
            }
            if (c.ifIdentifier()) {
                literalType(c.getIdentifier());
            }
            if (c.ifLanguage()) {
                if(literalType(c.getLanguage()) && languageCode(c.getLanguage().getString()));
            }
            if (c.ifPublisher()) {
                resourceOrLiteralType(c.getPublisher());
            }
            if (c.ifSpatial()) {
                resourceOrLiteralType(c.getSpatial());
            }
            if (c.ifRelation()) {
                resourceOrLiteralType(c.getRelation());
            }
            if (c.ifRights()) {
                resourceOrLiteralType(c.getRights());
            }
            if (c.ifSource()) {
                resourceOrLiteralType(c.getSource());
            }
            if (c.ifTemporal()) {
                if(resourceOrLiteralType(c.getTemporal()) && dateType(c.getTemporal()));
            }
            if (c.ifSubject()) {
                resourceOrLiteralType(c.getSubject());
            }
            if (c.ifTitle()) {
                literalType(c.getTitle());
            }
            if (c.ifType()) {
                resourceOrLiteralType(c.getType());
            }
            if (c.ifAlternative()) {
                literalType(c.getAlternative());
            }
            if (c.ifCreated()) {
                if(resourceOrLiteralType(c.getCreated()) && dateType(c.getCreated()));
            }
            if (c.ifExtent()) {
                resourceOrLiteralType(c.getExtent());
            }
            if (c.ifHasFormat()) {
                resourceOrLiteralType(c.getHasFormat());
            }
            if (c.ifHasPart()) {
                resourceOrLiteralType(c.getHasPart());
            }
            if (c.ifIsPartOf()) {
                resourceOrLiteralType(c.getIsPartOf());
            }
            if (c.ifIsReferencedBy()) {
                resourceOrLiteralType(c.getIsReferencedBy());
            }
        });

        edmType(data.getType().getType());

        Optional.ofNullable(data.getIsNextInSequenceList()).ifPresent(p -> p.forEach(isNextInSequence -> resourceType(isNextInSequence)));
        Optional.ofNullable(data.getIsRelatedToList()).ifPresent(p -> p.forEach(isRelatedTo -> resourceOrLiteralType(isRelatedTo)));

        return data;
    }

}
