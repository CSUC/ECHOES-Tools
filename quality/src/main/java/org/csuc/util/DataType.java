package org.csuc.util;

import eu.europeana.corelib.definitions.jibx.LanguageCodes;
import eu.europeana.corelib.definitions.jibx.LiteralType;
import eu.europeana.corelib.definitions.jibx.ResourceOrLiteralType;
import eu.europeana.corelib.definitions.jibx.ResourceType;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

/**
 * @author amartinez
 */
public class DataType {


    /**
     * @param value
     * @return
     */
    public static boolean aboutType(String value) throws Exception, URISyntaxException {
        if (!stringType(value)) throw new Exception("String is whitespace, empty (\"\") or null");
        if (StringUtils.containsWhitespace(value))
            throw new Exception(MessageFormat.format("\"{0}\" contains whitespace characters", value));
        try {
            new URI(value);
        } catch (URISyntaxException e) {
            throw e;
        }
        return true;
    }


    /**
     * @param value
     * @return
     */
    public static boolean stringType(String value) {
        boolean match = Optional.ofNullable(value).filter(f -> !StringUtils.isBlank(value)).isPresent();

        return match;
    }

//    /**
//     * @param value
//     * @return
//     */
//    public static boolean floatType(String value) throws Exception {
//        if (!isBlank(value)) throw new Exception("float is whitespace, empty (\"\") or null");
//        try{
//            Float.parseFloat(value);
//            return true;
//        }catch (NumberFormatException e){
//            throw e;
//        }
//    }

    /**
     * @param choice
     * @return
     */
    public static boolean literalType(LiteralType choice) throws Exception {
        if (Objects.nonNull(choice.getLang()))
            if (!languageCode(choice.getLang().getLang()))
                throw new Exception(MessageFormat.format("\"{0}\" language not suported", choice.getLang().getLang()));

        if (Objects.nonNull(choice.getString()))
            if (!stringType(choice.getString())) throw new Exception("String is whitespace, empty (\"\") or null");

        return true;
    }

    /**
     * @param obj
     * @return
     */
    public static boolean resourceType(Object obj) throws Exception {
        if (obj instanceof ResourceOrLiteralType.Resource) {
            String resource = ((ResourceOrLiteralType.Resource) obj).getResource();

            if (!stringType(resource)) throw new Exception("String is whitespace, empty (\"\") or null");
            if (StringUtils.containsWhitespace(resource))
                throw new Exception(MessageFormat.format("\"{0}\" contains whitespace characters", resource));
        }

        if (obj instanceof ResourceType) {
            String resource = ((ResourceType) obj).getResource();

            if (!stringType(resource)) throw new Exception("String is whitespace, empty (\"\") or null");
            if (StringUtils.containsWhitespace(resource))
                throw new Exception(MessageFormat.format("\"{0}\" contains whitespace characters", resource));
        }
        return true;
    }

    /**
     * @param choice
     * @return
     */
    public static boolean resourceOrLiteralType(ResourceOrLiteralType choice) throws Exception {
        if (Objects.nonNull(choice.getLang()))
            if (!languageCode(choice.getLang().getLang()))
                throw new Exception(MessageFormat.format("\"{0}\" language not suported", choice.getLang().getLang()));

        if (Objects.nonNull(choice.getResource()) && resourceType(choice.getResource())) return true;
        else if (Objects.nonNull(choice.getString())) {
            if (!stringType(choice.getString())) throw new Exception("String is whitespace, empty (\"\") or null");
        }

        return true;
    }

    /**
     * @param language
     * @return
     */
    public static boolean languageCode(String language) {
        LanguageCodes lang = Objects.nonNull(language) ? LanguageCodes.convert(language) : null;
        boolean match = Objects.nonNull(lang);
        return match;
    }

}
