package eu.smoothit.sis.qos;

import java.util.List;

import javax.ejb.Local;

/**
 * The local bean that interfaces with the billing system of the ISP,
 * so as to fetch the uplink profiles of its customers.
 * 
 * @author Sergios Soursos, Intracom Telecom
 *
 */
@Local
public interface BillingAdaptor {
	
	/**
	 * Interacts with the ISP to fetch the uplink profiles for certain IP addresses (peers).
	 * 
	 * @param ips The IP addresses of the peers in question.
	 * @return The list of customers' uplink profiles.
	 */
	public List<ProfileInfo> fetchProfiles(List<String> ips);

}
