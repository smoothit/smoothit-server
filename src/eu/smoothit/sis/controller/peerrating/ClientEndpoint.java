package eu.smoothit.sis.controller.peerrating;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * The Web Service interface for the SIS client.
 * 
 * @author Michael Makidis, Intracom Telecom
 */
@WebService(name = "SisClientPort", targetNamespace = "http://client.ws.sis.smoothit.eu/")
public interface ClientEndpoint {

	/**
	 * Computes and returns the ranked peer list.
	 * 
	 * @param req The request as defined in D3.1.
	 * @return The response as defined in D3.1.
	 */
	@WebMethod
	@WebResult(name = "response", targetNamespace = "http://client.ws.sis.smoothit.eu/")
	Response getRankedPeerList(Request req);

	/**
	 * This is a dummy method to test the functionality
	 * of the Web Service.
	 * Note: this method may be removed in a future release.
	 *
	 * @return	The sum of the two params.
	 */
	@WebMethod
	@WebResult(name = "sum", targetNamespace = "http://client.ws.sis.smoothit.eu/")
	@Deprecated
	int add(int i, int k);

	/**
	 * This is a simple method for request and response that returns
	 * a peer list with even-numbered IP addresses as preferred ones.
	 * Note: this method may be removed in a future release.
	 * 
	 * @param req 	The request as defined in D3.1.
	 * @return	The response as defined in D3.1.
	 */
	@WebMethod
	@WebResult(name = "response", targetNamespace = "http://client.ws.sis.smoothit.eu/")
	@Deprecated
	Response getSimpleRankedPeerList(Request req);

}
