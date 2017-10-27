/**
 * 
 */
package org.csuc.rest.api.utils;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author amartinez
 *
 */
public class ResponseError implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String error;
	private String description;
	private int responseCode;
	
	public ResponseError(HTTPStatusCode status) {
//		{
//			  "error": "invalid_request",
//			  "error_description": "Request was missing the 'redirect_uri' parameter.",
//			  "error_uri": "See the full API docs at https://authorization-server.com/docs/access_token"
//			}
		this.error = status.getError();
		this.description = status.getError_description();
		this.responseCode = status.getCode();
	}

	@JsonProperty("error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@JsonProperty("error_description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("responseCode")
	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	
	
}
