package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
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
        if (Objects.nonNull(choice.getLang())) {
            if (!languageCode(choice.getLang().getLang())) {
                logger.debug("[{}]      Lang        \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getLang().getLang(), false);
                return false;
            }
        }
        if (Objects.nonNull(choice.getResource())) {
            if (!resourceType(choice.getResource().getResource())) {
                logger.debug("[{}]      Resource    \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getResource().getResource(), false);
                return false;
            }
            if(Objects.nonNull(choice.getString()) && !choice.getString().isEmpty()){
                return false;
            }
        }
        if (Objects.nonNull(choice.getString())){
            if(Objects.nonNull(choice.getResource()))   return false;
            else if(!stringType(choice.getString())){
                logger.debug("[{}]      String  \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getString(), false);
                return false;
            }
        }
        logger.debug("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), choice.toString(),true);
        return true;
    }

    /**
     * @param choice
     * @return
     */
    protected boolean literalType(LiteralType choice) {
        if(Objects.nonNull(choice.getLang()))
            if(!languageCode(choice.getLang().getLang())){
                logger.debug("[{}]      Lang        \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getLang().getLang(), false);
                return false;
            }
        if(Objects.nonNull(choice.getString()))
            if(!stringType(choice.getString())){
                logger.debug("[{}]      String  \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getString(), false);
                return false;
            }
        logger.debug("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), choice.toString(),true);
        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean resourceType(String value) {
        //logger.debug("resource");
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
            boolean match =  listChoice.stream().anyMatch(predicate);
            logger.debug("[resourceType]        \"{}\"      validation      {}", value, match);
            return match;
        }
        logger.debug("[resourceType]        \"{}\"      validation      {}", value, false);
        return false;
    }

    /**
     * @param input
     * @return
     */
    protected boolean dateType(String input) {
        logger.debug("[dateType]     \"{}\"", input);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean aboutType(String value) {
        if(stringType(value) && !StringUtils.containsWhitespace(value)){
            logger.debug("[aboutType]       \"{}\"      validation      {}", value,  true);
            return true;
        }
        logger.debug("[aboutType]       \"{}\"      validation      {}", value,  false);
        return false;
    }

    /**
     *
     *
     * @param type
     * @return
     */
    protected boolean edmType(EdmType type) {
        boolean match = Objects.nonNull(type);
        logger.debug("[edmType]       \"{}\"        validate:    {}", match ? type.xmlValue() : null, match);

        return match;
    }

    /**
     * @param country
     * @return
     */
    protected boolean countryCode(String country) {
        logger.debug("[countryCodeType]      \"{}\"", CountryCodes.convert(country));
        return true;
    }

    /**
     * @param language
     * @return
     */
    protected boolean languageCode(String language) {
        LanguageCodes lang = Objects.nonNull(language) ? LanguageCodes.convert(language) : null;
        boolean match = Objects.nonNull(lang);
        logger.debug("[languageCodeType]     \"{}\"        validate:    {}",  match ? lang.xmlValue() : null, match);
        return match;
    }

    /**
     * @param value
     * @return
     */
    protected boolean stringType(String value) {
        boolean match = Optional.ofNullable(value).filter(f -> !StringUtils.isBlank(value)).isPresent();
        logger.debug("[stringType]     \"{}\"      validate        {}", value, match);
        return match;
    }

    /**
     * @param value
     * @return
     */
    protected boolean longType(String value) {
        try{
            Long.parseLong(value);
            logger.debug("[longType]     \"{}\"        validation      {}", value, true);
            return true;
        }catch (NumberFormatException e){
            logger.debug("[longType]     \"{}\"        validation      {}", value, false);
            return false;
        }
    }

    /**
     * @param value
     * @return
     */
    protected boolean integerType(String value) {
        try{
            Integer.parseInt(value);
            logger.debug("[integerType]     \"{}\"     validation      {}", value, true);
            return true;
        }catch (NumberFormatException e){
            logger.debug("[integerType]     \"{}\"     validation      {}", value, false);
            return false;
        }
    }

    /**
     * @param value
     * @return
     */
    protected boolean nonNegativeIntegerType(String value) {
        if(integerType(value)){
            if(BigInteger.valueOf(Integer.parseInt(value)).compareTo(BigInteger.ZERO) > 0){
                logger.debug("[nonNegativeIntegerType]     \"{}\"      validation      {}", value, true);
                return true;
            }
        }
        logger.debug("[nonNegativeIntegerType]     \"{}\"      validation      {}", value, false);
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean doubleType(String value) {
        try{
            Double.parseDouble(value);
            logger.debug("[doubleType]     \"{}\"      validation      {}", value, true);
            return true;
        }catch (NumberFormatException e){
            logger.debug("[doubleType]     \"{}\"      validation      {}", value, true);
            return false;
        }
    }

    /**
     * @param value
     * @return
     */
    protected boolean hexBinaryString(String value) {
        if(integerType(value)){
            try{
                Integer.toBinaryString(Integer.parseInt(value));
                logger.debug("[hexBinaryString]     \"{}\"      validation      {}", value, true);

                return true;
            }catch (NumberFormatException e){
                logger.debug("[hexBinaryString]     \"{}\"      validation      {}", value, true);
                return true;
            }
        }
        logger.debug("[hexBinaryString]     \"{}\"      validation      {}", value, false);
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean UGCType(String value) {
        logger.debug("[UGCType]     \"{}\"", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean colorSpaceType(String value) {
        logger.debug("[colorSpaceType]     \"{}\"", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean floatType(String value) {
        logger.debug("[floatType]        \"{}\"", value);

        return true;
    }

    /**
     * @param value
     * @return
     */
    protected boolean placeType(String value) {
        logger.debug("[placeType]     \"{}\"", value);

        return true;
    }
}
