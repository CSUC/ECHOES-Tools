/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.HarvestStatus;
import org.Morphia.Core.dao.ParserConfigDAO;
import org.Morphia.Core.dao.ParserType;
import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author amartinez
 *
 */
public class ParserConfigDAOImplTest extends TestCase {
	
	private static Logger logger = LogManager.getLogger(ParserConfigDAOImplTest.class);
	
	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private ParserConfigDAO dao = new ParserConfigDAOImpl(ParserConfig.class, echoes.getDatastore());
	
	private User user = new User();
	private String user_id = UUID.randomUUID().toString();
	
	private ParserConfig config = new ParserConfig();
	private String config_id = UUID.randomUUID().toString();
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		user.setId(user_id);
		
		config.setId(config_id);
		config.setStatus(HarvestStatus.READY.getValue());
		config.setSource("http://csuc.cat/");
		config.setStarttime(new Date());
		config.setType(ParserType.URL.toString());		
		config.setUserid(user);
		
		echoes.getDatastore().save(Arrays.asList(user, config));
		
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		if(Objects.nonNull(echoes.getDatastore()))
			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserConfigDAOImplTest#findAll()}.
	 */
	@Test
	public void testFindAll() {		
		List<ParserConfig> result = dao.findAll();
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(p->{logger.info(p.toJson());});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserConfigDAOImplTest#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		ParserConfig result = dao.findById(config_id);
		
		assertNotNull(result);
		
		logger.info(result.toJson());
	}

}
