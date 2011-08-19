package eu.smoothit.sis.controller.iop;

import javax.ejb.Local;

/**
 * The local interface for the timer used by the Controller 
 * implemented as an EJB3 session bean
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 *                  
 */
@Local
public interface DBTimer {
	
	/**
	 * Starts the timer.  
	 */
	public void startTimer();
	
	/**
	 * Stops the timer.  
	 */
	public void stopTimer();

}
