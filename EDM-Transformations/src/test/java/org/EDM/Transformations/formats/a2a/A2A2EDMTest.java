/**
 * 
 */
package org.EDM.Transformations.formats.a2a;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.EDM.Transformations.deserialize.JaxbUnmarshal;
import org.junit.Test;

import junit.framework.TestCase;
import nl.mindbus.a2a.A2AType;

/**
 * @author amartinez
 *
 */
public class A2A2EDMTest extends TestCase {

	/**
	 * Test method for
	 * {@link org.EDM.Transformations.formats.a2a.A2A2EDM#marshal(java.nio.charset.Charset, boolean)}.
	 */
	@Test
	public void testMarshal() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("a2a/a2a.xml").getFile());

		assertTrue(file.exists());
		JaxbUnmarshal jxb = new JaxbUnmarshal(file, new Class[] { A2AType.class });
		assertNotNull(jxb.getObject());

		new A2A2EDM("00000026-feb0-11e2-8fe8-3860770fff49:000207c4-26bd-11e3-a525-3cd92befe4f8",
				(A2AType) jxb.getObject(), properties(), System.out).marshal(StandardCharsets.UTF_8, true);
	}

	public Map<String, String> properties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties = new HashMap<String, String>();
		properties.put("edmType", "IMAGE");
		properties.put("provider", "provider");
		properties.put("dataProvider", "dataProvider");
		properties.put("language", "language");
		properties.put("rights", "rights");
		properties.put("set", "set");

		return properties;
	}
}
