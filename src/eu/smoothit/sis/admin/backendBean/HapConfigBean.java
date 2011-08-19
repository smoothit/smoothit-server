package eu.smoothit.sis.admin.backendBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.ComponentConfigAbstract;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

public class HapConfigBean extends ComponentConfigAbstract {
	// Constants
	// ----------------------------------------------------------------------------------
	public static final String COMPONENT_NAME = eu.smoothit.sis.init.web.SisWebInitializer.COMPONENT_NAME_CONTR_HAP; // "Controller-HAP-Config"
	public static final String PARAM_HAP_CONTROLLER_ON_OFF = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_ON_OFF;// "HAP-Controller-On-Off";
	public static final String PARAM_HAP_BILLING_URL = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_BILLING_URL;// "HAP-Billing-URL";
	public static final String PARAM_HAP_CONTROLLER_T_UPDATE = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_T_UPDATE;// "HAP-Controller-T-Update";
	public static final String PARAM_HAP_CONTROLLER_T = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_T;// "HAP-Controller-T";
	public static final String PARAM_HAP_CONTROLLER_N = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_N;// "HAP-Controller-N";
	public static final String PARAM_HAP_CONTROLLER_P1 = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_P1;// "HAP-Controller-P1";
	public static final String PARAM_HAP_CONTROLLER_P2 = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_P2;// "HAP-Controller-P2";
	public static final String PARAM_HAP_CONTROLLER_P3 = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_P3;// "HAP-Controller-P3";
	public static final String PARAM_HAP_CONTROLLER_AD = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_AD;// "HAP-Controller-Available-Download";
	public static final String PARAM_HAP_CONTROLLER_AU = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_AU;// "HAP-Controller-Available-Upload";
	public static final String PARAM_HAP_CONTROLLER_DI = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_DI;// "HAP-Controller-Download-Increase";
	public static final String PARAM_HAP_CONTROLLER_UI = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_UI;// "HAP-Controller-Upload-Increase";
	public static final String PARAM_HAP_CONTROLLER_RT = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_HAP_CONTROLLER_RT;// "HAP-Controller-Rating-Threshold";
	// Properties
	// ---------------------------------------------------------------------------------
	Map<String, String> storedProperties = new HashMap<String, String>();
	private Map<String, String> cachedProperties = new HashMap<String, String>();
	// enable save for most parameters
	// private boolean enableSave = false;

	private String controllerSwitcher;// Limit to "ture" or "false"
	private String billURL;
	private String timeToUpdate; // Limit input to Double
	private String timeToStartCalculation;// Limit input to Double
	private String numberOfHAPs; // limit input to Integer
	private String ratingCalculationParameter_P1;// Limit input to Double
	private String ratingCalculationParameter_P2;// Limit input to Double
	private String ratingCalculationParameter_P3; // Limit input to Double
	private String availableDownloadBandwidth;// Limit input to Double
	private String availableUploadBandwidth;// Limit input to Double
	private String increaseUploadBandwidth;// Limit input to Double
	private String increaseDownloaddBandwidth;// Limit input to Double
	private String ratingThreshold;// Limit input to Double

	// constructor
	public HapConfigBean() {
	}

	// Getter and Setter
	// ---------------------------------------------------------------------------------------

	public String getControllerSwitcher() {
		// This trigger read ALL IoP parameters each time refresh page
		if (FacesContext.getCurrentInstance().getRenderResponse())
			readFromDB();
		return controllerSwitcher;
	}

	public void setControllerSwitcher(String controllerSwitcher) {
		this.controllerSwitcher = controllerSwitcher;
	}

	public String getBillURL() {
		return billURL;
	}

	public void setBillURL(String billURL) {
		this.billURL = billURL;
	}

	public String getTimeToUpdate() {
		return timeToUpdate;
	}

	public void setTimeToUpdate(String timeToUpdate) {
		this.timeToUpdate = timeToUpdate;
	}

	public String getTimeToStartCalculation() {
		return timeToStartCalculation;
	}

	public void setTimeToStartCalculation(String timeToStartCalculation) {
		this.timeToStartCalculation = timeToStartCalculation;
	}

