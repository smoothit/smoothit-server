package eu.smoothit.sis.admin.backendBean.superclass;

import java.util.List;
import java.util.Map;

import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

public class ComponentConfigAbstract {
	// define DAO
	protected IComponentConfigDAO dataDAO;

	public ComponentConfigAbstract() {
		dataDAO = (IComponentConfigDAO) SisDAOFactory.getFactory()
				.createComponentConfigDAO();
		;
	}

	public void setDataDAO(IComponentConfigDAO dataDAO) {
		this.dataDAO = dataDAO;
	}

	// used by get instance of current FacesContext
	// private static FacesContext fc;
	// dataDAO = (IComponentConfigDAO) SisDAOFactory
	// .getFactory().createComponentConfigDAO();

	boolean unitTest;
	String unitTestFeedback;

	public boolean isUnitTest() {
		return unitTest;
	}

	public void setUnitTest(boolean unitTest) {
		this.unitTest = unitTest;
	}

	public String getUnitTestFeedback() {
		return unitTestFeedback;
	}

	/**
	 * create/update ComponentConfig entities if
	 * 
	 * @param componentName
	 * @param propName
	 * @param value
	 *            : if empty string, delete, return 0
	 * @return 0:delete, 1: new added;2: updated 3: error, find more than one 4:
	 *         no changed entry
	 */
	public int saveComponentConfigEntry(String componentName, String propName,
			String value) {
		ComponentConfigEntry ConfigEntry = new ComponentConfigEntry();
		ConfigEntry.setComponent(componentName);
		ConfigEntry.setPropName(propName);
		try {
			// ConfigEntry = dataDAO.findByComponentAndName(componentName,
			// propName);
			List<ComponentConfigEntry> foundConfiEntries = dataDAO
					.get(ConfigEntry);
			// found more than one entry, duplicate
			if (foundConfiEntries.size() > 1) {
				ToolSet
						.setErrorMessage("find more than one entry for component|property:"
								+ componentName + "|" + propName);

			} else if (foundConfiEntries.size() == 1) {
				ConfigEntry = foundConfiEntries.get(0);
				// remove if value is empty
				if (value.length() == 0) {
					dataDAO.remove(ConfigEntry);
					return 0;
					// no change if value not modified
				} else if (ConfigEntry.getValue().toString().equals(value)) {
					return 4;
				}
				// update existing one if value modified
				else {
					ConfigEntry.setValue(value);
					dataDAO.update(ConfigEntry);
					return 2;
				}
				// create new entry if value not empty
			} else if (foundConfiEntries.size() == 0 && value.length() != 0) {
				ConfigEntry = new ComponentConfigEntry(componentName, propName,
						value);
				dataDAO.persist(ConfigEntry);
				return 1;
			}

		} catch (Exception e) {
			ToolSet.setErrorMessage(e.getMessage() + " Cause: " + e.getCause());
			e.printStackTrace();
		}
		return 3;
	}

	public void saveComponentConfigEntries(
			Map<String, String> cachedProperties,
			Map<String, String> storedProperties, String componentName) {
		// retrieveInput();
		String property = "";
		String value = "";
		String feedback_deleted = "";
		String feedback_updated = "";
		String feedback_add = "";
		ComponentConfigEntry ConfigEntry;
		for (Map.Entry<String, String> valuePair : cachedProperties.entrySet()) {
			property = valuePair.getKey();
			value = valuePair.getValue();
			// property exist
			if (storedProperties.containsKey(property)) {
				// value is empty, remove the property
				if (value.length() == 0) {
					storedProperties.remove(property);
					ConfigEntry = new ComponentConfigEntry();
					ConfigEntry.setComponent(componentName);
					ConfigEntry.setPropName(property);
					try {
						ConfigEntry = dataDAO.get(ConfigEntry).get(0);
						dataDAO.remove(ConfigEntry);
					} catch (Exception e) {
						if (unitTest)
							e.printStackTrace();
						else
							ToolSet.setErrorMessage(e.getMessage() + " Cause: "
									+ e.getCause());
					}
					feedback_deleted = feedback_deleted + property + "<br>";
				}
				// value is not changed, return
				else if (storedProperties.get(property).equals(value)) {
					// feedback=feedback+"no change:" + property+"\n" ;
				}
				// value change, update
				else {
					ConfigEntry = new ComponentConfigEntry();
					ConfigEntry.setComponent(componentName);
					ConfigEntry.setPropName(property);
					storedProperties.put(property, value);
					try {
						ConfigEntry = dataDAO.get(ConfigEntry).get(0);
						ConfigEntry.setValue(value);
						dataDAO.update(ConfigEntry);
						feedback_updated = feedback_updated + property + "<br>";
					} catch (Exception e) {
						// ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						// + e.getCause());
						e.printStackTrace();
					}
				}

			}
			// add new property
			else if (value.length() > 0) {
				ConfigEntry = new ComponentConfigEntry(componentName, property,
						value);
				storedProperties.put(property, value);
				try {
					dataDAO.persist(ConfigEntry);
				} catch (Exception e) {
					if (unitTest)
						e.printStackTrace();
					else
						ToolSet.setErrorMessage(e.getMessage() + " Cause: "
								+ e.getCause());

				}
				feedback_add = feedback_add + property + "<br>";
			}

		}
		// reset
		cachedProperties.clear();
		// Set feedback
		String feedback_total = "";
		if (feedback_add.length() > 0) {
			feedback_total = feedback_total + "<tr>Added<td></td><td>"
					+ feedback_add + "</td></tr>";
			if (unitTest)
				unitTestFeedback = "feedback_add:" + feedback_add;
		}

		if (feedback_deleted.length() > 0) {
			feedback_total = feedback_total + "<tr>Deleted<td></td><td>"
					+ feedback_deleted + "</td></tr>";
			if (unitTest)
				unitTestFeedback = "feedback_deleted:" + feedback_deleted;
		}

		if (feedback_updated.length() > 0) {
			feedback_total = feedback_total + "<tr>Updated<td></td><td>"
					+ feedback_updated + "</td></tr>";
			if (unitTest)
				unitTestFeedback = "feedback_updated:" + feedback_updated;
		}

		if (feedback_total.length() > 0) {
			if (!unitTest)
				ToolSet
						.setErrorMessage("<table>" + feedback_total
								+ "</table>");
		}

		else {
			if (unitTest)
				unitTestFeedback = "NO Change submitted";
			// System.out.println("NO Change submitted");
			else
				ToolSet.setErrorMessage("NO Change submitted");
		}

	}

}
