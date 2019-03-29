/**
 * 
 */
package org.csuc.analyse.cli;

import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.analyse.factory.*;
import org.csuc.analyse.strategy.dom.Dom;
import org.csuc.analyse.strategy.dom4j.Dom4j;
import org.csuc.analyse.strategy.sax.Sax;
import org.csuc.analyse.strategy.xslt.Xslt;
import org.csuc.analyse.util.EnumTypes;
import org.csuc.analyse.util.FormatType;
import org.csuc.analyse.util.MethodType;
import org.csuc.analyse.util.TimeUtils;
import org.csuc.core.HDFS;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;


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

    public void doMain(String[] args) {
        ArgsBean bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }

        Parser factory = null;

        try {
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

            if (Objects.nonNull(factory)) {
                if (bean.isHdfs()) { // Hadoop FS
                    HDFS hdfs = new HDFS(bean.getHdfsuri(), bean.getHdfsuser(), bean.getHdfshome());
                    String uuid = UUID.randomUUID().toString();
                    if (Objects.equals(bean.getFormat(), FormatType.JSON)) {
                        factory.HDFS_JSON(
                                hdfs.getFileSystem(),
                                new Path("analyse", new Path(bean.getType().toString(), new Path(bean.getMethod().value(), new Path(uuid, MessageFormat.format("result.{0}", bean.getFormat().value())))))
                        );
                    } else if (Objects.equals(bean.getFormat(), FormatType.XML)) {
                        factory.HDFS_XML(
                                hdfs.getFileSystem(),
                                new Path("analyse", new Path(bean.getType().toString(), new Path(bean.getMethod().value(), new Path(uuid, MessageFormat.format("result.{0}", bean.getFormat().value())))))
                        );
                    }
                } else {
                    if (Objects.isNull(bean.getOut())) { // System.out
                        if (Objects.equals(bean.getFormat(), FormatType.JSON)) {
                            factory.JSON(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                        } else if (Objects.equals(bean.getFormat(), FormatType.XML)) {
                            factory.XML(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                        }
                    } else { // HDD
                        String uuid = UUID.randomUUID().toString();
                        java.nio.file.Path path = Paths.get(MessageFormat.format("{0}/analyse/{1}/{2}/{3}", bean.getOut(), bean.getType().toString(), bean.getMethod().value(), uuid));
                        if(Files.notExists(path))   Files.createDirectories(path);
                        if (Objects.equals(bean.getFormat(), FormatType.JSON)) {
                            factory.JSON(new FileOutputStream(Paths.get(MessageFormat.format("{0}/result.{1}", path, bean.getFormat().value())).toFile()));
                        } else if (Objects.equals(bean.getFormat(), FormatType.XML)) {
                            factory.XML(new FileOutputStream(Paths.get(MessageFormat.format("{0}/result.{1}", path, bean.getFormat().value())).toFile()));
                        }
                        logger.info(MessageFormat.format("End Write file into hdd: {0}/result.{1}", path, bean.getFormat().value()));
                    }
                }
            }

            logger.info(String.format("End %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
        }catch(Exception e){
            logger.error(e);
        }
	}
}
