package org.csuc.rest.api.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
	
	private String token;
	private String type = "Bearer";
	
	public TokenResponse(String token) {
		this.token = token;
	}
	
	public TokenResponse(String token, String type) {
		this.token = token;
		this.type = type;
	}

	@JsonProperty("token_access")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("token_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
}
