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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
import org.csuc.schematron.SchematronUtil;
import org.csuc.transformation.Transformations;
import org.openarchives.oai._2.OAIPMHtype;
import org.w3.x1999.x02.x22RdfSyntaxNs.RDFDocument;

/**
 * @author amartinez
 *
 */
public class OAIDownloadSet {

	private static Logger logger = LogManager.getLogger(OAIDownloadSet.class);
	
	private Instant inici = Instant.now();
	
	public OAIDownloadSet() {}
	
	public OAIDownloadSet(String[] args) {		
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
		logger.info(String.format("Duration: %s", App.duration(inici)));
	}
	
	@SuppressWarnings("deprecation")
	private void download(URL url,  String key, String set, String xslt) {
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
	
	/**
	 * 
	 * @param schematron
	 * @param source
	 */
	private void validateAndSchematron(String schematron, String source){
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
}
