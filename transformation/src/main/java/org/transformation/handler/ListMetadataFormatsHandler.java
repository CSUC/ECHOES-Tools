/**
 * 
 */
package org.transformation.handler;

import nl.mindbus.a2a.A2AType;
import org.transformation.client.OAIClient;
import org.transformation.parameters.ListMetadataParameters;
import org.transformation.parameters.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.util.Verb;

import java.io.InputStream;
import java.util.List;

public class ListMetadataFormatsHandler {
   
	private static Logger logger = LogManager.getLogger(ListMetadataFormatsHandler.class);
	
	private OAIClient client;
    private Parameters parameters = Parameters.parameters();

    public ListMetadataFormatsHandler(OAIClient client) {
        this.client = client;
    }


    public List<MetadataFormatType> handle(ListMetadataParameters listMetadataParameters) throws Exception {
        InputStream stream = client.execute(parameters
                    .withVerb(Verb.Type.ListMetadataFormats)
                    .include(listMetadataParameters));

        logger.info("[{}]	{}", Verb.Type.ListMetadataFormats, parameters.toUrl(client.getURL()));

        OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();

        return oai.getListMetadataFormats().getMetadataFormat();
    }
}
