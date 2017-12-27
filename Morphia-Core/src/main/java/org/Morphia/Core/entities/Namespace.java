/**
 * 
 */
package org.Morphia.Core.entities;

import org.bson.Document;

/**
 * @author amartinez
 *
 */
public class Namespace extends Document{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Namespace() {}
	
	public Namespace(String url, String prefix) {
		put("url", url);
		put("prefix", prefix);		
	}
	
}
