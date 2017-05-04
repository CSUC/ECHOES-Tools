/**
 * 
 */
package org.csuc.csv;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author amartinez
 *
 */
public class ProvidedCHOCSV extends HashMap<String, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] header = null;
	private String dcIdentifier = null;
	private String dcCreator = null;
	private String dcRights = null;
	private String dcTitle = null;
	private String dcDescription = null;
	private String dcCoverage = null;
	private String dcType = null;
	private String dcSource = null;
	private String dcSubject = null;
	private String dcDate = null;
	private String dcPublisher = null;
	private String dcFormat = null;
	private String dcRelation = null;
	private String dcContributor = null;
	private String dcLanguage = null;
	
	private String edmType = null;
	
	
	public ProvidedCHOCSV(String[] header, String identifier, String creator, String rights, String title, String description,
			String coverage, String dcType, String source, String subject, String date,
			String publisher, String format, String relation, String contributor, String language, String edmType) {
		
		this.header = header;
		
		this.dcIdentifier = identifier;
		this.dcCreator = creator;
		this.dcRights = rights;
		this.dcTitle = title;
		this.dcDescription = description;
		this.dcCoverage = coverage;
		this.dcType = dcType;
		this.dcSource = source;
		this.dcSubject = subject;
		this.dcDate = date;
		this.dcPublisher = publisher;
		this.dcFormat = format;
		this.dcRelation = relation;
		this.dcContributor = contributor;
		this.dcLanguage = language;
		
		this.edmType = edmType;
		
		getMap();
	}
			
	public String getDcIdentifier() {
		return dcIdentifier;
	}

	public void setDcIdentifier(String dcIdentifier) {
		this.dcIdentifier = dcIdentifier;
	}

	public String getDcCreator() {
		return dcCreator;
	}

	public void setDcCreator(String dcCreator) {
		this.dcCreator = dcCreator;
	}

	public String getDcRights() {
		return dcRights;
	}

	public void setDcRights(String dcRights) {
		this.dcRights = dcRights;
	}

	public String getDcTitle() {
		return dcTitle;
	}

	public void setDcTitle(String dcTitle) {
		this.dcTitle = dcTitle;
	}

	public String getDcDescription() {
		return dcDescription;
	}

	public void setDcDescription(String dcDescription) {
		this.dcDescription = dcDescription;
	}

	public String getDcCoverage() {
		return dcCoverage;
	}

	public void setDcCoverage(String dcCoverage) {
		this.dcCoverage = dcCoverage;
	}

	public String getDcType() {
		return dcType;
	}

	public void setDcType(String dcType) {
		this.dcType = dcType;
	}

	public String getDcSource() {
		return dcSource;
	}

	public void setDcSource(String dcSource) {
		this.dcSource = dcSource;
	}

	public String getDcSubject() {
		return dcSubject;
	}

	public void setDcSubject(String dcSubject) {
		this.dcSubject = dcSubject;
	}

	public String getEdmType() {
		return edmType;
	}

	public void setEdmType(String edmType) {
		this.edmType = edmType;
	}
	
	public String getDcDate() {
		return dcDate;
	}

	public void setDcDate(String dcDate) {
		this.dcDate = dcDate;
	}

	public String getDcPublisher() {
		return dcPublisher;
	}

	public void setDcPublisher(String dcPublisher) {
		this.dcPublisher = dcPublisher;
	}

	public String getDcFormat() {
		return dcFormat;
	}

	public void setDcFormat(String dcFormat) {
		this.dcFormat = dcFormat;
	}

	public String getDcRelation() {
		return dcRelation;
	}

	public void setDcRelation(String dcRelation) {
		this.dcRelation = dcRelation;
	}

	public String getDcContributor() {
		return dcContributor;
	}

	public void setDcContributor(String dcContributor) {
		this.dcContributor = dcContributor;
	}

	public String getDcLanguage() {
		return dcLanguage;
	}

	public void setDcLanguage(String dcLanguage) {
		this.dcLanguage = dcLanguage;
	}

	/**
	 * 
	 * @return
	 */
	public Object toObject(){
		return new Object[]{
			dcIdentifier.toString(),
			dcCreator.toString(), 
			dcRights.toString(),
			dcTitle.toString(), 
			dcDescription.toString(),
			dcCoverage.toString(), 
			dcSource.toString(), 
			dcSubject.toString(),
			dcDate,toString(),
			dcPublisher.toString(),
			dcFormat.toString(),
			dcRelation.toString(),
			dcContributor.toString(),
			dcLanguage.toString(),
			edmType.toString()
			};
	}
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/**
	 * 
	 * @return
	 */
	public void getMap(){
		put(header[0], this.dcIdentifier.isEmpty() ? null : this.dcIdentifier.toString());
		put(header[1], this.dcCreator.isEmpty() ? null : this.dcCreator.toString());
		put(header[2], this.dcRights.isEmpty() ? null : this.dcRights.toString());
		put(header[3], this.dcTitle.isEmpty() ? null : this.dcTitle.toString());
		put(header[4], this.dcDescription.isEmpty() ? null : this.dcDescription.toString());
		put(header[5], this.dcCoverage.isEmpty() ? null : this.dcCoverage.toString());
		put(header[6], this.dcType.isEmpty() ? null : this.dcType.toString());
		put(header[7], this.dcSource.isEmpty() ? null : this.dcSource.toString());
		put(header[8], this.dcSubject.isEmpty() ? null : this.dcSubject.toString());
		put(header[9], this.dcDate.isEmpty() ? null : dcDate.toString());
		put(header[10], this.dcPublisher.isEmpty() ? null : dcPublisher.toString());
		put(header[11], this.dcFormat.isEmpty() ? null : dcFormat.toString());
		put(header[12], this.dcRelation.isEmpty() ? null : dcRelation.toString());
		put(header[13], this.dcContributor.isEmpty() ? null : dcContributor.toString());
		put(header[14], this.dcLanguage.isEmpty() ? null : dcLanguage.toString());		
		put(header[15], this.edmType.isEmpty() ? null : edmType.toString());
		
	}

}

