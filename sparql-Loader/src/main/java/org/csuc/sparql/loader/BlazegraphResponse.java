/**
 * 
 */
package org.csuc.sparql.loader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author amartinez
 *
 */
@JacksonXmlRootElement(localName = "data")
public class BlazegraphResponse {
	
	@JacksonXmlProperty(localName = "modified")
	private String modified;
	@JacksonXmlProperty(localName = "milliseconds")
	private String milliseconds;
	
	public BlazegraphResponse() {
	}
	
	public BlazegraphResponse(String modified, String milliseconds){
		this.modified = modified;
		this.milliseconds = milliseconds;		
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public String getModified() {
		return modified;
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public String getMilliseconds() {
		return milliseconds;
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public void setMilliseconds(String milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	@JacksonXmlProperty(isAttribute=true)
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	@Override
	public String toString() {
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(true);
  	  	XmlMapper xmlMapper = new XmlMapper(module);
		try {
			return xmlMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			System.err.println(e);
			return null;
		}
	}
}
