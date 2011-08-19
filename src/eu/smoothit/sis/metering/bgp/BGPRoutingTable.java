package eu.smoothit.sis.metering.bgp;

import java.util.Collection;

/**
 * The interface for the BGP routing table. 
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public interface BGPRoutingTable {

	/**
	 * Sets the IP address of the router where the BGP routing table is read from. 
	 */
	public void setRouterAddress(String a);

	/**
	 * Sets the port number of the router where the BGP routing table is read from. 
	 */
	public void setRouterPort(int p);

	/**
	 * Sets the SNMP community string for reading the BGP routing table from a router. 
	 */
	public void setSNMPCommunity(String c);

	/**
	 * Sets the name of the file where the BGP routing table is read from. 
	 */
	public void setFileName(String f);

	/**
	 * Specifies to use a router to read the BGP routing table from. 
	 */
	public void useRouter();

	/**
	 * Specifies to use a file to read the BGP routing table from. 
	 */
	public void useFile();

	/**
	 * Specifies to use the database to read the BGP routing table from. 
	 */
	public void useDB();

	/**
   	 * Loads the BGP routing table into memory. Whenever this method is called, the 
   	 * routing table is reloaded. 
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
	public boolean readRoutingTable();

   	/**
   	 * Returns the complete BGP routing table.
   	 * 
   	 * @return	a collection of routing table entries.
   	 */
	public Collection<BGPRoutingTableEntry> getAllEntries();

   	/**
   	 * Returns the number of routing entries in the BGP routing table.
   	 *  
   	 * @return	number of routing entries.
   	 */
	public int getNumberOfEntries();

	/**
   	 * Returns the routing entry that represents the route to the destination IP 
   	 * address specified in the argument.
   	 * 
   	 * @return	the routing entry.
   	 */
	public BGPRoutingTableEntry getEntryForPrefix(String ip);

    /**
     * Returns and removes a routing entry from the routing table.
     *  
     * @return The routing entry or null if the routing table is empty.
     */
    public BGPRoutingTableEntry retrieveNext();

    /**
     * Returns the maximum value of the local preference attribute.
     * 
     * @return The maximum value.
     */
    public int getMaxLocalPreference();
    
    /**
     * Returns the maximum value of the AS path length.
     * 
     * @return The maximum value.
     */
    public int getMaxASPathLength();
    
    /**
     * Returns the maximum value of the MED attribute.
     * 
     * @return The maximum value.
     */
    public int getMaxMED();

    /**
   	 * Prints the complete BGP routing table to the logger.
   	 */
	public void print();

}
