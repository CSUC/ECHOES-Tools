package org.csuc.analyse.factory;

/**
 * @author amartinez
 */
public class FactoryParser {

    public static Parser createFactory(Parser parser){
        return parser;
    }
}
