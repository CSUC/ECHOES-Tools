/**
 * 
 */
package org.Morphia.Core.utils;

/**
 * @author amartinez
 *
 */
public enum ParserType {

	FILE("file"), 
	URL("url"), 
	OAI("oai");
	
	private String value;
	
	private ParserType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
