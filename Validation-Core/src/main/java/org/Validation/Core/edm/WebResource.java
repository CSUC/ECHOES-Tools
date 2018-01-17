package org.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.WebResourceType;
import org.Validation.Core.DataType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @author amartinez
 */
public class WebResource extends DataType implements InterfaceCoreClasses<WebResourceType> {

    private static Logger logger = LogManager.getLogger(WebResource.class);

    public WebResource(List<RDF.Choice> listChoice) {
        super(listChoice);
        logger.info("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public WebResourceType validate(WebResourceType data) {
        aboutType(data.getAbout());
        uriType(data.getAbout());

        Optional.ofNullable(data.getDescriptionList()).ifPresent(present-> present.forEach(description -> resourceOrLiteralType(description)));
        Optional.ofNullable(data.getFormatList()).ifPresent(present-> present.forEach(format -> resourceOrLiteralType(format)));
        Optional.ofNullable(data.getCreatedList()).ifPresent(present-> present.forEach(created -> resourceOrLiteralType(created)));

        Optional.ofNullable(data.getRights()).ifPresent(present-> resourceType(present.getResource()));

        return null;
    }
}
