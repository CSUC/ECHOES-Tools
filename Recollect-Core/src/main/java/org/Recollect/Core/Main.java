package org.Recollect.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.util.Granularity;
import org.Recollect.Core.util.UTCDateProvider;
import org.Recollect.Core.util.Verb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.RecordType;


/**
 * 
 *@author amartinez
 *
 * 
 *
 */
public class Main {
	
	private static Logger logger = LogManager.getLogger(Main.class);
	
	private static Instant inici = Instant.now();
		
	private static String host = null;
	private static String verb = null;
	private static String identifier = null;	
	private static String metadataPrefix = null;
	private static String from = null;
	private static String until = null;	
	private static String set = null;
	private static String granularity = null;
	private static String resumptionToken = null;
	
	
	private static String out = null;
	private static String edmType = null;
	private static String provider = null;
	
	private static Path pathWithSetSpec;
	private static String xslt = null;
	
	private static String language = "und";
	
	private static UTCDateProvider dateProvider = new UTCDateProvider();;
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main( String[] args ) throws Exception {
    	if(Objects.nonNull(args) && args.length != 0) {
    		for(int i = 0; i < args.length; i++) {    			
    			if(args[i].equals("--host"))	host = args[i+1];
    			if(args[i].equals("--verb"))	verb = args[i+1];
    			if(args[i].equals("--identifier"))	identifier = args[i+1];    			
    			if(args[i].equals("--metadataPrefix"))	metadataPrefix = args[i+1];
    			if(args[i].equals("--from"))	from = args[i+1];
    			if(args[i].equals("--until"))	until = args[i+1];    			
    			if(args[i].equals("--set"))	set = args[i+1];
    			if(args[i].equals("--granularity"))	granularity = args[i+1];
    			if(args[i].equals("--resumptionToken"))	resumptionToken = args[i+1];
    			
    			if(args[i].equals("--xslt"))	xslt = args[i+1];
    			if(args[i].equals("--out"))	out = args[i+1];
    			
    			if(args[i].equals("--edmType"))	edmType = args[i+1];
    			if(args[i].equals("--provider"))	provider = args[i+1];
    			
    			if(args[i].equals("--language"))	language = args[i+1];
    		}
    		
    		if(Objects.isNull(host)) throw new Exception("host must not be null");
    		if(Objects.isNull(verb)) throw new Exception(String.format("select valid verb: %s", 
					"Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords"));
    		
        	if(verb.equals(Verb.Type.ListRecords.toString())) 	ListRecords();
        	else if(verb.equals(Verb.Type.Identify.toString())) Identify();     		
        	else if(verb.equals(Verb.Type.ListMetadataFormats.toString()))	ListMetadataFormats();      		
        	else if(verb.equals(Verb.Type.ListSets.toString())) ListSets();        		
        	else if(verb.equals(Verb.Type.GetRecord.toString())) 	GetRecord();        		
        	else if(verb.equals(Verb.Type.ListIdentifiers.toString())) ListIdentifiers();        		
        	else {
        		logger.error(String.format("select valid verb: %s", 
        				"Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords"));
        	}        	
        	logger.info(String.format("End %s", duration(inici)));
    	}else logger.error("--host [host] --verb [Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords]");    	
    }
    
    /**
     * @throws Exception 
     * 
     */
    private static void ListRecords() throws Exception {
    	//Check parameters
    	if(Objects.isNull(metadataPrefix)) throw new Exception("metadataPrefix must not be null");
    	if(Objects.isNull(edmType)) throw new Exception(String.format("select valid edmType: %s", "TEXT, VIDEO, IMAGE, SOUND, 3D"));
    	
    	if(Objects.isNull(provider))	throw new Exception("provider must not be null");
		if(!Arrays.asList("TEXT", "VIDEO", "IMAGE", "SOUND", "3D").contains(edmType))
			throw new Exception(String.format("select valid edmType: %s", 
    				"TEXT, VIDEO, IMAGE, SOUND, 3D"));
		
		Map<String,String> xsltProperties = new HashMap<String,String>();
		xsltProperties.put("edmType", edmType);
		xsltProperties.put("dataProvider", provider);
		if(Objects.nonNull(set))	xsltProperties.put("set", set);
		if(Objects.nonNull(language))	xsltProperties.put("language", language);
		
		OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
		ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
    	listRecordsParameters.withMetadataPrefix(metadataPrefix);
    	
    	if(Objects.nonNull(resumptionToken)) listRecordsParameters.withgetResumptionToken(resumptionToken);
    	
    	if(Objects.nonNull(set)) listRecordsParameters.withSetSpec(set);
        
        if(Objects.nonNull(from)) {
        	if(Objects.nonNull(granularity))	listRecordsParameters.withFrom(dateProvider.parse(from, Granularity.fromRepresentation(granularity)));
        	else	listRecordsParameters.withFrom(dateProvider.parse(from));
        }
        if(Objects.nonNull(granularity))	listRecordsParameters.withGranularity(granularity);        
        if(Objects.nonNull(until)) {
        	if(Objects.nonNull(granularity)) listRecordsParameters.withUntil(dateProvider.parse(until, Granularity.fromRepresentation(granularity)));
        	else	listRecordsParameters.withFrom(dateProvider.parse(until)); 
        }
        
        if(Objects.nonNull(out)) {
        	if(Objects.nonNull(set))
        		pathWithSetSpec = Files.createDirectories(Paths.get(out + File.separator + listRecordsParameters.getSetSpec()));
        	else	pathWithSetSpec = Files.createDirectories(Paths.get(out));        	
        }   
        
        Iterator<RecordType> records = recollect.listRecords(listRecordsParameters);
       	try {
       		DownloadListRecordData downloadData;
       		if(Objects.nonNull(pathWithSetSpec)) {
       			if(Objects.nonNull(xslt)) downloadData = new DownloadListRecordData(pathWithSetSpec, xslt, xsltProperties);
       			else	downloadData = new DownloadListRecordData(pathWithSetSpec);
       		}
       		else {
       			if(Objects.nonNull(xslt)) downloadData = new DownloadListRecordData(xslt, xsltProperties);
       			else downloadData = new DownloadListRecordData();
       		}
       		downloadData.executeWithNode(records);
        		
       		logger.info(String.format("[HOST] %s [SET] %s [MESSAGE] %s",
       				oaiClient.getURL(), listRecordsParameters.getSetSpec(), downloadData.getStatus()));
		} catch (Exception e) {
			logger.error(e);
		}	
    }
    
