package eu.smoothit.sis.db.test;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

/**
 * 
 * @author Jonas Flick
 * 
 */
public class PersistentComponentConfigDAOTest extends TestCase {

	
	
	protected Logger logger = Logger
	.getLogger(PersistentComponentConfigDAOTest.class);
	
	
	public PersistentComponentConfigDAOTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing Removal of all data");
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		
		// for safety, clear the database first
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add a value
		ComponentConfigEntry ce = new ComponentConfigEntry("TestComp","Foo","Bar");
		dao.persist(ce);
		assertTrue(dao.countAll() == 1);
		// now clear the database again
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		
		//add new entry again
		ce = new ComponentConfigEntry();
		ce.setComponent("TestComp");
		ce.setPropName("Foo");
		ce.setValue("Bar");
		dao.persist(ce);
		assertTrue(dao.countAll() == 1);
		
		ce = new ComponentConfigEntry();
		ce.setComponent("TestComp");
		List<ComponentConfigEntry> list = dao.get(ce);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getPropName().equals("Foo"));
		assertTrue(list.get(0).getValue().equals("Bar"));
		
	}

	public void testIndividualFunctionality() {
		// for safety, clear the database first
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		
		//insert one entry
		ComponentConfigEntry ce = new ComponentConfigEntry();
		ce.setComponent("TestComp");
		ce.setPropName("Foo");
		ce.setValue("Bar");
		dao.persist(ce);
		assertTrue(dao.countAll() == 1);
		
		List<ComponentConfigEntry> list = dao.findByComponent("TestComp");
		assertTrue(list.size() == 1);
		
		ComponentConfigEntry entry = dao.findByComponentAndName("TestComp","Foo");
		assertNotNull(entry);
		assertTrue(entry.getValue().equals("Bar"));
		
	}



	
	
