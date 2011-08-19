package eu.smoothit.sis.controller.iop;

/**
 * Swarm info report entry by the client (peer), 
 * as defined in IoP design document.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */

public class ActivityReportEntry {
	
	/**
	 * The ID of the specific torrent.
	 */
	protected String torrentID;
	
	/**
	 * The location descriptor (URL) of the specific torrent.
	 */
	protected String torrentURL;
	
	/**
	 * The size of the content described by the specific torrent.
	 */
	protected double fileSize;
	
	/**
	 * The download progress (%) of the reporting peer.
	 */
	protected float progress;

	/**
	 * Gets the ID of the specific torrent.
	 * 
	 * @return The ID of the specific torrent.
	 */
	public String getTorrentID() {
		return torrentID;
	}

	/**
	 * Sets the ID of the specific torrent.
	 * 
	 * @param torrentID The ID of the specific torrent.
	 */
	public void setTorrentID(String torrentID) {
		this.torrentID = torrentID;
	}

	/**
	 * Gets the location descriptor (URL) of the specific torrent.
	 * 
	 * @return The location descriptor (URL) of the specific torrent.
	 */
	public String getTorrentURL() {
		return torrentURL;
	}

	/**
	 * Sets the location descriptor (URL) of the specific torrent.
	 * 
	 * @param torrentURL The location descriptor (URL) of the specific torrent.
	 */
	public void setTorrentURL(String torrentURL) {
		this.torrentURL = torrentURL;
	}

	/**
	 * Gets the size of the content described by the specific torrent.
	 * 
	 * @return The size of the content described by the specific torrent.
	 */
	public double getFileSize() {
		return fileSize;
	}

	/**
	 * Sets the size of the content described by the specific torrent.
	 * 
	 * @param fileSize The size of the content described by the specific torrent.
	 */
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Gets the download progress (%) of the reporting peer.
	 * 
	 * @return The download progress (%) of the reporting peer.
	 */
	public float getProgress() {
		return progress;
	}

	/**
	 * Sets the download progress (%) of the reporting peer.
	 * 
	 * @param progress The download progress (%) of the reporting peer.
	 */
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	

}
