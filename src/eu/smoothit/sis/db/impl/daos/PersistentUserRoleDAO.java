package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentUserRoleDAO extends JpaDao<Long, UserRole> implements IUserRoleDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentUserRoleDAO.class);

	private static String tableName = "UserRole";

	public PersistentUserRoleDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> get(UserRole entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of getProperties()" + query);
		return pm.queryDatabase(query);
	}

	private String createQueryConditionsFromParameters(UserRole entry) {
		StringBuffer ret = new StringBuffer();
		
		if (entry.getRoleGroup() != null) {
			ret.append(" ROLE_GROUP='" + entry.getRoleGroup() + "' AND");
		}
		if (entry.getRole() != null) {
			ret.append(" ROLE='" + entry.getRole() + "' AND");
		}

		String string = ret.toString();
		if (string.endsWith("AND")) {
			string = string.substring(0, string.length() - 4);
			return " WHERE" + string;
		}
		return string;
	}
	
	@Override
	public void remove(UserRole entry) {

		List<UserRole> userRoles = this.get(entry);
		for (UserRole role : userRoles) {
			for (User user : role.getUsers()) {
				if (user.getUserRoles() != null) {
					logger.debug(entry);
					logger.debug(userRoles.get(0));
					logger.debug("Vorher:" + user.getUserRoles().size());
					user.getUserRoles().remove(userRoles.get(0));
					logger.debug("Nachher:" + user.getUserRoles().size());
					pm.updateAndNotify(user, new DBNotifyMessage(
							PersistentComponentConfigDAO.class.getSimpleName(),
							DBNotifyMessage.ACTION.UPDATE));
				}
			}
		}

		pm.removeAndNotify(userRoles.get(0), new DBNotifyMessage(
				PersistentComponentConfigDAO.class.getSimpleName(),
				DBNotifyMessage.ACTION.REMOVE));
	}

}
