package eu.smoothit.sis.metering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.metering.Metering;
import eu.smoothit.sis.metering.bgp.BGPRoutingTable;
import eu.smoothit.sis.metering.impl.BGPRoutingTableEntryImpl;
import eu.smoothit.sis.metering.impl.BGPRoutingTableReader;
import eu.smoothit.sis.metering.impl.ParamTableEntry;

public class MeteringTestSuite {
	
	@Before 
	public void init() {
		//BasicConfigurator.configure();
	}

	@Test 
	public void bgpRoutingTableEntryTestCase() {
		BGPRoutingTableEntryImpl x = new BGPRoutingTableEntryImpl();

		x.setAttrPeer("192.168.1.1");
		assertEquals("192.168.1.1", x.getAttrPeer());

		x.setAttrIpAddrPrefixLen(16);
		assertEquals(16, x.getAttrIpAddrPrefixLen());

		x.setAttrIpAddrPrefix("192.168.0.0");
		assertEquals("192.168.0.0", x.getAttrIpAddrPrefix());

		x.setAttrOrigin(1);
		assertEquals(1, x.getAttrOrigin());

		x.setAttrASPathSegment("02:02:00:cd:00:c8");
		assertEquals("02:02:00:cd:00:c8", x.getAttrASPathSegment());
		assertEquals(2, x.getASPathLength());

		x.setAttrNextHop("192.168.1.1");
		assertEquals("192.168.1.1", x.getAttrNextHop());

		x.setAttrMultiExitDisc(40);
		assertEquals(40, x.getAttrMultiExitDisc());
		
		x.setAttrLocalPref(100);
		assertEquals(100, x.getAttrLocalPref());
		
		x.setAttrAtomicAggregate(1);
		assertEquals(1, x.getAttrAtomicAggregate());
		
		x.setAttrAggregatorAS(0);
		assertEquals(0, x.getAttrAggregatorAS());
		
		x.setAttrAggregatorAddr("0.0.0.0");
		assertEquals("0.0.0.0", x.getAttrAggregatorAddr());
		
		x.setAttrCalcLocalPref(-1);
		assertEquals(-1, x.getAttrCalcLocalPref());
		
		x.setAttrBest(1);
		assertEquals(1, x.getAttrBest());
		
		x.print();
    }
	
	
	@Test 
	public void bgpRoutingTableReaderFileTestCase() {
		
		BGPRoutingTable x = new BGPRoutingTableReader();
		x.print();
		
		// Test reading from file
		x.useFile();
		x.setFileName("nonexisting.conf");
		assertFalse(x.readRoutingTable());
		assertEquals(0, x.getNumberOfEntries());
		
		x.setFileName("/eu/smoothit/sis/metering/test/empty.conf");
		assertTrue(x.readRoutingTable());
		assertEquals(0, x.getNumberOfEntries());

		x.setFileName("/eu/smoothit/sis/metering/test/error.conf");
		assertFalse(x.readRoutingTable());
		assertEquals(0, x.getNumberOfEntries());

		x.setFileName("/eu/smoothit/sis/metering/test/bgp.conf");
		assertTrue(x.readRoutingTable());
		assertNull(x.getEntryForPrefix("192.110.0.0"));
		assertEquals(3, x.getNumberOfEntries());
		assertEquals("192.168.0.0", x.getEntryForPrefix("192.168.0.0").getAttrIpAddrPrefix());
		assertEquals(16, x.getEntryForPrefix("192.168.0.0").getAttrIpAddrPrefixLen());
		assertEquals(200, x.getEntryForPrefix("192.168.0.0").getAttrLocalPref());
		assertEquals(2, x.getEntryForPrefix("192.168.0.0").getASPathLength());
		assertEquals(100, x.getEntryForPrefix("192.168.0.0").getAttrMultiExitDisc());
		assertEquals("192.100.0.0", x.getEntryForPrefix("192.100.0.0").getAttrIpAddrPrefix());
		assertNotNull(x.getAllEntries());
		assertNotNull(x.getEntryForPrefix("192.100.1.1"));
		assertEquals("192.100.0.0", x.getEntryForPrefix("192.100.1.1").getAttrIpAddrPrefix());
		assertNull(x.getEntryForPrefix("1.1.1.1"));
		x.print();
	}
	
