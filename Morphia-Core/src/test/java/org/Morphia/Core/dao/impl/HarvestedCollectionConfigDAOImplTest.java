/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.utils.HarvestStatus;
import org.Morphia.Core.dao.HarvestedCollectionConfigDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
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
public class HarvestedCollectionConfigDAOImplTest extends TestCase {

	private static Logger logger = LogManager.getLogger(HarvestedCollectionConfigDAOImplTest.class);

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private HarvestedCollectionConfigDAO harvestedDAO = new HarvestedCollectionConfigDAOImpl(
			HarvestedCollectionConfig.class, echoes.getDatastore());

	private HarvestStatus status = HarvestStatus.READY;

	private HarvestedCollectionConfig harvested = new HarvestedCollectionConfig();
	private User user = new User("pir@csuc.cat");
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();

		harvested.setOaisetid("oai_set_id");
		harvested.setOaisource("oai_source");
		harvested.setHarcestmessage(status.getDescription());
		harvested.setMetadataconfig("metadataPrefix");
		harvested.setHarveststatus(status.getValue());
		harvested.setHarveststarttime(new Date());
		harvested.setLastharvested(null);
		harvested.setXsdconfig(null);

		user.setPassword("1234");
		user.setDigest("digest");
		harvested.setUser_id(user);

		echoes.getDatastore().save(Arrays.asList(user, harvested));
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
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findAll()}.
	 * @ 
	 */
	@Test
	public void testFindAll() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {logger.info(h.toJson());});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findById(java.lang.String)}.
	 * @ 
	 */
	@Test
	public void testFindById() {
		HarvestedCollectionConfig result = harvestedDAO.findById(harvested.getId());

		assertNotNull(result);
		assertEquals(harvested.getId(), result.getId());
		
		logger.info(result.toJson());	
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findAll(org.Morphia.Core.entities.User)}.
	 * @ 
	 */
	@Test
	public void testFindAllUser() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findAll(user);
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertTrue(user.equals(h.getUser_id()));
			logger.info(h.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findAll(java.lang.String)}.
	 * @ 
	 */
	@Test
	public void testFindAllString() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findAll(user.getId());

		result.forEach(h -> {
			assertEquals(user.getId(), h.getUser_id().getId());
			logger.info(h.toJson());
		});
				
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByStatus(org.Morphia.Core.dao.HarvestStatus)}.
	 * @ 
	 */
	@Test
	public void testFindByStatusHarvestStatus() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByStatus(status);
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {			
			assertEquals(status.getValue(), h.getHarveststatus());
			logger.info(h.toJson());
		});
				
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByStatus(org.Morphia.Core.dao.HarvestStatus, org.Morphia.Core.entities.User)}.
	 * @ 
	 */
	@Test
	public void testFindByStatusHarvestStatusUser() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByStatus(status, user);
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {			
			assertEquals(status.getValue(), h.getHarveststatus());
			assertTrue(user.equals(h.getUser_id()));
			logger.info(h.toJson());
		});
				
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByStatus(org.Morphia.Core.dao.HarvestStatus, java.lang.String)}.
	 * @ 
	 */
	@Test
	public void testFindByStatusHarvestStatusString() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByStatus(status, user.getId());
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {			
			assertEquals(status.getValue(), h.getHarveststatus());
			assertEquals(user.getId(), h.getUser_id().getId());
			logger.info(h.toJson());
		});
		
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByXsdConfig(java.lang.String)}.
	 * @ 
	 */
	@Test
	public void testFindByXsdConfigString() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByXsdConfig(null);
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {			
			assertNull(h.getXsdconfig());
			logger.info(h.toJson());
		});
		
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByXsdConfig(java.lang.String, org.Morphia.Core.entities.User)}.
	 * @ 
	 */
	@Test
	public void testFindByXsdConfigStringUser() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByXsdConfig(null, user);
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertEquals(null, h.getXsdconfig());
			assertTrue(user.equals(h.getUser_id()));			
			logger.info(h.toJson());
		});
		
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByXsdConfig(java.lang.String, java.lang.String)}.
	 * @ 
	 */
	@Test
	public void testFindByXsdConfigStringString() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByXsdConfig(null, user.getId());
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertEquals(null, h.getXsdconfig());
			assertEquals(user.getId(), h.getUser_id().getId());
			logger.info(h.toJson());
		});
		
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByUser(org.Morphia.Core.entities.User)}.
	 * @ 
	 */
	@Test
	public void testFindByUserUser() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByUser(user);
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertTrue(user.equals(h.getUser_id()));
			logger.info(h.toJson());		
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl#findByUser(java.lang.String)}.
	 * @ 
	 */
	@Test
	public void testFindByUserString() {
		List<HarvestedCollectionConfig> result = harvestedDAO.findByUser(user.getId());
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertEquals(user.getId(), h.getUser_id().getId());
			logger.info(h.toJson());
			});		
	}

}
