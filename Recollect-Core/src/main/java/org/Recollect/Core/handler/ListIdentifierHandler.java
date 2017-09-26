package org.Recollect.Core.handler;

import static org.Recollect.Core.parameters.Parameters.parameters;
import static org.Recollect.Core.util.Verb.Type.ListIdentifiers;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.deserialize.JaxbUnmarshal;
import org.Recollect.Core.util.Source;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.OAIPMHtype;


public class ListIdentifierHandler implements Source<HeaderType> { 
	
	private static Logger logger = LogManager.getLogger(ListIdentifierHandler.class);
	
    private ListIdentifiersParameters parameters;
    private OAIClient client;
    private String resumptionToken;
    private boolean ended = false;

    public ListIdentifierHandler(OAIClient client, ListIdentifiersParameters parameters) {
        this.parameters = parameters;
        this.client = client;
    }

    @Override
    public List<HeaderType> nextIteration() {
    	InputStream stream = null;	
		 try {			 
			 if (resumptionToken == null) { // First call
				 stream = client.execute(parameters()
							 .withVerb(ListIdentifiers)
							 .include(parameters));
			 }else {
				 stream = client.execute(parameters()
	                        .withVerb(ListIdentifiers)
	                        .include(parameters)
	                        .withResumptionToken(resumptionToken));
			 }
			 
			 OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(stream, OAIPMHtype.class).getObject();
			 
			 if(Objects.nonNull(oai.getListIdentifiers().getResumptionToken())){
				 if(!oai.getListIdentifiers().getResumptionToken().getValue().isEmpty()) {
					 resumptionToken = oai.getListIdentifiers().getResumptionToken().getValue();
				 }else ended = true;				 
			 }else ended = true;
			 
			 stream.close();
			 return oai.getListIdentifiers().getHeader();
		 }catch(Exception e) {
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
