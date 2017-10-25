/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.UserDAO;
import org.Morphia.Core.entities.User;
import org.Morphia.Core.utils.Password;
import org.Morphia.Core.utils.Role;
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
public class UserDAOImplTest extends TestCase {

	private static Logger logger = LogManager.getLogger(UserDAOImplTest.class);

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private UserDAO userdao = new UserDAOImpl(User.class, echoes.getDatastore());

	private User user = new User();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		user.setEmail("pir@csuc.cat");
		Password psswd = new Password("md5", "pir@csuc.cat");
		user.setPassword(psswd.getSecurePassword());
		user.setDigest(psswd.getAlgorithm());
		user.setRole(Role.Admin);
		
		echoes.getDatastore().save(user);
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.UserDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		List<User> result = userdao.findAll();

		result.stream().forEach(u -> {
			logger.info(u.toJson());
		});

		assertNotNull(result);
		assertEquals(1, result.size());

	}

	/**
	 * Test method for
	 * {@link org.Morphia.Core.dao.impl.UserDAOImpl#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		UserDAO userdao = new UserDAOImpl(User.class, echoes.getDatastore());

		User result = userdao.findById(user.getId());

		logger.info(result.toJson());
		assertNotNull(result);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
//		if (Objects.nonNull(echoes.getDatastore()))
//			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}

}
