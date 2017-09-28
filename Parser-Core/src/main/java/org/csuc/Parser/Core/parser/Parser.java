/**
 * 
 */
package org.csuc.Parser.Core.parser;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 *
 */
public class Parser implements ParserStrategy {

	private ParserStrategy strategy;
	
	public Parser(ParserStrategy st) {
		this.strategy = st;
	}
	
	@Override
	public void execute() {
		strategy.execute();		
	}

	@Override
	public Map<String, String> getMapValues() {
		return strategy.getMapValues();
	}

	@Override
	public Map<String, String> getNamespaces() {
		return strategy.getNamespaces();
	}

	@Override
	public Map<String, AtomicInteger> getElementNameCount() {
		return strategy.getElementNameCount();
	}
	
}
