package eu.smoothit.sis.controller.iop.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.controller.iop.ActiveTorrent;
import eu.smoothit.sis.controller.iop.ActivityReport;
import eu.smoothit.sis.controller.iop.ActivityReportEntry;
import eu.smoothit.sis.controller.iop.IoPEndpoint;
import eu.smoothit.sis.controller.iop.impl.IoPConfigImpl;
import eu.smoothit.sis.controller.iop.impl.IoPEndpointImpl;
import eu.smoothit.sis.controller.iop.impl.TorrentRatingImpl;

/**
 * Tests the IoP Web Service methods
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class IoPEndpointTestSuite {
	
	private static IoPEndpoint endpoint;
	
	@BeforeClass
	public static void init()
	{
		endpoint = new IoPEndpointImpl(new TorrentRatingImpl(), new IoPConfigImpl());
	}
	
	/**
     * Tests whether the getTorrentStats response is null, when the DB is empty
     */
	@Test
	public void torrentStatsNullTestCase() {
		
		assertNull(endpoint.getTorrentStats(5));
	}
		
	/**
     * Tests whether the reportActivity is handled without any problems
     */
	@Test
	public void reportActivityTestCase() {
		
		ActivityReport rep = new ActivityReport();
		rep.setIpAddress("10.0.0.1");
		rep.setPort(10);
		
		List<ActivityReportEntry> entries = new ArrayList<ActivityReportEntry>();
		
		ActivityReportEntry e = new ActivityReportEntry();
		e.setTorrentID("aaa");
		e.setTorrentURL("http://somwhere.com/this.torrent");
		e.setFileSize(1024d);
		e.setProgress(90f);
		entries.add(e);
		
		e = new ActivityReportEntry();
		e.setTorrentID("bbb");
		e.setTorrentURL("http://somwhere.com/that.torrent");
		e.setFileSize(2048d);
		e.setProgress(20f);
		entries.add(e);
		
		rep.setEntries(entries);		
		
		endpoint.reportActivity(rep);
	}
	
	/**
     * Tests whether the activeInTorrent response is null, when the DB is empty
     */
	@Test
	public void activeInTorrentsNullTestCase() {
		
		List<ActiveTorrent> list = new ArrayList<ActiveTorrent>();
		
		ActiveTorrent entry = new ActiveTorrent();
		entry.setInfohash("aaa");
		entry.setMaxPeers(10);
		entry.setSeeds(false);
		list.add(entry);
		
		entry = new ActiveTorrent();
		entry.setInfohash("bbb");
		entry.setMaxPeers(30);
		entry.setSeeds(true);
		list.add(entry);
		
		assertNotNull(endpoint.activeInTorrents(list));
	}
	
	/**
     * Tests whether the activeInTorrent response is null, 
     * when the number of peers to be returned is zero.
     */
	@Test
	public void activeInTorrentsZeroNumberTestCase() {
		
		List<ActiveTorrent> list = new ArrayList<ActiveTorrent>();
		
		ActiveTorrent entry = new ActiveTorrent();
		entry.setInfohash("aaa");
		entry.setMaxPeers(0);
		entry.setSeeds(false);
		list.add(entry);
		
		assertNotNull(endpoint.activeInTorrents(list));
	}
	
	/**
     * Tests whether the getConfigParams is returns null when the DB is empty
     */
	@Test
	public void getConfigParamsTestCase() {
		
		assertNull(endpoint.getConfigParams("10.0.0.1"));
	}

}
