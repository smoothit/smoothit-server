package eu.smoothit.sis.admin.backendBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.DataTable;
import eu.smoothit.sis.admin.util.DTOComparator;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.ILogEntryDAO;
import eu.smoothit.sis.db.impl.entities.LogEntry;

public class LogBean extends DataTable<LogEntry> {
	// Properties
	// ---------------------------------------------------------------------------------
	// private LogEntry searchExample;
	private List<SelectItem> Allusers;
	// either lastNdays or datepicker
	// private String searchMethodTag;
	// define DAO
	private ILogEntryDAO dataDAO = (ILogEntryDAO) SisDAOFactory.getFactory()
			.createLogEntryDAO();

	// initialization
	public LogBean() {
	}

	public List<LogEntry> getDataList() {
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
				dataList = dataDAO.getAll();
			} catch (Exception e) {
				ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						+ e.getCause());
				e.printStackTrace();
			}
			sort();
		}
	}

	// getter && setter
	// ----------------------------------------------------------------------------------
	public List<SelectItem> getAllusers() {
		return Allusers;
	}

	public void setAllusers(List<SelectItem> allusers) {
		Allusers = allusers;
	}

	// Form actions
	// ----------------------------------------------------------------------------------
	@Override
	public void actionSave() {
		// no need for viewer
	}

	@Override
	public void actionDelete() {
		// no need for viewer
	}

	public void actionRefresh() {
		setAllusers(fetchAllUsers());
		super.actionRefresh();
	}

	public List<SelectItem> fetchAllUsers() {
		List<LogEntry> users;
		List<SelectItem> usersAsSelectItems = new ArrayList<SelectItem>();
		List<String> usersAsSring = new ArrayList<String>();
		users = dataDAO.getAll();
		for (LogEntry u : users) {
			if (!usersAsSring.contains(u.getUsername())) {
				usersAsSring.add(u.getUsername());
				usersAsSelectItems.add(new SelectItem(u.getUsername()));
			}
		}
		return usersAsSelectItems;

	}

	public void sortByDateDesc() {
		// Get and set sort field and sort order.
		String sortFieldAttribute = "date";
		sortField = sortFieldAttribute;
		sortAscending = false;
		Collections.sort(dataList, new DTOComparator(sortField, sortAscending));
		// Clear row modes.
		editModeRows.clear();
		selectedRows.clear();

	}

}