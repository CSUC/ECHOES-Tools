package org.Recollect.Core.handler;

import nl.mindbus.a2a.A2AType;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.Parameters;
import org.Recollect.Core.util.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListIdentifiers;


public class ListIdentifierHandler implements Source<HeaderType> {

	private static Logger logger = LogManager.getLogger(ListIdentifierHandler.class);

    private ListIdentifiersParameters listIdentifiersParameters;
    private OAIClient client;
	private Parameters parameters = parameters();
    private String resumptionToken;
    private boolean ended = false;

    public ListIdentifierHandler(OAIClient client, ListIdentifiersParameters listIdentifiersParameters) {
        this.listIdentifiersParameters = listIdentifiersParameters;
        this.client = client;
    }

    @Override
    public List<HeaderType> nextIteration() throws Exception {
    	InputStream stream = null;
		if (resumptionToken == null) { // First call
			stream = client.execute(parameters
					.withVerb(ListIdentifiers)
					.include(listIdentifiersParameters));
		} else {
			stream = client.execute(parameters
					.withVerb(ListIdentifiers)
					.include(listIdentifiersParameters)
					.withResumptionToken(resumptionToken));
		}

		logger.info("[{}]	{}", ListIdentifiers, parameters.toUrl(client.getURL()));

		OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();

		if (Objects.nonNull(oai.getListIdentifiers().getResumptionToken())) {
			if (!oai.getListIdentifiers().getResumptionToken().getValue().isEmpty()) {
				resumptionToken = oai.getListIdentifiers().getResumptionToken().getValue();
			} else ended = true;
		} else ended = true;

		return oai.getListIdentifiers().getHeader();

    }

    @Override
    public boolean endReached() {
        return ended;
    }
}
