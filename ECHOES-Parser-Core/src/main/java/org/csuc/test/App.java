/**
 * 
 */
package org.csuc.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.csuc.jaxb.UnmarshalOAIPMH;
import org.csuc.parser.OAIFactory;
import org.csuc.parser.ParserFactory;
import org.csuc.parser.Sax;
import org.csuc.schematron.SchematronUtil;
import org.csuc.transformation.Transformations;
import org.openarchives.oai._2.OAIPMHtype;
import org.w3.x1999.x02.x22RdfSyntaxNs.RDFDocument;

/**
 * @author amartinez
 *
 */
public class App {
	private static Logger logger = LogManager.getLogger(App.class.getName());
	
	private static Set<String> SetSpec = new HashSet<String>();

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		if(args[0].equals("OAIDownloadAllSets")){
			OAIDownload(args);
		}if(args[0].equals("OAIDownloadSet")){
			OAIDownloadSet(args);
		}else if(args[0].equals("OAIParser")){
			OAIParser(args);
		}else{
			logger.info("java -jar [OAIDownloadAllSets | OAIDownloadSet | OAIParser]");
		}		
	}

	private static void OAIDownloadSet(String[] args){
		Instant inici = Instant.now();
		
		String url = null;
		String verb = null;
		String key = null;
		String metadataPrefix = null;
		String set = null;
		String xslt = null;
		String sch = null;
		
		for(int i = 0; i < args.length -1; i++){
			if(args[i].equals("--host"))	url= args[i+1];
			if(args[i].equals("--verb"))	verb = args[i+1];
			if(args[i].equals("--key"))	key = args[i+1];
			if(args[i].equals("--metadataPrefix"))	metadataPrefix = args[i+1];
			if(args[i].equals("--set"))	set = args[i+1];
			if(args[i].equals("--xslt"))	xslt = args[i+1];
			if(args[i].equals("--sch"))	sch = args[i+1];
		}		
		
		if(Objects.nonNull(url) && Objects.nonNull(verb) && Objects.nonNull(metadataPrefix) 
				&& Objects.nonNull(set) && Objects.nonNull(xslt)){			
			try {
				if(Objects.nonNull(key))	download(new URL(String.format("%s?verb=%s&key=%s&metadataPrefix=%s&set=%s", url, verb, key, metadataPrefix, set)), key, set, xslt);
				else	download(new URL(String.format("%s?verb=%s&metadataPrefix=%s&set=%s", url, verb, metadataPrefix, set)), null, set, xslt);				
			} catch (MalformedURLException e) {
				logger.error(e);
			}finally{
				validateAndSchematron(sch, set);
			}
		}else{
			for(String type : Arrays.asList(url, verb, metadataPrefix, set, xslt)){				
				Objects.requireNonNull(type, String.format("%s requiere non null!", type.getClass().getName()));
			}
			logger.info("--host [host] --verb [verb] --key [key] --metadataPrefix [metadataPrefix] --set [set] --xslt [verb]");
		}		
		logger.info(String.format("Duration: %s", duration(inici)));
	}
	
	@SuppressWarnings("deprecation")
	private static void download(URL url,  String key, String set, String xslt) {
		logger.info(url);		
		Transformations transformation = new Transformations(xslt);
		
		try {
			//SaxParserFactory
			OAIPMHtype oai = new UnmarshalOAIPMH(url).getOaipmh();

			transformation.transformationsFromString(IOUtils.toString(url), set);
			//transformation.transformationsFromUrl(url, set);
			
			if(oai.getListRecords().getResumptionToken() != null){
				if(!oai.getListRecords().getResumptionToken().getValue().isEmpty()){				
					String token = oai.getListRecords().getResumptionToken().getValue().trim();
					if(Objects.nonNull(key)){
						download(
							new URL(String.format("%s?verb=ListRecords&key=%s&resumptionToken=%s", StringUtils.replaceAll(url.toString(), "\\?verb.+", ""), key, token)), key, set, xslt);
					}else{
						download(
							new URL(String.format("%s?verb=ListRecords&resumptionToken=%s", StringUtils.replaceAll(url.toString(), "\\?verb.+", ""), token)), null, set, xslt);
					}
				}			
			}
		} catch (IOException e) {
			logger.error(e);
		} catch (TransformerConfigurationException e) {
			logger.error(e);
		} catch (TransformerException e) {
			logger.error(e);
		}
	}
	
	private static void OAIDownload(String[] args){
		try {
			String url = null;
			String key = null;
			String metadataPrefix = null;
			String xslt = null;
			
			
			if(args[1].equals("--host"))	url = args[2];
			if(args[3].equals("--key"))	key = args[4];
			if(args[5].equals("--metadataPrefix"))	metadataPrefix = args[6];
			if(args[7].equals("--xslt"))	xslt = args[8];
			
			if(Objects.nonNull(url) && Objects.nonNull(metadataPrefix) && Objects.nonNull(xslt)){		
				OAIPMHtype doc = new UnmarshalOAIPMH(new URL(url + "?verb=ListSets")).getOaipmh();
				
				doc.getListSets().getSet().forEach(setType->{SetSpec.add(setType.getSetSpec());});
				
				for(String set : SetSpec){
					logger.info(set);
					try {
						if(Objects.nonNull(key)){							
							download(new URL(String.format("%s?verb=ListRecords&key=%s&metadataPrefix=a2a&set=%s", url, key, set)), key, set, xslt);
						}else{
							download(new URL(String.format("%s?verb=ListRecords&metadataPrefix=a2a&set=%s", url, set)), null, set, xslt);
						}
						
					} catch (MalformedURLException e) {
						logger.error(e);
					}
				}				
			}			
		} catch (MalformedURLException e) {
			logger.error(e);
		}
	}
	
	private static void OAIParser(String[] args){
		if(args[1].equals("--host") && args[3].equals("--verb")
			&& args[5].equals("--metadataPrefix") && args[7].equals("--set")){
			String host = args[2];
			String verb = args[4];
			String key = null;
			String metadataPrefix = args[6];
			String set = args[8];
						
			ParserFactory factoryELO = new OAIFactory(new Sax(host, verb, key, metadataPrefix, set, null));			
			factoryELO.instanceFactory().execute();				
			factoryELO.getMapValues().entrySet().forEach(e->{
				String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
				logger.info(String.format("|%s|%s|%s|", e.getValue(), elementNameCount, e.getKey()));
			});	
			factoryELO.getDuration();
		}else if(args[1].equals("--host") && args[3].equals("--resumptionToken")){
			String host = args[1];			
			String resumptionToken = args[3];
				
			ParserFactory factoryELO = new OAIFactory(new Sax(host, null, null, null, null, resumptionToken));			
			factoryELO.instanceFactory().execute();				
			factoryELO.getMapValues().entrySet().forEach(e->{
				String elementNameCount = factoryELO.getElementNameCount().entrySet().stream().filter(f-> f.getKey().equals(e.getValue())).map(m->m.getValue().toString()).collect(Collectors.joining());            	
		        logger.info(String.format("%-25s-----%s", e.getValue() + "(" + elementNameCount +")", e.getKey()));
			});	
			factoryELO.getDuration();		
		}else if(args[1].equals("--help") || args[1].equals("-h")){
			logger.info("--host [host] --verb [verb] --metadataPrefix [metadataPrefix] --set [set]");
			logger.info("--host [host] --verb [verb] --resumptionToken [resumptionToken]");
		}else{
			logger.info("--host [host] --verb [verb] --metadataPrefix [metadataPrefix] --set [set]");
			logger.info("--host [host] --verb [verb] --resumptionToken [resumptionToken]");
		}
	}

	/**
	 * 
	 * @param schematron
	 * @param source
	 */
	private static void validateAndSchematron(String schematron, String source){
		try{
			File sch = new File(schematron);
			Path pathSet =
					Files.walk(Paths.get("./tmp"))
					.parallel()
					.filter(Files::isDirectory)
					.filter(f-> f.getFileName().toString().equals(source))
					.findFirst().get();
			
			Files.walk(pathSet)
			.parallel()
			.filter(Files::isRegularFile)
			.filter(f-> f.toString().endsWith(".xml"))
			//.filter(f-> SchematronUtil.isInvalid(sch, new StreamSource(f.toFile())))
			.forEach(f->{
				try {
					List<XmlValidationError> errors = new ArrayList<XmlValidationError>();
					
					RDFDocument.Factory.parse(f.toFile(),
						new XmlOptions().setErrorListener(errors).setLoadLineNumbers(XmlOptions.LOAD_LINE_NUMBERS_END_ELEMENT)).validate();
					
					if(!errors.isEmpty()){
						errors.forEach(xmlValidationErrors->{
							logger.error(
								String.format("\nFile: %s\nLine: %s\nColumn: %s\nMessage: %s",
									f.toFile(),
									xmlValidationErrors.getLine(),
									xmlValidationErrors.getColumn(),
									xmlValidationErrors.getMessage()));
							try {
								if(Files.deleteIfExists(f))						
									logger.info(String.format("delete file %s", f.getFileName()));
							} catch (IOException e) {
								logger.error(e);
							}
						});
					}else{
						if(SchematronUtil.isInvalid(sch, new StreamSource(f.toFile()))){
							logger.info(SchematronUtil.getFailedAssert(sch, new StreamSource(f.toFile())));					
							if(Files.deleteIfExists(f))						
								logger.info(String.format("delete file %s", f.getFileName()));
						}
					}					
				}catch(Exception e){
					logger.error(e);
				}
			});			
		}catch (Exception e){
			logger.error(e);
		}
	}
	
	public static String duration(Instant inici) {
		long diff = Duration.between(inici, Instant.now()).getSeconds();
	    return String.format("%02d:%02d:%02d", diff / 3600, diff % 3600 / 60, diff % 60);
	}
}
