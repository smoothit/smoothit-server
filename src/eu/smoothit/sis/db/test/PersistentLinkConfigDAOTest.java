package eu.smoothit.sis.db.test;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.ILinkConfigDAO;
import eu.smoothit.sis.db.impl.entities.LinkConfigEntry;

public class PersistentLinkConfigDAOTest extends TestCase {

	protected Logger logger = Logger.getLogger(PersistentIPRangeDAOTest.class);


	public PersistentLinkConfigDAOTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		// clear the database
		ILinkConfigDAO dao = factory.createLinkConfigDAO();
		dao.removeAll();

		// Add a value
		LinkConfigEntry config = new LinkConfigEntry();
		config.setSrc_prefix("192.168.0");
		config.setSrc_prefix_len(5);
		config.setDest_prefix("140.130.7");
		config.setDest_prefix_len(18);
		config.setRelation("Valid");
		config.setLinkCapacity(0d);
		config.setDelay(0d);
		assertTrue(dao.persist(config));
		assertTrue(dao.countAll() == 1);

		LinkConfigEntry config2 = new LinkConfigEntry();
		config2.setSrc_prefix("192.168.0");
		config2.setSrc_prefix_len(5);
		config2.setDest_prefix("140.130.7");
		config2.setDest_prefix_len(18);
		config2.setRelation("Valid");
		config2.setLinkCapacity(0d);
		config2.setDelay(0d);
		assertTrue(dao.persist(config2));
		assertTrue(dao.countAll() == 2);
		
		config2.setSrc_prefix("192.168.1");
		config2.setDest_prefix("140.130.6");
		assertTrue(dao.update(config2));
		assertTrue(dao.countAll() == 2);
//		logger.info(dao.printAll());
		
		config2 = new LinkConfigEntry();
		config2.setSrc_prefix("192.168.1");
		config2.setSrc_prefix_len(5);
		config2.setDest_prefix("140.130.6");
		config2.setDest_prefix_len(18);
		config2.setRelation("Valid");
		config2.setLinkCapacity(0d);
		config2.setDelay(0d);
		List<LinkConfigEntry> list = dao.get(config2);
		assertTrue(list.get(0).getDest_prefix().equals("140.130.6"));
		dao.remove(list.get(0));
//		logger.info(dao.printAll());
		
		
		// Retrieve added values
		LinkConfigEntry entry = dao.get(new LinkConfigEntry()).get(0);
		
