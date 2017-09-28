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
public interface ParserFactory {
	
	public ParserStrategy instanceFactory();
	
	public Map<String,String> getMapValues();
	public Map<String,String> getNamespaces();
	public Map<String, AtomicInteger> getElementNameCount();
	
	public String getDuration();
	
}