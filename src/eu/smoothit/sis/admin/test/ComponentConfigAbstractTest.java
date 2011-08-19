/**
 * 
 */
package eu.smoothit.sis.admin.test;


import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import eu.smoothit.sis.admin.backendBean.superclass.ComponentConfigAbstract;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import static org.junit.Assert.assertEquals;
/**
 * @author Ye
 *
 */
public class ComponentConfigAbstractTest {
	ComponentConfigAbstract config;
	Map<String, String> cachedProperties;
	Map<String, String> storedProperties;
	String componentName="Controler-IoP-Config";
	String PARAM_IOP_OPERATION_MODE="IoP-Operation-Mode";
	String operationModeInput;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		IComponentConfigDAO dataDAO = (IComponentConfigDAO) SisDAOFactory
		.getFactory().createComponentConfigDAO();
		cachedProperties = new HashMap<String, String>();
		storedProperties = new HashMap<String, String>();
		String operationModeStored="mode1";
		// clear existing data on db
		dataDAO.removeAll();
		// store one entry
		ComponentConfigEntry ConfigEntry = new ComponentConfigEntry();
		ConfigEntry.setComponent(componentName);
		ConfigEntry.setPropName(PARAM_IOP_OPERATION_MODE);
		ConfigEntry.setValue(operationModeStored);
		dataDAO.persist(ConfigEntry);
		// simulate input from db
		storedProperties.put(PARAM_IOP_OPERATION_MODE, operationModeStored);
		//TODO user mock instead of dao
//		IComponentConfigDAO daoMock=mock(IComponentConfigDAO.class);
		config=new ComponentConfigAbstract();
		config.setDataDAO(dataDAO);
		config.setUnitTest(true);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testSaveComponentConfigEntries(){		

		//test update
		operationModeInput="mode2";
		// simulate an input from UI
		cachedProperties.put(PARAM_IOP_OPERATION_MODE, operationModeInput);
		config.saveComponentConfigEntries(cachedProperties, storedProperties, componentName);
		assertEquals("feedback_updated:IoP-Operation-Mode<br>",config.getUnitTestFeedback());
		// test insert
		String newProperty= "Metering-BGP-File-Name";
		operationModeInput="new property value";
		cachedProperties.clear();
		cachedProperties.put(newProperty, operationModeInput);
		config.saveComponentConfigEntries(cachedProperties, storedProperties, componentName);
		assertEquals("feedback_add:Metering-BGP-File-Name<br>",config.getUnitTestFeedback());
		// test remove
		
	}

}
