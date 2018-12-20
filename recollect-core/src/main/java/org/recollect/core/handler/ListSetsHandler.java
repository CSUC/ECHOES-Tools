/**
 * 
 */
package org.recollect.core.handler;

import nl.mindbus.a2a.A2AType;
import org.recollect.core.client.OAIClient;
import org.recollect.core.parameters.Parameters;
import org.recollect.core.util.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.SetType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.recollect.core.util.Verb;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;


public class ListSetsHandler implements Source<SetType> {
   
	private static Logger logger = LogManager.getLogger(ListSetsHandler.class);
	
	private OAIClient client;
    private Parameters parameters = Parameters.parameters();

    private String resumptionToken;
    private boolean ended = false;

    public ListSetsHandler(OAIClient client) {
        this.client = client;
    }

    @Override
    public List<SetType> nextIteration() throws Exception {
        InputStream stream = null;
        if (resumptionToken == null) { // First call
            stream = client.execute(parameters
                    .withVerb(Verb.Type.ListSets));
        } else { // Resumption calls
            stream = client.execute(parameters
                    .withVerb(Verb.Type.ListSets)
                    .withResumptionToken(resumptionToken));
        }

        logger.info("[{}]	{}", Verb.Type.ListSets, parameters.toUrl(client.getURL()));

        OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();

        if (Objects.nonNull(oai.getListSets().getResumptionToken())) {
            if (!oai.getListSets().getResumptionToken().getValue().isEmpty()) {
                resumptionToken = oai.getListSets().getResumptionToken().getValue();
            } else ended = true;

        } else ended = true;

        return oai.getListSets().getSet();
    }
    
    @Override
    public boolean endReached() {
        return ended;
    }
}
