/**
 * 
 */
package org.csuc.utils.recollect;

/**
 * @author amartinez
 *
 */
public enum TransformationType {

	FILE("file"),
	URL("url"),
	OAI("oai");

	private final String value;

	TransformationType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static TransformationType convert(String value) {
		for (TransformationType inst : values()) {
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
