package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Used to store props into database
 * 
 * @author Christian Gross, KOM, TU Darmstadt
 * 
 */

@Entity
@Table(name = "hap_peer_statistic_entry", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ip_address", "listenport" }))
public class PeerStatisticsEntry implements Serializable {

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ipAddress) {
		ip_address = ipAddress;
	}

	public Integer getPort() {
		return listenport;
	}

	public void setPort(Integer port) {
		this.listenport = port;
	}

	public Double getLocal_upload_volume() {
		return local_upload_volume;
	}

	public void setLocal_upload_volume(Double localUploadVolume) {
		local_upload_volume = localUploadVolume;
	}

	public Double getTotal_upload_volume() {
		return total_upload_volume;
	}

	public void setTotal_upload_volume(Double totalUploadVolume) {
		total_upload_volume = totalUploadVolume;
	}

	public Double getTotal_download_volume() {
		return total_download_volume;
	}

	public void setTotal_download_volume(Double totalDownloadVolume) {
		total_download_volume = totalDownloadVolume;
	}

	public Double getHap_rating() {
		return hap_rating;
	}

	public void setHap_rating(Double hapRating) {
		hap_rating = hapRating;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3031835757104461302L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String ip_address;

	@Column
	private Integer listenport;

	@Column
	private Double local_upload_volume;

	@Column
	private Double total_upload_volume;

	@Column
	private Double total_download_volume;

	@Column
	private Double hap_rating;

	public PeerStatisticsEntry(String ip_address, Integer port,
			Double local_upload_volume, Double total_upload_volume,
			Double total_download_volume, Double hap_rating) {

		this.ip_address = ip_address;
		this.listenport = port;
		this.local_upload_volume = local_upload_volume;
		this.total_upload_volume = total_upload_volume;
		this.total_download_volume = total_download_volume;
		this.hap_rating = hap_rating;

	}

	public PeerStatisticsEntry() {
	};

	public String toString() {
		String returnValue = "";
		returnValue += "id: " + id + "; ";
		returnValue += "ip_address: " + ip_address + "; ";
		returnValue += "port: " + listenport + "; ";
		returnValue += "local_upload_volume: " + local_upload_volume + "; ";
		returnValue += "total_upload_volume: " + total_upload_volume + "; ";
		returnValue += "total_download_volume: " + total_download_volume + "; ";
		returnValue += "hap_rating: " + hap_rating + ";\n";

		return returnValue;
	}

}
