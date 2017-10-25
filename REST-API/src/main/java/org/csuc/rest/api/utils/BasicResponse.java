/**
 * 
 */
package org.csuc.rest.api.utils;

import org.bson.Document;

/**
 * @author amartinez
 *
 */
public class BasicResponse extends Document {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BasicResponse(String message) {
		put("message", message);
	}
	
}
