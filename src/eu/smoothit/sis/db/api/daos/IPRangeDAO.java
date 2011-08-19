package eu.smoothit.sis.db.api.daos;

import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;

/**
 * DAO for managing IPRanges
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */
public interface IPRangeDAO extends Dao<Long, IPRangeConfigEntry> {

//	/**
//	 * 
//	 * @return an instance of the DTO IPRange
//	 */
//	public IPRangeConfigEntry createIPRangeEntry();

//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be persisted
//	 * @return true if the persistence process was successful. False otherwise
//	 */
//	public boolean addIPRange(IPRangeConfigEntry entry);

//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be updated
//	 */
//	public void updateIPRange(IPRangeConfigEntry entry);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled be the
//	 *            returned IPRange entries
//	 * @return a list of IPRange entries, that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public List<IPRangeConfigEntry> getIPRanges(IPRangeConfigEntry entry);

//	/**
//	 * 
//	 * @param offset
//	 *            the offset of the range
//	 * @param amount
//	 *            the number of entries to be returned
//	 * @return a list of IPRange entries, that match the given offset and amount
//	 */
//	public List<IPRangeConfigEntry> getAllIPRanges(int offset, int amount);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the IPRange
//	 *            entries that shall be removed
//	 * 
//	 */
//	public void removeIPRanges(IPRangeConfigEntry entry);

//	/**
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the IPRange
//	 *            entries that shall be counted
//	 * @return the number of IPRange entries that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public long count(IPRangeConfigEntry entry);

	// --------------------------------------------------
	// Deprecated methods
	// --------------------------------------------------

//	@Deprecated
//	public void addLocalIPRange(IPRangeConfigEntry range);
//
//	@Deprecated
//	public void addRemoteIPRange(IPRangeConfigEntry range);

//	@Deprecated
	/*
	 * *
	 * 
	 * @return all known ranges that belong to remote peers. Empty list returend
	 * if none stored!
	 */
//	public List<IPRangeConfigEntry> getRemoteIPRanges();

//	@Deprecated
	/*
	 * *
	 * 
	 * @return ranges that belong to this ISP. Empty list returend if none
	 * stored!
	 */
//	public List<IPRangeConfigEntry> getLocalIPRanges();

}
