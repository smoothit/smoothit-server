package eu.smoothit.sis.admin.backendBean.superclass;

//import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import com.icesoft.faces.component.ext.HtmlDataTable;

import eu.smoothit.sis.admin.util.DTOComparator;
import eu.smoothit.sis.admin.util.ToolSet;



/**
 * A basic backing bean for a ice:dataTable component. The class is a base class
 * used by the child class of data table
 */
public abstract class DataTable<E> {
	private static final String DEFAULT_SORT_FIELD = "id";
	private static final boolean DEFAULT_SORT_ASCENDING = true;
	// default rows for each page
	private static final int DEFAULT_TABLE_ROWS = 5;
	// Allow selection of mutiple rows for delection
	private static final boolean DEFAULT_SELECT_MULTIPLE = true;
	private static final boolean DEFAULT_SELECT_ALL = false;
	// field used in sorting
	protected String sortField = DEFAULT_SORT_FIELD;
	protected boolean sortAscending = DEFAULT_SORT_ASCENDING;
	protected HtmlDataTable dataTable;
	// editModeRows contain rows that are current edited
	protected Map<Integer, Boolean> editModeRows = new HashMap<Integer, Boolean>();
	protected List<E> dataList;
	// selectedModeRows contain rows that are current selected
	protected Map<Integer, Boolean> selectedRows = new HashMap<Integer, Boolean>();
	// Indicate if in edit Mode
	protected boolean editMode;
	protected boolean multiSearchMode;
	protected boolean selectAll = DEFAULT_SELECT_ALL;
	private boolean selectMultiple = DEFAULT_SELECT_MULTIPLE;
	// used by get instance of current FacesContext

	/**
	 * @return an instance of jsf datatable
	 */
	public HtmlDataTable getDataTable() {
		if (dataTable == null) {
			dataTable = new HtmlDataTable();
			dataTable.setRows(DEFAULT_TABLE_ROWS);
		}
		return dataTable;
	}

	/**
	 * 
	 * @param dataTable
	 *            The datatable.
	 */
	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	/**
	 * Notice: type of contained date is defined in inherited class
	 * @return a list of date
	 */
	public List<E> getDataList() {
		return dataList;
	}

	/**
	 * @return name of sorted field.
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * @return boolean type
	 */
	public boolean isSortAscending() {
		return sortAscending;
	}

	// -----------------getter and setter----------------------------
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * @return If a row is in edit status
	 */
	public boolean isEditModeRow() {
		return isEditModeRow(dataTable.getRowIndex());
	}

	/**
	 * used for non return method of isEditModeRow()
	 * 
	 * @return boolean type
	 */
	public boolean isEditModeRow(int index) {
		Boolean editMode = editModeRows.get(index);
		return editMode != null && editMode;
	}

	/**
	 * If a row selected
	 */
	public boolean isSelectedRow() {
		return isSelectedRow(dataTable.getRowIndex());
	}

	/**
	 * If multiple rows of tabe are selected
	 */
	public boolean isSelectMultiple() {
		return selectMultiple;
	}

	/**
	 * If select all rows
	 */
	public boolean isSelectAll() {
		return selectAll;
	}

	/**
	 * Search data: search mode?
	 * 
	 * @return Search mode?
	 */
	public boolean isMultiSearchMode() {
		return multiSearchMode;
	}

	/**
	 * Select: set selection.
	 * 
	 * @param selected
	 *            row?
	 */
	public void setSelectedRow(boolean selected) {
		selectedRows.put(dataTable.getRowIndex(), selected);
	}

	/**
	 * Select by radio button: catch selected data item.
	 * 
	 * @param event
	 *            The value change event. This parameter is never used.
	 */
	public void setSelectedItem(ValueChangeEvent event) {
		selectedRows.put(dataTable.getRowIndex(), true);
	}

//	/**
//	 * Reset status of selected rows and sort data.
//	 */
//	public void actionSort(ActionEvent event) {
//
//		// Get and set sort field and sort order.
//		if (event != null) {
//			String sortFieldAttribute = ToolSet.getAttribute(event, "sortField");
//
//			if (sortField != null && sortField.equals(sortFieldAttribute)) {
//				sortAscending = !sortAscending;
//			} else {
//				sortField = sortFieldAttribute;
//				sortAscending = true;
//			}
//		}
//
//	}

