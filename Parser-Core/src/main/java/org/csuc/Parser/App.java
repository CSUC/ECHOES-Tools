/**
 * 
 */
package org.csuc.Parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.factory.*;
import org.csuc.Parser.Core.strategy.dom4j.Dom4j;
import org.csuc.Parser.Core.util.EnumTypes;
import org.csuc.Parser.Core.util.TimeUtils;

import java.io.IOException;
import java.net.MalformedURLException;
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
	
	private static String type = null;
	
	private static Parser factory = null;
	
	
	private static String url;
	private static String file;
	//private static String out;
		
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
				if(args[i].equals("--url"))	url = args[i+1];
				if(args[i].equals("--file"))	file = args[i+1];
				//if(args[i].equals("--out"))	out = args[i+1];
			}
			
			if(Objects.isNull(type))	throw new Exception("type must not be null"); 
			Arrays.stream(EnumTypes.values())
            	.filter(e -> e.equalsName(type))
            	.findFirst()
            	.orElseThrow(() -> new IllegalStateException(
            			String.format("select valid type: %s",
            					Arrays.stream(EnumTypes.values()).map(EnumTypes::toString).collect(Collectors.joining(",")))));
			
			if(EnumTypes.OAI.equalsName(type)) 	oai();							
			else if(EnumTypes.URL.equalsName(type)) url();
			else if(EnumTypes.FILE.equalsName(type)) file();
			
			if(Objects.nonNull(factory)) {
				//if(Objects.isNull(out)) {
					factory.getXPATHResult().forEach(logger::info);
                    logger.info(factory.getNamespaceResult());
				//}else {
				//	Path outpath = Files.createDirectories(Paths.get(out));
					//Write XPATH in File
				//}
			}
			logger.info(String.format("End %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
		}else logger.error("--type [oai, url, file]");		
	}
	
	private static void oai() throws Exception {
		if(Objects.isNull(url)) throw new Exception("url must not be null");
		factory = FactoryParser.createFactory(new ParserOAI(new Dom4j()));
		factory.execute(url);
	}
	
	
	private static void url() throws Exception {
		if(Objects.isNull(url)) throw new Exception("url must not be null");
		factory = FactoryParser.createFactory(new ParserURL(new Dom4j()));
		factory.execute(url);
	}
	
	private static void file() throws Exception {
		if(Objects.isNull(file)) throw new Exception("file must not be null");
		factory = FactoryParser.createFactory(new ParserFILE(new Dom4j()));
		factory.execute(file);
	}
}
