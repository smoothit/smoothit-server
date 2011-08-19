package eu.smoothit.sis.controller.hap.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.hap.HAPRating;
import eu.smoothit.sis.controller.hap.NeighborStats;
import eu.smoothit.sis.controller.hap.PeerStats;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.api.daos.IPeerStatisticsDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatisticsEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;
import eu.smoothit.sis.metering.Metering;
import eu.smoothit.sis.qos.BillingAdaptor;
import eu.smoothit.sis.qos.ProfileInfo;

/**
 * The implementation of the HAP rating EJB.
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 * 
 */
@Stateless
public class HAPRatingImpl implements HAPRating{
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(HAPRatingImpl.class.getName());
	
	/**
	 * The Billing adaptor Bean of the QoS Manager.
	 */
	@EJB 
	private BillingAdaptor billing;
	
	public HAPRatingImpl() {}
	
	public HAPRatingImpl(BillingAdaptor billing) {
		this.billing = billing;
	}

	/**
	 * The parameter p1 of the HAP rating formula.
	 */
	private float p1=1;
	
	/**
	 * The parameter p2 of the HAP rating formula.
	 */
	private float p2=1;
	
	/**
	 * The parameter p3 of the HAP rating formula.
	 */
	private float p3=1;
	
	/**
	 * The available download bandwidth of the ISP.
	 */
	private int ad=0;
	
	/**
	 * The available upload bandwidth of the ISP.
	 */
	private int au=0;
	
	/**
	 * The download bandwidth increase given to a HAP.
	 */
	private int di=1;
	
	/**
	 * The upload bandwidth increase given to a HAP.
	 */
	private int ui=1;
	
	/**
	 * The rating threshold for ingoring low rates.
	 */
	private float rt=0;
	
	/**
	 * The time period to calculate the HAP rates.
	 */
	private int t_update=1;
	
	/**
	 * The maximum number of HAPs allowed.
	 */
	private int n=-1;

