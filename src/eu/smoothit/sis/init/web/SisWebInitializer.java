package eu.smoothit.sis.init.web;

import java.io.IOException;
import java.util.Properties;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import eu.smoothit.sis.controller.hap.HAPTimer;
import eu.smoothit.sis.controller.iop.DBTimer;
import eu.smoothit.sis.metering.MeteringTimer;
import eu.smoothit.sis.security.login.AccountCreator;

/**
 * Basic Servlet Listener Using XDoclet.
 * 
 * @author Michael Makidis, Sergios Soursos, Intracom Telecom
 * @version 1.0
 * 
 * @web.listener                  
 */
public class SisWebInitializer implements ServletContextListener {
	
	public static final String COMPONENT_NAME_CONTR_IOP = "Controller-IoP-Config";
	
	public static final String PARAM_IOP_OPERATION_MODE = "IoP-Operation-Mode";
	public static final String VALUE_IOP_OPERATION_MODE_PLAIN = "Plain";
	public static final String VALUE_IOP_OPERATION_MODE_COMBO = "Combination";
	public static final String VALUE_IOP_OPERATION_MODE_COLLAB = "Collaboration";
	
	public static final String PARAM_IOP_REMOTE_CONNECTION_FLAG = "IoP-Remote-Connection-Flag";
	public static final String PARAM_IOP_LOCAL_IP_RANGES = "IoP-Local-IP-Ranges";
	public static final String PARAM_IOP_UNCHOKING_SLOTS = "IoP-Unchoking-Slots";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_T = "IoP-SIS-Swarm-Selection-T";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_ULOW = "IoP-SIS-Swarm-Selection-ULow";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_DLOW = "IoP-SIS-Swarm-Selection-DLow";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_U = "IoP-SIS-Swarm-Selection-U";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_D = "IoP-SIS-Swarm-Selection-D";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_X = "IoP-SIS-Swarm-Selection-X";
	
	public static final String PARAM_IOP_CONTROLLER_T_OUT = "IoP-Controller-T-Out";
	public static final String PARAM_IOP_CONTROLLER_T_AGE = "IoP-Controller-T-Age";
	
	// definitions for Metering
	public static final String COMPONENT_NAME_METERING = "Metering";
	public static final String METERING_PROP_REFRESH_RATE = "Metering-Refresh-Rate";
	// Sources for both remote and local options
	public static final String METERING_VAL_IP_INFO_SRC_DB = "DB";
	public static final String METERING_VAL_IP_INFO_SRC_ROUTER = "Router";
	public static final String METERING_VAL_IP_INFO_SRC_FILE = "File";
	// Remote and local file names
	public static final String METERING_PROP_BGP_FILE_NAME = "Metering-BGP-File-Name";
	public static final String METERING_PROP_LOCAL_FILE_NAME = "Metering-BGP-Local-File-Name";
	// Remote and local router Address, port,SNMP.
	//XX_BGP_XX:remote
	//XX_OSPF_XX: local
	public static final String METERING_PROP_BGP_ROUTER_ADDRESS = "Metering-BGP-Router-Address";
	public static final String METERING_PROP_BGP_ROUTER_PORT = "Metering-BGP-Router-Port";
	public static final String METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY = "Metering-BGP-Router-SNMP-Community";
	public static final String METERING_PROP_OSPF_ROUTER_ADDRESS = "Metering-OSPF-Router-Address";
	public static final String METERING_PROP_OSPF_ROUTER_PORT = "Metering-OSPF-Router-Port";
	public static final String METERING_PROP_OSPF_ROUTER_SNMP_COMMUNITY = "Metering-OSPF-Router-SNMP-Community";
	
	
	public static final String COMPONENT_NAME_CONTR_HAP = "Controller-HAP-Config";
	
	public static final String PARAM_HAP_CONTROLLER_ON_OFF = "HAP-Controller-On-Off";
	public static final String PARAM_HAP_BILLING_URL = "HAP-Billing-URL";
	public static final String PARAM_HAP_CONTROLLER_T_UPDATE = "HAP-Controller-T-Update";
	public static final String PARAM_HAP_CONTROLLER_T = "HAP-Controller-T";
	public static final String PARAM_HAP_CONTROLLER_N = "HAP-Controller-N";
	public static final String PARAM_HAP_CONTROLLER_P1 = "HAP-Controller-P1";
	public static final String PARAM_HAP_CONTROLLER_P2 = "HAP-Controller-P2";
	public static final String PARAM_HAP_CONTROLLER_P3 = "HAP-Controller-P3";
	public static final String PARAM_HAP_CONTROLLER_AD = "HAP-Controller-Available-Download";
	public static final String PARAM_HAP_CONTROLLER_AU = "HAP-Controller-Available-Upload";
	public static final String PARAM_HAP_CONTROLLER_DI = "HAP-Controller-Download-Increase";
	public static final String PARAM_HAP_CONTROLLER_UI = "HAP-Controller-Upload-Increase";
	public static final String PARAM_HAP_CONTROLLER_RT = "HAP-Controller-Rating-Threshold";	
	
	/**
	 * Logger.
	 */
	private static org.apache.log4j.Logger log = Logger
    	.getLogger(SisWebInitializer.class.getName());
	
	/**
	 * The Timer Service
	 */
	@EJB
	protected DBTimer dbtimer;
	
	@EJB
	private MeteringTimer mtimer;
	
	@EJB
	private HAPTimer htimer;

	/**
	 * Displays project name, version and build number upon initialization.
	 * 
	 * @param event The ServletContextEvent of this listener.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		Properties prop = getProperties(event);

		log.info("SmoothIT Information Service (SIS) version "
				+ prop.getProperty("Specification-Version") + " built "
				+ prop.getProperty("Built-Date") + " starting up.");
		
		AccountCreator.addAcount("admin", "admin", "JBossAdmin", "Roles");

		try {
			dbtimer.startTimer();
		} catch(Exception e) {
			log.error("Error starting DB timer: " + e.getMessage());
		}

		try {
			mtimer.startTimer();
		} catch(Exception e) {
			log.error("Error starting Metering timer: " + e.getMessage());
		}
		
		try {
			htimer.startTimer();
		} catch(Exception e) {
			log.error("Error starting HAP timer: " + e.getMessage());
		}

	}
	
	/**
	 * Displays project name, version and build number upon shut down.
	 * 
	 * @param event The ServletContextEvent of this listener.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Properties prop = getProperties(event);
		
		try {
			dbtimer.stopTimer();
		} catch(Exception e) {
			log.error("Error stopping DB timer: " + e.getMessage());
		}

		try {
			mtimer.stopTimer();
		} catch(Exception e) {
			log.error("Error stopping Metering timer: " + e.getMessage());
		}
		
		try {
			htimer.stopTimer();
		} catch(Exception e) {
			log.error("Error stopping HAP timer: " + e.getMessage());
		}
		
		log.info("SmoothIT Information Service (SIS) version "
				+ prop.getProperty("Specification-Version") + " built "
				+ prop.getProperty("Built-Date") + " shutting down.");
	}

	/**
	 * Loads assembly (JAR) properties from the manifest file.
	 * 
	 * @param event The ServletContextEvent of this listener.
	 * @return The properties that have been loaded from the manifest.
	 */
	protected Properties getProperties(ServletContextEvent event) {
		Properties prop = new Properties();
		try {
			prop.load(event.getServletContext().
					getResourceAsStream("/META-INF/MANIFEST.MF"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

}
