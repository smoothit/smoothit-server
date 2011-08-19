package eu.smoothit.sis.controller.hap.test;

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
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.metering.Metering;
import eu.smoothit.sis.qos.BillingAdaptor;
import eu.smoothit.sis.qos.impl.BillingAdaptorImpl;


/**
 * Tests the HAP Web Service methods
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class HAPEndpointTestSuite {
	
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
		
	}
	
	@AfterClass
	public static void deleteTestData()
	{
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		iStatsDao.removeAll();
		
		IPRangeDAO ipDao = SisDAOFactory.getFactory().createIPRangeDAO();
		ipDao.removeAll();
	}
	
	/**
     * Tests whether the reportActivity is handled without any problems
     */
	@Test
	public void reportActivityTestCase() {
		
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
	}
	
	/**
     * Tests whether the calcHAPRatings is handled without any problems
     */
	@Test
	public void calcHAPRatingsTestCase() {
		
		PeerStats rep = new PeerStats();
		rep.setIpAddress("10.0.2.1");
		rep.setPort(10);
		
		List<NeighborStats> entries = new ArrayList<NeighborStats>();
		
		NeighborStats n = new NeighborStats();
		n.setIpAddress("10.0.1.1");
		n.setDownVolume(10);
		n.setUpVolume(5);
		entries.add(n);
		
		n = new NeighborStats();
		n.setIpAddress("10.0.2.2");
		n.setDownVolume(20);
		n.setUpVolume(16);
		entries.add(n);
		
		rep.setNeighbors(entries);
		
		endpoint.reportActivity(rep);
		rater.calcHAPRatings();
	}

}
