/**
 * 
 */
package org.csuc.rest.api.typesafe;

/**
 * @author amartinez
 *
 */
public class TypesafeMongoDB {
	
	private String host;
	private int port;
	private String database;
	
	public TypesafeMongoDB() {}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
