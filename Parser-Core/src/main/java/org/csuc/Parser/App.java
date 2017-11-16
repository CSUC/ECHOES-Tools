/**
 * 
 */
package org.csuc.Parser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.parser.EnumTypes;
import org.csuc.Parser.Core.parser.FileParser;
import org.csuc.Parser.Core.parser.OAIFactory;
import org.csuc.Parser.Core.parser.OAIParser;
import org.csuc.Parser.Core.parser.ParserFactory;
import org.csuc.Parser.Core.parser.Sax;
import org.csuc.Parser.Core.parser.URLParser;


/**
 * @author amartinez
 *
 */
public class App {
	private static Logger logger = LogManager.getLogger(App.class.getName());
		
	private static Instant inici = Instant.now();
	
	private static String type = null;
	
	private static ParserFactory factoryELO = null;
	
	
	private static String url;
	private static String file;
	private static String out;
		
	/**
	 * @param args
	 * @throws Exception 
	 * @throws MalformedURLException 
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) throws Exception {		
		if(Objects.nonNull(args) && args.length != 0) {
			for(int i = 0; i < args.length -1; i++){
				if(args[i].equals("--type"))	type = args[i+1];
				if(args[i].equals("--url"))	url = args[i+1];
				if(args[i].equals("--file"))	file = args[i+1];
				if(args[i].equals("--out"))	out = args[i+1];
			}
			
			if(Objects.isNull(type))	throw new Exception("type must not be null"); 
			Arrays.stream(EnumTypes.values())
            	.filter(e -> e.equalsName(type))
            	.findFirst()
            	.orElseThrow(() -> new IllegalStateException(
            			String.format("select valid type: %s",
            					Arrays.stream(EnumTypes.values()).map(m-> m.toString()).collect(Collectors.joining(",")))));
			
			if(EnumTypes.OAI.equalsName(type)) 	oai();							
			else if(EnumTypes.URL.equalsName(type)) url();
			else if(EnumTypes.FILE.equalsName(type)) file();
			
			if(Objects.nonNull(factoryELO)) {
				factoryELO.instanceFactory().execute();
				if(Objects.isNull(out)) {
					if(!factoryELO.getMapValues().isEmpty()) {
						logger.info(String.format("tag;total;xpath"));
						factoryELO.getMapValues().entrySet().forEach(e->{
							String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
							logger.info(String.format("%s;%s;%s", e.getValue(), elementNameCount, e.getKey()));
						});					
						logger.info(factoryELO.getNamespaces());
						logger.info(factoryELO.getDuration());
					}					
				}else {
					Path outpath = Files.createDirectories(Paths.get(out));				
					if(!factoryELO.getMapValues().isEmpty()) {
						StringBuffer buffer = new StringBuffer();
						buffer.append(String.format("tag;total;xpath\n"));
						factoryELO.getMapValues().entrySet().forEach(e->{
							String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
							buffer.append(String.format("%s;%s;%s\n", e.getValue(), elementNameCount, e.getKey()));
						});
						factoryELO.getNamespaces().entrySet().forEach(e->buffer.append(String.format("%s\n", e)));
						buffer.append(factoryELO.getDuration());
						
						Files.write(Paths.get(outpath.toString() + File.separator + String.format("%sParser", type)), buffer.toString().getBytes(), new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.APPEND });
					}					
				}
			}
			logger.info(String.format("End %s", duration(inici)));
		}else logger.error("--type [oai, url, file]");		
	}
	
	private static void oai() throws Exception {
		if(Objects.isNull(url)) throw new Exception("url must not be null");
		factoryELO = new OAIFactory(new Sax(new OAIParser(url)));
	}
	
	
	private static void url() throws Exception {
		if(Objects.isNull(url)) throw new Exception("url must not be null");
		factoryELO = new OAIFactory(new Sax(new URLParser(url)));		
	}
	
	private static void file() throws Exception {
		if(Objects.isNull(file)) throw new Exception("file must not be null");
		factoryELO = new OAIFactory(new Sax(new FileParser(file)));	
	}
		
	public static String duration(Instant inici) {
		long diff = Duration.between(inici, Instant.now()).getSeconds();
	    return String.format("%02d:%02d:%02d", diff / 3600, diff % 3600 / 60, diff % 60);
	}
}
