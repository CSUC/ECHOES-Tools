/**
 * 
 */
package org.csuc.rest.api.context;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.Morphia.Core.entities.User;
import org.Morphia.Core.utils.Role;
import org.apache.commons.lang3.EnumUtils;
import org.csuc.rest.api.typesafe.ApplicationConfig;
import org.csuc.rest.api.typesafe.TypesafeToken;

/**
 * @author amartinez
 *
 */
public class BasicSecurityContext implements SecurityContext {

	private TypesafeToken tokenConf = new ApplicationConfig().getTokenConfig();
	
	private String AUTHENTICATION_SCHEME = tokenConf.getToken_type();

	private final User user;
	private final boolean secure;

	public BasicSecurityContext(User user, boolean secure) {
		this.user = user;
		this.secure = secure;
	}

	@Override
	public String getAuthenticationScheme() {
		return AUTHENTICATION_SCHEME;
	}

	@Override
	public Principal getUserPrincipal() {
		return new Principal() {	
			@Override
			public String getName() {
				return user.toJson();
			}
		};
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public boolean isUserInRole(String role) {
		return EnumUtils.isValidEnum(Role.class, role);
		
	}

}
