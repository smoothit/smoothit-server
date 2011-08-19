package eu.smoothit.sis.controller.iop;

/**
 * The useful information related to a peer (IP address and port number).
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class PeerInfo {
	
	/**
	 * The IP address of a peer.
	 */
	protected String ipAddress;
	
	/**
	 * The port number where the peer listens to.
	 */
	protected int port;

	/**
	 * Gets the IP address of a peer.
	 * 
	 * @return The IP address of a peer.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the IP address of a peer.
	 * 
	 * @param ipAddress The IP address of a peer.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the port number of a peer.
	 * 
	 * @return The port number of a peer.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port number of a peer.
	 * 
	 * @param port The port number of a peer.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
}
