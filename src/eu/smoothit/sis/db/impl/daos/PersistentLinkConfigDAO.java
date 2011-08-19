package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.ILinkConfigDAO;
import eu.smoothit.sis.db.impl.entities.LinkConfigEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentLinkConfigDAO extends JpaDao<Long, LinkConfigEntry> implements ILinkConfigDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentLinkConfigDAO.class);


	private static String tableName = "LinkConfigEntry";

	public PersistentLinkConfigDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<LinkConfigEntry> get(LinkConfigEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getLinkPropertiesEntries() = " + query);
		return pm.queryDatabase(query);
	}

	private String createQueryConditionsFromParameters(LinkConfigEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getDelay() != null) {
			ret.append(" DELAY=" + entry.getDelay() + " AND");
		}
		if (entry.getDest_prefix() != null) {
			ret.append(" DEST_PREFIX='" + entry.getDest_prefix() + "' AND");
		}
		if (entry.getDest_prefix_len() != null) {
			ret.append(" DEST_PREFIX_LEN=" + entry.getDest_prefix_len()
					+ " AND");
		}
		if (entry.getLinkCapacity() != null) {
			ret.append(" LINKCAPACITY=" + entry.getLinkCapacity() + " AND");
		}
		if (entry.getRelation() != null) {
			ret.append(" RELATION='" + entry.getRelation() + "' AND");
		}
		if (entry.getSrc_prefix() != null) {
			ret.append(" SRC_PREFIX='" + entry.getSrc_prefix() + "' AND");
		}
		if (entry.getSrc_prefix_len() != null) {
			ret.append(" SRC_PREFIX_LEN='" + entry.getSrc_prefix_len()
					+ "' AND");
		}

		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

}
