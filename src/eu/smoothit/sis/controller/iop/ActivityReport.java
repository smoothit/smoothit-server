package eu.smoothit.sis.controller.iop;

import java.util.List;

/**
 * Swarm info report send by client (peer),
 * as defined in IoP design document.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */

public class ActivityReport {
	
	/**
	 * IP address of the peer.
	 */
	protected String ipAddress;
	
	/**
	 * Port number of the peer.
	 */
	protected int port;
	
	/**
	 * Extra parameters to be passed by the peer.
	 */
	protected List<String> extentions;
	
	/**
	 * List of report entries per swarm.
	 */
	protected List<ActivityReportEntry> entries;
	
	/**
	 * Gets the IP address of the peer.
	 * 
	 * @return The IP address of the peer.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the IP address of the peer.
	 * 
	 * @param ipAddress The IP address of the peer
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the port number of the peer.
	 * 
	 * @return The port number of the peer.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port number of the peer.
	 * 
	 * @param port The port number of the peer
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Gets the extensions of the report.
	 * 
	 * @return A list with the extensions.
	 */
	public List<String> getExtentions() {
		return extentions;
	}

	/**
	 * Sets the extensions of the report.
	 * 
	 * @param extentions The extensions for this request.
	 */
	public void setExtentions(List<String> extentions) {
		this.extentions = extentions;
	}

	/**
	 * Gets the swarm entries of the report.
	 * 
	 * @return The swarm entries of the report.
	 */
	public List<ActivityReportEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets the swarm entries of the report.
	 * 
	 * @param entries The swarm entries of the report.
	 */
	public void setEntries(List<ActivityReportEntry> entries) {
		this.entries = entries;
	}

}
