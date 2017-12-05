package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.GetRecord;

import java.io.InputStream;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.OAIPMHtype;

public class GetRecordHandler {

	private static Logger logger = LogManager.getLogger(GetRecordHandler.class);

	private final OAIClient client;

	private Class<?>[] classType;

	public GetRecordHandler(OAIClient client, Class<?>[] classType) {
		this.client = client;
		this.classType = classType;
	}

	public RecordType handle(GetRecordParameters parameters) {
		InputStream stream = null;
		try {
			stream = client.execute(parameters().withVerb(GetRecord).include(parameters));

			OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, classType).getObject();

			stream.close();
			return oai.getGetRecord().getRecord();
		} catch (Exception e) {
			logger.error(e);
			return null;
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
}
