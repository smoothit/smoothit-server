package eu.smoothit.sis.controller.peerrating;

import java.util.List;

/**
 * Web service request from SIS client,
 * as defined in SmoothIT D3.1.
 * 
 * @author Michael Makidis, Intracom Telecom
 */
public class Request {
	
	/**
	 * Extra parameters to be passed at the SIS.
	 */
	protected List<String> extentions;

	/**
	 * IP addresses that must be ranked.
	 */
	protected List<RequestEntry> entries;

	/**
	 * Gets the extensions of the request.
	 * 
	 * @return A list with the extensions.
	 */
	public List<String> getExtentions() {
		return extentions;
	}

	/**
	 * Sets the extensions of the request.
	 * 
	 * @param extentions The extentions for this request.
	 */
	public void setExtentions(List<String> extentions) {
		this.extentions = extentions;
	}

	/**
	 * Gets the IP addresses that must be ranked.
	 * 
	 * @return A list with RequestEntry objects containing the IP addresses.
	 */
	public List<RequestEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets the IP addresses that must be ranked.
	 * 
	 * @param entries The list with RequestEntry objects containing the IP addresses. 
	 */
	public void setEntries(List<RequestEntry> entries) {
		this.entries = entries;
	}
	
	
}
