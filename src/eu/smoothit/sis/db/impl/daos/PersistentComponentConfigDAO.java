package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentComponentConfigDAO extends JpaDao<Long,ComponentConfigEntry> implements IComponentConfigDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentComponentConfigDAO.class);


	private static String tableName = "ComponentConfigEntry";

	public PersistentComponentConfigDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private String createQueryConditionsFromParameters(ComponentConfigEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getComponent() != null) {
			ret.append(" COMPONENT='" + entry.getComponent() + "' AND");
		}
		if (entry.getPropName() != null) {
			ret.append(" PROPNAME='" + entry.getPropName() + "' AND");
		}
		if (entry.getValue() != null) {
			if (entry.getValue() instanceof String) {
				ret.append(" VALUE='" + entry.getValue() + "' AND");
			} else {
				ret.append(" VALUE=" + entry.getValue() + " AND");
			}
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
	public List<ComponentConfigEntry> findByComponent(String componentName) {
		return pm.queryDatabase("from " + tableName + " WHERE COMPONENT='"
				+ componentName + "'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ComponentConfigEntry findByComponentAndName(String componentName,
			String propName) {
		List<ComponentConfigEntry> entries = pm
				.queryDatabase("from " + tableName + " WHERE COMPONENT='"
						+ componentName + "' AND PROPNAME='" + propName + "'");
		return entries.size() >= 1 ? (ComponentConfigEntry) entries.get(0) : null;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<ComponentConfigEntry> get(ComponentConfigEntry entity) {
		String conditions = createQueryConditionsFromParameters(entity);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

}
