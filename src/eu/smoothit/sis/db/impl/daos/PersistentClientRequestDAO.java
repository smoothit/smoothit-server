/**
 * 
 */
package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IClientRequestDAO;
import eu.smoothit.sis.db.impl.entities.ClientRequestEntry;
import eu.smoothit.sis.db.impl.entities.SwarmEntityEntry;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

/**
 * @author christian
 * 
 */
public class PersistentClientRequestDAO extends JpaDao<Long, ClientRequestEntry> implements IClientRequestDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentComponentConfigDAO.class);


	private static String clientRequestTable = "ClientRequestEntry";


	public PersistentClientRequestDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientRequestEntry> get(ClientRequestEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "select h from " + clientRequestTable + " h "+ conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

	@Override
	public void remove(ClientRequestEntry entry) {
		
		List<ClientRequestEntry> requests = this.get(entry);
		for (ClientRequestEntry req : requests) {
			for (SwarmEntityEntry se : req.getSwarms()) {
				if (se.getClientRequests() != null) {
					req.getSwarms().remove(requests.get(0));
					
					pm.updateAndNotify(req, new DBNotifyMessage(
							PersistentComponentConfigDAO.class.getSimpleName(),
							DBNotifyMessage.ACTION.UPDATE));
				}
			}
		}
		pm.removeAndNotify(requests.get(0), new DBNotifyMessage(
				PersistentComponentConfigDAO.class.getSimpleName(),
				DBNotifyMessage.ACTION.REMOVE));

//		String conditions = createQueryConditionsFromParameters(entry);
//		String query = "from " + clientRequestTable + conditions;
//		logger.debug("Resulting query of removeProperties() " + query);
//		pm.removeSpecifiedObjects(query, new DBNotifyMessage(
//				PersistentComponentConfigDAO.class.getSimpleName(),
//				DBNotifyMessage.ACTION.REMOVE));

	}

	private String createQueryConditionsFromParameters(ClientRequestEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getIP() != null) {
			ret.append(" h.ip='" + entry.getIP() + "' AND");
		}
		if (entry.getPort() != null) {
			ret.append(" h.port=" + entry.getPort() + " AND");
		}
		if (entry.getLastUpdate() != null) {
			ret.append(" h.lastupdate=" + entry.getLastUpdate() + " AND");
		}
		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

}
