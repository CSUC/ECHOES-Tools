package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.HarvestStatus;
import org.Morphia.Core.dao.HarvestedCollectionConfigDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import junit.framework.TestCase;

public class HarvestedCollectionConfigDAOImplTest extends TestCase {

	private static Logger logger = LogManager.getLogger(HarvestedCollectionConfigDAOImplTest.class);
	
	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private HarvestedCollectionConfigDAO harvestedDAO = 
			new HarvestedCollectionConfigDAOImpl(HarvestedCollectionConfig.class, echoes.getDatastore());
	
	private String harvested_uuid = UUID.randomUUID().toString();
	private String user_uuid = UUID.randomUUID().toString();
	
	private HarvestedCollectionConfig harvested = new HarvestedCollectionConfig();
	private User user = new User();
	
	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		
		harvested.setId(harvested_uuid);
		
		harvested.setOaisetid("oai_set_id");
		harvested.setOaisource("oai_source");
		harvested.setHarcestmessage(HarvestStatus.READY.getDescription());
		harvested.setMetadataconfig("metadataPrefix");
		harvested.setHarveststatus(HarvestStatus.READY.getValue());
		harvested.setHarveststarttime(new Date());
		harvested.setLastharvested(null);
		harvested.setXsdconfig(null);
		
		user.setId(user_uuid);
		user.setEmail("pir@csuc.cat");
		user.setPassword("1234");
		user.setDigest("digest");
		harvested.setUser_id(user);
		
		echoes.getDatastore().save(Arrays.asList(user, harvested));
	}
	
	
	@Test
	public void testFindAll() {		
		List<HarvestedCollectionConfig> result = harvestedDAO.findAll();
		
		result.forEach(h->{logger.info(h.toJson());});
		
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testFindById() {
		HarvestedCollectionConfig result = harvestedDAO.findById(harvested_uuid);
		
		logger.info(result.toJson());
		
		assertNotNull(result);
		
	}

	@Override
	protected void tearDown() throws Exception {		
		super.tearDown();
		if(Objects.nonNull(echoes.getDatastore()))
			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}
}
