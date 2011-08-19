package eu.smoothit.sis.db.test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IClientRequestDAO;
import eu.smoothit.sis.db.api.daos.ISwarmDAO;
import eu.smoothit.sis.db.impl.entities.ClientRequestEntry;
import eu.smoothit.sis.db.impl.entities.SwarmEntityEntry;


public class ClientSwarmRequestTest extends TestCase{

	protected Logger logger = Logger.getLogger(ClientSwarmRequestTest.class);


	public ClientSwarmRequestTest() {

	}
	
	public void testBasicFunctionality() {
		/*
		 * test for inserting two entries into the database
		 */
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IClientRequestDAO crdao = factory.createClientRequestDOA();
		ISwarmDAO sdao = factory.createSwarmDAO();
		
		crdao.removeAll();

		ClientRequestEntry clientEntry = new ClientRequestEntry();
		SwarmEntityEntry swarmEntry = new SwarmEntityEntry();

		SwarmEntityEntry swarmEntry2 = new SwarmEntityEntry();

		swarmEntry.setDownloadProgress(2.0);
		swarmEntry.setPlaybackProgress(50.0);
		swarmEntry.setTorrentHash("HASH");
		swarmEntry.setTorrentURL("www.tolleURL.com");
		swarmEntry.setAvailablity(true);
		swarmEntry.setNumberOfLeechers(100L);
		swarmEntry.setNumberOfLocalLeechers(50L);
		swarmEntry.setNumberOfSeeds(200L);
		swarmEntry.setNumberOfLocalSeeds(300L);

		swarmEntry2.setDownloadProgress(2.0);
		swarmEntry2.setPlaybackProgress(57.0);
		swarmEntry2.setTorrentHash("HASH2");
		swarmEntry2.setTorrentURL("www.tolleURL2.com");

		LinkedList<ClientRequestEntry> request = new LinkedList<ClientRequestEntry>();
		request.add(clientEntry);
		swarmEntry.setClientRequests(request);
		swarmEntry2.setClientRequests(request);

		clientEntry.setIP("127.0.0.1");
		clientEntry.setPort(5000);
		clientEntry.setLastUpdate(123465789L);
		LinkedList<SwarmEntityEntry> swarms = new LinkedList<SwarmEntityEntry>();
		swarms.add(swarmEntry);
		swarms.add(swarmEntry2);
		clientEntry.setSwarms(swarms);

		
		crdao.persist(clientEntry);
//		sdao.persist(swarmEntry);

		assertTrue(crdao.countAll() == 1);
		assertTrue(sdao.countAll() == 2);

		//fetch client request entry
		clientEntry = new ClientRequestEntry();
		clientEntry.setIP("127.0.0.1");
		clientEntry.setPort(5000);
		clientEntry.setLastUpdate(123465789L);
		List<ClientRequestEntry> list = crdao.get(clientEntry);
		assertTrue(list.size()==1);
		assertTrue(list.get(0).getPort()== 5000);
		assertTrue(list.get(0).getIP().equals("127.0.0.1"));
		assertTrue(list.get(0).getLastUpdate()==123465789L);
		
		//check object reference
		List<SwarmEntityEntry> swarmList = list.get(0).getSwarms();
		assertTrue(swarmList.size() == 2);
		
		swarmEntry = new SwarmEntityEntry();
		swarmEntry.setDownloadProgress(2.0);
		swarmEntry.setPlaybackProgress(50.0);
		swarmEntry.setTorrentHash("HASH");
		swarmEntry.setTorrentURL("www.tolleURL.com");
		
		swarmList = sdao.get(swarmEntry);
		assertTrue(swarmEntry != null);
		assertTrue(swarmList.size() == 1);
		
		
		// remove again
		sdao.remove(swarmEntry2);
		assertTrue(sdao.countAll() == 1);
		
	}
}
