package eu.smoothit.sis.controller.hap.impl;

/**
 * Temporary structure that hold HAP-related rate for a candidate peer. 
 * 
 * @author Sergios Soursos, Intracom Telecom
 *
 */
public class HapRateEntry {
	
	/**
	 * The IP address of the peer.
	 */
	protected String ip_address;
	
	/**
	 * The port for the peer.
	 */
	protected int port;
	
	/**
	 * The HAP-related rate of the peer.
	 */
	protected double rate;
	
	/**
	 * Gets the IP address of the peer.
	 * 
	 * @return The IP address of the peer.
	 */
	public String getIp_address() {
		return ip_address;
	}
	
	/**
	 * Sets the IP address of the peer.
	 * 
	 * @param ipAddress The IP address of the peer.
	 */
	public void setIp_address(String ipAddress) {
		ip_address = ipAddress;
	}
	
	/**
	 * Gets the port of the peer.
	 * 
	 * @return The port of the peer.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the port of the peer.
	 * 
	 * @param port The port of the peer.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Gets the Hap-related rate of the candidate peer.
	 * @return The rate of the peer.
	 */
	public double getRate() {
		return rate;
	}
	
	/**
	 * Sets the Hap-related rate of the candidate peer.
	 * 
	 * @param rate The rate of the peer.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
}
