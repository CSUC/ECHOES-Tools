/**
 * 
 */
package org.csuc.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.openarchives.ore.terms.AggregationDocument.Aggregation;

import eu.europeana.schemas.edm.EdmType;
import eu.europeana.schemas.edm.ProvidedCHOType;
import eu.europeana.schemas.edm.RDF;
import eu.europeana.schemas.edm.WebResourceType;

/**
 * @author amartinez
 *
 */
public class Newspapers {
	private static Logger logger = LogManager.getLogger(Newspapers.class);
	private String RESOURCE = "";
	
	
	private String file_titles = null;
	private String file_content = null;
	private String separator = ",";
	private String out = "./tmp/newspapers/";
	
	private List<List<String>> values;	
	private Map<String, List<List<String>>> mapValues = new HashMap<String, List<List<String>>>();
	private Map<String,String> mapTitlesValues;
	
	private Instant inici = Instant.now();
	
	public Newspapers() {}
	
	public Newspapers(String[] args) {
		for(int i = 0; i < args.length -1; i++){
			if(args[i].equals("--titles"))	file_titles = args[i+1];
			if(args[i].equals("--content"))	file_content = args[i+1];
			if(args[i].equals("--separator"))	separator = args[i+1];
		}
		try {
			if(Objects.nonNull(file_titles) && Objects.nonNull(file_content)) {
				try {
					createDirectories();
					mapTitlesValues = getMapTitles();
					parserCSVFile();
					
					values.stream().skip(1).forEach(v->{					
						String barcode = StringUtils.strip(v.get(8), "\"");				
						mapValues.computeIfAbsent(barcode, k -> new ArrayList<List<String>>()).add(v);
					});					
					mapValues.entrySet().forEach(e->{
						RDF rdf = RDF.Factory.newInstance();
						e.getValue().forEach(list->{createRDF(rdf, list);});
						try {
							String content = rdf.xmlText(xmloptions()).replaceAll("xml-fragment", "rdf:RDF");
							Files.write(Paths.get(out + e.getKey().toString() + ".xml"), content.getBytes(), StandardOpenOption.CREATE_NEW);
							logger.info(String.format("Save %s", out + e.getKey().toString() + ".xml"));
						} catch (Exception e1) {
							logger.error(e1);
						}
					});
				}catch(Exception e) {
					logger.error(e);
				}			
			}else{
				logger.info("--titles [file_titles] --content [file_content]");
			}
		}finally {
			logger.info(String.format("Duration: %s", App.duration(inici)));
		}		
	}
	
	private Map<String,String> getMapTitles() {
		try (Stream<String> lines = Files.lines(Paths.get(file_titles))) {
			return lines
					.map(line -> line.split(";")).skip(1)
					.collect(Collectors.toMap(line -> line[0], line -> line[1], (e1, e2) -> e1));		
		}catch(Exception e) {
			logger.error(e);
			return null;
		}
	}
	
	public void parserCSVFile() {
		try (Stream<String> lines = Files.lines(Paths.get(file_content))) {
			values = lines.map(line -> Arrays.asList(line.split(separator))).collect(Collectors.toList());
		}catch(Exception e) {
			logger.error(e);
		}		
	}
	