	/**
	 * Receives and stores a peer's activity with its neighbors
	 * 
	 * @param report The report of peer's activity with its neighbors.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void storePeerActivity(PeerStats report) {
		
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		
		String clientIp = report.getIpAddress();
		int clientPort = report.getPort();
		
		PeerStatisticsEntry pEntry;
		
		if ((report.getNeighbors() != null ) && (report.getNeighbors().size() > 0) ) {
			pEntry = new PeerStatisticsEntry();
			pEntry.setIp_address(clientIp);
			pEntry.setPort(clientPort);
						
			List<PeerStatisticsEntry> response = iStatsDao.get(pEntry);
			
			if (response.size() > 1 ){
				log.warn("Error: Duplicate entries for the same IP-port pair found in HAPPeerStatistics table!");
				log.warn("Nothing to be stored/updated in DB due to this error!");
			} else {
				
				if (response.size() == 0){
					pEntry = new PeerStatisticsEntry();
					pEntry.setIp_address(clientIp);
					pEntry.setPort(clientPort);
					pEntry.setHap_rating(0D);
					pEntry.setLocal_upload_volume(0D);
					pEntry.setTotal_download_volume(0D);
					pEntry.setTotal_upload_volume(0D);
				} else // response.size() == 1
					pEntry = response.get(0);
				
				double total_up = pEntry.getTotal_upload_volume();
				double total_down = pEntry.getTotal_download_volume();
				double local_up = pEntry.getLocal_upload_volume();
				
				for (NeighborStats n: report.getNeighbors()){
					String tmpIP = n.getIpAddress();
					
					total_up = total_up + n.getUpVolume();
					total_down = total_down + n.getDownVolume();
					
					Map<String, Double> params = 
						Metering.getInstance().getAddressParams(tmpIP);
					
					if (params !=null) {
						Double res = params.get(Metering.PARAM_PEER_LOCALITY);
						if (res!= null && res > 0)
							local_up = local_up + n.getUpVolume();
					}
				}
				
				pEntry.setLocal_upload_volume(local_up);
				pEntry.setTotal_download_volume(total_down);
				pEntry.setTotal_upload_volume(total_up);
				
				if (response.size() == 0)
					iStatsDao.persist(pEntry);
				else //  response.size() == 1
					iStatsDao.update(pEntry);
			}
			
		} else
			log.warn("Nothing to be stored in DB since entries list is null.");
		
	}
	
	/**
	 * Executes the HAP rating algorithm.
	 * 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void calcHAPRatings(){
		
		log.info("Calculating HAP ratings ...");
		
		// Step 1. Fetch HAP-related config params from DB
		IComponentConfigDAO compDao = SisDAOFactory.getFactory().createComponentConfigDAO();
		List<ComponentConfigEntry> lcce = compDao.findByComponent(SisWebInitializer.COMPONENT_NAME_CONTR_HAP);
		
		if (lcce != null && lcce.size() >0) 
			for (ComponentConfigEntry e: lcce){
				if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_P1))
					p1 = Float.parseFloat( (String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_P2))
					p2 = Float.parseFloat( (String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_P3))
					p3 = Float.parseFloat( (String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_AD))
					ad = Integer.parseInt((String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_AU))
					au = Integer.parseInt((String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_DI))
					di = Integer.parseInt((String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_UI))
					ui = Integer.parseInt((String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_RT))
					rt = Float.parseFloat((String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_T_UPDATE))
					t_update = Integer.parseInt((String) e.getValue());
				else if (e.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_HAP_CONTROLLER_N))
					n = Integer.parseInt((String) e.getValue());
			}
		
		// Step 2. Fetch from DB the IPs for which HAP-relate statistics exist
		//         and store them in a temporary HashMap structure for easier processing.
		IPeerStatisticsDAO iStatsDao = SisDAOFactory.getFactory().createPeerStatisticsDAO();
		List<List<String>> peers = iStatsDao.getPeers();
		
		if (peers == null || peers.size() ==0)
			return;
		
		log.debug("Number of peers in iStatsDao: " + peers.size());
		
		// Fills a temporary structure that will hold the bandwidth per (ip, port) tuple
		Map<String, Integer> candHaps= new HashMap<String, Integer>();
		for (List<String> tuple: peers){
			String info = tuple.get(0) + ":" + tuple.get(1);
			log.debug("Storing peer " +info);
			candHaps.put(info,0);
		}
		
		// Step 3. Ask Qos/Billing for profiles of the collected IPs
		//         and update the temporary HashMap structure
		
		List<String> ips = new ArrayList<String>();
		for (List<String> tuple: peers)
			if (!ips.contains(tuple.get(0)))
				ips.add(tuple.get(0));
		
		log.info("Retrieving info from Billing...");
		List<ProfileInfo> profs = billing.fetchProfiles(ips);
		
		if (profs != null && profs.size()>0)
			for (ProfileInfo p : profs) {
				PeerStatisticsEntry pse = new PeerStatisticsEntry();
				pse.setIp_address(p.getIp());
				log.debug("Searching iStatsDao for IP: "+ p.getIp());
				List<PeerStatisticsEntry> lpse = iStatsDao.get(pse);
			
				if (lpse == null || lpse.size() == 0)
					continue;
				
				log.debug("Entries returned: "+ lpse.size());
			
				for (PeerStatisticsEntry answ: lpse){
					String info = answ.getIp_address() + ":" + answ.getPort();
					log.debug("Updating entry (" +info+ ") with BW: "+ p.getUplink());
					candHaps.put(info, p.getUplink());
				}
			}
		
		// Step 4. Get 'promoted' HAPs and update the temporary HashMap structure
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		List<List<String>> prom = iHapDao.getPromotedHAPs();
		if (prom!= null && prom.size()>0){
			log.info("Number of peers promoteed to HAPs: " + prom.size());
			for (List<String> tuple: prom){
				String info = tuple.get(0) +":" +tuple.get(1);
				int old_bw = candHaps.get(info);
				candHaps.put(info, old_bw + ui);
				log.info("Peer " +info+ " and BW: " + old_bw + " has additional BW of " + ui);
			}
		} else
			log.info("No peers are already promoteed to HAPs");
		
		// Step 5. Calculate HAP ratings.
		List<HapRateEntry> list_of_haps = new ArrayList<HapRateEntry>();
		
		Object[] keys = candHaps.keySet().toArray();
		for (int i=0;i<keys.length;i++){
			
			String info = (String) keys[i];
			int bw = ((Integer) candHaps.get(info)).intValue();
			
			// To avoid divide by zero, in case of Billing Service down, 
			// consider the default value of '512' for the uplink capacity (Kbps)
			// if (bw <= 0)
			//	 bw = 512;
			// Included in Billing Adaptor implementation code...
			
			PeerStatisticsEntry e = new PeerStatisticsEntry();
			String t_ip = info.substring(0, info.indexOf(":"));
			int t_port = Integer.valueOf(info.substring(info.indexOf(":")+1)).intValue();
			e.setIp_address(t_ip);
			e.setPort(t_port);
			
			log.info("Calculating HAP rating for peer " +info);
			log.debug("Peer's BW: " + bw);
			
			List<PeerStatisticsEntry> lst = iStatsDao.get(e);
			// We expect here only one entry per (IP, port) since candHaps is constructed from iStatsDao
			// and we avoid duplicate entries in the 'storePeerActivity' function (above)
						
			double lu = 0;
			double tu = 0;
			double td = 0;
			
			PeerStatisticsEntry new_e = lst.get(0);
				
			lu = new_e.getLocal_upload_volume();
			td = new_e.getTotal_download_volume();
			tu = new_e.getTotal_upload_volume();
			
			log.debug("Peer's stats: " + ((Double) lu).toString() + ", " + ((Double) tu).toString() +", "+((Double) td).toString());
				
			new_e.setLocal_upload_volume(0D);
			new_e.setTotal_download_volume(0D);
			new_e.setTotal_upload_volume(0D);
			iStatsDao.update(new_e);
						
			double old_rate;
			if (new_e.getHap_rating() != null)
				old_rate = new_e.getHap_rating();
			else
				old_rate = 0;
			
			double seed_rt = 0;
			/* 
			 * ignore seeding ratio in the rating formula: seed_rt=0 !
			 * 
			if (td != 0 )
				seed_rt = tu / td;
			else
				seed_rt = 1000; // something very big, used for seeders
			*/ 
			
