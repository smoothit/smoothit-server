package eu.smoothit.sis.db.impl.daos;

import java.util.List;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;

public class PersistentUserDAO extends JpaDao<Long, User> implements IUserDAO {

	private final static Logger logger = Logger
			.getLogger(PersistentUserDAO.class);

	private static String tableName = "User";

	public PersistentUserDAO(IPersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private String createQueryConditionsFromParameters(User entry) {
		StringBuffer ret = new StringBuffer();
		if (entry.getUsername() != null) {
			ret.append(" username='" + entry.getUsername() + "' AND");
		}
		if (entry.getPassword() != null) {
			ret.append(" password='" + entry.getPassword() + "' AND");
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
	public List<User> get(User entry) {
		String conditions = createQueryConditionsFromParameters(entry);
		String query = "from " + tableName + conditions;
		logger.debug("Resulting query of users() = " + query);
		return pm.queryDatabase(query);
	}
	
	@Override
	public void remove(User entry) {

		List<User> users = this.get(entry);
		for (User user : users) {
			for (UserRole userRole : user.getUserRoles()) {
				if (userRole.getUsers() != null) {
					logger.debug(entry);
					logger.debug(users.get(0));
					logger.debug("Vorher:" + userRole.getUsers().size());
					userRole.getUsers().remove(users.get(0));
					logger.debug("Nachher:" + userRole.getUsers().size());
					pm.updateAndNotify(userRole, new DBNotifyMessage(
							PersistentComponentConfigDAO.class.getSimpleName(),
							DBNotifyMessage.ACTION.UPDATE));
				}
			}
		}

		pm.removeAndNotify(users.get(0), new DBNotifyMessage(
				PersistentComponentConfigDAO.class.getSimpleName(),
				DBNotifyMessage.ACTION.REMOVE));
	}

}
