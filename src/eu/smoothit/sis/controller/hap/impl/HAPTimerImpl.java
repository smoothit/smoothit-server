package eu.smoothit.sis.controller.hap.impl;

import java.util.Calendar;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.hap.HAPRating;
import eu.smoothit.sis.controller.hap.HAPTimer;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;

/**
 * The HAP Timer implementation, using Java's EE TimerService resource.
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 * 
 */

@Stateless
public class HAPTimerImpl implements HAPTimer{
	
	@Resource 
	private TimerService timer;
	
	@EJB
	private HAPRating rater;
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(HAPTimerImpl.class.getName());
	
	
	/**
	 * The default expiration period for the timer, in seconds.
	 */
	private static int default_t_update = 24 * 60 * 60; // 1 day
	
	/**
	 * The default start period for the timer, in hour of day (24h format).
	 */
	private static int default_t = 3;
	private static int default_t_mins = 0; // for precision purposes
	
	/**
	 * The default mode of the HAP ETM (enabled).
	 */
	private boolean hap_on = true;
	
	/**
	 * Reads from the DB the respective component configuration parameters and overwrites the
	 * default value of T_UPDATE, if new value exist in the DB.
	 */
	private void defineTUPDATE(){
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		ComponentConfigEntry config = dao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_HAP, SisWebInitializer.PARAM_HAP_CONTROLLER_T_UPDATE);
		
		if(config != null && config.getValue() != null && config.getValue() instanceof String)
			default_t_update = Integer.valueOf((String) config.getValue()).intValue();
		
		config = dao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_HAP, SisWebInitializer.PARAM_HAP_CONTROLLER_T);
		
		if(config != null && config.getValue() != null && config.getValue() instanceof String){
			default_t = Integer.valueOf((String) config.getValue()).intValue() / 60; // convert minutes to hours
			default_t_mins = Integer.valueOf((String) config.getValue()).intValue() % 60; // get remaining minutes
		}
		
		config = dao.findByComponentAndName(SisWebInitializer.COMPONENT_NAME_CONTR_HAP, SisWebInitializer.PARAM_HAP_CONTROLLER_ON_OFF);
		
		if(config != null && config.getValue() != null && config.getValue() instanceof String)
			hap_on = Boolean.valueOf((String) config.getValue()).booleanValue(); 
	}
	
	/**
	 * Creates a single timer in the TimerService.
	 * 
	 * @param update Defines whether the timer is updated (true) or created (false).
	 * 
	 */
	private void createSingleTimer(boolean update){
		
		Calendar now = Calendar.getInstance();
		
		if (hap_on){		
			Calendar working = (Calendar) now.clone();
			working.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), default_t, default_t_mins,0);
			long offset = working.getTimeInMillis() - now.getTimeInMillis();
			if (offset <= 0) {
				working.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH)+1);
				offset = working.getTimeInMillis() - now.getTimeInMillis();
			}
		
			timer.createTimer(offset , default_t_update * 1000 , "HAPTimer");
			if (!update)
				log.info("Timer started at " + now.getTime().toString() + " with T=" + default_t + ":" + default_t_mins +" and T_UPDATE=" + default_t_update);
			else
				log.info("Timer updated at " + now.getTime().toString() + " with T=" + default_t + ":" + default_t_mins +" and T_UPDATE=" + default_t_update);
		} else {
			timer.createTimer(2 * 60 * 60 * 1000 , "HAPTimer"); // expire in two hours
			if (!update)
				log.info("Timer started at " + now.getTime().toString() + " with expiration in 2 hours.");
			else
				log.info("Timer updated at " + now.getTime().toString() + " with expiration in 2 hours.");
		}
	}
	
	
	/**
	 * Starts the timer "HAPTimer". First is retrieves the initialization parameters from the DB,
	 * then it stops any running (old) timers and finally it creates the new timer.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void startTimer() {
		
		defineTUPDATE();
		stopTimer();
		
		createSingleTimer(false);
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
		
		if (timer.getInfo().toString().equals("HAPTimer")){
			
			log.info("HAPTimer expired!");
			
			long timeRemain = timer.getTimeRemaining();
			int old_t_update = default_t_update * 1000;
			
			rater.calcHAPRatings();
			
			defineTUPDATE();
			
			if (Math.abs((default_t_update * 1000) - timeRemain) > 0.05 * old_t_update){
				stopTimer();
				createSingleTimer(true);
			}				
		}
	}
	
	/**
	 * Deletes (cancels) all timers with name "HAPTimer".
	 */
	@Override
	public void stopTimer() {
		for (Object o : this.timer.getTimers())
			if (((Timer) o).getInfo().toString().equals("HAPTimer")){
				((Timer)o).cancel();
				log.debug("HAPTimer's instance canceled.");
			}
	}
	
	

}
