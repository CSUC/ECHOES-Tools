/**
 * 
 */
package org.csuc.rest.api.utils;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

/**
 * @author amartinez
 *
 */
public class Token {
	
	private String type;
	
	private String token;

	public Token() {}
	
	public Token(String type, String token) {
		this.type = type;
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