	@Test 
	public void bgpRoutingTableReaderRouterTestCase() {
		
		BGPRoutingTable x = new BGPRoutingTableReader();
		
		// Test reading from router
		x.useRouter();
		x.setRouterAddress("x/y/2");
		assertFalse(x.readRoutingTable());
		assertEquals(0, x.getNumberOfEntries());

		x.useRouter();
		x.setRouterAddress("localhost");
		assertFalse(x.readRoutingTable());
		assertEquals(0, x.getNumberOfEntries());
		
		x.setRouterAddress("192.41.135.203");
		x.setRouterPort(161);
		x.setSNMPCommunity("public");
		assertTrue(x.readRoutingTable());
		assertEquals(18, x.getNumberOfEntries());
		
		assertNotNull(x.getEntryForPrefix("20.10.0.0"));
		assertEquals("20.10.0.0", x.getEntryForPrefix("20.10.0.0").getAttrIpAddrPrefix());
		assertEquals("20.20.0.0", x.getEntryForPrefix("20.20.0.0").getAttrIpAddrPrefix());
		assertNotNull(x.getAllEntries());
		assertNotNull(x.getEntryForPrefix("20.20.0.1"));
		assertEquals("20.20.0.0", x.getEntryForPrefix("20.20.0.1").getAttrIpAddrPrefix());
		
		assertTrue(x.readRoutingTable());
		assertEquals(18, x.getNumberOfEntries());
	}
	
	@Test 
	public void bgpRoutingTableReaderDBTestCase() {
		
		BGPRoutingTable x = new BGPRoutingTableReader();

		// Test reading from DB
    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IPRangeDAO ipRangeDao = factory.createIPRangeDAO();
    	ipRangeDao.removeAll();
    	
    	IPRangeConfigEntry remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(3);
    	remote.setLocalPreference(100);
    	remote.setMED(10);
    	assertTrue(ipRangeDao.persist(remote));

    	x.useDB();
    	assertTrue(x.readRoutingTable());
		assertEquals(1, x.getNumberOfEntries());
		assertNotNull(x.getEntryForPrefix("30.10.0.0"));
		assertEquals("30.10.0.0", x.getEntryForPrefix("30.10.0.0").getAttrIpAddrPrefix());
		assertEquals(16, x.getEntryForPrefix("30.10.0.0").getAttrIpAddrPrefixLen());
		assertEquals(3, x.getEntryForPrefix("30.10.0.0").getASPathLength());
		assertEquals(100, x.getEntryForPrefix("30.10.0.0").getAttrLocalPref());
		assertEquals(10, x.getEntryForPrefix("30.10.0.0").getAttrMultiExitDisc());
		assertNotNull(x.getAllEntries());
		assertNotNull(x.getEntryForPrefix("30.10.0.1"));
		assertEquals("30.10.0.0", x.getEntryForPrefix("30.10.0.1").getAttrIpAddrPrefix());
	}

	@Test 
	public void paramTableEntryTestCase() {
		ParamTableEntry e1 = new ParamTableEntry();
		ParamTableEntry e2 = new ParamTableEntry();
		
		e1.setIPAddress("10.1.1.1");
		e1.setPrefixLength(16);
		e2.setIPAddress("10.1.1.2");
		e2.setPrefixLength(24);
		assertEquals(1, e1.compareTo(e2));
		assertEquals(-1, e2.compareTo(e1));
		e2.setPrefixLength(16);
		assertEquals(0, e1.compareTo(e2));
		e1.setParam("Key", 1.1);
		assertEquals((Double)1.1, e1.getParam("Key"));
	}
	
