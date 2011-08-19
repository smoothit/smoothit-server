package eu.smoothit.sis.db.jboss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Assert;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IConnectedPeerStatusDAO;
import eu.smoothit.sis.db.api.daos.ILinkConfigDAO;
import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.ConnectedPeerStatusEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.db.impl.entities.LinkConfigEntry;
import eu.smoothit.sis.db.impl.entities.LogEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;
import eu.smoothit.sis.db.impl.utils.DAOUtils;

/**
 * A on-JBoss Test of the DB Idea is to create an object of this and call
 * configStatus from a WebService Method, e.g. within the add dummy web service
 * (in ClientEndpointImpl)
 * 
 * @author Jonas Flick, Christian Gross, KOM, TU Darmstadt
 * 
 *         to run the test it is necessary to invoke to following url:
 *         http://localhost:8080/sis/jbosstest
 * 
 */
public class JBossDBTest extends Assert {
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(JBossDBTest.class.getName());

	private static Random random = new Random();

	private boolean passed;

	private StringBuffer buf;

	private SisDAOFactory factory;

	public JBossDBTest() {
		buf = new StringBuffer();
		passed = true;
		init();
	}

	public SisDAOFactory getFactory() {
		return factory;
	}

	public String getLogMessages() {
		return buf.toString();
	}

	/**
	 * init the db "environement"
	 */
	public void init() {
		appendToLog("Initializing...");
		try {
			factory = SisDAOFactory.getFactory();
		} catch (Exception e) {
			appendToLog("Unforseen Problem while initializing: "
					+ e.getMessage());
			return;
		}

		appendToLog("Initializing complete!");
	}

	/**
	 * 
	 */
	public void runtest() {
		// Init

		try {
			appendToLog("Running SIS DB Test...");
			// usage of all DAOs
			appendToLog("*****************************************************************");
			passed = passed && testComponentConfigDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");
			passed = passed && testIPRangeDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");
			passed = passed && testLinkConfigDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");
			passed = passed && testUserDAOAndUserRoleDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");
			passed = passed && testLogEntryDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");
			passed = passed && testPeerStatusDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");
			passed = passed && testSecurityDAO();
//			Assert.assertTrue(passed);
			appendToLog("*****************************************************************");

		} catch (Exception e) {
			appendToLog("Unforseen Problem while testing db: " + e);
			e.printStackTrace();
			appendToLog("Test passed: false");
		}

		appendToLog("SIS DB Test complete! Test passed: " + passed);

	}

	private void appendToLog(String logMessage) {
		log.info(logMessage);
		buf.append(logMessage);
		buf.append("\n");
	}

	private boolean testComponentConfigDAO() {
		// Generate some value
		String propName = "Random-j" + random.nextInt();
		String value = (new Date(System.currentTimeMillis())).toString();

		// Store it
		IComponentConfigDAO compDAO = this.getFactory()
				.createComponentConfigDAO();
		ComponentConfigEntry config = new ComponentConfigEntry();
		config.setComponent("JBossTest");
		config.setPropName(propName);
		config.setValue(value);
		compDAO.persist(config);

		// Retrieve it
		ComponentConfigEntry search = new ComponentConfigEntry();
		search.setComponent("JBossTest");
		search.setPropName(propName);
		List<ComponentConfigEntry> list = compDAO.get(search);
		boolean r = ((String) list.get(0).getValue()).equalsIgnoreCase(value);

		appendToLog(compDAO.printAll());
		compDAO.removeAll();
		r = compDAO.countAll() == 0;
		appendToLog("TestComponentConfigDAO passed: " + r);
		return r;
	}

	private boolean testSecurityDAO() {
		boolean r = false;
		SisDAOFactory factory = this.getFactory();
		IUserDAO userdao = factory.createUserDAO();
		assertNotNull(userdao);
		userdao.removeAll();
		assertTrue(userdao.countAll() == 0);

		User user = new User();
		assertNotNull(user);
		user.setUsername("user1");
		user.setPassword("secret1");
		userdao.persist(user);

		userdao = factory.createUserDAO();
		assertNotNull(userdao);
		user = new User();
		assertNotNull(user);
		user.setUsername("user2");
		user.setPassword("secret2");
		userdao.persist(user);

		r = (userdao.countAll() == 2);

		appendToLog("SecurityDAO test passed: " + r);
		return r;
	}

