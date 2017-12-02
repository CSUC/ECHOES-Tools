/**
 * 
 */
package org.csuc.Parser.Core.util;

/**
 * @author amartinez
 *
 */
public enum EnumTypes {
	OAI ("oai"),
	URL ("url"),
	FILE ("file");

	private final String name;       

	private EnumTypes(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		//(otherName == null) check is not needed because name.equals(null) returns false 
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
}
