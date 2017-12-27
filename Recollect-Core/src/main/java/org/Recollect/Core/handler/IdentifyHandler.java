/**
 * 
 */
package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.Identify;

import java.io.InputStream;

import org.Recollect.Core.client.OAIClient;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;

public class IdentifyHandler {
	
	private static Logger logger = LogManager.getLogger(IdentifyHandler.class);
	
    private final OAIClient client;

    public IdentifyHandler (OAIClient client) {
        this.client = client;
    }

    public IdentifyType handle () {
        InputStream stream = null;
        try {
            stream = client.execute(parameters()
                    .withVerb(Identify));
            
            OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();
            
            stream.close();
            return oai.getIdentify();
        }catch (Exception e) {        	
        	logger.error(e);
            return null;           
        }finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
