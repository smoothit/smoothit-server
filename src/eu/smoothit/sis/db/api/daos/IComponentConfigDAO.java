package eu.smoothit.sis.db.api.daos;

import java.util.List;

import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

/**
 * Simplest DAO. Can store generic properties for any component.
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */
public interface IComponentConfigDAO extends Dao<Long, ComponentConfigEntry> {

//	/**
//	 * 
//	 * @return an instance of the DTO PropsConfig
//	 */
//	public PropsConfigEntry createPropsConfig();

//	/**
//	 * 
//	 * @param entry
//	 *            the entry to be persisted
//	 * @return true if the persistence process was successful. False otherwise
//	 */
//	public boolean addProperty(PropsConfigEntry entry);

//	/**
//	 * 
//	 * @param propsConfig
//	 *            the entry to be updated
//	 */
//	public void updateProperty(PropsConfigEntry propsConfig);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled be the
//	 *            returned PropsConfig entries
//	 * @return a list of PropsConfig entries, that fulfill the conditions of the
//	 *         provided entry
//	 */
//	public List<PropsConfigEntry> getProperties(PropsConfigEntry entry);

//	/**
//	 * 
//	 * @param offset
//	 *            the offset of the range
//	 * @param amount
//	 *            the number of entries to be returned
//	 * @return a list of PropsConfig entries, that match the given offset and
//	 *         amount
//	 */
//	public List<PropsConfigEntry> getAllProperties(int offset, int amount);

//	/**
//	 * 
//	 * @param entry
//	 *            contains the conditions which must be fulfilled by the
//	 *            PropsConfig entries that shall be removed
//	 * 
//	 */
//	public void removeProperties(PropsConfigEntry entry);


//	/**
//	 * @param entry contains the conditions which must be fulfilled by the
//	 *            PropsConfig entries that shall be counted
//	 * @return the number of PropsConfig entries that fulfill the conditions of
//	 *         the provided entry
//	 */
//	public long count(PropsConfigEntry entry);

	/**
	 * 
	 * @param componentName
	 * @return a list of PropsConfigEntries
	 */
	public List<ComponentConfigEntry> findByComponent(String componentName);
	
	
	public ComponentConfigEntry findByComponentAndName(String componentName, String propName); 
	
	
//	/**
//	 * * Convenience method to get properties with String values
//	 * 
//	 * @param component the component name
//	 * 
//	 * @param property the property name
//	 * 
//	 * @return string value or null if no property with this comp/name and type
//	 * string is stored!
//	 */
//	public String getStringProperty(String component, String property);
//
//	/**
//	 * * Convienience method to add properties with String values Attention: If
//	 * a non String Propoerty of the same comp/name exists it will be overriden!
//	 * 
//	 * @param component the component name
//	 * 
//	 * @param property the property name
//	 * 
//	 * @param value the configuration value to be stored
//	 */
//	public void addStringProperty(String component, String property,
//			String value);

	
	
//	/**
//	 * Convenience method for retrieving a stored property value
//	 * @param component the component name
//	 * @param propertyName the property name
//	 * @return the property value as serializable
//	 */
//	public Serializable getProperty(String component, String propertyName);

}
