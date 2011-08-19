package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a log information about accounting.
 * @author Tomasz Stradomski
 *
 */
@Entity
@Table(name = "security_logs")
public class LogEntry implements Serializable {
	private static final long serialVersionUID = 6307993841856472490L;

	@Id
	@GeneratedValue
	private long id;
	private Date date;
	private String username;
	private String event;
	private String result;

	public LogEntry() {
		date = new Date();
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return (Date) date.clone();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogEntry other = (LogEntry) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String toString() {
		return "User = " + username + "; Date = " + date.toString()
				+ "; Event = " + event + "; Result = " + result;
	}

}
