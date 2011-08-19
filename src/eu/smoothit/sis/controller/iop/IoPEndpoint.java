package eu.smoothit.sis.controller.iop;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * The Web Service interface for the IoP client.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
@WebService(name = "SisIoPPort", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
public interface IoPEndpoint {
	
   /**
	 * Returns torrent popularity statistics based on previous client requests.
	 * 
	 * @param maxTorrents The maximum number of torrent statistics.
	 * @return Torrent statistics
	 */
	@WebMethod
	@WebResult(name = "stats", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	List<TorrentStat> getTorrentStats(int maxTorrents);
	
	/**
	 * Called by the IoP to set the current swarms it participates in and, as a reply,
	 * the SIS sends back the IPs of the local peers participating in the specific swarms.
	 * 
	 * @param torrents A list with the swarms the IoP participates in, as well as info 
	 * required on local peers participating in these swarms. 
	 * 
	 * @return The list of local peers.
	 */
	@WebMethod
	@WebResult(name = "response", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	List<ResponseEntry> activeInTorrents(List<ActiveTorrent> torrents);
	
	/**
	 * Sends a report activity of the peer for the swarms it participates in.  
	 * 
	 * @param report The report of peer's activity.
	 */
	@WebMethod
	@WebResult(name = "activity", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	void reportActivity(ActivityReport report);
	
	/**
	 * Sends the configuration parameters for the IoP functionality.  
	 * 
	 * @param ipAddress The IP address of the IoP.
	 */
	@WebMethod
	@WebResult(name = "config", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	ConfigParams getConfigParams(String ipAddress);
		
}
