package org.transformation.handler;

import nl.mindbus.a2a.A2AType;
import org.transformation.client.OAIClient;
import org.transformation.parameters.ListIdentifiersParameters;
import org.transformation.parameters.Parameters;
import org.transformation.util.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.util.Verb;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;


public class ListIdentifierHandler implements Source<HeaderType> {

	private static Logger logger = LogManager.getLogger(ListIdentifierHandler.class);

    private ListIdentifiersParameters listIdentifiersParameters;
    private OAIClient client;
	private Parameters parameters = Parameters.parameters();
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
					.withVerb(Verb.Type.ListIdentifiers)
					.include(listIdentifiersParameters));
		} else {
			stream = client.execute(parameters
					.withVerb(Verb.Type.ListIdentifiers)
					.include(listIdentifiersParameters)
					.withResumptionToken(resumptionToken));
		}

		logger.info("[{}]	{}", Verb.Type.ListIdentifiers, parameters.toUrl(client.getURL()));

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
