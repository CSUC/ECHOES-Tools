/**
 * 
 */
package org.csuc.rest.api.utils;

import java.io.Serializable;

/**
 * @author amartinez
 *
 */
public class Credentials implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
    private String password;
	
    public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}    
}
