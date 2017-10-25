/**
 * 
 */
package org.Morphia.Core.entities;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author amartinez
 *
 */
@Entity(value="parser_results", noClassnameStored = true )
@Indexes(
	@Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true)) 
)
public class ParserResults {

	@Id
	private String id;
	
	@Reference("parser_config_id")
	private ParserConfig parser_config_id;
	
	@Embedded("namespace")
	private List<Namespace> namespace;
	
	@Embedded("result")
	private List<ParserResultsJSON> json;

	public ParserResults() {
		setId(UUID.randomUUID().toString());
	}
	
	public ParserResults(UUID uuid) {
		setId(uuid.toString());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ParserConfig getParser_config_id() {
		return parser_config_id;
	}

	public void setParser_config_id(ParserConfig parser_config_id) {
		this.parser_config_id = parser_config_id;
	}

	public List<Namespace> getNamespace() {
		return namespace;
	}

	public void setNamespace(List<Namespace> namespace) {
		this.namespace = namespace;
	}

	public List<ParserResultsJSON> getJson() {
		return json;
	}

	public void setJson(List<ParserResultsJSON> json) {
		this.json = json;
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
				if(value instanceof ParserConfig)	builder.append(f.getName(), ((ParserConfig)value).toJson());
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
		if(!(obj instanceof ParserResults)) return false;
		
		ParserResults other = (ParserResults) obj;
        return Objects.equals(this.id, other.id);
	}
}
