/**
 * 
 */
package org.Recollect.Core.handler;

import nl.mindbus.a2a.A2AType;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.ListMetadataParameters;
import org.Recollect.Core.parameters.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.InputStream;
import java.util.List;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListMetadataFormats;

public class ListMetadataFormatsHandler {
   
	private static Logger logger = LogManager.getLogger(ListMetadataFormatsHandler.class);
	
	private OAIClient client;
    private Parameters parameters = parameters();

    public ListMetadataFormatsHandler(OAIClient client) {
        this.client = client;
    }


    public List<MetadataFormatType> handle(ListMetadataParameters listMetadataParameters) throws Exception {
        InputStream stream = client.execute(parameters
                    .withVerb(ListMetadataFormats)
                    .include(listMetadataParameters));

        logger.info("[{}]	{}", ListMetadataFormats, parameters.toUrl(client.getURL()));

        OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();

        return oai.getListMetadataFormats().getMetadataFormat();
    }
}
