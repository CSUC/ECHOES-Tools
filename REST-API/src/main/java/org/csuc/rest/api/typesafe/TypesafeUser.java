/**
 * 
 */
package org.csuc.rest.api.typesafe;

/**
 * @author amartinez
 *
 */
public class TypesafeUser {
	
	private String username;
	private String digest;
	
	public TypesafeUser() {
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
	
}
