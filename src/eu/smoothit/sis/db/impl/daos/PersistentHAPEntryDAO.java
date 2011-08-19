package eu.smoothit.sis.db.impl.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentHAPEntryDAO extends JpaDao<Long,HAPEntry> implements IHAPEntryDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentHAPEntryDAO.class);


	private static String tableName = "HAPEntry";

	public PersistentHAPEntryDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private String createQueryConditionsFromParameters(HAPEntry entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getIp_address() != null) {
			ret.append(" ip_address='" + entry.getIp_address() + "' AND");
		}
		if (entry.getListenport() != null) {
			ret.append(" listenport=" + entry.getListenport() + " AND");
		}
		if (entry.getHap_flag() != null) {
			ret.append(" hap_flag=" + entry.getHap_flag() + " AND");
		}
		

		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}

	
	@SuppressWarnings("unchecked")
	public List<HAPEntry> get(HAPEntry entity) {
		String conditions = createQueryConditionsFromParameters(entity);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> getPromotedHAPs() {
		String query = "SELECT ip_address, listenport FROM hap_entry where hap_flag=false";
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
	public List<List<String>> getNewHAPs() {
		String query = "SELECT ip_address, listenport FROM hap_entry where hap_flag=true";
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

}
