package eu.smoothit.sis.metering.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the metering parameters assigned to an IP address.
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public class ParamTableEntry implements Comparable<ParamTableEntry> {
	private String IPAddress;
	private int prefixLength;
	private Map<String, Double> params = new HashMap<String, Double>();

	/**
	 * Comparison based on the prefix length.
	 *    
	 * @param i The ParamTableEntry used in the comparison. 
	 * @return The result of the comparison.
	 */
	public int compareTo(ParamTableEntry i) {
		if(prefixLength < i.getPrefixLength()) return(1);
		if(prefixLength > i.getPrefixLength()) return(-1);
		return 0;
	}

	/**
	 * Sets the IP address for which the metering parameters are stored.
	 * 
	 * @param IPAddress The IP address to be set.
	 */
	public void setIPAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}
	
	/**
	 * Returns the IP address for which the metering parameters are stored.
	 * 
	 * @return The IP address.
	 */
	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * Sets the prefix length of the IP address.
	 * 
	 * @param prefixLength The prefix length to be set.
	 */
	public void setPrefixLength(int prefixLength) {
		this.prefixLength = prefixLength;
	}
	
	/**
	 * Returns the prefix length of the IP address.
	 * 
	 * @return The prefix length.
	 */
	public int getPrefixLength() {
		return prefixLength;
	}
	
	/**
	 * Sets a metering parameter for the IP address.
	 * 
	 * @param key The identifier of the metering parameter.
	 * @param value The value of the metering parameter.
	 */
	public void setParam(String key, Double value) {
		params.put(key, value);
	}
	
	/**
	 * Returns the value of the specified metering parameter.
	 * 
	 * @param key The identifier of the metering parameter.
	 * @return The value of the metering parameter.
	 */
	public Double getParam(String key) {
		return params.get(key);
	}

	/**
	 * Returns all metering parameters.
	 * 
	 * @return Metering parameters.
	 */
	public Map<String, Double> getParams() {
		return params;
	}	
}
