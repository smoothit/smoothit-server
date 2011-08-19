package eu.smoothit.sis.admin.backendBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import eu.smoothit.sis.admin.backendBean.superclass.DataTable;
import eu.smoothit.sis.admin.util.DTOComparator;
import eu.smoothit.sis.admin.util.ToolSet;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;

public class MonitoringViewBean extends DataTable<TorrentHashStatistic> {
	// Properties
	// ---------------------------------------------------------------------------------
	private static final Integer DEFAULT_TOP_Number = 10;
	private Integer numberOfTop = DEFAULT_TOP_Number;
	private String selectedIoPParameter;
	// timeoffset for Torrent
	long timeoffset;
	SisDAOFactory factory = SisDAOFactory.getFactory();
	IPeerStatusDAO peer_status_dao = factory.createPeerStatusDAO();
	// Number of available swarms
	private Integer swarmNrs;
	private List<PeerStatusEntry> associatedLocalPeersListPerSwarm;
	// if show details of related seeds for a swarm id
	private Boolean showDetailsTag = false;

	// related to associated iop, local peers per swarm
	private String infoHash_selectedSwarm;
	private List<PeerStatistic> localPeers_list;
	private List<PeerStatistic> IoP_list;

	// initialization
	public MonitoringViewBean() {
		timeoffset = 1;
	}

	// getter && setter
	// ----------------------------------------------------------------------------------
	public String getInfoHash_selectedSwarm() {
		return infoHash_selectedSwarm;
	}

	public List<PeerStatistic> getLocalPeers_list() {
		return localPeers_list;
	}

	public List<PeerStatistic> getIoP_list() {
		return IoP_list;
	}

	public long getTimeoffset() {
		// convert from unit minute to milisecond
		return timeoffset;
	}

	public void setTimeoffset(long timeoffset) {
		this.timeoffset = timeoffset;
	}

	public Boolean getShowDetailsTag() {
		return showDetailsTag;
	}

	public void setShowDetailsTag(Boolean showDetailsTag) {
		this.showDetailsTag = showDetailsTag;
	}

	public Integer getNumberOfTop() {
		return numberOfTop;
	}

	public void setNumberOfTop(Integer numberOfTop) {
		this.numberOfTop = numberOfTop;
	}

	public String getSelectedIoPParameter() {
		return selectedIoPParameter;
	}

	public void setSelectedIoPParameter(String selectedIoPParamter) {
		this.selectedIoPParameter = selectedIoPParamter;
	}

	public List<PeerStatusEntry> getAssociatedLocalPeersListPerSwarm() {
		return associatedLocalPeersListPerSwarm;
	}

	public void setAssociatedLocalPeersListPerSwarm(
			List<PeerStatusEntry> associatedLocalPeersListPerSwarm) {
		this.associatedLocalPeersListPerSwarm = associatedLocalPeersListPerSwarm;
	}

	public Integer getSwarmNrs() {
		return swarmNrs;
	}

	public void setSwarmNrs(Integer swarmNrs) {
		this.swarmNrs = swarmNrs;
	}

	// Form actions
	// ----------------------------------------------------------------------------------

	/**
	 * show associate peers and IoPs of selected Swarm
	 */
	public void showDetails() {
		// get selected swarm
		TorrentHashStatistic seletedSwarm = (TorrentHashStatistic) dataTable
				.getRowData();
		// swarms related info, TODO show it
		infoHash_selectedSwarm = seletedSwarm.getInfohash();
		localPeers_list = new ArrayList<PeerStatistic>();
		IoP_list = new ArrayList<PeerStatistic>();
		List<List<String>> localPeers_list_String = peer_status_dao
				.getListOfPeers(infoHash_selectedSwarm, timeoffset_stored);
		for (List<String> entry : localPeers_list_String) {
			PeerStatistic ins = new PeerStatistic(entry.get(0), entry.get(1),
					entry.get(2), entry.get(3), entry.get(4));
			localPeers_list.add(ins);
		}
		List<List<String>> IoP_list_String = peer_status_dao.getListOfIops(
				infoHash_selectedSwarm, timeoffset_stored);
		for (List<String> entry : IoP_list_String) {
			PeerStatistic ins = new PeerStatistic(entry.get(0), entry.get(1),
					entry.get(2), entry.get(3), entry.get(4));
			IoP_list.add(ins);
		}
		editModeRows.put(dataTable.getRowIndex(), true);
		showDetailsTag = true;
	}

