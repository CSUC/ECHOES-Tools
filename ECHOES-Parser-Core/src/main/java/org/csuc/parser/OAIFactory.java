/**
 * 
 */
package org.csuc.parser;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class OAIFactory implements ParserFactory{
	static final Logger logger = LogManager.getLogger(OAIFactory.class.getName());

	private ParserStrategy strategy;
	private Instant inici;
	
	public OAIFactory(ParserStrategy st) {
		inici = Instant.now();
		this.strategy = st;
	}
	
	@Override
	public ParserStrategy instanceFactory() {
		return new Parser(strategy);
	}

	@Override
	public Map<String,String> getMapValues() {
		return strategy.getMapValues();
	}

	@Override
	public Map<String,String> getNamespaces() {
		return strategy.getNamespaces();
	}

	@Override
	public Map<String, AtomicInteger> getElementNameCount() {
		return strategy.getElementNameCount();
	}

	@Override
	public void getDuration() {	
	  long diff = Duration.between(inici, Instant.now()).getSeconds();	       
	  logger.info(String.format("%02d:%02d:%02d", diff / 3600, diff % 3600 / 60, diff % 60));	  	
	}
	
	
}
