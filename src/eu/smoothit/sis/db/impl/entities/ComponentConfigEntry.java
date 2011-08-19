package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Used to store props into database
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */

@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = { "component",
// "propName" }))
@Table(name = "config_props_config_entry")
public class ComponentConfigEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3031835757104461302L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String component;

	@Column
	private String propName;

	/**
	 * Lob (Large Object) is used to store this to the DB as BLOB. Basic enables
	 * "LazyLoading"
	 */
	@Lob
	@Basic
	private Serializable value;

	public ComponentConfigEntry() {
		
	}
	
	public ComponentConfigEntry(String component, String propName, Serializable value) {
		this.component = component;
		this.propName = propName;
		this.value = value;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public String toString() {
		return "ID = " + id + " ; " + component + "." + propName + " = "
				+ value.toString();
	}

}
