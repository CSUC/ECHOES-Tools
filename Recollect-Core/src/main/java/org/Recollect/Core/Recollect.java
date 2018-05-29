/**
 * 
 */
package org.Recollect.Core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.*;

import javax.xml.bind.JAXBException;

/**
 * @author amartinez
 *
 */
public class Recollect {


    private JaxbUnmarshal jaxbUnmarshal;

	private OAIClient client;
	private IdentifyHandler identifyHandler;
	private ListMetadataFormatsHandler listMetadataFormatsHandler;
	
	public Recollect(OAIClient oaiClient) {
		this.client = oaiClient;
		this.identifyHandler = new IdentifyHandler(oaiClient);
		this.listMetadataFormatsHandler = new ListMetadataFormatsHandler(oaiClient);
	}
	
	
	public IdentifyType identify () throws Exception {
        return identifyHandler.handle();
    }

    public Iterator<MetadataFormatType> listMetadataFormats () throws Exception {
        return listMetadataFormatsHandler.handle(ListMetadataParameters.request()).iterator();
    }

    public Iterator<MetadataFormatType> listMetadataFormats (ListMetadataParameters parameters) throws Exception {
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

        return new ItemIterator(new ListRecordHandler(client, parameters, classType));
    }

    public Iterator<HeaderType> listIdentifiers (ListIdentifiersParameters parameters) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListIdentifiers verb requires the metadataPrefix");
        return new ItemIterator(new ListIdentifierHandler(client, parameters));
    }

    public Iterator<SetType> listSets () throws Exception {
        return new ItemIterator(new ListSetsHandler(client));
    }

    public boolean isOAI() throws MalformedURLException {
        jaxbUnmarshal = new JaxbUnmarshal(new URL(client.getURL()), new Class[]{OAIPMHtype.class});
        return jaxbUnmarshal.isValidating();
    }

    public List<String> gethandleEventErrors(){
        return (Objects.isNull(jaxbUnmarshal.getValidationEvent().getEventError())) ? null : jaxbUnmarshal.getValidationEvent().getEventError();
    }
}
