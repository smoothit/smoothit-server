package eu.smoothit.sis.controller.hap.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.controller.hap.HAPEndpoint;
import eu.smoothit.sis.controller.hap.HAPRating;
import eu.smoothit.sis.controller.hap.NeighborStats;
import eu.smoothit.sis.controller.hap.PeerStats;
import eu.smoothit.sis.controller.hap.impl.HAPEndpointImpl;
import eu.smoothit.sis.controller.hap.impl.HAPRatingImpl;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatisticsEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;
import eu.smoothit.sis.metering.Metering;
import eu.smoothit.sis.qos.BillingAdaptor;
import eu.smoothit.sis.qos.impl.BillingAdaptorImpl;

/**
 * Tests the HAP rating bean
 * 
 * @author Sergios Soursos, Marios Charalambides, Intracom Telecom
 */
public class HARRatingTestSuite {
	
	private static HAPEndpoint endpoint;
	private static HAPRating rater;
	private static BillingAdaptor billing;
	
	@BeforeClass
	public static void init()
	{
		billing = new BillingAdaptorImpl();
		rater = new HAPRatingImpl(billing);
		endpoint = new HAPEndpointImpl(rater);
		
		IPRangeDAO ipDao = SisDAOFactory.getFactory().createIPRangeDAO();
		IPRangeConfigEntry e = new IPRangeConfigEntry();
		e.setPrefix("10.0.0.0");
		e.setPrefix_len(24);
		e.setLocal(true);
		e.setASPathLength(0);
		e.setLocalPreference(100);
		e.setMED(75);
		ipDao.persist(e);
		
		e = new IPRangeConfigEntry();
		e.setPrefix("10.0.1.0");
		e.setPrefix_len(24);
		e.setLocal(true);
		e.setASPathLength(1);
		e.setLocalPreference(75);
		e.setMED(50);
		ipDao.persist(e);
		
		e = new IPRangeConfigEntry();
		e.setPrefix("10.0.2.0");
		e.setPrefix_len(24);
		e.setLocal(false);
		e.setASPathLength(2);
		e.setLocalPreference(50);
		e.setMED(50);
		ipDao.persist(e);
		
		Metering.getInstance().reloadConfig();
		Metering.getInstance().refresh();
		
		IComponentConfigDAO compDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_P1);
		cce.setValue("1");
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_P2);
		cce.setValue("1");
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_P3);
		cce.setValue("1");
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_AD);
		cce.setValue("16384");  // 2 * 1024 * 8 (Kbps)
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_AU);
		cce.setValue("16384");  // 2 * 1024 * 8 (Kbps)
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_DI);
		cce.setValue("8192");  // 1 * 1024 * 8 (Kbps)
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_UI);
		cce.setValue("8192");  // 1 * 1024 * 8(Kbps)
		compDao.persist(cce);
		
		// Hence N = 2!
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_RT);
		cce.setValue("0");
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_T_UPDATE);
		cce.setValue("60");
		compDao.persist(cce);
		
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_N);
		cce.setValue("4");
		compDao.persist(cce);
		
	}
	
	@AfterClass
	public static void deleteTestData()
	{
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		iStatsDao.removeAll();
		
		IPRangeDAO ipDao = SisDAOFactory.getFactory().createIPRangeDAO();
		ipDao.removeAll();
		
		IComponentConfigDAO compDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		compDao.removeAll();
		
		IHAPEntryDAO hapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		hapDao.removeAll();
	}
	
	/**
     * Tests whether the stored activity reports are stored as they should be.
     */
	@Test
	public void reportActivityTestCase1() {
		
		PeerStats rep = new PeerStats();
		rep.setIpAddress("10.0.0.1");
		rep.setPort(10);
		
		List<NeighborStats> entries = new ArrayList<NeighborStats>();
		
		NeighborStats n = new NeighborStats();
		n.setIpAddress("10.0.1.1");
		n.setDownVolume(10);
		n.setUpVolume(5);
		entries.add(n);
		
		n = new NeighborStats();
		n.setIpAddress("10.0.2.1");
		n.setDownVolume(20);
		n.setUpVolume(16);
		entries.add(n);
		
		rep.setNeighbors(entries);
		
		endpoint.reportActivity(rep);
		
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		List<PeerStatisticsEntry> res = iStatsDao.getAll();
		
		assertNotNull(res);
		assertEquals(1, res.size(),0.0001);
		assertEquals("10.0.0.1", res.get(0).getIp_address());
		assertEquals(10, res.get(0).getPort(), 0.0001);
		assertEquals(30, res.get(0).getTotal_download_volume(), 0.0001);
		assertEquals(21, res.get(0).getTotal_upload_volume(), 0.0001);
		assertEquals(5, res.get(0).getLocal_upload_volume(), 0.0001);
				
	}
	
	/**
     * Tests whether the stored activity reports are stored as they should be.
     */
	@Test
	public void reportActivityTestCase2() {
		
		PeerStats rep = new PeerStats();
		rep.setIpAddress("10.0.0.1");
		rep.setPort(10);
		
		List<NeighborStats> entries = new ArrayList<NeighborStats>();
		
		NeighborStats n = new NeighborStats();
		n.setIpAddress("10.0.1.1");
		n.setDownVolume(20);
		n.setUpVolume(6);
		entries.add(n);
		
		n = new NeighborStats();
		n.setIpAddress("10.0.2.1");
		n.setDownVolume(10);
		n.setUpVolume(1);
		entries.add(n);
		
		rep.setNeighbors(entries);
		
		endpoint.reportActivity(rep);
		
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		List<PeerStatisticsEntry> res = iStatsDao.getAll();
		
		assertNotNull(res);
		assertEquals(1, res.size(),0.0001);
		assertEquals("10.0.0.1", res.get(0).getIp_address());
		assertEquals(10, res.get(0).getPort(), 0.0001);
		assertEquals(60, res.get(0).getTotal_download_volume(), 0.0001);
		assertEquals(28, res.get(0).getTotal_upload_volume(), 0.0001);
		assertEquals(11, res.get(0).getLocal_upload_volume(), 0.0001);
				
	}
	
	/**
     * Tests the HAP rating algorithm, where N=4 and RT =0 (with only two peers active).
     */
	@Test
	public void calcHAPRatingsTestCase1() {
		
		PeerStats rep = new PeerStats();
		rep.setIpAddress("10.0.0.2");
		rep.setPort(10);
		
		List<NeighborStats> entries = new ArrayList<NeighborStats>();
		
		NeighborStats n = new NeighborStats();
		n.setIpAddress("10.0.0.1");
		n.setDownVolume(30);
		n.setUpVolume(10);
		entries.add(n);
		
		n = new NeighborStats();
		n.setIpAddress("10.0.1.1");
		n.setDownVolume(10);
		n.setUpVolume(3);
		entries.add(n);
		
		rep.setNeighbors(entries);
		
		endpoint.reportActivity(rep);
		
		rater.calcHAPRatings();
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		List<List<String>> proms = iHapDao.getPromotedHAPs();
		assertEquals(0, proms.size(),0.0001);
		List<List<String>> news = iHapDao.getNewHAPs();
		assertEquals(2, news.size(),0.0001);
		
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		List<PeerStatisticsEntry> res = iStatsDao.getAll();
		assertNotNull(res);
		assertEquals(2, res.size(),0.0001);
		for (int i=0; i<res.size();i++){
			if (res.get(i).getIp_address().equals("10.0.0.1")){
				double rt =  ((1024d * 8d * 11d) / (60d * 512d)) + /*(28d / 60d) +*/  ((1024d * 8d * 28d)/(60d * 512d));
				assertEquals(rt, res.get(i).getHap_rating(), 0.001);
			} else if (res.get(i).getIp_address().equals("10.0.0.2")){
				double rt =  ((1024d * 8d * 13d) / (60d * 512d)) + /*(13d / 40d) +*/  ((1024d * 8d * 13d)/(60d * 512d));
				assertEquals(rt, res.get(i).getHap_rating(), 0.001);
			}
		}
		
		List<HAPEntry> haps = iHapDao.getAll();
		assertEquals(2, haps.size(),0.0001);
		assertEquals("10.0.0.1", haps.get(0).getIp_address());
		assertEquals("10.0.0.2", haps.get(1).getIp_address());
				
	}
	
	/**
     * Tests the HAP rating algorithm, where N=1 and RT =0 (with only two peers active).
     * Also, some already promoted and new peers exist in the DB so as to test the respective
     * part of the code.
     */
	@Test
	public void calcHAPRatingsTestCase2() {
		
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		iStatsDao.removeAll();
		
		PeerStatisticsEntry p = new PeerStatisticsEntry();
		p.setIp_address("10.0.0.1");
		p.setPort(10);
		p.setTotal_download_volume(60D);
		p.setTotal_upload_volume(28D);
		p.setLocal_upload_volume(11D);
		iStatsDao.persist(p);
		
		p = new PeerStatisticsEntry();
		p.setIp_address("10.0.0.2");
		p.setPort(10);
		p.setTotal_download_volume(40D);
		p.setTotal_upload_volume(13D);
		p.setLocal_upload_volume(13D);
		iStatsDao.persist(p);
		
		p = new PeerStatisticsEntry();
		p.setIp_address("10.0.0.10");
		p.setPort(10);
		p.setTotal_download_volume(1D);
		p.setTotal_upload_volume(0.2D);
		p.setLocal_upload_volume(0.1D);
		p.setHap_rating(0.1);
		iStatsDao.persist(p);
		
		IHAPEntryDAO hapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		hapDao.removeAll();
				
		HAPEntry h = new HAPEntry();
		h.setIp_address("10.0.0.10");
		h.setListenport(10);
		h.setHap_flag(false);
		hapDao.persist(h);
		
		h = new HAPEntry();
		h.setIp_address("10.0.0.20");
		h.setListenport(10);
		h.setHap_flag(true);
		hapDao.persist(h);
		
		IComponentConfigDAO compDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_N);
		cce.setValue("1");
		compDao.update(cce);
		
		rater.calcHAPRatings();
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		List<List<String>> proms = iHapDao.getPromotedHAPs();
		assertEquals(1, proms.size(),0.0001);
		List<List<String>> news = iHapDao.getNewHAPs();
		assertEquals(1, news.size(),0.0001);
		
		List<PeerStatisticsEntry> res = iStatsDao.getAll();
		assertNotNull(res);
		assertEquals(3, res.size(),0.0001);
		for (int i=0; i<res.size();i++){
			if (res.get(i).getIp_address().equals("10.0.0.1")){
				double rt =  ((1024d * 8d * 11d) / (60d * 512d)) + /*(28d / 60d) +*/  ((1024d * 8d * 28d)/(60d * 512d));
				assertEquals(rt, res.get(i).getHap_rating(), 0.001);
			} else if (res.get(i).getIp_address().equals("10.0.0.2")) {
				double rt =  ((1024d * 8d * 13d) / (60d * 512d)) + /*(13d / 40d) +*/  ((1024d * 8d * 13d)/(60d * 512d));
				assertEquals(rt, res.get(i).getHap_rating(), 0.001);
			} else if (res.get(i).getIp_address().equals("10.0.0.10")) {
				double rt =  ((1024d * 8d * 0.1d) / (60d * (512d + 1024d * 8d))) + /*(0.2d / 1d) +*/  ((1024d * 8d * 0.2d)/(60d * (512d + 1024d * 8d))) + 0.1d * java.lang.Math.exp(-1);
				assertEquals(rt, res.get(i).getHap_rating(), 0.001);
			}
		}
		
		List<HAPEntry> haps = iHapDao.getAll();
		assertEquals(2, haps.size(),0.0001);
		assertEquals("10.0.0.1", haps.get(0).getIp_address());
		assertEquals("10.0.0.10", haps.get(1).getIp_address());
				
	}
	
	/**
     * Tests the HAP rating algorithm, where N is not set and RT = 8 
     * Only two peers are active, one with rt = 7.2584 and the other with rt = 10.8668
     */
	@Test
	public void calcHAPRatingsTestCase3() {
		
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		iStatsDao.removeAll();
		
		PeerStatisticsEntry p = new PeerStatisticsEntry();
		p.setIp_address("10.0.0.1");
		p.setPort(10);
		p.setTotal_download_volume(60D);
		p.setTotal_upload_volume(28D);
		p.setLocal_upload_volume(11D);
		iStatsDao.persist(p);
		
		p = new PeerStatisticsEntry();
		p.setIp_address("10.0.0.2");
		p.setPort(10);
		p.setTotal_download_volume(40D);
		p.setTotal_upload_volume(13D);
		p.setLocal_upload_volume(13D);
		iStatsDao.persist(p);
		
		IHAPEntryDAO hapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		hapDao.removeAll();
				
		IComponentConfigDAO compDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry cce = compDao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_HAP, SisWebInitializer.PARAM_HAP_CONTROLLER_N);
		compDao.remove(cce);
				
		cce = new ComponentConfigEntry();
		cce.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		cce.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_RT);
		cce.setValue("8");
		compDao.update(cce);
		
		rater.calcHAPRatings();
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		List<List<String>> proms = iHapDao.getPromotedHAPs();
		assertEquals(0, proms.size(),0.0001);
		List<List<String>> news = iHapDao.getNewHAPs();
		assertEquals(1, news.size(),0.0001);
		
		List<PeerStatisticsEntry> res = iStatsDao.getAll();
		assertNotNull(res);
		assertEquals(1, res.size(),0.0001);
		for (int i=0; i<res.size();i++){
			if (res.get(i).getIp_address().equals("10.0.0.1")){
				double rt =  ((1024d * 8d * 11d) / (60d * 512d)) + /*(28d / 60d) +*/  ((1024d * 8d * 28d)/(60d * 512d));
				assertEquals(rt, res.get(i).getHap_rating(), 0.001);
			}
		}
		
		List<HAPEntry> haps = iHapDao.getAll();
		assertEquals(1, haps.size(),0.0001);
		assertEquals("10.0.0.1", haps.get(0).getIp_address());
	}
	

	/**
     * Tests the final lists of peers to be promoted and demoted. These lists are returned to the NMS.
     * delta = true so that only the newly promoted peers are included in the reply.
     *  A, B, C, D: promoted
     *  B, C, E: new
     */
	@Test
	public void fetchHAPsTestCase1() {
		
		// 0. Empty the DB
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
		
		// 1. Fill in the DB
		//---------- promoted yesterday peers -------
		HAPEntry entry = new HAPEntry();
		entry.setIp_address("A");
		entry.setListenport(10);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("D");
		entry.setListenport(13);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		//--------- new peers to be promoted ----------
		entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
						
		// 2. Call the fetchHAPs
		List<List<String>> result = rater.fetchHAPs(true);
				
		// 3. Check what fetchHAPs returns (result)
		assertNotNull(result);
		
		List<String> promoted,demoted; 	
		promoted  = result.get(0); 
		demoted   = result.get(1);
		
		//promoted list must have one peer the E
		assertEquals(1, promoted.size());
		assertEquals("E", promoted.get(0));
		
		//demoted list must have two peers the A and D
		assertEquals(2, demoted.size());
		
		boolean exists = false;
				
		if(demoted.contains("A"))
		 exists = true;
		assertEquals(exists, true);
		
		exists = false;
		if(demoted.contains("D"))
		 exists = true;
		assertEquals(exists, true);

		
		// 4. Check what is stored in the DB
		
		// 4.1 check what exists
		// In DB must exists B,C,E
	    entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		List<HAPEntry> list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		// 4.1 check what does not exist
		// In DB must not exist A,D(flag = false),B,C,E (flag = true)
		entry = new HAPEntry();
		entry.setIp_address("A");
		entry.setListenport(10);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
		  
		entry = new HAPEntry();
		entry.setIp_address("D");
		entry.setListenport(13);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
		 
		entry = new HAPEntry();
	    entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(true);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
		 
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(true);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
			  
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(true);
		list = iHapDao.get(entry);
	    assertEquals(0, list.size());
				
	}
	
	/**
     * Tests the final lists of peers to be promoted and demoted. These lists are returned to the NMS.
     * delta = true so that only the newly promoted peers are included in the reply.
     *  No peer exist in DB.
     *  B, C, E: new peer to promoted.
     */
	
	@Test
	public void fetchHAPsTestCase2() {
	    // 0. Empty the DB
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
			
		HAPEntry entry = new HAPEntry();
			
		//--------- new peers to be promoted ----------
		entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
						
		// 2. Call the fetchHAPs
		List<List<String>> result = rater.fetchHAPs(true);
				
		// 3. Check what fetchHAPs returns (result)
		assertNotNull(result);
		
		List<String> promoted,demoted; 	
		promoted  = result.get(0); 
		demoted   = result.get(1);
		
		//demoted list must be null 
		assertNull(demoted);
		
		//promoted list must have size 3
		assertEquals(3, promoted.size());
						
		//check if B exists in the promoted list
		boolean exists = false;
		if(promoted.contains("B"))
		  exists = true;
		assertEquals(exists, true);
		
		//check if C exists in the promoted list
		exists = false;
		if(promoted.contains("C"))
		 exists = true;
		assertEquals(exists, true);
		
		//check if E exists in the promoted list
        exists = false;
		if(promoted.contains("E"))
		 exists = true;
		assertEquals(exists, true);
		
		// 4. Check what is stored in the DB
		
		// 4.1 check what exists
		// In DB must exists B,C,E
	    entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		List<HAPEntry> list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
	}

	/**
     * Tests the final lists of peers to be promoted and demoted. These lists are returned to the NMS.
     * delta = true so that only the newly promoted peers are included in the reply.
     *  A, B, C, D: promoted
     *  no new peer to be promoted.
     */
	@Test
	public void fetchHAPsTestCase3() {
		
		// 0. Empty the DB
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
		
		// 1. Fill in the DB
		//---------- promoted yesterday peers -------
		HAPEntry entry = new HAPEntry();
		entry.setIp_address("A");
		entry.setListenport(10);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("D");
		entry.setListenport(13);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		//--------- new peers to be promoted ----------
		/*entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
			*/			
		// 2. Call the fetchHAPs
		List<List<String>> result = rater.fetchHAPs(true);
				
		// 3. Check what fetchHAPs returns (result)
		assertNotNull(result);
		
		List<String> promoted,demoted; 	
		promoted  = result.get(0); 
		demoted   = result.get(1);
		
		//promoted list must be empty
		assertNull(promoted);
				
		//demoted list must have 4 peers the A ,B,C and D.
		assertEquals(4, demoted.size());
		
		boolean exists = false;
				
		if(demoted.contains("A"))
		 exists = true;
		assertEquals(exists, true);
		
		exists = false;
		if(demoted.contains("B"))
		 exists = true;
		assertEquals(exists, true);
		
		exists = false;
		if(demoted.contains("C"))
		 exists = true;
		assertEquals(exists, true);
				
		exists = false;
		if(demoted.contains("D"))
		 exists = true;
		assertEquals(exists, true);
			
		// 4. Check what is stored in the DB
		
		// 4.1 check what exists
		// In DB must not exists A,B,C,D
		entry = new HAPEntry();
		entry.setIp_address("A");
		entry.setListenport(10);
		entry.setHap_flag(false);
		List<HAPEntry> list = iHapDao.get(entry);
		assertEquals(0,list.size());
		
	    entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0,list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0,list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("D");
		entry.setListenport(13);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0,list.size());
		
	}
	
	/**
     * Tests the final lists of peers to be promoted and demoted. These lists are returned to the NMS.
     * delta = false so that all promoted peers (old and new ones) are included in the reply.
     *  A, B, C, D: promoted
     *  B, C, E: new
     */
	@Test
	public void fetchHAPsTestCase4() {
		
		// 0. Empty the DB
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
		
		// 1. Fill in the DB
		//---------- promoted yesterday peers -------
		HAPEntry entry = new HAPEntry();
		entry.setIp_address("A");
		entry.setListenport(10);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("D");
		entry.setListenport(13);
		entry.setHap_flag(false);
		iHapDao.persist(entry);
		
		//--------- new peers to be promoted ----------
		entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
						
		// 2. Call the fetchHAPs
		List<List<String>> result = rater.fetchHAPs(false);
				
		// 3. Check what fetchHAPs returns (result)
		assertNotNull(result);
		
		List<String> promoted,demoted; 	
		promoted  = result.get(0); 
		demoted   = result.get(1);
		
		//promoted list must have three peers: B, C, E
		assertEquals(3, promoted.size());
		
		assertTrue(promoted.contains("B"));
		assertTrue(promoted.contains("C"));
		assertTrue(promoted.contains("E"));		
		
		//demoted list must have two peers: A and D
		assertEquals(2, demoted.size());
		
		assertTrue(demoted.contains("A"));
		assertTrue(demoted.contains("D"));		
		
		// 4. Check what is stored in the DB
		
		// 4.1 check what exists
		// In DB must exists B,C,E
	    entry = new HAPEntry();
		entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(false);
		List<HAPEntry> list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(1, list.size());
		
		// 4.1 check what does not exist
		// In DB must not exist A,D(flag = false),B,C,E (flag = true)
		entry = new HAPEntry();
		entry.setIp_address("A");
		entry.setListenport(10);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
		  
		entry = new HAPEntry();
		entry.setIp_address("D");
		entry.setListenport(13);
		entry.setHap_flag(false);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
		 
		entry = new HAPEntry();
	    entry.setIp_address("B");
		entry.setListenport(11);
		entry.setHap_flag(true);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
		 
		entry = new HAPEntry();
		entry.setIp_address("C");
		entry.setListenport(12);
		entry.setHap_flag(true);
		list = iHapDao.get(entry);
		assertEquals(0, list.size());
			  
		entry = new HAPEntry();
		entry.setIp_address("E");
		entry.setListenport(14);
		entry.setHap_flag(true);
		list = iHapDao.get(entry);
	    assertEquals(0, list.size());
				
	}

}
