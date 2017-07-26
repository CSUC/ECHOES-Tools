/**
 * 
 */
package org.csuc.test;

import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.parser.OAIFactory;
import org.csuc.parser.ParserFactory;
import org.csuc.parser.Sax;

/**
 * @author amartinez
 *
 */
public class OAIParser {

	private static Logger logger = LogManager.getLogger(OAIParser.class);
	
	private Instant inici = Instant.now();
	
	public OAIParser() {}
	
	public OAIParser(String[] args) {		
		String host = null;
		String verb = null;
		String key = null;
		String metadataPrefix = null;
		String set = null;
		String resumptionToken = null;
		
		for(int i = 0; i < args.length -1; i++){
			if(args[i].equals("--host"))	host = args[i+1];
			if(args[i].equals("--verb"))	verb = args[i+1];
			if(args[i].equals("--key"))	key = args[i+1];
			if(args[i].equals("--metadataPrefix"))	metadataPrefix = args[i+1];
			if(args[i].equals("--set"))	set = args[i+1];
			if(args[i].equals("--resumptionToken"))	resumptionToken = args[i+1];			
		}
		
		if(Objects.nonNull(host) && Objects.nonNull(verb) && Objects.nonNull(metadataPrefix)
				&& Objects.nonNull(set)) {
			ParserFactory factoryELO = new OAIFactory(new Sax(host, verb, key, metadataPrefix, set, null));			
			factoryELO.instanceFactory().execute();				
			factoryELO.getMapValues().entrySet().forEach(e->{
				String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
				logger.info(String.format("|%s|%s|%s|", e.getValue(), elementNameCount, e.getKey()));
			});	
			factoryELO.getDuration();
			logger.info(String.format("Duration: %s", App.duration(inici)));
		}else if(Objects.nonNull(host) && Objects.nonNull(resumptionToken)) {
			ParserFactory factoryELO = new OAIFactory(new Sax(host, null, null, null, null, resumptionToken));			
			factoryELO.instanceFactory().execute();				
			factoryELO.getMapValues().entrySet().forEach(e->{
				String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
		        logger.info(String.format("%-25s-----%s", e.getValue() + "(" + elementNameCount +")", e.getKey()));
			});	
			factoryELO.getDuration();
			logger.info(String.format("Duration: %s", App.duration(inici)));
		}else {
			logger.info("--host [host] --verb [verb] --key [key] --metadataPrefix [metadataPrefix] --set [set]");
			logger.info("--host [host] --verb [verb] --key [key] --resumptionToken [resumptionToken]");
		}
	}
}