	private boolean testPeerStatusDAO() {

		boolean r = false;

		IPeerStatusDAO psdao = factory.createPeerStatusDAO();
		IConnectedPeerStatusDAO cpsdao = factory.createConnectedPeerStatusDAO();

		psdao.removeAll();
		cpsdao.removeAll();

		PeerStatusEntry ps = new PeerStatusEntry();
		ConnectedPeerStatusEntry cps = new ConnectedPeerStatusEntry();
		ConnectedPeerStatusEntry cps2 = new ConnectedPeerStatusEntry();

		ps.setDown_total(20.0F);
		ps.setFilename("test.txt");
		ps.setInfohash("tollerHash");
		ps.setListenport(2500);
		// ps.setLive(5);
		ps.setP_dropped(6);
		ps.setP_late(7);
		// ps.setP_played(25.0F);
		ps.setPeer_id("peerID");
		ps.setProgress(20.0F);
		ps.setStatus("Fertig");
		ps.setT_prebuf(10);
		ps.setT_stalled(20);
		ps.setTimestamp(System.currentTimeMillis());
		ps.setUp_rate(26F);
		ps.setUp_total(25F);
		ps.setValidRange("validRange");

		// first entry
		cps.setAddr("Addr");
		cps.setDown_rate(10F);
		cps.setDown_str("rate");
		cps.setDown_total(10F);
		cps.setG2g("G2G");
		// cps.setG2g_score(10F);
		cps.setPeer_id("peerID");
		cps.setUp_rate(10F);
		cps.setUp_str("str");
		cps.setUp_total(10F);
		cps.setStatus(ps);

		// second entry
		cps2.setAddr("Addr2");
		cps2.setDown_rate(20F);
		cps2.setDown_str("rate2");
		cps2.setDown_total(20F);
		cps2.setG2g("G2G");
		// cps2.setG2g_score(20F);
		cps2.setPeer_id("peerID2");
		cps2.setUp_rate(20F);
		cps2.setUp_str("str");
		cps2.setUp_total(20F);
		cps2.setStatus(ps);

		List<ConnectedPeerStatusEntry> list = new LinkedList<ConnectedPeerStatusEntry>();
		list.add(cps);
		list.add(cps2);
		ps.setList(list);

		appendToLog("start persisting");
		appendToLog("persisting ps");		
		psdao.persist(ps);
		appendToLog("persisting cps");
//		cpsdao.persist(cps);


		cps = new ConnectedPeerStatusEntry();
		cps.setAddr("Addr");

		List<ConnectedPeerStatusEntry> listcps = cpsdao.get(cps);
		r = (listcps.size() == 1);
		appendToLog("PeerStatusDAO test passed: " + r);
		return r;
	}

	private boolean testIPRangeDAO() {
		IPRangeDAO ipDAO = this.getFactory().createIPRangeDAO();
		IPRangeConfigEntry ipr = new IPRangeConfigEntry();
		ipr.setPrefix("192.168.0");
		ipr.setPrefix_len(3);
		ipr.setLocal(true);
		ipr.setASPathLength(50);
		ipr.setLocalPreference(20);
		ipr.setMED(35);

		// add a value
		IPRangeConfigEntry value = new IPRangeConfigEntry();
		value.setPrefix("192.168.0");
		value.setPrefix_len(3);
		value.setLocal(true);
		value.setASPathLength(50);
		value.setLocalPreference(20);
		value.setMED(35);
		ipDAO.persist(value);

		List<IPRangeConfigEntry> list = ipDAO.get(new IPRangeConfigEntry());

		boolean r = DAOUtils.contains(list, ipr);
		appendToLog("First test of TestIPRangeDAO f = " + r);
		appendToLog(ipDAO.printAll());

		ipDAO.removeAll();
		r = (ipDAO.countAll() == 0);
		appendToLog("TestIPRangeDAO passed: " + r);
		return r;
	}

	private boolean testLinkConfigDAO() {
		boolean r = false;
		ILinkConfigDAO lcd = this.getFactory().createLinkConfigDAO();

		IPRangeConfigEntry src = new IPRangeConfigEntry();
		src.setPrefix("192.168.0");
		src.setPrefix_len(3);
		src.setASPathLength(50);
		src.setLocalPreference(20);
		src.setMED(35);

		IPRangeConfigEntry dest = new IPRangeConfigEntry();
		dest.setPrefix("214.130.12");
		dest.setPrefix_len(4);
		dest.setASPathLength(60);
		dest.setLocalPreference(30);
		dest.setMED(45);

		String p = "A policy String";

		LinkConfigEntry entry = new LinkConfigEntry();
		entry.setSrc_prefix("192.168.0");
		entry.setSrc_prefix_len(3);
		entry.setDest_prefix("214.130.12");
		entry.setDest_prefix_len(4);
		entry.setRelation(p);
		entry.setLinkCapacity(23d);
		entry.setDelay(130d);
		lcd.persist(entry);

		// boolean r = lcd.get(src.getPrefix() + ".1",
		// dest.getPrefix() + ".2").getRelation().equalsIgnoreCase(p);
		appendToLog(lcd.printAll());
		appendToLog("TestLinkConfigDAO passed: " + r);
		lcd.removeAll();
		r = lcd.countAll() == 0;
		return r;
	}

