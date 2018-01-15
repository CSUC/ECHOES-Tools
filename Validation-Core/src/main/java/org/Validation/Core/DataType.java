package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * @author amartinez
 */
public class DataType {

    private static Logger logger = LogManager.getLogger(DataType.class);

    public boolean resourceOrLiteralType(ResourceOrLiteralType choice) {
        logger.info("[{}]           Lang:    {}    Resource:    {}          String:   {}",
                choice.getClass().getSimpleName(),
                Objects.nonNull(choice.getLang()) ? choice.getLang().getLang() : null,
                Objects.nonNull(choice.getResource()) ? choice.getResource().getResource() : null,
                choice.getString());

        return true;
    }

    public boolean literalType(LiteralType choice) {
        logger.info("[{}]           Lang:    {}    Resource:    {}          String:   {}",
                choice.getClass().getSimpleName(),
                Objects.nonNull(choice.getLang()) ? choice.getLang().getLang() : null,
                null,
                choice.getString());

        return true;
    }

    public boolean resourceType(ResourceType choice) {
        logger.info("[{}]         Resource:       {}", choice.getClass().getSimpleName(), choice.getResource());

        return true;
    }

    public boolean dateType(DateType choice) {
        logger.info(choice.getDatatype());

        return true;
    }

    public boolean aboutType(AboutType choice) {
        logger.info(choice.getAbout());

        return true;
    }

    public boolean edmType(EdmType choice) {
        logger.info("[{}]              {}", choice.getClass().getSimpleName(), choice.xmlValue());

        return true;
    }

    public boolean countryCode(String country) {
        logger.info(CountryCodes.convert(country));

        return true;
    }

    public boolean languageCode(String language) {
        logger.info(LanguageCodes.convert(language));

        return true;
    }
}
