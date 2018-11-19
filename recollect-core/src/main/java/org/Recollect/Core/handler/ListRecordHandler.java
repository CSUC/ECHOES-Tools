/**
 *
 */
package org.Recollect.Core.handler;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.parameters.Parameters;
import org.Recollect.Core.util.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListRecords;

/**
 * @author amartinez
 *
 */
public class ListRecordHandler implements Source<RecordType> {

	private static Logger logger = LogManager.getLogger(ListRecordHandler.class);

	private OAIClient client;
	private Parameters parameters = parameters();
	private String resumptionToken;
	private boolean ended = false;

	private Class<?>[] classType;

	public ListRecordHandler(OAIClient client, ListRecordsParameters listRecordsParameters, Class<?>[] classType) {
		this.client = client;
		this.parameters.withVerb(ListRecords).include(listRecordsParameters);
		this.resumptionToken = listRecordsParameters.getResumptionToken();
		this.classType = classType;
	}

	@Override
	public List<RecordType> nextIteration() throws Exception {
		InputStream stream = null;
		if (resumptionToken == null) { // First call
			stream = client.execute(parameters);
		} else {
			stream = client.execute(parameters.withResumptionToken(resumptionToken));
		}

		logger.info("[{}]	{}", ListRecords, parameters.toUrl(client.getURL()));

		OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, classType).getObject();

		if (!oai.getError().isEmpty()) {
			logger.error(oai.getError().stream().map(m-> String.format("[HOST] %s [SET] %s [CODE] %s [VALUE] %s", client.getURL(),
					parameters.getSet(), m.getCode(), m.getValue())).collect(Collectors.joining(",")));
		}

		if (Objects.nonNull(oai.getListRecords().getResumptionToken())) {
			if (!oai.getListRecords().getResumptionToken().getValue().isEmpty()) {
				resumptionToken = oai.getListRecords().getResumptionToken().getValue();
			} else
				ended = true;
		} else
			ended = true;

		return oai.getListRecords().getRecord();
	}

	@Override
	public boolean endReached() {
		return ended;
	}

}
