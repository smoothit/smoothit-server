package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Encapsulates a IP range described by prefix and prefix length Can be stored
 * to Database
 * 
 * @author Konstantin Pussep, KOM, TU Darmstadt
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */

@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = { "prefix",
// "prefix_len" }))
@Table(name = "config_iprange_config_entry")
public class IPRangeConfigEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6665658111796224867L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private String prefix;

	@Column(nullable = false)
	private Integer prefix_len;

	@Column
	private Boolean isLocal;

	@Column
	private Integer asPathLength;

	@Column
	private Integer localPreference;

	@Column
	private Integer med;

	public IPRangeConfigEntry() {
		// Used by Hibernate
	}

	/*
	 * public IPRangeConfigEntry(String prefix, Integer prefix_len, Boolean
	 * isLocal, Integer asPathLength, Integer localPreference, Integer med) {
	 * this.prefix = prefix; this.prefix_len = prefix_len; this.isLocal =
	 * isLocal; this.asPathLength = asPathLength; this.localPreference =
	 * localPreference; this.med = med; }
	 */

	public Boolean getLocal() {
		return isLocal;
	}
	
	public void setLocal(Boolean isLocal) {
		this.isLocal = isLocal;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getPrefix_len() {
		return prefix_len;
	}

	public void setPrefix_len(Integer prefix_len) {
		this.prefix_len = prefix_len;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String toString() {
		return prefix + "/" + prefix_len + ":(" + asPathLength + ","
				+ localPreference + "," + med + ")";
	}

	public Integer getASPathLength() {
		return asPathLength;
	}

	public Integer getLocalPreference() {
		return localPreference;
	}

	public Integer getMED() {
		return med;
	}

	public void setASPathLength(Integer asPathLength) {
		this.asPathLength = asPathLength;
	}

	public void setLocalPreference(Integer localPreference) {
		this.localPreference = localPreference;
	}

	public void setMED(Integer med) {
		this.med = med;
	}
}
