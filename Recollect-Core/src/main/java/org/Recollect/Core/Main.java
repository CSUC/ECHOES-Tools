package org.Recollect.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.download.DownloadJaxb;
import org.Recollect.Core.download.DownloadNode;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.util.Garbage;
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

	private static UTCDateProvider dateProvider = new UTCDateProvider();;

	private static OAIClient oaiClient;
	private static Recollect recollect;

	private static Map<String, String> properties;

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
			logger.info(String.format("End %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
		} else
			logger.error(
					"--host [host] --verb [Identify, ListMetadataFormats, ListSets, GetRecord, ListIdentifiers, ListRecords]");
	}

	/**
	 * @throws Exception
	 * 
	 */
	private static void ListRecords() throws Exception {
		// Check parameters
		if (Objects.isNull(metadataPrefix))
			throw new Exception("metadataPrefix must not be null");

		oaiClient = new HttpOAIClient(host);
		recollect = new Recollect(oaiClient);

		properties = new HashMap<String, String>();
		properties.put("edmType", edmType);
		properties.put("provider", provider);
		properties.put("dataProvider", dataProvider);
		properties.put("language", language);
		properties.put("rights", rights);

		if (Objects.isNull(set)) {
			ListSets().forEachRemaining(setType -> {
				downloadListRecords(setType.getSetSpec());
			});
		} else {
			downloadListRecords(set);
		}
	}

	/**
	 * 
	 */
	private static void Identify() {
		oaiClient = new HttpOAIClient(host);
		recollect = new Recollect(oaiClient);

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
		oaiClient = new HttpOAIClient(host);
		recollect = new Recollect(oaiClient);

		recollect.listMetadataFormats().forEachRemaining(metadataFormatType -> {
			logger.info(String.format("metadataPrefix: %s\nschema: %s\nmetadataNamespace: %s\n",
					metadataFormatType.getMetadataPrefix(), metadataFormatType.getSchema(),
					metadataFormatType.getMetadataNamespace()));
		});
	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 */
	private static Iterator<SetType> ListSets() throws Exception {
		oaiClient = new HttpOAIClient(host);
		recollect = new Recollect(oaiClient);

		Iterator<SetType> sets = recollect.listSets();
		sets.forEachRemaining(s->System.out.println(s.getSetSpec()));
		
		return sets;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private static void GetRecord() throws Exception {
		// Check parameters
		if (Objects.isNull(identifier))
			throw new Exception("identifier must not be null");
		if (Objects.isNull(metadataPrefix))
			throw new Exception("metadataPrefix must not be null");
		if (Objects.isNull(edmType))
			throw new Exception(String.format("select valid edmType: %s", "TEXT, VIDEO, IMAGE, SOUND, 3D"));

		if (Objects.isNull(provider))
			throw new Exception("provider must not be null");

		if (!Arrays.asList("TEXT", "VIDEO", "IMAGE", "SOUND", "3D").contains(edmType))
			throw new Exception(String.format("select valid edmType: %s", "TEXT, VIDEO, IMAGE, SOUND, 3D"));

		properties = new HashMap<String, String>();
		properties.put("edmType", edmType);
		properties.put("provider", provider);
		properties.put("dataProvider", dataProvider);
		properties.put("language", language);
		properties.put("rights", rights);

		oaiClient = new HttpOAIClient(host);
		recollect = new Recollect(oaiClient);

		GetRecordParameters getRecordParameters = new GetRecordParameters();
		getRecordParameters.withIdentifier(identifier);
		getRecordParameters.withMetadataFormatPrefix(metadataPrefix);

		if (Objects.nonNull(out))
			pathWithSetSpec = Files.createDirectories(Paths.get(out));

		try {
			if (Objects.nonNull(xslt)) {
				DownloadNode downloadData = null;
				if (Objects.nonNull(out))
					downloadData = new DownloadNode(pathWithSetSpec, xslt, properties);
				else
					downloadData = new DownloadNode(xslt, properties);

				downloadData.execute(
						recollect.getRecord(getRecordParameters, new Class[] { OAIPMHtype.class, OaiDcType.class }));

				logger.info(String.format("[HOST] %s [Identifier] %s [MESSAGE] %s", oaiClient.getURL(),
						getRecordParameters.getIdentifier(), downloadData.getStatus()));
			} else {
				DownloadJaxb downloadData = null;

				if (Objects.nonNull(out))
					downloadData = new DownloadJaxb(pathWithSetSpec, new Class[] { OAIPMHtype.class, A2AType.class });
				else
					downloadData = new DownloadJaxb(new Class[] { OAIPMHtype.class, A2AType.class });

				downloadData.execute(
						recollect.getRecord(getRecordParameters, new Class[] { OAIPMHtype.class, OaiDcType.class }),
						properties);

				logger.info(String.format("[HOST] %s [Identifier] %s [MESSAGE] %s", oaiClient.getURL(),
						getRecordParameters.getIdentifier(), downloadData.getStatus()));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	private static void ListIdentifiers() throws Exception {
		oaiClient = new HttpOAIClient(host);
		recollect = new Recollect(oaiClient);

		// Check parameters
		if (Objects.isNull(metadataPrefix))
			throw new Exception("metadataPrefix must not be null");

		ListIdentifiersParameters listIdentifiersParameters = new ListIdentifiersParameters();
		listIdentifiersParameters.withMetadataPrefix(metadataPrefix);

		Iterator<HeaderType> record = recollect.listIdentifiers(listIdentifiersParameters);

		try {
			record.forEachRemaining(headerType -> {
				logger.info(String.format("Identifier: %s\ndatestamp: %s\nSetSpec: %s\nStatus: %s\n",
						headerType.getIdentifier(), headerType.getDatestamp(), headerType.getSetSpec(),
						headerType.getStatus()));
			});
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * 
	 * @param s
	 */
	private static void downloadListRecords(String s) {
		properties.put("set", s);
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

			if (Objects.nonNull(out)) {
				if (Objects.nonNull(s))
					pathWithSetSpec = Files
							.createDirectories(Paths.get(out + File.separator + listRecordsParameters.getSetSpec()));
			}

			if (Objects.nonNull(xslt)) {
				DownloadNode downloadData = null;
				if (Objects.nonNull(out))
					downloadData = new DownloadNode(pathWithSetSpec, xslt, properties);
				else
					downloadData = new DownloadNode(xslt, properties);

				downloadData.execute(recollect.listRecords(listRecordsParameters, new Class[] { OAIPMHtype.class }));

				logger.info(String.format("[HOST] %s [SET] %s [MESSAGE] %s", oaiClient.getURL(),
						listRecordsParameters.getSetSpec(), downloadData.getStatus()));
			} else {
				DownloadJaxb downloadData = null;

				if (Objects.nonNull(out))
					downloadData = new DownloadJaxb(pathWithSetSpec, new Class[] { OAIPMHtype.class, A2AType.class, OaiDcType.class });
				else
					downloadData = new DownloadJaxb(new Class[] { OAIPMHtype.class, A2AType.class, OaiDcType.class });

				downloadData.execute(
						recollect.listRecords(listRecordsParameters, new Class[] { OAIPMHtype.class, A2AType.class, OaiDcType.class }),
						properties);

				logger.info(String.format("[HOST] %s [SET] %s [MESSAGE] %s", oaiClient.getURL(),
						listRecordsParameters.getSetSpec(), downloadData.getStatus()));
			}
		} catch (Exception e) {
			logger.error(e);
		}
		Garbage.gc();
	}
}
