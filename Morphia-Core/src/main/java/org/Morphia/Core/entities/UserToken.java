/**
 * 
 */
package org.Morphia.Core.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * @author amartinez
 *
 */
@Entity(value="token", noClassnameStored = true )
public class UserToken {

	@Id
	private String id;
	
	@Property("token_type")
	private String token_type;
	
	
	@Property("access_token")
	private String access_token;

	
	public UserToken() {}
	
	public UserToken(String id, String type, String token) {
		//String authStringEnc = Password.getSecurePassword(String.format("%s:%s", id, this.password), "SHA-256");
		this.id = id;
		this.token_type  = type;
		this.access_token = token;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	
}