	private boolean testUserDAOAndUserRoleDAO() {
		IUserDAO userDAO = getFactory().createUserDAO();
		IUserRoleDAO userRoleDAO = getFactory().createUserRoleDAO();
		List<User> collectionUser = new ArrayList<User>();
		List<UserRole> collectionUserRole = new ArrayList<UserRole>();
		
		
		
		// create first entry for the user table
		User userEntry = new User();
		userEntry.setUsername("test");
		userEntry.setPassword("pass");
		collectionUser.add(userEntry);
		

		// create two entries for the userRole table, which are related to the
		// former user entry to the user table
		UserRole userRoleEntry = new UserRole();
//		userRoleEntry.setUsername("test");
		userRoleEntry.setRole("FirstRole");
		userRoleEntry.setRoleGroup("RoleGroup");
		

		UserRole userRoleEntry2 = new UserRole();
//		userRoleEntry.setUsername("test");
		userRoleEntry2.setRole("SecondRole");
		userRoleEntry2.setRoleGroup("RoleGroup");
		collectionUserRole.add(userRoleEntry);
		collectionUserRole.add(userRoleEntry2);
		
		userRoleEntry.setUsers(collectionUser);
		userRoleEntry2.setUsers(collectionUser);
		userEntry.setUserRoles(collectionUserRole);
		
		userDAO.persist(userEntry);
		
		// retrieve the entries from the user table and the corresponding
		// userRole table
		List<User> userList = userDAO.get(new User());
		User result = userList.get(0);

		boolean userTest = result.getUsername().equals("test");
		userTest &= result.getPassword().equals("pass");
		if (userTest) {
			appendToLog("role was successfully tested");
		} else {
			appendToLog("role was unsuccessfully tested");
		}

		// test the roles
		boolean rolesTest = true;
		Collection<UserRole> roles = result.getUserRoles();
		appendToLog("number of userRoles" + userRoleDAO.countAll());
		rolesTest = rolesTest && (roles != null);
		rolesTest = rolesTest && (userRoleDAO.countAll() == 2);
		
//		UserRole userRoleResult = roles.iterator().next();
//		rolesTest &= roles.size() == 2;
//		rolesTest &= userRoleResult.getUsername().equals("test");
//		rolesTest &= userRoleResult.getRoleGroup().equals("RoleGroup");
//		rolesTest &= userRoleResult.getRole().equals("FirstRole")
//				|| userRoleResult.getRole().equals("SecondRole");
		if (rolesTest) {
			appendToLog("roleGroup was successfully tested");
		} else {
			appendToLog("roleGroup was unsuccessfully tested");
		}

		// clear the tables
		boolean clearTest = true;
		userDAO.removeAll();
		userRoleDAO.removeAll();
		

		clearTest &= userRoleDAO.countAll() == 0 && userDAO.countAll() == 0;
		if (clearTest) {
			appendToLog("tables are empty");
		} else {
			appendToLog("tables are not empty");
		}
		appendToLog("TestUserDAO and TestUserRoleDAO passed: "
				+ (userTest && rolesTest && clearTest));
		return userTest && rolesTest && clearTest;
	}

	private boolean testLogEntryDAO() {
		ILogEntryDAO logEntryDAO = getFactory().createLogEntryDAO();

		// adding an entry to the database
		LogEntry entry = new LogEntry();
		entry.setUsername("test");
		entry.setEvent("someEvent");
		entry.setResult("someResult");
		logEntryDAO.persist(entry);

		// testing the retrieved result
		boolean resultTesting = true;
		LogEntry result = logEntryDAO.get(new LogEntry()).get(0);
		resultTesting &= result.getUsername().equals("test");
		resultTesting &= result.getEvent().equals("someEvent");
		resultTesting &= result.getResult().equals("someResult");
		if (resultTesting) {
			appendToLog("logEntry was successfully tested");
		} else {
			appendToLog("logEntry was unsuccessfully tested");
		}

		// testing the count function
		boolean furtherTesting = true;
		entry = new LogEntry();
		entry.setUsername("test2");
		entry.setEvent("someEvent2");
		entry.setResult("someResult2");
		logEntryDAO.persist(entry);

		furtherTesting &= logEntryDAO.countAll() == 2;
		if (furtherTesting) {
			appendToLog("further testing was successfully tested");
		} else {
			appendToLog("further testing was unsuccessfully tested");
		}

		logEntryDAO.removeAll();

		if (logEntryDAO.countAll() == 0) {
			appendToLog("table is empty");
		} else {
			appendToLog("table is not empty");
		}

		appendToLog("TestLogEntryDAO passed: "
				+ (resultTesting && furtherTesting && logEntryDAO.countAll() == 0));
		return resultTesting && furtherTesting && logEntryDAO.countAll() == 0;
	}

}
