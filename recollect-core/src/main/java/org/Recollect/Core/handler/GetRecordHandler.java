package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.GetRecord;

import java.io.InputStream;
import java.util.Objects;

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

	public RecordType handle(GetRecordParameters parameters) throws Exception {
		InputStream stream = client.execute(parameters().withVerb(GetRecord).include(parameters));

		JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(stream, classType);

		if(!jaxbUnmarshal.getValidationEvent().getEventError().isEmpty())	throw new Exception(jaxbUnmarshal.getValidationEvent().getEventError().toString());

		OAIPMHtype oai = (OAIPMHtype) jaxbUnmarshal.getObject();

		return oai.getGetRecord().getRecord();
	}
}
