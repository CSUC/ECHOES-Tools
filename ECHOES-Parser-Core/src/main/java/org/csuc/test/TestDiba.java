/**
 * 
 */
package org.csuc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.csuc.schematron.SchematronUtil;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import eu.europeana.schemas.edm.EdmType;
import eu.europeana.schemas.edm.PlaceType;
import eu.europeana.schemas.edm.ProvidedCHOType;
import eu.europeana.schemas.edm.RDF;
import eu.europeana.schemas.edm.TimeSpanType;

/**
 * @author amartinez
 *
 */
public class TestDiba {

	private static Logger logger = LogManager.getLogger(TestDiba.class);
	
	private static String RESOURCE = "";
	private static String DBPEDIA = "http://dbpedia.org/data/";
	private static String SCH = "/amartinez/projectes/echoes/Entitats/SchematronEDM.sch";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
				Arrays.asList("/home/amartinez/Baixades/PC111E5M08033P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08096P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08126P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08138P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08211P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08270P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08297P_EDM.csv",
				"/home/amartinez/Baixades/PC111E5M08905P_EDM.csv")
				.forEach(data->{
					createEDMDiba(readWithCsvListReader(data), "/home/amartinez/Escriptori/ECHOES/diba");	
				});			
	}
	
	/**
	 * 
	 * @param schematron
	 * @param source
	 */
	@SuppressWarnings("unused")
	private static void validateSchematron(String schematron, String source){
		File sch = new File(schematron);
		try{
			Files.walk(Paths.get(source))
			.filter(Files::isRegularFile)
			.parallel()
			.filter(f-> f.toString().endsWith(".xml"))
			.filter(f-> SchematronUtil.isInvalid(sch, new StreamSource(f.toFile())))
			.forEach(f->{
				try {
					logger.info(SchematronUtil.getFailedAssert(sch, new StreamSource(f.toFile())));					
					if(Files.deleteIfExists(f))						
						logger.info(String.format("delete file %s", f.getFileName()));					
				}catch(Exception e){
					logger.error(e);
				}
			});
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * 		0  "edm.ProvidedCHO.dcidentifier", -> NumFitxa 0
	 *		1  "edm.ProvidedCHO.dccoverage", -> Població 1
//	 *		2  "edm.Place.skospreflabel", -> (Denom) 
	 *		3  "edm.ProvidedCHO.dcsubject", -> Àmbit 3 -> 2
	 *		4  "edm.ProvidedCHO.dctitle", -> Denom 4-> 3
//	 *		5  "edm.Place.skospreflabel", -> (Població)
	 *		6  "edm.ProvidedCHO.dctermsspatial", -> Ubicació 6->4
//	 *		7  "edm.Place.skospreflabel", -> (Ubicació) 
	 *		8  "edm.ProvidedCHO.dctype", -> Tipologia 8->5
	 *		9  "edm.ProvidedCHO.dcdescription", -> Descripció 9->6
	 *		10 "edm.ProvidedCHO.dcdescription", -> Observacions 10->7
	 *		11 "edm.ProvidedCHO.dcdescription", -> NotesConservació 11->8
	 *		12 "edm.ProvidedCHO.dccreator", -> Autor 12->9
	 *		13 "edm.ProvidedCHO.dcdate", -> Any 13->10 
	 *		14 "edm.ProvidedCHO.dctermstemporal", -> Segle 14->11
//	 *		15 "edm.TimeSpan.skospreflabel", -> (segle)
	 *		16 "edm.Place.skosnote", -> Emplaçament 16->12
	 *		17 "edm.Place.wgs84poslat", ->X 17->13 
	 *		18 "edm.Place.wgs84poslong", -> Y 18->14 
	 *		19 "edm.Place.wgs84posalt", -> Alçada 19->15 
	 *		20 "edm.ProvidedCHO.dcdescription", -> Història 20->16 
	 *		21 "edm.ProvidedCHO.dcrelation" -> Vincle 21->17
	 * 
	 * 
	 * @param input
	 * @param out
	 */
	private static void createEDMDiba(List<List<Object>> input, String outFolder){		
		Map<String,String> namespaces = new HashMap<String,String>();

		namespaces.put("http://purl.org/dc/elements/1.1/", "dc");
		namespaces.put("http://www.w3.org/2004/02/skos/core#", "skos");
		namespaces.put("http://www.openarchives.org/ore/terms/", "ore");
		namespaces.put("http://rdvocab.info/ElementsGr2/", "rdaGr2");
		namespaces.put("http://purl.org/dc/terms/", "dcterms");
		namespaces.put("http://www.w3.org/2003/01/geo/wgs84_pos#", "wgs84");

		XmlOptions options = 
				new XmlOptions()
					.setSaveSuggestedPrefixes(namespaces)
					.setSaveAggressiveNamespaces()					
					.setSavePrettyPrint();
		
		input.stream()
		.forEach(list->{
			Path path = createDirectories(outFolder + File.separator + list.get(1));
			
			try{				
				// Set up the validation error listener.
				ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
				XmlOptions validationOptions = new XmlOptions();				
				validationOptions.setErrorListener(validationErrors);
				
				RDF rdf = RDF.Factory.newInstance();
				
				//providedCHO
				ProvidedCHOType provided = rdf.addNewProvidedCHO();
				provided.setAbout(RESOURCE + "diba_" + StringUtils.replaceAll(list.get(1).toString(), "\\s", "_") + "_"+ StringUtils.replaceAll(list.get(0).toString(), "\\s", "_"));
				provided.setType2(EdmType.IMAGE);
				
				if(list.get(0) !=null) provided.addNewIdentifier().setStringValue(list.get(0).toString());
				if(list.get(1) !=null)	 provided.addNewCoverage().setStringValue(list.get(1).toString());
				if(list.get(2) !=null) provided.addNewSubject().setStringValue(list.get(2).toString());
				if(list.get(3) !=null) provided.addNewTitle().setStringValue(list.get(3).toString());
				
				//6
				if(list.get(1) !=null) provided.addNewSpatial().setResource(DBPEDIA + StringUtils.replaceAll(list.get(1).toString(), "\\s", "_"));
				if(list.get(3) !=null)	provided.addNewSpatial().setResource(RESOURCE + StringUtils.replaceAll(list.get(3).toString(), "\\s", "_"));					
								
				if(list.get(5) !=null) provided.addNewType().setStringValue(list.get(5).toString());
				if(list.get(6) !=null) provided.addNewDescription().setStringValue(list.get(6).toString());
				if(list.get(7) !=null) provided.addNewDescription().setStringValue(list.get(7).toString());
				if(list.get(8) !=null) provided.addNewDescription().setStringValue(list.get(8).toString());
				if(list.get(9) !=null)	provided.addNewCreated().setStringValue(list.get(9).toString());
				if(list.get(10) != null)	provided.addNewDate().setStringValue(list.get(10).toString());
				if(list.get(11) != null)	provided.addNewTemporal().setResource(RESOURCE + list.get(11).toString());	
				
				//TimeSpan					
				if(list.get(11) != null){
					TimeSpanType time = rdf.addNewTimeSpan();		
					time.setAbout(RESOURCE + StringUtils.replaceAll(list.get(11).toString(), "\\s", "_"));
					time.addNewPrefLabel().setStringValue(list.get(11).toString());
				}
				
				//Place 
				// 2
				if(Objects.nonNull(list.get(3))){
					PlaceType place = rdf.addNewPlace();
					place.setAbout(RESOURCE + StringUtils.replaceAll(list.get(3).toString(), "\\s", "_"));
					place.addNewPrefLabel().setStringValue(list.get(3).toString());
					if(list.get(13) != null) place.setLat(Float.parseFloat(list.get(13).toString()));
					if(list.get(14) != null) place.setLong(Float.parseFloat(list.get(14).toString()));
					if(list.get(15) != null)	place.setAlt(Float.parseFloat(list.get(15).toString()));
					if(list.get(4) !=null)	place.addNewNote().setStringValue(list.get(4).toString());
					if(list.get(1) !=null)	place.addNewIsPartOf().setResource(DBPEDIA + StringUtils.replaceAll(list.get(1).toString(), "\\s", "_"));
				}
												
				if(list.get(1) !=null){
					PlaceType place2 = rdf.addNewPlace();
					place2.addNewPrefLabel().setStringValue(list.get(1).toString());
					//16, 17, 18 i 19
					if(list.get(12) != null) place2.addNewNote().setStringValue(list.get(12).toString());
					place2.addNewHasPart().setResource(RESOURCE + StringUtils.replaceAll(list.get(3).toString(), "\\s", "_"));
					place2.setAbout(DBPEDIA + StringUtils.replaceAll(list.get(1).toString(), "\\s", "_"));
				}
				
				boolean isValid = rdf.validate(validationOptions);
				// Print the errors if the XML is invalid.
				if (!isValid){
					validationErrors.forEach(xmlValidationError->{
						logger.info(xmlValidationError);
					});					
				}
				else{
					String content = rdf.xmlText(options).replaceAll("xml-fragment", "rdf:RDF");
					Files.write(Paths.get(path.toString() + File.separator + list.get(0) + ".xml"), content.getBytes());
					
					if(SchematronUtil.isInvalid(new File(SCH),
							new StreamSource(Paths.get(Paths.get(path.toString() + File.separator + list.get(0) + ".xml").toString()).toFile()))){
						if(Files.deleteIfExists(Paths.get(Paths.get(path.toString() + File.separator + list.get(0) + ".xml").toString())))						
							logger.info(String.format("delete file %s",
								Paths.get(Paths.get(path.toString() + File.separator + list.get(0) + ".xml").toString()).getFileName()));		
					}
					
				}				
			}catch(Exception e){
				logger.error(e);
			}
		});
	}
	
	/**
	 * Reading using CsvListReader.
	 * 
	 * @return 
	 */
	private static List<List<Object>> readWithCsvListReader(String file) {		 
		List<List<Object>> result = new ArrayList<>();
		
		ICsvListReader listReader = null;
        try {
                listReader = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);                
                listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
                final CellProcessor[] processors = getProcessors();

                List<Object> customerList = new ArrayList<>();
        		while( (customerList = listReader.read(processors)) != null ) {
                       result.add(customerList);
                }
                
        }catch(SuperCsvException e ){
        	logger.error(e);
        } catch (FileNotFoundException e) {
        	logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}finally {
			if( listReader != null ) {
				try {
					listReader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
        }
        return result;
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
	
	/**
	 * 
	 * @return
	 */
	private static CellProcessor[] getProcessors() {		
		final CellProcessor[] processors = 
		new CellProcessor[] { 
				new Optional(),	//"edm.ProvidedCHO.dcidentifier", 0
				new Optional(),	//"edm.ProvidedCHO.dccoverage", 1
//				new Optional(),	//"edm.Place.skospreflabel",2
				new Optional(),	//"edm.ProvidedCHO.dcsubject",3
				new Optional(),	//"edm.ProvidedCHO.dctitle",4
//				new Optional(),	//"edm.Place.skospreflabel",5
				new Optional(),	//"edm.ProvidedCHO.dctermsspatial",6
//				new Optional(),	//"edm.Place.skospreflabel",7
				new Optional(),	//"edm.ProvidedCHO.dctype",8
				new Optional(),	//"edm.ProvidedCHO.dcdescription",9
				new Optional(),	//"edm.ProvidedCHO.dcdescription",10
				new Optional(),	//"edm.ProvidedCHO.dcdescription",11
				new Optional(),	//"edm.ProvidedCHO.dccreator",12
				new Optional(),	//"edm.ProvidedCHO.dcdate",13
				new Optional(),	//"edm.ProvidedCHO.dctermstemporal",14
//				new Optional(),	//"edm.TimeSpan.skospreflabel",15
				new Optional(),	//"edm.Place.skosnote",16
				new Optional(),	//"edm.Place.wgs84poslat",17
				new Optional(),	//"edm.Place.wgs84poslong",18
				new Optional(),	//"edm.Place.wgs84posalt",19
				new Optional(),	//"edm.ProvidedCHO.dcdescription",20
				new Optional(),	//"edm.ProvidedCHO.dcrelation"21
		};
	    return processors;
	}

}
