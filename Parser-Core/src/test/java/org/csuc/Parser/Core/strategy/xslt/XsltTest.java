package org.csuc.Parser.Core.strategy.xslt;

import org.csuc.Parser.Core.factory.FactoryParser;
import org.csuc.Parser.Core.factory.Parser;
import org.csuc.Parser.Core.factory.ParserOAI;
import org.csuc.Parser.Core.factory.ParserURL;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class XsltTest {
    @Test
    public void createXML() throws Exception {
        Parser p = FactoryParser.createFactory(new ParserURL(new Xslt()));

        p.execute(new URL("https://webservices.picturae.com/a2a/20a181d4-c896-489f-9d16-20a3b7306b15?verb=ListRecords&metadataPrefix=a2a"));
        p.XML(System.out);
    }

}