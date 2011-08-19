/**
 * 
 */
package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.ISwarmDAO;
import eu.smoothit.sis.db.impl.entities.ClientRequestEntry;
import eu.smoothit.sis.db.impl.entities.SwarmEntityEntry;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

/**
 * @author christian
 * 
 */
public class PersistentSwarmDAO extends JpaDao<Long, SwarmEntityEntry>
		implements ISwarmDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentComponentConfigDAO.class);

	private static String swarmTable = "SwarmEntityEntry";

	public PersistentSwarmDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SwarmEntityEntry> get(SwarmEntityEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "from " + swarmTable + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

	@Override
	public void remove(SwarmEntityEntry entry) {

		List<SwarmEntityEntry> swarms2 = this.get(entry);
		for (SwarmEntityEntry swarmEntity : swarms2) {
			for (ClientRequestEntry req : swarmEntity.getClientRequests()) {
				if (req.getSwarms() != null) {
					logger.debug(entry);
					logger.debug(swarms2.get(0));
					logger.debug(req.getSwarms().get(0));
					logger.debug(req.getSwarms().get(1));
					logger.debug("Vorher:" + req.getSwarms().size());
					req.getSwarms().remove(swarms2.get(0));
					logger.debug("Nachher:" + req.getSwarms().size());
					pm.updateAndNotify(req, new DBNotifyMessage(
							PersistentComponentConfigDAO.class.getSimpleName(),
							DBNotifyMessage.ACTION.UPDATE));
				}
			}
		}

		pm.removeAndNotify(swarms2.get(0), new DBNotifyMessage(
				PersistentComponentConfigDAO.class.getSimpleName(),
				DBNotifyMessage.ACTION.REMOVE));

		// String conditions = createQueryConditionsFromParameters(entry);
		// String query = "from " + swarmTable + conditions;
		// logger.debug("Resulting query of removeProperties() " + query);
		// pm.removeSpecifiedObjects(query, new DBNotifyMessage(
		// PersistentComponentConfigDAO.class.getSimpleName(),
		// DBNotifyMessage.ACTION.REMOVE));
	}

	private String createQueryConditionsFromParameters(SwarmEntityEntry entry) {
		StringBuffer ret = new StringBuffer();

		if (entry.getDownloadProgress() != null) {
			ret.append(" downloadProgress=" + entry.getDownloadProgress()
					+ " AND");
		}
		if (entry.getPlaybackProgress() != null) {
			ret.append(" playbackProgress=" + entry.getPlaybackProgress()
					+ " AND");
		}
		if (entry.getTorrentHash() != null) {
			ret.append(" torrentHash='" + entry.getTorrentHash() + "' AND");
		}
		if (entry.getTorrentURL() != null) {
			ret.append(" torrentURL='" + entry.getTorrentURL() + "' AND");
		}
		if (entry.getAvailability() != null) {
			ret.append(" availability=" + entry.getAvailability() + " AND");
		}
		if (entry.getNumberOfLeechers() != null) {
			ret.append(" numberOfLeechers=" + entry.getNumberOfLeechers()
					+ " AND");
		}
		if (entry.getNumberOfLocalLeechers() != null) {
			ret.append(" numberOfLocalLeechers="
					+ entry.getNumberOfLocalLeechers() + " AND");
		}
		if (entry.getNumberOfSeeds() != null) {
			ret.append(" numberOfSeeds=" + entry.getNumberOfSeeds() + " AND");
		}
		if (entry.getNumberOfLocalSeeds() != null) {
			ret.append(" numberOfLocalSeeds=" + entry.getNumberOfLocalSeeds()
					+ " AND");
		}
		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

	// private String createQueryConditionsFromParameters(IClientRequest entry)
	// {
	// StringBuffer ret = new StringBuffer();
	// if (entry.getIP() != null) {
	// ret.append(" ip='" + entry.getIP() + "' AND");
	// }
	// if (entry.getPort() != null) {
	// ret.append(" port=" + entry.getPort() + " AND");
	// }
	// if (entry.getLastUpdate() != null) {
	// ret.append(" lastupdate=" + entry.getLastUpdate() + " AND");
	// }
	// String string = ret.toString();
	// if (string.endsWith("AND")) {
	// string = string.substring(0, string.length() - 4);
	// return " WHERE" + string;
	// }
	// return string;
	// }

}
