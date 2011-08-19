package eu.smoothit.sis.db.test;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.db.impl.utils.DAOUtils;

public class PersistentIPRangeDAOTest extends TestCase {

	
	
	protected Logger logger = Logger
	.getLogger(PersistentIPRangeDAOTest.class);
	
	
	public PersistentIPRangeDAOTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		// clear the database
		IPRangeDAO dao = factory.createIPRangeDAO();
		dao.removeAll();

		// add values and create objects as reference
		IPRangeConfigEntry entry = new IPRangeConfigEntry();
		entry.setPrefix("190.180.0");
		entry.setPrefix_len(55);
		entry.setLocal(true);
		entry.setASPathLength(200);
		entry.setLocalPreference(10);
		entry.setMED(5);
		dao.persist(entry);
		assertTrue(dao.countAll() == 1);
		
		entry = new IPRangeConfigEntry();
		entry.setPrefix("190.180.0");
		entry.setPrefix_len(55);
		entry.setLocal(true);
		entry.setASPathLength(200);
		entry.setLocalPreference(10);
		entry.setMED(5);
		dao.get(entry);
		
		
		
		IPRangeConfigEntry local = new IPRangeConfigEntry();
		local.setPrefix("190.180.0");
		local.setPrefix_len(55);
		local.setLocal(true);
		local.setASPathLength(200);
		local.setLocalPreference(10);
		local.setMED(5);

		String string = "190.180.0" + "/" + 55 + ":(" + 200 + "," + 10 + ","
				+ 5 + ")";
		assertTrue(entry.toString().equals(string));

		IPRangeConfigEntry remote = new IPRangeConfigEntry();
		remote.setPrefix("237.42.23");
		remote.setPrefix_len(15);
		remote.setLocal(false);

		IPRangeConfigEntry entry2 = new IPRangeConfigEntry();
		entry2.setPrefix("237.42.23");
		entry2.setPrefix_len(15);
		entry2.setLocal(false);
		dao.persist(entry2);
		assertTrue(dao.countAll() == 2);

		// Retrieve added values
		IPRangeConfigEntry i = new IPRangeConfigEntry();
		i.setLocal(true);
		assertTrue(containsRange(dao.get(i), local));
		i.setLocal(false);
		assertTrue(containsRange(dao.get(i), remote));

		// Check that propertys of IPRanges are preserved
		i.setLocal(true);
		for (IPRangeConfigEntry r : dao.get(i)) {
			if (DAOUtils
					.equalRange(r, local.getPrefix(), local.getPrefix_len())) {
				assertEquals(200, r.getASPathLength().intValue());
				assertEquals(10, r.getLocalPreference().intValue());
				assertEquals(5, r.getMED().intValue());
			}
		}
		
	}

//	// public PersistentIPRangeDAOTest() {
//	// super(true);
//	// }
//
//	Logger logger = Logger.getLogger(PersistentIPRangeDAOTest.class);
//	protected IPRangeDAO dao;
//
//	public void createTheDAO() {
//		dao = SisDAOFactory.getFactory().createIPRangeDAO();
//	}
//
//	@Before
//	@Override
//	public void setUp() {
//		super.setUp();
//		createTheDAO();
//		assertNotNull(dao);
//	}
//
//	@Test
//	public void testDefaultValues() {
//		logger.info("Testing default return values");
//		// start fresh
//		dao.clear();
//		// Local IPRanges must be empty list
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setLocal(true);
//		assertTrue(dao.getIPRanges(entry).size() == 0);
//		// Remote likewise
//		entry.setLocal(false);
//		assertTrue(dao.getIPRanges(entry).size() == 0);
//	}
//
//	@Test
//	public void testInsertingOfValues() {

