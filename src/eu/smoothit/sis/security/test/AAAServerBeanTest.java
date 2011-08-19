package eu.smoothit.sis.security.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.security.access.AAAServerLocal;
import eu.smoothit.sis.security.login.AccountCreator;

public class AAAServerBeanTest extends TestCase {
	protected Logger logger = Logger.getLogger(AAAServerBeanTest.class);

	public AAAServerBeanTest() {
	}

	public void testBasicFunctionality() {

		AAAServerLocal dao = null;

		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.LocalInitialContextFactory");
		properties.put("sisds", "new://Resource?type=DataSource");
		properties.put("sisds.JdbcDriver", "com.mysql.jdbc.Driver");
		properties.put("sisds.JdbcUrl", "jdbc:mysql://localhost/sis");
		properties.put("sisds.UserName", "root");
		properties.put("sisds.Password", "root");

		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext(properties);
			dao = (AAAServerLocal) initialContext.lookup("AAAServerBeanLocal");

		} catch (NamingException e1) {
			logger.fatal("Unable to find AAAServerBeanLocal in JNDI", e1);
			return;
		}

		SisDAOFactory factory = SisDAOFactory.getFactory();

		ILogEntryDAO logEntryDao = factory.createLogEntryDAO();
		logEntryDao.removeAll();

		dao.addLogEntry("test", "auth", "ok");
		assertTrue(dao.getLogEntries().size() == 1);

		IUserDAO userDao = factory.createUserDAO();
		userDao.removeAll();

		AccountCreator.addAcount("admin", "admin", "JBossAdmin", "Roles");

		assertTrue(dao.checkRequestorID("admin", "admin"));
		assertTrue(dao.isCallerInRole("admin", "JBossAdmin"));
		assertFalse(dao.checkRequestorID("admin", "gregre"));
		assertFalse(dao.checkRequestorID("gregre", "amind"));
		assertFalse(dao.isCallerInRole("admin", "MockRole"));
		assertFalse(dao.isCallerInRole("test", "JBossAdmin"));
	}
}
