/**
 * 
 */
package org.Morphia.Core.utils;

/**
 * @author amartinez
 *
 */
public enum HarvestStatus {

	READY(0, "READY"), 
	BUSY(1, "BUSY"), 
	QUEUED(2, "QUEUED"), 
	OAI_ERROR(3, "OAI_ERROR"), 
	UNKNOWN_ERROR(-1, "UNKNOWN_ERROR");
	
	private int value; 
	private String description;
	
	private HarvestStatus(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
