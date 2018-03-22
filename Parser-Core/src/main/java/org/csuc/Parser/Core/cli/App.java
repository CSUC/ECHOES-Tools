/**
 * 
 */
package org.csuc.Parser.Core.cli;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.Parser.Core.factory.*;
import org.csuc.Parser.Core.strategy.dom.Dom;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.csuc.Parser.Core.strategy.sax.Sax;
import org.csuc.Parser.Core.strategy.xslt.Xslt;
import org.csuc.Parser.Core.util.*;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * @author amartinez
 *
 */
public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    private Instant inici = Instant.now();

    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException, URISyntaxException {
        new App().doMain(args);
    }

    public void doMain(String[] args) throws IOException, ParserConfigurationException, TransformerException, URISyntaxException {
        ArgsBean bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }

        Parser factory = null;

        if(bean.getType().equals(EnumTypes.OAI)){
            if(MethodType.DOM4J.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserOAI(new Dom4j()));
            }
            if(MethodType.SAX.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserOAI(new Sax()));
            }
            if(MethodType.DOM.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserOAI(new Dom()));
            }
            if(MethodType.XSLT.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserOAI(new Xslt()));
            }
            factory.execute(new URL(bean.getInput()));
        }else if(bean.getType().equals(EnumTypes.URL)){
            if(MethodType.DOM4J.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserURL(new Dom4j()));
            }
            if(MethodType.SAX.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserURL(new Sax()));
            }
            if(MethodType.DOM.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserURL(new Dom()));
            }
            if(MethodType.XSLT.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserURL(new Xslt()));
            }
            factory.execute(new URL(bean.getInput()));
        }else if(bean.getType().equals(EnumTypes.FILE)){
            if(MethodType.DOM4J.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserFILE(new Dom4j()));
            }
            if(MethodType.SAX.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserFILE(new Sax()));
            }
            if(MethodType.DOM.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserFILE(new Dom()));
            }
            if(MethodType.XSLT.equals(bean.getMethod())){
                factory = FactoryParser.createFactory(new ParserFILE(new Xslt()));
            }
            factory.execute(bean.getInput());
        }

        if(Objects.nonNull(factory)) {
            if (Objects.isNull(bean.getOut())) {
                if (Objects.equals(bean.getFormat(), FormatType.JSON)) {
                    factory.JSON(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                } else if (Objects.equals(bean.getFormat(), FormatType.XML)) {
                    factory.XML(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                } else {
                    factory.XML(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                }
            }else{
                if(Objects.equals(bean.getFormat(), FormatType.JSON)){
                    factory.JSON(new FileOutputStream(Paths.get(bean.getOut() + File.separator + "result.json").toFile()));
                }else if(Objects.equals(bean.getFormat(), FormatType.XML)){
                    factory.XML(new FileOutputStream(Paths.get(bean.getOut() + File.separator  + "result.xml").toFile()));
                }else{
                    factory.XML(new FileOutputStream(Paths.get(bean.getOut() + File.separator  + "result.json").toFile()));
                }
            }
        }
        logger.info(String.format("End %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
	}
}
