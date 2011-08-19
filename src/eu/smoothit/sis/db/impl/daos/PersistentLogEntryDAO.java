package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.impl.entities.LogEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentLogEntryDAO extends JpaDao<Long, LogEntry> implements ILogEntryDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentLogEntryDAO.class);

	private static String tableName = "LogEntry";

	public PersistentLogEntryDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<LogEntry> get(LogEntry entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "Select h from " + tableName + " h " + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

	private String createQueryConditionsFromParameters(LogEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getEvent() != null) {
			ret.append(" h.event='" + entry.getEvent() + "' AND");
		}
		if (entry.getResult() != null) {
			ret.append(" h.result='" + entry.getResult() + "' AND");
		}
		if (entry.getUsername() != null) {
			ret.append(" h.username='" + entry.getUsername() + "' AND");
		}

		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

}
