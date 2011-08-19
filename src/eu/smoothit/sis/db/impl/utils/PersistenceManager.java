package eu.smoothit.sis.db.impl.utils;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;

/**
 * PeristenceManager manages access to the PersistentContext.
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */
@Stateless
@Local(IPersistenceManager.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersistenceManager implements IPersistenceManager {

	private final static Logger logger = Logger
			.getLogger(PersistenceManager.class);

	@PersistenceContext(unitName = "sisdb")
	EntityManager manager;

//	private boolean transactions;

	public PersistenceManager() {
		logger.info("Persistence Manager created");
	}

//	@SuppressWarnings("unchecked")
//	public Object find(Class c, Object id) {
//		return manager.find(c, id);
//	}

	/**
	 * 
	 * @param query
	 *            contains the conditions for entries that shall be removed
	 * @return a list containing the requested entries
	 */
	@SuppressWarnings("unchecked")
	public List queryDatabase(String query) {
		logger.debug(query);
		List list = manager.createQuery(query).getResultList();
		return list;
	}

	
	@SuppressWarnings("unchecked")
	public List queryDatabaseNatively(String query) {
		logger.debug(query);
		List list = manager.createNativeQuery(query).getResultList();
		return list;
	}
	
	
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
//	public List getElementsInRange(String query, int start, int amount) {
//		Query q = manager.createQuery(query);
//		q.setFirstResult(start);
//		q.setMaxResults(amount);
//		return q.getResultList();
//	}

	/**
	 * 
	 * @param qry
	 *            contains the conditions for entries that shall be removed
	 * @param msg
	 *            the notify message
	 */
	@SuppressWarnings("unchecked")
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSpecifiedObjects(String qry, DBNotifyMessage msg) {
//		if (transactions)
//			manager.getTransaction().begin();
		List list = queryDatabase(qry);
		for (int i = 0; i < list.size(); i++) {
			manager.remove(list.get(i));
		}
//		if (transactions)
//			manager.getTransaction().commit();
		notifyDBObservers(msg);
	}

//	public void removeAll(String qry, DBNotifyMessage msg) {
//		manager.createQuery(qry).executeUpdate();
//		notifyDBObservers(msg);
//
//	}

	/**
	 * 
	 * @param object
	 *            the object to be persisted
	 * @param msg
	 *            the msg to be sent in order to notify the DBObservers
	 * @return true if the persistence process was successful, false otherwise
	 */
	public boolean persistAndNotify(Object object, DBNotifyMessage msg) {
		manager.persist(object);
		notifyDBObservers(msg);
		return true;
	}

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
	public boolean updateAndNotify(Object object, DBNotifyMessage msg) {
		manager.merge(object);
		notifyDBObservers(msg);
		return true;

	}

	/**
	 * removes an object from the database
	 * 
	 * @param object
	 *            the object to be remove
	 */
	public void removeAndNotify(Object object, DBNotifyMessage msg) {
		manager.remove(manager.merge(object));
		notifyDBObservers(msg);
	}

	/**
	 * notify all SISDBObservers
	 * 
	 * @param msg
	 *            the notify message
	 */
	private void notifyDBObservers(DBNotifyMessage msg) {
		SISDBNotifier notifier = SisDAOFactory.getFactory()
				.createSISDBNotifier();
		notifier.setChanged();
		notifier.notifyObservers(msg);
	}

//	@Override
//	public void enableTransactions(boolean flag) {
////		this.transactions = flag;
//	}

}
