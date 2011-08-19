package eu.smoothit.sis.controller.peerrating.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.controller.peerrating.PeerRating;
import eu.smoothit.sis.controller.peerrating.RequestEntry;
import eu.smoothit.sis.controller.peerrating.ResponseEntry;
import eu.smoothit.sis.controller.peerrating.impl.PeerRatingImpl;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;
import eu.smoothit.sis.metering.Metering;

/**
 * Tests the BGP locality ETM mechanism.
 * 
 * @author Michael Makidis, Sergios Soursos, Intracom Telecom 
 */
public class RatingTestSuite {
	
	private static Double maxP2 ;
	private static Double maxP3 ;
	private static Double maxP4 ;
	private static PeerRating rater;
	
	@BeforeClass
	public static void init()
	{
		rater = new PeerRatingImpl();
		
		IComponentConfigDAO dao = SisDAOFactory.getFactory().createComponentConfigDAO();
    	dao.removeAll();
    	
    	ComponentConfigEntry config = new ComponentConfigEntry();
    	config.setPropName(Metering.COMPONENT_NAME);
    	config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
    	config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
    	dao.persist(config);
    	
    	IPRangeDAO localIPdao = SisDAOFactory.getFactory().createIPRangeDAO();
    	localIPdao.removeAll();
    	IPRangeConfigEntry iprange = new IPRangeConfigEntry();
    	
    	// Local IP ranges
    	iprange.setLocal(true);
    	iprange.setPrefix("192.10.0.0");
    	iprange.setPrefix_len(16);
    	iprange.setLocalPreference(1);
    	iprange.setMED(1);
    	localIPdao.persist(iprange);
    	
    	iprange = new IPRangeConfigEntry();
    	iprange.setLocal(true);
    	iprange.setPrefix("192.20.0.0");
    	iprange.setPrefix_len(16);
    	iprange.setLocalPreference(1);
    	iprange.setMED(1);
    	localIPdao.persist(iprange);
    	
    	iprange = new IPRangeConfigEntry();
    	iprange.setLocal(true);
    	iprange.setPrefix("192.30.0.0");
    	iprange.setPrefix_len(16);
    	iprange.setLocalPreference(1);
    	iprange.setMED(1);
    	localIPdao.persist(iprange);
    	
    	// Remote IP ranges
    	iprange = new IPRangeConfigEntry();
    	iprange.setLocal(false);
    	iprange.setPrefix("200.10.0.0");
    	iprange.setPrefix_len(16);
    	iprange.setASPathLength(2);
    	iprange.setLocalPreference(200);
    	iprange.setMED(100);
    	localIPdao.persist(iprange);
    	
    	iprange = new IPRangeConfigEntry();
    	iprange.setLocal(false);
    	iprange.setPrefix("200.20.0.0");
    	iprange.setPrefix_len(16);
    	iprange.setASPathLength(3);
    	iprange.setLocalPreference(100);
    	iprange.setMED(150);
    	localIPdao.persist(iprange);
    	
    	iprange = new IPRangeConfigEntry();
    	iprange.setLocal(false);
    	iprange.setPrefix("200.30.0.0");
    	iprange.setPrefix_len(16);
    	iprange.setASPathLength(4);
    	iprange.setLocalPreference(50);
    	iprange.setMED(125);
    	localIPdao.persist(iprange);
    	
    	Metering.getInstance().reloadConfig();
    	Metering.getInstance().refresh();
    	Map<String, Double> maxParams = Metering.getInstance().getMaxParams();
		maxP2 = maxParams.get(Metering.PARAM_ROUTE_PREFERENCE);
		maxP3 = maxParams.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		maxP4 = maxParams.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
    	
	}
	
	@AfterClass
	public static void deleteTestData()
	{
		IComponentConfigDAO dao = SisDAOFactory.getFactory().createComponentConfigDAO();
		dao.removeAll();
    	
    	IPRangeDAO localIPdao = SisDAOFactory.getFactory().createIPRangeDAO();
    	localIPdao.removeAll();
    	
    	IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
    	iHapDao.removeAll();
    	
	}
	
