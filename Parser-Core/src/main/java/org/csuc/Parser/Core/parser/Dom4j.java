/**
 * 
 */
package org.csuc.Parser.Core.parser;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class Dom4j implements ParserStrategy {
	static final Logger logger = LogManager.getLogger(Dom4j.class.getName());
	
	public Dom4j() {
			
	}
	
	@Override
	public void execute() {
		logger.info("Dom4j Stretategy Parser");		
	}

	@Override
	public Map<String, String> getMapValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getNamespaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, AtomicInteger> getElementNameCount() {
		// TODO Auto-generated method stub
		return null;
	}
}
