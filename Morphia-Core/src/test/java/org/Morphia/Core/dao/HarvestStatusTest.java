/**
 * 
 */
package org.Morphia.Core.dao;

import org.Morphia.Core.utils.HarvestStatus;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author amartinez
 *
 */
public class HarvestStatusTest extends TestCase {

	/**
	 * Test method for {@link org.Morphia.Core.dao.HarvestStatus#getValue()}.
	 */
	@Test
	public void testGetValue() {		
		assertEquals(0, HarvestStatus.READY.getValue());
		assertEquals(1, HarvestStatus.BUSY.getValue());
		assertEquals(2, HarvestStatus.QUEUED.getValue());
		assertEquals(3, HarvestStatus.OAI_ERROR.getValue());
		assertEquals(-1, HarvestStatus.UNKNOWN_ERROR.getValue());
		
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.HarvestStatus#getDescription()}.
	 */
	@Test
	public void testGetDescription() {		
		assertEquals("READY", HarvestStatus.READY.getDescription());
		assertEquals("BUSY", HarvestStatus.BUSY.getDescription());
		assertEquals("QUEUED", HarvestStatus.QUEUED.getDescription());
		assertEquals("OAI_ERROR", HarvestStatus.OAI_ERROR.getDescription());
		assertEquals("UNKNOWN_ERROR", HarvestStatus.UNKNOWN_ERROR.getDescription());
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.HarvestStatus#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("READY", HarvestStatus.READY.toString());
		assertEquals("BUSY", HarvestStatus.BUSY.toString());
		assertEquals("QUEUED", HarvestStatus.QUEUED.toString());
		assertEquals("OAI_ERROR", HarvestStatus.OAI_ERROR.toString());
		assertEquals("UNKNOWN_ERROR", HarvestStatus.UNKNOWN_ERROR.toString());
	}

}
