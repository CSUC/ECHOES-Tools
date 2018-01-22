package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.HasView;
import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.ResourceType;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @author amartinez
 */
public class Aggregation extends DataType implements InterfaceCoreClasses<eu.europeana.corelib.definitions.jibx.Aggregation> {

    private static Logger logger = LogManager.getLogger(WebResource.class);

    public Aggregation(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public eu.europeana.corelib.definitions.jibx.Aggregation validate(eu.europeana.corelib.definitions.jibx.Aggregation data) {
        aboutType(data.getAbout());;

        Optional.ofNullable(data.getAggregatedCHO()).ifPresent(present-> resourceType(present));
        resourceOrLiteralType(data.getDataProvider());
        resourceOrLiteralType(data.getProvider());

        Optional.ofNullable(data.getHasViewList()).ifPresent(present-> present.forEach(hasView -> resourceType(hasView)));
        Optional.ofNullable(data.getIsShownAt()).ifPresent(present-> resourceType(present));
        Optional.ofNullable(data.getObject()).ifPresent(present-> resourceType(present));

        Optional.ofNullable(data.getRights()).ifPresent(present-> {
            resourceType(present);
            uriType(present.getResource());
        });

        Optional.ofNullable(data.getIntermediateProviderList()).ifPresent(present-> present.forEach(intermediateProvider -> resourceOrLiteralType(intermediateProvider)));

        return data;
    }
}
