package eu.smoothit.sis.db.api.daos;

import java.util.List;

import eu.smoothit.sis.db.impl.entities.PeerStatisticsEntry;

/**
 * Simplest DAO. Can store generic properties for any component.
 * 
 * @author Christian Gross, KOM, TU Darmstadt
 * 
 */
public interface IPeerStatisticsDAO extends Dao<Long, PeerStatisticsEntry> {


	public List<List<String>> getPeers();  
	
	
	
	
}
