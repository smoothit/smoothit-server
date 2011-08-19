package eu.smoothit.sis.security.access;

import java.util.List;

import javax.ejb.Local;

import eu.smoothit.sis.db.impl.entities.LogEntry;



/**
 * Defines a contract for AAAServer EJB component.
 * @author Tomasz Stradomski
 * @author Marcin Niemiec
 * @version 1.0.0
 */
@Local
public interface AAAServerLocal {
	/**
	 * Authorizes a user with given password. A password should be given in a plain text. 
	 * (checkRequestorID method will encode it with MD5 alghoritm)
	 * @param username a name of a user 
	 * @param password a plain text password
	 * @return true if a given password corresponds to a given username 
	 */
	public boolean checkRequestorID(String username, String password);

	/**
	 * Checks if a given user is associated with a given role.
	 * @param username a name if a user
	 * @param role a name of role
	 * @return true if a given user is associated with a given role.
	 */
	public boolean isCallerInRole(String username, String role);

	/**
	 * Add log entry into database. It should be used in order to log accounting information only.
	 * @param username a name of a user
	 * @param event description of an event
	 * @param result result of an event (suggested values: successful or unsuccessful)
	 */
	public void addLogEntry(String username, String event, String result);
	
	/**
	 * Gives a full list of logs that exist in database.
	 * @return a full list of logs
	 */
	public List<LogEntry> getLogEntries();
}
