package org.Morphia.Core.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.HarvestStatus;
import org.Morphia.Core.dao.HarvestedCollectionConfigDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.junit.Test;

import junit.framework.TestCase;

public class HarvestedCollectionDAOImplTest extends TestCase {

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private String uuid = UUID.randomUUID().toString();
	
	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		
		HarvestedCollectionConfig harvested = new HarvestedCollectionConfig();
		harvested.setId(uuid);
		
		harvested.setOaisetid("oai_set_id");
		harvested.setOaisource("oai_source");
		harvested.setHarcestmessage(HarvestStatus.READY.getDescription());
		harvested.setMetadataconfig("metadataPrefix");
		harvested.setHarveststatus(HarvestStatus.READY.getValue());
		harvested.setHarveststarttime(new Date());
		harvested.setLastharvested(null);
		harvested.setXsdconfig(null);
		
		echoes.getDatastore().save(harvested);
	}
	
	
	@Test
	public void testFindAll() {
		HarvestedCollectionConfigDAO harvestedDAO = 
				new HarvestedCollectionConfigDAOImpl(HarvestedCollectionConfig.class, echoes.getDatastore());
		
		List<HarvestedCollectionConfig> result = harvestedDAO.findAll();
		
		result.forEach(harvestedCollectionConfig->{
			System.out.println(harvestedCollectionConfig.toString());
		});
		
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testFindById() {
		HarvestedCollectionConfigDAO harvestedDAO = 
				new HarvestedCollectionConfigDAOImpl(HarvestedCollectionConfig.class, echoes.getDatastore());
		
		HarvestedCollectionConfig result = harvestedDAO.findById(uuid);		
		System.out.println(result.toString());
		
		assertNotNull(result);
		
	}

	@Override
	protected void tearDown() throws Exception {		
		super.tearDown();
		if(Objects.nonNull(echoes.getDatastore()))
			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}
}
