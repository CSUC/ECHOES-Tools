package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.HarvestStatus;
import org.Morphia.Core.dao.ParserResultsDAO;
import org.Morphia.Core.dao.ParserType;
import org.Morphia.Core.entities.Namespace;
import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.ParserResults;
import org.Morphia.Core.entities.ParserResultsJSON;
import org.Morphia.Core.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ParserResultsDAOImplTest extends TestCase {

	private static Logger logger = LogManager.getLogger(ParserResultsDAOImplTest.class);

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private ParserResultsDAO dao = new ParserResultsDAOImpl(ParserResults.class, echoes.getDatastore());

	private String parser_results_uuid = UUID.randomUUID().toString();
	private String parser_config_uuid = UUID.randomUUID().toString();
	private String user_uuid = UUID.randomUUID().toString();

	private ParserConfig config = new ParserConfig();
	private ParserResults results = new ParserResults();
	private User user = new User();

	@Before
	protected void setUp() throws Exception {
		super.setUp();

		config.setId(parser_config_uuid);
		config.setStatus(HarvestStatus.READY.getValue());
		config.setSource("http://csuc.cat/");
		config.setStarttime(new Date());
		config.setType(ParserType.URL.toString());

		user.setId(user_uuid);
		user.setEmail("pir@csuc.cat");
		user.setPassword("1234");
		user.setDigest("digest");
		config.setUserid(user);
		;

		results.setId(parser_results_uuid);
		results.setParser_config_id(config);
		results.setNamespace(Arrays.asList(new Namespace("http://www.w3.org/2001/XMLSchema-instance", "xsi")));
		results.setJson(Arrays.asList(new ParserResultsJSON("ns1:date[@calendar and @era and @normal]", 173,
				"/ns1:ead[@audience]/ns1:archdesc[@level and @type]/ns1:dsc[@type]/ns1:c01[@level]/ns1:c02[@level and @otherlevel]/ns1:c03[@level]/ns1:accessrestrict[@type]/ns1:p/ns1:date[@calendar and @era and @normal]")));

		echoes.getDatastore().save(Arrays.asList(user, config, results));
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		if (Objects.nonNull(echoes.getDatastore()))
			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}

	@Test
	public void testFindAll() {
		List<ParserResults> result = dao.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());

		result.forEach(p -> {
			logger.info(p.toJson());
		});
	}

	@Test
	public void testFindById() {
		ParserResults result = dao.findById(parser_results_uuid);

		assertNotNull(result);

		logger.info(result.toJson());
	}

}
