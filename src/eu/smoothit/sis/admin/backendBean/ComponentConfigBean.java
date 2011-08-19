package eu.smoothit.sis.admin.backendBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.DataTable;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

/**
 * BackBean for Component Config module
 * 
 */
public class ComponentConfigBean extends DataTable<ComponentConfigEntry> {
	// Constants
	// ----------------------------------------------------------------------------------
	public static final String COMPONENT_NAME_IOP = eu.smoothit.sis.init.web.SisWebInitializer.COMPONENT_NAME_CONTR_IOP;// "Controler-IoP-Config"
	public static final String COMPONENT_NAME_GENRIC = "PARAM_GENERIC_PEER_RATING";
	public static final String COMPONENT_NAME_CONTR_HAP = eu.smoothit.sis.init.web.SisWebInitializer.COMPONENT_NAME_CONTR_HAP;
	public static final String COMPONENT_NAME_METERING = eu.smoothit.sis.init.web.SisWebInitializer.COMPONENT_NAME_METERING;
	// define DAO
	private IComponentConfigDAO dataDAO = (IComponentConfigDAO) SisDAOFactory
			.getFactory().createComponentConfigDAO();
	// dataTableColumn Names used in sorting
	private static String idValue = "id";
	private static String componentName = "component";
	private static String propertyName = "propName";
	private static String propertyValue = "value";
	// input value of searching
	private String keyword_componentName;
	private String keyword_componentName_picker;
	// search parameters
	boolean searchModeMoreThanOnces = false;
	boolean isQuestion;

	// wrap operation mode values in a array for Selection List
	public SelectItem[] getComponentNameList() {
		SelectItem[] definitions = new SelectItem[4];
		definitions[0] = new SelectItem(COMPONENT_NAME_IOP, "IoP",
				"This is for IoP", false);
		definitions[1] = new SelectItem(COMPONENT_NAME_GENRIC,
				"GenericPeerRating", "This is for Generic peer", false);
		definitions[2] = new SelectItem(COMPONENT_NAME_CONTR_HAP, "HAP",
				"This is for HAP", false);
		definitions[3] = new SelectItem(COMPONENT_NAME_METERING, "Metering",
				"This is for Metering", false);
		return definitions;
	}

	// initialization
	public ComponentConfigBean() {
	}

	// setter and getter
	// ----------------------------------------------------------------------------------

	public String getKeyword_componentName_picker() {
		return keyword_componentName_picker;
	}

	public void setKeyword_componentName_picker(
			String keywordComponentNamePicker) {
		keyword_componentName_picker = keywordComponentNamePicker;
	}

	// searching input keywords
	public String getKeyword_componentName() {
		return keyword_componentName;
	}

	public void setKeyword_componentName(String keywordComponentName) {
		keyword_componentName = keywordComponentName;
	}

