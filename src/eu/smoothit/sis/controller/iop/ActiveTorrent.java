package eu.smoothit.sis.controller.iop;

/**
 * The structure that holds the information per swarm sent from the IoP to report
 * which the swarm it participates in, the number of local peers to be sent by the SIS
 * and whether seeds should be included among them.
 * 
 * @author Sergios Soursos, Intracom Telecom
 *                  
 */
public class ActiveTorrent {
	
	/**
	 * The torrent ID of the swarm.
	 */
	protected String infohash;
	
	/**
	 * The number of maximum peers to be returned by the SIS.
	 */
	protected int maxPeers;
	
	/**
	 * A flag denoting whether seeds should be included in the list 
	 * of peers returned by the SIS. 
	 */
	protected boolean seeds;

	/**
	 * Gets the torrent ID.
	 * 
	 * @return The torrent ID.
	 */
	public String getInfohash() {
		return infohash;
	}

	/**
	 * Sets the torrent ID.
	 * 
	 * @param infohash The torrent ID.
	 */
	public void setInfohash(String infohash) {
		this.infohash = infohash;
	}

	/**
	 * Gets the maximum number of peers.
	 * 
	 * @return The maximum number of peers.
	 */
	public int getMaxPeers() {
		return maxPeers;
	}

	/**
	 * Sets the maximum number of peers.
	 * 
	 * @param maxPeers The maximum number of peers.
	 */
	public void setMaxPeers(int maxPeers) {
		this.maxPeers = maxPeers;
	}

	/**
	 * Gets the flag for including seeds in the returned list.
	 * 
	 * @return The flag for including seeds in the returned list.
	 */
	public boolean isSeeds() {
		return seeds;
	}

	/**
	 * Sets the flag for including seeds in the returned list.
	 * 
	 * @param seeds The flag for including seeds in the returned list.
	 */
	public void setSeeds(boolean seeds) {
		this.seeds = seeds;
	}
	
	

}
