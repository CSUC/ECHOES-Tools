package org.transformation.handler;

import org.transformation.client.OAIClient;
import org.transformation.parameters.GetRecordParameters;
import org.transformation.parameters.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;
import org.transformation.util.Verb;

import java.io.InputStream;

public class GetRecordHandler {

	private static Logger logger = LogManager.getLogger(GetRecordHandler.class);

	private final OAIClient client;
	private Parameters parameters = Parameters.parameters();

	private Class<?>[] classType;

	public GetRecordHandler(OAIClient client, Class<?>[] classType) {
		this.client = client;
		this.classType = classType;
	}

	public RecordType handle(GetRecordParameters getRecordParameters) throws Exception {
		InputStream stream = client.execute(parameters.withVerb(Verb.Type.GetRecord).include(getRecordParameters));

		logger.info("[{}]	{}", Verb.Type.GetRecord, parameters.toUrl(client.getURL()));

		JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(stream, classType);

		if(!jaxbUnmarshal.getValidationEvent().getEventError().isEmpty())	throw new Exception(jaxbUnmarshal.getValidationEvent().getEventError().toString());

		OAIPMHtype oai = (OAIPMHtype) jaxbUnmarshal.getObject();

		return oai.getGetRecord().getRecord();
	}
}
