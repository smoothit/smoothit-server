package eu.smoothit.sis.controller.hap;

import java.util.List;

/**
 * Info about the HAP-related statistics of a peer.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */

public class PeerStats {
	
	/**
	 * The IP address of the reporting peer.
	 */
	protected String ipAddress;
	
	/**
	 * The listen port number of the reporting peer.
	 */
	protected int port;
	
	/**
	 * The statistics about the connected neighbors of the reporting peer.
	 */
	protected List<NeighborStats> neighbors;

	/**
	 * Gets the IP address of the reporting peer.
	 * 
	 * @return The IP address of the reporting peer.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the IP address of the reporting peer.
	 * 
	 * @param ipAddress The IP address of the reporting peer.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the listen port number of the reporting peer.
	 * 
	 * @return The listen port number of the reporting peer.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the listen port number of the reporting peer.
	 * 
	 * @param port The listen port number of the reporting peer.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the list of statistic info about the connected neighbors of the reporting peer.
	 * 
	 * @return The list of statistic info about the connected neighbors of the reporting peer.
	 */
	public List<NeighborStats> getNeighbors() {
		return neighbors;
	}

	/**
	 * Sets the list of statistic info about the connected neighbors of the reporting peer.
	 * 
	 * @param neighbors The list of statistic info about the connected neighbors of the reporting peer.
	 */
	public void setNeighbors(List<NeighborStats> neighbors) {
		this.neighbors = neighbors;
	}
		
}
