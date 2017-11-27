/**
 * 
 */
package org.EDM.Transformations.formats.a2a;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

		A2A2EDM a2a =
				new A2A2EDM(UUID.randomUUID().toString(),
				(A2AType) jxb.getObject(), properties(), System.out);

		assertNotNull(a2a);
		a2a.marshal(StandardCharsets.UTF_8, true);
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
