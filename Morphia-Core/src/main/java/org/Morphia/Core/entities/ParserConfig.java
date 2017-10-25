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
@Entity(value="parser_config", noClassnameStored = true )
@Indexes(
	@Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true)) 
)
public class ParserConfig {

	@Id
	private String id;
	
	@Property("type")
	private String type;
	
	@Property("source")
	private String source;
	
	@Property("status")
	private int status;
	
	@Property("start_time")
	private Date starttime;
	
	@Reference("user_id")
	private User userid;

	public ParserConfig() {
		setId(UUID.randomUUID().toString());
	}
	
	public ParserConfig(UUID uuid) {
		setId(uuid.toString());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
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
		if(!(obj instanceof ParserConfig)) return false;
		
		ParserConfig other = (ParserConfig) obj;
        return Objects.equals(this.id, other.id);
	}
}
