/**
 * 
 */
package org.Morphia.Core.dao;

import org.Morphia.Core.entities.UserToken;

/**
 * @author amartinez
 *
 */
public interface TokenDAO {

	public UserToken findById(String id);
	public UserToken findByToken(String token);
	public UserToken findByToken(String token, String type);
}
