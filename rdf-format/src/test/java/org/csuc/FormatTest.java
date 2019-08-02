package org.csuc;

import org.csuc.util.FormatType;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class FormatTest {

    private File f1;
    private File f2;

    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        f1 = new File(classLoader.getResource("ttl/58d4ea15-ffac-7ac9-87a2-a144e4b80c9f.xml").getFile());
        f2 = new File(classLoader.getResource("ttl/50c41eda-c033-4354-ab05-b59532908e72.ttl").getFile());
    }

    @Test
    public void format() {
        Format.format(f1, FormatType.JSONLD, System.out);
        Format.format(f2, FormatType.JSONLD, System.out);
    }

    @Test
    public void isValid() {
        assertTrue(Format.isValid(f1));
        assertTrue(Format.isValid(f1, FormatType.RDFXML));

        assertTrue(Format.isValid(f2));
        assertTrue(Format.isValid(f2, FormatType.TURTLE));
    }
}