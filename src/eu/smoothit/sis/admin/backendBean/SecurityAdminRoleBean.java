package eu.smoothit.sis.admin.backendBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.DataTable;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.User;
import eu.smoothit.sis.db.impl.entities.UserRole;

public class SecurityAdminRoleBean extends DataTable<UserRole> {
	// Properties
	// ---------------------------------------------------------------------------------
	private List<SelectItem> allRoles;
	private List<String> associatedRoleGroupRoleNames;
	List<UserRole> foundRoleList;
	// define DAO
	private IUserRoleDAO roleDAO = (IUserRoleDAO) SisDAOFactory.getFactory()
			.createUserRoleDAO();

	// initialization
	public SecurityAdminRoleBean() {
	}

	// getter && setter
	// ----------------------------------------------------------------------------------
	public List<SelectItem> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<SelectItem> allRoless) {
		this.allRoles = allRoless;
	}

	public List<String> getAssociatedRoleGroupRoleNames() {
		return associatedRoleGroupRoleNames;
	}

	public void setAssociatedRoleGroupRoleNames(
			List<String> associatedRoleGroupRoleNames) {
		this.associatedRoleGroupRoleNames = associatedRoleGroupRoleNames;
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
		List<UserRole> editItems = new ArrayList<UserRole>();
		for (int index = 0; index < dataList.size(); index++) {
			if (isEditModeRow(index)) {
				editItems.add(dataList.get(index));
			}
		}
		for (UserRole editItem : editItems) {
			if (editItem.getId() == null) {
				foundRoleList = roleDAO.get(editItem);
				if (!foundRoleList.isEmpty()) {
					UserRole userRole = foundRoleList.get(0);
					String message = "Input role+group pair "
							+ userRole.getRole() + "|"
							+ userRole.getRoleGroup()
							+ " exists in database, duplicated is not allowed";
					ToolSet.setErrorMessage(message);
				} else {
					try {
						roleDAO.persist(editItem);
					} catch (Exception e) {
						ToolSet.setErrorMessage(e.getMessage() + " Cause: "
								+ e.getCause());
						e.printStackTrace();
						return;
					}
					// Toggle edit mode and reload data.
					editMode = false;
				}
			} else {
				try {
					roleDAO.update(editItem);
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
					return;
				}
				editMode = false;
			}
		}
		if (foundRoleList != null)
			foundRoleList.clear();
		// reload data.
		super.actionSave();
	}

	@Override
	public void actionDelete() {
		// Get selected items.
		List<UserRole> deleteItems = new ArrayList<UserRole>();
		for (int index = 0; index < dataList.size(); index++) {
			if (isSelectedRow(index)) {
				deleteItems.add(dataList.get(index));
			}
		}
		if (deleteItems.isEmpty()) {
			ToolSet.setErrorMessage("Select at least one item to delete.");
			return;
		}
		Collection<User> associateUsers;
		String deletedItemsNames = "";
		for (UserRole deleteItem : deleteItems) {
			associateUsers = deleteItem.getUsers();
			if (associateUsers.isEmpty()) {
				try {
					roleDAO.remove(deleteItem);
					deletedItemsNames = deletedItemsNames + "("
							+ deleteItem.getRole() + ", "
							+ deleteItem.getRoleGroup() + ");<br>";
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
					return;
				}
			} else {
				String message = "There are users associate to "
						+ deleteItem.getRole() + "/"
						+ deleteItem.getRoleGroup()
						+ " pair, they must be deassociated first";
				ToolSet.setErrorMessage(message);
				return;
			}
		}
		if (deletedItemsNames.length() > 0)
			ToolSet.setErrorMessage("Deleted role|groups pairs: <br>"
					+ deletedItemsNames);
	}

	public void actionAdd() {
		actionAdd(new UserRole());
	}

	public void actionRefresh() {
		setAllRoles(fetchAllRoles());
		super.actionRefresh();
	}

	public List<UserRole> getDataList() {
		if (FacesContext.getCurrentInstance().getRenderResponse())
			refreshDataList();
		return dataList;
	}

	/**
	 * refresh data based on situation of searching/getALL/edit
	 */
	public void refreshDataList() {
		if (!editMode && !selectAll) {
			try {
				dataList = roleDAO.getAll();
			} catch (Exception e) {
				ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						+ e.getCause());
				e.printStackTrace();
			}
			sort();
		}
	}

}