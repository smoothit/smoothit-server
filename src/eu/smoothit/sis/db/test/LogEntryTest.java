/**
 * 
 */
package eu.smoothit.sis.db.test;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.impl.entities.LogEntry;

/**
 * @author christian, Tomasz Stradomski
 * 
 */
public class LogEntryTest extends TestCase {

	protected Logger logger = Logger.getLogger(LogEntryTest.class);


	public LogEntryTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		ILogEntryDAO dao = factory.createLogEntryDAO();

		dao.removeAll();
		//create one new entry
		LogEntry entry = new LogEntry();
		entry.setEvent("TestEvent");
		entry.setResult("theResult");
		entry.setUsername("user1");
		dao.persist(entry);
		assertTrue(dao.countAll() == 1);
		logger.info(dao.printAll());
		
		//retrieve inserted entry
		entry = new LogEntry();
		entry.setEvent("TestEvent");
		List<LogEntry> list = dao.get(entry);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getResult().equals("theResult"));
		assertTrue(list.get(0).getUsername().equals("user1"));
		
		
		//retrieve inserted entry once again and compare with previous result
		LogEntry secondEntry = new LogEntry();
		entry.setEvent("TestEvent");
		List<LogEntry> secondList = dao.get(secondEntry);
		assertTrue(secondList.size() == 1);
		assertTrue(list.get(0).getDate().equals(secondList.get(0).getDate()));
		assertEquals(list.get(0).getId(), secondList.get(0).getId());
		assertEquals(list.get(0).hashCode(), secondList.get(0).hashCode());
		assertTrue(list.get(0).equals(secondList.get(0)));
		
		assertTrue(list.get(0).equals(list.get(0)));
		assertFalse(list.get(0).equals(""));
		assertFalse(list.get(0).equals(null));
		assertFalse(list.get(0).equals(new LogEntry()));
		
		//now change entry
		entry = list.get(0);
		entry.setEvent("TestEventUpdate");
		dao.update(entry);
		assertTrue(dao.countAll() == 1);
		logger.info(dao.printAll());
		
		//verify that entry has been updated successfully
		entry = new LogEntry();
		entry.setEvent("TestEventUpdate");
		list = dao.get(entry);
		assertTrue(list.size() == 1);
		assertTrue(dao.countAll() == 1);
		assertTrue(list.get(0).getResult().equals("theResult"));
		assertTrue(list.get(0).getUsername().equals("user1"));
	
		//now remove entry
		dao.remove(list.get(0));
		assertTrue(dao.countAll() == 0);
		
		entry = new LogEntry();
		entry.setEvent("TestEvent");
		entry.setResult("theResult");
		entry.setUsername("user1");
		dao.persist(entry);
		assertTrue(dao.countAll() == 1);
		logger.info(dao.printAll());
		
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		

	}

}
