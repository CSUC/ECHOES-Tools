/**
 * 
 */
package org.csuc.rest.api.utils;

import javax.ws.rs.core.Response;

/**
 * @author amartinez
 *
 */
public enum HTTPStatusCode {
	
	BAD_REQUEST(Response.Status.BAD_REQUEST.getReasonPhrase(),"[INVALID_REQUEST] Request is not well-formed, syntactically incorrect, or violates schema.",Response.Status.BAD_REQUEST.getStatusCode()),
	UNAUTHORIZED(Response.Status.UNAUTHORIZED.getReasonPhrase(),"[AUTHENTICATION_FAILURE] Authentication failed due to invalid authentication credentials.",Response.Status.UNAUTHORIZED.getStatusCode());
//	OK("","",0),
//	CREATED("","",0),
//	ACCEPTED("","",0),
//	NO_CONTENT("","",0),
//	RESET_CONTENT("","",0),
//	PARTIAL_CONTENT("","",0),
//	MOVED_PERMANENTLY("","",0),
//	FOUND("","",0),
//	SEE_OTHER("","",0),
//	NOT_MODIFIED("","",0),
//	USE_PROXY("","",0),
//	TEMPORARY_REDIRECT("","",0),	
//	PAYMENT_REQUIRED("","",0),
//	FORBIDDEN("","",0),
//	NOT_FOUND("","",0),
//	METHOD_NOT_ALLOWED("","",0),
//	NOT_ACCEPTABLE("","",0),
//	PROXY_AUTHENTICATION_REQUIRED("","",0),
//	REQUEST_TIMEOUT("","",0),
//	CONFLICT("","",0),
//	GONE("","",0),
//	LENGTH_REQUIRED("","",0),
//	PRECONDITION_FAILED("","",0),
//	REQUEST_ENTITY_TOO_LARGE("","",0),
//	REQUEST_URI_TOO_LONG("","",0),
//	UNSUPPORTED_MEDIA_TYPE("","",0),
//	REQUESTED_RANGE_NOT_SATISFIABLE("","",0),
//	EXPECTATION_FAILED("","",0),
//	INTERNAL_SERVER_ERROR("","",0),
//	NOT_IMPLEMENTED("","",0),
//	BAD_GATEWAY("","",0),
//	SERVICE_UNAVAILABLE("","",0),
//	GATEWAY_TIMEOUT("","",0),
//	HTTP_VERSION_NOT_SUPPORTED("","",0);
	
	private String error;
	private String error_description;
	private int code;
	
	HTTPStatusCode(String error, String description, int code){
		this.error = error;
		this.error_description = description;
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
