package eu.smoothit.sis.qos.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.qos.BillingAdaptor;
import eu.smoothit.sis.qos.ProfileInfo;
import eu.smoothit.sis.qos.impl.BillingAdaptorImpl;

/**
 * Tests the BillingAdaptor bean
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class BillingAdaptorTestSuite {
	
	private static BillingAdaptor billing;
	
	@BeforeClass
	public static void init() {
		
		billing = new BillingAdaptorImpl();
		
	}
	
	@AfterClass
	public static void deleteTestData()	{
		
	}
	
	@Test
	public void BillingTestCase() {
		
		List<String> ips = new ArrayList<String>();
		ips.add("10.0.0.1");
		ips.add("10.0.0.2");
		
		List<ProfileInfo> profs = billing.fetchProfiles(ips);
		
		assertEquals(2, profs.size());
		for(ProfileInfo p: profs){
			if (p.getIp().equals("10.0.0.1"))
				assertEquals(512, p.getUplink());
			else if (p.getIp().equals("10.0.0.2"))
				assertEquals(512, p.getUplink());
		}
		
	}

}
