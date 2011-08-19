package eu.smoothit.sis.controller.peerrating.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.peerrating.ClientEndpoint;
import eu.smoothit.sis.controller.peerrating.PeerRating;
import eu.smoothit.sis.controller.peerrating.Request;
import eu.smoothit.sis.controller.peerrating.RequestEntry;
import eu.smoothit.sis.controller.peerrating.Response;
import eu.smoothit.sis.controller.peerrating.ResponseEntry;

/**
 * Client Web Service Endpoint using XDoclet.
 * 
 * @author Michael Makidis, Intracom Telecom
 * @version 1.1
 * 
 * @web.servlet 
 * 		name = "ClientEndpoint" 
 * 		display-name = "ClientEndpoint"
 * @web.servlet-mapping 
 * 		url-pattern = "/ClientEndpoint"    
 *                  
 */

@WebService(
		serviceName = "ClientService",
		endpointInterface = "eu.smoothit.sis.controller.peerrating.ClientEndpoint",
		targetNamespace = "http://client.ws.sis.smoothit.eu/",
		portName = "ClientServicePort")
public class ClientEndpointImpl implements ClientEndpoint {
	
	/**
	 * The Peer Rating service.
	 */
	@EJB
	protected PeerRating rater;

	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(ClientEndpointImpl.class.getName());
	
	/**
	 * Default constructor - initializes Rating Bean
	 */
	public ClientEndpointImpl()
	{
		/*try {
			InitialContext ic = new InitialContext();
			rater = (PeerRating) ic.lookup("sis/PeerRatingImpl/local");
		} catch (NamingException e) {
			log.error("SIS-Client WS JNDI init error");
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Constructor that sets
	 * 
	 * @param rater The Rating bean
	 */
	public ClientEndpointImpl(PeerRating rater) {
		this.rater = rater;
	}

	/**
	 * This is a dummy method to test the functionality
	 * of the Web Service.
	 * Note: this method may be removed in a future release.
	 *
	 * @return	The sum of the two params.
	 */
	@Deprecated
	@Override
	@WebMethod
	@WebResult(name = "sum", targetNamespace = "http://client.ws.sis.smoothit.eu/")
	public int add(int i, int k) {
		log.warn("Adding: " + i + " " + k + " -- method is depreciated");
		return i + k;
	}

	/**
	 * This is the main method for getting the ranked peer list using the
	 * generic ETM implementation.
	 * 
	 * @param req 	The request as defined in D3.1.
	 * @return	The response as defined in D3.1.
	 */
	@Override
	@WebMethod
	@WebResult(name = "response", targetNamespace = "http://client.ws.sis.smoothit.eu/")
	public Response getRankedPeerList(Request req) {
		Response response = new Response();
		
		if (req.getEntries() != null) {
			log.debug("Client WS request received");
			
			List<ResponseEntry> responseEntries = 
				rater.rankAddresses(req.getEntries());
			response.setEntries(responseEntries);
			
			log.debug("Client WS response sent");
		}

		return response;
	}
	
	/**
	 * This is a simple method for request and response that returns
	 * a peer list with even-numbered IP addresses as preferred ones.
	 * Note: this method may be removed in a future release.
	 * 
	 * @param req 	The request as defined in D3.1.
	 * @return	The response as defined in D3.1.
	 */
	@Deprecated
	@WebMethod
	@Override
	@WebResult(name = "response", targetNamespace = "http://client.ws.sis.smoothit.eu/")
	public Response getSimpleRankedPeerList(Request req) {
		

		Response response = new Response();

		ArrayList<ResponseEntry> responseEntries = new ArrayList<ResponseEntry>();
		if (req.getEntries() != null){
			log.info("Simple Client WS Request Received");
			for (RequestEntry reqEntry: req.getEntries()){
				ResponseEntry responseEntry = new ResponseEntry();
				responseEntry.setExtentions(reqEntry.getExtentions());
				responseEntry.setIpAddress(reqEntry.getIpAddress());
				if (Long.valueOf(reqEntry.getIpAddress().replace(".", "")) % 2 == 0){
					responseEntry.setPreference(100);
				}else{
					responseEntry.setPreference(0);
				}
				log.debug("IP: " + responseEntry.getIpAddress() + ", Preference: " + responseEntry.getPreference() );
				responseEntries.add(responseEntry);
			}
			log.info("Simple Client WS Response Sent");
		}

		response.setEntries(responseEntries);
		return response;
	}

}