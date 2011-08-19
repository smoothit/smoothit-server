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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * @author christian
 * 
 */
@Entity
@Table(name = "monitoring_peer_status_entry")
public class PeerStatusEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7281237914921779456L;

	@Column
	private Float down_rate;
	
	@Column
	private Float down_total;
	
	@Column
	private String filename;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	private String infohash;

	@Column
	private Boolean iop_flag;

	@Column
	private String ip_address;
	
	
	@Column
	private Integer listenport;

	@Column
	private Boolean live;

	@Column
	private Integer p_dropped;

	@Column
	private Integer p_late;

	@Column
	private Integer p_played;

	@Column
	private String peer_id;

	@OneToMany(targetEntity = eu.smoothit.sis.db.impl.entities.ConnectedPeerStatusEntry.class, cascade = { CascadeType.ALL }, mappedBy = "status", fetch=FetchType.EAGER)
//	@JoinTable(name = "config_client_request_entry_config_swarm_entity_entry", joinColumns = @JoinColumn(name = "config_client_request_entry", referencedColumnName = "request_id"), inverseJoinColumns = @JoinColumn(name = "config_swarm_entity_entry", referencedColumnName = "swarm_id"))
	private List<ConnectedPeerStatusEntry> peers;

	@Column
	private Float progress;

	@Column
	private String status;

	@Column
	private Integer t_prebuf;

	@Column
	private Integer t_stalled;

	@Column
	@Index(name="timestampindex")
	private Long timestamp;

	@Column
	private Float up_rate;

	@Column
	private Float up_total;

	@Column
	private String validRange;

	public Float getDown_rate() {
		return down_rate;
	}

	public Float getDown_total() {
		return down_total;
	}

	public String getFilename() {
		return filename;
	}

	public Integer getId() {
		return id;
	}

	public String getInfohash() {
		return infohash;
	}

	public Boolean getIop_flag() {
		return iop_flag;
	}

	/**
	 * @return the list
	 */
	public List<ConnectedPeerStatusEntry> getList() {
		return peers;
	}

	public Integer getListenport() {
		return listenport;
	}

	public Boolean getLive() {
		return live;
	}

	public Integer getP_dropped() {
		return p_dropped;
	}

	public Integer getP_late() {
		return p_late;
	}

	public Integer getP_played() {
		return p_played;
	}

	public String getPeer_id() {
		return peer_id;
	}

	public Float getProgress() {
		return progress;
	}

	public String getStatus() {
		return status;
	}

	public Integer getT_prebuf() {
		return t_prebuf;
	}

	public Integer getT_stalled() {
		return t_stalled;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public Float getUp_rate() {
		return up_rate;
	}

	public Float getUp_total() {
		return up_total;
	}

	public String getValidRange() {
		return validRange;
	}

	public void setDown_rate(Float downRate) {
		down_rate = downRate;
	}

	public void setDown_total(Float down_total) {
		this.down_total = down_total;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public void setInfohash(String infohash) {
		this.infohash = infohash;
	}

	public void setIop_flag(Boolean iopFlag) {
		iop_flag = iopFlag;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<ConnectedPeerStatusEntry> list) {
		this.peers = list;
	}

	public void setListenport(Integer listenport) {
		this.listenport = listenport;
	}

	public void setLive(Boolean live) {
		this.live = live;
	}

	public void setP_dropped(Integer p_dropped) {
		this.p_dropped = p_dropped;
	}

	public void setP_late(Integer p_late) {
		this.p_late = p_late;
	}

	public void setP_played(Integer p_played) {
		this.p_played = p_played;
	}

	public void setPeer_id(String peer_id) {
		this.peer_id = peer_id;
	}

	public void setProgress(Float progress) {
		this.progress = progress;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setT_prebuf(Integer t_prebuf) {
		this.t_prebuf = t_prebuf;
	}

	public void setT_stalled(Integer t_stalled) {
		this.t_stalled = t_stalled;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void setUp_rate(Float up_rate) {
		this.up_rate = up_rate;
	}

	public void setUp_total(Float up_total) {
		this.up_total = up_total;
	}

	public void setValidRange(String validRange) {
		this.validRange = validRange;
	}

	/**
	 * @param ip_address the ip_address to set
	 */
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	/**
	 * @return the ip_address
	 */
	public String getIp_address() {
		return ip_address;
	}

}
