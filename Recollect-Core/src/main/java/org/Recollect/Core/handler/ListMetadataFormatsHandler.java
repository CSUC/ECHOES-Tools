/**
 * 
 */
package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListMetadataFormats;

import java.io.InputStream;
import java.util.List;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.parameters.ListMetadataParameters;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;

public class ListMetadataFormatsHandler {
   
	private static Logger logger = LogManager.getLogger(ListMetadataFormatsHandler.class);
	
	private OAIClient client;

    public ListMetadataFormatsHandler(OAIClient client) {
        this.client = client;
    }


    public List<MetadataFormatType> handle(ListMetadataParameters parameters) {
        InputStream stream = null;
        try {
            stream = client.execute(parameters()
                    .withVerb(ListMetadataFormats)
                    .include(parameters));
            
            OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();
            
            return oai.getListMetadataFormats().getMetadataFormat();
        } catch (Exception e) {        	
        	logger.error(e);
            return null;           
        }finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
