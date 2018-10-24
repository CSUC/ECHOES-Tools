package org.csuc.Validation.Core.edm;

import eu.europeana.corelib.definitions.jibx.Created;
import eu.europeana.corelib.definitions.jibx.Description;
import eu.europeana.corelib.definitions.jibx.Format;
import eu.europeana.corelib.definitions.jibx.WebResourceType;
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
public class WebResource extends DataType implements InterfaceCoreClasses<WebResourceType> {

    private static Logger logger = LogManager.getLogger(WebResource.class);

    public WebResource() {
        super();
        logger.debug("VALIDATION     {}", this.getClass().getSimpleName());
    }

    @Override
    public WebResourceType validate(WebResourceType data) {
        WebResourceType webResourceType = new WebResourceType();

        if(aboutType(data.getAbout()) && uriType(data.getAbout()))  webResourceType.setAbout(data.getAbout());
        else return null;

        Optional.ofNullable(data.getDescriptionList()).ifPresent((List<Description> present) -> webResourceType.setDescriptionList(present.stream().filter(this::resourceOrLiteralType).collect(Collectors.toList())));
        Optional.ofNullable(data.getFormatList()).ifPresent((List<Format> present) -> webResourceType.setFormatList(present.stream().filter(this::resourceOrLiteralType).collect(Collectors.toList())));
        Optional.ofNullable(data.getCreatedList()).ifPresent((List<Created> present) -> webResourceType.setCreatedList(present.stream().filter(this::resourceOrLiteralType).filter(this::dateType).collect(Collectors.toList())));

        Optional.ofNullable(data.getRights()).filter(this::resourceType).ifPresent(webResourceType::setRights);

        return webResourceType;
    }
}
