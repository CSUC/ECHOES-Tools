/**
 * 
 */
package org.Recollect.Core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 *
 */
public class Garbage {

	private static Logger logger = LogManager.getLogger(Garbage.class);

	public static void gc() {
		Runtime garbage = Runtime.getRuntime();
		logger.info(String.format("Free memory: %s", garbage.freeMemory()));
		garbage.gc();
		logger.info(String.format("Free memory: %s", garbage.freeMemory()));
	}
}
