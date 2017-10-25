/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.ParserResultsDAO;
import org.Morphia.Core.entities.Namespace;
import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.ParserResults;
import org.Morphia.Core.entities.ParserResultsJSON;
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
public class ParserResultsDAOImplTest extends TestCase {

	private static Logger logger = LogManager.getLogger(ParserResultsDAOImplTest.class);

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private ParserResultsDAO dao = new ParserResultsDAOImpl(ParserResults.class, echoes.getDatastore());

	private ParserConfig config = new ParserConfig();
	private ParserResults results = new ParserResults();
	private User user = new User("pir@csuc.cat");
	
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

		user.setPassword("1234");
		user.setDigest("digest");
		config.setUserid(user);
	
		
		results.setParser_config_id(config);
		results.setNamespace(Arrays.asList(new Namespace("http://www.w3.org/2001/XMLSchema-instance", "xsi")));
		results.setJson(Arrays.asList(new ParserResultsJSON("ns1:date[@calendar and @era and @normal]", 173,
				"/ns1:ead[@audience]/ns1:archdesc[@level and @type]/ns1:dsc[@type]/ns1:c01[@level]/ns1:c02[@level and @otherlevel]/ns1:c03[@level]/ns1:accessrestrict[@type]/ns1:p/ns1:date[@calendar and @era and @normal]")));

		echoes.getDatastore().save(Arrays.asList(user, config, results));
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
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserResultsDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		List<ParserResults> result = dao.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserResultsDAOImpl#findAll(org.Morphia.Core.entities.ParserConfig)}.
	 */
	@Test
	public void testFindAllParserConfig() {
		List<ParserResults> result = dao.findAll(config);

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserResultsDAOImpl#findAll(java.lang.String)}.
	 */
	@Test
	public void testFindAllString() {
		List<ParserResults> result = dao.findAll(config.getId());

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.ParserResultsDAOImpl#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		ParserResults result = dao.findById(results.getId());

		assertNotNull(result);

		logger.info(result.toJson());
	}

}
