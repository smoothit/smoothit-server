package eu.smoothit.sis.controller.iop.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.iop.ActiveTorrent;
import eu.smoothit.sis.controller.iop.ActivityReport;
import eu.smoothit.sis.controller.iop.ActivityReportEntry;
import eu.smoothit.sis.controller.iop.ConfigParams;
import eu.smoothit.sis.controller.iop.IoPConfig;
import eu.smoothit.sis.controller.iop.IoPEndpoint;
import eu.smoothit.sis.controller.iop.PeerInfo;
import eu.smoothit.sis.controller.iop.ResponseEntry;
import eu.smoothit.sis.controller.iop.TorrentRating;
import eu.smoothit.sis.controller.iop.TorrentStat;

/**
 * IoP Web Service Endpoint using XDoclet
 * 
 * @author Michael Makidis, Sergios Soursos, Intracom Telecom
 * @version 2.0
 * 
 * @web.servlet 
 * 		name = "IoPEndpoint" 
 * 		display-name = "IoPEndpoint"
 * @web.servlet-mapping 
 * 		url-pattern = "/IoPEndpoint"                    
 */
@WebService(
		serviceName = "IoPService",
		endpointInterface = "eu.smoothit.sis.controller.iop.IoPEndpoint",
		targetNamespace = "http://iop.ws.sis.smoothit.eu/",
		portName = "IoPServicePort")
public class IoPEndpointImpl implements IoPEndpoint {
	
	@Resource
	WebServiceContext wsCtxt;
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(IoPEndpointImpl.class.getName());
	
	/**
	 * The Torrent Rating service
	 */
	@EJB
	protected TorrentRating rater;
	
	/**
	 * The IoP Configurator service
	 */
	@EJB
	protected IoPConfig configurator;
	
	/**
	 * Default constructor
	 */
	public IoPEndpointImpl() {}
	
	/**
	 * Constructor that sets the swarm rating bean
	 * 
	 * @param rater The Swarm Rating bean
	 */
	public IoPEndpointImpl(TorrentRating rater) {
		this.rater = rater;
	}
	
	/**
	 * Constructor that sets the configurator bean
	 * 
	 * @param configurator The Configurator bean
	 */
	public IoPEndpointImpl(IoPConfig configurator) {
		this.configurator = configurator;
	}
	
	/**
	 * Constructor that sets the swarm rating and configurator beans
	 * 
	 * @param rater The Swarm Rating and Configurator beans
	 */
	public IoPEndpointImpl(TorrentRating rater, IoPConfig configurator) {
		this.rater = rater;
		this.configurator = configurator;
	}

	/**
	 * Sets the current active torrents of the IoP, so that the IoP can be added
	 * as a preferred peer on a peer request list.
	 * 
	 * @param torrents The hashes of the torrents that the IoP currently participates 
	 * in, along with info about the number of peers to be returned.
	 *  
	 * @return Info (IP, port) about the local peers participating in each reported swarm.
	 */
	@Override
	@WebMethod
	public List<ResponseEntry> activeInTorrents(List<ActiveTorrent> torrents) {
		log.info("activeInTorrents called");
		
		if (torrents != null && torrents.size() > 0) {
			List<ResponseEntry> res = this.rater.updateAndRetrieveSwarmInfo(torrents);
			if (res != null && res.size() >0)
				for (ResponseEntry r: res){
					log.debug("Peers for torID = " + r.getTorrentID());
					List<PeerInfo> ps = r.getPeers();
					if (ps != null && ps.size() >0)
						for (PeerInfo p: ps){
							log.debug("\t Peer's IP = " + p.getIpAddress());
							log.debug("\t Peer's port = " + p.getPort());
						}
					else
						log.warn("\t No peers to report!");
				}
			else
				log.warn("No torrents found, nothing to report!");
			return res;
		} else {
			log.warn("Nothing to report due to null input!");
			return null;
		}
	}

	/**
	 * Returns torrent popularity statistics based on previous client requests.
	 * 
	 * @param maxTorrents The maximum number of torrent statistics.
	 * @return Torrent statistics
	 */
	@Override
	@WebMethod
	@WebResult(name = "stats", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	public List<TorrentStat> getTorrentStats(int maxTorrents) {
				
		log.info("getTorrentStats called");
		List<TorrentStat> res = this.rater.getRankedTorrents(maxTorrents);
		
		if (res != null && res.size() > 0) {
			log.debug("Sending reply of size = " + res.size());
			for (TorrentStat t: res){
				log.debug("\t torrent ID = " + t.getTorrentHash());
				log.debug("\t torrent Rate = " + ((Double)t.getRate()).toString());
				log.debug("\t torrent URL = " + t.getTorrentURL());
			}
		} else
			log.warn("No torrents ratings exist!");
										
		return res;
	}

	/**
	 * Receives the list of swarms a peer participates in as well as other related info .
	 * 
	 * @param report The activity report sent by a peer.
	 */
	@Override
	@WebMethod
	@WebResult(name = "activity", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	public void reportActivity(ActivityReport report) {
		
		log.info("reportActivity called");
		
		if (report != null) {
			log.debug("New activity report received");
			
			try{
				MessageContext msgCtxt = wsCtxt.getMessageContext();
				HttpServletRequest req = (HttpServletRequest)msgCtxt.get(MessageContext.SERVLET_REQUEST);
				String clientIP = req.getRemoteAddr();
				
				log.debug("Client's IP and port: " + clientIP + ":" + Integer.toString(report.getPort()));
				
				report.setIpAddress(clientIP);
				
			} catch (NullPointerException e){
				log.debug("Client's IP and port: " + report.getIpAddress() + ":" + Integer.toString(report.getPort()));
			}			
			
			if ((report.getEntries() != null) && (report.getEntries().size() > 0))
				for (ActivityReportEntry entry: report.getEntries())
					log.debug("torrentID: " + entry.getTorrentID() + ", torrentURL: " + entry.getTorrentURL() + 
							", filesize: " + Double.toString(entry.getFileSize()) + ", progress" + Float.toString(entry.getProgress()));
			
			this.rater.storePeerActivity(report);
		}
		
	}
	
	/**
	 * Returns the configuration parameters for the IoP operation.
	 * 
	 * @param ipAddress The IP address of the IoP.
	 * 
	 * @return The configuration parameters for the IoP. 
	 */
	@Override
	@WebMethod
	@WebResult(name = "config", targetNamespace = "http://iop.ws.sis.smoothit.eu/")
	public ConfigParams getConfigParams(String ipAddress){
		
		log.info("getConfigParams request received from " + ipAddress);
		
		ConfigParams res = configurator.getIoPConfigParams(ipAddress);
		
		if (res != null){
			log.debug("Returning the following config params:");
			log.debug("\t x = " + ((Float) res.getX()).toString());
			log.debug("\t slots = " + res.getSlots());
			log.debug("\t u = " + ((Float) res.getU()).toString());
			log.debug("\t mode = " + res.getMode());
			log.debug("\t t = " + res.getT());
			log.debug("\t connect = " + ((Boolean) res.isRemotes()).toString());
			log.debug("\t d = " + ((Float) res.getD()).toString());
			log.debug("\t dlow = " + ((Float) res.getDlow()).toString());
			log.debug("\t ulow = " + ((Float) res.getUlow()).toString());
			if (res.getLocalIPRanges() != null && res.getLocalIPRanges().length >0){
				log.debug("\t ip list size = " + res.getLocalIPRanges().length);
				for (String i: res.getLocalIPRanges())
					log.debug("\t\t entry = " + i);
			}
		}

		return res;
		
	}

}
