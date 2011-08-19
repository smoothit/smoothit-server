package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Used to store props into database
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */

@Entity
@Table(name = "hap_entry")
public class HAPEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3031835757104461302L;

	@Column
	private Boolean hap_flag;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String ip_address;

	@Column
	private Integer listenport;

	public HAPEntry() {
		
	}

	public HAPEntry(String ip_address, Integer listenport, Boolean hap_flag) {
		this.ip_address = ip_address;
		this.listenport = listenport;
		this.hap_flag = hap_flag;
	}

	public Boolean getHap_flag() {
		return hap_flag;
	}

	public long getId() {
		return id;
	}
	
	public String getIp_address() {
		return ip_address;
	}

	public Integer getListenport() {
		return listenport;
	}

	public void setHap_flag(Boolean hapFlag) {
		hap_flag = hapFlag;
	}
	
	protected void setId(long id) {
		this.id = id;
	}


	public void setIp_address(String ipAddress) {
		ip_address = ipAddress;
	}

	public void setListenport(Integer listenport) {
		this.listenport = listenport;
	}

	public String toString() {
		String returnValue = "";
		returnValue += "id: " + id + "; ";
		returnValue += "ip_address: " + ip_address + "; ";
		returnValue += "port: " + listenport + "; ";
		returnValue += "hap_flag: " + hap_flag + "; ";
		return returnValue;
	}

}
