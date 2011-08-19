package eu.smoothit.sis.intersis.server.impl;

import javax.jws.WebService;

import eu.smoothit.sis.controller.peerrating.Request;
import eu.smoothit.sis.controller.peerrating.Response;
import eu.smoothit.sis.intersis.server.InterSisEndpoint;

/**
 * Basic Servlet Using XDoclet.
 * 
 * @author Michael Makidis
 * @version 1.0
 * 
 * @web.servlet 
 * 		name = "InterSis" 
 * 		display-name = "InterSis"
 * @web.servlet-mapping 
 * 		url-pattern = "/InterSis"                    
 */

@WebService(serviceName = "InterSisService", endpointInterface = "eu.smoothit.sis.intersis.server.InterSisEndpoint", targetNamespace = "http://intersis.ws.sis.smoothit.eu/", portName = "InterSisPort")
public class InterSisServiceImpl implements InterSisEndpoint {

	/**
	 * This is the main method for authorizing a server, as defined in D3.1.
	 * 
	 * @param key The key as defined in D3.1.
	 * @return The response as defined in D3.1.
	 */
	@Override
	public boolean authorizeServer(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This is the main method for getting the ranked peer list.
	 * 
	 * @param req The request as defined in D3.1.
	 * @return The response as defined in D3.1.
	 */
	@Override
	public Response getRankedPeerList(Request req) {
		// TODO Auto-generated method stub
		return new Response();
	}

}
