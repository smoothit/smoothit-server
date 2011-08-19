/**
 * 
 */
package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author christian
 * 
 */
@Entity
@Table(name = "client_swarm_info_entry")
public class ClientSwarmInfoEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -608653452218787989L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String ip;

	@Column
	private Integer port;

	@Column 
	private String torrent_id;
	
	@Column 
	private String torrent_url;
	
	@Column
	private Double file_size;
	
	@Column
	private Float download_progress;
	
	@Column
	private Long timestamp;
	
	@Column
	private Boolean iop_flag;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTorrent_id() {
		return torrent_id;
	}

	public void setTorrent_id(String torrentId) {
		torrent_id = torrentId;
	}

	public String getTorrent_url() {
		return torrent_url;
	}

	public void setTorrent_url(String torrentUrl) {
		torrent_url = torrentUrl;
	}

	public Double getFile_size() {
		return file_size;
	}

	public void setFile_size(Double fileSize) {
		file_size = fileSize;
	}

	public Float getDownload_progress() {
		return download_progress;
	}

	public void setDownload_progress(Float downloadProgress) {
		download_progress = downloadProgress;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean isIop_flag() {
		return iop_flag;
	}

	public void setIop_flag(Boolean iopFlag) {
		iop_flag = iopFlag;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
