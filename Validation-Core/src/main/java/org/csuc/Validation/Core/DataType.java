package org.csuc.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.EDM.Transformations.formats.a2a.PlaceType;
import org.EDM.Transformations.formats.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author amartinez
 */
public class DataType {

    private static Logger logger = LogManager.getLogger(DataType.class);

    public DataType() {}

    /**
     * @param choice
     * @return
     */
    protected boolean resourceOrLiteralType(ResourceOrLiteralType choice) {
        if (Objects.nonNull(choice.getLang())) {
            if (!languageCode(choice.getLang().getLang())) {
                logger.error("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice), false);
                return false;
            }
        }
        if (Objects.nonNull(choice.getResource())) {
            if (!resourceType(choice.getResource())) {
                logger.error("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice), false);
                return false;
            }else if(Objects.nonNull(choice.getString()) && !choice.getString().isEmpty()){
                logger.error("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice), false);
                return false;
            }
        }else{
            if (Objects.nonNull(choice.getString())){
                if(!stringType(choice.getString())){
                    logger.error("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice), false);
                    return false;
                }
            }
        }
        logger.debug("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice),true);
        return true;
    }

    /**
     * @param choice
     * @return
     */
    protected boolean literalType(LiteralType choice) {
        if(Objects.nonNull(choice.getLang()))
            if(!languageCode(choice.getLang().getLang())){
                logger.error("[{}]      Lang        \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getLang().getLang(), false);
                return false;
            }
        if(Objects.nonNull(choice.getString()))
            if(!stringType(choice.getString())){
                logger.error("[{}]      String  \"{}\"      validation      {}", choice.getClass().getSimpleName(), choice.getString(), false);
                return false;
            }
        logger.debug("[{}]      {}      validation      {}", choice.getClass().getSimpleName(), prettyPrint(choice),true);
        return true;
    }

    /**
     *
     * @param obj
     * @return
     */
    protected boolean resourceType(Object obj) {
        String resource = null;
        if(obj instanceof ResourceOrLiteralType.Resource){
            resource = ((ResourceOrLiteralType.Resource) obj).getResource();
            if (stringType(resource) && !StringUtils.containsWhitespace(resource)) {
//                boolean match =  listChoice.stream().anyMatch(predicate(resource));
//                logger.debug("[{}]       [resourceType]        \"{}\"      validation      {}", obj.getClass().getSimpleName(), prettyPrint(obj), match);
//                return match;
                logger.debug("[{}]       [resourceType]        \"{}\"      validation      {}", obj.getClass().getSimpleName(), prettyPrint(obj), true);
                return true;
            }
        }
        if(obj instanceof ResourceType){
            resource = ((ResourceType) obj).getResource();
            if (stringType(resource) && !StringUtils.containsWhitespace(resource)) {
                logger.debug("[{}]       [resourceType]        \"{}\"      validation      {}", obj.getClass().getSimpleName(), prettyPrint(obj), true);
                return true;
            }
        }
        return false;
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
        logger.error("[aboutType]       \"{}\"      validation      {}", value,  false);
        return false;
    }

    /**
     *
     * @param obj
     * @return
     */
    protected boolean dateType(Object obj) {
        if (obj instanceof ResourceOrLiteralType) {
            ResourceOrLiteralType resourceOrLiteralType = (ResourceOrLiteralType) obj;

            try {
                TimeUtil.format(resourceOrLiteralType.getString());
            } catch (Exception e) {
                logger.error("[dateType]       {}      validation      {}", prettyPrint(resourceOrLiteralType), false);
                return false;
            }

        } else if (obj instanceof LiteralType) {
            LiteralType literalType = (LiteralType) obj;

            try {
                TimeUtil.format(literalType.getString());
            } catch (Exception e) {
                logger.error("[dateType]       {}      validation      {}", prettyPrint(literalType), false);
                return false;
            }

        } else if (obj instanceof String) {
            try {
                TimeUtil.format(obj.toString());
            } catch (Exception e) {
                logger.error("[dateType]       {}      validation      {}", prettyPrint(obj.toString()), false);
                return false;
            }

        }
        logger.debug("[dateType]       \"{}\"      validation      {}", prettyPrint(obj), true);
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
            logger.error("[longType]     \"{}\"        validation      {}", value, false);
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
            logger.error("[integerType]     \"{}\"     validation      {}", value, false);
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
        logger.error("[nonNegativeIntegerType]     \"{}\"      validation      {}", value, false);
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
            logger.debug("[doubleType]     \"{}\"      validation      {}", value, false);
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
                logger.error("[hexBinaryString]     \"{}\"      validation      {}", value, false);
                return false;
            }
        }
        logger.error("[hexBinaryString]     \"{}\"      validation      {}", value, false);
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
        try{
            Float.parseFloat(value);
            logger.debug("[floatType]     \"{}\"      validation      {}", value, true);
            return true;
        }catch (NumberFormatException e){
            logger.error("[floatType]     \"{}\"      validation      {}", value, false);
            return false;
        }
    }

    /**
     * @param value
     * @return
     */
    protected boolean placeType(String value) {
        PlaceType placeTypes = Objects.nonNull(value) ? PlaceType.convert(value) : null;
        boolean match = Objects.nonNull(placeTypes);
        logger.debug("[placeType]      \"{}\"     validation", match ? placeTypes.value() : null, match);
        return match;
    }

    /**
     *
     * @param value
     * @return
     */
    protected boolean uriType(String value){
        URL url;
        try {
            url = new URL(value);
        } catch (MalformedURLException e) {
            logger.error("[uriType]     \"{}\"      validation      {}", value, false);
            return false;
        }
        try {
            url.toURI();
        } catch (URISyntaxException e) {
            logger.error("[uriType]     \"{}\"      validation      {}", value, false);
            return false;
        }

        logger.debug("[uriType]     \"{}\"      validation      {}", value, true);
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
        }else if (obj instanceof ResourceType){
            ResourceType resourceType = (ResourceType) obj;
            return MessageFormat.format("Resource:       {0}", resourceType.getResource().isEmpty() ? null : resourceType.getResource().toString());
        }
        else if (obj instanceof String){
            return MessageFormat.format("{0}", obj.toString());
        }

        return null;
    }

    /**
     *
     * @param value
     * @return
     */
    private Predicate<RDF.Choice> predicate(String value){
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

        return predicate;
    }
}
