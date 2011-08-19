package eu.smoothit.sis.controller.iop;

import java.util.List;

import javax.ejb.Local;

/**
 * The local interface for the rating swarms 
 * implemented as an EJB3 session bean
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 *                  
 */
@Local
public interface TorrentRating {
	
	/**
	 *  Prepares a sorted list of TorrentStat entries which
	 *  represents the most popular torrents according to the SIS (see D2.3).
	 * 
	 *  @param maxTorrents The maximum number of torrents to be returned.
	 *  
	 *  @return The list of ranked torrents
	 */
	public List<TorrentStat> getRankedTorrents(int maxTorrents);
	
	/**
	 *  Receives and stores the activity per swarm from a given peer (see D2.3).
	 * 
	 *  @param report The report object to be stored.
	 *  
	 */
	public void storePeerActivity(ActivityReport report);
	
	/**
	 *  Receives the activity report from the IoP, stores it in the DB and
	 *  informs the IoP about the local peers that are active in the specific
	 *  swarms.
	 *  
	 *  @param torrents The list of torrent the IoP participates in, along with
	 *  some parameters for the information (local peers) to be sent by the SIS.
	 *  
	 *  @return The list of of active local peers, per swarm.
	 */
	public List<ResponseEntry> updateAndRetrieveSwarmInfo(List<ActiveTorrent> torrents);

}
