package eu.smoothit.sis.db.api.daos;

import java.util.List;

public interface Dao<K, E> {
	
	
	public boolean persist(E entity);
	
	public boolean update(E entity);

	public void remove(E entity);

//	public E findById(Object id);
	
	public List<E> getAll();
	
	public void removeAll();

	public Long countAll();
	
	public abstract List<E> get(E entity);
	
	public String printAll();
	
}
