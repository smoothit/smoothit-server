package eu.smoothit.sis.controller.iop.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.iop.ActiveTorrent;
import eu.smoothit.sis.controller.iop.ActivityReport;
import eu.smoothit.sis.controller.iop.ActivityReportEntry;
import eu.smoothit.sis.controller.iop.PeerInfo;
import eu.smoothit.sis.controller.iop.ResponseEntry;
import eu.smoothit.sis.controller.iop.TorrentRating;
import eu.smoothit.sis.controller.iop.TorrentStat;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IClientSwarmInfoDAO;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ClientSwarmInfoEntry;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;

/**
 * The implementation of the IoP swarm rating EJB.
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 * 
 */
@Stateless
public class TorrentRatingImpl implements TorrentRating{
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(TorrentRatingImpl.class.getName());
	
	private long retrieveTAge(SisDAOFactory factory){
		
		long t = 1200000 * 1l; // 20 mins
		
		IComponentConfigDAO compDao = factory.createComponentConfigDAO();
		ComponentConfigEntry config = compDao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_IOP, SisWebInitializer.PARAM_IOP_CONTROLLER_T_AGE);
		if(config != null && config.getValue() != null && config.getValue() instanceof String)
			t = Long.valueOf((String) config.getValue()).longValue() * 1000L;
		
