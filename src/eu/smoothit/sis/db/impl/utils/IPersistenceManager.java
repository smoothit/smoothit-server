package eu.smoothit.sis.db.impl.utils;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IPersistenceManager {

	/**
	 * 
	 * @param query
	 *            contains the conditions for entries that shall be removed
	 * @return a list containing the requested entries
	 */
	@SuppressWarnings("unchecked")
	public List queryDatabase(String query);
	
	
	public List queryDatabaseNatively(String query);

//	/**
//	 * 
//	 * @param query
//	 *            specifies the selected table
//	 * @param start
//	 *            the start of the range
//	 * @param amount
//	 *            the number of entries to be returned at most
//	 * @return a list containing the requested entries
//	 */
//	@SuppressWarnings("unchecked")
//	public List getElementsInRange(String query, int start, int amount);

	/**
	 * 
	 * @param qry
	 *            contains the conditions for entries that shall be removed
	 * @param msg
	 *            the notify message
	 */
	public void removeSpecifiedObjects(String qry, DBNotifyMessage msg);

	/**
	 * 
	 * @param object
	 *            the object to be persisted
	 * @param msg
	 *            the msg to be sent in order to notify the DBObservers
	 * @return true if the persistence process was successful, false otherwise
	 */
	public boolean persistAndNotify(Object object, DBNotifyMessage msg);

	/**
	 * merges an existing object to the database. If the old object is not
	 * stored within the database, merge creates a new object with the new
	 * values
	 * 
	 * @param object
	 *            contains the object for the database
	 * @param msg
	 *            contains the message for the notification-mechanism
	 */
	public boolean updateAndNotify(Object object, DBNotifyMessage msg);

	/**
	 * removes an object from the database
	 * 
	 * @param object
	 *            the object to be remove
	 */
	public void removeAndNotify(Object object, DBNotifyMessage msg);

//	public void removeAll(String qry, DBNotifyMessage msg);

//	@SuppressWarnings("unchecked")
//	public Object find(Class c, Object id);

//	public void enableTransactions(boolean flag);

}
