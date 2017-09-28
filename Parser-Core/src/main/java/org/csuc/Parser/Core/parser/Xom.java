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
public class Xom implements ParserStrategy{

static final Logger logger = LogManager.getLogger(Xom.class.getName());
	
	public Xom() {
			
	}
	
	@Override
	public void execute() {
		logger.info("Xom Stretategy Parser");		
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
