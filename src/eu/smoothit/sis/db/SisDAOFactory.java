package eu.smoothit.sis.db;

import org.apache.log4j.Logger;

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
import eu.smoothit.sis.db.impl.factories.JpaDAOFactory;
import eu.smoothit.sis.db.impl.utils.SISDBNotifier;

/**
 * Singleton Factory, that creates DAOs.
 * 
 * When used outside of JBoss, the persistentContext must be setup before usage!
 * (By calling JpaDAOFactory.setPersistenceConfiguration(...))
 * 
 * 
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */
public abstract class SisDAOFactory {

	protected final static Logger logger = Logger
			.getLogger(SisDAOFactory.class);

	/**
	 * The Factory Singleton to be used by clients of the SIS DB Module
	 */
	private static SisDAOFactory singleton;

	protected SisDAOFactory() {
		super();
	}

	public static synchronized SisDAOFactory getFactory() {
		if (singleton == null)
			singleton = new JpaDAOFactory();// create default one
		return singleton;
	}

	/**
	 * Creates LinkConfigDAO Note: Different Instances share same configuration,
	 * because of common database/ persistentContext
	 * 
	 * @return a new LinkConfigDAO
	 */
	public abstract ILinkConfigDAO createLinkConfigDAO();

	/**
	 * Creates IPRangeDAO Note: Different Instances share same configuration,
	 * because of common database/ persistentContext
	 * 
	 * @return a new IPRangeDAO
	 */
	public abstract IPRangeDAO createIPRangeDAO();

	/**
	 * Creates ComponentConfigDAO Note: Different Instances share same
	 * configuration, because of common database/ persistentContext
	 * 
	 * @return a new ComponentConfigDAO
	 */
	public abstract IComponentConfigDAO createComponentConfigDAO();

	/**
	 * Creates an IUserDAO
	 * 
	 * @return a new IUserDAO for accessing data from the security_user table
	 */
	public abstract IUserDAO createUserDAO();

	/**
	 * Creates an ILogEntryDAO
	 * 
	 * @return a new ILogEntryDAO for accessing data from the security_logs
	 *         table
	 */
	public abstract ILogEntryDAO createLogEntryDAO();

	/**
	 * Creates an IUserRoleDAO
	 * 
	 * @return a new IUserRoleDAO for accessing data from the security_roles
	 *         table
	 */
	public abstract IUserRoleDAO createUserRoleDAO();

	/**
	 * Creates an ISwarmRequestDAO
	 * 
	 * @return a new ISwarmRequestDAO for accessing data from the
	 *         config_swarm_entity_entry table
	 */
	public abstract IClientRequestDAO createClientRequestDOA();

	public abstract ISwarmDAO createSwarmDAO();

	/**
	 * Creates an IPeerStatusDAO
	 * 
	 * @return a new IPeerStatusDAO for accessing data from the
	 *         monitoring_peer_status_entry table
	 */
	public abstract IPeerStatusDAO createPeerStatusDAO();

	/**
	 * Creates an IConnectedPeerStatusDAO
	 * 
	 * @return a new IConnectedPeerStatusDAO for accessing data from the
	 *         monitoring_connected_peer_status_entry table
	 */
	public abstract IConnectedPeerStatusDAO createConnectedPeerStatusDAO();

	/**
	 * @return an instance of the SISDBNotifier class
	 */
	public abstract SISDBNotifier createSISDBNotifier();

	public abstract IClientSwarmInfoDAO createClientSwarmInfoDAO();

	public abstract IPeerStatisticsDAO createPeerStatisticsDAO();
	
	public abstract IHAPEntryDAO createHAPEntryDAO();

}
