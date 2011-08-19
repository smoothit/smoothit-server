package eu.smoothit.sis.intersis.server;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import eu.smoothit.sis.controller.peerrating.Request;
import eu.smoothit.sis.controller.peerrating.Response;

/**
 * The Web Service interface for the InterSIS communication.
 * 
 * @author Michael Makidis, Intracom Telecom
 */
@WebService(name = "InterSisPort", targetNamespace = "http://intersis.ws.sis.smoothit.eu/")
public interface InterSisEndpoint {
	
	/**
	 * This is the main method for authorizing a server, as defined in D3.1.
	 * 
	 * @param key The key as defined in D3.1.
	 * @return The response as defined in D3.1.
	 */
	@WebMethod
	@WebResult(name = "response", targetNamespace = "http://intersis.ws.sis.smoothit.eu/")
	public boolean authorizeServer(String key);
	
	/**
	 * This is the main method for getting the ranked peer list.
	 * 
	 * @param req The request as defined in D3.1.
	 * @return The response as defined in D3.1.
	 */
	@WebMethod
	@WebResult(name = "response", targetNamespace = "http://intersis.ws.sis.smoothit.eu/")
	Response getRankedPeerList(Request req);

}
