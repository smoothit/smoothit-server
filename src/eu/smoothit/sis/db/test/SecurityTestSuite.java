package eu.smoothit.sis.db.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;

public class SecurityTestSuite extends TestCase {

	protected Logger logger = Logger.getLogger(SecurityTestSuite.class);

	public SecurityTestSuite() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IUserDAO userDAO = factory.createUserDAO();
		IUserRoleDAO userRoleDAO = factory.createUserRoleDAO();

		assertNotNull(userRoleDAO);
		assertNotNull(userDAO);
		userDAO.removeAll();
		userRoleDAO.removeAll();
		assertTrue(userDAO.countAll() == 0);

		UserRole role = new UserRole();
		role.setRole("superrolle");
		role.setRoleGroup("supergruppe");

		List<UserRole> collectionRoles = new LinkedList<UserRole>();
		collectionRoles.add(role);

		List<User> collectionUsers = new LinkedList<User>();
		User user = new User();
		assertNotNull(user);
		user.setUsername("user1");
		user.setPassword("secret1");
		collectionUsers.add(user);
		user.setUserRoles(collectionRoles);

		// store first user
		userDAO.persist(user);

		userDAO = factory.createUserDAO();
		assertNotNull(userDAO);
		user = new User();
		assertNotNull(user);
		user.setUsername("user2");
		user.setPassword("secret2");
//		user.setUserRoles(collectionRoles);
		
		collectionUsers.add(user);
		role.setUsers(collectionUsers);

		// add second user and userRole
		userDAO.persist(user);
//		userRoleDAO.persist(role);

		assertTrue(userDAO.countAll() == 2);
		assertTrue(userRoleDAO.countAll() == 1);

		/*
		 * modify first entry
		 */
		user = new User();
		user.setUsername("user1");
		List<User> users = userDAO.get(user);
		assertTrue(users.size() == 1);
		user = users.get(0);
		assertTrue(user.getPassword().equals("secret1"));
		user.setPassword("secret_new");
		userDAO.update(user);

		/*
		 * retrieve modified entry again and check whether modification was
		 * successful
		 */
		user = new User();
		user.setUsername("user1");
		users = userDAO.get(user);
		assertTrue(users.size() == 1);
		user = users.get(0);
		assertTrue(user.getPassword().equals("secret_new"));
		userDAO.removeAll();
		userRoleDAO.removeAll();
		assertTrue(userDAO.countAll() == 0);
		assertTrue(userRoleDAO.countAll() == 0);

		user = new User("user1", "pwd");
		UserRole userRole = new UserRole("myRole", "myGroup");
		List<UserRole> listRoles = new ArrayList<UserRole>();
		listRoles.add(userRole);
		user.setUserRoles(listRoles);
		List<User> listUsers = new ArrayList<User>();
		listUsers.add(user);
		userRole.setUsers(listUsers);
		userDAO.persist(user);
		assertTrue(userDAO.countAll() == 1);
		assertTrue(userRoleDAO.countAll() == 1);

		userRole = new UserRole("myRole", "myGroup");
		listRoles = userRoleDAO.get(userRole);
		assertTrue(listRoles.size() == 1);
		userRoleDAO.remove(listRoles.get(0));
		assertTrue(!listRoles.get(0).equals(userRole));

		
		user = new User("user1", "pwd");
		userRole = new UserRole("myRole", "myGroup");
		listRoles = new ArrayList<UserRole>();
		listRoles.add(userRole);
		user.setUserRoles(listRoles);
		listUsers = new ArrayList<User>();
		listUsers.add(user);
		userRole.setUsers(listUsers);
		userDAO.persist(user);
		
		user = new User("user1", "pwd");
		listUsers = userDAO.get(user);
		assertTrue(listUsers.size() == 1);
		userDAO.remove(listUsers.get(0));
		assertTrue(listUsers.get(0).equals(user));

		// userDAO.removeAll();
		// userRoleDAO.removeAll();
		assertTrue(userDAO.countAll() == 0);
		assertTrue(userRoleDAO.countAll() == 0);

	}

	// update user with its associated roles
	// console return successful feedback
	// Hibernate: delete from security_users_security_userroles where
	// security_users=?
	// Hibernate: insert into security_users_security_userroles (security_users,
	// security_roles) //values (?, ?)
	// Hibernate: insert into security_users_security_userroles (security_users,
	// // //security_roles) values (?, ?)
	public void testUpdateUserAssociatedRoles() {
	        SisDAOFactory factory = SisDAOFactory.getFactory();
	        IUserDAO uDAO = factory.createUserDAO();
	        IUserRoleDAO rDAO = factory.createUserRoleDAO();
	        
	        //first add one entry
	    	UserRole role = new UserRole();
			role.setRole("JBossAdmin");
			role.setRoleGroup("JBossAdmins");
			List<UserRole> collectionRoles  = new LinkedList<UserRole>();
			collectionRoles.add(role);
			List<User> collectionUsers  = new LinkedList<User>();
			User user = new User();
			assertNotNull(user);
			user.setUsername("user1");
			user.setPassword("secret1");
			collectionUsers.add(user);
			user.setUserRoles(collectionRoles);
			role.setUsers(collectionUsers);
			
			//store first user
			uDAO.persist(user);
			
			
			//create new UserRole
			UserRole normalRole = new UserRole();
			normalRole.setRole("NormalUser");
			normalRole.setRoleGroup("NormalUsers");
			rDAO.persist(normalRole);
			
			//retrieve new Role
			normalRole = new UserRole();
			normalRole.setRole("NormalUser");
			normalRole.setRoleGroup("NormalUsers");
			normalRole = rDAO.get(normalRole).get(0);
	        
			UserRole adminRole = new UserRole();
			adminRole.setRole("JBossAdmin");
			adminRole=rDAO.get(adminRole).get(0);
	        
	        User newAssoicatedUser=new User();
	        newAssoicatedUser.setUsername("user1");
	        user=uDAO.get(newAssoicatedUser).get(0);
	        
	        Collection<UserRole>assoicatedRoles=new ArrayList<UserRole>();
	        assoicatedRoles.add(normalRole);
	        user.setUserRoles(assoicatedRoles);//associated user with a new Role
	        Collection<User> users =new ArrayList<User>();
	        users.add(user);
	        normalRole.setUsers(users);
	        user.equals(new User("user1", "secret1"));
	        user.hashCode();
	        normalRole.equals(adminRole);
	        normalRole.hashCode();
//	        uDAO.update(user);
	        rDAO.update(normalRole);
	        
	        User deluser = new User("user1", "secret1");
	        uDAO.remove(deluser);
	        rDAO.remove(adminRole);

	    }

}
