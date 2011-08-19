/**
 * 
 */
package eu.smoothit.sis.db.api.daos;

import java.util.List;

import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;

/**
 * @author christian
 * 
 */
public interface IPeerStatusDAO extends Dao<Long, PeerStatusEntry> {

	/**
	 * return the statistics for a distinct selected torrent swarm
	 * 
	 * @param timestamp
	 *            all torrents greater then a given timestamp are only selected
	 * @param infohash
	 *            the torrent swarm to be selected and for which the statistics
	 *            should be calculated
	 * @return the distinct selected statistics per torrent. It contains the
	 *         following statistics in the following order:0 --> infohash, 1 -->
	 *         sum of down_rate, 2 --> sum of up_rate, 3 --> number of local
	 *         peers, 4 --> number of leechers, 5 --> number of seeders, 6 -->
	 *         number of IoPs
	 */
	public List<String> getStatForTorrentHash(String infohash, long timestamp);

	/**
	 * returns the total down rate within a given swarm
	 * 
	 * @param infohash
	 *            the infohash indicating the swarm
	 * @param timestamp
	 *            all torrents greater then a given timestamp are only selected
	 * @return the total down rate within the given swarm
	 */
	public Double getTotalDownstreamforTorrentHash(String infohash,
			long timestamp);

	/**
	 * returns the total up rate within a given swarm
	 * 
	 * @param infohash
	 *            the infohash indicating the swarm
	 * @param timestamp
	 *            all torrents greater then a given timestamp are only selected
	 * @return the total up rate within the given swarm
	 */
	public Double getTotalUpstreamforTorrentHash(String infohash, long timestamp);

	/**
	 * returns the number of local peers within a given swarm
	 * 
	 * @param infohash
	 *            the infohash indicating the swarm
	 * @param timestamp
	 *            all torrents greater then a given timestamp are only selected
	 * @return the number of local peers within the given swarm
	 */
	public Long getNumberOfLocalPeersForTorrentHash(String infohash,
			long timestamp);

	/**
	 * returns the number of IoP within a given swarm
	 * 
	 * @param infohash
	 *            the infohash indicating the swarm
	 * @param timestamp
	 *            all torrents greater then a given timestamp are only selected
	 * @return the number of IoPs within the given swarm
	 */
	public Long getNumberOfIopsForTorrentHash(String infohash, long timestamp);

	/**
	 * returns all available swarm entries (distinct selected) which are younger
	 * then the given timestamp
	 * 
	 * @param timestamp
	 *            the timestamp indicating the age boundary
	 * @return a list of infohashes
	 */
	public List<String> getTorrents(long timestamp);

	/**
	 * return the statistics for distinct selected torrent hashes
	 * 
	 * @param timestamp
	 *            all torrents greater then a given timestamp are only selected
	 * @return the distinct selected statistics per torrent. It contains the
	 *         following statistics in the following order: 0 --> infohash, 1
	 *         --> sum of down_rate, 2 --> sum of up_rate, 3 --> number of local
	 *         peers, 4 --> number of leechers, 5 --> number of seeders, 6 -->
	 *         number of IoPs
	 */
	public List<List<String>> getStatForAllTorrents(long timestamp);

	/**
	 * return as list of peers within a given swarm, which are currently not
	 * marked being an IoP (iop_flag = false)
	 * 
	 * @param infohash
	 *            the infohash of the swarm
	 * @param timestamp
	 *            only entries with a timestamp greater then the given timestamp
	 *            are selected
	 * @return a list of peers with the following fields per peer: 0-->
	 *         ip_address, 1 -->listenport, 2-->progress, 3-->avg down rate, 4
	 *         --> avg up rate
	 */
	public List<List<String>> getListOfPeers(String infohash, long timestamp);

	/**
	 * return as list of peers within a given swarm, which are currently
	 * participating in given swarm as an IoP (iop_flag = true)
	 * 
	 * @param infohash
	 *            the infohash of the swarm
	 * @param timestamp
	 *            only entries with a timestamp greater then the given timestamp
	 *            are selected
	 * @return a list of iop with the following fields per iop: 0--> ip_address,
	 *         1 -->listenport, 2-->progress, 3-->avg down rate, 4 --> avg up
	 *         rate
	 */
	public List<List<String>> getListOfIops(String infohash, long timestamp);

	/**
	 * 
	 * @param infohash
	 *            the torrenthash
	 * @param timestamp
	 *            only entries with a timestamp greater then the given timestamp
	 *            are selected
	 * @return the number of leechers within a given swarm that are not marked
	 *         as an iop
	 */
	Long getNumberOfLeechersForTorrentHash(String infohash, long timestamp);

	/**
	 * 
	 * @param infohash
	 *            the torrenthash
	 * @param timestamp
	 *            only entries with a timestamp greater then the given timestamp
	 *            are selected
	 * @return the number of seeders within a given swarm that are not marked as
	 *         an iop
	 */
	Long getNumberOfSeedersForTorrentHash(String infohash, long timestamp);

}
