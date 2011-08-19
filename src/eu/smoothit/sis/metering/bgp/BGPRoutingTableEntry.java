package eu.smoothit.sis.metering.bgp;

/**
 * The interface for the BGP routing table entry. 
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public interface BGPRoutingTableEntry {
    
    /**
     * Prints all attributes of this routing entry to the logger.
     */
    public void print();
    
    /**
     * Returns the bgp4PathAttrPeer attribute of this entry.
     */
    public String getAttrPeer();

    /**
     * Sets the bgp4PathAttrPeer attribute of this entry.
     */
    public void setAttrPeer(String attrPeer);

    /**
     * Returns the bgp4PathAttrIpAddrPrefixLen attribute of this entry.
     */
    public int getAttrIpAddrPrefixLen();

    /**
     * Sets the bgp4PathAttrIpAddrPrefixLen attribute of this entry.
     */
    public void setAttrIpAddrPrefixLen(int attrIpAddrPrefixLen);

    /**
     * Returns the bgp4PathAttrIpAddrPrefix attribute of this entry.
     */
    public String getAttrIpAddrPrefix();

    /**
     * Sets the bgp4PathAttrIpAddrPrefix attribute of this entry.
     */
    public void setAttrIpAddrPrefix(String attrIpAddrPrefix);

    /**
     * Returns the bgp4PathAttrOrigin attribute of this entry.
     */
    public int getAttrOrigin();

    /**
     * Sets the bgp4PathAttrOrigin attribute of this entry.
     */
    public void setAttrOrigin(int attrOrigin);

    /**
     * Returns the bgp4PathAttrASPathSegment attribute of this entry.
     */
    public String getAttrASPathSegment();

    /**
     * Sets the bgp4PathAttrASPathSegment attribute of this entry and calculates
     * the AS path length.
     */
    public void setAttrASPathSegment(String attrASPathSegment);
    
    /**
     * Returns the AS path length of this entry.
     */
    public int getASPathLength();

    /**
     * Sets the AS path length of this entry.
     */
    public void setASPathLength(int asPathLength);

    /**
     * Returns the bgp4PathAttrNextHop attribute of this entry.
     */
    public String getAttrNextHop();

    /**
     * Sets the bgp4PathAttrNextHop attribute of this entry.
     */
    public void setAttrNextHop(String attrNextHop);

    /**
     * Returns the bgp4PathAttrMultiExitDisc attribute of this entry.
     */
    public int getAttrMultiExitDisc();

    /**
     * Sets the bgp4PathAttrMultiExitDisc attribute of this entry.
     */
    public void setAttrMultiExitDisc(int attrMultiExitDisc);

    /**
     * Returns the bgp4PathAttrLocalPref attribute of this entry.
     */
    public int getAttrLocalPref();

    /**
     * Sets the bgp4PathAttrLocalPref attribute of this entry.
     */
    public void setAttrLocalPref(int attrLocalPref);

    /**
     * Returns the bgp4PathAttrAtomicAggregate attribute of this entry.
     */
    public int getAttrAtomicAggregate();

    /**
     * Sets the bgp4PathAttrAtomicAggregate attribute of this entry.
     */
    public void setAttrAtomicAggregate(int attrAtomicAggregate);

    /**
     * Returns the bgp4PathAttrAggregatorAS attribute of this entry.
     */
    public int getAttrAggregatorAS();

    /**
     * Sets the bgp4PathAttrAggregatorAS attribute of this entry.
     */
    public void setAttrAggregatorAS(int attrAggregatorAS);

    /**
     * Returns the bgp4PathAttrAggregatorAddr attribute of this entry.
     */
    public String getAttrAggregatorAddr();

    /**
     * Sets the bgp4PathAttrAggregatorAddr attribute of this entry.
     */
    public void setAttrAggregatorAddr(String attrAggregatorAddr);

    /**
     * Returns the bgp4PathAttrCalcLocalPref attribute of this entry.
     */
    public int getAttrCalcLocalPref();

    /**
     * Sets the bgp4PathAttrCalcLocalPref attribute of this entry.
     */
    public void setAttrCalcLocalPref(int attrCalcLocalPref);

    /**
     * Returns the bgp4PathAttrBest attribute of this entry.
     */
    public int getAttrBest();

    /**
     * Sets the bgp4PathAttrBest attribute of this entry.
     */
    public void setAttrBest(int attrBest);

}
