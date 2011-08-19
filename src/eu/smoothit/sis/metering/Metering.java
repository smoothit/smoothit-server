package eu.smoothit.sis.metering;

import java.util.Map;
import java.util.Observable;

import eu.smoothit.sis.metering.impl.MeteringImpl;

/**
 * The Metering interface. It supports the generic peer ranking interface approach.
 * Each metering parameter is identified by a string and its value is represented by a double.
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public abstract class Metering extends Observable {

	/*
	 * Parameter names used on the Metering interface. 
	 */
	public static final String PARAM_PEER_LOCALITY="PeerLocality";
	public static final String PARAM_ROUTE_PREFERENCE="RoutePreference";
	public static final String PARAM_PHYSICAL_PEER_PROXIMITY="PhysicalPeerProximity";
	public static final String PARAM_EXTERNAL_ROUTE_PREFERENCE="ExternalRoutePreference";

	public static final String COMPONENT_NAME = "Metering";
	public static final String METERING_PROP_REMOTE_IP_INFO_SRC = "Metering-Remote-IP-Info-Src";
	public static final String METERING_PROP_LOCAL_IP_INFO_SRC = "Metering-Local-IP-Info-Src";
	public static final String METERING_PROP_BGP_ROUTER_ADDRESS = "Metering-BGP-Router-Address";
	public static final String METERING_PROP_BGP_ROUTER_PORT = "Metering-BGP-Router-Port";
	public static final String METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY = "Metering-BGP-Router-SNMP-Community";
	public static final String METERING_PROP_BGP_FILE_NAME = "Metering-BGP-File-Name";
	public static final String METERING_PROP_REFRESH_RATE = "Metering-Refresh-Rate";

	public static final String METERING_VAL_IP_INFO_SRC_DB = "DB";
	public static final String METERING_VAL_IP_INFO_SRC_ROUTER = "Router";
	public static final String METERING_VAL_IP_INFO_SRC_FILE = "File";

	public static final long DEFAULT_UPDATE_INTERVALL = 3600; // in seconds
	public static final long MIN_UPDATE_INTERVALL = 1800; // in seconds
	public static final String DEFAULT_BGP_ROUTER_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_BGP_ROUTER_PORT = 161;
	public static final String DEFAULT_BGP_ROUTER_SNMP_COMMUNITY = "public";
	public static final String DEFAULT_BGP_FILE_NAME = "bgp.conf";

	private static Metering instance;

	protected Metering() {}

    /**
     * Returns the unique instance of Metering.
     * 
     * @return the unique instance.
     */
	public static synchronized Metering getInstance() {
		if(instance == null)
			instance = MeteringImpl.getInstance();
		return instance;
	}
	
	/**
	 * Reloads the metering configuration.
	 */
	public abstract void reloadConfig();

	/**
   	 * Refreshes the metering information. Reloads the BGP routing table into memory. 
   	 * Whenever this method is called, the routing table is reloaded.
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
	public abstract boolean refresh();

	/**
	 * Returns the metering parameters assigned to an IP address. 
	 * 
	 * @param IPAddress The IP address for which the parameters are requested.
	 * @return The parameters stored in a Map<String, Double> or null if no matching entry was found.
	 */
	public abstract Map<String, Double> getAddressParams(String IPAddress);

	/**
	 * Returns the maximum values of the metering parameters.
	 *  
	 * @return The maximum values stored in a Map<String, Double>.
	 */
	public abstract Map<String, Double> getMaxParams();
	
   	/**
   	 * Prints the status of the Metering component to the logger.
   	 */
	public abstract void print();
	
}
