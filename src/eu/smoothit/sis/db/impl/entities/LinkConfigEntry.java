package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = { "src_prefix",
// "src_prefix_len", "dest_prefix", "dest_prefix_len" }))
@Table(name = "config_link_config_entry")
public class LinkConfigEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5801985502378733437L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String src_prefix;

	@Column(nullable = false)
	private Integer src_prefix_len;

	@Column(nullable = false)
	private String dest_prefix;

	@Column(nullable = false)
	private Integer dest_prefix_len;

	@Column
	private String relation;

	@Column
	private Double linkCapacity;

	@Column
	private Double delay;

	public LinkConfigEntry() {
		// Used by hibernate
	}

	/*
	 * public ConfigEntry(String src_prefix, Integer src_prefix_len, String
	 * dest_prefix, Integer dest_prefix_len, String relation, Double
	 * linkCapacity, Double delay) { super(); this.src_prefix = src_prefix;
	 * this.src_prefix_len = src_prefix_len; this.dest_prefix = dest_prefix;
	 * this.dest_prefix_len = dest_prefix_len; this.relation = relation;
	 * this.linkCapacity = linkCapacity; this.delay = delay; }
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSrc_prefix() {
		return src_prefix;
	}

	public void setSrc_prefix(String src_prefix) {
		this.src_prefix = src_prefix;
	}

	public Integer getSrc_prefix_len() {
		return src_prefix_len;
	}

	public void setSrc_prefix_len(Integer src_prefix_len) {
		this.src_prefix_len = src_prefix_len;
	}

	public String getDest_prefix() {
		return dest_prefix;
	}

	public void setDest_prefix(String dest_prefix) {
		this.dest_prefix = dest_prefix;
	}

	public Integer getDest_prefix_len() {
		return dest_prefix_len;
	}

	public void setDest_prefix_len(Integer dest_prefix_len) {
		this.dest_prefix_len = dest_prefix_len;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Double getLinkCapacity() {
		return linkCapacity;
	}

	public void setLinkCapacity(Double linkCapacity) {
		this.linkCapacity = linkCapacity;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}

	public Double getDelay() {
		return delay;
	}

	public String toString() {
		return src_prefix + "/" + src_prefix_len + " -> " + dest_prefix + "/"
				+ dest_prefix_len + " : (" + linkCapacity + " , " + delay
				+ " , " + relation + ")";
	}

}
