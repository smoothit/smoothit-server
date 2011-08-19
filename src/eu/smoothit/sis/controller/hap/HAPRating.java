package eu.smoothit.sis.controller.hap;

import java.util.List;

import javax.ejb.Local;

/**
 * The local interface for the rating HAP candidates 
 * implemented as an EJB3 session bean
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 *                  
 */
@Local
public interface HAPRating {
	
	/**
	 *  Receives and stores a peer's activity with its neighbors.
	 * 
	 *  @param report The report of peer's activity with its neighbors.
	 *  
	 */
	public void storePeerActivity(PeerStats report);
	
	/**
	 * Executes the HAP rating algorithm.
	 * 
	 */
	public void calcHAPRatings();
	
	/**
	 * Decides which peers to be promoted to and demoted from HAPs.
	 * 
	 * @param delta Whether to report peers that are already HAPs and should remain HAPs (true=no, false=yes)
	 * @return The list of of promoted and demoted HAPs.
	 */
	public List<List<String>> fetchHAPs(boolean delta);

}
