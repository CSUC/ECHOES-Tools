package org.Recollect.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.download.Download;
import org.Recollect.Core.download.FactoryDownload;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.util.Granularity;
import org.Recollect.Core.util.TimeUtils;
import org.Recollect.Core.util.UTCDateProvider;
import org.Recollect.Core.util.Verb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.SetType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;

/**
 * 
 * @author amartinez
 *
 * 
 *
 */
public class Main {

	private static Logger logger = LogManager.getLogger(Main.class);

	private static Instant inici = Instant.now();

	private static String host;
	private static String verb;
	private static String identifier;
	private static String metadataPrefix;
	private static String from;
	private static String until;
	private static String set;
	private static String granularity;
	private static String resumptionToken;

	private static String out = null;

	private static String edmType;
	private static String provider;
	private static String rights;
	private static String language = "und";
	private static String dataProvider;

	private static Path pathWithSetSpec;
	private static String xslt;

	private static UTCDateProvider dateProvider = new UTCDateProvider();

	private static Map<String, String> properties;

    private static AtomicInteger emittedTotal = new AtomicInteger(0);
    private static AtomicInteger receivedTotal = new AtomicInteger(0);

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (Objects.nonNull(args) && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("--host"))
					host = args[i + 1];
				if (args[i].equals("--verb"))
					verb = args[i + 1];
				if (args[i].equals("--identifier"))
					identifier = args[i + 1];
				if (args[i].equals("--metadataPrefix"))
					metadataPrefix = args[i + 1];
				if (args[i].equals("--from"))
					from = args[i + 1];
				if (args[i].equals("--until"))
					until = args[i + 1];
				if (args[i].equals("--set"))
					set = args[i + 1];
				if (args[i].equals("--granularity"))
					granularity = args[i + 1];
				if (args[i].equals("--resumptionToken"))
					resumptionToken = args[i + 1];
				if (args[i].equals("--dataProvider"))
					dataProvider = args[i + 1];
				if (args[i].equals("--rights"))
					rights = args[i + 1];

				if (args[i].equals("--xslt"))
					xslt = args[i + 1];
				if (args[i].equals("--out"))
					out = args[i + 1];

				if (args[i].equals("--edmType"))
					edmType = args[i + 1];
				if (args[i].equals("--provider"))
					provider = args[i + 1];

