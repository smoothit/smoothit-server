package eu.smoothit.sis.qos.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.controller.hap.HAPRating;
import eu.smoothit.sis.controller.hap.impl.HAPRatingImpl;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.qos.QosEndpoint;
import eu.smoothit.sis.qos.impl.QosEndpointImpl;

/**
 * Tests the HAP Web Service methods
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class QoSEndpointTestSuite {
	
	private static QosEndpoint endpoint;
	private static HAPRating rater;
	
	@BeforeClass
	public static void init() {
		
		rater = new HAPRatingImpl();
		endpoint = new QosEndpointImpl(rater);
		
	}
	
	@AfterClass
	public static void deleteTestData()	{
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
		
	}
	
	/**
     * Tests whether the getHAPs is handled without any problems, with flag = true
     */
	@Test
	public void getHAPsTestCase1() {
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
		
		HAPEntry entry = new HAPEntry();
		entry.setIp_address("10.0.0.1");
		entry.setListenport(10);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("10.0.0.2");
		entry.setListenport(20);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("10.0.0.3");
		entry.setListenport(30);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		endpoint.getHAPs(true);	
	}
	
	/**
     * Tests whether the getHAPs is handled without any problems, with flag = false
     */
	@Test
	public void getHAPsTestCase2() {
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		iHapDao.removeAll();
		
		HAPEntry entry = new HAPEntry();
		entry.setIp_address("10.0.0.1");
		entry.setListenport(10);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("10.0.0.2");
		entry.setListenport(20);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		entry = new HAPEntry();
		entry.setIp_address("10.0.0.3");
		entry.setListenport(30);
		entry.setHap_flag(true);
		iHapDao.persist(entry);
		
		endpoint.getHAPs(false);
	}

}
