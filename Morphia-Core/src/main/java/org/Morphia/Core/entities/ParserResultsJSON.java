/**
 * 
 */
package org.Morphia.Core.entities;

import org.bson.Document;

/**
 * @author amartinez
 *
 */
public class ParserResultsJSON extends Document{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParserResultsJSON() {}
	
	public ParserResultsJSON(String tag, int count, String xpath) {
		put("tag", tag);
		put("count", count);
		put("xpath", xpath);
	}
}
