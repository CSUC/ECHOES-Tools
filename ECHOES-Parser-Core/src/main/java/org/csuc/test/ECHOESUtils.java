/**
 * 
 */
package org.csuc.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
public class ECHOESUtils {

	private static Logger logger = LogManager.getLogger(ECHOESUtils.class);
	
	public ECHOESUtils() {}
	
	/**
	 * 
	 * @param schematron
	 * @param source
	 */
	static void validateAndSchematron(String schematron, String source){
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
	
	
	@SuppressWarnings("deprecation")
	static void download(URL url,  String key, String set, String xslt) {
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
}

