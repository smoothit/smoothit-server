package eu.smoothit.sis.db.test;

import java.util.List;

import javax.ejb.EJBException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatisticsEntry;

/**
 * 
 * @author Jonas Flick
 * 
 */
public class PersistentHAPEntryDAOTest extends TestCase {

	protected Logger logger = Logger
			.getLogger(PersistentHAPEntryDAOTest.class);

	public PersistentHAPEntryDAOTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing testBasicFunctionality");
		IHAPEntryDAO dao = factory.createHAPEntryDAO();

		// for safety, clear the database first
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add a value
		HAPEntry pse = new HAPEntry("127.0.0.1", 1234,true);
		dao.persist(pse);
		assertTrue(dao.countAll() == 1);
		// now clear the database again
		dao.removeAll();
		assertTrue(dao.countAll() == 0);

		// add new entry again
		pse = new HAPEntry();

		pse.setIp_address("127.0.0.2");
		pse.setListenport(1234);
		pse.setHap_flag(true);

		dao.persist(pse);
		assertTrue(dao.countAll() == 1);
		assertTrue(dao.getPromotedHAPs().size() == 0);
//		System.out.println(dao.getPromotedHAPs());
		assertTrue(dao.getNewHAPs().size() == 1);
//		System.out.println(dao.getNewHAPs());
		pse = new HAPEntry();

		pse.setIp_address("127.0.0.2");
		pse.setListenport(1234);
		pse.setHap_flag(true);

		List<HAPEntry> list = dao.get(pse);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getIp_address().equals("127.0.0.2"));
		assertTrue(list.get(0).getListenport() == 1234);
		assertTrue(list.get(0).getHap_flag());
	}

	
	
	public void testBasicFunctionalityDuplicateEntry() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing testBasicFunctionality");
		IHAPEntryDAO dao = factory.createHAPEntryDAO();

		// for safety, clear the database first
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add a value
		HAPEntry pse = new HAPEntry("127.0.0.1", 1234,true);
		dao.persist(pse);
		assertTrue(dao.countAll() == 1);
		// now clear the database again
		dao.removeAll();
		assertTrue(dao.countAll() == 0);

		// add new entry again
		pse = new HAPEntry();
		pse.setIp_address("127.0.0.2");
		pse.setListenport(1234);
		pse.setHap_flag(true);
		dao.persist(pse);
		// add new entry twice
		pse = new HAPEntry();
		pse.setIp_address("127.0.0.2");
		pse.setListenport(1234);
		pse.setHap_flag(true);
		dao.persist(pse);
		
		assertTrue(dao.countAll() == 2);
		assertTrue(dao.getPromotedHAPs().size() == 0);
//		System.out.println(dao.getPromotedHAPs());
		assertTrue(dao.getNewHAPs().size() == 2);
//		System.out.println(dao.getNewHAPs());

		pse = new HAPEntry();
		pse.setIp_address("127.0.0.2");
		pse.setListenport(1234);
		pse.setHap_flag(true);
		
		List<HAPEntry> list = dao.get(pse);
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getIp_address().equals("127.0.0.2"));
		assertTrue(list.get(0).getListenport() == 1234);
		assertTrue(list.get(0).getHap_flag());
		
		assertTrue(list.get(1).getIp_address().equals("127.0.0.2"));
		assertTrue(list.get(1).getListenport() == 1234);
		assertTrue(list.get(1).getHap_flag());
	}
	
}
