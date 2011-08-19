package eu.smoothit.sis.controller.peerrating;

import java.util.List;

/**
 * IP address rank request from SIS client,
 * as defined in SmoothIT D3.1.
 * 
 * @author Michael Makidis, Intracom Telecom
 */
public class RequestEntry {
	
	/**
	 * IP address that should be ranked.
	 */
	protected String ipAddress;
	
	/**
	 * Extra parameters for this request.
	 */
	protected List<String> extentions;

	/**
	 * Get the extra parameters for this request.
	 * 
	 * @return A list with the extensions.
	 */
	public List<String> getExtentions() {
		return extentions;
	}

	/**
	 * Set the extra parameters for this request.
	 * 
	 * @param extentions The extentions for this request entry.
	 */
	public void setExtentions(List<String> extentions) {
		this.extentions = extentions;
	}

	/**
	 * Get the IP address that should be ranked.
	 * 
	 * @return The IP address that should be ranked.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set the IP address that should be ranked.
	 * 
	 * @param ipAddress The IP address that should be ranked.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
		
}
