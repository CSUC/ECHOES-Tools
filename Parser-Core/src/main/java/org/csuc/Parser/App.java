/**
 * 
 */
package org.csuc.Parser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.Parser.Core.factory.*;
import org.csuc.Parser.Core.strategy.dom.Dom;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.csuc.Parser.Core.strategy.sax.Sax;
import org.csuc.Parser.Core.strategy.stax.Stax;
import org.csuc.Parser.Core.strategy.xom.Xom;
import org.csuc.Parser.Core.strategy.xslt.Xslt;
import org.csuc.Parser.Core.util.EnumTypes;
import org.csuc.Parser.Core.util.MethodType;
import org.csuc.Parser.Core.util.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author amartinez
 *
 */
public class App {
	private static Logger logger = LogManager.getLogger(App.class.getName());
		
	private static Instant inici = Instant.now();
	

	
	private static Parser factory = null;

    private static String type;
    private static String method;
    private static String input;
	private static String out;
	private static String format;
		
	/**
	 * @param args
	 * @throws Exception 
	 * @throws MalformedURLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {		
		if(Objects.nonNull(args) && args.length != 0) {
			for(int i = 0; i < args.length -1; i++){
				if(args[i].equals("--type"))	type = args[i+1];
				if(args[i].equals("--method"))	method = args[i+1];
				if(args[i].equals("--input"))	input = args[i+1];
				if(args[i].equals("--out"))	out = args[i+1];
                if(args[i].equals("--format"))	format = args[i+1];
			}
			
			if(Objects.isNull(type))	throw new Exception("type must not be null");
            if(Objects.isNull(method)) throw new Exception("method must not be null");
            if(Objects.isNull(input)) throw new Exception("input must not be null");

			Arrays.stream(EnumTypes.values())
            	.filter(e -> e.equalsName(type))
            	.findFirst()
            	.orElseThrow(() -> new IllegalStateException(
            			String.format("select valid type: %s",
            					Arrays.stream(EnumTypes.values()).map(EnumTypes::toString).collect(Collectors.joining(",")))));

            if(Objects.isNull(method))	throw new Exception("type must not be null");
            Arrays.stream(MethodType.values())
                    .filter(e -> e.equalsName(method))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            String.format("select valid method: %s",
                                    Arrays.stream(MethodType.values()).map(MethodType::toString).collect(Collectors.joining(",")))));
			
			if(EnumTypes.OAI.equalsName(type)) 	oai();							
			else if(EnumTypes.URL.equalsName(type)) url();
			else if(EnumTypes.FILE.equalsName(type)) file();
			
			if(Objects.nonNull(factory)) {
				if(Objects.isNull(out)) {
				    if(Objects.equals("json", format)){
                        factory.JSON(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                    }else if(Objects.equals("xml", format)){
                        factory.XML(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                    }else{
                        factory.XML(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream());
                    }
				}else{
                    if(Objects.equals("json", format)){
                        factory.JSON(new FileOutputStream(Paths.get(out + "result.json").toFile()));
                    }else if(Objects.equals("xml", format)){
                        factory.XML(new FileOutputStream(Paths.get(out + "result.xml").toFile()));
                    }else{
                        factory.XML(new FileOutputStream(Paths.get(out + "result.xml").toFile()));
                    }
                }
			}
			logger.info(String.format("End %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));

		}else logger.error(
		        String.format("--type [%s] --method [%s] --input [input] --out [out] --format [xml or json]",
                    Arrays.stream(EnumTypes.values()).map(m-> m.toString()).collect(Collectors.joining(", ")),
                    Arrays.stream(MethodType.values()).map(m-> m.toString()).collect(Collectors.joining(", "))
            ));
	}
	
	private static void oai() throws Exception {
        if(MethodType.DOM4J.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserOAI(new Dom4j()));
        }
        if(MethodType.SAX.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserOAI(new Sax()));
        }
        if(MethodType.DOM.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserOAI(new Dom()));
        }
        if(MethodType.XSLT.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserOAI(new Xslt()));
        }
        if(MethodType.XOM.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserOAI(new Xom()));
        }
        if(MethodType.STAX.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserOAI(new Stax()));
        }
        factory.execute(new URL(input));

	}
	
	
	private static void url() throws Exception {
        if(MethodType.DOM4J.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserURL(new Dom4j()));
        }
        if(MethodType.SAX.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserURL(new Sax()));
        }
        if(MethodType.DOM.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserURL(new Dom()));
        }
        if(MethodType.XSLT.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserURL(new Xslt()));
        }
        if(MethodType.XOM.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserURL(new Xom()));
        }
        if(MethodType.STAX.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserURL(new Stax()));
        }

        factory.execute(new URL(input));

	}
	
	private static void file() throws Exception {
        if(MethodType.DOM4J.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserFILE(new Dom4j()));
        }
        if(MethodType.SAX.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserFILE(new Sax()));
        }
        if(MethodType.DOM.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserFILE(new Dom()));
        }
        if(MethodType.XSLT.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserFILE(new Xslt()));
        }
        if(MethodType.XOM.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserFILE(new Xom()));
        }
        if(MethodType.STAX.equalsName(method)){
            factory = FactoryParser.createFactory(new ParserFILE(new Stax()));
        }

        factory.execute(input);

	}
}