	public String getNumberOfHAPs() {
		return numberOfHAPs;
	}

	public void setNumberOfHAPs(String numberOfHAPs) {
		this.numberOfHAPs = numberOfHAPs;
	}

	public String getRatingCalculationParameter_P1() {
		return ratingCalculationParameter_P1;
	}

	public void setRatingCalculationParameter_P1(
			String ratingCalculationParameterP1) {
		ratingCalculationParameter_P1 = ratingCalculationParameterP1;
	}

	public String getRatingCalculationParameter_P2() {
		return ratingCalculationParameter_P2;
	}

	public void setRatingCalculationParameter_P2(
			String ratingCalculationParameterP2) {
		ratingCalculationParameter_P2 = ratingCalculationParameterP2;
	}

	public String getRatingCalculationParameter_P3() {
		return ratingCalculationParameter_P3;
	}

	public void setRatingCalculationParameter_P3(
			String ratingCalculationParameterP3) {
		ratingCalculationParameter_P3 = ratingCalculationParameterP3;
	}

	public String getAvailableDownloadBandwidth() {
		return availableDownloadBandwidth;
	}

	public void setAvailableDownloadBandwidth(String availableDownloadBandwidth) {
		this.availableDownloadBandwidth = availableDownloadBandwidth;
	}

	public String getAvailableUploadBandwidth() {
		return availableUploadBandwidth;
	}

	public void setAvailableUploadBandwidth(String availableUploadBandwidth) {
		this.availableUploadBandwidth = availableUploadBandwidth;
	}

	public String getIncreaseUploadBandwidth() {
		return increaseUploadBandwidth;
	}

	public void setIncreaseUploadBandwidth(String increaseUploadBandwidth) {
		this.increaseUploadBandwidth = increaseUploadBandwidth;
	}

	public String getIncreaseDownloaddBandwidth() {
		return increaseDownloaddBandwidth;
	}

	public void setIncreaseDownloaddBandwidth(String increaseDownloaddBandwidth) {
		this.increaseDownloaddBandwidth = increaseDownloaddBandwidth;
	}

	public String getRatingThreshold() {
		return ratingThreshold;
	}

	public void setRatingThreshold(String ratingThreshold) {
		this.ratingThreshold = ratingThreshold;
	}

	// wrap true and false in a array for radio selection
	public SelectItem[] getControllerSwitcherMode() {

		SelectItem[] definitions = { new SelectItem("false", "OFF"),
				new SelectItem("true", "ON") };
		return definitions;
	}

	// Form actions
	// -------------------------------------------------------------------------------
	/**
	 * retrieval parameters if existed in DB and show on UI used on init, after
	 * save iopProperties is stored params Map
	 */
	public void readFromDB() {
		ComponentConfigEntry ConfigEntry = new ComponentConfigEntry();
		ConfigEntry.setComponent(COMPONENT_NAME);
		List<ComponentConfigEntry> storedConfigEntries = dataDAO
				.get(ConfigEntry);
		storedProperties.clear();
		for (ComponentConfigEntry e : storedConfigEntries) {
			storedProperties.put(e.getPropName(), (String) e.getValue());
		}
		restoreValues();

	}

