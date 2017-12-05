package org.csuc.Parser.Core.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.Parser.Core.strategy.dom.Dom;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.csuc.Parser.Core.strategy.sax.Sax;
import org.csuc.Parser.Core.strategy.xslt.Xslt;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class FactoryParserTest {

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
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserFILESax() throws Exception {
        Parser parser = FactoryParser.createFactory(new ParserFILE(new Sax()));
        parser.execute(xml.getPath());
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserURLDom4j() throws MalformedURLException {
        Parser parser = FactoryParser.createFactory(new ParserURL(new Dom4j()));
        parser.execute(new URL(url));
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserFILEDom4j() throws MalformedURLException {
        Parser parser = FactoryParser.createFactory(new ParserFILE(new Dom4j()));
        parser.execute(xml.getPath());
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserURLDOM() throws MalformedURLException, ParserConfigurationException {
        Parser parser = FactoryParser.createFactory(new ParserURL(new Dom()));
        parser.execute(new URL(url));
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserFILEDOM() throws MalformedURLException, ParserConfigurationException {
        Parser parser = FactoryParser.createFactory(new ParserFILE(new Dom()));
        parser.execute(xml.getPath());
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserURLXSLT() throws IOException, ParserConfigurationException, TransformerException {
        Parser parser = FactoryParser.createFactory(new ParserURL(new Xslt()));
        parser.execute(new URL(url));
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }

    @Test
    public void testParserFILEXSLT() throws IOException, ParserConfigurationException, TransformerException {
        Parser parser = FactoryParser.createFactory(new ParserFILE(new Xslt()));
        parser.execute(xml.getPath());
        parser.XML(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
        parser.JSON(IoBuilder.forLogger(FactoryParserTest.class).setLevel(Level.INFO).buildOutputStream());
    }
}