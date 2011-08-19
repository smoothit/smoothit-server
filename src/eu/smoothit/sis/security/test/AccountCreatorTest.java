package eu.smoothit.sis.security.test;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.AfterClass;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.security.login.AccountCreator;

/**
 * Tests functionality of AccountCreator class.
 * @author Tomasz Stradomski
 *
 */
public class AccountCreatorTest extends TestCase {

	protected Logger logger = Logger.getLogger(AccountCreatorTest.class);

	public AccountCreatorTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IUserDAO dao = factory.createUserDAO();

		// firstly clean appropriate tables
		dao.removeAll();

		// try to add account 
		AccountCreator.addAcount("admin", "admin", "JBossAdmin", "Roles");
		assertTrue(dao.countAll() == 1);

		dao.removeAll();

		
		// try to add account twice - second operation should NOT finish successfully
		AccountCreator.addAcount("admin", "admin", "JBossAdmin", "Roles" );
		assertTrue(dao.countAll() == 1);

		assertFalse(AccountCreator.addAcount("admin", "admin", "JBossAdmin", "Roles"));
		dao.removeAll();
	}

	
	@AfterClass
	public void tearDown(){
//		Add default admin account 
		AccountCreator.addAcount("admin", "admin", "JBossAdmin", "Roles");
	}

}
