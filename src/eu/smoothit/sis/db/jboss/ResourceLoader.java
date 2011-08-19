package eu.smoothit.sis.db.jboss;

import java.io.InputStream;

import org.apache.log4j.Logger;



/**
 * Class loads resources (e.g. conf files) from war file
 * @author Jonas Flick, KOM, TU Darmstadt
 * @deprecated Not used currently
 */
public class ResourceLoader 
{
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger.getLogger(ResourceLoader.class.getName());
	
	/**
	 * Just for testing purposes...
	 */
	public static boolean testLoadingConfigFile() 
	{
		log.info("Trying to load Config file...");
		final InputStream result = loadConfFile();
		log.info(result);
		log.info("Done loading config file!");
		return result !=null;
	}
	
	/**
	 * Loads default config file
	 * @return stream containing config contents
	 */
	public static InputStream loadConfFile()
	{
		return loadResource("/setup.conf");
	}
	
	/**
	 * Loads a resource from the war file when deployed on JBoss
	 * Resources must be placed in WEB-INF and accessed with preceeding "/" or they won't be found!!!
	 * @param resName - e.g. /setup.conf
	 * @return stream containing resource contents
	 */
	public static InputStream loadResource(String resName)
	{
		log.info("Loading Resource from war file: "+resName);
		InputStream result = null;
		
		result = ResourceLoader.class.getResourceAsStream(resName);
		
		return result;
	}

}
