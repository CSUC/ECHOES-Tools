/**
 * 
 */
package org.csuc.rest.api.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * @author amartinez
 *
 */
public enum ResponseStatusCode {
	
	OK(
		Response
			.status(
				Response.Status.OK)),
	CREATED(
		Response
			.status(
				Response.Status.CREATED)),
	ACCEPTED(
		Response
			.status(
				Response.Status.ACCEPTED)),
	BAD_REQUEST(
		Response
			.status(
				Response.Status.BAD_REQUEST)
			.entity(
				new ResponseError(
					Response.Status.BAD_REQUEST.getReasonPhrase(),
					"[INVALID_REQUEST] Request is not well-formed, syntactically incorrect, or violates schema.", 
					Response.Status.BAD_REQUEST.getStatusCode()))),
	CONFLICT(
		Response
			.status(
				Response.Status.CONFLICT)
			.entity(
				new ResponseError(
					Response.Status.CONFLICT.getReasonPhrase(),
					"[AlreadyExists] The requested is not available. The field allredy exists of the system. Please select a different field and try again.", 
					Response.Status.CONFLICT.getStatusCode()))),
	NOT_FOUND(
		Response
			.status(
				Response.Status.NOT_FOUND)
			.entity(
				new ResponseError(
					Response.Status.NOT_FOUND.getReasonPhrase(),
					"[RESOURCE_NOT_FOUND] The specified resource does not exist.", 
					Response.Status.NOT_FOUND.getStatusCode()))),
	UNAUTHORIZED(
		Response
			.status(
				Response.Status.UNAUTHORIZED)
			.entity(
				new ResponseError(
					Response.Status.UNAUTHORIZED.getReasonPhrase(),
					"[AUTHENTICATION_FAILURE] Authentication failed due to invalid authentication credentials.", 
					Response.Status.UNAUTHORIZED.getStatusCode()))),
	INTERNAL_SERVER_ERROR(
		Response
			.status(
				Response.Status.INTERNAL_SERVER_ERROR)
			.entity(
				new ResponseError(
					Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					"[INTERNAL_SERVER_ERROR] An internal server error has occurred.", 
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))),
	NO_CONTENT(
		Response
			.status(
				Response.Status.NO_CONTENT)
			.entity(
				new ResponseError(
					Response.Status.NO_CONTENT.getReasonPhrase(),
					"[INTERNAL_SERVER_ERROR] An internal server error has occurred.", 
					Response.Status.NO_CONTENT.getStatusCode()))),
	NOT_MODIFIED(
		Response
			.status(
				Response.Status.NOT_MODIFIED)
			.entity(
				new ResponseError(
					Response.Status.NOT_MODIFIED.getReasonPhrase(),
					"[INTERNAL_SERVER_ERROR] An internal server error has occurred.", 
					Response.Status.NOT_MODIFIED.getStatusCode())));
//	RESET_CONTENT(),
//	PARTIAL_CONTENT(),
//	MOVED_PERMANENTLY(),
//	FOUND(),
//	SEE_OTHER(),
//	NOT_MODIFIED(),
//	USE_PROXY(),
//	TEMPORARY_REDIRECT(),	
//	PAYMENT_REQUIRED(),
//	FORBIDDEN(),
//	METHOD_NOT_ALLOWED(),
//	NOT_ACCEPTABLE(),
//	PROXY_AUTHENTICATION_REQUIRED(),
//	REQUEST_TIMEOUT(),
//	CONFLICT(),
//	GONE(),
//	LENGTH_REQUIRED(),
//	PRECONDITION_FAILED(),
//	REQUEST_ENTITY_TOO_LARGE(),
//	REQUEST_URI_TOO_LONG(),
//	UNSUPPORTED_MEDIA_TYPE(),
//	REQUESTED_RANGE_NOT_SATISFIABLE(),
//	EXPECTATION_FAILED(),
//	INTERNAL_SERVER_ERROR(),
//	NOT_IMPLEMENTED(),
//	BAD_GATEWAY(),
//	SERVICE_UNAVAILABLE(),
//	GATEWAY_TIMEOUT(),
//	HTTP_VERSION_NOT_SUPPORTED();
	
	private ResponseBuilder response;
	
	
	ResponseStatusCode(ResponseBuilder response){
		this.response = response;
	}
	
	public ResponseBuilder responseBuilder() {
		return response;
	}
	
	public Response build() {
		return response.build();
	}

}
