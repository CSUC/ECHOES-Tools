/**
 * 
 */
package org.csuc.rest.api.utils.jwt;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author amartinez
 *
 */
public class Token {
	
	@JsonProperty
	private String access_token;
	
	@JsonProperty
	private long expire_in;
	
	@JsonProperty
	private long issued_at;
	
	@JsonProperty
	private String token_type;
	
	public Token() {}

	public Token(String access, long expires, long issued, String type){
		this.access_token = access;
		this.expire_in = expires;
		this.issued_at = issued;
		this.token_type = type;		
	}
	
	@JsonGetter("access_token")
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@JsonGetter("expire_in")
	public long getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(long expire_in) {
		this.expire_in = expire_in;
	}

	@JsonGetter("issued_at")
	public long getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(long issued_at) {
		this.issued_at = issued_at;
	}
	
	@JsonGetter("token_type")
	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	
}
