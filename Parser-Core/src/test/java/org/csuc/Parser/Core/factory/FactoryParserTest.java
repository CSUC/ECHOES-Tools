package org.csuc.Parser.Core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.csuc.Parser.Core.strategy.sax.Sax;
import org.csuc.Parser.Core.strategy.stax.Stax;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class FactoryParserTest {

    private static Logger logger = LogManager.getLogger(FactoryParserTest.class);

    private String url;
    private File xml;

    @Before
    public void setUp() throws Exception {
        url = "https://webservices.picturae.com/a2a/20a181d4-c896-489f-9d16-20a3b7306b15?verb=ListRecords&metadataPrefix=a2a";
        xml = new File(getClass().getClassLoader().getResource("a2a.xml").getFile());
    }

    @Test
    public void testParserURLSax() throws Exception {
        Parser parser = FactoryParser.createFactory(new ParserURL(new Sax()));
        parser.execute(new URL(url));
        assertTrue(parser.getXPATHResult().size() > 0);
        assertTrue(parser.getNamespaceResult().size() > 0);
        parser.getXPATHResult().forEach(logger::info);
        logger.info(parser.getNamespaceResult());
    }

    @Test
    public void testParserFILESax() throws Exception {
        Parser parser = FactoryParser.createFactory(new ParserFILE(new Sax()));
        parser.execute(xml.getPath());
        assertTrue(parser.getXPATHResult().size() > 0);
        assertTrue(parser.getNamespaceResult().size() > 0);
        parser.getXPATHResult().forEach(logger::info);
        logger.info(parser.getNamespaceResult());
    }

    @Test
    public void testParserURLDom4j() throws MalformedURLException {
        Parser parser = FactoryParser.createFactory(new ParserURL(new Dom4j()));
        parser.execute(new URL(url));
        assertTrue(parser.getXPATHResult().size() > 0);
        assertTrue(parser.getNamespaceResult().size() > 0);
        parser.getXPATHResult().forEach(logger::info);
        logger.info(parser.getNamespaceResult());
    }

    @Test
    public void testParserFILEDom4j() throws MalformedURLException {
        Parser parser = FactoryParser.createFactory(new ParserFILE(new Dom4j()));
        parser.execute(xml.getPath());
        assertTrue(parser.getXPATHResult().size() > 0);
        assertTrue(parser.getNamespaceResult().size() > 0);
        parser.getXPATHResult().forEach(logger::info);
        logger.info(parser.getNamespaceResult());

    }

    @Test
    public void testParserFILEStax() throws MalformedURLException {
        //Parser parser = FactoryParser.createFactory(new ParserFILE(new Stax()));
        //parser.execute(xml.getPath());

        //assertTrue(parser.getXPATHResult().size() > 0);
        //parser.getXPATHResult().forEach(logger::info);
    }
}