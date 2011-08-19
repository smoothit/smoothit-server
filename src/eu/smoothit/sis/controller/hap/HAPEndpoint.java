package eu.smoothit.sis.controller.hap;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * The Web Service interface for the HAP-related functionalities provided by SIS to peers and NMS.
 * 
 * @author Sergios Soursos, Intracom Telecom
 */

@WebService(name = "SisHAPPort", targetNamespace = "http://hap.ws.sis.smoothit.eu/")
public interface HAPEndpoint {
	
	/**
	 * Receives a report from a peer about the activity with its neighbors.  
	 * 
	 * @param report The report of peer's activity with its neighbors.
	 */
	@WebMethod
	@WebResult(name = "peerstats", targetNamespace = "http://hap.ws.sis.smoothit.eu/")
	void reportActivity(PeerStats report);

}
