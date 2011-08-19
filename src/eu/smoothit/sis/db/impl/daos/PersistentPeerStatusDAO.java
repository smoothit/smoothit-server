/**
 * 
 */
package eu.smoothit.sis.db.impl.daos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

/**
 * @author christian
 * 
 */
public class PersistentPeerStatusDAO extends JpaDao<Long, PeerStatusEntry>
		implements IPeerStatusDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentComponentConfigDAO.class);

	private static String peerStatusTable = "PeerStatusEntry";

	public PersistentPeerStatusDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PeerStatusEntry> get(PeerStatusEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "SELECT h FROM " + peerStatusTable + " h" + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

	private String createQueryConditionsFromParameters(PeerStatusEntry entry) {
		StringBuffer ret = new StringBuffer();

		if (entry.getDown_total() != null) {
			ret.append(" h.down_total=" + entry.getDown_total() + " AND");
		}
		if (entry.getFilename() != null) {
			ret.append(" h.filename='" + entry.getFilename() + "' AND");
		}
		if (entry.getInfohash() != null) {
			ret.append(" h.infohash='" + entry.getInfohash() + "' AND");
		}
		if (entry.getListenport() != null) {
			ret.append(" h.listenport=" + entry.getListenport() + " AND");
		}
		if (entry.getLive() != null) {
			ret.append(" h.live=" + entry.getLive() + " AND");
		}
		if (entry.getP_dropped() != null) {
			ret.append(" h.p_dropped=" + entry.getP_dropped() + " AND");
		}
		if (entry.getP_late() != null) {
			ret.append(" h.p_late=" + entry.getP_late() + " AND");
		}
		if (entry.getP_played() != null) {
			ret.append(" h.p_played=" + entry.getP_played() + " AND");
		}
		if (entry.getPeer_id() != null) {
			ret.append(" h.peer_id='" + entry.getPeer_id() + "' AND");
		}
		if (entry.getProgress() != null) {
			ret.append(" h.progress=" + entry.getProgress() + " AND");
		}
		if (entry.getStatus() != null) {
			ret.append(" h.status='" + entry.getStatus() + "' AND");
		}
		if (entry.getT_prebuf() != null) {
			ret.append(" h.t_prebuf=" + entry.getT_prebuf() + " AND");
		}
		if (entry.getT_stalled() != null) {
			ret.append(" h.t_stalled=" + entry.getT_stalled() + " AND");
		}
		if (entry.getTimestamp() != null) {
			ret.append(" h.timestamp=" + entry.getTimestamp() + " AND");
		}
		if (entry.getUp_rate() != null) {
			ret.append(" h.up_rate=" + entry.getUp_rate() + " AND");
		}
		if (entry.getUp_total() != null) {
			ret.append(" h.up_total=" + entry.getUp_total() + " AND");
		}
		if (entry.getDown_rate() != null) {
			ret.append(" h.down_rate=" + entry.getDown_rate() + " AND");
		}
		if (entry.getIop_flag() != null) {
			ret.append(" h.iop_flag=" + entry.getIop_flag() + " AND");
		}
		if (entry.getValidRange() != null) {
			ret.append(" h.validRange='" + entry.getValidRange() + "' AND");
		}
		if (entry.getIp_address() != null) {
			ret.append(" ip_address='" + entry.getIp_address() + "' AND");
		}
		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

	public List<String> getTorrents(long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		String query = "SELECT DISTINCT(infohash) FROM " + peerStatusTable
				+ " h WHERE h.timestamp > " + timestamp + " ";
		logger.debug("Resulting query of getTorrents()" + query);

		List<String> result = pm.queryDatabase(query);
		return result != null ? result : new ArrayList<String>();
	}

	@Override
	public List<String> getStatForTorrentHash(String infohash, long timeoffset) {
		List<String> result = new ArrayList<String>();
		result.add(infohash);
		result.add(getTotalDownstreamforTorrentHash(infohash, timeoffset)
				.toString());
		result.add(getTotalUpstreamforTorrentHash(infohash, timeoffset)
				.toString());
		result.add(getNumberOfLocalPeersForTorrentHash(infohash, timeoffset)
				.toString());
		result.add(getNumberOfLeechersForTorrentHash(infohash, timeoffset)
				.toString());
		result.add(getNumberOfSeedersForTorrentHash(infohash, timeoffset)
				.toString());
		result.add(getNumberOfIopsForTorrentHash(infohash, timeoffset)
				.toString());
		return result;
	}

	@Override
	public List<List<String>> getStatForAllTorrents(long timeoffset) {
		List<String> torrents = getTorrents(timeoffset);
		List<List<String>> stats = new ArrayList<List<String>>();
		for (String infohash : torrents) {
			List<String> result = new ArrayList<String>();
			result.add(infohash);
			result.add(getTotalDownstreamforTorrentHash(infohash, timeoffset)
					.toString());
			result.add(getTotalUpstreamforTorrentHash(infohash, timeoffset)
					.toString());
			result
					.add(getNumberOfLocalPeersForTorrentHash(infohash,
							timeoffset).toString());
			result.add(getNumberOfLeechersForTorrentHash(infohash, timeoffset)
					.toString());
			result.add(getNumberOfSeedersForTorrentHash(infohash, timeoffset)
					.toString());
			result.add(getNumberOfIopsForTorrentHash(infohash, timeoffset)
					.toString());
			stats.add(result);
		}
		return stats;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double getTotalDownstreamforTorrentHash(String infohash,
			long timeoffset) {
		float timestamp = System.currentTimeMillis() - timeoffset;

		String query = "SELECT SUM(down_total) FROM monitoring_peer_status_entry WHERE id IN (Select MAX(ID) from monitoring_peer_status_entry where infohash= '"
				+ infohash
				+ "' and timestamp >"
				+ timestamp
				+ " group by ip_address, listenport)";
		logger.debug(query);
		logger.debug("Resulting query of getTotalDownRateforTorrentHash() "
				+ query);

		List l = pm.queryDatabaseNatively(query);
		if (l != null && l.size() == 1) {
			Double result = (Double) l.get(0);
			return result != null ? result : -1;
		} else {
			return -1.0;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Double getTotalUpstreamforTorrentHash(String infohash,
			long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		String query = "SELECT SUM(up_total) FROM monitoring_peer_status_entry WHERE id IN (Select MAX(ID) from monitoring_peer_status_entry where infohash= '"
				+ infohash
				+ "' and timestamp >"
				+ timestamp
				+ " group by ip_address, listenport)";
		logger.debug("Resulting query of getTotalDownRateforTorrentHash() "
				+ query);

		List l = pm.queryDatabaseNatively(query);
		if (l != null && l.size() == 1) {
			Double result = (Double) l.get(0);
			return result != null ? result : -1;
		} else {
			return -1.0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getNumberOfLocalPeersForTorrentHash(String infohash,
			long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		String query = "SELECT count(DISTINCT ip_address, listenport) FROM monitoring_peer_status_entry WHERE infohash='"
				+ infohash
				+ "' and timestamp >"
				+ timestamp
				+ " and iop_flag=false";
		logger
				.debug("Resulting query of getNumberOfLocalPeersForTorrentHash() "
						+ query);

		List l = pm.queryDatabaseNatively(query);

		if (l != null && l.size() == 1) {
			Long result = ((BigInteger) pm.queryDatabaseNatively(query).get(0))
					.longValue();

			return (result != null ? result : -1);
		} else {
			return -1L;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getNumberOfLeechersForTorrentHash(String infohash,
			long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		String query = "SELECT count(DISTINCT ip_address, listenport) FROM monitoring_peer_status_entry WHERE infohash='"
				+ infohash
				+ "' and timestamp >"
				+ timestamp
				+ " and progress < 100 and iop_flag=false";
		logger
				.debug("Resulting query of getNumberOfLocalPeersForTorrentHash() "
						+ query);

		List l = pm.queryDatabaseNatively(query);

		if (l != null && l.size() == 1) {
			Long result = ((BigInteger) pm.queryDatabaseNatively(query).get(0))
					.longValue();

			return (result != null ? result : -1);
		} else {
			return -1L;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getNumberOfSeedersForTorrentHash(String infohash,
			long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		String query = "SELECT count(DISTINCT ip_address, listenport) FROM monitoring_peer_status_entry WHERE infohash='"
				+ infohash
				+ "' and timestamp >"
				+ timestamp
				+ " and progress = 100 and iop_flag=false";
		logger
				.debug("Resulting query of getNumberOfLocalPeersForTorrentHash() "
						+ query);

		List l = pm.queryDatabaseNatively(query);

		if (l != null && l.size() == 1) {
			Long result = ((BigInteger) pm.queryDatabaseNatively(query).get(0))
					.longValue();

			return (result != null ? result : -1);
		} else {
			return -1L;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getNumberOfIopsForTorrentHash(String infohash, long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		String query = "SELECT count(DISTINCT ip_address, listenport) FROM monitoring_peer_status_entry WHERE infohash='"
				+ infohash
				+ "' and timestamp >"
				+ timestamp
				+ " and iop_flag=true";
		logger.debug("Resulting query of getNumberOfIopsForTorrentHash() "
				+ query);
		List l = pm.queryDatabaseNatively(query);

		if (l != null && l.size() == 1) {
			Long result = ((BigInteger) pm.queryDatabaseNatively(query).get(0))
					.longValue();

			return (result != null ? result : -1);
		} else {
			return -1L;
		}
	}

	@Override
	public List<List<String>> getListOfPeers(String infohash, long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;

		String query = "SELECT ip_address, listenport, progress, down_rate, up_rate FROM monitoring_peer_status_entry WHERE id IN (Select MAX(ID) from monitoring_peer_status_entry where infohash= '"
				+ infohash
				+ "' and timestamp > "
				+ timestamp
				+ " and iop_flag=false GROUP BY ip_address, listenport)";
//		String query = "SELECT ip_address, listenport, avg(progress), avg(down_rate), avg(up_rate) FROM "
//				+ peerStatusTable
//				+ " h WHERE h.infohash='"
//				+ infohash
//				+ "' and h.timestamp >"
//				+ timestamp
//				+ " and h.iop_flag=false GROUP BY ip_address, listenport";
		logger.debug("Resulting query of getListOfPeers()" + query);
		List<List<String>> result = new ArrayList<List<String>>();
		List<Object[]> temp = pm.queryDatabaseNatively(query);
		for (Object[] object : temp) {
			List<String> row = new ArrayList<String>();
			for (int i = 0; i < object.length; i++) {
				if (object[i] != null) {
					row.add(object[i].toString());
				} else {
					row.add("NULL");
				}
			}
			result.add(row);
		}

		return result;
	}

	@Override
	public List<List<String>> getListOfIops(String infohash, long timeoffset) {
		long timestamp = System.currentTimeMillis() - timeoffset;
		
		String query = "SELECT ip_address, listenport, progress, down_rate, up_rate FROM monitoring_peer_status_entry WHERE id IN (Select MAX(ID) from monitoring_peer_status_entry where infohash= '"
			+ infohash
			+ "' and timestamp >"
			+ timestamp
			+ " and iop_flag=true GROUP BY ip_address, listenport)";
		
//		String query = "SELECT ip_address, listenport, avg(progress), avg(down_rate), avg(up_rate) FROM "
//				+ peerStatusTable
//				+ " h WHERE h.infohash='"
//				+ infohash
//				+ "' and h.timestamp >"
//				+ timestamp
//				+ " and h.iop_flag=true GROUP BY ip_address, listenport";
		logger.debug("Resulting query of getListOfPeers()" + query);
		List<List<String>> result = new ArrayList<List<String>>();
		List<Object[]> temp = pm.queryDatabaseNatively(query);
		for (Object[] object : temp) {
			List<String> row = new ArrayList<String>();
			for (int i = 0; i < object.length; i++) {
				if (object[i] != null) {
					row.add(object[i].toString());
				} else {
					row.add("NULL");
				}
			}
			result.add(row);
		}

		return result;
	}

}
