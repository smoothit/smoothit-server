/**
 * 
 */
package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IConnectedPeerStatusDAO;
import eu.smoothit.sis.db.impl.entities.ConnectedPeerStatusEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

/**
 * @author christian
 * 
 */
public class PersistentConnectedPeerStatusDAO extends
		JpaDao<Long, ConnectedPeerStatusEntry> implements
		IConnectedPeerStatusDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentComponentConfigDAO.class);

	private static String connectedPeerStatusTable = "ConnectedPeerStatusEntry";

	public PersistentConnectedPeerStatusDAO(
			IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConnectedPeerStatusEntry> get(ConnectedPeerStatusEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "from " + connectedPeerStatusTable + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

	@Override
	public void remove(ConnectedPeerStatusEntry entry) {

		List<ConnectedPeerStatusEntry> cps = this.get(entry);
		for (ConnectedPeerStatusEntry cp : cps) {
			PeerStatusEntry peerStatus = cp.getStatus();
			if (peerStatus.getList() != null) {
				logger.debug("Vorher: " + peerStatus.getList().size());
				peerStatus.getList().remove(cps.get(0));
				logger.debug("Nachher: " + peerStatus.getList().size());
				pm.updateAndNotify(peerStatus, new DBNotifyMessage(
						PersistentComponentConfigDAO.class.getSimpleName(),
						DBNotifyMessage.ACTION.UPDATE));
				
			}
		}
	
		pm.removeAndNotify(cps.get(0), new DBNotifyMessage(
				PersistentComponentConfigDAO.class.getSimpleName(),
				DBNotifyMessage.ACTION.REMOVE));
	}

	private String createQueryConditionsFromParameters(
			ConnectedPeerStatusEntry entry) {
		StringBuffer ret = new StringBuffer();

		if (entry.getAddr() != null) {
			ret.append(" addr='" + entry.getAddr() + "' AND");
		}
		if (entry.getDown_rate() != null) {
			ret.append(" down_rate=" + entry.getDown_rate() + " AND");
		}
		if (entry.getDown_str() != null) {
			ret.append(" down_str='" + entry.getDown_str() + "' AND");
		}
		if (entry.getDown_total() != null) {
			ret.append(" down_total=" + entry.getDown_total() + " AND");
		}
		if (entry.getG2g() != null) {
			ret.append(" g2g='" + entry.getG2g() + "' AND");
		}
		if (entry.getG2g_score() != null) {
			ret.append(" g2g_score=" + entry.getG2g_score() + " AND");
		}
		if (entry.getPeer_id() != null) {
			ret.append(" peer_id='" + entry.getPeer_id() + "' AND");
		}
		if (entry.getUp_rate() != null) {
			ret.append(" up_rate=" + entry.getUp_rate() + " AND");
		}
		if (entry.getUp_str() != null) {
			ret.append(" up_str='" + entry.getUp_str() + "' AND");
		}
		if (entry.getUp_total() != null) {
			ret.append(" up_total=" + entry.getUp_total() + " AND");
		}
		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

}
