package eu.smoothit.sis.db.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;

public class UserRoleAPITest {

	private final static Logger logger = Logger
			.getLogger(UserRoleAPITest.class);

	/**
	 * Part1 Demonstrates User cannot fetch associated
	 * roles(User.getUserRoles()) PART2 Demonstrates IUserRoleDAO cannot persist
	 * userRole associated users(UserRole.setUsers())
	 */
	@Test
	public void testUserRole() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IUserDAO uDAO = factory.createUserDAO();
		IUserRoleDAO rDAO = factory.createUserRoleDAO();
		uDAO.removeAll(); // clear all first
		rDAO.removeAll(); // clear all first

		// Part1
		User user = new User();
		UserRole roleGroup = new UserRole();
		roleGroup.setRole("role1");
		roleGroup.setRoleGroup("group1");
		user.setPassword("psw1");
		user.setUsername("user1");
		List<UserRole> userRoles = new ArrayList<UserRole>();
		userRoles.add(roleGroup);
		user.setUserRoles(userRoles);
		uDAO.persist(user);// persist user and its accociated roles
		// successfully.
		List<User> users = uDAO.getAll();// get all users
		for (User u : users) {
			logger
					.debug("*************************OUTPUT USERS related info**********************************");
			logger.debug("user name:" + u.getUsername());// OK
			logger.debug("psw:" + u.getPassword());// OK
			logger.debug("Belong to class;" + u.getClass());// OK
			// The API used below caused
			// org.hibernate.LazyInitializationException
			Collection<UserRole> roles = u.getUserRoles();
		}

		// PART2
		roleGroup = new UserRole();
		roleGroup.setRole("role2");
		roleGroup.setRoleGroup("group2");
		user = new User();
		user.setPassword("psw2");
		user.setUsername("user2");
		users.clear();// clear all first.
		users.add(user);
		roleGroup.setUsers(users);
		rDAO.persist(roleGroup);// persist
		userRoles = new ArrayList<UserRole>();// get all userRoles
		userRoles = rDAO.getAll();
		for (UserRole userRole : userRoles) {
			logger
					.debug("*************************OUTPUT UserRoles related info**********************************");
			logger.debug(userRole.getRole());
			Collection<User> usersForRole = userRole.getUsers();//
			// If output below is null, then it shows associated users are not
			// able to be persisted via rDAO.persist(roleGroup)
			for (User u : usersForRole) {
				logger.debug("user name:" + u.getUsername());
				logger.debug("psw:" + u.getPassword());
				logger.debug("Belong to class;" + u.getClass());
			}

		}

	}

	public static void main(String[] args) {
		UserRoleAPITest ins = new UserRoleAPITest();
		ins.testUserRole();

	}

}
