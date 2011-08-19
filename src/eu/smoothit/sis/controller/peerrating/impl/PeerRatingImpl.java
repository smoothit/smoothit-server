package eu.smoothit.sis.controller.peerrating.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.peerrating.PeerRating;
import eu.smoothit.sis.controller.peerrating.RequestEntry;
import eu.smoothit.sis.controller.peerrating.ResponseEntry;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IHAPEntryDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.HAPEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;
import eu.smoothit.sis.metering.Metering;


/**
 * Generic Peer Rating Service implemented as an EJB3 session bean
 * 
 * @author Michael Makidis, Sergios Soursos, Intracom Telecom
 * @version 1.2
 *                  
 */
@Stateless
public class PeerRatingImpl implements PeerRating {
	
	public static final String COMPONENT_NAME = "Controler_Rate";
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(PeerRatingImpl.class.getName());
	
	/**
	 * A comparator for sorting ResponseEntries based on their SIS preference           
	 */
	protected static class ResponseEntryPreferenceComparator
		implements Comparator<ResponseEntry>
	{
		public int compare(ResponseEntry o1, ResponseEntry o2) {
			return (o2.getPreference() - o1.getPreference());
		}
	}
	
	/**
	 * Prepares a sorted list of rated ResponseEntries with the generic peer
	 * rating algorithm (see D2.3).
	 * 
	 *  @param addresses The list of IP addresses to be sorted.
	 *  
	 *  @return The sorted and rated list of peer IP addresses.
	 *         
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ResponseEntry> rankAddresses(List<RequestEntry> addresses)
	{
		if(addresses == null) throw new IllegalArgumentException("Addresses cannot be null");
		
		List<ResponseEntry> res = new ArrayList<ResponseEntry>();
		for(RequestEntry reqEntry : addresses)
		{
			String address = reqEntry.getIpAddress();
			if(address == null)
				throw new IllegalArgumentException("Address cannot be null");
			log.debug("Rating address " + address);
			
			Map<String, Double> params = 
				Metering.getInstance().getAddressParams(address);
			
			Map<String, Double> weightedParams = new HashMap<String, Double>();
			ResponseEntry respEntry = new ResponseEntry();
			respEntry.setIpAddress(address);
			int rate = 0;

			if(params != null) { 	// When Metering has a BGP entry for the respective IP (range)
				
				double peerLocality = 0;
				for(Map.Entry<String, Double> param: params.entrySet())
				{
					if (param.getKey().equalsIgnoreCase(Metering.PARAM_PEER_LOCALITY))
						peerLocality = param.getValue();
					
					int weight = getWeight(param.getKey(), address);
					Double value = param.getValue();
					weightedParams.put(param.getKey(), weight*value);
					log.debug("Address: " + address + " param: " + param + " value: " + value + " weight: " + weight);
				}
				rate = calculateRateWithSum(address, weightedParams,peerLocality); // this is just one type of function (SUM)
			}
			respEntry.setPreference(rate);
			log.info("Address: " + address + " rate: " + rate);
			res.add(respEntry);
		}
		Collections.sort(res, new ResponseEntryPreferenceComparator());
		return res;
	}
	
	/**
	 * Check if a given IP is promoted to HAP or not.
	 * 
	 *  @param ipAddresses The IP address to be checked.
	 *  
	 *  @return 0 if the IP is not a HAP, 1 if the IP is a HAP.
	 *         
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private int defineHap(String ipAddress){
		
		int res = 0;
		
		IHAPEntryDAO iHapDao = SisDAOFactory.getFactory().createHAPEntryDAO();
		HAPEntry he = new HAPEntry();
		he.setIp_address(ipAddress);
		he.setHap_flag(false); // false = already promoted
		List<HAPEntry> lhe = iHapDao.get(he);
		
		if (lhe != null && lhe.size() > 0)
			res = 1;
		
		return res;		
	}
	
	/**
	 * Calculates the rate of an IP address based on a set of parameters with 
	 * weighted values. This method implements the simple Sum function to 
	 * calculate the rate.
	 * 
	 *  @param address The IP address of the peer to be ranked
	 *  @param weightedParams A map of parameters and weighted values
	 *  @param peerLocality The value of the peerLocality param
	 *  
	 *  @return The rate of the IP address
	 *         
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private int calculateRateWithSum(String address, Map<String, Double> weightedParams, double peerLocality)
	{
		double res = 0;
		boolean hap_on = false;
		
		IComponentConfigDAO dao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry config = dao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_HAP, SisWebInitializer.PARAM_HAP_CONTROLLER_ON_OFF);
		
		if(config != null && config.getValue() != null && config.getValue() instanceof String)
			hap_on = Boolean.valueOf((String) config.getValue()).booleanValue();		
		
		res = weightedParams.get(Metering.PARAM_PEER_LOCALITY);
		if (res > 0 && hap_on) {
			res = res + 1 * defineHap(address);	// increase rate for local peers by one when they are HAPs
		}
		
		for (Map.Entry<String, Double> e : weightedParams.entrySet())
		{
			if (e.getKey().equalsIgnoreCase(Metering.PARAM_PEER_LOCALITY))
				continue;
			
			res += (1 - peerLocality) * e.getValue();
		}
		
		return (int)res;
	}
	
	/**
	 * Returns the weight for an IP address' parameter
	 * 
	 *  @param param The name of the parameter
	 *  @param address The peer IP address
	 *  
	 *  @return The weight for that address' parameter.
	 *         
	 */
	private int getWeight(String param, String address)
	{
		int result = 1;

		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		ComponentConfigEntry config = dao.findByComponentAndName(COMPONENT_NAME, param + "_weight");

		// check the DB if the weight is in there
		if(config != null && config.getValue() != null && config.getValue() instanceof String) {
			log.debug("Weight from DB param: " + param + " weight: " + config.getValue());
			result = Integer.valueOf((String) config.getValue()).intValue() ;
		} else { // compute the parameter's weight
			log.debug("Computing weight for param: " + param);
			result = calculateBgpParameterWeight(param, address);
		}
		return result;
	}

	/**
	 * Returns the weight for an IP address' BGP parameter by using the BGP
	 * algorithm (see D2.3).
	 * 
	 *  @param param The name of the parameter
	 *  @param address The peer IP address
	 *  
	 *  @return The BGP weight for that address' parameter.
	 *         
	 */
	private int calculateBgpParameterWeight(String param, String address) {
		int result = 1;
		
		Map<String, Double> maxParams = Metering.getInstance().getMaxParams();
		
		if(param.equalsIgnoreCase(Metering.PARAM_PEER_LOCALITY))
		{
			result = (int)( (maxParams.get(Metering.PARAM_ROUTE_PREFERENCE)+1) *
					(maxParams.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY)+1) *
					(maxParams.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE)+1) );
		}
		else if(param.equalsIgnoreCase(Metering.PARAM_ROUTE_PREFERENCE))
		{
			result = (int) ( maxParams.get(Metering.PARAM_ROUTE_PREFERENCE) *
					(maxParams.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY)+1) *
					(maxParams.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE)+1)
					);
		}
		else if(param.equalsIgnoreCase(Metering.PARAM_PHYSICAL_PEER_PROXIMITY))
		{
			result = (int) ( maxParams.get(Metering.PARAM_PHYSICAL_PEER_PROXIMITY) *
					(maxParams.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE)+1)
					);
		}
		else if(param.equalsIgnoreCase(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE))
		{
			result = (int)( 1 * maxParams.get(Metering.PARAM_EXTERNAL_ROUTE_PREFERENCE) );
		}
		return result;
	}

}
