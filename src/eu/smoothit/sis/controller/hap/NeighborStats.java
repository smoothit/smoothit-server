package eu.smoothit.sis.controller.hap;

/**
 * Info about the uploaded/downloaded volumes per neighbor IP.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */

public class NeighborStats {
	
	/**
	 * The IP address of a neighbor peer.
	 */
	protected String ipAddress;
	
	/**
	 * The uploaded volume to that neighbor peer (in MBs).
	 */
	protected int upVolume;
	
	/**
	 * The downloaded volume from that neighbor peer (in MBs).
	 */
	protected int downVolume;
	
	/**
	 * Gets the IP address of a neighbor peer.
	 * 
	 * @return The IP address of a neighbor peer
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the IP address of a neighbor peer.
	 * 
	 * @param ipAddress The IP address of a neighbor peer
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the uploaded volume to that neighbor peer (in MBs).
	 * 
	 * @return The uploaded volume to that neighbor peer (in MBs).
	 */
	public int getUpVolume() {
		return upVolume;
	}

	/**
	 * Sets the uploaded volume to that neighbor peer (in MBs).
	 * 
	 * @param upVolume The uploaded volume to that neighbor peer (in MBs).
	 */
	public void setUpVolume(int upVolume) {
		this.upVolume = upVolume;
	}

	/**
	 * Gets the downloaded volume to that neighbor peer (in MBs).
	 * 
	 * @return The downloaded volume from that neighbor peer (in MBs).
	 */
	public int getDownVolume() {
		return downVolume;
	}

	/**
	 * Sets the downloaded volume to that neighbor peer (in MBs).
	 * 
	 * @param downVolume The downloaded volume from that neighbor peer (in MBs).
	 */
	public void setDownVolume(int downVolume) {
		this.downVolume = downVolume;
	}

}
