/**
 * 
 */
package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author christian
 * 
 */
@Entity
@Table(name = "monitoring_connected_peer_status_entry")
public class ConnectedPeerStatusEntry  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5520549518897841324L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	private Float down_rate;

	@Column
	private Float up_total;

	@Column
	private String addr;

	@Column
	private String g2g;

	@Column
	private String down_str;

	@Column
	private Float down_total;

	@Column
	private String g2g_score;

	@Column
	private Float up_rate;

	@Column
	private String peer_id;

	@Column
	private String up_str;

	@ManyToOne(targetEntity = eu.smoothit.sis.db.impl.entities.PeerStatusEntry.class, cascade = { CascadeType.ALL })
//	@JoinColumn(name = "status_id", referencedColumnName = "id")
	private PeerStatusEntry status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getDown_rate() {
		return down_rate;
	}

	public void setDown_rate(Float down_rate) {
		this.down_rate = down_rate;
	}

	public Float getUp_total() {
		return up_total;
	}

	public void setUp_total(Float up_total) {
		this.up_total = up_total;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getG2g() {
		return g2g;
	}

	public void setG2g(String g2g) {
		this.g2g = g2g;
	}

	public String getDown_str() {
		return down_str;
	}

	public void setDown_str(String down_str) {
		this.down_str = down_str;
	}

	public Float getDown_total() {
		return down_total;
	}

	public void setDown_total(Float down_total) {
		this.down_total = down_total;
	}

	public String getG2g_score() {
		return g2g_score;
	}

	public void setG2g_score(String g2g_score) {
		this.g2g_score = g2g_score;
	}

	public Float getUp_rate() {
		return up_rate;
	}

	public void setUp_rate(Float up_rate) {
		this.up_rate = up_rate;
	}

	public String getPeer_id() {
		return peer_id;
	}

	public void setPeer_id(String peer_id) {
		this.peer_id = peer_id;
	}

	public String getUp_str() {
		return up_str;
	}

	public void setUp_str(String up_str) {
		this.up_str = up_str;
	}

	public void setStatus(PeerStatusEntry status) {
		this.status = status;
	}

	public PeerStatusEntry getStatus() {
		return status;
	}

}