	public void hideDetails() {
		editModeRows.clear();
		showDetailsTag = false;
	}

	/**
	 * get top N entries based on selected filed
	 */
	public void actionFiltering() {
		dataTable.setRows(getNumberOfTop()); // top X
		sortField = getSelectedIoPParameter(); // "numberOfLeechers";

	}

	@Override
	public void actionRefresh() {
		super.actionRefresh();
	}

	public void sortByLocalPeerDesc() {
		// Get and set sort field and sort order.
		String sortFieldAttribute = "nrOfAssociatedLocalPeers";
		sortField = sortFieldAttribute;
		sortAscending = false;
		if (dataList != null && !dataList.isEmpty())
			Collections.sort(dataList, new DTOComparator(sortField,
					sortAscending));
		// Clear row modes.
		editModeRows.clear();
	}

	@Override
	public void actionDelete() {
		// NOT used

	}

	@Override
	public void actionSave() {
		// NOT used
	}

	public List<TorrentHashStatistic> getDataList() {
		if (FacesContext.getCurrentInstance().getRenderResponse())
			refreshDataList();
		return dataList;
	}

	long timeoffset_stored;

	public void refreshDataList() {
		// enable save button for timeoffset
		// convert from minutes to miliseconds
		timeoffset_stored = timeoffset * 60 * 1000;
		dataList = getStatistics();
	}

	public List<TorrentHashStatistic> getStatistics() {
		TorrentHashStatistic statEntry;
		List<TorrentHashStatistic> statEntries = new ArrayList<TorrentHashStatistic>();
		List<String> torrentHashs = peer_status_dao
				.getTorrents(timeoffset_stored);
		for (String torrent : torrentHashs) {
			List<String> stat = peer_status_dao.getStatForTorrentHash(torrent,
					timeoffset_stored);
			statEntry = new TorrentHashStatistic(stat.get(0), stat.get(1), stat
					.get(2), stat.get(3), stat.get(4), stat.get(5), stat.get(6));
			statEntries.add(statEntry);
		}
		return statEntries;
	}

	public void saveTimeoffset() {
		ToolSet.setErrorMessage("Timeoffset saved");
		setShowDetailsTag(false);
	}

	public void timeoffsetValueChangeListener(ValueChangeEvent event) {
		// advanceSubmit
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

	public static class PeerStatistic {
		private String ipAddress;
		private String port;
		private String progress;
		private String down_rate;
		private String up_rate;

		public String getIpAddress() {
			return ipAddress;
		}

		public String getPort() {
			return port;
		}

		public String getProgress() {
			return progress;
		}

		public String getDown_rate() {
			return down_rate;
		}

		public String getUp_rate() {
			return up_rate;
		}

		public PeerStatistic(String ipAddress, String port, String progress,
				String down_rate, String up_rate) {
			this.ipAddress = ipAddress;
			this.port = port;
			this.progress = progress;
			this.down_rate = down_rate;
			this.up_rate = up_rate;
		}

	}
}

class TorrentHashStatistic {
	private String infohash;
	private String sum_of_down_rate;// Float, use String can avoid default 0
	// shown on page
	private String sum_of_up_rate;
	private String number_of_local_peers;
	private String number_of_leechers;
	private String number_of_seeder;
	private String number_of_IoPs;

	public TorrentHashStatistic(String infohash, String sum_of_down_rate,
			String sum_of_up_rate, String number_of_local_peers,
			String number_of_leechers, String number_of_seeder,
			String number_of_IoPs) {
		this.infohash = infohash;
		this.sum_of_down_rate = sum_of_down_rate;
		this.sum_of_up_rate = sum_of_up_rate;
		this.number_of_local_peers = number_of_local_peers;
		this.number_of_leechers = number_of_leechers;
		this.number_of_seeder = number_of_seeder;
		this.number_of_IoPs = number_of_IoPs;
	}

	public String getInfohash() {
		return infohash;
	}

	public String getSum_of_down_rate() {
		return sum_of_down_rate;
	}

	public String getSum_of_up_rate() {
		return sum_of_up_rate;
	}

	public String getNumber_of_local_peers() {
		return number_of_local_peers;
	}

	public String getNumber_of_leechers() {
		return number_of_leechers;
	}

	public String getNumber_of_seeder() {
		return number_of_seeder;
	}

	public String getNumber_of_IoPs() {
		return number_of_IoPs;
	}
}
