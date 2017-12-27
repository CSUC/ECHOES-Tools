/**
 * 
 */
package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.util.Source;
import org.Recollect.Core.util.Verb.Type;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;

/**
 * @author amartinez
 *
 */
public class ListRecordHandler implements Source<RecordType> {

	private static Logger logger = LogManager.getLogger(ListRecordHandler.class);

	private ListRecordsParameters parameters;
	private OAIClient client;
	private String resumptionToken;
	private boolean ended = false;

	private Class<?>[] classType;

	public ListRecordHandler(OAIClient client, ListRecordsParameters parameters, Class<?>[] classType) {
		this.client = client;
		this.parameters = parameters;
		this.resumptionToken = parameters.getResumptionToken();

		this.classType = classType;
	}

	@Override
	public List<RecordType> nextIteration() {
		InputStream stream = null;
		try {
			if (resumptionToken == null) { // First call
				stream = client.execute(parameters().withVerb(Type.ListRecords).include(parameters));
			} else {
				stream = client.execute(parameters().withVerb(Type.ListRecords).include(parameters)
						.withResumptionToken(resumptionToken));
			}

			OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, classType).getObject();

			if (!oai.getError().isEmpty()) {
				oai.getError().stream().forEach(errorType -> {
					logger.info(String.format("[HOST] %s [SET] %s [CODE] %s [VALUE] %s", client.getURL(),
							parameters.getSetSpec(), errorType.getCode(), errorType.getValue()));
				});
			}

			if (Objects.nonNull(oai.getListRecords().getResumptionToken())) {
				if (!oai.getListRecords().getResumptionToken().getValue().isEmpty()) {
					resumptionToken = oai.getListRecords().getResumptionToken().getValue();
				} else
					ended = true;

			} else
				ended = true;
			stream.close();
			return oai.getListRecords().getRecord();
		} catch (Exception e) {
			ended = true;
			return null;
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	@Override
	public boolean endReached() {
		return ended;
	}

}