//	}
//
//	@Test
//	public void testAddingOfValues() {
//		logger.info("Testing Updating of Values");
//		// clear the database
//		dao.clear();
//
//		// add values and create objects as reference
//		IPRange rangeA = dao.createIPRangeEntry();
//		rangeA.setPrefix("192.168.0");
//		rangeA.setPrefix_len(5);
//
//		// add a value
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("192.168.0");
//		entry.setPrefix_len(5);
//		entry.setLocal(true);
//		dao.addIPRange(entry);
//
//		IPRange rangeB = dao.createIPRangeEntry();
//		rangeB.setPrefix("140.130.7");
//		rangeB.setPrefix_len(5);
//
//		// add a value
//		IPRange entry2 = dao.createIPRangeEntry();
//		entry2.setPrefix("140.130.7");
//		entry2.setPrefix_len(5);
//		entry2.setLocal(false);
//		dao.addIPRange(entry2);
//
//		// Now retrieve all these values
//		IPRange search = dao.createIPRangeEntry();
//		search.setLocal(true);
//		assertTrue(containsRange(dao.getIPRanges(search), rangeA));
//		assertTrue(!containsRange(dao.getIPRanges(search), rangeB));
//		search.setLocal(false);
//		assertTrue(containsRange(dao.getIPRanges(search), rangeB));
//		assertTrue(!containsRange(dao.getIPRanges(search), rangeA));
//
//		// Update values
//		// Former local range becomes now remote and wise versa
//		search.setPrefix("192.168.0");
//		search.setPrefix_len(5);
//		search.setLocal(true);
//		List<IPRange> list = dao.getIPRanges(search);
//		rangeA = list.get(0);
//		((IPRangeConfigEntry) rangeA).setLocal(false);
//		dao.updateIPRange(rangeA);
//		search.setPrefix("140.130.7");
//		search.setPrefix_len(5);
//		search.setLocal(false);
//		list = dao.getIPRanges(search);
//		rangeB = (IPRange) list.get(0);
//		((IPRangeConfigEntry) rangeB).setLocal(true);
//		dao.updateIPRange(rangeB);
//
//		// Now retrieve all these vals
//		search = dao.createIPRangeEntry();
//		search.setLocal(true);
//		assertTrue(!containsRange(dao.getIPRanges(search), rangeA));
//		assertTrue(containsRange(dao.getIPRanges(search), rangeB));
//		search.setLocal(false);
//		assertTrue(!containsRange(dao.getIPRanges(search), rangeB));
//		assertTrue(containsRange(dao.getIPRanges(search), rangeA));
//	}
//
//	@Test
//	public void testRemoval() {
//		logger.info("Testing Removal");
//		// first of all clean the database
//		dao.clear();
//
//		// add a value
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("190.180.0");
//		entry.setPrefix_len(55);
//		entry.setLocal(true);
//		entry.setASPathLength(200);
//		entry.setLocalPreference(10);
//		entry.setMED(5);
//		assertTrue(dao.addIPRange(entry));
//		assertTrue(dao.getIPRanges(dao.createIPRangeEntry()).size() == 1);
//
//		// remove a value
//		IPRange rem = dao.createIPRangeEntry();
//		rem.setPrefix("190.180.0");
//		rem.setPrefix_len(55);
//		dao.removeIPRanges(rem);
//
//		// Assert that it's gone
//		assertTrue(dao.getIPRanges(dao.createIPRangeEntry()).size() == 0);
//
//		// add a value
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("190.180.0");
//		entry.setPrefix_len(55);
//		entry.setLocal(true);
//		entry.setASPathLength(200);
//		entry.setLocalPreference(10);
//		entry.setMED(5);
//		assertTrue(dao.addIPRange(entry));
//		assertTrue(dao.addIPRange(entry));
//		assertTrue(dao.getIPRanges(dao.createIPRangeEntry()).size() == 1);
//
//		// remove a value
//		IPRange result = dao.getIPRanges(dao.createIPRangeEntry()).get(0);
//		dao.removeIPRanges(result);
//
//		// Assert that it's gone
//		assertTrue(dao.getIPRanges(dao.createIPRangeEntry()).size() == 0);
//
//		// clean the database
//		dao.clear();
//
//		// Fill the database
//		createSomeEntries();
//
//		assertTrue(dao.countAll() == 9);
//		rem = dao.createIPRangeEntry();
//		rem.setPrefix("140.130.7");
//		dao.removeIPRanges(rem);
//		assertTrue(dao.countAll() == 5);
//		assertTrue(dao.getIPRanges(rem).size() == 0);
//		rem.setPrefix("140.130.8");
//		rem.setLocal(false);
//		dao.removeIPRanges(rem);
//		assertTrue(dao.countAll() == 3);
//
//	}
//
//	@Test
//	public void testQueryGeneration() {
//		logger.info("Testing the query-generation");
//		// first of all clean the database
//		dao.clear();
//		// Add a value
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("190.180.0");
//		entry.setPrefix_len(55);
//		entry.setLocal(true);
//		entry.setASPathLength(200);
//		entry.setLocalPreference(10);
//		entry.setMED(5);
//		assertTrue(dao.addIPRange(entry));
//
//		// use all parameters to retrieve this entry
//		IPRange search = dao.createIPRangeEntry();
//		search.setPrefix("190.180.0");
//		search.setPrefix_len(55);
//		search.setLocal(true);
//		search.setASPathLength(200);
//		search.setLocalPreference(10);
//		search.setMED(5);
//		List<IPRange> list = dao.getIPRanges(search);
//		assertTrue(list.size() == 1);
//		assertTrue(list.get(0).getPrefix().equals("190.180.0"));
//	}
//
//	@Test
//	public void testGettingProperies() {
//		logger.info("Testing the retrieving of properties");
//		// first of all clean the database
//		dao.clear();
//
//		// fill the database
//		createSomeEntries();
//
//		// 
//		IPRange search = dao.createIPRangeEntry();
//		search.setPrefix("No Match");
//		List<IPRange> list = dao.getIPRanges(search);
//		assertNotNull(list);
//		assertTrue(list.size() == 0);
//
//		// check for the getIPRanges()
//		assertTrue(dao.getIPRanges(dao.createIPRangeEntry()).size() == 9);
//
//		search.setPrefix("140.130.7");
//		search.setASPathLength(10);
//		search.setLocalPreference(11);
//		search.setMED(12);
//		list = dao.getIPRanges(search);
//		assertTrue(list.size() == 4);
////		assertEquals((long) ((IPRangeConfigEntry) list.get(0)).getPrefix_len(),
////				1l);
////		assertEquals((long) ((IPRangeConfigEntry) list.get(1)).getPrefix_len(),
////				2l);
////		assertEquals((long) ((IPRangeConfigEntry) list.get(2)).getPrefix_len(),
////				3l);
////		assertEquals((long) ((IPRangeConfigEntry) list.get(3)).getPrefix_len(),
////				4l);
//
//		search = dao.createIPRangeEntry();
//		search.setPrefix("140.130.8");
//		search.setMED(12);
//		list = dao.getIPRanges(search);
//		assertTrue(list.size() == 5);
////		assertTrue(((IPRangeConfigEntry) list.get(0)).isLocal());
////		assertTrue(((IPRangeConfigEntry) list.get(1)).isLocal());
////		assertTrue(((IPRangeConfigEntry) list.get(2)).isLocal());
////		assertTrue(!((IPRangeConfigEntry) list.get(3)).isLocal());
////		assertTrue(!((IPRangeConfigEntry) list.get(4)).isLocal());
//
//		// test the method getAllIPRanges()
//
//		// test for an empty range
//		assertTrue(dao.getAllIPRanges(0, 0).size() == 0);
//
//		// test for the complete range
//		list = dao.getAllIPRanges(0, 9);
//		assertTrue(list.size() == 9);
////		assertTrue(!list.get(0).isLocal());
////		assertTrue(list.get(0).getPrefix_len() == 1);
////		assertTrue(list.get(list.size() - 1).getPrefix_len() == 5);
//
//		// test for an arbitrary range
//		list = dao.getAllIPRanges(3, 4);
//		assertTrue(list.size() == 4);
////		assertTrue(list.get(0).isLocal());
////		assertTrue(list.get(0).getPrefix_len() == 4);
////		assertTrue(list.get(list.size() - 1).getASPathLength() == 999);
//
//		// test for a too large range
//		assertTrue(dao.getAllIPRanges(0, 234).size() == 9);
//
//		// clear the database
//		dao.clear();
//	}
//
//	@Test
//	public void testUpdatingOfValue() {
//		logger.info("Testing Updating of Values");
//		// first of all clean the database
//		dao.clear();
//
//		// add a value
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(18);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		assertTrue(dao.addIPRange(entry));
//
//		// Now retrieve that value
//		IPRange search = dao.createIPRangeEntry();
//		search.setPrefix("140.130.7");
//		search.setPrefix_len(18);
//		search.setLocal(false);
//		search.setASPathLength(10);
//		search.setLocalPreference(11);
//		search.setMED(12);
//		IPRangeConfigEntry result = (IPRangeConfigEntry) dao
//				.getIPRanges(search).get(0);
//		assertTrue(!result.isLocal());
//		assertEquals((long) result.getASPathLength(), 10l);
//		assertEquals((long) result.getLocalPreference(), 11l);
//		assertEquals((long) result.getMED(), 12l);
//
//		result.setASPathLength(100);
//		result.setLocalPreference(101);
//		result.setMED(102);
//		dao.updateIPRange(result);
//
//		// Now retrieve that changed value
//		search.setASPathLength(null);
//		search.setLocalPreference(null);
//		search.setMED(null);
//		result = (IPRangeConfigEntry) dao.getIPRanges(search).get(0);
//		assertTrue(!result.isLocal());
//		assertEquals((long) result.getASPathLength(), 100l);
//		assertEquals((long) result.getLocalPreference(), 101l);
//		assertEquals((long) result.getMED(), 102l);
//
//		// clean the database
//		dao.clear();
//	}
//
//	@Test
//	public void testPersistence() {
//		logger.info("Testing persistence");
//
//		assertNotNull(dao);
//
//		// start with clean db
//		testReset();
//
//		// check db is empty
//		IPRange search = dao.createIPRangeEntry();
//		search.setLocal(true);
//		assertEquals(0, dao.getIPRanges(search).size());
//		search.setLocal(false);
//		assertEquals(0, dao.getIPRanges(search).size());
//
//		// now insert some values
//		testInsertingOfValues();
//		// now check what happens if we restart the database
//
//		// restartBackend();
//		// Recreate the DAO
//		createTheDAO();
//		// With PersistentDAO there should be persitence after recreate
//		// So values will still be there
//		search.setLocal(true);
//		assertTrue(dao.getIPRanges(search).size() > 0);
//		search.setLocal(false);
//		assertTrue(dao.getIPRanges(search).size() > 0);
//		// other cases!
//	}
//
//	@Test
//	public void testPerformanceDuringErrors() {
//		logger.info("Testing the performance while Errors");
//		// first of all clean the database
//		dao.clear();
//
//		// check for functioning while null
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(null);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		assertTrue(!dao.addIPRange(entry));
//
//		// add a value
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(18);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		assertTrue(dao.addIPRange(entry));
//
//		// create an error by adding a duplicate entry
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(18);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(13);
//		//FIXME: should be false due to uniqueness constraint
//		//assertTrue(!dao.addIPRange(entry));
//
//		IPRange search = dao.createIPRangeEntry();
//		search.setPrefix("140.130.7");
//		search.setPrefix_len(18);
//		IPRangeConfigEntry result = (IPRangeConfigEntry) dao
//				.getIPRanges(search).get(0);
//		result.setMED(13);
//		
//		dao.updateIPRange(result);
//
//		assertTrue(dao.countAll() == 1);
//
//		// Now retrieve that value
//		search = dao.createIPRangeEntry();
//		search.setPrefix("140.130.7");
//		search.setPrefix_len(18);
//		result = (IPRangeConfigEntry) dao
//				.getIPRanges(search).get(0);
//		assertTrue(!result.isLocal());
//		assertEquals((long) result.getASPathLength(), 10l);
//		assertEquals((long) result.getLocalPreference(), 11l);
//		assertEquals((long) result.getMED(), 13l);
//	}
//
//	@Test
//	public void testCounter() {
//		logger.info("Testing the counter");
//		// first of all clean the database
//		dao.clear();
//
//		// fill the database
//		createSomeEntries();
//
//		// check for completeness
//		long countA = dao.countAll();
//		long countB = dao.count(dao.createIPRangeEntry());
//		assertTrue(countA == countB && countA == 9);
//
//		// check for partial matches
//		IPRange search = dao.createIPRangeEntry();
//		search.setPrefix("140.130.7");
//		search.setLocal(false);
//		search.setASPathLength(10);
//		search.setLocalPreference(11);
//		search.setMED(12);
//		assertTrue(dao.count(search) == 3);
//		search = dao.createIPRangeEntry();
//		search.setPrefix_len(4);
//		search.setMED(12);
//		assertTrue(dao.count(search) == 2);
//		search = dao.createIPRangeEntry();
//		search.setLocal(false);
//		search.setASPathLength(999);
//		assertTrue(dao.count(search) == 2);
//		search = dao.createIPRangeEntry();
//		search.setPrefix("140.130.8");
//		search.setPrefix_len(5);
//		search.setLocal(false);
//		search.setMED(12);
//		assertTrue(dao.count(search) == 1);
//	}
//
//	@Test
//	public void testReset() {
//		logger.debug("Testing Removal of all data");
//		dao.clear();
//
//		// Check that values are gone
//		IPRange search = dao.createIPRangeEntry();
//		search.setLocal(true);
//		assertTrue("Config has " + dao.getIPRanges(search).size()
//				+ " entries, not 0!", dao.getIPRanges(search).size() == 0);
//		search.setLocal(false);
//		assertTrue(dao.getIPRanges(search).size() == 0);
//	}
//
//	@Test
//	public void testPrintContent() {
//		logger.info("Testing the printContent-method");
//
//		// clear the database
//		dao.clear();
//
//		// Add a local and foreign IPRange
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("190.180.0");
//		entry.setPrefix_len(55);
//		entry.setLocal(true);
//		entry.setASPathLength(200);
//		entry.setLocalPreference(10);
//		entry.setMED(5);
//		assertTrue(dao.addIPRange(entry));
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("190.180.1");
//		entry.setPrefix_len(55);
//		entry.setLocal(false);
//		entry.setASPathLength(200);
//		entry.setLocalPreference(10);
//		entry.setMED(5);
//		assertTrue(dao.addIPRange(entry));
//
//		// print the content
//		String string = "Local IPRanges:\n" + "\t" + "190.180.0" + "/" + 55
//				+ ":(" + 200 + "," + 10 + "," + 5 + ")" + "\n";
//		string += "Remote IPRanges:\n" + "\t" + "190.180.1" + "/" + 55 + ":("
//				+ 200 + "," + 10 + "," + 5 + ")" + "\n";
//		assertTrue(dao.printContent().equals(string));
//	}
//
	private boolean containsRange(List<IPRangeConfigEntry> ranges, IPRangeConfigEntry theRange) {
		for (IPRangeConfigEntry aRange : ranges) {
			if (aRange.getPrefix().equals(theRange.getPrefix())
					&& aRange.getPrefix_len().equals(theRange.getPrefix_len())) {
				return true;
			}
		}
		return false;
	}
//
//	private void createSomeEntries() {
//		IPRange entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(1);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(2);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(3);
//		entry.setLocal(false);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.7");
//		entry.setPrefix_len(4);
//		entry.setLocal(true);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.8");
//		entry.setPrefix_len(1);
//		entry.setLocal(true);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.8");
//		entry.setPrefix_len(2);
//		entry.setLocal(true);
//		entry.setASPathLength(10);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.8");
//		entry.setPrefix_len(3);
//		entry.setLocal(true);
//		entry.setASPathLength(999);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.8");
//		entry.setPrefix_len(4);
//		entry.setLocal(false);
//		entry.setASPathLength(999);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//		entry = dao.createIPRangeEntry();
//		entry.setPrefix("140.130.8");
//		entry.setPrefix_len(5);
//		entry.setLocal(false);
//		entry.setASPathLength(999);
//		entry.setLocalPreference(11);
//		entry.setMED(12);
//		dao.addIPRange(entry);
//	}

}
