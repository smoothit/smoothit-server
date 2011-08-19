package eu.smoothit.sis.db.impl.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

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
 * Represents a user in a database.
 * 
 * @author Tomasz Stradomski
 * 
 */
@Entity
@Table(name = "security_users")
public class User implements Serializable {
	private static final long serialVersionUID = 3841070913096721752L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "username", unique=true)
	private String username;

	@Column(name = "password")
	private String password;

	@ManyToMany(targetEntity = eu.smoothit.sis.db.impl.entities.UserRole.class, cascade = { CascadeType.ALL },fetch=FetchType.EAGER)
	@JoinTable(name = "security_users_security_userroles", joinColumns = @JoinColumn(name = "security_users", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "security_roles", referencedColumnName = "id"))
	private Collection<UserRole> userRoles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
		userRoles = new ArrayList<UserRole>();
	}

	public User(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int r = 1;
		r = prime * r + ((username == null) ? 0 : username.hashCode());
		return r;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
