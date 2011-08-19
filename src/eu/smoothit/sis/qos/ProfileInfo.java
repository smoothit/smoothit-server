package eu.smoothit.sis.qos;

/**
 * The structure that holds the profile of a single customer of the ISP.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class ProfileInfo {
	
	/**
	 * The IP address assigned to the customer.
	 */
	protected String ip;
	
	/**
	 * The uplink capacity of the customer's access link (in Kbits).
	 */
	protected int uplink;
	
	/**
	 * Gets the customer's assigned IP.
	 * 
	 * @return The IP of the customer.
	 */
	public String getIp() {
		return ip;
	}
	
	/**
	 * Sets the customer's assigned IP.
	 * 
	 * @param ip The customer's IP.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * Gets the customer's uplink capacity.
	 * 
	 * @return The customer's uplink capacity (in Kbits)
	 */
	public int getUplink() {
		return uplink;
	}
	
	/**
	 * Sets the customer's uplink capacity.
	 * 
	 * @param uplink The customers' uplink capacity (in Kbits).
	 */
	public void setUplink(int uplink) {
		this.uplink = uplink;
	}
}
