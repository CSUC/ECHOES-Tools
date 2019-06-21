package org.csuc.util.format;

/**
 * @author amartinez
 */
public class FactoryFormat {

    public static FormatInterface createFactory(FormatInterface formatInterface) {
        return formatInterface;
    }
}
