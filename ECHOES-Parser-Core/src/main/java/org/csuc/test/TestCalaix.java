/**
 * 
 */
package org.csuc.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.csuc.jaxb.UnmarshalOAIPMH;
import org.csuc.schematron.SchematronUtil;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.ResumptionTokenType;
import org.w3.x1999.x02.x22RdfSyntaxNs.RDFDocument;
import org.w3c.dom.Node;

import eu.europeana.schemas.edm.RDF;

/**
 * @author amartinez
 *
 */
public class TestCalaix {

	private static Logger logger = LogManager.getLogger(TestCalaix.class);
	
	private static Path PATH;
	
	private static OAIPMHtype oai;
	private static String host;
	private static String metadataPrefix;
	private static String sch;
	private static String out;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		if(Objects.nonNull(args) && args.length >0){		
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("--host"))	host= args[i+1];
				if(args[i].equals("--metadataPrefix"))	metadataPrefix = args[i+1];		
				if(args[i].equals("--out"))	out = args[i+1];		
				if(args[i].equals("--sch"))	sch = args[i+1];
			}
		}
					
//		PATH = createDirectories(out);			
//		downloadEDMCalaixNodeToString(String.format("%s?verb=ListRecords&metadataPrefix=%s", host, metadataPrefix));
//		downloadEDMCalaixXmlBeans(String.format("%s?verb=ListRecords&metadataPrefix=%s", host, metadataPrefix));
		
		
//		validateSchematron(sch, out);
		validateSchematron("/home/amartinez/projectes/ECHOES-Parser/ECHOES-Parser-Core/target/SchematronEDM.sch",
				"/home/amartinez/projectes/ECHOES-Parser/ECHOES-Parser-Core/target/tmp");
//		}
		
	}
	
	/**
	 * 
	 * @param schematron
	 * @param source
	 */
	private static void validateSchematron(String schematron, String source){
		File sch = new File(schematron);
		try{
			Files.walk(Paths.get(source))
			.filter(Files::isRegularFile)
//			.parallel()
			.filter(f-> f.toString().endsWith(".xml"))
//			.filter(f-> SchematronUtil.isInvalid(sch, new StreamSource(f.toFile())))
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
//							try {
//								if(Files.deleteIfExists(f))						
									logger.info(String.format("delete file %s", f.getFileName()));
//							} catch (IOException e) {
//								logger.error(e);
//							}
						});
					}
					if(SchematronUtil.isInvalid(sch, new StreamSource(f.toFile()))){
						logger.info(SchematronUtil.getFailedAssert(sch, new StreamSource(f.toFile())));					
//						if(Files.deleteIfExists(f))						
							logger.info(String.format("delete file %s", f.getFileName()));
					}
									
				}catch(Exception e){
					logger.error(e);
				}
			});
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * 
	 * @param url
	 */
