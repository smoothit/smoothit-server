package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IClientSwarmInfoDAO;
import eu.smoothit.sis.db.impl.entities.ClientSwarmInfoEntry;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage.ACTION;

public class PersistentClientSwarmInfoDAO extends
		JpaDao<Long, ClientSwarmInfoEntry> implements IClientSwarmInfoDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentClientSwarmInfoDAO.class);

	private static String tableName = "ClientSwarmInfoEntry";

	public PersistentClientSwarmInfoDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientSwarmInfoEntry> get(ClientSwarmInfoEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getLinkPropertiesEntries() = " + query);
		return pm.queryDatabase(query);
	}

	private String createQueryConditionsFromParameters(
			ClientSwarmInfoEntry entry) {
		StringBuffer ret = new StringBuffer();

		if (entry.getId() != null) {
			ret.append(" id =" + entry.getId() + " AND");
		}
		if (entry.getIp() != null) {
			ret.append(" ip='" + entry.getIp() + "' AND");
		}
		if (entry.getPort() != null) {
			ret.append(" port=" + entry.getPort() + " AND");
		}
		if (entry.getTimestamp() != null) {
			ret.append(" timestamp=" + entry.getTimestamp() + " AND");
		}
		if (entry.getTorrent_id() != null) {
			ret.append(" torrent_id='" + entry.getTorrent_id() + "' AND");
		}
		if (entry.getTorrent_url() != null) {
			ret.append(" torrent_url='" + entry.getTorrent_url() + "' AND");
		}
		if (entry.getDownload_progress() != null) {
			ret.append(" download_progress='" + entry.getDownload_progress()
					+ "' AND");
		}
		if (entry.getFile_size() != null) {
			ret.append(" file_size=" + entry.getFile_size() + " AND");
		}
		if (entry.isIop_flag() != null) {
			ret.append(" iop_flag=" + entry.isIop_flag() + " AND");
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
	public Double getFileSizeForTorrentID(String torrentId) {
		String query = "from " + tableName + " WHERE torrent_id='" + torrentId
				+ "'";
		logger.debug("Resulting query of getFileSizeForTorrentID() = " + query);
		List<ClientSwarmInfoEntry> result = (List<ClientSwarmInfoEntry>) pm
				.queryDatabase(query);

		if (result != null && result.size() > 0) {
			return result.get(0).getFile_size();
		}
		return -1.0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getNumberOfLocalLeechers(String torrentId) {
		String query = "Select DISTINCT(ip) from " + tableName + " h WHERE torrent_id='" + torrentId
				+ "' AND download_progress < 100.00";
		logger.debug("Resulting query of getNumberOfLocalLeechers() = " + query);
		List<ClientSwarmInfoEntry> result = (List<ClientSwarmInfoEntry>) pm
				.queryDatabase(query);
		logger.debug(result);
		if (result != null && result.size() >= 0) {
			return result.size();
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLocalLeechers(String torrentId) {
		String query = "Select DISTINCT(ip) from " + tableName + " h WHERE torrent_id='" + torrentId
				+ "' AND download_progress < 100.00";
		logger.debug("Resulting query of getLocalLeechers() = " + query);
		return (List<String>) pm.queryDatabase(query);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLocalSeeders(String torrentId) {
		String query = "SELECT DISTINCT(ip) from " + tableName + " h WHERE torrent_id='" + torrentId
				+ "' AND download_progress = 100.00";
		logger.debug("Resulting query of getLocalSeeders() = " + query);
		return (List<String>) pm.queryDatabase(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getNumberOfLocalSeeders(String torrentId) {
		String query = "Select DISTINCT(ip) from " + tableName + " h WHERE torrent_id='" + torrentId
				+ "' AND download_progress = 100";
		logger.debug("Resulting query of getNumberOfLocalSeeder() = " + query);
		List<ClientSwarmInfoEntry> result = (List<ClientSwarmInfoEntry>) pm
				.queryDatabase(query);
		if (result != null && result.size() >= 0) {
			return result.size();
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSwarmsYoungerThen(Long t_age_timestamp, Boolean flag) {
		String query;
		if (flag)
			query = "Select DISTINCT(torrent_id) from " + tableName + " h WHERE timestamp > "
				+ t_age_timestamp + " AND iop_flag= "+ flag;
		else
			query = "Select DISTINCT(torrent_id) from " + tableName + " h WHERE timestamp > "
			+ t_age_timestamp + " AND (iop_flag= "+ flag + " OR iop_flag= NULL )";
		
		logger.debug("Resulting query of getSwarmYoungerThen() = " + query);
		return (List<String>) pm.queryDatabase(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteSwarmsOlderThen(Long t_out_timestamp) {
		String query = "from " + tableName + " WHERE timestamp < "
				+ t_out_timestamp;
		logger.debug("Resulting query of getSwarmYoungerThen() = " + query);
		List<ClientSwarmInfoEntry> result = pm.queryDatabase(query);
		for (ClientSwarmInfoEntry clientSwarmInfoEntry : result) {
			pm.removeAndNotify(clientSwarmInfoEntry,
					new DBNotifyMessage(PersistentClientSwarmInfoDAO.class
							.getName(), ACTION.REMOVE));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setIoPParticipation(String torrentId, boolean flag) {
		String query = "from " + tableName + " WHERE torrent_id='" + torrentId
				+ "'";
		logger.debug("Resulting query of getLinkPropertiesEntries() = " + query);
		List<ClientSwarmInfoEntry> result = (List<ClientSwarmInfoEntry>) pm
				.queryDatabase(query);

		if (result != null) {
			for (ClientSwarmInfoEntry clientSwarmInfoEntry : result) {
				clientSwarmInfoEntry.setIop_flag(flag);
				pm.updateAndNotify(clientSwarmInfoEntry, new DBNotifyMessage(
						PersistentClientSwarmInfoDAO.class.getName(),
						ACTION.UPDATE));
			}
		}
	}

}
