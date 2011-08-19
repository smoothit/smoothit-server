package eu.smoothit.sis.controller.iop.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.iop.DBTimer;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IClientSwarmInfoDAO;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;

/**
 * The DB Timer implementation, using Java's EE TimerService resource.
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 * 
 */

@Stateless
public class DBTimerImpl implements DBTimer{
	private @Resource TimerService timer;
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(DBTimerImpl.class.getName());
	
	/**
	 * The default expiration period for the timer, in seconds.
	 */
	private static int default_t_out = 300; // 5 mins
	
	/**
	 * The default age above which entries from the DB are removed, in seconds.
	 */
	private static long default_t_age = 1800; // 30 mins
	
	
	/**
	 * Starts the timer "DBTimer". First is retrieves the initialization parameters from the DB,
	 * then it stops any running (old) timers and finally it creates the new timer.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void startTimer() {
		
		defineTOUTAGE();
		stopTimer();
		createSingleTimer(default_t_out);
	
		java.util.Date now = new java.util.Date();
		log.debug("Timer started at " + now.toString() + " with T_OUT=" + default_t_out + " and T_AGE=" + ((Long)default_t_age).toString());

	}
	
	/**
	 * Reads from the DB the respective component configuration parameters and overwrites the
	 * default value of T_OUT and T_AGE, if new values exist in the DB.
	 */
	private void defineTOUTAGE(){
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		ComponentConfigEntry config = dao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_IOP, SisWebInitializer.PARAM_IOP_CONTROLLER_T_OUT);
		
		if(config != null && config.getValue() != null && config.getValue() instanceof String)
			default_t_out = Integer.valueOf((String) config.getValue()).intValue();
		
		config = dao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_IOP, SisWebInitializer.PARAM_IOP_CONTROLLER_T_AGE);
		
		if(config != null && config.getValue() != null && config.getValue() instanceof String)
			default_t_age = Integer.valueOf((String) config.getValue()).intValue();
	}
	
	/**
	 * Creates a single timer in the TimerService.
	 * 
	 * @param i The timeout period of the timer.
	 */
	private void createSingleTimer(int i){
		timer.createTimer(i * 1000 , i * 1000, "DBTimer");
	}
	
	/**
	 * Deletes entries with timestamp older than aged from the table clientSwarmInfo. 
	 * 
	 * @param aged The threshold of the entries' timestamp to be deleted.
	 */
	private void deleteOldEntries(long aged){
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IClientSwarmInfoDAO dao = factory.createClientSwarmInfoDAO();
	
		dao.deleteSwarmsOlderThen(aged);
	}
	
	/**
	 * Upon expiration of the timer, old entries from the DB are deleted and 
	 * new timer is created if its initialization info has changed. 
	 * 
	 * @param timer The timer that is expired.
	 */
	@Timeout
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void timeoutHandler(Timer timer) {
		
		if (timer.getInfo().toString().equals("DBTimer")){
			long timeRemain = timer.getTimeRemaining();
			int old_t_out = default_t_out * 1000;
			
			java.util.Date now = new java.util.Date();
			long aged = now.getTime() - (default_t_age * 1000);
						
			deleteOldEntries(aged);
			log.debug("Timer expired at " + now.toString());
			
			defineTOUTAGE();
			
			// If stored t_out value is absolute different more than 5% of the used t_out value then
			// stop current timer and create a new one
			if (Math.abs((default_t_out * 1000) - timeRemain) > 0.05 * old_t_out){
				stopTimer();
				createSingleTimer(default_t_out);
			
				log.debug("Timer updated at " + now.toString() + " with T_OUT=" + default_t_out + " and T_AGE=" + ((Long)default_t_age).toString());
			}
				
		}
	}
	
	/**
	 * Deletes (cancels) all timers with name "DBTimer".
	 */
	@Override
	public void stopTimer() {
		for (Object o : this.timer.getTimers())
			if (((Timer) o).getInfo().toString().equals("DBTimer")){
				((Timer)o).cancel();
				log.debug("DBTimer's instance canceled.");
			}
	}

}
