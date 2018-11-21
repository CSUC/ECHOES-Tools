/**
 * 
 */
package org.Recollect.Core.handler;

import nl.mindbus.a2a.A2AType;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.InputStream;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.Identify;

public class IdentifyHandler {
	
	private static Logger logger = LogManager.getLogger(IdentifyHandler.class);
	
    private final OAIClient client;
    private Parameters parameters = parameters();

    public IdentifyHandler (OAIClient client) {
        this.client = client;
    }

    public IdentifyType handle () throws Exception {
        InputStream stream = client.execute(parameters.withVerb(Identify));

        logger.info("[{}]	{}", Identify, parameters.toUrl(client.getURL()));

        OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();

        return oai.getIdentify();
    }
}
