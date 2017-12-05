/**
 * 
 */
package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListSets;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.util.Source;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.SetType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;


public class ListSetsHandler implements Source<SetType> {
   
	private static Logger logger = LogManager.getLogger(ListSetsHandler.class);
	
	private OAIClient client;
    private String resumptionToken;
    private boolean ended = false;

    public ListSetsHandler(OAIClient client) {
        this.client = client;
    }

    @Override
    public List<SetType> nextIteration() {
        InputStream stream = null;
        try {
            if (resumptionToken == null) { // First call
                stream = client.execute(parameters()
                        .withVerb(ListSets));
            } else { // Resumption calls
                stream = client.execute(parameters()
                        .withVerb(ListSets)
                        .withResumptionToken(resumptionToken));
            }
            OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}).getObject();
           
            if(Objects.nonNull(oai.getListSets().getResumptionToken())){
				 if(!oai.getListSets().getResumptionToken().getValue().isEmpty()) {
					 resumptionToken = oai.getListSets().getResumptionToken().getValue();
				 }else ended = true;
				 
			 }else ended = true;
            
            stream.close();
            return  oai.getListSets().getSet();
        } catch (Exception e) {        	
        	logger.error(e);
            return null;           
        }finally {
            IOUtils.closeQuietly(stream);
        }
    }
    
    @Override
    public boolean endReached() {
        return ended;
    }
}