			double rate = p1 * ((1024 * 8 * lu) / (t_update * bw)) + p2 * seed_rt + p3 * ((1024 * 8 * tu)/(t_update * bw));
			
			log.debug("Peer's rate:" + ((Double) rate).toString());
			
			double new_rate = rate + java.lang.Math.exp(-1) * old_rate;
			
			log.debug("Peer's new rate:" + ((Double) new_rate).toString());
			
			log.debug("Rating threshold :" + ((Float) rt).toString());
			
			if (rate >= rt) {
				
				log.debug("Peer added to list_of_haps");
				
				new_e.setHap_rating(new_rate);
				iStatsDao.update(new_e);
												
				HapRateEntry hapEnt = new HapRateEntry();
				String tt_ip = info.substring(0, info.indexOf(":"));
				int tt_port = Integer.valueOf(info.substring(info.indexOf(":")+1)).intValue();
				hapEnt.setIp_address(tt_ip);
				hapEnt.setPort(tt_port);
				hapEnt.setRate(new_rate);
				
				list_of_haps.add(hapEnt);
			} else {
				log.debug("Peer removed from iStatsDao");
				iStatsDao.remove(new_e);
			}
		}
		
		// Step 6. Sort list
		Collections.sort(list_of_haps, new HAPComparator());
				
		
		// Step 6. Calculate N (if it does not exist as a config param)
		if (n == -1) {
			log.debug("Calculating N...");
			int n1 = ad / di;
			int n2 = au / ui;
			
			n = java.lang.Math.min(n1, n2);
			
			log.debug("New N: " + n);
		} else
			log.debug("N: " + n);
		
		// Step 7. Keep top N entries of the ranked list
		if (n <= list_of_haps.size())
			list_of_haps = list_of_haps.subList(0, n);
			
		
		// Step 8. Remove previous 'new' HAPs
		log.info("Removing previous 'new' HAPs...");
		List<List<String>> old_new = iHapDao.getNewHAPs();
		if (old_new != null && old_new.size()>0){
			log.debug("Previous new HAPs: " + old_new.size());
			for (List<String> tuple: old_new){
				HAPEntry he = new HAPEntry();
				he.setIp_address(tuple.get(0));
				he.setListenport(Integer.parseInt(tuple.get(1)));
				List<HAPEntry> lhe = iHapDao.get(he);
				iHapDao.remove(lhe.get(0));
			}
		} else
			log.debug("No previous new HAPs");
		
		// Step 9. Store 'new' HAPs
		// At this point, only 'promoted' HAPs are included in the iHapDao (due to step 8).
		// Inserting a 'new' HAP does not exclude the fact that the peer may already be 'promoted'
		// This is to be examined later while determining which peers to promote, denote or keep promoted.
		log.info("Adding 'new' HAPs...");
		for (HapRateEntry h : list_of_haps){
			HAPEntry he = new HAPEntry();
			he.setIp_address(h.getIp_address());
			he.setListenport(h.getPort());
			he.setHap_flag(true);
			iHapDao.persist(he);
			log.debug("Adding IP: " + h.getIp_address()+" and port: " +h.getPort()+ " to new haps");			
		}
	}
	
	/**
	 * Decides which peers to be promoted to and demoted from HAPs.
	 * 
	 * @param delta Whether to report peers that are already HAPs and should remain HAPs (true=no, false=yes)
	 * @return The list of of promoted and demoted HAPs.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public List<List<String>> fetchHAPs(boolean delta){
		
		List<String> promoted = new ArrayList<String>();
		List<String> demoted = new ArrayList<String>();
		
		boolean proms_exist = true;
		boolean news_exist = true;		
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		
		// The list with already promoted HAPs (from previous run).
		List<List<String>> old_proms = iHapDao.getPromotedHAPs();
		// The list of new peers to be promoted to HAPs.
		List<List<String>> old_news = iHapDao.getNewHAPs();
		
		
		if (old_proms == null || old_proms.size() <=0)
			proms_exist=false;
		
		if (old_news == null || old_news.size() <=0)
			news_exist=false;
		
		if (!proms_exist){
			if (news_exist)
				// if no old HAPs exist but new HAPs do exist.
				for(List<String> n :old_news){
					String ip = n.get(0);
					int port = Integer.valueOf(n.get(1));
					
					// Avoid sending duplicate IP entries as promoted.
					if (!promoted.contains(ip))
						promoted.add(ip);
					
					HAPEntry he = new HAPEntry();
					he.setIp_address(ip);
					he.setListenport(port);
					List<HAPEntry> updt = iHapDao.get(he);
					//Update peer's status to promoted HAP (not "new").
					if (updt != null && updt.size() >0){
						updt.get(0).setHap_flag(false);
						iHapDao.update(updt.get(0));
					}
				}
			else 
				promoted = null;
			demoted = null;
			
		} else {
			if (!news_exist){
				// if old HAPs exist but no new HAPs exist.
				promoted = null;
				for(List<String> n :old_proms){
					String ip = n.get(0);
					int port = Integer.valueOf(n.get(1));
					
					// Avoid sending duplicate IP entries as demoted.
					if (!demoted.contains(ip))
						demoted.add(ip);
					
					HAPEntry he = new HAPEntry();
					he.setIp_address(ip);
					he.setListenport(port);
					List<HAPEntry> updt = iHapDao.get(he);
					//Remove peer from promoted HAPs.
					if (updt != null && updt.size() >0)
						iHapDao.remove(updt.get(0));
				}
			} else {
				// if old and new HAPs exist.
				
				// for peers decided to be promoted
				for(List<String> n :old_news){
					String ip = n.get(0);
					int port = Integer.valueOf(n.get(1));
					
					if (!old_proms.contains(n)){
						//if peer is not already included in the 'old promoted' list
						promoted.add(ip);
					
						HAPEntry he = new HAPEntry();
						he.setIp_address(ip);
						he.setListenport(port);
						he.setHap_flag(true);
						List<HAPEntry> updt = iHapDao.get(he);
						//Update peer's status to promoted HAP (not "new").
						if (updt != null && updt.size() >0){
							updt.get(0).setHap_flag(false);
							iHapDao.update(updt.get(0));
						}
					} else {
						//if peer is already included in the 'old promoted' list
						if (!delta)
							promoted.add(ip);
						HAPEntry he = new HAPEntry();
						he.setIp_address(ip);
						he.setListenport(port);
						he.setHap_flag(true);
						List<HAPEntry> updt = iHapDao.get(he);
						//Remove peer from promoted HAPs.
						if (updt != null && updt.size() >0)
							iHapDao.remove(updt.get(0));
					}
				}
				
				// for peers already promoted
				for(List<String> n :old_proms){
					String ip = n.get(0);
					int port = Integer.valueOf(n.get(1));
					
					
					if (!old_news.contains(n)){
						//if peer is not promoted any more
						demoted.add(ip);
					
						HAPEntry he = new HAPEntry();
						he.setIp_address(ip);
						he.setListenport(port);
						he.setHap_flag(false);
						List<HAPEntry> updt = iHapDao.get(he);
						if (updt != null && updt.size() >0)
							iHapDao.remove(updt.get(0));
					} else {
						// Do nothing! Already done something in the above 'for' statement.
					}
				}
				
			}
		}
		
		List<List<String>> res = new ArrayList<List<String>>(); 
		res.add(promoted);
		res.add(demoted);
		
		return res;
	}
	
	/**
	 * A comparator for sorting HapRateEntry objects based on their rate
	 */
	protected static class HAPComparator implements Comparator<HapRateEntry> 
	{
		public int compare(HapRateEntry o1, HapRateEntry o2) {
			if (o2.getRate() > o1.getRate())
				return 1;
			else if (o2.getRate() < o1.getRate())
				return -1;
			else
				return 0;
		}
	}
	

}
