package eu.smoothit.sis.metering.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.metering.bgp.BGPRoutingTable;
import eu.smoothit.sis.metering.bgp.BGPRoutingTableEntry;

/**
 * Reads the BGP routing table from a router, from a file, or from the SIS DB and 
 * loads it into memory. It implements the BGPRoutingTable interface.  
 * 
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
public class BGPRoutingTableReader implements BGPRoutingTable {

	private static Logger logger = Logger.getLogger(BGPRoutingTableReader.class.getName());
	private HashMap<String, BGPRoutingTableEntry> routingTable = new HashMap<String, BGPRoutingTableEntry>(); 
	private static final int SRC_FILE = 1;
	private static final int SRC_ROUTER = 2;
	private static final int SRC_DB = 3;
	private int infoSrc = SRC_FILE;
    private String fileName = "bgpinfo.conf";
    private String host = "localhost";
    private int port = 161;
    private static Snmp snmpSession = null;
    private Address snmpAddress;
    private CommunityTarget snmpTarget;
    private String snmpCommunity = "public";
    private int snmpVersion = SnmpConstants.version2c;
    private int snmpRetries = 5;
    private int snmpTimeout = 1000;
    private int maxLocalPreference = 0;
    private int maxASPathLength = 0;
    private int maxMED = 0;

    private Vector<Integer> numberOfEntries = new Vector<Integer>(); // for test purposes
    int n = 0; // for test purposes

    /**
     * Default constructor.
     */
    public BGPRoutingTableReader() {}
    
	/**
	 * Sets the IP address of the router where the BGP routing table is read from. 
	 */
	public void setRouterAddress(String a) {
    	host = a;
    }

	/**
	 * Sets the port number of the router where the BGP routing table is read from. 
	 */
	public void setRouterPort(int p) {
    	port = p;
    }

	/**
	 * Sets the SNMP community string for reading the BGP routing table from a router. 
	 */
	public void setSNMPCommunity(String c) {
		snmpCommunity = c;
    }

	/**
	 * Sets the name of the file where the BGP routing table is read from. 
	 */
	public void setFileName(String f) {
    	fileName = f;
    }

	/**
	 * Specifies to use a router to read the BGP routing table from. 
	 */
	public void useRouter() {
    	infoSrc = SRC_ROUTER;
    }

	/**
	 * Specifies to use a file to read the BGP routing table from. 
	 */
	public void useFile() {
    	infoSrc = SRC_FILE;
    }

	/**
	 * Specifies to use the database to read the BGP routing table from. 
	 */
	public void useDB() {
    	infoSrc = SRC_DB;
    }

   	/**
   	 * Returns the complete BGP routing table.
   	 * 
   	 * @return	a collection of routing table entries.
   	 */
	public Collection<BGPRoutingTableEntry> getAllEntries() {
    	return routingTable.values();
    }

   	/**
   	 * Returns the number of routing entries in the BGP routing table.
   	 *  
   	 * @return	number of routing entries.
   	 */
    public int getNumberOfEntries() {
   		return routingTable.size();
	}

	/**
   	 * Returns the routing entry that represents the route to the destination IP 
   	 * address specified in the argument.
   	 * 
   	 * @return	the routing entry.
   	 */
    public BGPRoutingTableEntry getEntryForPrefix(String ip) {
    	int maxPrefixLength = 0;
    	int addr = convertIPAddressToInt(ip);
    	BGPRoutingTableEntry foundEntry = null;
    	
    	for(BGPRoutingTableEntry entry : routingTable.values()) {
    		if(entry.getAttrBest() == 2) {
    			// If the route is selected as the best route by BGP, check for matching
    			int prefixLength = entry.getAttrIpAddrPrefixLen();
    			if(prefixLength > maxPrefixLength) {
    				// If the prefixLength is greater than the prefix length of the last 
    				// matching entry, the new entry is checked for matching   
    				int prefix = convertIPAddressToInt(entry.getAttrIpAddrPrefix());
    				if(prefix == (addr & (0xFFFFFFFF << (32 - prefixLength)))) {
    					// There is a new matching with larger prefix length
    					maxPrefixLength = prefixLength;
    					foundEntry = entry;
    				}
    			}
    		}
    	}
    	return(foundEntry);    	
    }
    
    /**
     * Returns and removes a routing entry from the routing table.
     *  
     * @return The routing entry or null if the routing table is empty.
     */
    public BGPRoutingTableEntry retrieveNext() {
   		if(routingTable.size() == 0) return null;
    	
   		String key = routingTable.keySet().iterator().next();
   		BGPRoutingTableEntry entry = routingTable.get(key);
   		routingTable.remove(key);
   		return entry;
    }

    /**
     * Returns the maximum value of the local preference attribute.
     * 
     * @return The maximum value.
     */
    public int getMaxLocalPreference() {
    	return maxLocalPreference;
    }
    
    /**
     * Returns the maximum value of the AS path length.
     * 
     * @return The maximum value.
     */
    public int getMaxASPathLength() {
    	return maxASPathLength;
    }
    
    /**
     * Returns the maximum value of the MED attribute.
     * 
     * @return The maximum value.
     */
    public int getMaxMED() {
    	return maxMED;
    }
    
   	/**
   	 * Prints the complete BGP routing table to the logger.
   	 */
    public void print() {
       	if(routingTable.size() == 0) {
       		logger.trace("Dump routing table: routing table is empty");
       	} else {
       		logger.trace("Dump routing table:");
       	}
       	int index = 1;
   		for(BGPRoutingTableEntry entry : routingTable.values()) {
   			logger.trace("===== " + (index) + ". Entry =====");
   			entry.print();
   			index++;
   		}    		
	}
    
   	/**
   	 * Loads the BGP routing table into memory. Whenever this method is called, the 
   	 * routing table is reloaded. 
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
    public boolean readRoutingTable() {
    	if(infoSrc == SRC_ROUTER) {
    		return readRoutingTableFromRouter();
    	} else if(infoSrc == SRC_DB) {
    		return readRoutingTableFromDB();
    	} else {
    		return readRoutingTableFromFile();
    	}
    }

    /**
     * Opens an SNMP connection over TCP or UDP.
     * 
     * @param useTCP specifies to use TCP or UDP.
     * @return	<code>true</code> if the connection was opened successfully,
   	 * 			<code>false</code> otherwise.
     */
    private boolean openConnection(boolean useTCP) {
    	
    	try {
    		if(useTCP) {
        		snmpAddress = new TcpAddress(host + "/" + port);
    		} else {
        		snmpAddress = new UdpAddress(host + "/" + port);    			
    		}
    	} catch(Exception ex) {
        	logger.error("Invalid router address: " + ex.getMessage());
            return(false);
        }
    	
        try {
        	AbstractTransportMapping transport;
        	if(useTCP) {
           		transport = new DefaultTcpTransportMapping();
        	} else {
           		transport = new DefaultUdpTransportMapping();
        	}
       		snmpSession =  new Snmp(transport);
        	
            snmpTarget = new CommunityTarget();
            snmpTarget.setCommunity(new OctetString(snmpCommunity));
            snmpTarget.setVersion(snmpVersion);
            snmpTarget.setAddress(snmpAddress);
            snmpTarget.setRetries(snmpRetries);
            snmpTarget.setTimeout(snmpTimeout);
            snmpSession.listen();

            PDU request = new PDU();
            request.setType(PDU.GETNEXT);
            request.add(new VariableBinding(new OID("")));

            // Send SNMP request to test the connection
   			ResponseEvent responseEvent = snmpSession.send(request, snmpTarget);
   			if(responseEvent == null || responseEvent.getResponse() == null) {
   				return(false);
   			}
        } catch(IOException ex) {
        	logger.error("IO error: " + ex.getMessage());
            return(false);
        }
        
        return(true);
    }
    
   	/**
   	 * Reads the BGP routing table over SNMP from a router and loads it into memory. 
   	 * Whenever this method is called, the routing table is reloaded. 
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
    private boolean readRoutingTableFromRouter() {
    	logger.info("Reading routing table from router " + host + "/" + port);
    	
    	reset();

    	// Try to open a connection to the router first via TCP. If that fails, try to open via UDP.
    	boolean isTCP = false;
        if(!openConnection(true)) {
        	if(!openConnection(false)) {
            	logger.error("Opening connection to " + host + "/" + port +" failed");
            	return(false);
        	} else {
            	logger.debug("Opened UDP connection to "  + host + "/" + port);
        	}
        } else {
        	logger.debug("Opened TCP connection to "  + host + "/" + port);
        	isTCP = true;
        }

        // The BGP OIDs to be read from the router.
       	Vector<String> oidsToRead = new Vector<String>();
       	oidsToRead.add("1.3.6.1.2.1.15.6.1.13");
       	oidsToRead.add("1.3.6.1.2.1.15.6.1.2");
       	oidsToRead.add("1.3.6.1.2.1.15.6.1.3");
       	oidsToRead.add("1.3.6.1.2.1.15.6.1.5");
       	oidsToRead.add("1.3.6.1.2.1.15.6.1.7");
       	oidsToRead.add("1.3.6.1.2.1.15.6.1.8");

       	try {
        	for(String oid : oidsToRead) {
                PDU request = new PDU();
                request.setType(PDU.GETBULK);
        		request.setNonRepeaters(0);
        		if(isTCP)
        			request.setMaxRepetitions(100);
        		else
        			request.setMaxRepetitions(30);

        		logger.debug("Reading OID " + oid);
        		n=0; // counting the total number of entries, it is for test purposes
	    		OID tmpOID = new OID(oid);
	            request.add(new VariableBinding(tmpOID));
	
	            PDU response = null;
	    		boolean finished = false;
	    		while(!finished) {
	    			// Send SNMP request for the next OID to the router and process the SNMP response
	    			logger.trace("SNMP request: " + request.toString());
	    			ResponseEvent responseEvent = snmpSession.send(request, snmpTarget);
	       			if(responseEvent == null) {
	       				logger.error("Error receiving SNMP response from " + snmpAddress.toString());
	       				snmpSession.close();
	       				return(false);
	       			} else {
	        			response = responseEvent.getResponse();
	        			if(response == null) {
	        				logger.error("Error receiving SNMP response from " + snmpAddress.toString()  + ": Timeout");
	        				if(responseEvent.getError() != null) {
	        					logger.error("Error msg: " + responseEvent.getError().getMessage());
	        				}
	        				// Remove all entries from the routing table and return
	        				reset();
	        				snmpSession.close();
	        				return(false);    				
	        			}
	    			}
	
	    			if(response.size() == 0) {
	    				logger.error("SNMP response size is zero");
	    			}
	    			if((response.getErrorStatus() != 0) || (response.getType() != PDU.RESPONSE) || (response.size() == 0)) {
	    				logger.error("Error in SNMP response: " + response.toString());
	    				if(responseEvent.getError() != null) {
	    					logger.error("Error msg: " + responseEvent.getError().getMessage());
	    				}
						// Remove all entries from the routing table and return
	    				reset();
	    				snmpSession.close();
						return(false);
	    			} else {
	        			//logger.trace("SNMP response: " + response.toString());
		    			//logger.trace("SNMP response size: " + response.size());
						//logger.trace("VB: " + response.get(0));
	
						for (int i=0; i<response.size(); i++) {
	    					VariableBinding vb = response.get(i);
	    					if((vb.getOid() == null) || (vb.getOid().size() < tmpOID.size())
	    						|| (tmpOID.leftMostCompare(tmpOID.size(), vb.getOid()) != 0)) {
	    						// End of items with specified OID
	    						logger.trace("OID: " + vb.getOid() + ". End of items with OID " + tmpOID.toString());
	    						finished = true;
	    						break;
	    					} else if(Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
	    						// Syntax exception
	    						logger.error("Syntax exception: " + vb.toString());
	    						// Remove all entries from the routing table and return
	    						reset();
	    						snmpSession.close();
	    						return(false);
	    					} else if(vb.getOid().compareTo(tmpOID) <= 0) {
	    						logger.error("Entry received is not lexicographic successor of requested one: " + vb.toString() + " <= " + tmpOID);
	    						// Remove all entries from the routing table and return
	    						reset();
	    						snmpSession.close();
	    						return(false);
	    					} else {
	    						// Process the entry in the SNMP response
	    			        	if(tmpOID.leftMostCompare(10, response.get(i).getOid()) != 0) {
	    			        		logger.trace("Last OID found: " + tmpOID.toString() + ", " + response.get(i).getOid().toString());
	    			        		break;
	    			        	}

	    						logger.trace("Processing SNMP entry: " + vb.toString());
	    						if(processEntry(vb) == false) {
	    							logger.error("Error processing SNMP entry: " + vb.toString());
	        						// Remove all entries from the routing table and return
	    							reset();
	    							snmpSession.close();
	        						return(false);
	    						}
	    						n++; // for test purposes
	    					}
	    				}
						
						// Prepare the next SNMP request
						VariableBinding next = response.get(response.size()-1);
						next.setVariable(new Null());
						request.set(0, next);
						request.setRequestID(new Integer32(0));
	    			}
	    		}
	    		numberOfEntries.add(n); // for test purposes
        	}
        	snmpSession.close();
        	
        } catch(IOException ex) {
        	logger.error("IO error: " + ex.getMessage());
            return(false);
        }

        logger.debug("Number of entries read: " + numberOfEntries); // for test purposes
        numberOfEntries.clear();
        
        return(true);
    }
    
    /**
     * Processes the SNMP response and updates the BGP routing table in memory accordingly. 
     * VariableBinding represents an item received from SNMP that contains the OID and its 
     * corresponding value. This method creates a new BGPRoutingTableEntry and assigns the
     * OID value to the appropriate routing entry.
     * 
     * @return	<code>true</code> if the SNMP item was processed successfully,
   	 * 			<code>false</code> otherwise.
     */
    private boolean processEntry(VariableBinding vb) {
    	
    	int oidSize = vb.getOid().size();
    	int oidIdentifiers[] = vb.getOid().getValue();
    	String objectValue = vb.getVariable().toString();
    	
    	if(oidSize != 19) {
    		// OID does not have expected 19 tokens
    		return false;
    	}
    	
        int index = oidIdentifiers[9]; // oid index defining the type of the SNMP entry
        
        BGPRoutingTableEntryImpl routingEntry = null;
        switch(index) {
        	case 1: // bgp4PathAttrPeer(1)
            	routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrPeer(objectValue);
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
        		break;
            case 2: // bgp4PathAttrIpAddrPrefixLen(2)
            	routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrIpAddrPrefixLen(Integer.parseInt(objectValue));
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 3: // bgp4PathAttrIpAddrPrefix(3)
            	routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrIpAddrPrefix(objectValue);
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 4: // bgp4PathAttrOrigin(4)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrOrigin(Integer.parseInt(objectValue));
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 5: // bgp4PathAttrASPathSegment(5)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrASPathSegment(objectValue);
            		// Update the maximum AS path length
                	int asPathLength = routingEntry.getASPathLength();
            		if(asPathLength > maxASPathLength) maxASPathLength = asPathLength;
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 6: // bgp4PathAttrNextHop(6)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrNextHop(objectValue);
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 7: // bgp4PathAttrMultiExitDisc(7)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
                	int med = Integer.parseInt(objectValue);
            		routingEntry.setAttrMultiExitDisc(med);
            		// Update the maximum MED
            		if(med > maxMED) maxMED = med;
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 8: // bgp4PathAttrLocalPref(8)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
                	int localPref = Integer.parseInt(objectValue);
            		routingEntry.setAttrLocalPref(localPref);
            		// Update the maximum local preference
            		if(localPref > maxLocalPreference) maxLocalPreference = localPref;
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 9: // bgp4PathAttrAtomicAggregate(9)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(21));
            	if(routingEntry != null) {
            		routingEntry.setAttrAtomicAggregate(Integer.parseInt(objectValue));
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 10: // bgp4PathAttrAggregatorAS(10)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(22));
            	if(routingEntry != null) {
            		routingEntry.setAttrAggregatorAS(Integer.parseInt(objectValue));
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 11: // bgp4PathAttrAggregatorAddr(11)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(22));
            	if(routingEntry != null) {
            		routingEntry.setAttrAggregatorAddr(objectValue);
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 12: // bgp4PathAttrCalcLocalPref(12)
        		routingEntry = (BGPRoutingTableEntryImpl) routingTable.get(vb.getOid().toString().substring(22));
            	if(routingEntry != null) {
            		routingEntry.setAttrCalcLocalPref(Integer.parseInt(objectValue));
            	} else {
            		logger.trace("OID not found: " + vb.getOid().toString());
            	}
                break;
            case 13: // bgp4PathAttrBest(13)
            	if(Integer.parseInt(objectValue) == 2) {
            		// if the routing entry is a best path, it is stored in the routingTable
            		routingEntry = new BGPRoutingTableEntryImpl();
            		logger.trace("Adding entry: " + vb.getOid().toString().substring(22));
            		routingTable.put(vb.getOid().toString().substring(22), routingEntry);
            	} else {
            		logger.trace("Not a best path: " + vb.getOid().toString());
            	}
            	break;
            default:
            	logger.warn("Unknown SNMP entry: " + vb.toString());
        }

        return true;
    }
    
   	/**
   	 * Reads the BGP routing table from a file and loads it into memory. 
   	 * Whenever this method is called, the routing table is reloaded. 
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
    private boolean readRoutingTableFromFile() {
    	logger.info("Reading routing table from file " + fileName);

    	reset();
    	
    	// Open file with BGP information
    	BufferedReader in = null;
       	InputStream instream = getClass().getResourceAsStream(fileName);
       	if(instream == null) {
       		logger.debug("File could not be located as a resource, trying to open it in the file system: " + fileName);
       		try {
				instream = new FileInputStream(fileName);
			} catch (FileNotFoundException e) {
	       		logger.error("File not found: " + fileName);
	       		return(false);
			}
       	}
       	in = new BufferedReader(new InputStreamReader(instream));
    	
       	String strLine = null;
       	int index = 1; // index of the BGP entry in the file
       	int attrNumber = 1; // next expected attribute number that is read from the file 
       	BGPRoutingTableEntry routingEntry = new BGPRoutingTableEntryImpl();

       	try {
        	strLine = in.readLine();
        	while(strLine != null) {
        		try {
        			strLine = strLine.trim();
           			if(strLine.compareTo("") == 0 || strLine.startsWith("#")) {
        				strLine = in.readLine();
        				continue;
        			}
        			
    	        	// bgp4PathAttrPeer(1)
        			if(attrNumber == 1) {
        				if(strLine.startsWith(index + ".bgp4PathAttrPeer")) {
        					routingEntry.setAttrPeer(strLine.substring(strLine.indexOf('=')+2));
        				} else {
        					attrNumber = 2;
        				}
        			}
    	        	// bgp4PathAttrIpAddrPrefixLen(2)
        			if(attrNumber == 2) {
        				if(strLine.startsWith(index + ".bgp4PathAttrIpAddrPrefixLen")) {
        					routingEntry.setAttrIpAddrPrefixLen(Integer.parseInt(strLine.substring(strLine.indexOf('=')+2)));
        				} else {
        					// Prefix length not found, stop parsing the file
							logger.error("bgp4PathAttrIpAddrPrefixLen not found");
							reset();
							in.close();
        					return(false);
        				}
        			}
        			// bgp4PathAttrIpAddrPrefix(3)
        			if(attrNumber == 3) {
        				if(strLine.startsWith(index + ".bgp4PathAttrIpAddrPrefix")) {
        					routingEntry.setAttrIpAddrPrefix(strLine.substring(strLine.indexOf('=')+2));
        				} else {
        					// Prefix address not found, stop parsing the file
							logger.error("bgp4PathAttrIpAddrPrefix not found");
							reset();
							in.close();
        					return(false);
        				}
        			}
        			// bgp4PathAttrOrigin(4)
        			if(attrNumber == 4) {
        				if(strLine.startsWith(index + ".bgp4PathAttrOrigin")) {
        					routingEntry.setAttrOrigin(Integer.parseInt(strLine.substring(strLine.indexOf('=')+2)));
        				} else {
        					attrNumber = 5;
        				}
        			}
        			// bgp4PathAttrASPathSegment(5)
        			if(attrNumber == 5) {
        				if(strLine.startsWith(index + ".bgp4PathAttrASPathSegment")) {
        					String asPath = strLine.substring(strLine.indexOf('=')+2);
        					routingEntry.setAttrASPathSegment(asPath);
        	        		// Update the maximum AS path length
        	            	StringTokenizer st = new StringTokenizer(asPath,":");
        	            	int asPathLength = (st.countTokens() / 2) -1;
        	        		if(asPathLength > maxASPathLength) maxASPathLength = asPathLength;
        				} else {
        	        		// AS path not found, stop parsing the file
							logger.error("bgp4PathAttrASPathSegment not found");
							reset();
							in.close();
        	        		return(false);
        				}
        			}
        			// bgp4PathAttrNextHop(6)
        			if(attrNumber == 6) {
        				if(strLine.startsWith(index + ".bgp4PathAttrNextHop")) {
        					routingEntry.setAttrNextHop(strLine.substring(strLine.indexOf('=')+2));
        				} else {
        					attrNumber = 7;
        				}
        			}
        			// bgp4PathAttrMultiExitDisc(7)
        			if(attrNumber == 7) {
        				if(strLine.startsWith(index + ".bgp4PathAttrMultiExitDisc")) {
        					int med = Integer.parseInt(strLine.substring(strLine.indexOf('=')+2));
        					routingEntry.setAttrMultiExitDisc(med);
        	        		// Update the maximum MED
        	        		if(med > maxMED) maxMED = med;
        				} else {
        					attrNumber = 8;
        				}
    				}
        			// bgp4PathAttrLocalPref(8)
        			if(attrNumber == 8) {
        				if(strLine.startsWith(index + ".bgp4PathAttrLocalPref")) {
        					int localPref = Integer.parseInt(strLine.substring(strLine.indexOf('=')+2));
        					routingEntry.setAttrLocalPref(localPref);
        	        		// Update the maximum local preference
        	        		if(localPref > maxLocalPreference) maxLocalPreference = localPref;
        				} else {
        					attrNumber = 9;
        				}
        			}
        			// bgp4PathAttrAtomicAggregate(9)
        			if(attrNumber == 9) {
        				if(strLine.startsWith(index + ".bgp4PathAttrAtomicAggregate")) {
        					routingEntry.setAttrAtomicAggregate(Integer.parseInt(strLine.substring(strLine.indexOf('=')+2)));
        				} else {
        					attrNumber = 10;
        				}
        			}
        			// bgp4PathAttrAggregatorAS(10)
        			if(attrNumber == 10) {
        				if(strLine.startsWith(index + ".bgp4PathAttrAggregatorAS")) {
        					routingEntry.setAttrAggregatorAS(Integer.parseInt(strLine.substring(strLine.indexOf('=')+2)));
        				} else {
        					attrNumber = 11;
        				}
        			}
        			// bgp4PathAttrAggregatorAddr(11)
        			if(attrNumber == 11) {
        				if(strLine.startsWith(index + ".bgp4PathAttrAggregatorAddr")) {
        					routingEntry.setAttrAggregatorAddr(strLine.substring(strLine.indexOf('=')+2));
        				} else {
        					attrNumber = 12;
        				}
        			}
        			// bgp4PathAttrCalcLocalPref(12)
        			if(attrNumber == 12) {
        				if(strLine.startsWith(index + ".bgp4PathAttrCalcLocalPref")) {
        					routingEntry.setAttrCalcLocalPref(Integer.parseInt(strLine.substring(strLine.indexOf('=')+2)));
        				} else {
        					attrNumber = 13;
        				}
        			}
        			// bgp4PathAttrBest(13)
        			if(attrNumber == 13) {
        				if(strLine.startsWith(index + ".bgp4PathAttrBest")) {
        					routingEntry.setAttrBest(Integer.parseInt(strLine.substring(strLine.indexOf('=')+2)));
        					strLine = in.readLine();
        				}
    	        		routingTable.put(Integer.valueOf(index).toString(), routingEntry);
    	        		routingEntry = new BGPRoutingTableEntryImpl();
    	        		index++;
    	        		attrNumber = 1;
        			} else {
        				attrNumber ++;
        				strLine = in.readLine();
        			}

                } catch(NumberFormatException e) {
                	logger.error("Error parsing file: " + e.getMessage());
                	reset();
                	in.close();
                	return(false);
                }
        	}
        	
        	if(attrNumber > 5) {
        		routingTable.put(Integer.valueOf(index).toString(), routingEntry);
        	} else if(attrNumber != 1) {
        		// Last entry was not complete
        		logger.error("Missing mandatory fields");
        		reset();
            	in.close();
        		return(false);
        	}
        	
        	in.close();
        	
    	} catch(IOException e) {
   			logger.error("Error: " + e.getMessage());
   			reset();
    		return(false);       		
   		}

   		return(true);
    }

   	/**
   	 * Reads the BGP routing table from the SIS DB and loads it into memory. 
   	 * Whenever this method is called, the routing table is reloaded. 
   	 *   
   	 * @return	<code>true</code> if the routing table was loaded successfully,
   	 * 			<code>false</code> otherwise.
   	 */
    private boolean readRoutingTableFromDB() {
    	logger.info("Reading routing table from SIS DB");
    	
    	reset();
    	
    	IPRangeDAO dao;
    	dao = SisDAOFactory.getFactory().createIPRangeDAO();
    	IPRangeConfigEntry entry = new IPRangeConfigEntry();
    	entry.setLocal(false);
    	List<IPRangeConfigEntry> ipRanges = dao.get(entry);

    	int index = 1;
    	for(IPRangeConfigEntry i : ipRanges) {
        	BGPRoutingTableEntry routingEntry = new BGPRoutingTableEntryImpl();
    	
        	routingEntry.setAttrIpAddrPrefixLen(i.getPrefix_len());
        	routingEntry.setAttrIpAddrPrefix(i.getPrefix());
        	routingEntry.setAttrLocalPref(i.getLocalPreference());
        	routingEntry.setASPathLength(i.getASPathLength());
        	routingEntry.setAttrMultiExitDisc(i.getMED());

        	// Update maximum values
        	if(i.getLocalPreference() > maxLocalPreference) maxLocalPreference = i.getLocalPreference();
        	if(i.getASPathLength() > maxASPathLength) maxASPathLength = i.getASPathLength();
        	if(i.getMED() > maxMED) maxMED = i.getMED();
        	
        	routingTable.put(Integer.valueOf(index).toString(), routingEntry);
        	index++;
    	}
    	
    	return(true);
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

    /**
     * Resets the BGPRoutingTableReader and removes the routing table from the memory.
     */
    private void reset() {
    	routingTable.clear();
    	maxLocalPreference = 0;
    	maxASPathLength = 0;
    	maxMED = 0;
    }

}
