package org.csuc.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.EDM.Transformations.formats.utils.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class DataTypeTest extends DataType{

    private static Logger logger = LogManager.getLogger(DataTypeTest.class);


    @Before
    public void setUp() throws Exception {
        File xml = new File(getClass().getClassLoader().getResource("edm.xml").getFile());
        assertTrue(xml.exists());

        Validate validate = new Validate(Paths.get(xml.toURI()), StandardCharsets.UTF_8, RDF.class, null);
        //assertNotNull(validate.getRDF());
    }

    @Test
    public void resourceOrLiteralType() {
        ResourceOrLiteralType resourceOrLiteralType = new ResourceOrLiteralType();
        ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
        resource.setResource(" a s da3 ");
        resourceOrLiteralType.setResource(resource);
        assertFalse(resourceOrLiteralType(resourceOrLiteralType));

        resourceOrLiteralType.setResource(null);
        resourceOrLiteralType.setString("asdadadadsads");

        assertTrue(resourceOrLiteralType(resourceOrLiteralType));
    }

    @Test
    public void literalType() {
        LiteralType literalType = new LiteralType();
        literalType.setString("adadadadsad");

        assertTrue(literalType(literalType));

        LiteralType.Lang lang = new LiteralType.Lang();
        lang.setLang("aasddas");

        literalType.setLang(lang);

        assertFalse(literalType(literalType));

    }

    @Test
    public void resourceType() {
        ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
        resource.setResource("Place:Leiden");
        assertTrue(resourceType(resource));

    }

    @Test
    public void dateType(){
        assertFalse(dateType("1760-12-100"));

        assertTrue(dateType("01/11/1989"));
        assertEquals("11-01-1989",TimeUtil.format("01/11/1989"));

        assertTrue(dateType("1987"));
        assertEquals("1987", TimeUtil.format("1987"));

        assertTrue(dateType("09-1984"));
        assertEquals("09-1984", TimeUtil.format("09-1984", TimeUtil.MONTHYEAR));

        assertTrue(dateType("30-10-1988"));
        assertEquals("30-10-1988", TimeUtil.format("30-10-1988"));

        assertTrue(dateType("30-10-1988"));
        assertEquals("30-10-1988", TimeUtil.format("30-10-1988"));

        assertTrue(dateType("[1973]"));
        assertEquals("1973", TimeUtil.format("[1973]", TimeUtil.YEAR));
    }

    @Test
    public void aboutType() {
        assertTrue(aboutType("http://hdl.handle.net/10687/35725"));
        assertTrue(aboutType("ProvidedCHO:"));

        assertFalse(aboutType(" http://hdl.handle.net/10687/35725 "));
        assertFalse(aboutType("  "));
        assertFalse(aboutType(""));
        assertFalse(aboutType(null));
    }

    @Test
    public void edmType() {
        Stream.of(EdmType.values()).forEach((EdmType edmType) -> assertTrue(edmType(edmType)));

        assertFalse(edmType(null));
    }

    @Test
    public void countryCode() {
    }

    @Test
    public void languageCode() {
        Stream.of(LanguageCodes.values()).forEach((LanguageCodes languageCodes) -> assertTrue(languageCode(languageCodes.xmlValue())));

        assertFalse(languageCode("adadsad"));
        assertFalse(languageCode(null));
    }

    @Test
    public void stringType() {
    }

    @Test
    public void longType() {
        assertTrue(longType("0"));
        assertTrue(longType("473"));
        assertTrue(longType("-0"));
        assertTrue(longType("1100110"));
        assertTrue(longType("99"));
        assertTrue(longType("999"));

        assertFalse(longType("-FF"));
        assertFalse(longType("Hazelnut"));
        assertFalse(longType("asd"));
    }

    @Test
    public void integerType() {
        assertTrue(integerType("0"));
        assertTrue(integerType("473"));

        assertFalse(integerType("asd"));
    }

    @Test
    public void nonNegativeIntegerType() {
//        assertTrue(nonNegativeIntegerType("473"));
//
//        assertFalse(nonNegativeIntegerType("-0"));
//        assertFalse(nonNegativeIntegerType("asd"));
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

    @Test
    public void uriType(){
        assertTrue(uriType("http://allefriezen.nl/zoeken/deeds/f740898c-3a00-4f72-97a5-a112eac590ed"));
        assertFalse(uriType(null));
        assertFalse(uriType(""));
        assertFalse(uriType("      "));
        assertFalse(uriType("http://allefriezen.nl/zoeken/d eeds/f740898c -3a00-4 f72-97a5-a112eac590ed"));
    }
}