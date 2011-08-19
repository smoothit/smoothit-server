package eu.smoothit.sis.qos.impl;

import java.util.List;

import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ejb.EJB;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.hap.HAPRating;
import eu.smoothit.sis.qos.HapResponse;
import eu.smoothit.sis.qos.QosEndpoint;

/**
 * QoS Web Service Endpoint using XDoclet
 * 
 * @author Marios Charalambides, Sergios Soursos, Intracom Telecom
 * 
 * @web.servlet 
 * 		name = "QosEndpoint" 
 * 		display-name = "QosEndpoint"
 * @web.servlet-mapping 
 * 		url-pattern = "/QosEndpoint"                    
 */
@WebService(
		serviceName = "QosService",
		endpointInterface = "eu.smoothit.sis.qos.QosEndpoint",
		targetNamespace = "http://qos.ws.sis.smoothit.eu/",
		portName = "QosServicePort")
public class QosEndpointImpl implements QosEndpoint{
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(QosEndpointImpl.class.getName());
	
	
	/**
	 * The HAP Rating service
	 */
	@EJB
	protected HAPRating hapRater;
	
	/**
	 * Default constructor
	 */
	public QosEndpointImpl() {}
	
	/**
	 * Constructor that sets the HAP rating bean
	 * 
	 * @param hapRater The HAP Rating bean
	 */
	public QosEndpointImpl(HAPRating hapRater){
		this.hapRater = hapRater;
	}
	
	/**
	 * Returns a structure containing the lists of promoted and demoted HAPs.
	 * 
	 * @return A merged list of HAP IPs.
	 */
	@Override
	@WebMethod
	@WebResult(name = "haps", targetNamespace = "http://qos.ws.sis.smoothit.eu/")
	public HapResponse getHAPs(boolean delta){
		
		log.info("getHAps called.");
		
		HapResponse res = new HapResponse();
		
		List<List<String>> tmp = hapRater.fetchHAPs(delta);
		if (tmp !=null && tmp.size() == 2){
			res.setPromoted(tmp.get(0));
			res.setDemoted(tmp.get(1));
		} else
			res = null;
		
		return res;
	}

}