    /**
     * 
     */
    private static void Identify() {
    	OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
    	IdentifyType identify = recollect.identify();
    	
    	logger.info(
    			String.format("RepositoryName: %s\nBaseURL: %s\nProtocolVersion: %s\nAdminEmail: %s\nEarliestDatestamp: %s\n"
    					+ "DeletedRecord: %s\nGranularity: %s\n", identify.getRepositoryName(), identify.getBaseURL(),
    					identify.getProtocolVersion(), identify.getAdminEmail(), identify.getEarliestDatestamp(),
    					identify.getDeletedRecord(), identify.getGranularity()));
    }
    
    /**
     * 
     */
    private static void ListMetadataFormats() {
    	OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
    	recollect.listMetadataFormats().forEachRemaining(metadataFormatType->{    		
    		logger.info(String.format("metadataPrefix: %s\nschema: %s\nmetadataNamespace: %s\n", 
    				metadataFormatType.getMetadataPrefix(), metadataFormatType.getSchema(), metadataFormatType.getMetadataNamespace()));
    	});
    }

    /**
     * @throws Exception 
     * 
     */
	private static void ListSets() throws Exception {
		OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
    	recollect.listSets().forEachRemaining(setType->{
    		logger.info(String.format("setSpec: %s\nsetName: %ssetDescription: %s\n", 
    				setType.getSetSpec(), setType.getSetName(), setType.getSetDescription()));
    	});
	}
    
	/**
	 * 
	 * @throws Exception
	 */
	private static void GetRecord() throws Exception {
		//Check parameters
		if(Objects.isNull(identifier)) throw new Exception("identifier must not be null");
		if(Objects.isNull(metadataPrefix)) throw new Exception("metadataPrefix must not be null");
		if(Objects.isNull(edmType)) throw new Exception(String.format("select valid edmType: %s", "TEXT, VIDEO, IMAGE, SOUND, 3D"));
		  		
		if(Objects.isNull(provider)) throw new Exception("provider must not be null");
		
		if(!Arrays.asList("TEXT", "VIDEO", "IMAGE", "SOUND", "3D").contains(edmType))
			throw new Exception(String.format("select valid edmType: %s", 
    				"TEXT, VIDEO, IMAGE, SOUND, 3D"));
		
		Map<String,String> xsltProperties = new HashMap<String,String>();
		xsltProperties.put("edmType", edmType);
		xsltProperties.put("dataProvider", provider);
		
		OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
		GetRecordParameters getRecordParameters = new GetRecordParameters();
		getRecordParameters.withIdentifier(identifier);
		getRecordParameters.withMetadataFormatPrefix(metadataPrefix);
		
		
		if(Objects.nonNull(out))	pathWithSetSpec = Files.createDirectories(Paths.get(out));
		
		RecordType record = recollect.getRecord(getRecordParameters);
		
		try {
       		DownloadListRecordData downloadData;
       		if(Objects.nonNull(pathWithSetSpec)) {
       			if(Objects.nonNull(xslt)) 	downloadData = new DownloadListRecordData(pathWithSetSpec, xslt, xsltProperties);
       			else	downloadData = new DownloadListRecordData(pathWithSetSpec);
       		}
       		else {
       			if(Objects.nonNull(xslt))	downloadData = new DownloadListRecordData(xslt, xsltProperties);
       			else downloadData = new DownloadListRecordData();       			
       		}
			downloadData.executeWithNode(record);
	    		
	   		logger.info(String.format("[HOST] %s [Identifier] %s [MESSAGE] %s",
	   				oaiClient.getURL(), getRecordParameters.getIdentifier(), downloadData.getStatus()));
		} catch (Exception e) {
			logger.error(e);
		}		
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	private static void ListIdentifiers() throws Exception {
		OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
    	//Check parameters
    	if(Objects.isNull(metadataPrefix)) throw new Exception("metadataPrefix must not be null");
    	
    	ListIdentifiersParameters listIdentifiersParameters = new ListIdentifiersParameters();
    	listIdentifiersParameters.withMetadataPrefix(metadataPrefix);
    	
    	Iterator<HeaderType> record = recollect.listIdentifiers(listIdentifiersParameters);
    	
    	try {
    		record.forEachRemaining(headerType->{
    			logger.info(
    				String.format("Identifier: %s\ndatestamp: %s\nSetSpec: %s\nStatus: %s\n",
    					headerType.getIdentifier(), headerType.getDatestamp(), headerType.getSetSpec(), headerType.getStatus()));
    		});
    	}catch(Exception e) {
    		logger.error(e);
    	}
    	
	}
	
    /**
     * 
     * @param inici
     * @return
     */
    public static String duration(Instant inici) {
        long diff = Duration.between(inici, Instant.now()).getSeconds();
        return String.format("%02d:%02d:%02d", diff / 3600, diff % 3600 / 60, diff % 60);
    }
}
