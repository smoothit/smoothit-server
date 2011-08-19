package eu.smoothit.sis.admin.backendBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.ComponentConfigAbstract;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

public class IoPConfigBean extends ComponentConfigAbstract {
	// Constants
	// ----------------------------------------------------------------------------------
	// public static final String IOP_Nr_TOP_TORRENTS =
	public static final String COMPONENT_NAME = eu.smoothit.sis.init.web.SisWebInitializer.COMPONENT_NAME_CONTR_IOP;// "Controler-IoP-Config"
	// IoP operation mode
	public static final String PARAM_IOP_OPERATION_MODE = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_OPERATION_MODE;// "IoP-Operation-Mode";
	public static final String VALUE_IOP_OPERATION_MODE_PLAIN = eu.smoothit.sis.init.web.SisWebInitializer.VALUE_IOP_OPERATION_MODE_PLAIN; // "Plain";
	public static final String VALUE_IOP_OPERATION_MODE_COMBO = eu.smoothit.sis.init.web.SisWebInitializer.VALUE_IOP_OPERATION_MODE_COMBO; // "Combination";
	public static final String VALUE_IOP_OPERATION_MODE_COLLAB = eu.smoothit.sis.init.web.SisWebInitializer.VALUE_IOP_OPERATION_MODE_COLLAB; // "Collaboration";
	// Enable/disable to connect to remote peers
	public static final String PARAM_IOP_REMOTE_CONNECTION_FLAG = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_REMOTE_CONNECTION_FLAG; // ""IoP-Remote-Connection-Flag"";
	// private String PARAM_IOP_REMOTE_CONNECTION_FLAG_PERSISTED;
	// Number of unchoking slots per swarm
	public static final String PARAM_IOP_UNCHOKING_SLOTS = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_UNCHOKING_SLOTS; // "IoP-Unchoking-Slots"
	// private String PARAM_IOP_UNCHOKING_SLOTS_PERSISTED;
	// time period to ask the SIS for new torrent statistics
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_T = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_T;// "IoP-SIS-Swarm-Selection-T";
	public static final String PARAM_IOP_CONTROLLER_T_OUT = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_CONTROLLER_T_OUT;// "IoP-Controller-T-Out"
	public static final String PARAM_IOP_CONTROLLER_T_AGE = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_CONTROLLER_T_AGE;// "IoP-Controller-T-Age"
	// Lower bounds for upload and download bandwidth
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_ULOW = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_ULOW; // "IoP-SIS-Swarm-Selection-ULow";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_DLOW = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_DLOW; // "IoP-SIS-Swarm-Selection-DLow";
	// Total upload and download bandwidth
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_U = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_U;// "IoP-SIS-Swarm-Selection-U";
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_D = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_D;// "IoP-SIS-Swarm-Selection-D";
	// /the percentage
	public static final String PARAM_IOP_SIS_SWARM_SELECTION_X = eu.smoothit.sis.init.web.SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_X;// "IoP-SIS-Swarm-Selection-X";

	// wrap operation mode values in a array for radio selection
	public SelectItem[] getOperationModeOptions() {
		SelectItem[] definitions = new SelectItem[3];
		definitions[0] = new SelectItem(VALUE_IOP_OPERATION_MODE_PLAIN, "Plan",
				"This is for Plan mode", false);
		definitions[1] = new SelectItem(VALUE_IOP_OPERATION_MODE_COMBO,
				"Combine", "This is for Plan mode", false);
		definitions[2] = new SelectItem(VALUE_IOP_OPERATION_MODE_COLLAB,
				"Collab", "This is for Plan mode", false);
		return definitions;
	}

	// wrap true and false in a array for radio selection
	public SelectItem[] getRemoteConnectionOptions() {
		SelectItem[] definitions = { new SelectItem("false", "Disable"),
				new SelectItem("true", "Enable") };
		return definitions;
	}

	// ----------------------------------------------------------------------------------
	// Constants end

	// Properties
	// ---------------------------------------------------------------------------------
	Map<String, String> storedProperties = new HashMap<String, String>();
	private Map<String, String> cachedProperties = new HashMap<String, String>();
	// enable save for most parameters
	// private boolean enableSave = false;
	private String operationMode;// Limit to String
	// "Plan","Combination","Collaboration"
	private String remoteConnectionFlag; // Limit to "true" or "false"
	private String numberOfUnchokingSlots; // Limit input to Integer

	private String timePeriodforStatistics;// Limit input to Double
	private String timePeriodforOut;// Limit input to Double
	private String timePeriodforAge;// Limit input to Double

	private String lowerBoundsForUploadBandwidth;// Limit input to Double
	private String lowerBoundsForDownloadBandwidth;// Limit input to Double
	private String totalUploadBandwidth;// Limit input to Double
	private String totalDownloadBandwidth;// Limit input to Double
	private String thePercentageValue;// Limit input to Double

	// constructor
	public IoPConfigBean() {
	}

