package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;
import java.util.List;

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

@Entity
@Table(name = "config_swarm_entity_entry")
public class SwarmEntityEntry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long swarm_id;

	@Column
	private Double downloadProgress;

	@Column
	private Double playbackProgress;

	@Column
	private String torrentHash;

	@Column
	private Boolean availability;

	@Column
	private Long numberOfLeechers;

	@Column
	private Long numberOfLocalLeechers;

	@Column
	private Long numberOfSeeds;

	@Column
	private Long numberOfLocalSeeds;

	@Column
	private String torrentURL;
	

	@ManyToMany(targetEntity = eu.smoothit.sis.db.impl.entities.ClientRequestEntry.class,mappedBy="swarms", fetch=FetchType.EAGER)
//	@ManyToMany(targetEntity = eu.smoothit.sis.db.impl.daos.ClientRequestEntry.class, cascade = CascadeType.ALL)
	@JoinTable(name = "config_client_request_entry_config_swarm_entity_entry", joinColumns = @JoinColumn(name = "config_client_request_entry_id", referencedColumnName = "request_id"), inverseJoinColumns = @JoinColumn(name = "config_swarm_entity_entry_id", referencedColumnName = "swarm_id"))
	private List<ClientRequestEntry> requests;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1579090944967780736L;

	public List<ClientRequestEntry> getClientRequests() {
		return requests;
	}

	public Double getDownloadProgress() {
		return downloadProgress;
	}

	public Long getId() {
		return swarm_id;
	}

	protected void setId(Long id) {
		this.swarm_id = id;
	}

	public Double getPlaybackProgress() {
		return playbackProgress;
	}

	public String getTorrentHash() {
		return torrentHash;
	}

	public String getTorrentURL() {
		return torrentURL;
	}

	public void setClientRequests(List<ClientRequestEntry> requests) {
		this.requests = requests;
	}

	public void setDownloadProgress(Double progress) {
		this.downloadProgress = progress;
	}

	public void setPlaybackProgress(Double progress) {
		this.playbackProgress = progress;
	}

	public void setTorrentHash(String hash) {
		this.torrentHash = hash;
	}

	public void setTorrentURL(String url) {
		this.torrentURL = url;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public Long getNumberOfLeechers() {
		return numberOfLeechers;
	}

	public Long getNumberOfLocalLeechers() {
		return numberOfLocalLeechers;
	}

	public Long getNumberOfLocalSeeds() {
		return numberOfLocalSeeds;
	}

	public Long getNumberOfSeeds() {
		return numberOfSeeds;
	}

	public void setAvailablity(Boolean flag) {
		this.availability = flag;
	}

	public void setNumberOfLeechers(Long number) {
		this.numberOfLeechers = number;
	}

	public void setNumberOfLocalLeechers(Long number) {
		this.numberOfLocalLeechers = number;
	}

	public void setNumberOfLocalSeeds(Long number) {
		this.numberOfLocalSeeds = number;
	}

	public void setNumberOfSeeds(Long number) {
		this.numberOfSeeds = number;

	}

	
}
