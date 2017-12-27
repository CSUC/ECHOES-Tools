/**
 * 
 */
package org.Recollect.Core;

import java.util.Iterator;

import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.util.ItemIterator;
import org.Recollect.Core.handler.GetRecordHandler;
import org.Recollect.Core.handler.IdentifyHandler;
import org.Recollect.Core.handler.ListIdentifierHandler;
import org.Recollect.Core.handler.ListMetadataFormatsHandler;
import org.Recollect.Core.handler.ListRecordHandler;
import org.Recollect.Core.handler.ListSetsHandler;
import org.Recollect.Core.parameters.GetRecordParameters;
import org.Recollect.Core.parameters.ListIdentifiersParameters;
import org.Recollect.Core.parameters.ListMetadataParameters;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.openarchives.oai._2.HeaderType;
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.SetType;

/**
 * @author amartinez
 *
 */
public class Recollect {

	//private Logger logger = LogManager.getLogger(Recollect.class);
	
	private OAIClient client;
	private IdentifyHandler identifyHandler;
	private ListMetadataFormatsHandler listMetadataFormatsHandler;
	
	public Recollect(OAIClient oaiClient) {
		this.client = oaiClient;
		this.identifyHandler = new IdentifyHandler(oaiClient);
		this.listMetadataFormatsHandler = new ListMetadataFormatsHandler(oaiClient);
	}
	
	
	public IdentifyType identify () {
        return identifyHandler.handle();
    }

    public Iterator<MetadataFormatType> listMetadataFormats (){
        return listMetadataFormatsHandler.handle(ListMetadataParameters.request()).iterator();
    }

    public Iterator<MetadataFormatType> listMetadataFormats (ListMetadataParameters parameters) {
        return listMetadataFormatsHandler.handle(parameters).iterator();
    }

    public RecordType getRecord (GetRecordParameters parameters, Class<?>[] classType) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("GetRecord verb requires identifier and metadataPrefix parameters");
        return new GetRecordHandler(client, classType).handle(parameters);
    }

    public Iterator<RecordType> listRecords (ListRecordsParameters parameters, Class<?>[] classType) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListRecords verb requires the metadataPrefix");
        return new ItemIterator<RecordType>(new ListRecordHandler(client, parameters, classType));
    }

    public Iterator<HeaderType> listIdentifiers (ListIdentifiersParameters parameters) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListIdentifiers verb requires the metadataPrefix");
        return new ItemIterator<HeaderType>(new ListIdentifierHandler(client, parameters));
    }

    public Iterator<SetType> listSets () throws Exception {
        try {
            return new ItemIterator<SetType>(new ListSetsHandler(client));
        } catch (Exception ex) {
        	throw new Exception(ex);
        }
    }
}
