package eu.smoothit.sis.security.access;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.security.auth.spi.Util;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.impl.entities.LogEntry;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;

/**
 * AAAServerBean class is an implementation of sis-security component.
 * 
 * @author Tomasz Stradomski
 * @version 1.0.0
 */
@Stateless
@Local(AAAServerLocal.class)
public class AAAServerBean implements AAAServerLocal {

	/*
	 * @see
	 * eu.smoothit.sis.security.aaa.access.AAAServerRemote#addLogRecord(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLogEntry(String username, String event, String result) {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		ILogEntryDAO logEntryDao = factory.createLogEntryDAO();
		
		LogEntry logEntry = new LogEntry();
		logEntry.setUsername(username);
		logEntry.setEvent(event);
		logEntry.setResult(result);
		
		logEntryDao.persist(logEntry);
	}

	/*
	 * @see
	 * eu.smoothit.sis.security.aaa.access.AAAServerRemote#checkRequestorID(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkRequestorID(String username, String password) {

		String hashedPassword = Util.createPasswordHash("MD5",
				Util.BASE64_ENCODING, null, null, password);

		SisDAOFactory factory = SisDAOFactory.getFactory();
		IUserDAO userDao = factory.createUserDAO();
		
		User userToFind = new User();
		userToFind.setUsername(username);
		userToFind.setPassword(hashedPassword);
		
		List<User> list = userDao.get(userToFind);
		return (list.size()==1);
	}

	/*
	 * @see
	 * eu.smoothit.sis.security.aaa.access.AAAServerRemote#isCallerInRole(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public boolean isCallerInRole(String username, String role) {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IUserDAO userDao = factory.createUserDAO();
		User userToFind = new User();
		userToFind.setUsername(username);
		userToFind.setPassword(null);
		List<User> list = userDao.get(userToFind);
		return (list.size() == 1) && hasRole(list.get(0), role);
	}

	@Override
	public List<LogEntry> getLogEntries() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		ILogEntryDAO logEntryDao = factory.createLogEntryDAO();
		logEntryDao.getAll();
		return logEntryDao.getAll();
	}

	private boolean hasRole(User user, String role) {
		for (UserRole userRole : user.getUserRoles()) {
			if (userRole.getRole().equals(role)) {
				return true;
			}
		}
		return false;
	}
}
