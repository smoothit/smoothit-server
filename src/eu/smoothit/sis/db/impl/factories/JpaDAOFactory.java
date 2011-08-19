package eu.smoothit.sis.db.impl.factories;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IClientRequestDAO;
import eu.smoothit.sis.db.api.daos.IClientSwarmInfoDAO;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IConnectedPeerStatusDAO;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.api.daos.ILinkConfigDAO;
import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.db.api.daos.ISwarmDAO;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.daos.PersistentClientRequestDAO;
import eu.smoothit.sis.db.impl.daos.PersistentClientSwarmInfoDAO;
import eu.smoothit.sis.db.impl.daos.PersistentComponentConfigDAO;
import eu.smoothit.sis.db.impl.daos.PersistentConnectedPeerStatusDAO;
import eu.smoothit.sis.db.impl.daos.PersistentHAPEntryDAO;
import eu.smoothit.sis.db.impl.daos.PersistentIPRangeDAO;
import eu.smoothit.sis.db.impl.daos.PersistentLinkConfigDAO;
import eu.smoothit.sis.db.impl.daos.PersistentLogEntryDAO;
import eu.smoothit.sis.db.impl.daos.PersistentPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.daos.PersistentPeerStatusDAO;
import eu.smoothit.sis.db.impl.daos.PersistentSwarmDAO;
import eu.smoothit.sis.db.impl.daos.PersistentUserDAO;
import eu.smoothit.sis.db.impl.daos.PersistentUserRoleDAO;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;
import eu.smoothit.sis.db.impl.utils.SISDBNotifier;

/**
 * Concrete Factory, whose DAOs use JPA/Hibernate for persistence
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */
public class JpaDAOFactory extends SisDAOFactory {

	private static SISDBNotifier notifierInstance;

	// private final static String PERSISTENCE_FILE_JUNIT = "resources"
	// + File.separator + "META-INF" + File.separator
	// + "hibernate.cfg.xml";

	private Properties jBossEnv;

	private IPersistenceManager manager;

	/**
	 * Init factory
	 */
	public JpaDAOFactory() {
		super();
	}

	private synchronized IPersistenceManager getPersistenceManager() {

		if (this.manager != null) {
			return this.manager;
		}

		if (jBossEnv == null) {
			// in case that we are not in the JBoss Env try to init bean inside
			// OpenEJB
			jBossEnv = new Properties();
			jBossEnv.put("java.naming.factory.initial",
					"org.jnp.interfaces.NamingContextFactory");
			jBossEnv.put("java.naming.provider.url", "localhost:1099");
			jBossEnv.put("java.naming.factory.url.pkgs",
					"org.jboss.naming:org.jnp.interfaces");
		}
		try {
			Context ctx = new InitialContext(jBossEnv);
			manager = (IPersistenceManager) ctx
					.lookup("sis/PersistenceManager/local");
			ctx.close();
			logger.debug("Creating Persistence Manager in JBoss Env");
		} catch (NamingException e) {

			// in case that we are not in the JBoss Env try to init bean inside
			// OpenEJB
			logger.debug("Creating Persistence Manager in OpenEJB Env");
			Properties properties = new Properties();
			properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.openejb.client.LocalInitialContextFactory");
			properties.put("sisds", "new://Resource?type=DataSource");
			properties.put("sisds.JdbcDriver", "com.mysql.jdbc.Driver");
			properties.put("sisds.JdbcUrl", "jdbc:mysql://localhost/sis");
			properties.put("sisds.UserName", "root");
			properties.put("sisds.Password", "root");

			// properties.put("sisdb", "new://Resource?type=DataSource");
			// properties.put("sisdb.JdbcDriver", "org.hsqldb.jdbcDriver");
			// properties.put("sisdb.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
			InitialContext initialContext = null;
			try {
				initialContext = new InitialContext(properties);
				Object object = initialContext
						.lookup("PersistenceManagerLocal");
				if (object instanceof IPersistenceManager) {
					manager = (IPersistenceManager) object;
				} else {
					e.printStackTrace();
				}
			} catch (NamingException e1) {
				// close initail context if not null
				e1.printStackTrace();
				if (initialContext != null) {
					try {
						initialContext.close();
					} catch (NamingException e2) {
						e2.printStackTrace();
					}
				}
			}

		}
		return manager;
	}

	@Override
	public synchronized IComponentConfigDAO createComponentConfigDAO() {
		return new PersistentComponentConfigDAO(getPersistenceManager());
	}

	@Override
	public synchronized IPRangeDAO createIPRangeDAO() {
		return new PersistentIPRangeDAO(getPersistenceManager());
	}

	@Override
	public synchronized ILinkConfigDAO createLinkConfigDAO() {
		return new PersistentLinkConfigDAO(getPersistenceManager());
	}


	public synchronized SISDBNotifier createSISDBNotifier() {
		if (notifierInstance == null) {
			notifierInstance = new SISDBNotifier();
		}
		return notifierInstance;
	}

	@Override
	public synchronized IUserDAO createUserDAO() {
		return new PersistentUserDAO(getPersistenceManager());
	}

	@Override
	public synchronized ILogEntryDAO createLogEntryDAO() {
		return new PersistentLogEntryDAO(getPersistenceManager());
	}

	@Override
	public synchronized IUserRoleDAO createUserRoleDAO() {
		return new PersistentUserRoleDAO(getPersistenceManager());
	}

	@Override
	public synchronized IClientRequestDAO createClientRequestDOA() {
		return new PersistentClientRequestDAO(getPersistenceManager());
	}

	@Override
	public synchronized IPeerStatusDAO createPeerStatusDAO() {
		return new PersistentPeerStatusDAO(getPersistenceManager());
	}

	@Override
	public ISwarmDAO createSwarmDAO() {
		return new PersistentSwarmDAO(getPersistenceManager());
	}

	@Override
	public IConnectedPeerStatusDAO createConnectedPeerStatusDAO() {
		return new PersistentConnectedPeerStatusDAO(getPersistenceManager());
	}

	@Override
	public IClientSwarmInfoDAO createClientSwarmInfoDAO() {
		return new PersistentClientSwarmInfoDAO(getPersistenceManager());
	}

	@Override
	public IPeerStatisticsDAO createPeerStatisticsDAO() {
		return new PersistentPeerStatisticsDAO(getPersistenceManager());
	}

	@Override
	public IHAPEntryDAO createHAPEntryDAO() {
		return new PersistentHAPEntryDAO(getPersistenceManager());
	}

}
