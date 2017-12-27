/**
 * 
 */
package org.Morphia.Core.entities;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author amartinez
 *
 */
@Entity(value="harvested_collection_config", noClassnameStored = true )
@Indexes(
	@Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true)) 
)
public class HarvestedCollectionConfig {
	
	@Id
	private String id;
	
	@Property("oai_source")
	private String oaisource;
	
	@Property("oai_set")
	private String oaisetid;
	
	@Property("harvest_message")
	private String harcestmessage;
	
	@Property("metadata_config")
	private String metadataconfig;
	
	@Property("harvest_status")
	private int harveststatus;
	
	@Property("harvest_start_time")
	private Date harveststarttime;
	
	@Property("last_harvested")
	private Date lastharvested;
	
	@Property("xsd_config")
	private String xsdconfig;

	@Reference("user_id")
	private User user_id;
	
	public HarvestedCollectionConfig() {
		setId(UUID.randomUUID().toString());
	}
	
	public HarvestedCollectionConfig(UUID uuid) {
		setId(uuid.toString());
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setOaisource(String oaisource) {
		this.oaisource = oaisource;
	}

	public void setOaisetid(String oaisetid) {
		this.oaisetid = oaisetid;
	}

	public void setHarcestmessage(String harcestmessage) {
		this.harcestmessage = harcestmessage;
	}

	public void setMetadataconfig(String metadataconfig) {
		this.metadataconfig = metadataconfig;
	}

	public void setHarveststarttime(Date harveststarttime) {
		this.harveststarttime = harveststarttime;
	}

	public void setLastharvested(Date lastharvested) {
		this.lastharvested = lastharvested;
	}

	public void setXsdconfig(String xsdconfig) {
		this.xsdconfig = xsdconfig;
	}
	
	public int getHarveststatus() {
		return harveststatus;
	}

	public void setHarveststatus(int harveststatus) {
		this.harveststatus = harveststatus;
	}

	public String getId() {
		return id;
	}

	public String getOaisource() {
		return oaisource;
	}

	public String getOaisetid() {
		return oaisetid;
	}

	public String getHarcestmessage() {
		return harcestmessage;
	}

	public String getMetadataconfig() {
		return metadataconfig;
	}

	public Date getHarveststarttime() {
		return harveststarttime;
	}

	public Date getLastharvested() {
		return lastharvested;
	}

	public String getXsdconfig() {
		return xsdconfig;
	}
	
	public User getUser_id() {
		return user_id;
	}
	
	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);		
		for(java.lang.reflect.Field f : getClass().getDeclaredFields()){			
			f.setAccessible(true);
			try {
				Object value = f.get(this);
				builder.append(f.getName(), value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	public String toJson() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		for(java.lang.reflect.Field f : getClass().getDeclaredFields()){			
			f.setAccessible(true);
			try {
				Object value = f.get(this);
				if(value instanceof User)	builder.append(f.getName(), ((User)value).toJson());
				else builder.append(f.getName(), value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj))	return false;		        
		if(!(obj instanceof HarvestedCollectionConfig)) return false;
		
		HarvestedCollectionConfig other = (HarvestedCollectionConfig) obj;
        return Objects.equals(this.id, other.id);
	}
}
