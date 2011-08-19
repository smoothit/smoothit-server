package eu.smoothit.sis.admin.backendBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import eu.smoothit.sis.admin.backendBean.superclass.ComponentConfigAbstract;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;

public class MeteringConfigBean extends ComponentConfigAbstract {
	// initialization
	private Collection<SelectItem> IpRangSources;
	private List<SelectItem> ipRangeOptions;

	public MeteringConfigBean() {
		IpRangSources = new ArrayList<SelectItem>();
		IpRangSources.add(new SelectItem(METERING_VAL_IP_INFO_SRC_DB));
		IpRangSources.add(new SelectItem(METERING_VAL_IP_INFO_SRC_FILE));
		IpRangSources.add(new SelectItem(METERING_VAL_IP_INFO_SRC_ROUTER));
		// add UI menu for BGP source selection
		ipRangeOptions = new ArrayList<SelectItem>();
		ipRangeOptions.add(new SelectItem(METERING_PROP_LOCAL_IP_INFO_SRC,
				"Local"));
		ipRangeOptions.add(new SelectItem(METERING_PROP_REMOTE_IP_INFO_SRC,
				"Remote"));
	}

	// Constants
	// ----------------------------------------------------------------------------------
	public static final String METERING_PROP_REMOTE_IP_INFO_SRC = "Metering-Remote-IP-Info-Src";
	public static final String METERING_PROP_LOCAL_IP_INFO_SRC = "Metering-Local-IP-Info-Src";
	public static final String COMPONENT_NAME = eu.smoothit.sis.init.web.SisWebInitializer.COMPONENT_NAME_METERING; // COMPONENT_NAME_METERING;
	public static final String METERING_PROP_REFRESH_RATE = "Metering-Refresh-Rate";
	// source options for both remote and local
	public static final String METERING_VAL_IP_INFO_SRC_DB = eu.smoothit.sis.init.web.SisWebInitializer.METERING_VAL_IP_INFO_SRC_DB; // "DB";
	public static final String METERING_VAL_IP_INFO_SRC_ROUTER = eu.smoothit.sis.init.web.SisWebInitializer.METERING_VAL_IP_INFO_SRC_ROUTER; // "Router";
	public static final String METERING_VAL_IP_INFO_SRC_FILE = eu.smoothit.sis.init.web.SisWebInitializer.METERING_VAL_IP_INFO_SRC_FILE; // "File";
	public static final String initialSourceValueWhenNonSelected = "Select from left menu";
	// remote file
	public static final String METERING_PROP_BGP_FILE_NAME = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_BGP_FILE_NAME;// "Metering-BGP-File-Name";
	public static final String METERING_PROP_LOCAL_FILE_NAME = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_LOCAL_FILE_NAME;// "Metering-BGP-Local-File-Name";
	// Router
	public static final String METERING_PROP_BGP_ROUTER_ADDRESS = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_BGP_ROUTER_ADDRESS;// "Metering-BGP-Router-Address";
	public static final String METERING_PROP_BGP_ROUTER_PORT = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_BGP_ROUTER_PORT;// "Metering-BGP-Router-Port";
	public static final String METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY;// "Metering-BGP-Router-SNMP-Community";
	public static final String METERING_PROP_OSPF_ROUTER_ADDRESS = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_OSPF_ROUTER_ADDRESS;// "Metering-OSPF-Router-Address";
	public static final String METERING_PROP_OSPF_ROUTER_PORT = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_OSPF_ROUTER_PORT;// "Metering-OSPF-Router-Port";
	public static final String METERING_PROP_OSPF_ROUTER_SNMP_COMMUNITY = eu.smoothit.sis.init.web.SisWebInitializer.METERING_PROP_OSPF_ROUTER_SNMP_COMMUNITY;// "Metering-OSPF-Router-SNMP-Community";
	// Properties
	Map<String, String> storedProperties = new HashMap<String, String>();
	private Map<String, String> cachedProperties = new HashMap<String, String>();
	// ---------------------------------------------------------------------------------------
	// refresh rate
	private String refreshRate;
	private String ipRangeOption;
	private boolean ipRangeOptionSelected = false;
	private boolean ipRangeSourceEditable = false;
	private String selectIpRangeOption;
	// file path config related
	private String fileName;
	private String routerAddress;
	private String routerPort;
	private String routerSNMP;
	// ip range config
	private String selectIpRangeSource;
	// used for ajax enabled save, cancel button when value changed/reset
	private String selectIpRangeSource_persisted;

	// Router config parameters

	// getter & setter-------------------------------------------------
	public String getRefreshRate() {
		// read all Metering parameters each time refresh page
		if (FacesContext.getCurrentInstance().getRenderResponse())
			readFromDB();
		return refreshRate;
	}

