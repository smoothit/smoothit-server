package eu.smoothit.sis.controller.peerrating;

import java.util.List;

/**
 * IP address rank response from SIS client,
 * as defined in SmoothIT D3.1.
 * 
 * @author Michael Makidis, Intracom Telecom
 */
public class ResponseEntry {
	
	/**
	 * IP address that has been ranked.
	 */
	protected String ipAddress;
	
	/**
	 * The SIS (ISP) preference for this IP address.
	 */
	protected int preference;

	/**
	 * Extra parameters for this response.
	 */
	protected List<String> extentions;
	
	/**
	 * Get the IP address that has been ranked.
	 * 
	 * @return The IP address that has been ranked.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set the IP address that has been ranked.
	 * 
	 * @param ipAddress The IP address that has been ranked.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Get the ranking of this IP address.
	 * 
	 * @return The SIS (ISP) preference for this IP address.
	 */
	public int getPreference() {
		return preference;
	}

	/**
	 * Set the ranking of this IP address.
	 * 
	 * @param preference The SIS (ISP) preference for this IP address.
	 */
	public void setPreference(int preference) {
		this.preference = preference;
	}

	/**
	 * Get the extra parameters for this response.
	 * 
	 * @return A list with the extensions.
	 */
	public List<String> getExtentions() {
		return extentions;
	}

	/**
	 * Set the extra parameters for this response.
	 * 
	 * @param extentions The extentions for this response entry.
	 */
	public void setExtentions(List<String> extentions) {
		this.extentions = extentions;
	}

}
