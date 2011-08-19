package eu.smoothit.sis.admin.backendBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import eu.smoothit.sis.admin.backendBean.superclass.DataTable;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;

public class IPRangeConfigBean extends DataTable<IPRangeConfigEntry> {
	// private IPRangeConfigEntry apiQuestions;
	// define DAO
	SisDAOFactory factory = SisDAOFactory.getFactory();
	protected IPRangeDAO dataDAO = factory.createIPRangeDAO();
	// if all, then all fetch filter local/remote value, otherwise pass it to
	// refresh DAO.getALL
	private String localFlag = "all";
	// dataTableColumn Names for sorting TODO
	private static String idValue = "id";

	// initialization
	public IPRangeConfigBean() {
	}

	// getter && setter
	// ----------------------------------------------------------------------------------

	public String getIdValue() {
		return idValue;
	}

	public List<IPRangeConfigEntry> getDataList() {
		refreshDataList();
		return dataList;
	}

	public void refreshDataList() {
		// editMode not refresh data
		if (!editMode) {
			// filterLocal stand for local/remote selection
			if (localFlag.equals("all")) {
				try {
					dataList = dataDAO.getAll();
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
				}
			} else {
				try {
					IPRangeConfigEntry newEntry = new IPRangeConfigEntry();
					newEntry.setLocal(Boolean.parseBoolean(localFlag));
					dataList = dataDAO.get(newEntry);
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
				}
			}
			sort();
		}
	}

	// Form actions
	// ----------------------------------------------------------------------------------

	public void actionLoad_old() {
		try {
			// show all records
			dataList = dataDAO.getAll();
		} catch (Exception e) {
			ToolSet.setErrorMessage(e.getMessage() + " Cause: " + e.getCause());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * overload actionAdd(E) as JSF web page allows methods without parameters
	 * only.
	 */
	public void actionAdd() {
		IPRangeConfigEntry newEntry = new IPRangeConfigEntry();
		if (localFlag.equals("all"))
			// default set to local
			newEntry.setLocal(true);
		else
			newEntry.setLocal(Boolean.parseBoolean(localFlag));
		actionAdd(newEntry);
	}

	@Override
	public void actionDelete() {
		// Get selected items.
		List<IPRangeConfigEntry> deleteItems = new ArrayList<IPRangeConfigEntry>();
		for (int index = 0; index < dataList.size(); index++) {
			if (isSelectedRow(index)) {
				deleteItems.add(dataList.get(index));
			}
		}
		if (deleteItems.isEmpty()) {
			ToolSet.setErrorMessage("Select at least one item to delete.");
			return;
		}
		String deletedItemsNames = "";
		StringBuffer buf1 = new StringBuffer();
		try {
			for (IPRangeConfigEntry deleteItem : deleteItems) {
				dataDAO.remove(deleteItem);
				buf1.append("(ID" + deleteItem.getId() + ")");
			}
			deletedItemsNames = buf1.toString();
		} catch (Exception e) {
			ToolSet.setErrorMessage(e.getMessage() + " Cause: " + e.getCause());
			e.printStackTrace();
			return;
		}
		// Feedback indicating which items deleted
		ToolSet.setErrorMessage("Deleted entries: " + deletedItemsNames);
	}

	@Override
	public void actionSave() {
		// do not log for actionSave, as there are serialized type,which cause
		// exception
		List<IPRangeConfigEntry> editItems = new ArrayList<IPRangeConfigEntry>();
		for (int index = 0; index < dataList.size(); index++) {
			if (isEditModeRow(index)) {
				editItems.add(dataList.get(index));
			}
		}
		try {
			for (IPRangeConfigEntry editItem : editItems) {
				if (editItem.getId() == 0) {
					// id=0 stand for new added entry
					dataDAO.persist(editItem);
					ToolSet.setErrorMessage("A new entry added");
				} else {
					// otherwise, it is existing entry, update it.
					dataDAO.update(editItem);
					ToolSet.setErrorMessage("One entry updated");
				}
			}
		} catch (Exception e) {
			ToolSet.setErrorMessage(e.getMessage() + " Cause: " + e.getCause());
			e.printStackTrace();
			return;
		}
		// reload data.
		super.actionSave();
	}

	@Override
	public void actionRefresh() {
		localFlag = "all";
		super.actionRefresh();
		ToolSet.resetErrorMessage();
	}

	public void filtering_Listener(ValueChangeEvent event) {
		IPRangeConfigEntry Question = new IPRangeConfigEntry();
		// set local
		String localTag = (String) event.getNewValue();
		// remote or local
		if (!localTag.equals("all")) {
			Question.setLocal(Boolean.parseBoolean(localTag));
			// local or remote
			localFlag = localTag;
			try {
				dataList = dataDAO.get(Question);
			} catch (Exception e) {
				ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						+ e.getCause());
				e.printStackTrace();
				return;
			}
		} else
			localFlag = "all";

	}

}
