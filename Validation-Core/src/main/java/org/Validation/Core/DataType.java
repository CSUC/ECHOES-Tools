package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.EDM.Transformations.formats.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.format.DateTimeParseException;
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
                logger.info("[{}]      Lang        \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getLang().getLang(), false);
                return false;
            }
        }
        if (Objects.nonNull(choice.getResource())) {
            if (!resourceType(choice.getResource().getResource())) {
                logger.info("[{}]      Resource    \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getResource().getResource(), false);
                return false;
            }else if(Objects.nonNull(choice.getString()) && !choice.getString().isEmpty()){
                logger.info("[{}]      Resource    \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getResource().getResource(), false);
                return false;
            }
        }else{
            if (Objects.nonNull(choice.getString())){
                if(!stringType(choice.getString())){
                    logger.info("[{}]      String  \"{}\"      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice), false);
                    return false;
                }
            }
        }
        logger.info("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice),true);
        return true;
    }

    /**
     * @param choice
     * @return
     */
    protected boolean literalType(LiteralType choice) {
        if(Objects.nonNull(choice.getLang()))
            if(!languageCode(choice.getLang().getLang())){
                logger.info("[{}]      Lang        \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getLang().getLang(), false);
                return false;
            }
        if(Objects.nonNull(choice.getString()))
            if(!stringType(choice.getString())){
                logger.info("[{}]      String  \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getString(), false);
                return false;
            }
        logger.info("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice),true);
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
     * @param value
     * @return
     */
    protected boolean aboutType(String value) {
        if(stringType(value) && !StringUtils.containsWhitespace(value)){
            logger.info("[aboutType]       \"{}\"      validation      {}", value,  true);
            return true;
        }
        logger.info("[aboutType]       \"{}\"      validation      {}", value,  false);
        return false;
    }

    /**
     *
     * @param obj
     * @return
     */
    protected boolean dateType(Object obj) {
        if(obj instanceof ResourceOrLiteralType){
            ResourceOrLiteralType resourceOrLiteralType = (ResourceOrLiteralType) obj;

            if(Objects.nonNull(resourceOrLiteralType.getString())){
               try{
                   TimeUtil.format(resourceOrLiteralType.getString());
               }catch(Exception e){
                   logger.debug("[dateType]       {}      validation      {}", prettyPrint(resourceOrLiteralType),  false);
                   return false;
               }
            }
        }else if (obj instanceof LiteralType){
            LiteralType literalType = (LiteralType) obj;

            if(Objects.nonNull(literalType.getString())){
                try{
                    TimeUtil.format(literalType.getString());
                }catch(Exception e){
                    logger.debug("[dateType]       {}      validation      {}", prettyPrint(literalType),  false);
                    return false;
                }
            }
        }
        logger.debug("[dateType]       \"{}\"      validation      {}", prettyPrint(obj),  true);
        return true;
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
        CountryCodes countryCodes = Objects.nonNull(country) ? CountryCodes.convert(country) : null;
        boolean match = Objects.nonNull(countryCodes);
        logger.debug("[countryCodeType]      \"{}\"     validation", match ? countryCodes.xmlValue() : null, match);
        return match;
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

    /**
     *
     * @param obj
     * @return
     */
    private String prettyPrint(Object obj){
        if(obj instanceof ResourceOrLiteralType){
            ResourceOrLiteralType resourceOrLiteralType = (ResourceOrLiteralType) obj;
            return MessageFormat.format("Lang:      {0}     Resource:       {1}     String:     {2}",
                    Objects.nonNull(resourceOrLiteralType.getLang()) ? resourceOrLiteralType.getLang().getLang() : null,
                    Objects.nonNull(resourceOrLiteralType.getResource()) ? resourceOrLiteralType.getResource().getResource() : null ,
                    Objects.nonNull(resourceOrLiteralType.getString()) ? (resourceOrLiteralType.getString().isEmpty() ? null : resourceOrLiteralType.getString() ) : null );
        }
        else if( obj instanceof  LiteralType){
            LiteralType literalType = (LiteralType) obj;
            return MessageFormat.format("Lang:      {0}     Resource:       {1}     String:     {2}",
                    Objects.nonNull(literalType.getLang()) ? literalType.getLang().getLang() : null,
                    null,
                    Objects.nonNull(literalType.getString()) ? (literalType.getString().isEmpty() ? null : literalType.getString() ) : null );
        }
        return null;
    }
}