	// Getter and Setter
	// ---------------------------------------------------------------------------------------
	public Map<String, String> getCachedProperties() {
		return cachedProperties;
	}
	public String getOperationMode() {
		// This trigger read ALL IoP parameters each time refresh page
		if (FacesContext.getCurrentInstance().getRenderResponse())
			readFromDB();

		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getRemoteConnectionFlag() {
		return remoteConnectionFlag;
	}

	public void setRemoteConnectionFlag(String remoteConnectionFlag) {
		this.remoteConnectionFlag = remoteConnectionFlag;
	}

	public String getNumberOfUnchokingSlots() {
		return numberOfUnchokingSlots;
	}

	public void setNumberOfUnchokingSlots(String numberOfUnchokingSlots) {
		this.numberOfUnchokingSlots = numberOfUnchokingSlots;
	}

	public String getTimePeriodforStatistics() {
		return timePeriodforStatistics;
	}

	public void setTimePeriodforStatistics(String timePeriodforStatistics) {
		this.timePeriodforStatistics = timePeriodforStatistics;
	}

	public String getTimePeriodforOut() {
		return timePeriodforOut;
	}

	public void setTimePeriodforOut(String timePeriodforOut) {
		this.timePeriodforOut = timePeriodforOut;
	}

	public String getTimePeriodforAge() {
		return timePeriodforAge;
	}

	public void setTimePeriodforAge(String timePeriodforAge) {
		this.timePeriodforAge = timePeriodforAge;
	}

	public String getLowerBoundsForUploadBandwidth() {
		return lowerBoundsForUploadBandwidth;
	}

	public void setLowerBoundsForUploadBandwidth(
			String lowerBoundsForUploadBandwidth) {
		this.lowerBoundsForUploadBandwidth = lowerBoundsForUploadBandwidth;
	}

	public String getLowerBoundsForDownloadBandwidth() {
		return lowerBoundsForDownloadBandwidth;
	}

	public void setLowerBoundsForDownloadBandwidth(
			String lowerBoundsForDownloadBandwidth) {
		this.lowerBoundsForDownloadBandwidth = lowerBoundsForDownloadBandwidth;
	}

	public String getTotalUploadBandwidth() {
		return totalUploadBandwidth;
	}

	public void setTotalUploadBandwidth(String totalUploadBandwidth) {
		this.totalUploadBandwidth = totalUploadBandwidth;
	}

	public String getTotalDownloadBandwidth() {
		return totalDownloadBandwidth;
	}

	public void setTotalDownloadBandwidth(String totalDownloadBandwidth) {
		this.totalDownloadBandwidth = totalDownloadBandwidth;
	}

	public String getThePercentageValue() {
		return thePercentageValue;
	}

	public void setThePercentageValue(String thePercentageValue) {
		this.thePercentageValue = thePercentageValue;
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
			if (e.getKey().equals(PARAM_IOP_OPERATION_MODE)) {
				operationMode = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_REMOTE_CONNECTION_FLAG)) {
				remoteConnectionFlag = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_UNCHOKING_SLOTS)) {
				numberOfUnchokingSlots = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_SIS_SWARM_SELECTION_T)) {
				timePeriodforStatistics = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_CONTROLLER_T_OUT)) {
				timePeriodforOut = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_CONTROLLER_T_AGE)) {
				timePeriodforAge = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_SIS_SWARM_SELECTION_ULOW)) {
				lowerBoundsForUploadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_SIS_SWARM_SELECTION_DLOW)) {
				lowerBoundsForDownloadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_SIS_SWARM_SELECTION_U)) {
				totalUploadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_SIS_SWARM_SELECTION_D)) {
				totalDownloadBandwidth = e.getValue();
			} else if (e.getKey().equals(PARAM_IOP_SIS_SWARM_SELECTION_X)) {
				thePercentageValue = e.getValue();
			}
		}
	}

	/**
	 * read input from UI and store into a temp MAP
	 */
	public void retrieveInput() {
		cachedProperties.put(PARAM_IOP_OPERATION_MODE, operationMode);
		cachedProperties.put(PARAM_IOP_REMOTE_CONNECTION_FLAG,
				remoteConnectionFlag);
		cachedProperties.put(PARAM_IOP_UNCHOKING_SLOTS, numberOfUnchokingSlots);
		cachedProperties.put(PARAM_IOP_SIS_SWARM_SELECTION_T,
				timePeriodforStatistics);
		cachedProperties.put(PARAM_IOP_CONTROLLER_T_OUT, timePeriodforOut);
		cachedProperties.put(PARAM_IOP_CONTROLLER_T_AGE, timePeriodforAge);
		cachedProperties.put(PARAM_IOP_SIS_SWARM_SELECTION_ULOW,
				lowerBoundsForUploadBandwidth);
		cachedProperties.put(PARAM_IOP_SIS_SWARM_SELECTION_DLOW,
				lowerBoundsForDownloadBandwidth);
		cachedProperties.put(PARAM_IOP_SIS_SWARM_SELECTION_U,
				totalUploadBandwidth);
		cachedProperties.put(PARAM_IOP_SIS_SWARM_SELECTION_D,
				totalDownloadBandwidth);
		cachedProperties.put(PARAM_IOP_SIS_SWARM_SELECTION_X,
				thePercentageValue);
	}

	public void saveParameters() {
		retrieveInput();
		saveComponentConfigEntries(cachedProperties, storedProperties,
				COMPONENT_NAME);
	}

	public void cancelChange() {
		// restore view using persisted value in DB.
		restoreValues();
		ToolSet.refreshPage();
	}

}
