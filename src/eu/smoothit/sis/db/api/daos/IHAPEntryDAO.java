package eu.smoothit.sis.db.api.daos;

import java.util.List;

import eu.smoothit.sis.db.impl.entities.HAPEntry;

/**
 * Simplest DAO. Can store generic properties for any component.
 * 
 * @author Christian Gross, KOM, TU Darmstadt
 * 
 */
public interface IHAPEntryDAO extends Dao<Long, HAPEntry> {

	List<List<String>> getPromotedHAPs();

	List<List<String>> getNewHAPs();

	
	
}
