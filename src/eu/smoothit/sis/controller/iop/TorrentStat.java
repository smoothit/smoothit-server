package eu.smoothit.sis.controller.iop;

/**
 * A structure for holding torrent statistics provided to the IoP.
 * 
 * @author Michael Makidis, Sergios Soursos, Intracom Telecom
 */
public class TorrentStat{
	
	/**
	 * The hash code of the specific torrent.
	 */
	protected String torrentHash;
	
	/**
	 * The SIS's rate of the specific torrent.
	 */
	protected double rate;
	
	/**
	 * The location descriptor (URL) of the specific torrent.
	 */
	protected String torrentURL;
	
	/**
	 * Gets the hash code of the specific torrent.
	 * 
	 * @return The torrent hash code.
	 */
	public String getTorrentHash() {
		return torrentHash;
	}
	
	/**
	 * Sets the hash code of the specific torrent.
	 * 
	 * @param torrentHash The torrent hash code.
	 */
	public void setTorrentHash(String torrentHash) {
		this.torrentHash = torrentHash;
	}
	
	/**
	 * Gets the swarm rating of the specific torrent.
	 * 
	 * @return The swarm rating.
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * Sets the swarm rating of the specific torrent.
	 * 
	 * @param rate The swarm rating.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * Gets the location descriptor (URL) of the specific torrent.
	 * 
	 * @return The location descriptor (URL).
	 */
	public String getTorrentURL() {
		return torrentURL;
	}
	
	/**
	 * Sets the location descriptor (URL) of the specific torrent.
	 * 
	 * @param torrentURL The location descriptor (URL).
	 */
	public void setTorrentURL(String torrentURL) {
		this.torrentURL = torrentURL;
	}
	

}
