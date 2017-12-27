/**
 * 
 */
package org.Recollect.Core.interfaces;

/**
 * @author amartinez
 *
 */
public interface VerbInterface {
	
	/**
	 * 
	 * @param metadataPrefix
	 * @param from
	 * @param until
	 * @param granularity
	 * @param set
	 */
	public void ListRecords(String metadataPrefix, String from, String until, String granularity, String set) throws Exception;
	
	/**
	 * 
	 */
	public void Identify() throws Exception;
	
	/**
	 * 
	 */
	public void ListMedataFormats() throws Exception;	
	
	/**
	 * 
	 */
	public void ListSets() throws Exception;
	
	/**
	 * 
	 * @param identifier
	 * @param metadataPrefix
	 */
	public void GetRecord(String identifier, String metadataPrefix) throws Exception;
	
	/**
	 * 
	 * @param metadataPrefix
	 * @param from
	 * @param until
	 * @param granularity
	 * @param set
	 */
	public void ListIdentifiers(String metadataPrefix, String from, String until, String granularity, String set) throws Exception;
	
}
