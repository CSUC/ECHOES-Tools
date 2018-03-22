package org.csuc.Validation.Core;

/**
 * @author amartinez
 */
public class FactoryCoreClasses {

    public static <T> InterfaceCoreClasses<T> createFactory(InterfaceCoreClasses<T> clas){
        return clas;
    }
}
