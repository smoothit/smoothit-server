package eu.smoothit.sis.metering.impl;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import eu.smoothit.sis.metering.bgp.BGPRoutingTableEntry;

/**
 * Represents a routing entry in a BGP routing table. It includes all BGP 
 * attributes of a route. 
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public class BGPRoutingTableEntryImpl implements BGPRoutingTableEntry{
	/*
	 * -bgp4PathAttrTable(6)
	   |  |
	   |  +--bgp4PathAttrEntry(1)
	   |     |  Index: bgp4PathAttrIpAddrPrefix, bgp4PathAttrIpAddrPrefixLen, bgp4PathAttrPeer
	   |     |
	   |     +-- -R-- IpAddr    bgp4PathAttrPeer(1)
	   |     +-- -R-- INTEGER   bgp4PathAttrIpAddrPrefixLen(2)
	   |     |        Range: 0..32
	   |     +-- -R-- IpAddr    bgp4PathAttrIpAddrPrefix(3)
	   |     +-- -R-- EnumVal   bgp4PathAttrOrigin(4)
	   |     |        Values: igp(1), egp(2), incomplete(3)
	   |     +-- -R-- String    bgp4PathAttrASPathSegment(5)
	   |     |        Size: 2..255
	   |     +-- -R-- IpAddr    bgp4PathAttrNextHop(6)
	   |     +-- -R-- INTEGER   bgp4PathAttrMultiExitDisc(7)
	   |     |        Range: -1..2147483647
	   |     +-- -R-- INTEGER   bgp4PathAttrLocalPref(8)
	   |     |        Range: -1..2147483647
	   |     +-- -R-- EnumVal   bgp4PathAttrAtomicAggregate(9)
	   |     |        Values: lessSpecificRrouteNotSelected(1), lessSpecificRouteSelected(2)
	   |     +-- -R-- INTEGER   bgp4PathAttrAggregatorAS(10)
	   |     |        Range: 0..65535
	   |     +-- -R-- IpAddr    bgp4PathAttrAggregatorAddr(11)
	   |     +-- -R-- INTEGER   bgp4PathAttrCalcLocalPref(12)
	   |     |        Range: -1..2147483647
	   |     +-- -R-- EnumVal   bgp4PathAttrBest(13)
	   |     |        Values: false(1), true(2)
	   |     +-- -R-- String    bgp4PathAttrUnknown(14)
	   |              Size: 0..255
	   |
	 * 
	 */

	private static Logger logger = Logger.getLogger(BGPRoutingTableEntryImpl.class.getName());

    private String attrPeer = "0.0.0.0";			//1.3.6.1.2.1.15.6.1.1
    private int attrIpAddrPrefixLen = -1;			//1.3.6.1.2.1.15.6.1.2
    private String attrIpAddrPrefix = "0.0.0.0";	//1.3.6.1.2.1.15.6.1.3
    private int attrOrigin = 1;						//1.3.6.1.2.1.15.6.1.4
    private String attrASPathSegment = "";			//1.3.6.1.2.1.15.6.1.5
    private String attrNextHop = "0.0.0.0";			//1.3.6.1.2.1.15.6.1.6
    private int attrMultiExitDisc = -1;     		//1.3.6.1.2.1.15.6.1.7     
    private int attrLocalPref = -1;					//1.3.6.1.2.1.15.6.1.8
    private int attrAtomicAggregate = 1;    		//1.3.6.1.2.1.15.6.1.9
    private int attrAggregatorAS = 0;       		//1.3.6.1.2.1.15.6.1.10
    private String attrAggregatorAddr = "0.0.0.0";  //1.3.6.1.2.1.15.6.1.11
    private int attrCalcLocalPref = -1;				//1.3.6.1.2.1.15.6.1.12
    private int attrBest = 2;						//1.3.6.1.2.1.15.6.1.13
    private int asPathLength = -1;

    /**
     * Default constructor.
     */
    public BGPRoutingTableEntryImpl() {}
    
    /**
     * Prints all attributes of this routing entry to the logger.
     */
    public void print() {
    	logger.trace("bgp4PathAttrPeer(1): " + attrPeer);
    	logger.trace("bgp4PathAttrIpAddrPrefixLen(2): " + attrIpAddrPrefixLen);
    	logger.trace("bgp4PathAttrIpAddrPrefix(3): " + attrIpAddrPrefix);
    	logger.trace("bgp4PathAttrOrigin(4): " + attrOrigin);
    	logger.trace("bgp4PathAttrASPathSegment(5): " + attrASPathSegment);
    	logger.trace("bgp4PathAttrNextHop(6):  " + attrNextHop);
    	logger.trace("bgp4PathAttrMultiExitDisc(7): " + attrMultiExitDisc);
    	logger.trace("bgp4PathAttrLocalPref(8): " + attrLocalPref);
    	logger.trace("bgp4PathAttrAtomicAggregate(9): " + attrAtomicAggregate);
    	logger.trace("bgp4PathAttrAggregatorAS(10): " + attrAggregatorAS);
    	logger.trace("bgp4PathAttrAggregatorAddr(11): " + attrAggregatorAddr);
    	logger.trace("bgp4PathAttrCalcLocalPref(12) :" + attrCalcLocalPref);
    	logger.trace("bgp4PathAttrBest(13) :" + attrBest);
    }
    
    /**
     * Returns the bgp4PathAttrPeer attribute of this entry.
     */
    public String getAttrPeer() {
        return attrPeer;
    }

    /**
     * Sets the bgp4PathAttrPeer attribute of this entry.
     */
    public void setAttrPeer(String attrPeer) {
        this.attrPeer = attrPeer;
    }

    /**
     * Returns the bgp4PathAttrIpAddrPrefixLen attribute of this entry.
     */
    public int getAttrIpAddrPrefixLen() {
        return attrIpAddrPrefixLen;
    }

    /**
     * Sets the bgp4PathAttrIpAddrPrefixLen attribute of this entry.
     */
    public void setAttrIpAddrPrefixLen(int attrIpAddrPrefixLen) {
        this.attrIpAddrPrefixLen = attrIpAddrPrefixLen;
    }

    /**
     * Returns the bgp4PathAttrIpAddrPrefix attribute of this entry.
     */
    public String getAttrIpAddrPrefix() {
        return attrIpAddrPrefix;
    }

    /**
     * Sets the bgp4PathAttrIpAddrPrefix attribute of this entry.
     */
    public void setAttrIpAddrPrefix(String attrIpAddrPrefix) {
        this.attrIpAddrPrefix = attrIpAddrPrefix;
    }

    /**
     * Returns the bgp4PathAttrOrigin attribute of this entry.
     */
    public int getAttrOrigin() {
        return attrOrigin;
    }

    /**
     * Sets the bgp4PathAttrOrigin attribute of this entry.
     */
    public void setAttrOrigin(int attrOrigin) {
        this.attrOrigin = attrOrigin;
    }

    /**
     * Returns the bgp4PathAttrASPathSegment attribute of this entry.
     */
    public String getAttrASPathSegment() {
        return attrASPathSegment;
    }

    /**
     * Sets the bgp4PathAttrASPathSegment attribute of this entry and calculates
     * the AS path length.
     */
    public void setAttrASPathSegment(String attrASPathSegment) {
    	this.attrASPathSegment = attrASPathSegment;
    	StringTokenizer st = new StringTokenizer(attrASPathSegment,":");
    	asPathLength = (st.countTokens() / 2) -1;
    }
    
    /**
     * Returns the AS path length of this entry.
     */
    public int getASPathLength() {
        return asPathLength;
    }

    /**
     * Sets the AS path length of this entry.
     */
    public void setASPathLength(int asPathLength) {
        this.asPathLength = asPathLength;
    }

    /**
     * Returns the bgp4PathAttrNextHop attribute of this entry.
     */
    public String getAttrNextHop() {
        return attrNextHop;
    }

    /**
     * Sets the bgp4PathAttrNextHop attribute of this entry.
     */
    public void setAttrNextHop(String attrNextHop) {
        this.attrNextHop = attrNextHop;
    }

    /**
     * Returns the bgp4PathAttrMultiExitDisc attribute of this entry.
     */
    public int getAttrMultiExitDisc() {
        return attrMultiExitDisc;
    }

    /**
     * Sets the bgp4PathAttrMultiExitDisc attribute of this entry.
     */
    public void setAttrMultiExitDisc(int attrMultiExitDisc) {
        this.attrMultiExitDisc = attrMultiExitDisc;
    }

    /**
     * Returns the bgp4PathAttrLocalPref attribute of this entry.
     */
    public int getAttrLocalPref() {
        return attrLocalPref;
    }

    /**
     * Sets the bgp4PathAttrLocalPref attribute of this entry.
     */
    public void setAttrLocalPref(int attrLocalPref) {
       	this.attrLocalPref = attrLocalPref;
    }

    /**
     * Returns the bgp4PathAttrAtomicAggregate attribute of this entry.
     */
    public int getAttrAtomicAggregate() {
        return attrAtomicAggregate;
    }

    /**
     * Sets the bgp4PathAttrAtomicAggregate attribute of this entry.
     */
    public void setAttrAtomicAggregate(int attrAtomicAggregate) {
        this.attrAtomicAggregate = attrAtomicAggregate;
    }

    /**
     * Returns the bgp4PathAttrAggregatorAS attribute of this entry.
     */
    public int getAttrAggregatorAS() {
        return attrAggregatorAS;
    }

    /**
     * Sets the bgp4PathAttrAggregatorAS attribute of this entry.
     */
    public void setAttrAggregatorAS(int attrAggregatorAS) {
        this.attrAggregatorAS = attrAggregatorAS;
    }

    /**
     * Returns the bgp4PathAttrAggregatorAddr attribute of this entry.
     */
    public String getAttrAggregatorAddr() {
        return attrAggregatorAddr;
    }

    /**
     * Sets the bgp4PathAttrAggregatorAddr attribute of this entry.
     */
    public void setAttrAggregatorAddr(String attrAggregatorAddr) {
        this.attrAggregatorAddr = attrAggregatorAddr;
    }

    /**
     * Returns the bgp4PathAttrCalcLocalPref attribute of this entry.
     */
    public int getAttrCalcLocalPref() {
        return attrCalcLocalPref;
    }

    /**
     * Sets the bgp4PathAttrCalcLocalPref attribute of this entry.
     */
    public void setAttrCalcLocalPref(int attrCalcLocalPref) {
       	this.attrCalcLocalPref = attrCalcLocalPref;
    }

    /**
     * Returns the bgp4PathAttrBest attribute of this entry.
     */
    public int getAttrBest() {
        return attrBest;
    }

    /**
     * Sets the bgp4PathAttrBest attribute of this entry.
     */
    public void setAttrBest(int attrBest) {
        this.attrBest = attrBest;
    }

}