/**
 * 
 */
package org.Recollect.Core.typesafe;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author amartinez
 *
 */
public class CollectionConfig {

	String name;
	String host;
	String dspace_collection;
	Set<String> sets;
	
	public CollectionConfig() {}
	
	public CollectionConfig(String name, String host, String dspace, Set<String> collections) {
		this.name = name;
		this.host = host;
		this.dspace_collection = dspace;
		this.sets = collections;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDspace_collection() {
		return dspace_collection;
	}

	public void setDspace_collection(String dspace_collection) {
		this.dspace_collection = dspace_collection;
	}

	public Set<String> getSets() {
		return sets;
	}

	public void setSets(Set<String> sets) {
		this.sets = sets;
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);		
		for(Field f : getClass().getDeclaredFields()){			
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
