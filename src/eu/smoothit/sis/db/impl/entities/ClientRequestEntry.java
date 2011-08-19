/**
 * 
 */
package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author christian
 * 
 */
@Entity
@Table(name = "config_client_request_entry")
public class ClientRequestEntry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long request_id;

	@Column
	private String ip;

	@Column
	private Integer port;
	
	@Column
	private Long lastupdate;

	@ManyToMany(targetEntity = eu.smoothit.sis.db.impl.entities.SwarmEntityEntry.class, cascade = { CascadeType.ALL }, fetch=FetchType.EAGER)
	@JoinTable(name = "config_client_request_entry_config_swarm_entity_entry", joinColumns = @JoinColumn(name = "config_client_request_entry", referencedColumnName = "request_id"), inverseJoinColumns = @JoinColumn(name = "config_swarm_entity_entry", referencedColumnName = "swarm_id"))
	private List<SwarmEntityEntry> swarms;

	/**
	 * 
	 */
	private static final long serialVersionUID = -9144608570370489304L;

	public String getIP() {
		return ip;
	}

	public Long getId() {
		return request_id;
	}

	public void setId(Long id) {
		this.request_id = id;
	}

	public Long getLastUpdate() {
		return lastupdate;
	}

	public List<SwarmEntityEntry> getSwarms() {
		return swarms;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public void setLastUpdate(Long date) {
		this.lastupdate = date;
	}

	public void setSwarms(List<SwarmEntityEntry> swarms) {
		this.swarms = swarms;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
