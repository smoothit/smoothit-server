package eu.smoothit.sis.controller.iop;

import java.util.List;

/**
 * The response sent from the SIS to the IoP in order to report
 * the local peers active in the swarms that the IoP particpates in.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class ResponseEntry {
	
	/**
	 * The torrent ID of a swarm.
	 */
	protected String torrentID;
	
	/**
	 * The list of peers active in the specific swarm.
	 */
	protected List<PeerInfo> peers;

	/**
	 * Gets the torrent ID of a swarm.
	 * 
	 * @return The torrent ID of the swarm
	 */
	public String getTorrentID() {
		return torrentID;
	}

	/**
	 * Sets the torrent ID of a swarm.
	 * 
	 * @param torrentID The torrent ID of the swarm
	 */
	public void setTorrentID(String torrentID) {
		this.torrentID = torrentID;
	}

	/**
	 * Gets the list of local peers active in the swarm.
	 * 
	 * @return The list of local peers in the swarm
	 */
	public List<PeerInfo> getPeers() {
		return peers;
	}

	/**
	 * Sets the list of local peers active in the swarm.
	 * 
	 * @param peers The list of local peers in the swarm
	 */
	public void setPeers(List<PeerInfo> peers) {
		this.peers = peers;
	}

}
