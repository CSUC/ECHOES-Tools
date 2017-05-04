/**
 * 
 */
package org.csuc.csv;

import java.util.HashMap;

import static org.apache.commons.lang3.StringEscapeUtils.escapeCsv;

/**
 * @author amartinez
 *
 */
public class AgentCSV extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] header = null;
	
	private String identifier = null;
	private String skosPrefLabel = null;
	private String skosAltLabel = null;
	private String edmIsRelatedTo = null;
	private String rdaGr2ProfessionOrOcupation = null;
	
	
	
	public AgentCSV(String[] header, String identifier, String skosPrefLabel, String skosAltLabel, String edmIsRelatedTo,
			String rdaGr2ProfessionOrOcupation ) {
		
		this.header = header;
		
		this.identifier = escapeCsv(identifier);
		this.skosPrefLabel = escapeCsv(skosPrefLabel);
		this.skosAltLabel = escapeCsv(skosAltLabel);
		this.edmIsRelatedTo = escapeCsv(edmIsRelatedTo);
		this.rdaGr2ProfessionOrOcupation =  escapeCsv(rdaGr2ProfessionOrOcupation);
		
		getMap();
	}



	public String getIdentifier() {
		return identifier;
	}



	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}



	public String getSkosPrefLabel() {
		return skosPrefLabel;
	}



	public void setSkosPrefLabel(String skosPrefLabel) {
		this.skosPrefLabel = skosPrefLabel;
	}



	public String getSkosAltLabel() {
		return skosAltLabel;
	}



	public void setSkosAltLabel(String skosAltLabel) {
		this.skosAltLabel = skosAltLabel;
	}



	public String getEdmIsRelatedTo() {
		return edmIsRelatedTo;
	}



	public void setEdmIsRelatedTo(String edmIsRelatedTo) {
		this.edmIsRelatedTo = edmIsRelatedTo;
	}



	public String getRdaGr2ProfessionOrOcupation() {
		return rdaGr2ProfessionOrOcupation;
	}



	public void setRdaGr2ProfessionOrOcupation(String rdaGr2ProfessionOrOcupation) {
		this.rdaGr2ProfessionOrOcupation = rdaGr2ProfessionOrOcupation;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object toObject(){
		return new Object[]{
			identifier.toString(),
			skosPrefLabel.toString(),
			skosAltLabel.toString(),
			edmIsRelatedTo.toString(),
			rdaGr2ProfessionOrOcupation.toString()				
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public void getMap(){
		put(header[16], this.identifier.isEmpty() ? null : this.identifier.toString());
		put(header[17], this.skosPrefLabel.isEmpty() ? null : this.skosPrefLabel.toString());
		put(header[18], this.skosAltLabel.isEmpty() ? null : this.skosAltLabel.toString());
		put(header[19], this.edmIsRelatedTo.isEmpty() ? null : this.edmIsRelatedTo.toString());
		put(header[20], this.rdaGr2ProfessionOrOcupation.isEmpty() ? null : this.rdaGr2ProfessionOrOcupation.toString());				
	}
}