	/**
	 * 
	 * @param rdf
	 * @param list
	 */
	@SuppressWarnings("unused")
	private void createRDF(RDF rdf, List<String> list) {
		String scanfilename = StringUtils.strip(list.get(0), "\"");
		String scanlink = StringUtils.strip(list.get(1), "\"");
		String ocrfilename = StringUtils.strip(list.get(2), "\"");
		String ocrlink = StringUtils.strip(list.get(3), "\"");
		String periodicalcode = StringUtils.strip(list.get(4), "\"");
		String date = StringUtils.strip(list.get(5), "\"");
		String page = StringUtils.strip(list.get(6), "\"");
		String edition = StringUtils.strip(list.get(7), "\"");
		String barcode = StringUtils.strip(list.get(8), "\"");
		
		try{	
			//providedCHO
			ProvidedCHOType provided;
			if(Stream.of(rdf.getProvidedCHOArray()).filter(f-> f.getAbout().equals(RESOURCE + "newspaper:" + StringUtils.replaceAll(barcode.toString(), "\\s", "_"))).count() == 0) {
				provided = rdf.addNewProvidedCHO();
				provided.setAbout(RESOURCE + "newspaper:" + StringUtils.replaceAll(barcode.toString(), "\\s", "_"));
				provided.setType2(EdmType.TEXT);
			}else {
				provided =
					Stream.of(rdf.getProvidedCHOArray()).filter(f-> f.getAbout().equals(RESOURCE + "newspaper:" + StringUtils.replaceAll(barcode.toString(), "\\s", "_"))).findFirst().get();
			}
			if(Stream.of(provided.getTitleArray()).filter(f-> f.getStringValue().equals(mapTitlesValues.get(periodicalcode))).count() == 0)
				provided.addNewTitle().setStringValue(mapTitlesValues.get(periodicalcode));
			if(Stream.of(provided.getDateArray()).filter(f-> f.getStringValue().equals(date)).count() == 0)
				provided.addNewDate().setStringValue(date);
			if(Stream.of(provided.getIsPartOfArray()).filter(f-> f.getStringValue().equals(String.format("%s,p.%s,%s", periodicalcode,page,edition))).count() == 0)
				provided.addNewIsPartOf().setStringValue(String.format("%s,p.%s,%s", periodicalcode,page,edition));
			if(Stream.of(provided.getIsRelatedToArray()).filter(f-> f.getResource().equals(StringUtils.replaceAll(ocrlink, "\\s", "_"))).count() == 0)
				provided.addNewIsRelatedTo().setResource(StringUtils.replaceAll(ocrlink, "\\s", "_"));
			if(Stream.of(provided.getLanguageArray()).filter(f-> f.getStringValue().equals("NL")).count() == 0)
				provided.addNewLanguage().setStringValue("NL");
			
			//WebResource
			WebResourceType webresource;
			if(Stream.of(rdf.getWebResourceArray()).filter(f-> f.getAbout().equals(scanlink)).count() == 0) {
				webresource = rdf.addNewWebResource();
				webresource.setAbout(StringUtils.replaceAll(scanlink, "\\s", "_"));							
			}else{
				webresource = 
						Stream.of(rdf.getWebResourceArray()).filter(f-> f.getAbout().equals(scanlink)).findFirst().get();
			}
			if(Stream.of(webresource.getIsFormatOfArray()).filter(f-> f.getStringValue().equals(scanlink)).count() == 0)
				webresource.addNewIsFormatOf().setStringValue(scanlink);
			if(Stream.of(webresource.getDescriptionArray()).filter(f-> f.getStringValue().equals(scanfilename)).count() == 0)
				webresource.addNewDescription().setStringValue(scanfilename);
			if(Stream.of(webresource.getIssuedArray()).filter(f-> f.getStringValue().equals(date)).count() == 0)
				webresource.addNewIssued().setStringValue(date);
			if(Stream.of(webresource.getExtentArray()).filter(f-> f.getStringValue().equals(page)).count() == 0)
				webresource.addNewExtent().setStringValue(page);
				
			//Aggregation
			Aggregation aggregation;
			if(Stream.of(rdf.getAggregationArray()).filter(f-> f.getAbout().equals(ocrlink)).count() == 0) {							
				aggregation = rdf.addNewAggregation();
				aggregation.setAbout(StringUtils.replaceAll(ocrlink, "\\s", "_"));							
			}else {
				aggregation = 
					Stream.of(rdf.getAggregationArray()).filter(f-> f.getAbout().equals(ocrlink)).findFirst().get();							
			}						
			aggregation.addNewAggregatedCHO().setResource(StringUtils.replaceAll(ocrlink, "\\s", "_"));						
			aggregation.addNewDataProvider().setStringValue(periodicalcode);
									
			aggregation.addNewIsShownAt().setResource(StringUtils.replaceAll(ocrlink, "\\s", "_"));
			aggregation.addNewIsShownBy().setResource(StringUtils.replaceAll(scanlink, "\\s", "_"));;
			
			aggregation.addNewProvider().setStringValue(periodicalcode);
			aggregation.addNewRights2().setResource("http://creativecommons.org/publicdomain/mark/1.0/");
			
		}catch(Exception e) {
			logger.error(e);
		}
	}
	
	private XmlOptions xmloptions() {
		Map<String,String> namespaces = new HashMap<String,String>();

		namespaces.put("http://purl.org/dc/elements/1.1/", "dc");
		namespaces.put("http://www.w3.org/2004/02/skos/core#", "skos");
		namespaces.put("http://www.openarchives.org/ore/terms/", "ore");
		namespaces.put("http://rdvocab.info/ElementsGr2/", "rdaGr2");
		namespaces.put("http://purl.org/dc/terms/", "dcterms");
		namespaces.put("http://www.w3.org/2003/01/geo/wgs84_pos#", "wgs84");
				
		return new XmlOptions()
				.setSaveSuggestedPrefixes(namespaces)
				.setSaveAggressiveNamespaces()					
				.setSavePrettyPrint();
	
	}
	
	private void createDirectories() {
		try {
			if(!Files.exists(Paths.get(out), LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectories(Paths.get(out));
			}
			
		}catch (IOException e) {
			logger.error(e);
		}
	}
	
	public String getFile_titles() {
		return file_titles;
	}

	public void setFile_titles(String file_titles) {
		this.file_titles = file_titles;
	}

	public String getFile_content() {
		return file_content;
	}

	public void setFile_content(String file_content) {
		this.file_content = file_content;
	}
}
