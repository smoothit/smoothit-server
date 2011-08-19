package eu.smoothit.sis.metering.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.metering.Metering;
import eu.smoothit.sis.metering.MeteringTimer;

/**
 * The Metering Timer implementation, using Java's EE TimerService resource.
 * It is used to periodically update the Metering status.
 *  
 * @author Peter Racz, Communications Systems Group (CSG), University of Zurich 
 */
@Stateless
public class MeteringTimerImpl implements MeteringTimer {
	
	private @Resource TimerService ts;

	private static Logger logger = Logger.getLogger(MeteringTimerImpl.class.getName());
	private static boolean isFirst = true;

	/**
	 * Reads the metering refresh rate from the database.
	 * 
	 * @return the refresh rate in seconds.
	 */
	private long getRefreshRate() {
		// Get the refresh rate from the DB
		IComponentConfigDAO dao = SisDAOFactory.getFactory().createComponentConfigDAO();
		long updateIntervall;
		
		ComponentConfigEntry config = dao.findByComponentAndName(Metering.COMPONENT_NAME, Metering.METERING_PROP_REFRESH_RATE);
		if(config == null || config.getValue() == null) {
			logger.warn(Metering.METERING_PROP_REFRESH_RATE + " not found in SIS DB. Using default value: " + Metering.DEFAULT_UPDATE_INTERVALL + "s");
			updateIntervall = Metering.DEFAULT_UPDATE_INTERVALL;
		} else {
			if(config.getValue() instanceof Integer) {
				updateIntervall = (Integer)config.getValue();
	    		logger.info(Metering.METERING_PROP_REFRESH_RATE + " = " + updateIntervall + "s");
			} else if(config.getValue() instanceof String) {
				logger.warn(Metering.METERING_PROP_REFRESH_RATE + " is a String");
				String tmp = (String)config.getValue();
				try {
					updateIntervall = Integer.valueOf(tmp);
					logger.info(Metering.METERING_PROP_REFRESH_RATE + " = " + updateIntervall + "s");    							
				} catch(Exception ex) {
		        	logger.warn("Invalid refresh rate: " + ex.getMessage() + ". Using default value: " + Metering.DEFAULT_UPDATE_INTERVALL + "s");
		        	updateIntervall = Metering.DEFAULT_UPDATE_INTERVALL;
				}
			} else {
				logger.warn(Metering.METERING_PROP_REFRESH_RATE + " is not an Integer. Using default value: " + Metering.DEFAULT_UPDATE_INTERVALL + "s");
		        updateIntervall = Metering.DEFAULT_UPDATE_INTERVALL;
			}
			
			if(updateIntervall != 0 && updateIntervall < Metering.MIN_UPDATE_INTERVALL) {
				updateIntervall = Metering.MIN_UPDATE_INTERVALL;
				logger.warn(Metering.METERING_PROP_REFRESH_RATE + " is smaller than " + Metering.MIN_UPDATE_INTERVALL + "s. Setting the refresh rate to " + updateIntervall + "s.");
			}
		}
		
    	updateIntervall *= 1000; // convert to milliseconds
		
		return updateIntervall;
	}
	

	/**
	 * Starts the metering timer with the name "MeteringTimer".
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void startTimer() {
		// stop any already existing instances
		stopTimer();
		
		ts.createTimer(5500, "MeteringTimer");
		logger.debug("MeteringTimer started.");
	}
	
	/**
	 * Cancels all timers with the name "MeteringTimer".
	 */
	@Override
	public void stopTimer() {		
		for(Object o : ts.getTimers())
			if (((Timer) o).getInfo().toString().equals("MeteringTimer")) {
				((Timer)o).cancel();
				logger.debug("MeteringTimer canceled.");
			}
	}

	/**
	 * The metering timeout handler that triggers the update of the metering status
	 * and restarts the metering timer with a possible new expiration time.
	 * 
	 * @param timer the timer that has expired.
	 */
	@Timeout
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void timeoutHandler(Timer timer) {

		if(timer.getInfo().toString().equals("MeteringTimer")){
			logger.debug("MeteringTimer expired.");
			
			stopTimer();

			long updateIntervall = getRefreshRate();
			boolean doRefresh = true;
			if(updateIntervall == 0) {
				updateIntervall = Metering.DEFAULT_UPDATE_INTERVALL * 1000;
				doRefresh = false;
			}
			ts.createTimer(updateIntervall, updateIntervall, "MeteringTimer");
			logger.debug("MeteringTimer restarted.");
		
			if(isFirst || doRefresh) {
				isFirst = false;
				// Update the Metering
				Metering.getInstance().reloadConfig();
				Metering.getInstance().refresh();
				Metering.getInstance().print(); // for testing purposes
			}
		}
	}

}
