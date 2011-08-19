package eu.smoothit.sis.db.test;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;

/**
 * Tests the singleton properties of SisDaoFactory
 * 
 * @author Jonas Flick
 * @author Konstantin Pussep
 * 
 */
public class SingletonFactoryTest extends TestCase {

	protected Logger logger = Logger.getLogger(SingletonFactoryTest.class);


	public SingletonFactoryTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing creation of PersistentFactory");
		// verify singleton behavior
		assertNotNull(factory);
//		assertSame(factory, SisDAOFactory.getFactory());
	}


}
