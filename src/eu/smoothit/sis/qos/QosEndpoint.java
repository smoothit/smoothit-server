package eu.smoothit.sis.qos;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * The Web Service interface for the Network client.
 * 
 * @author Marios Charalambides, Sergios Soursos, Intracom Telecom
 */

@WebService(name = "QosPort", targetNamespace = "http://qos.ws.sis.smoothit.eu/")
public interface QosEndpoint {
	 /**
	 * Returns a structure containing the lists of promoted and demoted HAPS.
	 * 
	 * @return a merged list of HAP IPs.
	 */
	@WebMethod
	@WebResult(name = "haps", targetNamespace = "http://qos.ws.sis.smoothit.eu/")
	public HapResponse getHAPs(boolean delta);

}