				if (args[i].equals("--language"))
					language = args[i + 1];

			}

			if (Objects.isNull(host))
				throw new Exception("host must not be null");

			if (Objects.isNull(verb))
				throw new Exception(String.format("select valid verb: %s",
						"Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords"));

			if (verb.equals(Verb.Type.ListRecords.toString()))
				ListRecords();
			else if (verb.equals(Verb.Type.Identify.toString()))
				Identify();
			else if (verb.equals(Verb.Type.ListMetadataFormats.toString()))
				ListMetadataFormats();
			else if (verb.equals(Verb.Type.ListSets.toString()))
				ListSets();
			else if (verb.equals(Verb.Type.GetRecord.toString()))
				GetRecord();
			else if (verb.equals(Verb.Type.ListIdentifiers.toString()))
				ListIdentifiers();
			else {
				logger.error(String.format("select valid verb: %s",
						"Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords"));
			}
			logger.info(String.format("End Recollect %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
		} else
			logger.error(
					"--host [host] --verb [Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords]");
	}

	/**
	 * @throws Exception
	 * 
	 */
	private static void ListRecords() throws Exception {
		properties = new HashMap<>();
		properties.put("edmType", edmType);
		properties.put("provider", provider);
		properties.put("dataProvider", dataProvider);
		properties.put("language", language);
		properties.put("rights", rights);

		if (Objects.isNull(set)) {
			ListSets().forEachRemaining((SetType setType) -> downloadRecords(setType.getSetSpec()));
		} else {
			downloadRecords(set);
		}
	}

	/**
	 * 
	 */
	private static void Identify() {
        OAIClient oaiClient = new HttpOAIClient(host);
        Recollect recollect = new Recollect(oaiClient);

		IdentifyType identify = recollect.identify();

		logger.info(String.format(
				"RepositoryName: %s\nBaseURL: %s\nProtocolVersion: %s\nAdminEmail: %s\nEarliestDatestamp: %s\n"
						+ "DeletedRecord: %s\nGranularity: %s\n",
				identify.getRepositoryName(), identify.getBaseURL(), identify.getProtocolVersion(),
				identify.getAdminEmail(), identify.getEarliestDatestamp(), identify.getDeletedRecord(),
				identify.getGranularity()));
	}

	/**
	 * 
	 */
	private static void ListMetadataFormats() {
		OAIClient oaiClient = new HttpOAIClient(host);
		Recollect recollect = new Recollect(oaiClient);

		recollect.listMetadataFormats().forEachRemaining(metadataFormatType ->
				logger.info(String.format("metadataPrefix: %s\nschema: %s\nmetadataNamespace: %s\n",
                metadataFormatType.getMetadataPrefix(), metadataFormatType.getSchema(),
                metadataFormatType.getMetadataNamespace())));
	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 */
	private static Iterator<SetType> ListSets() throws Exception {
        OAIClient oaiClient = new HttpOAIClient(host);
		Recollect recollect = new Recollect(oaiClient);

		Iterator<SetType> sets = recollect.listSets();
		//sets.forEachRemaining(s->System.out.println(s.getSetSpec()));
		
		return sets;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private static void GetRecord() throws Exception {
	    Instant timeRecord = Instant.now();

		properties = new HashMap<>();
		properties.put("edmType", edmType);
		properties.put("provider", provider);
		properties.put("dataProvider", dataProvider);
		properties.put("language", language);
		properties.put("rights", rights);

		try{
            OAIClient oaiClient = new HttpOAIClient(host);
            Recollect recollect = new Recollect(oaiClient);

            GetRecordParameters getRecordParameters = new GetRecordParameters();
            getRecordParameters.withIdentifier(identifier);
            getRecordParameters.withMetadataFormatPrefix(metadataPrefix);

            if (Objects.nonNull(out))
                pathWithSetSpec = Files.createDirectories(Paths.get(out));

            //FactoryDownload.createDownloadRecordType(recollect.getRecord(getRecordParameters, new Class[] { OAIPMHtype.class, A2AType.class, OaiDcType.class }),xslt);

            Observable<Download> observable;

            if(Objects.isNull(xslt)) {
                observable = FactoryDownload.createDownloadRecordType(recollect.getRecord(getRecordParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}),xslt);

                observable
                        .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                        .observeOn(Schedulers.io())
                        .subscribe(
                                (Download l) ->{
                                    logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                    l.execute(pathWithSetSpec, properties );
                                } ,
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", getRecordParameters.getIdentifier(), TimeUtils.duration(timeRecord, DateTimeFormatter.ISO_TIME)))
                        );
                Thread.sleep(3000);
            }
        }catch(Exception e) {
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

		// Check parameters
		if (Objects.isNull(metadataPrefix))
			throw new Exception("metadataPrefix must not be null");

		ListIdentifiersParameters listIdentifiersParameters = new ListIdentifiersParameters();
		listIdentifiersParameters.withMetadataPrefix(metadataPrefix);

		Iterator<HeaderType> record = recollect.listIdentifiers(listIdentifiersParameters);

		try {
			record.forEachRemaining((HeaderType headerType) ->
                    logger.info(String.format("Identifier: %s\ndatestamp: %s\nSetSpec: %s\nStatus: %s\n",
                        headerType.getIdentifier(), headerType.getDatestamp(), headerType.getSetSpec(),
                        headerType.getStatus())));
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * 
	 * @param s
	 */
	private static void downloadRecords(String s) {
		Instant timeSet = Instant.now();
	    logger.info(String.format("set: %s", s));
	    properties.put("set", s);

        OAIClient oaiClient = new HttpOAIClient(host);
        Recollect recollect = new Recollect(oaiClient);

		try {
			ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
			listRecordsParameters.withMetadataPrefix(metadataPrefix);
			listRecordsParameters.withSetSpec(s);

			if (Objects.nonNull(resumptionToken))
				listRecordsParameters.withgetResumptionToken(resumptionToken);

			if (Objects.nonNull(from)) {
				if (Objects.nonNull(granularity))
					listRecordsParameters
							.withFrom(dateProvider.parse(from, Granularity.fromRepresentation(granularity)));
				else
					listRecordsParameters.withFrom(dateProvider.parse(from));
			}
			if (Objects.nonNull(granularity))
				listRecordsParameters.withGranularity(granularity);
			if (Objects.nonNull(until)) {
				if (Objects.nonNull(granularity))
					listRecordsParameters
							.withUntil(dateProvider.parse(until, Granularity.fromRepresentation(granularity)));
				else
					listRecordsParameters.withFrom(dateProvider.parse(until));
			}

			if (Objects.nonNull(out))
                pathWithSetSpec = Files.createDirectories(Paths.get(out + File.separator + listRecordsParameters.getSetSpec()));

            Observable<Download> observable;

            if(Objects.isNull(xslt)) {
				observable = FactoryDownload.createDownloadIterator(recollect.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}),xslt);

                observable
                        .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                        .observeOn(Schedulers.io())
                        .subscribe(
                                (Download l) ->{
                                    logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                    l.execute(pathWithSetSpec, properties );
                                } ,
                                e -> logger.error("Error: " + e),
                                () -> logger.info(String.format("Completed %s: %s", s, TimeUtils.duration(timeSet, DateTimeFormatter.ISO_TIME)))
                        );
                Thread.sleep(3000);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		//Garbage.gc();
	}

}
