/**
 * 
 */
package org.Morphia.Core.entities;


import java.util.Objects;
import java.util.UUID;

import org.Morphia.Core.utils.Role;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

/**
 * @author amartinez
 *
 */
/**
 * @author amartinez
 *
 */
@Entity(value="user", noClassnameStored = true )
@Indexes(
	@Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true)) 
)
//@Validation(value = "{password:{$ne:null}}")
public class User {

	@Id
	private String id;
	
	@Property("uuid")
	private String uuid;
	
	@Property("password")
	private String password;
	
	@Property("digest_algorithm")
	private String digest;

	@Property("role")
	private Role role;
	
	public User() {
	}
	
	public User(String email) {
		setId(email);
		setUuid(UUID.randomUUID().toString());
	}	
	
	public User(UUID uuid) {
		setId(uuid.toString());
		setUuid(uuid.toString());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
		
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
				builder.append(f.getName(), value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj))	return false;		        
		if(!(obj instanceof User)) return false;
		
        User other = (User) obj;
        return Objects.equals(this.id, other.id);
	}
}
