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
import org.Morphia.Core.dao.HarvestedItemsDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.entities.HarvestedItems;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author amartinez
 *
 */
public class HarvestedItemsDAOImplTest extends TestCase {

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private String uuid = UUID.randomUUID().toString();
	private String id = "oai_id";
	
	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		HarvestedCollectionConfig harvested = new HarvestedCollectionConfig();
		harvested.setId(uuid);
		
		harvested.setOaisetid("col_10803_78");
		harvested.setOaisource("http://tdx.cat/oai/request");
		harvested.setHarcestmessage(HarvestStatus.READY.getDescription());
		harvested.setMetadataconfig("oai_dc");
		harvested.setHarveststatus(HarvestStatus.READY.getValue());
		harvested.setHarveststarttime(new Date());
		harvested.setLastharvested(null);
		harvested.setXsdconfig(null);
		
		HarvestedItems items = new HarvestedItems();
		items.setId(id);
		items.setLastharavested(new Date());
		items.setHarvestedcollection(harvested);
		
		echoes.getDatastore().save(Arrays.asList(harvested, items));
		
		
	}
	
	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedItemsDaoImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		HarvestedItemsDAO itemsDAO = 
				new HarvestedItemsDAOImpl(HarvestedItems.class, echoes.getDatastore());
		
		
		List<HarvestedItems> result = itemsDAO.findAll();
		result.stream().forEach(harvesteditems->{
			System.out.println(harvesteditems);
		});
		
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedItemsDaoImpl#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		HarvestedItemsDAO itemsDAO = 
				new HarvestedItemsDAOImpl(HarvestedItems.class, echoes.getDatastore());
		
		HarvestedItems result = itemsDAO.findById(id);
		
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
