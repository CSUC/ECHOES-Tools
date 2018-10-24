/**
 * 
 */
package org.csuc.utils.parser;

/**
 * @author amartinez
 *
 */
public enum ParserType {

	FILE("file"), 
	URL("url"), 
	OAI("oai");

	private final String value;

	ParserType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static ParserType convert(String value) {
		for (ParserType inst : values()) {
			if (inst.value().equals(value)) {
				return inst;
			}
		}
		return null;
	}

	public boolean equalsName(String otherName) {
		//(otherName == null) check is not needed because name.equals(null) returns false
		return value.equals(otherName);
	}

	@Override
	public String toString() {
		return value;
	}
}
