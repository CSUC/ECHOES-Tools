package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

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
        ResourceOrLiteralType resourceOrLiteralType = new ResourceOrLiteralType();
        ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
        resource.setResource(" a s da3 ");
        resourceOrLiteralType.setResource(resource);
        assertFalse(dataType.resourceOrLiteralType(resourceOrLiteralType));

        resourceOrLiteralType.setResource(null);
        resourceOrLiteralType.setString("asdadadadsads");

        assertTrue(dataType.resourceOrLiteralType(resourceOrLiteralType));
    }

    @Test
    public void literalType() {
        LiteralType literalType = new LiteralType();
        literalType.setString("adadadadsad");

        assertTrue(dataType.literalType(literalType));

        LiteralType.Lang lang = new LiteralType.Lang();
        lang.setLang("aasddas");

        literalType.setLang(lang);

        assertFalse(dataType.literalType(literalType));

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
        Stream.of(EdmType.values()).forEach((EdmType edmType) -> assertTrue(dataType.edmType(edmType)));

        assertFalse(dataType.edmType(null));
    }

    @Test
    public void countryCode() {
    }

    @Test
    public void languageCode() {
        Stream.of(LanguageCodes.values()).forEach((LanguageCodes languageCodes) -> assertTrue(dataType.languageCode(languageCodes.xmlValue())));

        assertFalse(dataType.languageCode("adadsad"));
        assertFalse(dataType.languageCode(null));
    }

    @Test
    public void stringType() {
    }

    @Test
    public void longType() {
        assertTrue(dataType.longType("0"));
        assertTrue(dataType.longType("473"));
        assertTrue(dataType.longType("-0"));
        assertTrue(dataType.longType("1100110"));
        assertTrue(dataType.longType("99"));
        assertTrue(dataType.longType("999"));

        assertFalse(dataType.longType("-FF"));
        assertFalse(dataType.longType("Hazelnut"));
        assertFalse(dataType.longType("asd"));
    }

    @Test
    public void integerType() {
        assertTrue(dataType.integerType("0"));
        assertTrue(dataType.integerType("473"));

        assertFalse(dataType.integerType("asd"));
    }

    @Test
    public void nonNegativeIntegerType() {
//        assertTrue(dataType.nonNegativeIntegerType("473"));
//
//        assertFalse(dataType.nonNegativeIntegerType("-0"));
//        assertFalse(dataType.nonNegativeIntegerType("asd"));
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