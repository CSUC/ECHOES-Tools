/**
 * 
 */
package org.Morphia.Core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class Password {
	
	static Logger logger = LogManager.getLogger(Password.class);
	
	private String algorithm;
	private String psswd;
	
	
	/**
	 * 
	 * @param digest
	 * @param password
	 */
	public Password(String digest, String password) {
		this.algorithm = digest;
		this.psswd = password;		
	}

	/**
	 * 
	 * @return
	 */
	public String getSecurePassword() {		
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(psswd.getBytes());

			byte byteData[] = md.digest();

	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++)
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        
	    	return sb.toString();   	
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
	    	return null;
	    }
	}
	
	/**
	 * 
	 * @param original
	 * @param algorithm
	 * @return
	 */
	public static String getSecurePassword(String original, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(original.getBytes());

			byte byteData[] = md.digest();

	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++)
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        
	    	return sb.toString();   	
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
	    	return null;
	    }
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
}
