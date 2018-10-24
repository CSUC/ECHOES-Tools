/**
 * 
 */
package org.Recollect.Core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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


	private List<Exception> exceptionList = new ArrayList<>();

    /**
     *
     * @param oaiClient
     */
	public Recollect(OAIClient oaiClient) {
		this.client = oaiClient;
		this.identifyHandler = new IdentifyHandler(oaiClient);
		this.listMetadataFormatsHandler = new ListMetadataFormatsHandler(oaiClient);
	}

    /**
     *
     * @return
     */
	public IdentifyType identify () {
	    try{
            return identifyHandler.handle();
        }catch (Exception e){
	        exceptionList.add(e);
	        return null;
        }
    }

    /**
     *
     * @return
     */
    public Iterator<MetadataFormatType> listMetadataFormats(){
        try{
            return listMetadataFormatsHandler.handle(ListMetadataParameters.request()).iterator();
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }

    /**
     *
     * @param parameters
     * @return
     */
    public Iterator<MetadataFormatType> listMetadataFormats (ListMetadataParameters parameters) {
        try{
            return listMetadataFormatsHandler.handle(parameters).iterator();
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }


    /**
     *
     * @param parameters
     * @param classType
     * @return
     * @throws Exception
     */
    public RecordType getRecord (GetRecordParameters parameters, Class<?>[] classType) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("GetRecord verb requires identifier and metadataPrefix parameters");
        try{
            return new GetRecordHandler(client, classType).handle(parameters);
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }


    /**
     *
     * @param parameters
     * @param classType
     * @return
     * @throws Exception
     */
    public Iterator<RecordType> listRecords (ListRecordsParameters parameters, Class<?>[] classType) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListRecords verb requires the metadataPrefix");
        try {
            return new ItemIterator(new ListRecordHandler(client, parameters, classType));
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }

    /**
     *
     * @param parameters
     * @return
     * @throws Exception
     */
    public Iterator<HeaderType> listIdentifiers (ListIdentifiersParameters parameters) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListIdentifiers verb requires the metadataPrefix");
        try{
            return new ItemIterator(new ListIdentifierHandler(client, parameters));
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }

    public Iterator<SetType> listSets () {
        try {
            return new ItemIterator(new ListSetsHandler(client));
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }

    public boolean isOAI() throws MalformedURLException {
        jaxbUnmarshal = new JaxbUnmarshal(new URL(client.getURL()), new Class[]{OAIPMHtype.class});
        return jaxbUnmarshal.isValidating();
    }

    public List<String> gethandleEventErrors(){
        return (Objects.isNull(jaxbUnmarshal.getValidationEvent().getEventError())) ? null : jaxbUnmarshal.getValidationEvent().getEventError();
    }

    public List<Exception> getExceptionList() {
        return exceptionList;
    }
}