//
//	@Test
//	public void testDefaultValues() {
//		logger.info("Testing default return values");
//		// Start with Clean db
//		dao.clear();
//		// Properties that don't exist return null
//		// add a value
//		PropsConfig configEntry = dao.createPropsConfig();
//		assertTrue(dao.getProperties(configEntry).size() == 0);
//	}
//
//	@Test
//	public void testAddingOfValues() {
//		logger.info("Testing Adding of values");
//		// now clear the database
//		dao.clear();
//
//		// Add some vals
//		PropsConfig configEntry = dao.createPropsConfig();
//		configEntry.setComponent("TestComp1");
//		configEntry.setPropName("Foo1");
//		configEntry.setValue("Bar11");
//		assertTrue(dao.addProperty(configEntry));
//		PropsConfig configEntry2 = dao.createPropsConfig();
//		configEntry2.setComponent("TestComp1");
//		configEntry2.setPropName("Foo2");
//		configEntry2.setValue("Bar12");
//		assertTrue(dao.addProperty(configEntry2));
//		PropsConfig configEntry3 = dao.createPropsConfig();
//		configEntry3.setComponent("TestComp2");
//		configEntry3.setPropName("Foo1");
//		configEntry3.setValue("Bar21");
//		assertTrue(dao.addProperty(configEntry3));
//		PropsConfig configEntry4 = dao.createPropsConfig();
//		configEntry4.setComponent("TestComp2");
//		configEntry4.setPropName("Foo2");
//		configEntry4.setValue("Bar22");
//		assertTrue(dao.addProperty(configEntry4));
//
//		// Check if the four values were added
//		assertTrue(dao.countAll() == 4);
//
//		// find a certain one
//		PropsConfig searchEntry = dao.createPropsConfig();
//		searchEntry.setComponent("TestComp1");
//		searchEntry.setPropName("Foo1");
//		PropsConfig entry = dao.getProperties(searchEntry).get(0);
//		assertTrue(entry.getValue().equals("Bar11"));
//	}
//
//	@Test
//	public void testUpdatingOfValues() {
//		logger.info("Testing Updating of Values");
//		// first of all clean the database
//		dao.clear();
//
//		// add a value
//		PropsConfig ce = dao.createPropsConfig();
//		ce.setComponent("component1");
//		ce.setPropName("propName2");
//		ce.setValue(10);
//		dao.addProperty(ce);
//
//		// Now retrieve that value
//		PropsConfig searchEntry = dao.createPropsConfig();
//		searchEntry.setComponent("component1");
//		searchEntry.setPropName("propName2");
//		PropsConfig resultEntry = dao.getProperties(searchEntry).get(0);
//		assertTrue(resultEntry.getComponent().equals("component1"));
//		assertTrue(resultEntry.getPropName().equals("propName2"));
//		assertTrue(resultEntry.getValue().equals(10));
//
//		// Change that value again with the other method
//		searchEntry.setValue(null);
//		resultEntry = dao.getProperties(searchEntry).get(0);
//		resultEntry.setValue("value1339");
//		dao.updateProperty(resultEntry);
//
//		// Now retrieve that changed value
//		resultEntry = dao.getProperties(searchEntry).get(0);
//		assertTrue(resultEntry.getComponent().equals("component1"));
//		assertTrue(resultEntry.getPropName().equals("propName2"));
//		assertTrue(resultEntry.getValue().equals("value1339"));
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
//		// check for the getProperties()
//		assertTrue(dao.getProperties(dao.createPropsConfig()).size() == 9);
//
//		PropsConfig searchConfig = dao.createPropsConfig();
//		searchConfig.setComponent("1");
//		List<PropsConfig> list = dao.getProperties(searchConfig);
//		assertTrue(list.size() == 3);
////		assertTrue(list.get(0).getValue().equals("11"));
////		assertTrue(list.get(1).getValue().equals("12"));
////		assertTrue(list.get(2).getValue().equals("13"));
//
//		PropsConfig searchConfig2 = dao.createPropsConfig();
//		searchConfig2.setPropName("2");
//		list = dao.getProperties(searchConfig2);
//		assertTrue(list.size() == 3);
////		assertTrue(list.get(0).getValue().equals("12"));
////		assertTrue(list.get(1).getValue().equals("22"));
////		assertTrue(list.get(2).getValue().equals("32"));
//
//		// test the method getAllProperties()
//
//		// test for an empty range
//		assertTrue(dao.getAllProperties(0, 0).size() == 0);
//
//		// test for the complete range
//		list = dao.getAllProperties(0, 9);
//		assertTrue(list.size() == 9);
////		assertTrue(list.get(0).getValue().equals("11"));
////		assertTrue(list.get(list.size() - 1).getValue().equals("33"));
//
//		// test for an arbitrary range
//		list = dao.getAllProperties(3, 4);
//		assertTrue(list.size() == 4);
////		assertTrue(list.get(0).getValue().equals("21"));
////		assertTrue(list.get(list.size() - 1).getValue().equals("31"));
//
//		// test for a too large range
//		assertTrue(dao.getAllProperties(0, 234).size() == 9);
//	}
//
//	@Test
//	public void testPersistence() {
//		logger.info("Testing persistence");
//		// first of all clean the database
//		dao.clear();
//
//		// add a value
//		PropsConfig ce = dao.createPropsConfig();
//		ce.setComponent("TestComp");
//		ce.setPropName("Foo");
//		ce.setValue("Bar");
//		dao.addProperty(ce);
//
//		// Now retrieve that value
//		PropsConfig search = dao.createPropsConfig();
//		search.setComponent("TestComp");
//		search.setPropName("Foo");
//		PropsConfig entry = dao.getProperties(search).get(0);
//		assertTrue(entry.getValue().equals("Bar"));
//
//		// now check what happens if we restart the database
//		// restartBackend();
//		// Recreate the DAO
//		dao = SisDAOFactory.getFactory().createComponentConfigDAO();
//		// With PersistentDAO there should be persitence after recreate
//		// So values will still be there
//		entry = dao.getProperties(search).get(0);
//		assertNotNull(entry.getValue());
//		assertTrue(entry.getValue().equals("Bar"));
//	}
//
//	@Test
//	public void testRemoval() {
//		logger.info("Testing Removal");
//		// first of all clean the database
//		dao.clear();
//
//		// add a value
//		PropsConfig ce = dao.createPropsConfig();
//		ce.setComponent("TestComp");
//		ce.setPropName("Foo");
//		ce.setValue("Bar");
//		dao.addProperty(ce);
//
//		// remove a value
//		PropsConfig rem = dao.createPropsConfig();
//		rem.setComponent("TestComp");
//		rem.setPropName("Foo");
//		dao.removeProperties(rem);
//
//		// Assert that it's gone
//		assertTrue(dao.getProperties(rem).size() == 0);
//
//		// add a value
//		ce = dao.createPropsConfig();
//		ce.setComponent("TestComp");
//		ce.setPropName("Foo");
//		ce.setValue("Bar");
//		dao.addProperty(ce);
//
//		// clean the database
//		dao.clear();
//
//		// Fill the database
//		createSomeEntries();
//
//		assertTrue(dao.countAll() == 9);
//		rem.setComponent("1");
//		rem.setPropName(null);
//		dao.removeProperties(rem);
//		assertTrue(dao.countAll() == 6);
//		assertTrue(dao.getProperties(rem).size() == 0);
//		rem.setComponent(null);
//		rem.setPropName("2");
//		dao.removeProperties(rem);
//		assertTrue(dao.countAll() == 4);
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
//		PropsConfig search = dao.createPropsConfig();
//		long countB = dao.count(search);
//		assertTrue(countA == countB && countA == 9);
//
//		// check for partial matches
//		search.setPropName("1");
//		assertTrue(dao.count(search) == 3);
//		search.setPropName("2");
//		assertTrue(dao.count(search) == 3);
//		search.setPropName("3");
//		assertTrue(dao.count(search) == 3);
//		search.setComponent("1");
//		search.setPropName(null);
//		assertTrue(dao.count(search) == 3);
//		search.setComponent("2");
//		assertTrue(dao.count(search) == 3);
//		search.setComponent("3");
//		assertTrue(dao.count(search) == 3);
//		search.setComponent("1");
//		search.setPropName("1");
//		assertTrue(dao.count(search) == 1);
//	}
//
//	@Test
//	public void testPerformanceDuringErrors() {
//		logger.info("Testing the performance while Errors");
//		// first of all clean the database
//		dao.clear();
//
//		PropsConfig config = dao.createPropsConfig();
//		config.setComponent("TestComp");
//		config.setValue("Bar");
//		//assertTrue(!dao.addProperty(config));
//
//		// add a value
//		config = dao.createPropsConfig();
//		config.setComponent("TestComp");
//		config.setPropName("Foo");
//		config.setValue("Bar");
//		assertTrue(dao.addProperty(config));
//
//		// create an error by adding a duplicate entry
//		config = dao.createPropsConfig();
//		config.setComponent("TestComp");
//		config.setPropName("Foo");
//		config.setValue("Bar2");
//		//FIXME: should be false due to uniqueness constraint
//		//assertTrue(!dao.addProperty(config));
//		assertTrue(dao.countAll()==1);
//
//		// Now retrieve that value
//		PropsConfig search = dao.createPropsConfig();
//		search.setComponent("TestComp");
//		search.setPropName("Foo");
//		PropsConfig entry = dao.getProperties(search).get(0);
//		assertTrue(entry.getValue().equals("Bar"));
//	}
//
//	@Test
//	public void testPrintContent() {
//		logger.info("Testing the printContent-method");
//
//		// clear the database
//		dao.clear();
//
//		// Add a value
//		PropsConfig config = dao.createPropsConfig();
//		config.setComponent("TestComp");
//		config.setPropName("Foo");
//		config.setValue("Bar");
//		assertTrue(dao.addProperty(config));
//
//		// print the content
//		PropsConfig search = dao.createPropsConfig();
//		PropsConfigEntry entry = (PropsConfigEntry) dao.getProperties(search)
//				.get(0);
//		String string = "Properties:\n" + "\t" + "ID = " + entry.getId()
//				+ " ; " + "TestComp" + "." + "Foo" + " = " + "Bar" + "\n";
//		assertTrue(dao.printContent().equals(string));
//	}
//
//	private void createSomeEntries() {
//		PropsConfig a1 = dao.createPropsConfig();
//		a1.setComponent("1");
//		a1.setPropName("1");
//		a1.setValue("11");
//		dao.addProperty(a1);
//		PropsConfig a2 = dao.createPropsConfig();
//		a2.setComponent("1");
//		a2.setPropName("2");
//		a2.setValue("12");
//		dao.addProperty(a2);
//		PropsConfig a3 = dao.createPropsConfig();
//		a3.setComponent("1");
//		a3.setPropName("3");
//		a3.setValue("13");
//		dao.addProperty(a3);
//		PropsConfig a4 = dao.createPropsConfig();
//		a4.setComponent("2");
//		a4.setPropName("1");
//		a4.setValue("21");
//		dao.addProperty(a4);
//		PropsConfig a5 = dao.createPropsConfig();
//		a5.setComponent("2");
//		a5.setPropName("2");
//		a5.setValue("22");
//		dao.addProperty(a5);
//		PropsConfig a6 = dao.createPropsConfig();
//		a6.setComponent("2");
//		a6.setPropName("3");
//		a6.setValue("23");
//		dao.addProperty(a6);
//		PropsConfig a7 = dao.createPropsConfig();
//		a7.setComponent("3");
//		a7.setPropName("1");
//		a7.setValue("31");
//		dao.addProperty(a7);
//		PropsConfig a8 = dao.createPropsConfig();
//		a8.setComponent("3");
//		a8.setPropName("2");
//		a8.setValue("32");
//		dao.addProperty(a8);
//		PropsConfig a9 = dao.createPropsConfig();
//		a9.setComponent("3");
//		a9.setPropName("3");
//		a9.setValue("33");
//		dao.addProperty(a9);
//	}

}
