package org.Validation.Core.classes;

import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import org.Validation.Core.InterfaceCoreClasses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 */
public class ProvidedCHO implements InterfaceCoreClasses<ProvidedCHOType> {

    private static Logger logger = LogManager.getLogger(ProvidedCHO.class);


    public ProvidedCHO() {
    }

    @Override
    public ProvidedCHOType validate(ProvidedCHOType data) {
        return null;
    }

}
