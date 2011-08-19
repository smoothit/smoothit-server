package eu.smoothit.sis.controller.iop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.controller.iop.ActiveTorrent;
import eu.smoothit.sis.controller.iop.ActivityReport;
import eu.smoothit.sis.controller.iop.ActivityReportEntry;
import eu.smoothit.sis.controller.iop.PeerInfo;
import eu.smoothit.sis.controller.iop.TorrentRating;
import eu.smoothit.sis.controller.iop.TorrentStat;
import eu.smoothit.sis.controller.iop.impl.TorrentRatingImpl;
import eu.smoothit.sis.controller.iop.ResponseEntry;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IClientSwarmInfoDAO;
import eu.smoothit.sis.db.impl.entities.ClientSwarmInfoEntry;


/**
 * Tests the Swarm Rating Service methods
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class TorrentRatingTestSuite {
	
	private static TorrentRating rater;
	
	@BeforeClass
	public static void init()
	{
		rater = new TorrentRatingImpl();
	}
	
	@AfterClass
	public static void deleteTestData()
	{
		IClientSwarmInfoDAO iClientDao = SisDAOFactory.getFactory().createClientSwarmInfoDAO();
		iClientDao .removeAll();
	}

	
	/*
	 * Checks whether report info from the peers is stored correctly in the DB.
	 * 
	 */
	@Test
	public void storePeerActivityOneClientInsertTestCase() {
		
		// Create test data
		ActivityReport report = new ActivityReport();
		report.setIpAddress("10.0.0.1");
		report.setPort(80);
		List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
		for(int i=1;i<=5;i++){
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa" + i);
			e.setTorrentURL("http://server/torrents/aaa" + i);
			e.setProgress((float) (1 + 0.1 * i ) * 40);
			e.setFileSize((double) 1024 * i);
			list.add(e);
		}
		report.setEntries(list);
		
		// Dispatch data to rater
		rater.storePeerActivity(report);
		
		// Access DB and check if entries has been stored
		IClientSwarmInfoDAO iClientDao = SisDAOFactory.getFactory().createClientSwarmInfoDAO();
		
		for (ActivityReportEntry e: list){
			String torID = e.getTorrentID();
			ClientSwarmInfoEntry check = new ClientSwarmInfoEntry();
			check.setIp("10.0.0.1");
			check.setTorrent_id(torID);
			List<ClientSwarmInfoEntry> results = iClientDao.get(check);
			assertNotNull(results);
			assertEquals(1, results.size());
			assertEquals(torID, results.get(0).getTorrent_id());
			assertEquals(e.getTorrentURL(), results.get(0).getTorrent_url());
			assertEquals(e.getProgress(), results.get(0).getDownload_progress(), 0.0001);
			assertEquals(e.getFileSize(), results.get(0).getFile_size(), 0.0001);
		}
		
	}
	
	/*
	 * This test is based on previous one! Assumes that the data inserted 
	 * in the DB are still there and it tries to update them!
	 */
	@Test
	public void storePeerActivityOneClientUpdateTestCase() {

		// Create test data
		ActivityReport report = new ActivityReport();
		report.setIpAddress("10.0.0.1");
		report.setPort(80);
		List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
		for(int i=1;i<=5;i++){
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa" + i);
			e.setTorrentURL("http://server/torrents/aaa" + i);
			e.setProgress((float) (1 + 0.1 * i ) * 60);
			e.setFileSize((double) 1024 * i);
			list.add(e);
		}
		report.setEntries(list);
		
		// Dispatch data to rater
		rater.storePeerActivity(report);
		
		// Access DB and check if updates have been made
		IClientSwarmInfoDAO iClientDao = SisDAOFactory.getFactory().createClientSwarmInfoDAO();
		
		for (ActivityReportEntry e: list){
			String torID = e.getTorrentID();
			ClientSwarmInfoEntry check = new ClientSwarmInfoEntry();
			check.setIp("10.0.0.1");
			check.setTorrent_id(torID);
			List<ClientSwarmInfoEntry> results = iClientDao.get(check);
			assertNotNull(results);
			assertEquals(1, results.size());
			assertEquals(torID, results.get(0).getTorrent_id());
			assertEquals(e.getTorrentURL(), results.get(0).getTorrent_url());
			assertEquals(e.getProgress(), results.get(0).getDownload_progress(), 0.0001);
			assertEquals(e.getFileSize(), results.get(0).getFile_size(), 0.0001);
		}
		
		// Now we can remove data from DB (also from previous test).
		iClientDao .removeAll();
	}
	
	/*
	 * This test checks the correct implementation of the swarm rating function,
	 * when the number of swarms requested is zero!
	 * 
	 */
	@Test
	public void getRankedTorrentsZeroTestCase() {
		
		List<TorrentStat> result = rater.getRankedTorrents(0);
		assertNull(result);
		
	}
	
	/*
	 * This test checks the correct implementation of the swarm rating function,
	 * when 5 leechers participating in a single swarm exist.
	 * 
	 */
	@Test
	public void getRankedTorrentsSingleSwarm1TestCase() {
		
		for (int i=1;i<=5;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.0." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) (1 + 0.1 * i ) * 60);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		List<TorrentStat> result = rater.getRankedTorrents(1);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("aaa", result.get(0).getTorrentHash());
		assertEquals("http://server/torrents/aaa", result.get(0).getTorrentURL());
		double rate = (double) 1024 * 5 / (0 +1);
		assertEquals(rate, result.get(0).getRate(), 0.0001);
	} 
	
	/*
	 * This test checks the correct implementation of the swarm rating function,
	 * when 5 leechers and 2 seeds participating in a single swarm exist.
	 * This test requires the data from the previous one !
	 * 
	 */
	@Test
	public void getRankedTorrentsSingleSwarm2TestCase() {
		
		int leechers = 5;
		int seeds = 2;
		
		for (int i=1;i<=seeds;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.0." + (leechers+i));
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) 100);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		List<TorrentStat> result = rater.getRankedTorrents(1);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("aaa", result.get(0).getTorrentHash());
		assertEquals("http://server/torrents/aaa", result.get(0).getTorrentURL());
		double rate = (double) 1024 * leechers / (seeds +1);
		assertEquals(rate, result.get(0).getRate(), 0.0001);
			
	}
	
	/*
	 * This test checks the correct implementation of the swarm rating function,
	 * when 5 leechers and 2 seeds participate in one swarm (from previous tests).
	 * and 4 leechers and 3 seeds participate in another swarm.
	 * 
	 */
	@Test
	public void getRankedTorrentsTwoSwarmsTestCase() {

		int leechers1 = 5;
		int seeds1 = 2;
		
		int leechers = 4;
		int seeds = 3;
		
		for (int i=1;i<=leechers;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.1." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("bbb");
			e.setTorrentURL("http://server/torrents/bbb");
			e.setProgress((float) (1 + 0.1 * i ) * 50);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		for (int i=1;i<=seeds;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.1." + (leechers+i));
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("bbb");
			e.setTorrentURL("http://server/torrents/bbb");
			e.setProgress((float) 100);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		List<TorrentStat> result = rater.getRankedTorrents(2);
		assertNotNull(result);
		assertEquals(2, result.size());
		
		assertEquals("aaa", result.get(0).getTorrentHash());
		assertEquals("http://server/torrents/aaa", result.get(0).getTorrentURL());
		double rate = (double) 1024 * leechers1 / (seeds1 +1);
		assertEquals(rate, result.get(0).getRate(), 0.0001);
		
		assertEquals("bbb", result.get(1).getTorrentHash());
		assertEquals("http://server/torrents/bbb", result.get(1).getTorrentURL());
		rate = (double) 1024 * leechers / (seeds +1);
		assertEquals(rate, result.get(1).getRate(), 0.0001);
		
		// Now we can remove data from DB (also from previous tests).
		IClientSwarmInfoDAO iClientDao = SisDAOFactory.getFactory().createClientSwarmInfoDAO();
		iClientDao .removeAll();
	}
	
	/*
	 * This test checks the information sent back to the IoP when it queries
	 * the SIS for local leechers and seeds for a swarm.
	 * 
	 * Case 1: total number of peers = 15 (10 leechers, 5 seeds)
	 *         requested number of peers = 5
	 *         
	 * Two more requests from IoPs are included which are not supposed to
	 * return any peers since either the specific swarms (bbb and ccc) do 
	 * not exist or that the number of requested peers is zero.
	 * 
	 */
	@Test
	public void updateAndRetrieveSwarmInfo1TestCase(){
		
		int leechers = 10;
		List<String> leech_list = new ArrayList<String>(leechers);
		int seeds = 5;
		List<String> seed_list = new ArrayList<String>(seeds);
		int requested = 5;
		
		for (int i=1;i<=leechers;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.0." + i);
			leech_list.add("10.0.0." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) (1 + 0.1 * i ) * 30);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		for (int i=1;i<=seeds;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("20.0.0." + i);
			seed_list.add("20.0.0." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) 100);
			e.setFileSize((double) 1024);
			list.add(e);
			
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		List<ActiveTorrent> request = new ArrayList<ActiveTorrent>();
		ActiveTorrent entry = new ActiveTorrent();
		entry.setInfohash("aaa");
		entry.setMaxPeers(requested);
		entry.setSeeds(true);
		request.add(entry);
		
		entry = new ActiveTorrent();
		entry.setInfohash("bbb");
		entry.setMaxPeers(0);
		entry.setSeeds(false);
		request.add(entry);
		
		entry = new ActiveTorrent();
		entry.setInfohash("ccc");
		entry.setMaxPeers(10);
		entry.setSeeds(false);
		request.add(entry);
		
		List<ResponseEntry> response = rater.updateAndRetrieveSwarmInfo(request);
		
		assertNotNull(response);
		assertEquals(response.size(), 1);
		
		for (ResponseEntry r: response){
			if ((r.getTorrentID().equals("bbb")) || (r.getTorrentID().equals("ccc")))
				assertNull(r.getPeers());
			else if (r.getTorrentID().equals("aaa")){
				int count_leechers = 0;
				int count_seeds = 0;
				
				List<PeerInfo> peers = r.getPeers();
				assertEquals(requested, peers.size());
				for (PeerInfo p:peers){
					if (seed_list.contains(p.getIpAddress()))
						count_seeds++;
					else if (leech_list.contains(p.getIpAddress()))
						count_leechers++;
				}
				assertEquals(count_seeds,2);
				assertEquals(count_leechers,3);
			}		
		}
		
		// Now we can remove data from DB.
		IClientSwarmInfoDAO iClientDao = SisDAOFactory.getFactory().createClientSwarmInfoDAO();
		iClientDao .removeAll();
	}
	
	/*
	 * This test checks the information sent back to the IoP when it queries
	 * the SIS for local leechers and seeds for a swarm.
	 * 
	 * Case 2: total number of peers = 10 (7 leechers, 3 seeds)
	 *         requested number of peers = 12
	 *         
	 */
	@Test
	public void updateAndRetrieveSwarmInfo2TestCase(){
		
		int leechers = 7;
		List<String> leech_list = new ArrayList<String>(leechers);
		int seeds = 3;
		List<String> seed_list = new ArrayList<String>(seeds);
		int requested = 12;
		
		for (int i=1;i<=leechers;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.0." + i);
			leech_list.add("10.0.0." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) (1 + 0.1 * i ) * 30);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		for (int i=1;i<=seeds;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("20.0.0." + i);
			seed_list.add("20.0.0." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) 100);
			e.setFileSize((double) 1024);
			list.add(e);
			
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		List<ActiveTorrent> request = new ArrayList<ActiveTorrent>();
		ActiveTorrent entry = new ActiveTorrent();
		entry.setInfohash("aaa");
		entry.setMaxPeers(requested);
		entry.setSeeds(true);
		request.add(entry);
		
		List<ResponseEntry> response = rater.updateAndRetrieveSwarmInfo(request);
		
		assertNotNull(response);
		assertEquals(response.size(), 1);
		
		if (response.get(0).getTorrentID().equals("aaa")){
			int count_leechers = 0;
			int count_seeds = 0;
			
			List<PeerInfo> peers = response.get(0).getPeers();
			assertEquals(10, peers.size());
			for (PeerInfo p:peers){
				if (seed_list.contains(p.getIpAddress()))
					count_seeds++;
				else if (leech_list.contains(p.getIpAddress()))
					count_leechers++;
			}
			assertEquals(count_seeds,3);
			assertEquals(count_leechers,7);
		}		
	}
	
	/*
	 * This test checks the information sent back to the IoP when it queries
	 * the SIS for local leechers for a swarm.
	 * 
	 * Case 3: total number of peers = 10 (10 leechers)
	 *         requested number of peers = 7
	 *         
	 */
	@Test
	public void updateAndRetrieveSwarmInfo3TestCase(){
		
		int leechers = 10;
		List<String> leech_list = new ArrayList<String>(leechers);
		List<String> seed_list = new ArrayList<String>(leechers);
		int requested = 7;
		
		for (int i=1;i<=leechers;i++){
			ActivityReport report = new ActivityReport();
			report.setIpAddress("10.0.0." + i);
			leech_list.add("10.0.0." + i);
			report.setPort(80);
			List<ActivityReportEntry> list = new ArrayList<ActivityReportEntry>();
			ActivityReportEntry e = new ActivityReportEntry();
			e.setTorrentID("aaa");
			e.setTorrentURL("http://server/torrents/aaa");
			e.setProgress((float) (1 + 0.1 * i ) * 30);
			e.setFileSize((double) 1024);
			list.add(e);
			report.setEntries(list);
			
			rater.storePeerActivity(report);
		}
		
		List<ActiveTorrent> request = new ArrayList<ActiveTorrent>();
		ActiveTorrent entry = new ActiveTorrent();
		entry.setInfohash("aaa");
		entry.setMaxPeers(requested);
		entry.setSeeds(false);
		request.add(entry);
		
		List<ResponseEntry> response = rater.updateAndRetrieveSwarmInfo(request);
		
		assertNotNull(response);
		assertEquals(response.size(), 1);
		
		if (response.get(0).getTorrentID().equals("aaa")){
			int count_leechers = 0;
			int count_seeds = 0;
			
			List<PeerInfo> peers = response.get(0).getPeers();
			assertEquals(requested, peers.size());
			for (PeerInfo p:peers){
				if (seed_list.contains(p.getIpAddress()))
					count_seeds++;
				else if (leech_list.contains(p.getIpAddress()))
					count_leechers++;
			}
			assertEquals(count_seeds,0);
			assertEquals(count_leechers,7);
		}
		
		// Now we can remove data from DB.
		IClientSwarmInfoDAO iClientDao = SisDAOFactory.getFactory().createClientSwarmInfoDAO();
		iClientDao .removeAll();
		
	}
	
}
