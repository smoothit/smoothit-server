/**
 * 
 */
package eu.smoothit.sis.db.test;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IConnectedPeerStatusDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.db.impl.entities.ConnectedPeerStatusEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;

/**
 * @author christian
 * 
 */
public class PeerStatusTest extends TestCase {

	protected Logger logger = Logger.getLogger(PeerStatusTest.class);

	public PeerStatusTest() {
	}

	public void testBasicFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IPeerStatusDAO peer_status_dao = factory.createPeerStatusDAO();
		IConnectedPeerStatusDAO connected_peer_status_dao = factory
				.createConnectedPeerStatusDAO();

		peer_status_dao.removeAll();
		connected_peer_status_dao.removeAll();

		PeerStatusEntry ps = new PeerStatusEntry();
		ConnectedPeerStatusEntry cps = new ConnectedPeerStatusEntry();
		ConnectedPeerStatusEntry cps2 = new ConnectedPeerStatusEntry();

		ps.setDown_total(20.0F);
		ps.setFilename("test.txt");
		ps.setInfohash("tollerHash");
		ps.setListenport(2500);
		ps.setIp_address("192.168.2.1");
		// ps.setLive(5);
		ps.setP_dropped(6);
		ps.setP_late(7);
		// ps.setP_played(25.0F);
		ps.setPeer_id("peerID");
		ps.setProgress(20.0F);
		ps.setStatus("Fertig");
		ps.setT_prebuf(10);
		ps.setDown_rate(23F);
		ps.setIop_flag(true);
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

		// connected_peer_status_dao.persist(cps);
		peer_status_dao.persist(ps);

		ps = new PeerStatusEntry();

		ps.setDown_total(20.0F);
		// ps.setFilename("test.txt");
		// ps.setInfohash("tollerHash");
		// ps.setListenport(2500);
		// //ps.setLive(5);
		// ps.setP_dropped(6);
		// ps.setP_late(7);
		// //ps.setP_played(25.0F);
		// ps.setPeer_id("peerID");
		// ps.setProgress(20.0F);
		// ps.setStatus("Fertig");
		// ps.setT_prebuf(10);
		// ps.setT_stalled(20);
		// ps.setTimestamp(12345678F);
		// ps.setUp_rate(26F);
		// ps.setUp_total(25F);
		// ps.setValidRange("validRange");
		List<PeerStatusEntry> listps = peer_status_dao.getAll();
		// List<PeerStatusEntry> listps = peer_status_dao.get(ps);
		assertTrue(listps.size() == 1);
		ps = listps.get(0);
		assertTrue(ps != null);
		list = ps.getList();
		assertTrue(list != null);
		assertTrue(list.size() == 2);

		// List<ConnectedPeerStatusEntry> listcps =
		// connected_peer_status_dao.getAll();
		// remove second connected peer status entry
		// connected_peer_status_dao.remove(listcps.get(1));

		listps = peer_status_dao.get(new PeerStatusEntry());
		assertTrue(listps.size() == 1);
		ps = listps.get(0);
		assertTrue(ps != null);
		list = ps.getList();
		assertTrue(list != null);
		assertTrue(list.size() == 2);

		// remove peer status entry and ensure that depending
		// connected_peer_status_entry is deleted as well
		peer_status_dao.remove(listps.get(0));
		listps = peer_status_dao.get(new PeerStatusEntry());
		assertTrue(listps.size() == 0);

