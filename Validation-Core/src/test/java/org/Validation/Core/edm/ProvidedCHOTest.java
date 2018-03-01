package org.Validation.Core.edm;

import org.Validation.Core.schematron.SchematronUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;


public class ProvidedCHOTest {

    private static Logger logger = LogManager.getLogger(ProvidedCHOTest.class);

    @Test
    public void validate() throws Exception {
        File schema = new File(getClass().getClassLoader().getResource("ProvidedCHO.sch").getFile());

        assertTrue(Files.exists(schema.toPath()));

        File file = new File(getClass().getClassLoader().getResource("edm.xml").getFile());

        assertTrue(Files.exists(file.toPath()));

        SchematronUtil sch = new SchematronUtil(schema, file);


        if(!sch.isValid()){
            logger.info(sch.getSVRLFailedAssert().toString());
            logger.info(sch.getFailedAssert());
        }else {
            logger.info("{}   is valid", file.getName());
        }
    }
}