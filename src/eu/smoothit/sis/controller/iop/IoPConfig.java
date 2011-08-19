package eu.smoothit.sis.controller.iop;

import javax.ejb.Local;

/**
 * The local interface for the IoP configuration service 
 * implemented as an EJB3 session bean
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 *                  
 */
@Local
public interface IoPConfig {
	
	/**
	 * Prepares a list of configuration parameters for the operation of the IoP
	 * 
	 *  @param ipAddress The IP address of the requesting IoP.
	 *  
	 *  @return The list of configuration parameters.
	 *         
	 */
	public ConfigParams getIoPConfigParams(String ipAddress);

}
