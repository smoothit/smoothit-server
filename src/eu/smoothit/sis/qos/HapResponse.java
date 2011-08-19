package eu.smoothit.sis.qos;

import java.util.List;

/**
 * The structure that holds the report to be send to the NMS,
 * containing all the peers (IPs) that are promoted to or demoted from HAPs.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class HapResponse {
	
	/**
	 * The list of promoted peers.
	 */
	protected List<String> promoted;
	
	/**
	 * The list of demoted peers.
	 */
	protected List<String> demoted;

	/**
	 * Gets the list of promoted peers.
	 * 
	 * @return The list of IPs of the promoted peers.
	 */
	public List<String> getPromoted() {
		return promoted;
	}

	/**
	 * Sets the list of promoted peers.
	 * 
	 * @param promoted The list of promoted peers' IP addresses.
	 */
	public void setPromoted(List<String> promoted) {
		this.promoted = promoted;
	}

	/**
	 * Gets the list of demoted peers.
	 * 
	 * @return The list of IPs of the demoted peers.
	 */
	public List<String> getDemoted() {
		return demoted;
	}

	/**
	 * Sets the list of demoted peers.
	 * 
	 * @param demoted The list of deomoted peers' IP addresses.
	 */
	public void setDemoted(List<String> demoted) {
		this.demoted = demoted;
	}
	
	

}
