package eu.smoothit.sis.db.impl.daos;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import eu.smoothit.sis.db.api.daos.Dao;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.IPersistenceManager;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage.ACTION;

public abstract class JpaDao<K, E> implements Dao<K, E> {
	protected Class<E> entityClass;

	protected IPersistenceManager pm;

	@SuppressWarnings("unchecked")
	public JpaDao(IPersistenceManager pm) {
		this.pm = pm;
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass
				.getActualTypeArguments()[1];
	}

	@Override
	public boolean persist(E entity) {
		return pm.persistAndNotify(entity, new DBNotifyMessage(entityClass
				.getName(), ACTION.PERSIST));
	}

	@Override
	public boolean update(E entity) {
		return pm.updateAndNotify(entity, new DBNotifyMessage(entityClass
				.getName(), ACTION.UPDATE));
	}
	
	
	
	@Override
	public void remove(E entity) {
		pm.removeAndNotify(entity, new DBNotifyMessage(entityClass.getName(),
				ACTION.REMOVE));
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public E findById(Object id) {
//		return (E) pm.find(entityClass, id);
//	}

	@SuppressWarnings("unchecked")
	public List<E> getAll() {
		return pm
				.queryDatabase("SELECT h FROM " + entityClass.getName() + " h");
	}

	@Override
	public void removeAll() {
		pm.removeSpecifiedObjects("SELECT h FROM " + entityClass.getName()
				+ " h", new DBNotifyMessage(entityClass.getName(),
				ACTION.REMOVE));
		// pm.removeAll(
		// "DELETE FROM " + entityClass.getName() + " h",
		// new DBNotifyMessage(entityClass.getName(), ACTION.DELETE));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long countAll() {
		List result = pm.queryDatabase("SELECT COUNT(h) FROM "
				+ entityClass.getName() + " h");
		return result != null && result.size() > 0 ? (Long) result.get(0)
				: Long.valueOf(0);
	}

	@Override
	public String printAll() {
		StringBuffer buf = new StringBuffer();
		buf.append("Properties:\n");
		for (E req : getAll()) {
			buf.append("\t" + req.toString() + "\n");
		}
		return buf.toString();
	}
	
}
