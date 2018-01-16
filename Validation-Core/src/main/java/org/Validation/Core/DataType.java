package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author amartinez
 */
public class DataType {

    private static Logger logger = LogManager.getLogger(DataType.class);
    private List<RDF.Choice> listChoice;

    public DataType(List<RDF.Choice> listChoice) {
        this.listChoice = listChoice;
    }

    /**
     * @param choice
     * @return
     */
    protected boolean resourceOrLiteralType(ResourceOrLiteralType choice) {
        logger.info("[{}]", choice.getClass().getSimpleName());

        Optional.ofNullable(choice.getLang()).ifPresent(p -> languageCode(p.getLang()));
        Optional.ofNullable(choice.getResource()).ifPresent(p -> resourceType(p.getResource()));
        Optional.ofNullable(choice.getString()).filter(f -> !f.isEmpty()).ifPresent(p -> stringType(p));

        return true;
    }

    /**
     * @param choice
     * @return
     */
    protected boolean literalType(LiteralType choice) {
        logger.info("[{}]", choice.getClass().getSimpleName());

        Optional.ofNullable(choice.getLang()).ifPresent(p -> languageCode(p.getLang()));
        Optional.ofNullable(choice.getString()).ifPresent(p -> stringType(p));

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean resourceType(String value) {
        logger.info("resource      \"{}\"", value);
        if (stringType(value) && !StringUtils.containsWhitespace(value)) {
            Predicate<RDF.Choice> predicate = e -> {
                if (e.ifProvidedCHO())
                    return Objects.equals(value, e.getProvidedCHO().getAbout());
                else if (e.ifAgent())
                    return Objects.equals(value, e.getAgent().getAbout());
                else if (e.ifAggregation())
                    return Objects.equals(value, e.getAggregation().getAbout());
                else if (e.ifConcept())
                    return Objects.equals(value, e.getConcept().getAbout());
                else if (e.ifLicense())
                    return Objects.equals(value, e.getLicense().getAbout());
                else if (e.ifPlace())
                    return Objects.equals(value, e.getPlace().getAbout());
                else if (e.ifService())
                    return Objects.equals(value, e.getService().getAbout());
                else if (e.ifTimeSpan())
                    return Objects.equals(value, e.getTimeSpan().getAbout());
                else if (e.ifWebResource())
                    return Objects.equals(value, e.getWebResource().getAbout());
                return false;
            };
            return listChoice.stream().anyMatch(predicate);
        }
        return false;

    }

    /**
     * @param input
     * @return
     */
    protected boolean dateType(String input) {
        logger.info("date     {}", input);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean aboutType(String value) {
        logger.info("about      \"{}\"", value);

        return stringType(value) ? !StringUtils.containsWhitespace(value) : false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean edmType(String value) {
        EdmType type = EdmType.convert(value);
        logger.info("{}       {}", type.getClass().getSimpleName(), type.xmlValue());

        return true;
    }

    /**
     * @param country
     * @return
     */
    protected boolean countryCode(String country) {
        logger.info("countryCode      {}", CountryCodes.convert(country));

        return true;
    }

    /**
     * @param language
     * @return
     */
    protected boolean languageCode(String language) {
        logger.info("language     {}", LanguageCodes.convert(language));

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean stringType(String value) {
        return Optional.ofNullable(value).filter(f -> !StringUtils.isBlank(value)).isPresent();
    }

    /**
     * @param value
     * @return
     */
    protected boolean longType(String value) {
        logger.info("long     {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean integerType(String value) {
        logger.info("integer     {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean nonNegativeIntegerType(String value) {
        logger.info("nonNegativeInteger     {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean doubleType(String value) {
        logger.info("double       {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean hexBinaryString(String value) {
        logger.info("hexBinary     {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean UGCType(String value) {
        logger.info("UGC     {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean colorSpaceType(String value) {
        logger.info("colorSpace     {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean floatType(String value) {
        logger.info("float        {}", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean placeType(String value) {
        logger.info("place     {}", value);

        return true;
    }
}
