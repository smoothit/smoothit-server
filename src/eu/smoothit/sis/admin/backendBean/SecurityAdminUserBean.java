package eu.smoothit.sis.admin.backendBean;

import org.jboss.security.auth.spi.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.DataTable;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserDAO;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;

public class SecurityAdminUserBean extends DataTable<User> {
	// Properties
	// ---------------------------------------------------------------------------------
	private User searchExample;
	private List<SelectItem> allRoles;
	private List<String> associatedRoleGroupRoleNames;
	private boolean usersAssociatedRoleGroupEditable;
	// define DAO
	private IUserRoleDAO roleDAO = (IUserRoleDAO) SisDAOFactory.getFactory()
			.createUserRoleDAO();
	private IUserDAO userDAO = (IUserDAO) SisDAOFactory.getFactory()
			.createUserDAO();

	// initialization
	public SecurityAdminUserBean() {
		searchExample = new User();
		usersAssociatedRoleGroupEditable = false;
	}

	public List<User> getDataList() {
		if (FacesContext.getCurrentInstance().getRenderResponse())
			refreshDataList();
		return dataList;
	}

	/**
	 * refresh data based on situation of searching/getALL/edit
	 */
	public void refreshDataList() {
		if (!editMode && editModeRows.size() == 0 && !selectAll) {
			try {
				dataList = userDAO.getAll();
			} catch (Exception e) {
				ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						+ e.getCause());
				e.printStackTrace();
			}
			sort();
			// Clear selected and editmode rows and sort data.
			setAllRoles(fetchAllRoles());
		}
	}

	// getter && setter
	// ----------------------------------------------------------------------------------
	public List<SelectItem> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<SelectItem> allRoless) {
		this.allRoles = allRoless;
	}

	public boolean getUsersAssociatedRoleGroupEditable() {
		return usersAssociatedRoleGroupEditable;
	}

	public List<String> getAssociatedRoleGroupRoleNames() {
		return associatedRoleGroupRoleNames;
	}

	public void setAssociatedRoleGroupRoleNames(
			List<String> associatedRoleGroupRoleNames) {
		this.associatedRoleGroupRoleNames = associatedRoleGroupRoleNames;
	}

	public User getSearchExample() {
		return searchExample;
	}

	public void setSearchExample(User searchExample) {
		this.searchExample = searchExample;
	}

	// Form actions
	// ----------------------------------------------------------------------------------
	public List<SelectItem> fetchAllRoles() {
		List<UserRole> roles;
		List<SelectItem> roleAsSelectItems = new ArrayList<SelectItem>();
		roles = roleDAO.getAll();
		for (UserRole u : roles) {
			roleAsSelectItems.add(new SelectItem(u.getRole() + "|"
					+ u.getRoleGroup()));
		}
		return roleAsSelectItems;

	}

	@Override
	public void actionSave() {
		List<User> editItems = new ArrayList<User>();
		for (int index = 0; index < dataList.size(); index++) {
			if (isEditModeRow(index)) {
				editItems.add(dataList.get(index));
			}
		}
		Collection<User> foundUserList = new ArrayList<User>();
		User existedUser = new User();

		for (User editItem : editItems) {
			String hash = Util.createPasswordHash("MD5", Util.BASE64_ENCODING,
					null, null, editItem.getPassword());
			editItem.setPassword(hash);
			if (editItem.getId() == null) {
				existedUser.setUsername(editItem.getUsername());
				foundUserList = userDAO.get(existedUser);
				if (foundUserList.isEmpty()) {
					try {
						// id=null stand for new added entry
						userDAO.persist(editItem);
					} catch (Exception e) {
						ToolSet.setErrorMessage(e.getMessage() + " Cause: "
								+ e.getCause());
						e.printStackTrace();
						return;
					}
				} else {
					String message = "Input user name "
							+ editItem.getUsername()
							+ " exists in database, duplicated is not allowed";
					ToolSet.setErrorMessage(message);
					return;
				}
			} else {
				try {
					// otherwise, it is existing entry, update it.
					userDAO.update(editItem);
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
					return;
				}

			}
		}
		// reload data.
		super.actionSave();
		usersAssociatedRoleGroupEditable = false;
	}

	@Override
	public void actionDelete() {
		// Get selected items.
		List<User> deleteItems = new ArrayList<User>();
		String deletedItemsNames = "";
		for (int index = 0; index < dataList.size(); index++) {
			if (isSelectedRow(index)) {
				deleteItems.add(dataList.get(index));
			}
		}
		if (deleteItems.isEmpty()) {
			ToolSet.setErrorMessage("Select at least one item to delete.");
			return;
		}
		Collection<UserRole> roles;
		for (User deleteItem : deleteItems) {
			// Check if there are roles assoicated with the user, before delete
			// action.
			roles = deleteItem.getUserRoles();
			if (roles.isEmpty()) {
				try {
					userDAO.remove(deleteItem);
					deletedItemsNames = deletedItemsNames + "("
							+ deleteItem.getUsername() + ");<br>";
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
					return;
				}
			} else {
				String message = "Disassociate from role before delete!";
				ToolSet.setErrorMessage(message);
				return;
			}

		}
		if (deletedItemsNames.length() > 0)
			ToolSet
					.setErrorMessage("Deleted entries: <br>"
							+ deletedItemsNames);
	}

	public void actionAdd() {
		actionAdd(new User());
	}

	public void actionRefresh() {
		searchExample = new User();
		usersAssociatedRoleGroupEditable = false;
		ToolSet.resetErrorMessage();
		super.actionRefresh();
	}

	// Listeners
	// ---------------------------------------------------------------
	public void fetchAssociatedRoleGroups(ActionEvent e) {
		User selectedUser = (User) dataTable.getRowData();
		Collection<UserRole> userRoles = selectedUser.getUserRoles();
		List<String> roleNames = new ArrayList<String>();
		for (UserRole r : userRoles) {
			roleNames.add(r.getRole() + "|" + r.getRoleGroup());
		}
		setAssociatedRoleGroupRoleNames(roleNames);
		usersAssociatedRoleGroupEditable = true;
		editModeRows.put(dataTable.getRowIndex(), true);
	}

	public void saveAssociatedUsers(ActionEvent e) {
		User selectedUser = (User) dataTable.getRowData();
		selectedUser = userDAO.get(selectedUser).get(0);
		UserRole entity;
		Collection<UserRole> matchedRoles = new ArrayList<UserRole>();
		for (String roleGroup : getAssociatedRoleGroupRoleNames()) {
			entity = new UserRole();
			String roleName = roleGroup.split("\\|")[0];
			String groupName = roleGroup.split("\\|")[1];
			entity.setRole(roleName);
			entity.setRoleGroup(groupName);
			matchedRoles.add(roleDAO.get(entity).get(0));
		}
		selectedUser.setUserRoles(matchedRoles);
		userDAO.update(selectedUser);
		// Toggle edit mode and reload data.
		actionSave();
		usersAssociatedRoleGroupEditable = false;
		if (!e.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			e.setPhaseId(PhaseId.INVOKE_APPLICATION);
			e.queue();
			return;
		}
	}
}