package eu.smoothit.sis.db.test;

import java.util.List;

import javax.ejb.EJBException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.entities.PeerStatisticsEntry;

/**
 * 
 * @author Jonas Flick
 * 
 */
public class PersistentPeerStatisticsDAOTest extends TestCase {

	protected Logger logger = Logger
			.getLogger(PersistentPeerStatisticsDAOTest.class);

	public PersistentPeerStatisticsDAOTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing testBasicFunctionality");
		IPeerStatisticsDAO dao = factory.createPeerStatisticsDAO();

		// for safety, clear the database first
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add a value
		PeerStatisticsEntry pse = new PeerStatisticsEntry("127.0.0.1", 1234,
				123.1, 234.2, 345.3, 456.4);
		dao.persist(pse);
		assertTrue(dao.countAll() == 1);
		// now clear the database again
		dao.removeAll();
		assertTrue(dao.countAll() == 0);

		// add new entry again
		pse = new PeerStatisticsEntry();

		pse.setIp_address("127.0.0.2");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);

		dao.persist(pse);
		assertTrue(dao.countAll() == 1);

		pse = new PeerStatisticsEntry();
		pse.setIp_address("127.0.0.2");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);

		System.out.println(dao.getPeers());
		
		
		List<PeerStatisticsEntry> list = dao.get(pse);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getIp_address().equals("127.0.0.2"));
		assertTrue(list.get(0).getPort() == 1234);
		assertTrue(list.get(0).getLocal_upload_volume() == 987.6);
		assertTrue(list.get(0).getTotal_download_volume() == 876.5);
		assertTrue(list.get(0).getTotal_upload_volume() == 765.4);
		assertTrue(list.get(0).getHap_rating() == 654.3);

	}

	public void testTableConstraintsWithException() {
		/*
		 * Test with two times the same entry
		 */
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing testBasicFunctionality");
		IPeerStatisticsDAO dao = factory.createPeerStatisticsDAO();

		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add new entry again
		PeerStatisticsEntry pse = new PeerStatisticsEntry();

		pse.setIp_address("127.0.0.2");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);

		dao.persist(pse);

		// add new entry again
		pse = new PeerStatisticsEntry();

		pse.setIp_address("127.0.0.2");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);

		try {
			dao.persist(pse);
			assertTrue(false);
		} catch (EJBException e) {
			assertTrue(dao.countAll() == 1);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	public void testTableConstraintsWithoutException() {
		/*
		 * Test with almost same entry except port number
		 */
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing testBasicFunctionality");
		IPeerStatisticsDAO dao = factory.createPeerStatisticsDAO();

		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add new entry again
		PeerStatisticsEntry pse = new PeerStatisticsEntry();

		pse.setIp_address("127.0.0.2");
		pse.setPort(1235);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);

		dao.persist(pse);

		// add new entry again
		pse = new PeerStatisticsEntry();

		pse.setIp_address("127.0.0.2");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);

		try {
			dao.persist(pse);
			assertTrue(dao.countAll() == 2);
		} catch (EJBException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	public void testTableConstraintsWithoutException2() {
		/*
		 * Test with almost same entry except IP address
		 */
		SisDAOFactory factory = SisDAOFactory.getFactory();
		logger.info("Testing testBasicFunctionality");
		IPeerStatisticsDAO dao = factory.createPeerStatisticsDAO();
	
		dao.removeAll();
		assertTrue(dao.countAll() == 0);
		// add new entry again
		PeerStatisticsEntry pse = new PeerStatisticsEntry();
	
		pse.setIp_address("127.0.0.1");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);
	
		dao.persist(pse);
	
		// add new entry again
		pse = new PeerStatisticsEntry();
	
		pse.setIp_address("127.0.0.2");
		pse.setPort(1234);
		pse.setLocal_upload_volume(987.6);
		pse.setTotal_download_volume(876.5);
		pse.setTotal_upload_volume(765.4);
		pse.setHap_rating(654.3);
	
		try {
			dao.persist(pse);
			assertTrue(dao.countAll() == 2);
		} catch (EJBException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

}
