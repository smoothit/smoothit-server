package eu.smoothit.sis.security.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;


/**
 * Extends org.jboss.security.auth.spi.DatabaseServerLoginModule. Accounting
 * functionality added.
 * 
 * @see org.jboss.security.auth.spi.DatabaseServerLoginModule
 * @author Tomasz Stradomski
 * @version 1.0.0
 * 
 */
public class SisDatabaseServerLoginModule extends DatabaseServerLoginModule {

	/**
	 * Value of this field is read from "login-config.xml" file from
	 * isAccountingEnable module-option, for example <module-option
	 * name="isAccountingEnable">true</module-option> 
	 * If true, information about accounting is stored in database.
	 */
	protected boolean isAccountingEnable = false;
	/**
	 * Value for this field is read from "login-config.xml" file from
	 * insertIntoQuery module-option, for example <module-option
	 * name="logInsertQuery">INSERT INTO LOGS(date, event, result, username)
	 * VALUES (?, ?, ?, ?)</module-option> 
	 * It describes a query that should be performed on database in order to 
	 * store information about accounting.
	 */
	protected String insertIntoQuery;
	/**
	 * A date of last successful authorization. 
	 */
	private static Date lastSuccessfulAuthorization = new Date(1);

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		isAccountingEnable = Boolean.parseBoolean((String) options
				.get("isAccountingEnable"));
		insertIntoQuery = (String) options.get("logInsertQuery");
	}

	/**
	 * Performs normal login() operation, if isAccountingEnable is true it will 
	 * insert information about an event into database.
	 * @return true if logging process finished successfully
	 * @throws LoginException if logging process finished unsuccessfully
	 * @see org.jboss.security.auth.spi.UsernamePasswordLoginModule#login()
	 */
	@Override
	public boolean login() throws LoginException {
		if (!isAccountingEnable) {
			return super.login();
		} else {
			return loginWithAccounting();
		}
	}

	/* If caching of security credentials is disabled (DefaultCacheTimeout attribute
	 * in "jboss-service.xml" is set to 0), application server will call this method for
	 * 3 times. Because of only one accounting operation should be logged, the method checks
	 * a date of last authorization. If this date is at most one second smaller than the date 
	 * of method calling, the method will not log information about accounting. It should prevent
	 * from multiple logging of one event.   
	 * 
	 */
	private boolean loginWithAccounting() throws LoginException {
		boolean logStatus = false;
		try {
			logStatus = super.login();
		} catch (LoginException e) {
			logEvent("authorization", "unsuccessful", getUsername());
			lastSuccessfulAuthorization = new Date(1);
			throw e;
		}
		long lastLogDateInMs = lastSuccessfulAuthorization.getTime();
		if (new Date().after(new Date(lastLogDateInMs + 1000))) {
			logEvent("authorization", "successful", getUsername());
		}
		return logStatus;
	}

	/**
	 * Stores information about an event (with event date) in database.  
	 * @param event description of an event
	 * @param result describes if event has finished successfully or unsuccessfully
	 * @param user a name of user
	 */
	private void logEvent(String event, String result, String user) {
		InitialContext ctx = null;
		DataSource ds = null;
		Connection c = null;
		PreparedStatement ps = null;

		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(dsJndiName);
		} catch (NamingException e) {
			log.error("Unable to lookup " + dsJndiName, e);
		}

		try {
			c = ds.getConnection();
		} catch (SQLException e) {
			log.error("Unable to get connection with database", e);
		}
		try {
			ps = c.prepareStatement(insertIntoQuery);
			lastSuccessfulAuthorization = new Date();
			ps.setTimestamp(1, new java.sql.Timestamp(
					lastSuccessfulAuthorization.getTime()), Calendar
					.getInstance());
			ps.setString(2, event);
			ps.setString(3, result);
			ps.setString(4, user);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("Unable to execute statement on database", e);
		} finally {
			
			if(ps != null){
				try{
					ps.close();
				}catch(SQLException e){
					log.error("Unable to close a statement", e);
				}
			}
			if(c != null){
				try {
					c.close();
				} catch (SQLException e) {
					log.error("Unable to close a connection with database", e);
				}
			}
		}
	}
}
