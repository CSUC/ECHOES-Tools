/**
 * 
 */
package org.EDM.Transformations.formats;

import org.w3c.dom.Node;

import java.io.OutputStream;
import java.io.Writer;
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
	 * @param outs
	 */
	void marshal(Charset encoding, boolean alone, OutputStream outs);

	/**
	 *
	 * @param encoding
	 * @param alone
	 * @param writer
	 */
	void marshal(Charset encoding, boolean alone, Writer writer);
}
