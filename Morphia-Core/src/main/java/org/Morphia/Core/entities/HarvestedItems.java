/**
 * 
 */
package org.Morphia.Core.entities;

import java.util.Date;

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
@Entity(value="harvested_item", noClassnameStored = true )
@Indexes(
	@Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true)) 
)
public class HarvestedItems {

	@Id
	private String id;
	
	@Property("last_haravested")
	private Date lastharavested;
	
	@Reference
	private HarvestedCollectionConfig harvestedcollection;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLastharavested() {
		return lastharavested;
	}

	public void setLastharavested(Date lastharavested) {
		this.lastharavested = lastharavested;
	}

	public HarvestedCollectionConfig getHarvestedcollection() {
		return harvestedcollection;
	}

	public void setHarvestedcollection(HarvestedCollectionConfig harvestedcollection) {
		this.harvestedcollection = harvestedcollection;
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
}
