package org.csuc.Validation.Core.schematron;

import org.bson.Document;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SchematronTest {

    @Test
    public void isValid() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        //File edm = new File(classLoader.getResource("edm.xml").getFile());
        File edm = new File("/home/amartinez/Baixades/72e106a7-a554-41df-8c1b-f92227eb6dbd/t11_a/0ef3bd2e-8188-4d70-9837-672e6afefa3f.rdf");

        File sch = new File(classLoader.getResource("SchematronEDM.sch").getFile());

        assertNotNull(edm);
        assertTrue(Files.exists(Paths.get(edm.toURI()), LinkOption.NOFOLLOW_LINKS));

        assertNotNull(sch);
        assertTrue(Files.exists(Paths.get(sch.toURI()), LinkOption.NOFOLLOW_LINKS));

        Schematron schematron = new Schematron(edm);

        System.out.println(schematron.isValid());


        schematron.getSVRLFailedAssert().toDocument().forEach(consumer ->{
            System.out.println(consumer.toJson());
        });
//        System.out.println(schematron.getSVRLFailedAssert().toDocument().stream().map(m-> m.toJson()).collect(Collectors.joining()));
//        assertTrue(schematron.isValid());
//        assertEquals(0, schematron.getSVRLFailedAssert().length());
    }
}