	@Test 
	public void meteringInterfaceFileTestCase() {

    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IComponentConfigDAO meteringDao = factory.createComponentConfigDAO();
    	ComponentConfigEntry config;

    	// Test reading from file
		meteringDao.removeAll();
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REFRESH_RATE);
		config.setValue(50);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_BGP_FILE_NAME);
		config.setValue("/eu/smoothit/sis/metering/test/bgp.conf");
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_FILE);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_LOCAL_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_FILE);
		assertTrue(meteringDao.persist(config));
		
    	Metering m = Metering.getInstance();
    	m.reloadConfig();
    	m.refresh();
    	
		assertEquals((Double)200.0, m.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)2.0, m.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)100.0, m.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
		assertEquals((Double)1.0, m.getAddressParams("192.168.10.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m.getAddressParams("192.168.10.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getAddressParams("192.168.10.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
		assertTrue(m.refresh());
		m.print();
	}
	
	@Test 
	public void meteringInterfaceRouterTestCase() {

    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IComponentConfigDAO meteringDao = factory.createComponentConfigDAO();
    	ComponentConfigEntry config;

    	// Test reading from router		
		meteringDao.removeAll();
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REFRESH_RATE);
		config.setValue(50);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_ROUTER);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_BGP_ROUTER_ADDRESS);
		config.setValue("192.41.135.203");
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_BGP_ROUTER_PORT);
		config.setValue(161);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY);
		config.setValue("public");
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_LOCAL_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));
		
    	Metering m = Metering.getInstance();
    	m.reloadConfig();
		assertTrue(m.refresh());

		assertEquals((Double)150.0, m.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)2.0, m.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)200.0, m.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
		assertEquals((Double)1.0, m.getAddressParams("40.10.0.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.5, m.getAddressParams("40.10.0.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)1.0, m.getAddressParams("40.10.0.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
	}
	
	@Test 
	public void meteringInterfaceDBTestCase() {

    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IComponentConfigDAO meteringDao = factory.createComponentConfigDAO();
    	IPRangeDAO ipRangeDao = factory.createIPRangeDAO();
    	ComponentConfigEntry config;

    	// Test reading from DB		
		meteringDao.removeAll();
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REFRESH_RATE);
		config.setValue("50");
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));
		config.setComponent(Metering.COMPONENT_NAME);
		config = new ComponentConfigEntry();
		config.setPropName(Metering.METERING_PROP_LOCAL_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));

		ipRangeDao.removeAll();
    	IPRangeConfigEntry remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(3);
    	remote.setLocalPreference(100);
    	remote.setMED(10);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.128.0");
    	remote.setPrefix_len(17);
    	remote.setASPathLength(4);
    	remote.setLocalPreference(200);
    	remote.setMED(50);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(true);
    	remote.setPrefix("40.10.0.0");
    	remote.setPrefix_len(16);
    	assertTrue(ipRangeDao.persist(remote));

    	Metering m = Metering.getInstance();
    	m.reloadConfig();
    	assertTrue(m.refresh());

    	assertEquals((Double)200.0, m.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)4.0, m.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)50.0, m.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)1.0, m.getAddressParams("40.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)1.0, m.getAddressParams("40.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)1.0, m.getAddressParams("40.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)1.0, m.getAddressParams("40.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)0.5, m.getAddressParams("30.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.25, m.getAddressParams("30.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.8, m.getAddressParams("30.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.132.25").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)1.0, m.getAddressParams("30.10.133.25").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m.getAddressParams("30.10.134.25").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.135.25").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
	}

	@Test 
	public void meteringInterfaceZeroLocPrefAndMEDTestCase() {
    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IComponentConfigDAO meteringDao = factory.createComponentConfigDAO();
    	IPRangeDAO ipRangeDao = factory.createIPRangeDAO();
    	ComponentConfigEntry config;

		meteringDao.removeAll();
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));
		config.setComponent(Metering.COMPONENT_NAME);
		config = new ComponentConfigEntry();
		config.setPropName(Metering.METERING_PROP_LOCAL_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));

		ipRangeDao.removeAll();
    	IPRangeConfigEntry remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(3);
    	remote.setLocalPreference(0);
    	remote.setMED(0);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.128.0");
    	remote.setPrefix_len(17);
    	remote.setASPathLength(4);
    	remote.setLocalPreference(0);
    	remote.setMED(0);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.20.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(0);
    	remote.setLocalPreference(0);
    	remote.setMED(0);
    	assertTrue(ipRangeDao.persist(remote));

    	Metering m = Metering.getInstance();
    	m.reloadConfig();
    	assertTrue(m.refresh());

    	assertEquals((Double)0.0, m.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)4.0, m.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.25, m.getAddressParams("30.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.132.25").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.133.25").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m.getAddressParams("30.10.134.25").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.135.25").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		ipRangeDao.removeAll();
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(0);
    	remote.setLocalPreference(0);
    	remote.setMED(0);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.128.0");
    	remote.setPrefix_len(17);
    	remote.setASPathLength(0);
    	remote.setLocalPreference(0);
    	remote.setMED(0);
    	assertTrue(ipRangeDao.persist(remote));

    	m.reloadConfig();
    	assertTrue(m.refresh());

    	assertEquals((Double)0.0, m.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.132.25").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.133.25").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m.getAddressParams("30.10.134.25").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.135.25").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
	}
	
	@Test 
	public void meteringInterfaceDefaultValuesTestCase() {

    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IComponentConfigDAO meteringDao = factory.createComponentConfigDAO();
    	IPRangeDAO ipRangeDao = factory.createIPRangeDAO();
    	ComponentConfigEntry config;

    	// Test reading according to default values		
		meteringDao.removeAll();

		ipRangeDao.removeAll();
    	IPRangeConfigEntry remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(3);
    	remote.setLocalPreference(100);
    	remote.setMED(10);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.128.0");
    	remote.setPrefix_len(17);
    	remote.setASPathLength(4);
    	remote.setLocalPreference(200);
    	remote.setMED(50);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(true);
    	remote.setPrefix("40.10.0.0");
    	remote.setPrefix_len(16);
    	assertTrue(ipRangeDao.persist(remote));

    	Metering m = Metering.getInstance();
    	m.reloadConfig();
    	assertTrue(m.refresh());

    	assertEquals((Double)200.0, m.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)4.0, m.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)50.0, m.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)1.0, m.getAddressParams("40.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)1.0, m.getAddressParams("40.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)1.0, m.getAddressParams("40.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)1.0, m.getAddressParams("40.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)0.5, m.getAddressParams("30.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.25, m.getAddressParams("30.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.8, m.getAddressParams("30.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m.getAddressParams("30.10.132.25").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)1.0, m.getAddressParams("30.10.133.25").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m.getAddressParams("30.10.134.25").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m.getAddressParams("30.10.135.25").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
		
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_ROUTER);
		assertTrue(meteringDao.persist(config));
    	m.reloadConfig();
    	assertFalse(m.refresh());

    	meteringDao.removeAll();
    	
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_FILE);
		assertTrue(meteringDao.persist(config));
    	m.reloadConfig();
    	assertFalse(m.refresh());
	}
	
	@Test 
	public void meteringInterfaceSingletonTestCase() {

    	SisDAOFactory factory = SisDAOFactory.getFactory();
    	IComponentConfigDAO meteringDao = factory.createComponentConfigDAO();
    	IPRangeDAO ipRangeDao = factory.createIPRangeDAO();
    	ComponentConfigEntry config;

    	// Test reading from DB		
		meteringDao.removeAll();
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REFRESH_RATE);
		config.setValue(2);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_REMOTE_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));
		config = new ComponentConfigEntry();
		config.setComponent(Metering.COMPONENT_NAME);
		config.setPropName(Metering.METERING_PROP_LOCAL_IP_INFO_SRC);
		config.setValue(Metering.METERING_VAL_IP_INFO_SRC_DB);
		assertTrue(meteringDao.persist(config));

		ipRangeDao.removeAll();
    	IPRangeConfigEntry remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.0.0");
    	remote.setPrefix_len(16);
    	remote.setASPathLength(3);
    	remote.setLocalPreference(100);
    	remote.setMED(10);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(false);
    	remote.setPrefix("30.10.128.0");
    	remote.setPrefix_len(17);
    	remote.setASPathLength(4);
    	remote.setLocalPreference(200);
    	remote.setMED(50);
    	assertTrue(ipRangeDao.persist(remote));
    	remote = new IPRangeConfigEntry();
    	remote.setLocal(true);
    	remote.setPrefix("40.10.0.0");
    	remote.setPrefix_len(16);
    	assertTrue(ipRangeDao.persist(remote));

    	Metering m = Metering.getInstance();
    	m.reloadConfig();
    	assertTrue(m.refresh());
    	
		// Test metering singleton
		Metering m2 = Metering.getInstance();
		m2.print();
		assertEquals((Double)200.0, m2.getMaxParams().get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)4.0, m2.getMaxParams().get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)50.0, m2.getMaxParams().get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)1.0, m2.getAddressParams("40.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)1.0, m2.getAddressParams("40.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)1.0, m2.getAddressParams("40.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)1.0, m2.getAddressParams("40.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m2.getAddressParams("30.10.1.15").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)0.5, m2.getAddressParams("30.10.2.15").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.25, m2.getAddressParams("30.10.3.15").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.8, m2.getAddressParams("30.10.4.15").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));

		assertEquals((Double)0.0, m2.getAddressParams("30.10.201.25").get(Metering.PARAM_PEER_LOCALITY));
		assertEquals((Double)1.0, m2.getAddressParams("30.10.202.25").get(Metering.PARAM_ROUTE_PREFERENCE));
		assertEquals((Double)0.0, m2.getAddressParams("30.10.203.25").get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY));
		assertEquals((Double)0.0, m2.getAddressParams("30.10.204.25").get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE));
    	assertTrue(m2.refresh());
	}

}
