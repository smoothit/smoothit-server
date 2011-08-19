package eu.smoothit.sis.db.test;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IClientSwarmInfoDAO;
import eu.smoothit.sis.db.impl.entities.ClientSwarmInfoEntry;

public class ClientSwarmInfoTest extends TestCase {

	protected Logger logger = Logger.getLogger(ClientSwarmInfoTest.class);

	public ClientSwarmInfoTest() {

	}

	public void testBasicFunctionality() {
		/*
		 * test for inserting two entries into the database
		 */
		Long timestamp = System.currentTimeMillis();

		SisDAOFactory factory = SisDAOFactory.getFactory();
		IClientSwarmInfoDAO csidao = factory.createClientSwarmInfoDAO();
		csidao.removeAll();

		ClientSwarmInfoEntry csiEntry = new ClientSwarmInfoEntry();
		csiEntry.setTorrent_url("www.tolleURL.com");
		csiEntry.setDownload_progress(100.00F);
		csiEntry.setIp("127.0.0.1");
		csiEntry.setFile_size(200000.00);
		csiEntry.setIop_flag(false);
		csiEntry.setPort(2002);
		csiEntry.setTimestamp(timestamp);
		csiEntry.setTorrent_id("Torrent_ID");
		csidao.persist(csiEntry);

		assertTrue(csidao.getLocalLeechers("Torrent_ID").size() == 0);
		assertTrue(csidao.getLocalSeeders("Torrent_ID").size() == 1);

		csiEntry = new ClientSwarmInfoEntry();
		csiEntry.setTorrent_url("www.tolleURL.com");
		csiEntry.setIp("127.0.0.1");
		csiEntry.setDownload_progress(100.00F);
		csiEntry.setFile_size(200000.00);
		csiEntry.setIop_flag(false);
		csiEntry.setPort(2002);
		csiEntry.setTimestamp(timestamp);
		csiEntry.setTorrent_id("Torrent_ID");

		assertTrue(csidao.get(csiEntry).size() == 1);

		assertEquals("www.tolleURL.com", csiEntry.getTorrent_url());
		assertEquals(100.00F, csiEntry.getDownload_progress());
		assertEquals(200000.00, csiEntry.getFile_size());
		assertFalse(csiEntry.isIop_flag());
		assertEquals((int) 2002, (int) csiEntry.getPort());
		assertEquals(100.00F, csiEntry.getDownload_progress());
		assertEquals("127.0.0.1", csiEntry.getIp());

		assertEquals(200000.00, csidao.getFileSizeForTorrentID("Torrent_ID"));
		assertEquals(0, csidao.getNumberOfLocalLeechers("Torrent_ID"));
		assertEquals(1, csidao.getNumberOfLocalSeeders("Torrent_ID"));
		assertTrue(csidao.countAll() == 1);
		assertTrue(csidao.getSwarmsYoungerThen(timestamp - 10000, false).size() == 1);
		assertTrue(csidao.getSwarmsYoungerThen(timestamp + 1000, false).size() == 0);
		csidao.setIoPParticipation("Torrent_ID", true);
		assertTrue(csidao.getAll().get(0).isIop_flag() == true);
		csidao.removeAll();
		assertTrue(csidao.countAll() == 0);

		// add again new entry
		csiEntry = new ClientSwarmInfoEntry();
		csiEntry.setTorrent_url("www.tolleURL.com");
		csiEntry.setDownload_progress(100.00F);
		csiEntry.setIp("127.0.0.1");
		csiEntry.setFile_size(200000.00);
		csiEntry.setIop_flag(false);
		csiEntry.setPort(2002);
		csiEntry.setTimestamp(timestamp);
		csiEntry.setTorrent_id("Torrent_ID");
		csidao.persist(csiEntry);
		assertTrue(csidao.countAll() == 1);
		csidao.deleteSwarmsOlderThen(timestamp + 1000);
		assertTrue(csidao.countAll() == 0);

	}
}
