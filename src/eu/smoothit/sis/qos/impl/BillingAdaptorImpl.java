package eu.smoothit.sis.qos.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import eu.smoothit.sis.qos.BillingAdaptor;
import eu.smoothit.sis.qos.ProfileInfo;

/**
 * The implementation of local bean that interfaces with the billing system of the ISP,
 * so as to fetch the uplink profiles of its customers.
 * 
 * @author Sergios Soursos, Intracom Telecom
 *
 */
@Stateless
public class BillingAdaptorImpl implements BillingAdaptor{
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(BillingAdaptorImpl.class.getName());
	
	public BillingAdaptorImpl() {}
	
	/**
	 * Interacts with the ISP to fetch the uplink profiles for certain IP addresses (peers).
	 * 
	 * @param ips The IP addresses of the peers in question.
	 * @return The list of customers' uplink profiles.
	 */
	@Override
	public List<ProfileInfo> fetchProfiles(List<String> ips){
		List<ProfileInfo> res = new ArrayList<ProfileInfo>();

		//TODO Implement a client that interfaces with the NMS 
		// and fetches the customers profiles, given the IP.	
		
		//  Assigning default values for now.
		for (String ip: ips){
			ProfileInfo pi = new ProfileInfo();
			pi.setIp(ip);
	        pi.setUplink(512);
	        res.add(pi);
		}

		return res;
	}

}