	/**
	 * used by isSelectedRow()
	 */
	protected boolean isSelectedRow(int index) {
		Boolean selected = selectedRows.get(index);
		return selected != null && selected;
	}

	// Functions
	// ----------------------------------------------------------
	/**
	 * sort datalist according to user action in actionSort()
	 */
	public void sort() {
		if (sortField != null) {
			Collections.sort(dataList, new DTOComparator(sortField,
					sortAscending));
			// Clear row modes.
			editModeRows.clear();
			selectedRows.clear();
		}
	}

//	/**
//	 * Toggle select type between multiple (checkboxes) and single
//	 * (radiobuttons).
//	 */
//	/**
//	 * Select all items.
//	 */
//	public void actionSelectAll() {
//		// disable search mode
//		multiSearchMode = false;
//		// Toggle.
//		selectAll = !selectAll;
//		// Radio buttons doesn't allow multiple selections. Switch to
//		// checkboxes.
//		if (selectAll) {
//			selectMultiple = true;
//		}
//		for (int index = dataTable.getFirst(); index < (dataTable.getFirst() + dataTable
//				.getRows()); index++) {
//			selectedRows.put(index, selectAll);
//		}
//	}

	/**
	 * Add one record each time able to add muliple records one time, but
	 * blocked
	 */
	public void actionAdd(E dataItem) {
		dataList.add(dataItem);
		editModeRows.put(dataList.size() - 1, true);
		editMode = true;
		pageLast();
	}

	/**
	 * Toggle edit mode on selected items. Edit items able to edit more than one
	 * items at the same time
	 */
	public void actionEdit() {
		// disable search mode
		multiSearchMode = false;
		editModeRows.putAll(selectedRows);
		selectedRows.clear();
		if (!editModeRows.containsValue(Boolean.TRUE)) {
			ToolSet.setErrorMessage("Select at least one item to edit.");
		} else {
			editMode = true;
		}
	}

	/**
	 *save new added entries or modified exiting entry
	 */
	public void actionSave() {
		// Toggle edit mode and reload data.
		editMode = false;
		editModeRows.clear();
		selectedRows.clear();
	}
	
	
	
	/**
	 * Delete selected items.
	 */
	public abstract void actionDelete();

	/**
	 * Paging datatable: go to first page.
	 */

	/**
	 * Refresh items. include actionLoad, which include sort()
	 */
	public void actionRefresh() {
		// Reset settings.
		editMode = false;
		editModeRows.clear();
		multiSearchMode = false;
		// dataTable.setRows(dataTable.getRows());
		selectMultiple = DEFAULT_SELECT_MULTIPLE;
		selectAll = DEFAULT_SELECT_ALL;
	}

	/**
	 * Go to first page
	 */
	public void pageFirst() {
		dataTable.setFirst(0);
	}

	/**
	 * Set global error message.
	 * 
	 * @param errorMessage
	 *            The error message.
	 */

	/**
	 * Paging datatable: go to last page.
	 */
	public void pageLast() {
		int rows = dataTable.getRows();
		if (rows != 0) { // Prevent ArithmeticException: / by zero.
			int count = dataTable.getRowCount();
			dataTable.setFirst(count
					- ((count % rows != 0) ? count % rows : rows));
		}
	}

	// help functions
	// -------------------------------------------------
//	protected static void setErrorMessage(String errorMessage) {
//		// remove existing message
//		resetErrorMessage();
//		fc = FacesContext.getCurrentInstance();
//		fc.addMessage(null, new FacesMessage(errorMessage));
//	}
//
//	protected static void resetErrorMessage() {
//		// remove existing message
//		fc = FacesContext.getCurrentInstance();
//		Iterator<FacesMessage> iterator = fc.getMessages();
//		while (iterator.hasNext()) {
//			FacesMessage fm = (FacesMessage) iterator.next();
//			fm.setDetail("");
//			fm.setSummary("");
//		}
//	}

}