		List<ConnectedPeerStatusEntry> listcps = connected_peer_status_dao
				.get(new ConnectedPeerStatusEntry());
		assertTrue(listcps.size() == 0);

	}

	public void testBasicFunctionality2() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IPeerStatusDAO peer_status_dao = factory.createPeerStatusDAO();
		IConnectedPeerStatusDAO connected_peer_status_dao = factory
				.createConnectedPeerStatusDAO();

		peer_status_dao.removeAll();
		connected_peer_status_dao.removeAll();

		PeerStatusEntry ps = new PeerStatusEntry();
		ConnectedPeerStatusEntry cps = new ConnectedPeerStatusEntry();
		ConnectedPeerStatusEntry cps2 = new ConnectedPeerStatusEntry();

		long timestamp = System.currentTimeMillis();

		ps.setDown_total(20.0F);
		ps.setFilename("test.txt");
		ps.setIp_address("192.168.2.1");
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
		ps.setTimestamp(timestamp);
		ps.setUp_rate(26F);
		ps.setUp_total(25F);
		ps.setDown_rate(23F);
		ps.setIop_flag(true);
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

		// connected_peer_status_dao.persist(cps);
		peer_status_dao.persist(ps);

		ps = new PeerStatusEntry();

		ps.setDown_total(20.0F);
		ps.setFilename("test.txt");
		ps.setIp_address("192.168.2.1");
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
		ps.setTimestamp(timestamp);
		ps.setUp_rate(26F);
		ps.setUp_total(25F);
		ps.setDown_rate(23F);
		ps.setIop_flag(true);
		ps.setValidRange("validRange");
		List<PeerStatusEntry> listps = peer_status_dao.get(ps);

		assertTrue(listps.size() == 1);
		ps = listps.get(0);
		assertTrue(ps != null);
		list = ps.getList();
		assertTrue(list != null);
		assertTrue(list.size() == 2);

		listps = peer_status_dao.get(new PeerStatusEntry());
		assertTrue(listps.size() == 1);
		ps = listps.get(0);
		assertTrue(ps != null);
		list = ps.getList();
		assertTrue(list != null);
		assertTrue(list.size() == 2);

		// remove peer status entry and ensure that depending
		// connected_peer_status_entry is deleted as well

		list = connected_peer_status_dao.getAll();
		assertTrue(list.size() == 2);
		connected_peer_status_dao.remove(list.get(0));
		list = connected_peer_status_dao.get(new ConnectedPeerStatusEntry());
		assertTrue(list.size() == 0);

	}

	public void testAdvancedQueryFunctionality() {

		logger.setLevel(Level.DEBUG);
		/*
		 * insert data
		 */

		SisDAOFactory factory = SisDAOFactory.getFactory();
		IPeerStatusDAO peer_status_dao = factory.createPeerStatusDAO();
		IConnectedPeerStatusDAO connected_peer_status_dao = factory
				.createConnectedPeerStatusDAO();

		peer_status_dao.removeAll();
		connected_peer_status_dao.removeAll();

		/*
		 * test query functionality if no data is present
		 */

		logger.debug("**************START**************");

		logger.debug("Test for query functionality if database is empty\n");

		List<String> torrentHashs = peer_status_dao.getTorrents(60000);
		assertTrue(torrentHashs != null);
		logger.debug(torrentHashs);
		assertTrue(torrentHashs.size() == 0);

		logger.debug(peer_status_dao.getStatForAllTorrents(60000));

		for (String torrent : torrentHashs) {
			List<String> stat = peer_status_dao.getStatForTorrentHash(torrent,
					60000);
			assertTrue(stat != null);
			logger.debug("Statistics for torrent " + torrent);
			logger.debug(stat);
			logger.debug("List of peers for torrent  " + torrent);
			logger.debug(peer_status_dao.getListOfPeers(torrent, 60000));
			logger.debug("List of IoPs for torrent " + torrent);
			logger.debug(peer_status_dao.getListOfIops(torrent, 60000));
			assertTrue(stat.size() == 7);
		}

		logger.debug("*************DONE***************\n\n");

		/**
		 * first entry
		 */

		PeerStatusEntry ps = new PeerStatusEntry();
		ConnectedPeerStatusEntry cps = new ConnectedPeerStatusEntry();
		ConnectedPeerStatusEntry cps2 = new ConnectedPeerStatusEntry();

		ps.setDown_total(20.0F);
		ps.setFilename("test.txt");
		ps.setInfohash("tollerHash");
		ps.setIp_address("192.168.2.1");
		ps.setIop_flag(true);
		ps.setListenport(2500);
		// ps.setLive(5);
		ps.setP_dropped(6);
		ps.setP_late(7);
		// ps.setP_played(25.0F);
		ps.setPeer_id("peerID");
		ps.setProgress(20.0F);
		ps.setStatus("Fertig");
		ps.setT_prebuf(10);
		ps.setDown_rate(23F);
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

		peer_status_dao.persist(ps);

		/**
		 * second entry
		 */

		cps = new ConnectedPeerStatusEntry();
		cps2 = new ConnectedPeerStatusEntry();
		ps = new PeerStatusEntry();

		ps.setDown_total(20.0F);
		ps.setFilename("test.txt");
		ps.setInfohash("tollerHash2");
		ps.setIp_address("192.168.2.1");
		ps.setIop_flag(false);
		ps.setListenport(2500);
		// ps.setLive(5);
		ps.setP_dropped(6);
		ps.setP_late(7);
		// ps.setP_played(25.0F);
		ps.setPeer_id("peerID");
		ps.setProgress(20.0F);
		ps.setStatus("Fertig");
		ps.setT_prebuf(10);
		ps.setDown_rate(43F);
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

		list = new LinkedList<ConnectedPeerStatusEntry>();
		list.add(cps);
		list.add(cps2);
		ps.setList(list);

		peer_status_dao.persist(ps);

		/**
		 * third entry
		 */

		cps = new ConnectedPeerStatusEntry();
		cps2 = new ConnectedPeerStatusEntry();
		ps = new PeerStatusEntry();

		ps.setDown_total(30.0F);
		ps.setFilename("test.txt");
		ps.setInfohash("tollerHash2");
		ps.setIp_address("192.168.2.1");
		ps.setIop_flag(false);
		ps.setListenport(2500);
		// ps.setLive(5);
		ps.setP_dropped(6);
		ps.setP_late(7);
		// ps.setP_played(25.0F);
		ps.setPeer_id("peerID");
		ps.setProgress(20.0F);
		ps.setStatus("Fertig");
		ps.setT_prebuf(10);
		ps.setDown_rate(43F);
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

		list = new LinkedList<ConnectedPeerStatusEntry>();
		list.add(cps);
		list.add(cps2);
		ps.setList(list);

		peer_status_dao.persist(ps);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * test query functionality
		 */

		logger.debug("**************START**************");

		logger.debug("Test for query functionality with data");

		torrentHashs = peer_status_dao.getTorrents(60000);
		assertTrue(torrentHashs != null);
		logger.debug(torrentHashs);
		assertTrue(torrentHashs.size() == 2);

		logger.debug(peer_status_dao.getStatForAllTorrents(60000));

		for (String torrent : torrentHashs) {
			List<String> stat = peer_status_dao.getStatForTorrentHash(torrent,
					60000);
			assertTrue(stat != null);
			logger.debug("\nData for torrent: " + torrent);
			logger.debug("--------------------------------------------");
			logger.debug("Overall statistic for torrent: " + torrent);
			logger.debug(stat);
			logger.debug("\nList of peers for torrent: " + torrent);
			logger.debug(peer_status_dao.getListOfPeers(torrent, 60000));
			logger.debug("\nList of IoPs for torrent: " + torrent);
			logger.debug(peer_status_dao.getListOfIops(torrent, 60000));
			assertTrue(stat.size() == 7);
			logger.debug("--------------------------------------------\n\n");
		}

		peer_status_dao.removeAll();
		torrentHashs = peer_status_dao.getTorrents(60000);
		assertTrue(torrentHashs != null);
		logger.debug(torrentHashs);

		logger.debug("*************DONE***************\n\n");
	}

}
