package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class DataTypeTest {

    private DataType dataType;

    @Before
    public void setUp() throws Exception {
        File xml = new File(getClass().getClassLoader().getResource("edm.xml").getFile());
        assertTrue(xml.exists());

        Validate validate = new Validate(new FileInputStream(xml), StandardCharsets.UTF_8, RDF.class);
        assertNotNull(validate.getRDF());

        dataType = new DataType(validate.getRDF().getChoiceList());
    }

    @Test
    public void resourceOrLiteralType() {
    }

    @Test
    public void literalType() {
    }

    @Test
    public void resourceType() {
        assertTrue(dataType.resourceType("Place:Leiden"));
        assertFalse(dataType.resourceType("ProvidedCHO:"));
    }

    @Test
    public void dateType() {
    }

    @Test
    public void aboutType() {
        assertTrue(dataType.aboutType("http://hdl.handle.net/10687/35725"));
        assertTrue(dataType.aboutType("ProvidedCHO:"));
        assertFalse(dataType.aboutType(" http://hdl.handle.net/10687/35725 "));
        assertFalse(dataType.aboutType("  "));
        assertFalse(dataType.aboutType(""));
        assertFalse(dataType.aboutType(null));
    }

    @Test
    public void edmType() {
    }

    @Test
    public void countryCode() {
    }

    @Test
    public void languageCode() {
    }

    @Test
    public void stringType() {
    }

    @Test
    public void longType() {
    }

    @Test
    public void integerType() {
    }

    @Test
    public void nonNegativeIntegerType() {
    }

    @Test
    public void doubleType() {
    }

    @Test
    public void hexBinaryString() {
    }

    @Test
    public void UGCType() {
    }

    @Test
    public void colorSpaceType() {
    }

    @Test
    public void floatType() {
    }

    @Test
    public void placeType() {
    }
}