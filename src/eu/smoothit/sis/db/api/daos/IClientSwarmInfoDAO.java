package eu.smoothit.sis.db.api.daos;

import java.util.List;

import eu.smoothit.sis.db.impl.entities.ClientSwarmInfoEntry;

public interface IClientSwarmInfoDAO extends Dao<Long, ClientSwarmInfoEntry> {


	
	public Double getFileSizeForTorrentID(String torrent_id);
	
	public int getNumberOfLocalLeechers(String torrent_id);
	
	public int getNumberOfLocalSeeders(String torrent_id);
	
	public List<String> getSwarmsYoungerThen(Long t_age_timestamp, Boolean flag);
	
	public void setIoPParticipation(String torrent_id, boolean flag);

	public void deleteSwarmsOlderThen(Long t_out_timestamp);

	public List<String> getLocalLeechers(String torrentId);

	public List<String> getLocalSeeders(String torrentId);
	
	
}
