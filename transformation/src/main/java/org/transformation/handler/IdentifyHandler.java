/**
 * 
 */
package org.transformation.handler;

import nl.mindbus.a2a.A2AType;
import org.transformation.client.OAIClient;
import org.transformation.parameters.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.util.Verb;

import java.io.InputStream;

public class IdentifyHandler {
	
	private static Logger logger = LogManager.getLogger(IdentifyHandler.class);
	
    private final OAIClient client;
    private Parameters parameters = Parameters.parameters();

    public IdentifyHandler (OAIClient client) {
        this.client = client;
    }

    public IdentifyType handle () throws Exception {
        InputStream stream = client.execute(parameters.withVerb(Verb.Type.Identify));

        logger.info("[{}]	{}", Verb.Type.Identify, parameters.toUrl(client.getURL()));

        OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();

        return oai.getIdentify();
    }
}
