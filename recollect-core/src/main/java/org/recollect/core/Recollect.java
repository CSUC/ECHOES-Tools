/**
 * 
 */
package org.recollect.core;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.recollect.core.client.OAIClient;
import org.recollect.core.util.ItemIterator;
import org.recollect.core.handler.GetRecordHandler;
import org.recollect.core.handler.IdentifyHandler;
import org.recollect.core.handler.ListIdentifierHandler;
import org.recollect.core.handler.ListMetadataFormatsHandler;
import org.recollect.core.handler.ListRecordHandler;
import org.recollect.core.handler.ListSetsHandler;
import org.recollect.core.parameters.GetRecordParameters;
import org.recollect.core.parameters.ListIdentifiersParameters;
import org.recollect.core.parameters.ListMetadataParameters;
import org.recollect.core.parameters.ListRecordsParameters;
import org.csuc.deserialize.JaxbUnmarshal;
import org.openarchives.oai._2.*;

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
    public ItemIterator<RecordType> listRecords (ListRecordsParameters parameters, Class<?>[] classType) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListRecords verb requires the metadataPrefix");
        return new ItemIterator(new ListRecordHandler(client, parameters, classType));
    }

    /**
     *
     * @param parameters
     * @return
     * @throws Exception
     */
    public ItemIterator<HeaderType> listIdentifiers (ListIdentifiersParameters parameters) throws Exception {
        if (!parameters.areValid())
        	throw new Exception("ListIdentifiers verb requires the metadataPrefix");
        try{
            return new ItemIterator(new ListIdentifierHandler(client, parameters));
        }catch (Exception e){
            exceptionList.add(e);
            return null;
        }
    }

    public ItemIterator<SetType> listSets () {
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

    public boolean isOAI(String url) throws MalformedURLException {
        jaxbUnmarshal = new JaxbUnmarshal(new URL(url), new Class[]{OAIPMHtype.class});
        return jaxbUnmarshal.isValidating();
    }

    public int size() throws MalformedURLException {
        if(isOAI()){
            return Optional.ofNullable(((OAIPMHtype) jaxbUnmarshal.getObject()).getListRecords())
                    .map(ListRecordsType::getResumptionToken)
                    .map(ResumptionTokenType::getCompleteListSize)
                    .map(BigInteger::intValueExact)
                    .orElse(0);
        }
        return 0;
    }

    public int size(String url) throws MalformedURLException {
        if(isOAI(url)){
            return Optional.ofNullable(((OAIPMHtype) jaxbUnmarshal.getObject()).getListRecords())
                    .map(ListRecordsType::getResumptionToken)
                    .map(ResumptionTokenType::getCompleteListSize)
                    .map(BigInteger::intValueExact)
                    .orElse(0);
        }
        return 0;
    }

    public List<String> gethandleEventErrors(){
        return (Objects.isNull(jaxbUnmarshal.getValidationEvent().getEventError())) ? null : jaxbUnmarshal.getValidationEvent().getEventError();
    }

    public List<Exception> getExceptionList() {
        return exceptionList;
    }
}