		return t;

	}
	
   /**
	 * Prepares a sorted list of TorrentStat entries which
	 * represents the most popular torrents according to the SIS (see D2.3).
	 * 
	 *  @param maxTorrents The maximum number of torrents to be returned.
	 *  
	 *  @return The sorted list of torrents.
	 *         
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<TorrentStat> getRankedTorrents(int maxTorrents) {
		
		if (maxTorrents == 0){
			log.warn("MaxTorrents cannot be zero!");
			return null;
		}
		
		List<TorrentStat> res = new ArrayList<TorrentStat>();
		
		//  Steps:
		//  1. Collect all reported torrents: 
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IClientSwarmInfoDAO dao = factory.createClientSwarmInfoDAO();
		
		long t_age_timestamp = retrieveTAge(factory);

		java.util.Date now = new java.util.Date();
		long timestamp = now.getTime() - t_age_timestamp;
		
		log.debug("Timestamp for comparison (1): " + ((Long) timestamp).toString());
		
		List<String> tmp = dao.getSwarmsYoungerThen(timestamp,false);
		
		if ((tmp == null) || (tmp.size() == 0))
			return null;
		else
			log.debug("number of reported torrents = " + tmp.size());
				
		
		// 2. Rate and sort torrent list
		for(String torID : tmp ){
			log.info("processing torID = " + torID);
			Double fileSize = dao.getFileSizeForTorrentID(torID);
			int locSeeds = dao.getNumberOfLocalSeeders(torID);
			int locLeechers = dao.getNumberOfLocalLeechers(torID);
			 
			double rating = fileSize * locLeechers / (locSeeds +1);
			log.info("calculated rate = " + ((Double) rating).toString());
			 
			TorrentStat torStat = new TorrentStat();
			torStat.setRate(rating);
			torStat.setTorrentHash(torID);
			
			String torURL = "";
			
			ClientSwarmInfoEntry t = new ClientSwarmInfoEntry();
			t.setTorrent_id(torID);
			List<ClientSwarmInfoEntry> lst = dao.get(t);
			if (lst != null && lst.size() > 0){
				log.debug("resulting list size = " + lst.size());
				for (ClientSwarmInfoEntry i : lst)
					if ((i.getTorrent_url() != null) && (!(i.getTorrent_url().equals("")))){
						torURL = i.getTorrent_url();
						break;
					}
			}
			
			log.debug("torURL = " + torURL);
			torStat.setTorrentURL(torURL);
						 
			res.add(torStat);					 
		}
		
		log.debug("final size = " + res.size());
		
		Collections.sort(res, new TorrentStatComparator());
		
		log.debug("maxTorrents = " + maxTorrents);
		
		// 3. Trim list if size larger than maxTorrents
		if (res.size() > maxTorrents)
			res = res.subList(0, maxTorrents);
		
		log.debug("final 2 size = " + res.size());
 
		return res;
	}
	
	/**
	 * Receives the list of swarms a peer participates in as well as other related info .
	 * 
	 * @param report The activity report sent by a peer.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void storePeerActivity(ActivityReport report) {
		
		IClientSwarmInfoDAO iClientDao= SisDAOFactory.getFactory().createClientSwarmInfoDAO();
				
		String clientIp = report.getIpAddress();
		int clientPort = report.getPort();
		ClientSwarmInfoEntry clientEntry;
		
		if ((report.getEntries() != null) && (report.getEntries().size() > 0))
			for (ActivityReportEntry e: report.getEntries()){
				clientEntry = new ClientSwarmInfoEntry();
				clientEntry.setIp(clientIp);
				clientEntry.setPort(clientPort);
				clientEntry.setTorrent_id(e.getTorrentID());
				clientEntry.setTorrent_url(e.getTorrentURL());
				clientEntry.setFile_size((Double) e.getFileSize());
				
				List<ClientSwarmInfoEntry> response = iClientDao.get(clientEntry);
				
				if (response == null || response.size() == 0) {
					clientEntry.setDownload_progress((Float) e.getProgress());
					java.util.Date now = new java.util.Date();
					Long timestamp = now.getTime();
					clientEntry.setTimestamp(timestamp);
					iClientDao.persist(clientEntry);
				} else if (response.size() ==1) {
					ClientSwarmInfoEntry responseEntry = response.get(0);
					responseEntry.setDownload_progress(e.getProgress());
					java.util.Date now = new java.util.Date();
					Long timestamp = now.getTime();
					responseEntry.setTimestamp(timestamp);
					iClientDao.update(responseEntry);
				} else{
					log.warn("Error: Duplicate entries for the same swarm-client pair found in ClientSwarmInfo table!");
					log.warn("Nothing to be stored/updated in DB due to this error!");
				}
			}
		else
			log.warn("Nothing to be stored in DB since entries list is null.");
				
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ResponseEntry> updateAndRetrieveSwarmInfo(List<ActiveTorrent> torrents){
		
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IClientSwarmInfoDAO iClientDao= factory.createClientSwarmInfoDAO();
		
		long t_age_timestamp = retrieveTAge(factory);
		
		java.util.Date now = new java.util.Date();
		long timestamp = now.getTime() - t_age_timestamp;
		
		log.debug("Timestamp for comparison (2): " + ((Long) timestamp).toString());
		
		List<String> old_info = iClientDao.getSwarmsYoungerThen(timestamp, true);
		if (old_info != null && old_info.size() > 0)
			log.debug("old_info size: " + old_info.size());
		
		if ((torrents == null) || (torrents.size() == 0)){
			
			if (old_info != null && old_info.size() > 0) 
				for (String id: old_info)
					iClientDao.setIoPParticipation(id, false);
			
			return null;
		}
		
		List<ResponseEntry> res = new ArrayList<ResponseEntry>();
		
		for (ActiveTorrent tor: torrents){
			String torID = tor.getInfohash();
			log.debug("torrent ID: " + torID);
			
			ClientSwarmInfoEntry check = new ClientSwarmInfoEntry();
			check.setTorrent_id(torID);
			if (iClientDao.get(check) == null || iClientDao.get(check).size() == 0)
				continue;
			
			log.debug("torrent ID: " + torID + " = found !");
			
			if (old_info != null && old_info.size() >0 && old_info.contains(torID))
				old_info.remove(torID);

			iClientDao.setIoPParticipation(torID, true);
			
			ResponseEntry e = new ResponseEntry ();
			e.setTorrentID(torID);
			List<PeerInfo> peer_list = new ArrayList<PeerInfo>();
			
			int maxp = tor.getMaxPeers();
			int no_seeds = 0;
			int no_leechers = 0;
			
			int final_no_seeds = 0;
			int final_no_leechers = 0;			
			
			if (maxp == 0)
				e.setPeers(null);
			else if (tor.isSeeds()) {
				no_seeds = iClientDao.getNumberOfLocalSeeders(torID);
				no_leechers = iClientDao.getNumberOfLocalLeechers(torID);
				
				log.debug("Req. seeds= " + no_seeds);
				log.debug("Req. leechers= " + no_leechers);
				
				final_no_seeds = 0;
				final_no_leechers = 0;
				
				float factor = (no_seeds + no_leechers) / (float) maxp;
				
				if (factor > 1){
					final_no_leechers = ((Float)(no_leechers / factor)).intValue();
					final_no_seeds = ((Float)(no_seeds / factor)).intValue();
					if ((final_no_seeds + final_no_leechers) > maxp) {
						if (no_seeds > no_leechers)
							final_no_seeds = final_no_seeds - 1;
						else
							final_no_leechers = final_no_leechers - 1;
					} else if ((final_no_seeds + final_no_leechers) < maxp){
						if (final_no_seeds < no_seeds)
							final_no_seeds = final_no_seeds + 1;
						else
							final_no_leechers = final_no_leechers + 1;
					}
				} else if (factor <= 1){
					final_no_seeds = no_seeds;
					final_no_leechers = no_leechers;
				}
					
				List<String> seeds = iClientDao.getLocalSeeders(torID);
				if (seeds != null && seeds.size() >0){
					seeds = seeds.subList(0, final_no_seeds);
					for (String s: seeds){
						PeerInfo tmp = createEntry(s, torID, iClientDao);
						peer_list.add(tmp);
					}
				}
				
				List<String> leechers = iClientDao.getLocalLeechers(torID);
				if (leechers != null && leechers.size() >0){
					leechers = leechers.subList(0, final_no_leechers);
					for (String l: leechers){
						PeerInfo tmp = createEntry(l, torID, iClientDao);
						peer_list.add(tmp);
					}
				}
				
				e.setPeers(peer_list);
			}
			else {
				no_leechers = iClientDao.getNumberOfLocalLeechers(torID);
				log.debug("Req. leechers= " + no_leechers);
				
				if (no_leechers >= maxp)
					final_no_leechers = maxp;
				else
					final_no_leechers = no_leechers;				
				
				List<String> leechers = iClientDao.getLocalLeechers(torID);
				if (leechers != null && leechers.size() >0){
					leechers = leechers.subList(0, final_no_leechers);
					for (String l: leechers){
						PeerInfo tmp = createEntry(l, torID, iClientDao);
						peer_list.add(tmp);
					}
				}
				
				e.setPeers(peer_list);
			}
						
			res.add(e);
		}
		
		if (old_info != null && old_info.size() > 0) 
			for (String id: old_info)
				iClientDao.setIoPParticipation(id, false);
		
		return res;
	}
	
	/**
	 * Creates and entry (per peer and per torrent ID) to be returned to the IoP.
	 * This entry will include the IP of the peer and its port.
	 * 
	 * @param ip The IP of the peer (seed or leecher) to be included in the entry.
	 * @param tor The torrent ID the peer participates in.
	 * @param icdao The DAO to retrieve the port of the peer.
	 * 
	 * @return The entry created.
	 * 
	 */
	private PeerInfo createEntry(String ip, String tor, IClientSwarmInfoDAO icdao){
		PeerInfo tmp = new PeerInfo();
		tmp.setIpAddress(ip);
		
		int tmp_port = 0;
		ClientSwarmInfoEntry t = new ClientSwarmInfoEntry();
		t.setTorrent_id(tor);
		t.setIp(ip);
		List<ClientSwarmInfoEntry> lst = icdao.get(t);
		if (lst != null && lst.size() > 0)
			for (ClientSwarmInfoEntry i : lst)
				if (i.getPort()!= null){
					tmp_port = i.getPort();
					break;
				}
		
		tmp.setPort(tmp_port);
		return tmp;
	}
	
	/**
	 * A comparator for sorting TorrentStat objects based on their rate
	 */
	protected static class TorrentStatComparator implements Comparator<TorrentStat> 
	{
		public int compare(TorrentStat o1, TorrentStat o2) {
			return ((Double)(o2.getRate() - o1.getRate())).intValue();
		}
	}

}
