/**
 * 
 */
package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListMetadataFormats;

import java.io.InputStream;
import java.util.List;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.deserialize.JaxbUnmarshal;
import org.Recollect.Core.parameters.ListMetadataParameters;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.OAIPMHtype;

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
            
            OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, OAIPMHtype.class).getObject();
            
            return oai.getListMetadataFormats().getMetadataFormat();
        } catch (Exception e) {        	
        	logger.error(e);
            return null;           
        }finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
