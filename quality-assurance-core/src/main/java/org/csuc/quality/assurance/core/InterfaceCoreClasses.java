package org.csuc.quality.assurance.core;

import eu.europeana.corelib.definitions.jibx.*;

/**
 *
 *
 * @author amartinez
 *
 */
public interface InterfaceCoreClasses<T> {

    T validate(T data) throws Exception;
    T validate() throws Exception;
}
