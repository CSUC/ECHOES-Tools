/**
 * 
 */
package org.csuc.rest.api.typesafe;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

/**
 * @author amartinez
 *
 */
public class ApplicationConfig extends ConfigBeanFactory{
	
	private Config conf;
	
	public ApplicationConfig() {
		conf = ConfigFactory.load("conf/application");
		
	}
	
	public TypesafeUser getUserConfig() {
		return create(conf.getConfig("user"), TypesafeUser.class);
	}
	
	public TypesafeMongoDB getMongodbConfig() {
		return create(conf.getConfig("mongodb"), TypesafeMongoDB.class);
	}
	
	public TypesafeToken getTokenConfig() {
		return create(conf.getConfig("token"), TypesafeToken.class);
	}
	
}
