package eu.smoothit.sis.security.login;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.security.auth.spi.Util;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;

/**
 * Creates a user account. Tables: security_user, security_roles, 
 * security_user_security_userroles will be updated.
 * 
 * @author Tomasz Stradomski
 * 
 */
public class AccountCreator {

	private final static Logger logger = Logger.getLogger(AccountCreator.class);

	/**
	 * Add an account to the database. 
	 * @param accountName
	 *            An account name that will be added to database.
	 * @param accountPassword
	 *            An account password.
	 * @param role
	 *            A role that a user given by accountName fulfill.
	 * @param roleGroup
	 *            A group name that 'role' belongs to.
	 * @return true if account was added successfully, otherwise false
	 */
	public static boolean addAcount(String accountName, String accountPassword,
			String role, String roleGroup) {

		boolean status = false;
		final String hashedPassword = createPasswordHash(accountPassword);

		SisDAOFactory factory = SisDAOFactory.getFactory();
		IUserDAO dao = factory.createUserDAO();

		User user = new User();
		user.setUsername("admin");
		user.setPassword(hashedPassword);

		UserRole userRole = new UserRole();
		userRole.setRole("JBossAdmin");
		userRole.setRoleGroup("Roles");

		user.getUserRoles().add(userRole);

		List<User> listOfAdmins = dao.get(user);
		if (listOfAdmins.size() != 0) {
			logger.info("Account " + user.getUsername() + " already exists");
		} else {
			status = dao.persist(user);
		}

		return status;
	}

	private static String createPasswordHash(String password) {
		return Util.createPasswordHash("MD5", Util.BASE64_ENCODING, null, null,
				password);
	}
}