	/**
	 * assign retrievaled params to UI inputs if contained used on initial and
	 * cancelsave
	 */
	public void restoreValues() {
		for (Map.Entry<String, String> e : storedProperties.entrySet()) {
			// storedValuePairs.put(property,value );
			if (e.getKey().equals(PARAM_HAP_CONTROLLER_ON_OFF)) {
				controllerSwitcher = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_BILLING_URL)) {
				billURL = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_T_UPDATE)) {
				timeToUpdate = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_T)) {
				timeToStartCalculation = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_N)) {
				numberOfHAPs = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_P1)) {
				ratingCalculationParameter_P1 = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_P2)) {
				ratingCalculationParameter_P2 = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_P3)) {
				ratingCalculationParameter_P3 = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_AD)) {
				availableDownloadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_AU)) {
				availableUploadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_UI)) {
				increaseUploadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_DI)) {
				increaseDownloaddBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_HAP_CONTROLLER_RT)) {
				ratingThreshold = e.getValue();
			}
		}
	}

	/**
	 * read input from UI and store into a temp MAP
	 */
	public void retrieveInput() {
		cachedProperties.put(PARAM_HAP_CONTROLLER_ON_OFF, controllerSwitcher);
		cachedProperties.put(PARAM_HAP_BILLING_URL, billURL);
		cachedProperties.put(PARAM_HAP_CONTROLLER_T_UPDATE, timeToUpdate);
		cachedProperties.put(PARAM_HAP_CONTROLLER_T, timeToStartCalculation);
		cachedProperties.put(PARAM_HAP_CONTROLLER_N, numberOfHAPs);
		cachedProperties.put(PARAM_HAP_CONTROLLER_P1,
				ratingCalculationParameter_P1);
		cachedProperties.put(PARAM_HAP_CONTROLLER_P2,
				ratingCalculationParameter_P2);
		cachedProperties.put(PARAM_HAP_CONTROLLER_P3,
				ratingCalculationParameter_P3);
		cachedProperties.put(PARAM_HAP_CONTROLLER_AD,
				availableDownloadBandwidth);
		cachedProperties.put(PARAM_HAP_CONTROLLER_AU, availableUploadBandwidth);
		cachedProperties.put(PARAM_HAP_CONTROLLER_UI, increaseUploadBandwidth);
		cachedProperties.put(PARAM_HAP_CONTROLLER_DI,
				increaseDownloaddBandwidth);
		cachedProperties.put(PARAM_HAP_CONTROLLER_RT, ratingThreshold);
	}

	public void saveChange() {
		retrieveInput();
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
					ConfigEntry.setComponent(COMPONENT_NAME);
					ConfigEntry.setPropName(property);
					try {
						ConfigEntry = dataDAO.get(ConfigEntry).get(0);
						dataDAO.remove(ConfigEntry);
					} catch (Exception e) {
						ToolSet.setErrorMessage(e.getMessage() + " Cause: "
								+ e.getCause());
						e.printStackTrace();
					}
					feedback_deleted = feedback_deleted + property + "<br>";
				}
				// value is not changed, return
				else if (storedProperties.get(property).equals(value)) {
				}
				// value change, update
				else {
					ConfigEntry = new ComponentConfigEntry();
					ConfigEntry.setComponent(COMPONENT_NAME);
					ConfigEntry.setPropName(property);
					storedProperties.put(property, value);
					try {
						ConfigEntry = dataDAO.get(ConfigEntry).get(0);
						ConfigEntry.setValue(value);
						dataDAO.update(ConfigEntry);
						feedback_updated = feedback_updated + property + "<br>";
					} catch (Exception e) {
						ToolSet.setErrorMessage(e.getMessage() + " Cause: "
								+ e.getCause());
						e.printStackTrace();
					}
				}

			}
			// add new property
			else if (value.length() > 0) {
				ConfigEntry = new ComponentConfigEntry(COMPONENT_NAME,
						property, value);
				storedProperties.put(property, value);
				try {
					dataDAO.persist(ConfigEntry);
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
				}
				feedback_add = feedback_add + property + "<br>";
			}
		}
		// reset
		cachedProperties.clear();
		String feedback_total = "";
		if (feedback_add.length() > 0)
			feedback_total = feedback_total + "<tr>Added<td></td><td>"
					+ feedback_add + "</td></tr>";
		if (feedback_deleted.length() > 0)
			feedback_total = feedback_total + "<tr>Deleted<td></td><td>"
					+ feedback_deleted + "</td></tr>";
		if (feedback_updated.length() > 0)
			feedback_total = feedback_total + "<tr>Updated<td></td><td>"
					+ feedback_updated + "</td></tr>";
		if (feedback_total.length() > 0) {
			ToolSet.setErrorMessage("<table>" + feedback_total + "</table>");
		} else
			ToolSet.setErrorMessage("NO Change submitted");
	}

	public void cancelChange() {
		// restore view using persisted value in DB.
		restoreValues();
		ToolSet.refreshPage();
	}
}
