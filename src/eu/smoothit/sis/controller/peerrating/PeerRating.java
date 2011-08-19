package eu.smoothit.sis.controller.peerrating;

import java.util.List;

import javax.ejb.Local;

/**
 * The local interface for the Generic Peer Rating Service
 * implemented as an EJB3 session bean
 * 
 * @author Michael Makidis, Intracom Telecom
 * @version 1.0
 *                  
 */
@Local
public interface PeerRating {
	
	/**
	 * Prepares a sorted list of rated ResponseEntries with the generic peer
	 * rating algorithm (see D2.3).
	 * 
	 *  @param addresses The list of IP addresses to be sorted.
	 *  
	 *  @return The sorted and rated list of peer IP addresses.
	 *         
	 */
	public List<ResponseEntry> rankAddresses(List<RequestEntry> addresses);

}
