package eu.smoothit.sis.controller.peerrating;

import java.util.List;

/**
 * Web service response from SIS client,
 * as defined in SmoothIT D3.1.
 * 
 * @author Michael Makidis, Intracom Telecom                  
 */
public class Response {
	
	/**
	 * Extra parameters to be passed at the client.
	 */
	protected List<String> extentions;

	/**
	 * IP addresses that have been ranked.
	 */
	protected List<ResponseEntry> entries;

	/**
	 * Gets the extensions of the response.
	 * 
	 * @return A list with the extensions.
	 */
	public List<String> getExtentions() {
		return extentions;
	}

	/**
	 * Sets the extensions of the response.
	 * 
	 * @param extentions The extentions for this response.
	 */
	public void setExtentions(List<String> extentions) {
		this.extentions = extentions;
	}

	/**
	 * Gets the ranked IP addresses.
	 * 
	 * @return A list with ResponseEntry objects containing the ranking.
	 */
	public List<ResponseEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Sets the ranked IP addresses.
	 * 
	 * @param entries The list with ResponseEntry objects containing the ranking.
	 */
	public void setEntries(List<ResponseEntry> entries) {
		this.entries = entries;
	}

}