	public void setRefreshRate(String refreshRate) {
		this.refreshRate = refreshRate;

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRouterAddress() {
		return routerAddress;
	}

	public void setRouterAddress(String routerAddress) {
		this.routerAddress = routerAddress;
	}

	public String getRouterPort() {
		return routerPort;
	}

	public void setRouterPort(String routerPort) {
		this.routerPort = routerPort;
	}

	public String getRouterSNMP() {
		return routerSNMP;
	}

	public void setRouterSNMP(String routerSNMP) {
		this.routerSNMP = routerSNMP;
	}

	public void setIpRangeOptions(List<SelectItem> ipRangeOptions) {
		this.ipRangeOptions = ipRangeOptions;
	}

	public List<SelectItem> getIpRangeOptions() {
		return ipRangeOptions;
	}

	public boolean getIpRangeSourceEditable() {
		return ipRangeSourceEditable;
	}

	// replaced with selectIpRangeSource
	private String ipRangeSource;

	public String getIpRangeOption() {
		return ipRangeOption;
	}

	public void setIpRangeOption(String ipRangeOption) {
		this.ipRangeOption = ipRangeOption;
	}

	public String getSelectIpRangeOption() {
		return selectIpRangeOption;
	}

	public void setSelectIpRangeOption(String selectIpRangeOption) {
		this.selectIpRangeOption = selectIpRangeOption;
	}

	public String getIpRangeSource() {

		return ipRangeSource;
	}

	public void setIpRangeSource(String ipRangeSource) {
		this.ipRangeSource = ipRangeSource;
	}

	public Collection<SelectItem> getIpRangeSources() {
		return IpRangSources;
	}

	// file path
	public String getSelectIpRangeSource() {
		return selectIpRangeSource;
	}

	public void setSelectIpRangeSource(String selectIpRangeSource) {
		this.selectIpRangeSource = selectIpRangeSource;
	}

	public boolean isIpRangeSourceEditable() {
		return ipRangeSourceEditable;
	}

	public boolean isIpRangeOptionSelected() {
		return ipRangeOptionSelected;
	}

	public void setIpRangeOptionSelected(boolean ipRangeOptionSelected) {
		this.ipRangeOptionSelected = ipRangeOptionSelected;
	}

	// Form actions
	// -------------------------------------------------------------------------------

	public void editIpRangeSource() {
		ipRangeSourceEditable = true;
	}

	public void cancelIpRangeSourceChange() {
		ipRangeSourceEditable = false;
	}

	// Listener
	// -----------------------------------------------------------------

	String sourceForRemoteOption;
	String sourceForLocalOption;
	boolean fileFlag = false;
	boolean routerFlag = false;
	boolean dbFlag = false;

	public boolean getFileFlag() {
		return fileFlag;
	}

	public boolean getRouterFlag() {
		return routerFlag;
	}

	public boolean getDbFlag() {
		return dbFlag;
	}

	public void ipRangeOptionChangeListener(ValueChangeEvent event) {
		// reset
		selectIpRangeSource = "";
		fileFlag = false;
		routerFlag = false;
		dbFlag = false;
		// if previous action of change source not saved,when change option,
		selectIpRangeOption = (String) event.getNewValue();
		restoreValues();
		if (selectIpRangeSource == null) {
		} else if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_FILE)) {
			fileFlag = true;
			routerFlag = false;
			dbFlag = false;
		} else if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_ROUTER)) {
			routerFlag = true;
			fileFlag = false;
			dbFlag = false;
		} else if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_DB)) {
			routerFlag = false;
			fileFlag = false;
			dbFlag = true;
		}
		// advance submit progress
		ToolSet.advanceSubmit(event);
	}

	public void ipRangeSourceChangeListener(ValueChangeEvent event) {
		routerFlag = false;
		fileFlag = false;
		dbFlag = false;

	}

	public void restoreValue_part2() {
		// assign Router/File values
		for (Map.Entry<String, String> e : storedProperties.entrySet()) {
			if (selectIpRangeOption != null) {
				if (selectIpRangeOption.equals(METERING_PROP_LOCAL_IP_INFO_SRC)) {
					if (e.getKey().equals(METERING_PROP_LOCAL_FILE_NAME)) {
						fileName = e.getValue();
					} else if (e.getKey().equals(
							METERING_PROP_OSPF_ROUTER_ADDRESS)) {
						routerAddress = e.getValue();
					} else if (e.getKey()
							.equals(METERING_PROP_OSPF_ROUTER_PORT)) {
						routerPort = e.getValue();
					} else if (e.getKey().equals(
							METERING_PROP_OSPF_ROUTER_SNMP_COMMUNITY)) {
						routerSNMP = e.getValue();
					}
				} else if (selectIpRangeOption
						.equals(METERING_PROP_REMOTE_IP_INFO_SRC)) {
					if (e.getKey().equals(METERING_PROP_BGP_FILE_NAME)) {
						fileName = e.getValue();
					} else if (e.getKey().equals(
							METERING_PROP_BGP_ROUTER_ADDRESS)) {
						routerAddress = e.getValue();
					} else if (e.getKey().equals(METERING_PROP_BGP_ROUTER_PORT)) {
						routerPort = e.getValue();
					} else if (e.getKey().equals(
							METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY)) {
						routerSNMP = e.getValue();
					}
				}
			}
		}
	}

	public void saveIpRangOptionWithIpRangeSource() {
		if (selectIpRangeSource != null && selectIpRangeSource.length() > 0) {
			saveComponentConfigEntry(COMPONENT_NAME, selectIpRangeOption,
					selectIpRangeSource);
			selectIpRangeSource_persisted = selectIpRangeSource;
			ToolSet.setErrorMessage(selectIpRangeOption
					+ " now is associated to " + selectIpRangeSource);
			// show DB,File or router interface according to option
			if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_FILE)) {
				fileFlag = true;
				routerFlag = false;
				dbFlag = false;
			} else if (selectIpRangeSource
					.equals(METERING_VAL_IP_INFO_SRC_ROUTER)) {
				routerFlag = true;
				fileFlag = false;
				dbFlag = false;
			} else if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_DB)) {
				routerFlag = false;
				fileFlag = false;
				dbFlag = true;
			}
		}
		// ipRangeSourceValueReassigned = false;
	}

	public void cancelIpRangOptionWithIpRangeSource() {
		// refresh
		selectIpRangeSource = selectIpRangeSource_persisted;
	}

	/**
	 * assign retrievaled params to UI inputs if contained used on initial and
	 * cancelsave
	 */
	public void restoreValues() {
		// reset
		sourceForRemoteOption = null;
		sourceForLocalOption = null;
		selectIpRangeSource = null;
		// retrive stored value
		if (storedProperties.containsKey(METERING_PROP_REFRESH_RATE))
			refreshRate = storedProperties.get(METERING_PROP_REFRESH_RATE);
		if (storedProperties.containsKey(METERING_PROP_REMOTE_IP_INFO_SRC))
			sourceForRemoteOption = storedProperties
					.get(METERING_PROP_REMOTE_IP_INFO_SRC);
		if (storedProperties.containsKey(METERING_PROP_LOCAL_IP_INFO_SRC))
			sourceForLocalOption = storedProperties
					.get(METERING_PROP_LOCAL_IP_INFO_SRC);
		// selectIpRangeOption=null only for the first time
		if (selectIpRangeOption != null) {
			// when select remote
			if (selectIpRangeOption.equals(METERING_PROP_REMOTE_IP_INFO_SRC)) {
				if (sourceForRemoteOption != null) {
					selectIpRangeSource = sourceForRemoteOption;
				}
			}
			// when select local
			else if (selectIpRangeOption
					.equals(METERING_PROP_LOCAL_IP_INFO_SRC)) {
				if (sourceForLocalOption != null) {
					selectIpRangeSource = sourceForLocalOption;
				}
			}
			// if remote
			if (storedProperties.containsKey(METERING_PROP_REMOTE_IP_INFO_SRC)) {
				sourceForRemoteOption = storedProperties
						.get(METERING_PROP_REMOTE_IP_INFO_SRC);
				// in case of update from CC interface
				if (selectIpRangeOption
						.equals(METERING_PROP_REMOTE_IP_INFO_SRC)) {
					selectIpRangeSource = sourceForRemoteOption;
				}
			}
			if (storedProperties.containsKey(METERING_PROP_LOCAL_IP_INFO_SRC)) {
				sourceForLocalOption = storedProperties
						.get(METERING_PROP_LOCAL_IP_INFO_SRC);
				// in case of update from CC interface
				if (selectIpRangeOption.equals(METERING_PROP_LOCAL_IP_INFO_SRC)) {
					selectIpRangeSource = sourceForLocalOption;
				}
			}
		}
	}

	/**
	 * retrieval parameters if existed in DB and show on UI used on init, after
	 * saveProperties is stored params Map
	 */
	public void readFromDB() {
		// reset
		storedProperties.clear();
		ComponentConfigEntry ConfigEntry = new ComponentConfigEntry();
		ConfigEntry.setComponent(COMPONENT_NAME);
		List<ComponentConfigEntry> storedConfigEntries = dataDAO
				.get(ConfigEntry);
		for (ComponentConfigEntry e : storedConfigEntries) {
			storedProperties.put(e.getPropName(), (String) e.getValue());
		}
		// read refreshrate, source association from db
		restoreValues();
		// read router/file from db
		restoreValue_part2();
	}

	/**
	 * read input from UI and store into a temp MAP
	 */
	private void retrieveInput() {

		if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_FILE)) {
			if (selectIpRangeOption.equals(METERING_PROP_LOCAL_IP_INFO_SRC)) {
				cachedProperties.put(METERING_PROP_LOCAL_FILE_NAME, fileName);
			} else {
				cachedProperties.put(METERING_PROP_BGP_FILE_NAME, fileName);
			}
		} else if (selectIpRangeSource.equals(METERING_VAL_IP_INFO_SRC_ROUTER)) {
			if (selectIpRangeOption.equals(METERING_PROP_LOCAL_IP_INFO_SRC)) {
				cachedProperties.put(METERING_PROP_OSPF_ROUTER_ADDRESS,
						routerAddress);
				cachedProperties
						.put(METERING_PROP_OSPF_ROUTER_PORT, routerPort);
				cachedProperties.put(METERING_PROP_OSPF_ROUTER_SNMP_COMMUNITY,
						routerSNMP);
			} else {
				cachedProperties.put(METERING_PROP_BGP_ROUTER_ADDRESS,
						routerAddress);
				cachedProperties.put(METERING_PROP_BGP_ROUTER_PORT, routerPort);
				cachedProperties.put(METERING_PROP_BGP_ROUTER_SNMP_COMMUNITY,
						routerSNMP);

			}
		}
	}

	public void saveRefreshRate() {
		// get value back equal:no change, "":remove, update, add
		ComponentConfigEntry ConfigEntry;
		// property of refresh rate exists
		if (storedProperties.containsKey(METERING_PROP_REFRESH_RATE)) {
			// value is empty, remove the property
			if (refreshRate.length() == 0) {
				storedProperties.remove(METERING_PROP_REFRESH_RATE);
				ConfigEntry = new ComponentConfigEntry();
				ConfigEntry.setComponent(COMPONENT_NAME);
				ConfigEntry.setPropName(METERING_PROP_REFRESH_RATE);
				try {
					ConfigEntry = dataDAO.get(ConfigEntry).get(0);
					dataDAO.remove(ConfigEntry);
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
				}
				ToolSet.setErrorMessage("RefreshRate Deleted");
			}
			// value is not changed, return
			else if (storedProperties.get(METERING_PROP_REFRESH_RATE).equals(
					refreshRate)) {
			}
			// value change, update
			else {
				ConfigEntry = new ComponentConfigEntry();
				ConfigEntry.setComponent(COMPONENT_NAME);
				ConfigEntry.setPropName(METERING_PROP_REFRESH_RATE);
				storedProperties.put(METERING_PROP_REFRESH_RATE, refreshRate);
				try {
					ConfigEntry = dataDAO.get(ConfigEntry).get(0);
					ConfigEntry.setValue(refreshRate);
					dataDAO.update(ConfigEntry);
					ToolSet.setErrorMessage("RefreshRate Updated");
				} catch (Exception e) {
					ToolSet.setErrorMessage(e.getMessage() + " Cause: "
							+ e.getCause());
					e.printStackTrace();
				}
			}

		}
		// add new property
		else if (refreshRate.length() > 0) {
			ConfigEntry = new ComponentConfigEntry(COMPONENT_NAME,
					METERING_PROP_REFRESH_RATE, refreshRate);
			storedProperties.put(METERING_PROP_REFRESH_RATE, refreshRate);
			try {
				dataDAO.persist(ConfigEntry);
			} catch (Exception e) {
				ToolSet.setErrorMessage(e.getMessage() + " Cause: "
						+ e.getCause());
				e.printStackTrace();
			}
			ToolSet.setErrorMessage("RefreshRate Added");
		}

	}

	// reset
	public void saveParameters() {
		retrieveInput();
		saveComponentConfigEntries(cachedProperties, storedProperties,
				COMPONENT_NAME);
	}

	public void saveChangeAborted() {
		retrieveInput();
		String property;
		String value;
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
					feedback_deleted = feedback_deleted + property + ";";
				}
				// value is not changed, return
				else if (storedProperties.get(property).equals(value)) {
					// feedback=feedback+"no change:" + property+"\n" ;
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
						feedback_updated = feedback_updated + property + ";";
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
				feedback_add = feedback_add + property + ";";
			}

		}
		// reset
		cachedProperties.clear();
		String feedback_total = "";
		if (feedback_add.length() > 0)
			feedback_total = feedback_total + "Added:" + feedback_add + "<br>";
		if (feedback_deleted.length() > 0)
			feedback_total = feedback_total + "Deleted:" + feedback_deleted
					+ "<br>";
		if (feedback_updated.length() > 0)
			feedback_total = feedback_total + "Updated:" + feedback_updated
					+ "<br>";
		if (feedback_total.length() > 0)
			ToolSet.setErrorMessage(feedback_total);
		else
			ToolSet.setErrorMessage("NO Change submitted");
	}

	public void cancelChange() {
		ToolSet.refreshPage();
	}

}
