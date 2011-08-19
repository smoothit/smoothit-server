package eu.smoothit.sis.db.api.daos;

import eu.smoothit.sis.db.impl.entities.LinkConfigEntry;

/**
 * DAO for managing Link configurations
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 */
public interface ILinkConfigDAO extends Dao<Long, LinkConfigEntry> {

//	/**
//	 * 
//	 * @return an instance of the DTO LinkProperties
//	 */
//	public LinkConfigEntry createLinkPropertiesEntry();
//
//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be persisted
//	 * @return true if the persistence process was successful. False otherwise
//	 */
//	public boolean addLinkPropertiesEntry(LinkConfigEntry entry);
//
//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be updated
//	 */
//	public void updateLinkProperties(LinkConfigEntry entry);
//
//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled be the
//	 *            returned LinkProperties entries
//	 * @return a list of LinkProperties entries, that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public List<LinkConfigEntry> getLinkPropertiesEntries(LinkConfigEntry entry);
//
//	/**
//	 * 
//	 * @param offset
//	 *            the offset of the range
//	 * @param amount
//	 *            the number of entries to be returned
//	 * @return a list of LinkProperties entries, that match the given offset and
//	 *         amount
//	 */
//	public List<LinkConfigEntry> getAllLinkPropertiesEntries(int offset,
//			int amount);
//
//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the
//	 *            LinkProperties entries that shall be removed
//	 * 
//	 */
//	public void removeLinkPropertiesEntries(LinkConfigEntry entry);
//
//	/**
//	 * @param entry contains the conditions which must be fulfilled by the
//	 *            LinkProperties entries that shall be counted
//	 * @return the number of LinkProperties entries that fulfill the conditions of
//	 *         the provided entry
//	 */
//	public long count(LinkConfigEntry entry);
//
//	/**
//	 * 
//	 * @param localIP the local IP address
//	 * @param remoteIP the remote IP address
//	 * @return the link property entry for the given local and remote IP address
//	 */
//	public LinkConfigEntry getLinkProperties(String localIP, String remoteIP);
//
//	// --------------------------------------------------
//	// Deprecated methods
//	// --------------------------------------------------
//
//	@Deprecated
//	/*
//	 * * Add a LinkProperty for src -> dest
//	 * 
//	 * @param src
//	 * 
//	 * @param dest
//	 * 
//	 * @param props
//	 */
//	public void addLinkProperties(IPRangeConfigEntry src, IPRangeConfigEntry dest,
//			LinkConfigEntry props);

}
