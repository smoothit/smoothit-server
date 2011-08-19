package eu.smoothit.sis.controller.hap.impl;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.hap.HAPEndpoint;
import eu.smoothit.sis.controller.hap.HAPRating;
import eu.smoothit.sis.controller.hap.NeighborStats;
import eu.smoothit.sis.controller.hap.PeerStats;

/**
 * HAP Web Service Endpoint using XDoclet
 * 
 * @author Sergios Soursos, Intracom Telecom
 * 
 * @web.servlet 
 * 		name = "HAPEndpoint" 
 * 		display-name = "HAPEndpoint"
 * @web.servlet-mapping 
 * 		url-pattern = "/HAPEndpoint"                    
 */
@WebService(
		serviceName = "HAPService",
		endpointInterface = "eu.smoothit.sis.controller.hap.HAPEndpoint",
		targetNamespace = "http://hap.ws.sis.smoothit.eu/",
		portName = "HAPServicePort")
public class HAPEndpointImpl implements HAPEndpoint {
	
	@Resource
	WebServiceContext wsCtxt;
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(HAPEndpointImpl.class.getName());
	
	/**
	 * The HAP Rating service
	 */
	@EJB
	protected HAPRating rater;
	
	/**
	 * Default constructor
	 */
	public HAPEndpointImpl() {}
	
	/**
	 * Constructor that sets the HAP rating bean
	 * 
	 * @param rater The HAP Rating bean
	 */
	public HAPEndpointImpl(HAPRating rater){
		this.rater = rater;
	}

	/**
	 * Receives a report from a peer about the activity with its neighbors.
	 * 
	 * @param report The report of peer's activity with its neighbors.
	 */
	@Override
	@WebMethod
	@WebResult(name = "peerstats", targetNamespace = "http://hap.ws.sis.smoothit.eu/")
	public void reportActivity(PeerStats report) {
		
		log.info("reportActivity called");
		
		if (report != null) {
			// log.info("New activity report received");
			
			try{
				MessageContext msgCtxt = wsCtxt.getMessageContext();
				HttpServletRequest req = (HttpServletRequest)msgCtxt.get(MessageContext.SERVLET_REQUEST);
				String clientIP = req.getRemoteAddr();
				
				log.debug("Client's IP and port: " + clientIP + ":" + Integer.toString(report.getPort()));
				
				report.setIpAddress(clientIP);
				
			} catch (NullPointerException e){
				log.warn("Error in extracting IP address from client's message!");
			}			
			
			if ((report.getNeighbors() != null) && (report.getNeighbors().size() > 0))
				for (NeighborStats entry: report.getNeighbors())
					log.debug("neighbor IP: " + entry.getIpAddress() + ", upload: " + entry.getUpVolume() + 
							", download: " + entry.getDownVolume());
			
			this.rater.storePeerActivity(report);
		}
		
	}
	
	

}
