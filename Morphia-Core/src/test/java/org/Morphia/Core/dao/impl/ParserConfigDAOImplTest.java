/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.ParserConfigDAO;
import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.User;
import org.Morphia.Core.utils.HarvestStatus;
import org.Morphia.Core.utils.ParserType;
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

	private User user = new User("pir@csuc.cat");
	private ParserConfig config = new ParserConfig();

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
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
		if (Objects.nonNull(echoes.getDatastore()))
			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserConfigDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		List<ParserConfig> result = dao.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserConfigDAOImpl#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		ParserConfig result = dao.findById(config.getId());

		assertNotNull(result);

		logger.info(result.toJson());
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserConfigDAOImpl#findByUser(org.Morphia.Core.entities.User)}.
	 */
	@Test
	public void testFindByUser() {
		List<ParserConfig> result = dao.findByUser(user);

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserConfigDAOImpl#findByUser(java.lang.String)}.
	 */
	@Test
	public void testFindByUserId() {
		List<ParserConfig> result = dao.findByUser(user.getId());

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

}