		assertTrue(entry.getRelation().equals("Valid"));
		String string = "192.168.0" + "/" + 5 + " -> " + "140.130.7" + "/" + 18
				+ " : (" + 0.0 + " , " + 0.0 + " , " + "Valid" + ")";
		assertTrue(entry.toString().equals(string));
		dao.remove(entry);
		assertTrue(dao.countAll() == 0);
	}

	public void testIndividualFunctionality() {
		logger.info("No individual functionality to test");
		assertTrue(true);

	}

	// protected Logger logger = Logger
	// .getLogger(PersistentLinkConfigDAOTest.class);
	// private LinkConfigDAO dao;
	//
	// @Override
	// public void setUp() {
	// super.setUp();
	// createTheDAO();
	// assertNotNull(dao);
	// }
	//
	// public void createTheDAO() {
	// dao = SisDAOFactory.getFactory().createLinkConfigDAO();
	// }
	//
	// @Test
	// public void testDefaultValues() {
	// logger.debug("Testing default return values");
	// dao.clear();
	// // links that don't exist return null
	// assertTrue(dao.getLinkProperties("192.168.0.15", "237.42.23.3") == null);
	// }
	//
	// @Test
	// public void testAddingOfValues() {

	// }
	//
	// @Test
	// public void testRemoval() {
	// logger.info("Testing Removal");
	// // first of all clean the database
	// dao.clear();
	//
	// // add a value
	// LinkProperties config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("192.168.0");
	// config.setSrc_prefix_len(5);
	// config.setDest_prefix("140.130.7");
	// config.setDest_prefix_len(18);
	// config.setRelation("Valid");
	// config.setLinkCapacity(0d);
	// config.setDelay(0d);
	// assertTrue(dao.addLinkPropertiesEntry(config));
	// assertTrue(dao
	// .getLinkPropertiesEntries(dao.createLinkPropertiesEntry())
	// .size() == 1);
	//
	// // remove a value
	// dao.removeLinkPropertiesEntries(dao.createLinkPropertiesEntry());
	//
	// // Assert that it's gone
	// assertTrue(dao
	// .getLinkPropertiesEntries(dao.createLinkPropertiesEntry())
	// .size() == 0);
	//
	// // add a value
	// config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("192.168.0");
	// config.setSrc_prefix_len(5);
	// config.setDest_prefix("140.130.7");
	// config.setDest_prefix_len(18);
	// config.setRelation("Valid");
	// config.setLinkCapacity(0d);
	// config.setDelay(0d);
	// assertTrue(dao.addLinkPropertiesEntry(config));
	// assertTrue(dao
	// .getLinkPropertiesEntries(dao.createLinkPropertiesEntry())
	// .size() == 1);
	//
	// // remove a value
	// LinkProperties entry = dao.getLinkPropertiesEntries(
	// dao.createLinkPropertiesEntry()).get(0);
	// dao.removeLinkPropertiesEntries(entry);
	//
	// // Assert that it's gone
	// assertTrue(dao
	// .getLinkPropertiesEntries(dao.createLinkPropertiesEntry())
	// .size() == 0);
	//
	// // clean the database
	// dao.clear();
	//
	// // Fill the database
	// createSomeEntries();
	//
	// assertTrue(dao.countAll() == 9);
	// LinkProperties search = dao.createLinkPropertiesEntry();
	// search.setRelation("Valid");
	// dao.removeLinkPropertiesEntries(search);
	// assertTrue(dao.countAll() == 4);
	// assertTrue(dao.getLinkPropertiesEntries(search).size() == 0);
	// search.setRelation(null);
	// search.setLinkCapacity(3d);
	// search.setDelay(1d);
	// dao.removeLinkPropertiesEntries(search);
	// assertTrue(dao.countAll() == 3);
	// }
	//
	// @Test
	// public void testUpdatingOfValues() {
	// logger.debug("Testing Updating of Values");
	//
	// // clean the db
	// dao.clear();
	// assertTrue(dao.countAll() == 0);
	//
	// LinkProperties config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("192.168.0");
	// config.setSrc_prefix_len(5);
	// config.setDest_prefix("140.130.7");
	// config.setDest_prefix_len(18);
	// config.setRelation("Valid");
	// config.setLinkCapacity(0d);
	// config.setDelay(0d);
	// assertTrue(dao.addLinkPropertiesEntry(config));
	// assertTrue(dao.countAll() == 1);
	//
	// // Now retrieve all these vals
	// LinkProperties search = dao.createLinkPropertiesEntry();
	// search.setSrc_prefix("192.168.0");
	// LinkProperties entry = dao.getLinkPropertiesEntries(search).get(0);
	//
	// assertTrue(entry.getSrc_prefix().equals("192.168.0"));
	// assertTrue(entry.getSrc_prefix_len() == 5);
	// assertTrue(entry.getDest_prefix().equals("140.130.7"));
	// assertTrue(entry.getDest_prefix_len() == 18);
	// assertTrue(entry.getRelation().equals("Valid"));
	// assertTrue(entry.getLinkCapacity() == 0);
	// assertTrue(entry.getDelay() == 0);
	//
	// // Update vals
	// // The properties of corresponding links change
	// entry.setRelation("Invalid");
	// entry.setLinkCapacity(1d);
	// entry.setDelay(2d);
	// dao.updateLinkProperties(entry);
	//
	// // Now retrieve all these vals
	// assertTrue(dao.countAll() == 1);
	// entry = (LinkConfigEntry) dao.getLinkPropertiesEntries(search).get(0);
	//
	// assertTrue(entry.getSrc_prefix().equals("192.168.0"));
	// assertTrue(entry.getSrc_prefix_len() == 5);
	// assertTrue(entry.getDest_prefix().equals("140.130.7"));
	// assertTrue(entry.getDest_prefix_len() == 18);
	// assertTrue(entry.getRelation().equals("Invalid"));
	// assertTrue(entry.getLinkCapacity() == 1);
	// assertTrue(entry.getDelay() == 2);
	//
	// }
	//
	// @Test
	// public void testPersistence() {
	// // start with clean db
	// testReset();
	//
	// // now insert some values
	// testInsertingOfValues();
	//
	// // Recreate the DAO
	// createTheDAO();
	// // With PersistentDAO there should be persistence after recreate
	// // So values will still be there
	// assertTrue(dao.getLinkProperties("192.180.0.15", "237.42.23.3")
	// .getRelation().equalsIgnoreCase("Allow"));
	// dao.clear();
	// }
	//
	// @Test
	// public void testInsertingOfValues() {
	// logger.debug("Testing Insertion");
	// // Add a value
	// LinkProperties config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("192.180.0");
	// config.setSrc_prefix_len(55);
	// config.setDest_prefix("237.42.23");
	// config.setDest_prefix_len(15);
	// config.setRelation("Allow");
	// config.setLinkCapacity(1d);
	// config.setDelay(15d);
	// dao.addLinkPropertiesEntry(config);
	//
	// // Retrieve added values
	// assertTrue(dao.getLinkProperties("192.180.0.15", "237.42.23.3")
	// .getRelation().equalsIgnoreCase("Allow"));
	// }
	//
	// @Test
	// public void testGettingProperies() {
	// logger.info("Testing the retrieving of properties");
	// // first of all clean the database
	// dao.clear();
	//
	// // fill the database
	// createSomeEntries();
	//
	// // test the method getAllMeteringConfigEntries()
	//
	// // test for an empty range
	// assertTrue(dao.getAllLinkPropertiesEntries(0, 0).size() == 0);
	//
	// // test for the complete range
	// List<LinkProperties> list = dao.getAllLinkPropertiesEntries(0, 9);
	// assertTrue(list.size() == 9);
	// // assertTrue(list.get(0).getRelation().equals("Valid"));
	// // assertTrue(list.get(list.size() - 1).getRelation().equals("Invalid"));
	//
	// // test for an arbitrary range
	// list = dao.getAllLinkPropertiesEntries(3, 4);
	// assertTrue(list.size() == 4);
	// // assertTrue(list.get(0).getRelation().equals("Valid"));
	// // assertTrue(list.get(list.size() - 1).getRelation().equals("Invalid"));
	//
	// // test for a too large range
	// assertTrue(dao.getAllLinkPropertiesEntries(0, 234).size() == 9);
	//
	// // clear the database
	// dao.clear();
	// }
	//
	// @Test
	// public void testQueryGeneration() {
	// logger.info("Testing the query-generation");
	// // first of all clean the database
	// dao.clear();
	// // Add a value
	// LinkProperties config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("192.168.0");
	// config.setSrc_prefix_len(5);
	// config.setDest_prefix("140.130.7");
	// config.setDest_prefix_len(18);
	// config.setRelation("Valid");
	// config.setLinkCapacity(0d);
	// config.setDelay(0d);
	// assertTrue(dao.addLinkPropertiesEntry(config));
	//
	// // use all parameters to retrieve this entry
	// LinkProperties search = dao.createLinkPropertiesEntry();
	// search.setSrc_prefix("192.168.0");
	// search.setSrc_prefix_len(5);
	// search.setDest_prefix("140.130.7");
	// search.setDest_prefix_len(18);
	// search.setRelation("Valid");
	// search.setLinkCapacity(0d);
	// search.setDelay(0d);
	// List<LinkProperties> list = dao.getLinkPropertiesEntries(search);
	// assertTrue(list.size() == 1);
	// assertTrue(list.get(0).getRelation().equals("Valid"));
	// }
	//
	// @Test
	// public void testCounter() {
	// logger.info("Testing the counter");
	// // first of all clean the database
	// dao.clear();
	//
	// // fill the database
	// createSomeEntries();
	//
	// // check for completeness
	// long countA = dao.countAll();
	// long countB = dao.count(dao.createLinkPropertiesEntry());
	// assertTrue(countA == countB && countA == 9);
	//
	// // check for partial matches
	// LinkProperties search = dao.createLinkPropertiesEntry();
	// search.setRelation("Valid");
	// assertTrue(dao.count(search) == 5);
	// search.setDelay(0d);
	// assertTrue(dao.count(search) == 3);
	// search.setRelation(null);
	// search.setDelay(1d);
	// assertTrue(dao.count(search) == 4);
	// search.setSrc_prefix("192.168.0");
	// search.setLinkCapacity(2d);
	// search.setDelay(null);
	// assertTrue(dao.count(search) == 2);
	// }
	//
	// @Test
	// public void testPerformanceDuringErrors() {
	// logger.info("Testing the performance while Errors");
	// // first of all clean the database
	// dao.clear();
	//
	// // Test for correct null handling
	// LinkProperties config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("190.180.0");
	// config.setSrc_prefix_len(null);
	// config.setDest_prefix("237.42.23");
	// config.setDest_prefix_len(15);
	// config.setRelation("Allow");
	// config.setLinkCapacity(1d);
	// config.setDelay(15d);
	// assertTrue(!dao.addLinkPropertiesEntry(config));
	//
	// // add a value
	// config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("190.180.0");
	// config.setSrc_prefix_len(55);
	// config.setDest_prefix("237.42.23");
	// config.setDest_prefix_len(15);
	// config.setRelation("Allow");
	// config.setLinkCapacity(1d);
	// config.setDelay(15d);
	// assertTrue(dao.addLinkPropertiesEntry(config));
	//
	// // create an error by adding a duplicate entry
	// config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("190.180.0");
	// config.setSrc_prefix_len(55);
	// config.setDest_prefix("237.42.23");
	// config.setDest_prefix_len(15);
	// config.setRelation("Allow");
	// config.setLinkCapacity(1d);
	// config.setDelay(15d);
	// //FIXME: should be false due to uniqueness constraint
	// //assertTrue(!dao.addLinkPropertiesEntry(config));
	//
	// // Now retrieve that value
	// assertTrue(dao.getLinkProperties("190.180.0.15", "237.42.23.3")
	// .getRelation().equalsIgnoreCase("Allow")
	// && dao.getLinkProperties("190.180.0.15", "237.42.23.3")
	// .getLinkCapacity() == 1d
	// && dao.getLinkProperties("190.180.0.15", "237.42.23.3")
	// .getDelay() == 15d);
	// }
	//
	// @Test
	// public void testReset() {
	// logger.debug("Testing Removal of all data");
	// dao.clear();
	//
	// // Check that values are gone
	// assertTrue(dao.getLinkProperties("192.180.0.15", "237.42.23.3") == null);
	// }
	//
	// @Test
	// public void testPrintContent() {
	// logger.info("Testing the printContent-method");
	//
	// // clear the database
	// dao.clear();
	//
	// // Add a value
	// LinkProperties config = dao.createLinkPropertiesEntry();
	// config.setSrc_prefix("192.168.0");
	// config.setSrc_prefix_len(5);
	// config.setDest_prefix("140.130.7");
	// config.setDest_prefix_len(18);
	// config.setRelation("Valid");
	// config.setLinkCapacity(0d);
	// config.setDelay(0d);
	// assertTrue(dao.addLinkPropertiesEntry(config));
	//
	// // print the content
	// String string = "Link Configurations:\n" + "\t" + "192.168.0" + "/" + 5
	// + " -> " + "140.130.7" + "/" + 18 + " : (" + 0.0 + " , " + 0.0
	// + " , " + "Valid" + ")" + "\n";
	// assertTrue(dao.printContent().equals(string));
	// }
	//
	// private void createSomeEntries() {
	// LinkProperties entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(1);
	// entry.setRelation("Valid");
	// entry.setLinkCapacity(0d);
	// entry.setDelay(0d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(2);
	// entry.setRelation("Valid");
	// entry.setLinkCapacity(0d);
	// entry.setDelay(1d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(3);
	// entry.setRelation("Valid");
	// entry.setLinkCapacity(1d);
	// entry.setDelay(0d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(4);
	// entry.setRelation("Valid");
	// entry.setLinkCapacity(1d);
	// entry.setDelay(1d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(5);
	// entry.setRelation("Valid");
	// entry.setLinkCapacity(2d);
	// entry.setDelay(0d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(6);
	// entry.setRelation("Invalid");
	// entry.setLinkCapacity(2d);
	// entry.setDelay(1d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(7);
	// entry.setRelation("Invalid");
	// entry.setLinkCapacity(3d);
	// entry.setDelay(0d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(8);
	// entry.setRelation("Invalid");
	// entry.setLinkCapacity(3d);
	// entry.setDelay(1d);
	// dao.addLinkPropertiesEntry(entry);
	// entry = dao.createLinkPropertiesEntry();
	// entry.setSrc_prefix("192.168.0");
	// entry.setSrc_prefix_len(5);
	// entry.setDest_prefix("140.130.7");
	// entry.setDest_prefix_len(9);
	// entry.setRelation("Invalid");
	// entry.setLinkCapacity(4d);
	// entry.setDelay(0d);
	// dao.addLinkPropertiesEntry(entry);
	// }
}
