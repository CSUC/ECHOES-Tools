/**
 * 
 */
package org.EDM.Transformations.formats;

import java.nio.charset.Charset;


/**
 * 
 * 
 * 
 * @author amartinez
 *
 */
public interface EDM {
	
	/**
	 * 
	 */
	void edmProvidedCHO();
	
	/**
	 * 
	 */
	void edmAgent();
	
	/**
	 * 
	 */
	void edmPlace();
	
	/**
	 * 
	 */
	void edmTimeSpan();
	
	/**
	 * 
	 */
	void skosConcept();
	
	/**
	 * 
	 * 
	 */
	void edmWebResource();
	
	
	/**
	 * 
	 */
	void oreAggregation();
	
	
	/**
	 * 
	 * @param encoding
	 * @param alone
	 */
	void marshal(Charset encoding, boolean alone);
}
