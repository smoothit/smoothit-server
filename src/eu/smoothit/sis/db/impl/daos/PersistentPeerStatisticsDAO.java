package eu.smoothit.sis.db.impl.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.entities.PeerStatisticsEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentPeerStatisticsDAO extends
		JpaDao<Long, PeerStatisticsEntry> implements IPeerStatisticsDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentPeerStatisticsDAO.class);

	private static String tableName = "PeerStatisticsEntry";

	public PersistentPeerStatisticsDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private String createQueryConditionsFromParameters(PeerStatisticsEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getIp_address() != null) {
			ret.append(" ip_address='" + entry.getIp_address() + "' AND");
		}
		if (entry.getPort() != null) {
			ret.append(" listenport=" + entry.getPort() + " AND");
		}
		if (entry.getLocal_upload_volume() != null) {
			ret.append(" local_upload_volume=" + entry.getLocal_upload_volume()
					+ " AND");
		}
		if (entry.getTotal_upload_volume() != null) {
			ret.append(" total_upload_volume=" + entry.getTotal_upload_volume()
					+ " AND");
		}
		if (entry.getTotal_download_volume() != null) {
			ret.append(" total_download_volume="
					+ entry.getTotal_download_volume() + " AND");
		}
		if (entry.getHap_rating() != null) {
			ret.append(" hap_rating=" + entry.getHap_rating() + " AND");
		}

		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> getPeers() {
		String query = "SELECT ip_address, listenport FROM hap_peer_statistic_entry";
		List l = pm.queryDatabaseNatively(query);
		List<List<String>> result = new ArrayList<List<String>>();
		if (l != null && l.size() > 0) {
			for (int i = 0; i < l.size(); i++) {
				Object[] temp = (Object[]) l.get(i);
				if (temp != null && temp.length == 2) {
					List<String> inner = new ArrayList<String>();
					inner.add((String) temp[0]);
					inner.add(String.valueOf(temp[1]).toString());
					result.add(inner);
				}
			}

		} 
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PeerStatisticsEntry> get(PeerStatisticsEntry entity) {
		String conditions = createQueryConditionsFromParameters(entity);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

}
