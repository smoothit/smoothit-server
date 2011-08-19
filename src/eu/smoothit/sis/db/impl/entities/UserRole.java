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
 * Represents a role of a user in the database.
 * 
 * @author Tomasz Stradomski
 * 
 */
@Entity
@Table(name = "security_roles")
public class UserRole implements Serializable {
	private static final long serialVersionUID = -7700973464825872986L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "role", unique=true)
	private String role;

	@Column(name = "role_group")
	private String roleGroup;

	@ManyToMany(targetEntity = eu.smoothit.sis.db.impl.entities.User.class, mappedBy = "userRoles", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "security_users_security_userroles", joinColumns = @JoinColumn(name = "security_users", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "security_roles", referencedColumnName = "id"))
	private Collection<User> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleGroup() {
		return roleGroup;
	}

	public void setRoleGroup(String roleGroup) {
		this.roleGroup = roleGroup;
	}

	public UserRole(String role, String roleGroup) {
		this();
		this.role = role;
		this.roleGroup = roleGroup;
	}

	public UserRole() {
		users = new ArrayList<User>();
	}

	// @Override
	// public String toString() {
	// return "Username = " + username + "; RoleGroup = " + roleGroup
	// + "; Role = " + role;
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
