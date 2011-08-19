package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentIPRangeDAO extends JpaDao<Long, IPRangeConfigEntry> implements IPRangeDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentIPRangeDAO.class);
	

	private static String tableName = "IPRangeConfigEntry";

	public PersistentIPRangeDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private String createQueryConditionsFromParameters(IPRangeConfigEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getPrefix() != null) {
			ret.append(" PREFIX='" + entry.getPrefix() + "' AND");
		}
		if (entry.getPrefix_len() != null) {
			ret.append(" PREFIX_LEN=" + entry.getPrefix_len() + " AND");
		}
		if (entry.getLocal() != null) {
			ret.append(" ISLOCAL=" + entry.getLocal() + " AND");
		}
		if (entry.getASPathLength() != null) {
			ret.append(" ASPATHLENGTH=" + entry.getASPathLength() + " AND");
		}
		if (entry.getLocalPreference() != null) {
			ret.append(" LOCALPREFERENCE=" + entry.getLocalPreference()
					+ " AND");
		}
		if (entry.getMED() != null) {
			ret.append(" MED=" + entry.getMED() + " AND");
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
	public List<IPRangeConfigEntry> get(IPRangeConfigEntry entity) {
		String conditions = createQueryConditionsFromParameters(entity);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

}
