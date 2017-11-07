/**
 * 
 */
package org.csuc.rest.api.typesafe;

/**
 * @author amartinez
 *
 */
public class TypesafeToken {

	private String token_type;
	private int expiresAt;

	public TypesafeToken() {
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public int getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(int expiresAt) {
		this.expiresAt = expiresAt;
	}
}