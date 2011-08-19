package eu.smoothit.sis.metering.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.metering.Metering;
import eu.smoothit.sis.metering.bgp.BGPRoutingTableEntry;

/**
 * The Metering component supports the generic peer ranking interface. It calculates the
 * parameters assigned to an IP address based on the BGP routing table. The routing table
 * can be read from a router, from a file, or from the SIS DB. It implements the Metering 
 * interface.  
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public class MeteringImpl extends Metering {
	
	private static Logger logger = Logger.getLogger(MeteringImpl.class.getName());
	private BGPRoutingTableReader routingTableReader = new BGPRoutingTableReader();
	private static Metering instance = new MeteringImpl();
	
	private Vector<Map<Integer, ParamTableEntry>> paramTable = new Vector<Map<Integer, ParamTableEntry>>();
	private int numberOfEntries = 0;
	private Map<String, Double> maxParams = Collections.synchronizedMap(new HashMap<String, Double>());
	
	private boolean isLoading = false;

    /**
     * Default constructor.
     */
    private MeteringImpl() {
    	for(int i=0; i<=32; i++)
    		paramTable.add(Collections.synchronizedMap(new HashMap<Integer, ParamTableEntry>()));
		//reloadConfig();
    	//refresh();
    }
    
    /**
     * Returns the unique instance of MeteringImpl.
     * 
     * @return the unique instance.
     */
    public static Metering getInstance() {
    	return instance;
    }
	
	/**
	 * Reloads the metering configuration.
	 */
	public synchronized void reloadConfig() {
		// Read metering configuration data from DB
		logger.info("Reloading metering configuration");
		
		IComponentConfigDAO dao = SisDAOFactory.getFactory().createComponentConfigDAO();
		ComponentConfigEntry config = new ComponentConfigEntry();
		Serializable returnValue;
		String configValueStr;
		int configValueInt;
		List<ComponentConfigEntry> configEntries = null;
		config.setComponent(COMPONENT_NAME);
		
		// Get the source of remote IP information from the DB
		config.setPropName(METERING_PROP_REMOTE_IP_INFO_SRC);
    	configEntries = dao.get(config);    	
    	if(configEntries == null || configEntries.size() == 0) {
    		logger.warn(METERING_PROP_REMOTE_IP_INFO_SRC + " not found in SIS DB. Using default value: " + METERING_VAL_IP_INFO_SRC_DB);
    		routingTableReader.useDB();
    	} else {
    		returnValue = configEntries.get(0).getValue();
    		if(returnValue instanceof String) {
    			configValueStr = (String) configEntries.get(0).getValue();
    			if(configValueStr.equals(METERING_VAL_IP_INFO_SRC_ROUTER)) {
    				logger.info(METERING_PROP_REMOTE_IP_INFO_SRC + " = " + METERING_VAL_IP_INFO_SRC_ROUTER);
    				routingTableReader.useRouter();
    			
    				// Get BGP router address from the DB
    				config.setPropName(METERING_PROP_BGP_ROUTER_ADDRESS);
    				configEntries = dao.get(config);
    				if(configEntries == null || configEntries.size() == 0) {
    					logger.warn(METERING_PROP_BGP_ROUTER_ADDRESS + " not found in SIS DB. Using default value: " + DEFAULT_BGP_ROUTER_ADDRESS);
    					routingTableReader.setRouterAddress(DEFAULT_BGP_ROUTER_ADDRESS);
    				} else {
    					returnValue = configEntries.get(0).getValue();
    					if(returnValue instanceof String) {
    						configValueStr = (String) configEntries.get(0).getValue();
    						routingTableReader.setRouterAddress(configValueStr);
    						logger.info(METERING_PROP_BGP_ROUTER_ADDRESS + " = " + configValueStr);
    					} else {
    						logger.warn(METERING_PROP_BGP_ROUTER_ADDRESS + " is not a String. Using default value: " + DEFAULT_BGP_ROUTER_ADDRESS);
    						routingTableReader.setRouterAddress(DEFAULT_BGP_ROUTER_ADDRESS);
    					}	
    				}
    	    	
    				// Get BGP router port from the DB
    				config.setPropName(METERING_PROP_BGP_ROUTER_PORT);
    				configEntries = dao.get(config);
    				if(configEntries == null || configEntries.size() == 0) {
    					logger.warn(METERING_PROP_BGP_ROUTER_PORT + " not found in SIS DB. Using default value: " + DEFAULT_BGP_ROUTER_PORT);
    					routingTableReader.setRouterPort(DEFAULT_BGP_ROUTER_PORT);
    				} else {
    					returnValue = configEntries.get(0).getValue();
    					if(returnValue instanceof Integer) {
    						configValueInt = (Integer) configEntries.get(0).getValue();
    						routingTableReader.setRouterPort(configValueInt);
    						logger.info(METERING_PROP_BGP_ROUTER_PORT + " = " + configValueInt);
    					} else if(returnValue instanceof String) {
    						logger.warn(METERING_PROP_BGP_ROUTER_PORT + " is a String");
    						String tmp = (String)returnValue;
    						try {
    							configValueInt = Integer.valueOf(tmp);
        						routingTableReader.setRouterPort(configValueInt);
        						logger.info(METERING_PROP_BGP_ROUTER_PORT + " = " + configValueInt);    							
    						} catch(Exception ex) {
    				        	logger.warn("Invalid port number: " + ex.getMessage() + ". Using default value: " + DEFAULT_BGP_ROUTER_PORT);
    				        	routingTableReader.setRouterPort(DEFAULT_BGP_ROUTER_PORT);
    						}
    					} else {
    						logger.warn(METERING_PROP_BGP_ROUTER_PORT + " is not an Integer. Using default value: " + DEFAULT_BGP_ROUTER_PORT);
    						routingTableReader.setRouterPort(DEFAULT_BGP_ROUTER_PORT);
    					}
   					}
    			
   					// Get SNMP community for the BGP router from the DB
   					config.setPropName(METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY);
   					configEntries = dao.get(config);
   					if(configEntries == null || configEntries.size() == 0) {
   						logger.warn(METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY + " not found in SIS DB. Using default value: " + DEFAULT_BGP_ROUTER_SNMP_COMMUNITY);
   						routingTableReader.setSNMPCommunity(DEFAULT_BGP_ROUTER_SNMP_COMMUNITY);
   					} else {
   						returnValue = configEntries.get(0).getValue();
   						if(returnValue instanceof String) {
   							configValueStr = (String) configEntries.get(0).getValue();
   							routingTableReader.setSNMPCommunity(configValueStr);
   							logger.info(METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY + " = " + configValueStr);   							
    					} else {
    						logger.warn(METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY + " is not a String. Using default value: " + DEFAULT_BGP_ROUTER_SNMP_COMMUNITY);
    						routingTableReader.setSNMPCommunity(DEFAULT_BGP_ROUTER_SNMP_COMMUNITY);
    					}
					}
				} else if(configValueStr.equals(METERING_VAL_IP_INFO_SRC_DB)) {
					logger.info(METERING_PROP_REMOTE_IP_INFO_SRC + " = " + METERING_VAL_IP_INFO_SRC_DB);
					routingTableReader.useDB();
				} else if(configValueStr.equals(METERING_VAL_IP_INFO_SRC_FILE)) {
					logger.info(METERING_PROP_REMOTE_IP_INFO_SRC + " = " + METERING_VAL_IP_INFO_SRC_FILE);
					routingTableReader.useFile();
    			
					// Get file name from the DB
					config.setPropName(METERING_PROP_BGP_FILE_NAME);
					configEntries = dao.get(config);
					if(configEntries == null || configEntries.size() == 0) {
						logger.warn(METERING_PROP_BGP_FILE_NAME + " not found in SIS DB. Using default value: " + DEFAULT_BGP_FILE_NAME);
						routingTableReader.setFileName(DEFAULT_BGP_FILE_NAME);
					} else {
						returnValue = configEntries.get(0).getValue();
						if(returnValue instanceof String) {
							configValueStr = (String) configEntries.get(0).getValue();
							routingTableReader.setFileName(configValueStr);
							logger.info(METERING_PROP_BGP_FILE_NAME + " = " + configValueStr);
    					} else {
    						logger.warn(METERING_PROP_BGP_FILE_NAME + " is not a String. Using default value: " + DEFAULT_BGP_FILE_NAME);
    						routingTableReader.setFileName(DEFAULT_BGP_FILE_NAME);
    					}							
					}    			
				} else {
					logger.warn("Unsupported value (" + configValueStr + ") for " + METERING_PROP_REMOTE_IP_INFO_SRC + ". Using default value: " + METERING_VAL_IP_INFO_SRC_DB);
					routingTableReader.useDB();    			
				}
    		} else {
    			logger.warn(METERING_PROP_REMOTE_IP_INFO_SRC + " is not a String. Using default value: " + METERING_VAL_IP_INFO_SRC_DB);
        		routingTableReader.useDB();
    		}
    	}
    	
    	// Get the source of local IP information from the DB
		config.setPropName(METERING_PROP_LOCAL_IP_INFO_SRC);
    	configEntries = dao.get(config);    	
    	if(configEntries == null || configEntries.size() == 0) {
    		logger.warn(METERING_PROP_LOCAL_IP_INFO_SRC + " not found in SIS DB. Using default value: " + METERING_VAL_IP_INFO_SRC_DB);
    		
    	} else {
    		returnValue = configEntries.get(0).getValue();
			if(returnValue instanceof String) {
				configValueStr = (String) configEntries.get(0).getValue();
				if(configValueStr.equals(METERING_VAL_IP_INFO_SRC_DB)) {
					logger.info(METERING_PROP_LOCAL_IP_INFO_SRC + " = " + METERING_VAL_IP_INFO_SRC_DB);
				} else {
					logger.warn("Unsupported value (" + configValueStr + ") for " + METERING_PROP_LOCAL_IP_INFO_SRC + ". Using default value: " + METERING_VAL_IP_INFO_SRC_DB);
				}
    		} else {
				logger.warn(METERING_PROP_LOCAL_IP_INFO_SRC + " is not a String. Using default value: " + METERING_VAL_IP_INFO_SRC_DB);
			}		
    	}
    	/*
		// Get the refresh rate from the DB
		config.setPropName(METERING_PROP_REFRESH_RATE);
    	configEntries = dao.get(config);    	
    	if(configEntries == null || configEntries.size() == 0) {
    		logger.warn(METERING_PROP_REFRESH_RATE + " not found in SIS DB. Using default values.");
    		updateIntervall = DEFAULT_UPDATE_INTERVALL;
    	} else {
    		returnValue = configEntries.get(0).getValue();    		
    		if (returnValue instanceof Integer) {
    			updateIntervall = (Integer)returnValue;
        		logger.info(METERING_PROP_REFRESH_RATE + " = " + updateIntervall + "s");
			} else if(returnValue instanceof String) {
				logger.warn(METERING_PROP_REFRESH_RATE + " is a String");
				String tmp = (String)returnValue;
				try {
					configValueInt = Integer.valueOf(tmp);
					updateIntervall = configValueInt;
					logger.info(METERING_PROP_REFRESH_RATE + " = " + updateIntervall + "s");    							
				} catch(Exception ex) {
		        	logger.error("Invalid refresh rate: " + ex.getMessage());
				}
    		} else {
    			logger.error(METERING_PROP_REFRESH_RATE + " is not an Integer");
    		}
    		
    		if(updateIntervall <= 10) {
    			updateIntervall = DEFAULT_UPDATE_INTERVALL;
    			logger.warn(METERING_PROP_REFRESH_RATE + " is smaller than 10s. Setting the refresh rate to " + updateIntervall + "s.");
    		}
    	}
    	*/
	}

	/**
   	 * Refreshes the metering information. Reloads the BGP routing table into memory. 
   	 * Whenever this method is called, the routing table is reloaded.
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
	public boolean refresh() {

		synchronized(this) {
			if(isLoading) {
				logger.debug("Metering is already refreshing");
				return false;
			}			
			isLoading = true;
		}			
		logger.info("Refreshing metering state");

		boolean retValue = false;
		int prefix;
		
		// Read remote IP ranges via the routingTableReader 
		retValue = routingTableReader.readRoutingTable();
		logger.debug("Reading BGP routing data finished");

		// Reset the metering status
		numberOfEntries = 0;
    	for(int i=0; i<=32; i++) paramTable.get(i).clear();		
		maxParams.clear();
		
		// Read local IP ranges from SIS DB
		IPRangeDAO dao = SisDAOFactory.getFactory().createIPRangeDAO();
		IPRangeConfigEntry iprange = new IPRangeConfigEntry();
		iprange.setLocal(true);
		List<IPRangeConfigEntry> localIPs = dao.get(iprange);
		for(IPRangeConfigEntry i : localIPs) {
			ParamTableEntry paramEntry = new ParamTableEntry();
			paramEntry.setIPAddress(i.getPrefix());
			paramEntry.setPrefixLength(i.getPrefix_len());
			paramEntry.setParam(PARAM_PEER_LOCALITY, 1.0);
			paramEntry.setParam(PARAM_ROUTE_PREFERENCE, 1.0);
			paramEntry.setParam(PARAM_PHYSICAL_PEER_PROXIMITY, 1.0);
			paramEntry.setParam(PARAM_EXTERNAL_ROUTE_PREFERENCE, 1.0);
			
			prefix = convertIPAddressToInt(paramEntry.getIPAddress());
    		prefix = prefix & (0xFFFFFFFF << (32 - paramEntry.getPrefixLength()));
			paramTable.get(paramEntry.getPrefixLength()).put(prefix, paramEntry);
			numberOfEntries++;
		}
		
		// Process remote IP ranges if the reading from the router was successful 
		if(retValue) {
			logger.debug("Processing BGP data...");
			
			maxParams.put(PARAM_ROUTE_PREFERENCE, new Double(routingTableReader.getMaxLocalPreference()));
			maxParams.put(PARAM_PHYSICAL_PEER_PROXIMITY, new Double(routingTableReader.getMaxASPathLength()));
			maxParams.put(PARAM_EXTERNAL_ROUTE_PREFERENCE, new Double(routingTableReader.getMaxMED()));
			
			for(BGPRoutingTableEntry routingEntry=routingTableReader.retrieveNext(); routingEntry!=null; routingEntry=routingTableReader.retrieveNext()) {
				if(routingEntry.getAttrBest() == 2 && !routingEntry.getAttrIpAddrPrefix().equals("0.0.0.0") && routingEntry.getAttrIpAddrPrefixLen() > 0 && routingEntry.getASPathLength() != -1) {
					// If the routing entry represents a best path selected by BGP, then calculate parameters and store them
					ParamTableEntry paramEntry = new ParamTableEntry();
					paramEntry.setIPAddress(routingEntry.getAttrIpAddrPrefix());
					paramEntry.setPrefixLength(routingEntry.getAttrIpAddrPrefixLen());
					paramEntry.setParam(PARAM_PEER_LOCALITY, 0.0);
					
					if(routingEntry.getAttrLocalPref() < 0 || maxParams.get(PARAM_ROUTE_PREFERENCE) == 0) {
						// If local preference is not set or max local preference is zero
						paramEntry.setParam(PARAM_ROUTE_PREFERENCE, 0.0);
					} else {
						paramEntry.setParam(PARAM_ROUTE_PREFERENCE, routingEntry.getAttrLocalPref() / maxParams.get(PARAM_ROUTE_PREFERENCE));
					}
					
					if(maxParams.get(PARAM_PHYSICAL_PEER_PROXIMITY) == 0) {
						paramEntry.setParam(PARAM_PHYSICAL_PEER_PROXIMITY, 0.0);
					} else {
						paramEntry.setParam(PARAM_PHYSICAL_PEER_PROXIMITY, 1.0 - routingEntry.getASPathLength() / maxParams.get(PARAM_PHYSICAL_PEER_PROXIMITY));
					}
					
					if(routingEntry.getAttrMultiExitDisc() < 0 || maxParams.get(PARAM_EXTERNAL_ROUTE_PREFERENCE) == 0) {
						// If MED is not set or max MED is zero
						paramEntry.setParam(PARAM_EXTERNAL_ROUTE_PREFERENCE, 0.0);
					} else {
						paramEntry.setParam(PARAM_EXTERNAL_ROUTE_PREFERENCE, 1.0 - routingEntry.getAttrMultiExitDisc() / maxParams.get(PARAM_EXTERNAL_ROUTE_PREFERENCE));
					}
					
					// Insert the new paramEntry into the paramTable
					prefix = convertIPAddressToInt(paramEntry.getIPAddress());
		    		prefix = prefix & (0xFFFFFFFF << (32 - paramEntry.getPrefixLength()));
					paramTable.get(paramEntry.getPrefixLength()).put(prefix, paramEntry);

			        numberOfEntries++;
				}
			}
		} 

		// notify observers
		setChanged();
		notifyObservers();

		synchronized(this) {
			isLoading = false;
		}
		logger.debug("Refreshing metering state finished");
		return retValue;
	}

	/**
	 * Returns the metering parameters assigned to an IP address. 
	 * 
	 * @param IPAddress The IP address for which the parameters are requested.
	 * @return The parameters stored in a Map<String, Double> or null if no matching entry was found.
	 */
	public Map<String, Double> getAddressParams(String IPAddress) {
		logger.debug("Reading metering parameters for " + IPAddress);
	    int prefix;
	    int addr = convertIPAddressToInt(IPAddress);
	    ParamTableEntry entry;

	    for(int j=32; j>=0; j--) {
	    	prefix = addr & (0xFFFFFFFF << (32 - j));
	    	entry = paramTable.get(j).get(prefix);
	    	if(entry != null) {
    			// There is a matching
    			logger.debug("Matching entry for " + IPAddress + ": " + entry.getIPAddress() + "/" + entry.getPrefixLength());
    	    	logger.debug("Metering parameters for IP address " + IPAddress + ": " + entry.getParams().toString());
    	    	return(entry.getParams());
	    	}
	    }

		// No matching entry found
    	logger.warn("No matching entry found for IP address: " + IPAddress);
    	return(null);
	}

	/**
	 * Returns the maximum values of the metering parameters.
	 *  
	 * @return The maximum values stored in a Map<String, Double>.
	 */
	public Map<String, Double> getMaxParams() {
		logger.debug("Reading maximum parameters: " + maxParams.toString());
		return maxParams;
	}
	
   	/**
   	 * Prints the status of the Metering component to the logger.
   	 */
	public synchronized void print() {
		logger.debug("Metering information:");

		if(isLoading) {
			logger.debug("Metering is currently refreshing");
			return;
		}
		
		logger.debug("Maximum parameters: " + maxParams.toString());
		logger.debug("Number of entries: " + numberOfEntries);

		int n = 10;
		if(numberOfEntries > n) {
			logger.debug("First " + n + " entries:");
		} else {
			n = numberOfEntries;
		}

		int i=0;
		int j = 32;
		while(i < n) {
			for(ParamTableEntry p : paramTable.get(j).values()) {
				if(i >= n) break; 
				logger.debug((i+1) + ". Entry: " + p.getIPAddress() + "/" + p.getPrefixLength() + ", " + p.getParams().toString());
				i++;
			}
			j--;
		}
	}

    /**
     * Converts an IP address from String representation to Integer.
     * 
     * @return	IP address as an Integer.
     */
    private int convertIPAddressToInt(String addrStr) {
		StringTokenizer st = new StringTokenizer(addrStr, ".");
		int addr = 0; 
		addr = Integer.parseInt(st.nextToken()) << 24;
		addr = addr | (Integer.parseInt(st.nextToken()) << 16);
		addr = addr | (Integer.parseInt(st.nextToken()) << 8);
		addr = addr | Integer.parseInt(st.nextToken());
		return addr;
    }

}