	public String getIdValue() {
		return idValue;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public List<ComponentConfigEntry> getDataList() {
		// refresh data every time on load page
		if (FacesContext.getCurrentInstance().getRenderResponse())
			refreshDataList();
		return dataList;
	}

	/**
	 * refresh data based on situation of searching/getALL/edit
	 */
	public void refreshDataList() {
		if (multiSearchMode) {
			if (searchModeMoreThanOnces) {
				multiColumnsSearch();
			}
			sort();
			// query again when refresh query result
			searchModeMoreThanOnces = true;
			// otherwise, load all when not in edit mode and not all rows of
			// current selected
		} else if (!editMode && !selectAll) {
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

	/**
	 * Searching more than one fields
	 */
	// ComponentConfigEntry Question;
	public void multiColumnsSearch() {
		isQuestion = false;
		// TODO validate at least one criteria before search
		ComponentConfigEntry Question;
		Question = new ComponentConfigEntry();
		if (keyword_componentName.trim().length() > 0) {
			Question.setComponent(keyword_componentName);
			isQuestion = true;
		}
		if (isQuestion) {
			try {
				dataList = dataDAO.get(Question);
			} catch (Exception e) {
				ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						+ e.getCause());
				e.printStackTrace();
				// return false;
			}
			if (dataList.size() == 0) {
				ToolSet.setErrorMessage("Search Return No Result");
			}
		}
		// if no creterias given
		else {
			multiSearchMode = false;
			ToolSet
					.setErrorMessage("Please give at least one criteria for searching ");
		}
		// search criteria change, disable requery previous query
		searchModeMoreThanOnces = false;
	}

	/**
	 * Pick a predefined criteria for searching
	 * 
	 * @param event
	 */
	public void filterPickerListener(ValueChangeEvent event) {
		keyword_componentName_picker = (String) event.getNewValue();
		keyword_componentName = keyword_componentName_picker;
		// jump to stage of invoke application(refresh)
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}

	}

	/**
	 * overload actionAdd(E) as JSF web page allows methods without parameters
	 * only.
	 */
	public void actionAdd() {
		// disable search mode
		multiSearchMode = false;
		actionAdd(new ComponentConfigEntry());
	}

	@Override
	public void actionDelete() {
		// disable search mode
		multiSearchMode = false;
		List<ComponentConfigEntry> deleteItems = new ArrayList<ComponentConfigEntry>();
		for (int index = 0; index < dataList.size(); index++) {
			// Get selected items.
			if (isSelectedRow(index)) {
				deleteItems.add(dataList.get(index));
			}
		}
		if (deleteItems.isEmpty()) {
			ToolSet.setErrorMessage("Select at least one item to delete.");
			return;
		}
		StringBuffer buf = new StringBuffer();
		try {
			for (ComponentConfigEntry deleteItem : deleteItems) {
				dataDAO.remove(deleteItem);
				buf.append("(" + deleteItem.getComponent() + ", "
						+ deleteItem.getPropName() + ");<br>");
			}
		} catch (Exception e) {
			ToolSet.setErrorMessage(e.getMessage() + " Cause: " + e.getCause());
			e.printStackTrace();
			return;
		}
		String deletedItemsNames = buf.toString();
		if (deletedItemsNames.length() > 0)
			ToolSet
					.setErrorMessage("Deleted entries: <br>"
							+ deletedItemsNames);
	}

	// form actions
	// ----------------------------------------------------------------------------------
	/*
	 * save new added entries or modified exiting entry
	 */
	@Override
	public void actionSave() {
		List<ComponentConfigEntry> editItems = new ArrayList<ComponentConfigEntry>();
		for (int index = 0; index < dataList.size(); index++) {
			if (isEditModeRow(index)) {
				editItems.add(dataList.get(index));
			}
		}
		String addItemNames = "";
		String updatItemNames = "";
		StringBuffer buf3 = new StringBuffer();
		StringBuffer buf2 = new StringBuffer();
		try {
			for (ComponentConfigEntry editItem : editItems) {
				// id=0 stand for new added entry
				if (editItem.getId() == 0) {
					dataDAO.persist(editItem);
					buf2.append("(" + editItem.getComponent() + ","
							+ editItem.getPropName() + ")");
				} else {
					// otherwise, it is existing entry, update it.
					dataDAO.update(editItem);
					buf3.append("(" + editItem.getComponent() + ","
							+ editItem.getPropName() + ")");
				}

			}
			addItemNames = buf2.toString();
			updatItemNames = buf3.toString();
			setFeedback(updatItemNames, addItemNames);
		} catch (Exception e) {
			ToolSet.setErrorMessage(e.getMessage() + " Cause: " + e.getCause());
			e.printStackTrace();
			return;
		}

		// reload data.
		super.actionSave();
	}

	private void setFeedback(String updatedItems, String AddedItems) {
		String feedback_total = "";
		if (AddedItems.length() > 0)
			feedback_total = feedback_total + "<tr>Added<td></td><td>"
					+ AddedItems + "</td></tr>";
		if (updatedItems.length() > 0)
			feedback_total = feedback_total + "<tr>Updated<td></td><td>"
					+ updatedItems + "</td></tr>";
		if (feedback_total.length() > 0) {
			ToolSet.setErrorMessage("<table>" + feedback_total + "</table>");
		} else
			ToolSet.setErrorMessage("NO Change submitted");
	}

	@Override
	public void actionRefresh() {
		// reset question each time
		// reset, hidden all input UI
		keyword_componentName = "";
		keyword_componentName_picker = "";
		super.actionRefresh();
		ToolSet.resetErrorMessage();
	}

	/**
	 * Search data. Toggle search mode, load data and go to first page.
	 */
	public void actionSearch() {
		// reset search
		multiSearchMode = true;
		multiColumnsSearch();
	}

}
