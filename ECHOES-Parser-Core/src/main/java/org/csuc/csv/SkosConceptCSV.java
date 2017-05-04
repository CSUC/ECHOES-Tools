package org.csuc.csv;


import static org.apache.commons.lang3.StringEscapeUtils.escapeCsv;

import java.util.HashMap;

/**
 * 
 * @author amartinez
 *
 */
public class SkosConceptCSV extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] header = null;
	private String skosPrefLabel = null;
	
	public SkosConceptCSV(String[] header, String skosPrefLabel) {
		this.header = header;
		
		this.skosPrefLabel = escapeCsv(skosPrefLabel);
		
		
		
		getMap();
	}
	
	public String getSkosPrefLabel() {
		return skosPrefLabel;
	}

	public void setSkosPrefLabel(String skosPrefLabel) {
		this.skosPrefLabel = skosPrefLabel;
	}

	public Object toObject(){
		return new Object[]{
			skosPrefLabel.toString()
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public void getMap(){
		put(header[21], this.skosPrefLabel.isEmpty() ? null : this.skosPrefLabel.toString());
					
	}
}