	/**
     * Tests preference for one local and one remote IP (with MED value set)
     */
	@Test
	public void simplelocalRemoteInfoMEDTestCase() {
		
		List<RequestEntry> addressList = new ArrayList<RequestEntry>();
		
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("200.10.0.15");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.1.15");
		addressList.add(entry);
		
		List<ResponseEntry> responseList = rater.rankAddresses(addressList);
		
		assertNotNull(responseList);
		assertEquals(2, responseList.size());
		assertEquals("192.10.1.15", responseList.get(0).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(0).getPreference(), 0.0001);

		assertEquals("200.10.0.15", responseList.get(1).getIpAddress());
		Map<String, Double> params = Metering.getInstance().getAddressParams("200.10.0.15");
		Double p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		Double p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		Double p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		
		Double wp2 = maxP2 * (maxP3+1) * (maxP4+1);
		Double wp3 = maxP3 * (maxP4+1);
		Double wp4 = maxP4;
		
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(1).getPreference(), 0.0001);

	}
	
	/**
     * Tests preference for one local and one remote IP (with MED value set).
     * For the remote IP, no BGP information exists.
     */
	@Test
	public void simplelocalRemoteInfoMEDNotExistingTestCase() {
		
		List<RequestEntry> addressList = new ArrayList<RequestEntry>();
		
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("200.100.0.15");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.1.15");
		addressList.add(entry);
		
		List<ResponseEntry> responseList = rater.rankAddresses(addressList);
		
		assertNotNull(responseList);
		assertEquals(2, responseList.size());
		assertEquals("192.10.1.15", responseList.get(0).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(0).getPreference(), 0.0001);

		assertEquals("200.100.0.15", responseList.get(1).getIpAddress());
		// the rating received for the unknown IP should be equal to zero
		assertEquals(0, responseList.get(1).getPreference(), 0.0001);

	}

	
	/**
     * Tests preference for two locals and three remote IPs (with MED value set)
     */
	@Test
	public void localsRemotesInfoMEDTestCase() {
		
		List<RequestEntry> addressList = new ArrayList<RequestEntry>();
		
		// Local and Remote IPs (shuffled)
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("200.30.2.23");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.1.15");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("200.20.12.4");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.20.3.25");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("200.10.1.15");
		addressList.add(entry);
		
		List<ResponseEntry> responseList = rater.rankAddresses(addressList);
		
		assertNotNull(responseList);
		assertEquals(5, responseList.size());

		assertEquals("192.10.1.15", responseList.get(0).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(0).getPreference(), 0.0001);
		
		assertEquals("192.20.3.25", responseList.get(1).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(1).getPreference(), 0.0001);
		
		Double wp2 = maxP2 * (maxP3+1) * (maxP4+1);
		Double wp3 = maxP3 * (maxP4+1);
		Double wp4 = maxP4;
		
		assertEquals("200.10.1.15", responseList.get(2).getIpAddress());
		Map<String, Double> params = Metering.getInstance().getAddressParams("200.10.1.15");
		Double p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		Double p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		Double p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(2).getPreference(), 0.0001);
		
		assertEquals("200.20.12.4", responseList.get(3).getIpAddress());
		params = Metering.getInstance().getAddressParams("200.20.12.4");
		p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(3).getPreference(), 0.0001);
		
		assertEquals("200.30.2.23", responseList.get(4).getIpAddress());
		params = Metering.getInstance().getAddressParams("200.30.2.23");
		p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(4).getPreference(), 0.0001);
	}
	
	/**
     * Tests preference for two locals and three remote IPs (without MED value set)
     */
	@Test
	public void localsRemotesInfoNoMEDTestCase() {
		
		// Set weight of MED to zero in DB for calculations in the rater
		IComponentConfigDAO propsDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry config = new ComponentConfigEntry();
		config.setComponent(PeerRatingImpl.COMPONENT_NAME);
		config.setPropName(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE + "_weight");
		config.setValue("0");
		propsDao.persist(config);
		
		List<RequestEntry> addressList = new ArrayList<RequestEntry>();
		
		// Local and Remote IPs (shuffled)
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("200.30.10.23");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.10.15");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("200.20.10.4");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.20.10.25");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("200.10.10.15");
		addressList.add(entry);
		
		List<ResponseEntry> responseList = rater.rankAddresses(addressList);
		
		assertNotNull(responseList);
		assertEquals(5, responseList.size());
		
		assertEquals("192.10.10.15", responseList.get(0).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(0).getPreference(), 0.0001);
		
		assertEquals("192.20.10.25", responseList.get(1).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(1).getPreference(), 0.0001);
		
		Double wp2 = maxP2 * (maxP3+1) * (maxP4+1);
		Double wp3 = maxP3 * (maxP4+1);
		// Set weight of MED to zero for local calculations
		Double wp4 = (Double) 0.0d;
		
		assertEquals("200.10.10.15", responseList.get(2).getIpAddress());
		Map<String, Double> params = Metering.getInstance().getAddressParams("200.10.10.15");
		Double p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		Double p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		Double p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(2).getPreference(), 0.0001);
		
		assertEquals("200.20.10.4", responseList.get(3).getIpAddress());
		params = Metering.getInstance().getAddressParams("200.20.10.4");
		p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(3).getPreference(), 0.0001);

		assertEquals("200.30.10.23", responseList.get(4).getIpAddress());
		params = Metering.getInstance().getAddressParams("200.30.10.23");
		p2 = params.get(Metering.PARAM_ROUTE_PREFERENCE);
		p3 = params.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY);
		p4 = params.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE);
		assertEquals(wp2*p2 + wp3*p3 + wp4*p4, responseList.get(4).getPreference(), 0.0001);
		
	}
	
	/**
     * Tests preference for two locals, one promoted to HAP and one not.
     */
	@Test
	public void localsInfoHAPTestCase() {
		
		// Enable the HAP mechanism
		IComponentConfigDAO propsDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry config = new ComponentConfigEntry();
		config.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		config.setPropName(SisWebInitializer.PARAM_HAP_CONTROLLER_ON_OFF);
		config.setValue("true");
		propsDao.persist(config);
		
		// insert in the DB an entry with a promoted HAP
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		HAPEntry he = new HAPEntry();
		he.setIp_address("192.10.10.15");
		he.setListenport(10);
		he.setHap_flag(false); // false = already promoted
		iHapDao.persist(he);
		
		List<RequestEntry> addressList = new ArrayList<RequestEntry>();
		
		// Local IPs
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("192.10.10.14");
		addressList.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.10.15");
		addressList.add(entry);
		
		List<ResponseEntry> responseList = rater.rankAddresses(addressList);
		
		assertNotNull(responseList);
		assertEquals(2, responseList.size());
		
		assertEquals("192.10.10.15", responseList.get(0).getIpAddress());
		assertEquals(((maxP2+1) * (maxP3+1) * (maxP4+1)) + 1, responseList.get(0).getPreference(), 0.0001);
		
		assertEquals("192.10.10.14", responseList.get(1).getIpAddress());
		assertEquals((maxP2+1) * (maxP3+1) * (maxP4+1), responseList.get(1).getPreference(), 0.0001);		
	}

}
