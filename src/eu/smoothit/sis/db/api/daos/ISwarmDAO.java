package eu.smoothit.sis.db.api.daos;

import eu.smoothit.sis.db.impl.entities.SwarmEntityEntry;

public interface ISwarmDAO extends Dao<Long, SwarmEntityEntry> {

//	/**
//	 * 
//	 * @return an instance of the DTO ISwarmEntity
//	 */
//	public SwarmEntityEntry createSwarmEntityEntry();

	
//	/**
//	 * 
//	 * @return an instance of the DTO IClientRequest
//	 */
//	public ClientRequestEntry createClientRequestEntry();
	
//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be persisted
//	 * @return true if the persistence process was successful. False otherwise
//	 */
//	public boolean addSwarmEntityEntry(SwarmEntityEntry entry);

//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be persisted
//	 * @return true if the persistence process was successful. False otherwise
//	 */
//	public boolean addClientRequestEntry(ClientRequestEntry entry);
	
//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be updated
//	 */
//	public void updateSwarmEntityEntry(SwarmEntityEntry entry);
	
	
//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be updated
//	 */
//	public void updateClientRequestEntry(ClientRequestEntry entry);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled be the
//	 *            returned ISwarmEntity entries
//	 * @return a list of ISwarmEntity entries, that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public List<SwarmEntityEntry> getSwarmEntities(SwarmEntityEntry entry);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled be the
//	 *            returned IClientRequest entries
//	 * @return a list of IClientRequest entries, that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public List<ClientRequestEntry> getClientRequests(ClientRequestEntry entry);
	
//	/**
//	 * 
//	 * @param offset
//	 *            the offset of the range
//	 * @param amount
//	 *            the number of entries to be returned
//	 * @return a list of ISwarmEntity entries, that match the given offset and amount
//	 */
//	public List<SwarmEntityEntry> getAllSwarmEntities(int offset, int amount);
	
	
//	/**
//	 * 
//	 * @param offset
//	 *            the offset of the range
//	 * @param amount
//	 *            the number of entries to be returned
//	 * @return a list of IClientRequest entries, that match the given offset and amount
//	 */
//	public List<ClientRequestEntry> getAllClientRequests(int offset, int amount);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the ISwarmEntity
//	 *            entries that shall be removed
//	 * 
//	 */
//	public void removeSwarmEntities(SwarmEntityEntry entry);
	
//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the IClientRequest
//	 *            entries that shall be removed
//	 * 
//	 */
//	public void removeClientRequests(ClientRequestEntry entry);

//	/**
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the ISwarmEntity
//	 *            entries that shall be counted
//	 * @return the number of ISwarmEntity entries that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public long count(SwarmEntityEntry entry);
	
	
//	/**
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the IClientRequest
//	 *            entries that shall be counted
//	 * @return the number of IClientRequest entries that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public long count(ClientRequestEntry entry);
	
	
}