//	private static void downloadEDMCalaixNodeToString(String url){
//		logger.info(url);
//		try {
//			oai = new UnmarshalOAIPMH(new URL(url)).getOaipmh();
//			
//			ResumptionTokenType token = oai.getListRecords().getResumptionToken();
//			
//			oai.getListRecords().getRecord().forEach(recordType->{				
//				if(Objects.isNull(recordType.getHeader().getStatus())){
//					 try {
//						 List<XmlValidationError> errors = new ArrayList<XmlValidationError>();
//						 
//						 String filename = PATH.toFile().getAbsolutePath() 
//									+ File.separator + recordType.getHeader().getIdentifier().replaceAll("/", "_") + ".xml";
//						 
//						 RDF rdf = RDFDocument.Factory.parse((Node) recordType.getMetadata().getAny(),
//								 new XmlOptions().setErrorListener(errors).setLoadLineNumbers(XmlOptions.LOAD_LINE_NUMBERS_END_ELEMENT)).getRDF();
//						 
//						 if(rdf.validate()){
//							 rdf.save(new FileOutputStream(Paths.get(filename).toFile()), new XmlOptions().setSavePrettyPrint());
//						 }else{
//							 errors.forEach(xmlValidationErrors->{
//									logger.error(
//										String.format("\nFile: %s\nLine: %s\nColumn: %s\nMessage: %s",
//											Paths.get(filename).toFile(),
//											xmlValidationErrors.getLine(),
//											xmlValidationErrors.getColumn(),
//											xmlValidationErrors.getMessage()));
//									try {
//										if(Files.deleteIfExists(Paths.get(filename)))						
//											logger.info(String.format("delete file %s", Paths.get(filename).getFileName()));
//									} catch (IOException e) {
//										logger.error(e);
//									}
//								});
//						 }
//					 } catch (IOException | XmlException e) {
//						 logger.error(e);
//					 }
//				}
//			 });
//			
//			if(Objects.nonNull(token)){				
//				if(Objects.nonNull(token.getValue()) && !token.getValue().isEmpty())
//					downloadEDMCalaixNodeToString(host + "?verb=ListRecords&resumptionToken=" + token.getValue());
//			}
//		} catch (MalformedURLException e) {
//			logger.error(e);
//		}
//	}
	
	@SuppressWarnings("unused")
	private static void downloadEDMCalaixXmlBeans(String url){
		logger.info(url);
		try {
			oai = new UnmarshalOAIPMH(new URL(url)).getOaipmh();
			if(Objects.nonNull(oai)){
				ResumptionTokenType token = oai.getListRecords().getResumptionToken();
				
				oai.getListRecords().getRecord().forEach(recordType->{				
					if(Objects.isNull(recordType.getHeader().getStatus())){
					//if(!recordType.getHeader().getStatus().equals(StatusType.DELETED)){
						 try {
							 String filename = PATH.toFile().getAbsolutePath() 
										+ File.separator + recordType.getHeader().getIdentifier().replaceAll("/", "_") + ".xml";
							 
							 Files.write(Paths.get(filename), nodeToString((Node) recordType.getMetadata().getAny()).getBytes());
							 
						 } catch (IOException e) {
							 logger.error(e);
						 }
					}
				 });
				
				if(Objects.nonNull(token)){				
					if(Objects.nonNull(token.getValue()) && !token.getValue().isEmpty())
						downloadEDMCalaixXmlBeans(host + "?verb=ListRecords&resumptionToken=" + token.getValue());
				}
			}			
		} catch (MalformedURLException e) {
			logger.error(e);
		}		
	}
	
	private static String nodeToString(Node input){
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
		    t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    t.setOutputProperty(OutputKeys.INDENT, "yes");
		    t.transform(new DOMSource(input), new StreamResult(sw));
		} catch (TransformerException te) {
			logger.error(te);
		}
		return sw.toString();
	}
	
	/**
	 * 
	 * @param out
	 * @return
	 */
	private static Path createDirectories(String out){
		try {
			if(!Files.exists(Paths.get(out), LinkOption.NOFOLLOW_LINKS))	Files.createDirectories(Paths.get(out));
			LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
			
			if(!Files.exists(Paths.get(out + File.separator + ldt.getYear()), LinkOption.NOFOLLOW_LINKS))	
				Files.createDirectories(Paths.get(out + File.separator + ldt.getYear()));
			
			if(!Files.exists(Paths.get(out + File.separator + ldt.getYear() + File.separator +ldt.getMonth()), LinkOption.NOFOLLOW_LINKS))	
				Files.createDirectories(Paths.get(out + File.separator + ldt.getYear() + File.separator +ldt.getMonth()));
			
			if(!Files.exists(Paths.get(out + File.separator + ldt.getYear() + File.separator +ldt.getMonth() + File.separator + ldt.getDayOfMonth()), LinkOption.NOFOLLOW_LINKS))	
				Files.createDirectories(Paths.get(out + File.separator + ldt.getYear() + File.separator +ldt.getMonth() + File.separator + ldt.getDayOfMonth()));
			
			
			return Paths.get(out + File.separator + ldt.getYear() + File.separator +ldt.getMonth() + File.separator + ldt.getDayOfMonth());
			
		} catch (IOException e) {
			logger.error(e);
			return null;
		}
	}
	
}
