package org.Recollect.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.ListRecordsParameters;
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
@SuppressWarnings("unused")
public class Main {
	
	private static Logger logger = LogManager.getLogger(Main.class);
	
	private static Instant inici = Instant.now();
	
	private static String echoesFolder = "/tmp/echoes";
	
	private static String host = null;
	private static String verb = null;
	private static String identifier = null;	
	private static String metadataPrefix = null;
	private static String from = null;
	private static String until = null;	
	private static String set = null;
	private static String resumptionToken = null;
	
	private static String edmType = null;
	private static String provider = null;
	
		
	
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
    			if(args[i].equals("--resumptionToken"))	resumptionToken = args[i+1];
    			
    			if(args[i].equals("--xslt"))	GlobalAttributes.XSLT = args[i+1];
    			if(args[i].equals("--out"))	echoesFolder = args[i+1];
    			
    			if(args[i].equals("--edmType"))	edmType = args[i+1];
    			if(args[i].equals("--provider"))	provider = args[i+1];
    			
    		}
    		
    		Objects.requireNonNull(host, "host must not be null");
    		Objects.requireNonNull(verb, 
    				String.format("select valid verb: %s", 
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
		Objects.requireNonNull(metadataPrefix, "metadataPrefix must not be null");
		Objects.requireNonNull(set, "set must not be null");
		Objects.requireNonNull(edmType, 
				String.format("select valid edmType: %s", 
						"TEXT, VIDEO, IMAGE, SOUND, 3D"));    		
		Objects.requireNonNull(provider, "provider must not be null");
		Objects.requireNonNull(GlobalAttributes.XSLT, "xslt must not be null");    		   
		if(!Arrays.asList("TEXT", "VIDEO", "IMAGE", "SOUND", "3D").contains(edmType))
			throw new Exception(String.format("select valid edmType: %s", 
    				"TEXT, VIDEO, IMAGE, SOUND, 3D"));
		
		OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
		ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
    	listRecordsParameters.withMetadataPrefix(metadataPrefix);
        listRecordsParameters.withSetSpec(set);
        //listRecordsParameters.withFrom(new Date());
        
        GlobalAttributes.echoesPathWithSetSpec = 
        		Files.createDirectories(Paths.get(echoesFolder + File.separator + listRecordsParameters.getSetSpec()));
        
        Iterator<RecordType> records = recollect.listRecords(listRecordsParameters);
    	
       	try {        						
       		DownloadListRecordData downloadData = new DownloadListRecordData(GlobalAttributes.echoesPathWithSetSpec);
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
		Objects.requireNonNull(identifier, "identifier must not be null");
		Objects.requireNonNull(metadataPrefix, "metadataPrefix must not be null");        		
		Objects.requireNonNull(edmType, 
				String.format("select valid edmType: %s", 
						"TEXT, VIDEO, IMAGE, SOUND, 3D"));    		
		Objects.requireNonNull(provider, "provider must not be null");
		Objects.requireNonNull(GlobalAttributes.XSLT, "xslt must not be null");    		   
		if(!Arrays.asList("TEXT", "VIDEO", "IMAGE", "SOUND", "3D").contains(edmType))
			throw new Exception(String.format("select valid edmType: %s", 
    				"TEXT, VIDEO, IMAGE, SOUND, 3D"));
		
		OAIClient oaiClient = new HttpOAIClient(host);         	
    	Recollect recollect = new Recollect(oaiClient);
    	
		GetRecordParameters getRecordParameters = new GetRecordParameters();
		getRecordParameters.withIdentifier(identifier);
		getRecordParameters.withMetadataFormatPrefix(metadataPrefix);
		
		GlobalAttributes.echoesPathWithSetSpec = 
        		Files.createDirectories(Paths.get(echoesFolder + File.separator + set));
		
		RecordType record = recollect.getRecord(getRecordParameters);
		
		try { 
			DownloadListRecordData downloadData = new DownloadListRecordData(GlobalAttributes.echoesPathWithSetSpec);
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
    	Objects.requireNonNull(metadataPrefix, "metadataPrefix must not be null");
    	
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
