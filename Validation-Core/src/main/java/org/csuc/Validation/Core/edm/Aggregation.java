package org.csuc.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.*;
import org.csuc.Validation.Core.DataType;
import org.csuc.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author amartinez
 */
public class Aggregation extends DataType implements InterfaceCoreClasses<eu.europeana.corelib.definitions.jibx.Aggregation> {

    private static Logger logger = LogManager.getLogger(WebResource.class);

    public Aggregation() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public eu.europeana.corelib.definitions.jibx.Aggregation validate(eu.europeana.corelib.definitions.jibx.Aggregation data) {
        eu.europeana.corelib.definitions.jibx.Aggregation aggregation = new eu.europeana.corelib.definitions.jibx.Aggregation();

        if(aboutType(data.getAbout()))  aggregation.setAbout(data.getAbout());
        else return null;

        Optional.ofNullable(data.getAggregatedCHO()).filter(this::resourceType).ifPresent(aggregation::setAggregatedCHO);
        if(resourceOrLiteralType(data.getDataProvider())) aggregation.setDataProvider(data.getDataProvider());
        if(resourceOrLiteralType(data.getProvider())) aggregation.setProvider(data.getProvider());
        Optional.ofNullable(data.getHasViewList()).ifPresent((List<HasView> present) -> aggregation.setHasViewList(present.stream().filter(this::resourceType).collect(Collectors.toList())));


        Optional.ofNullable(data.getIsShownAt()).filter(this::resourceType).ifPresent(aggregation::setIsShownAt);
        Optional.ofNullable(data.getObject()).filter(this::resourceType).ifPresent(aggregation::setObject);
        Optional.ofNullable(data.getRights()).filter((Rights1 f) -> resourceType(f) && uriType(f.getResource())).ifPresent(aggregation::setRights);

        Optional.ofNullable(data.getIntermediateProviderList()).ifPresent((List<IntermediateProvider> present) -> aggregation.setIntermediateProviderList(present.stream().filter(this::resourceOrLiteralType).collect(Collectors.toList())));

        return aggregation;
    }
}
