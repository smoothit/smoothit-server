package eu.smoothit.sis.controller.peerrating.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import eu.smoothit.sis.controller.peerrating.ClientEndpoint;
import eu.smoothit.sis.controller.peerrating.Request;
import eu.smoothit.sis.controller.peerrating.RequestEntry;
import eu.smoothit.sis.controller.peerrating.Response;
import eu.smoothit.sis.controller.peerrating.impl.ClientEndpointImpl;
import eu.smoothit.sis.controller.peerrating.impl.PeerRatingImpl;

/**
 * Tests the Client Web Service methods
 * 
 * @author Michael Makidis, Intracom Telecom
 */
public class ClientEndpointTestSuite {
	

	/**
     * Tests for a simple Web Method request with peer rating ETM.
     */
	@Test
	public void clientResponseTestCase() {
		ClientEndpoint client = new ClientEndpointImpl(new PeerRatingImpl());
		Request req = new Request();
		ArrayList<RequestEntry> entries = new ArrayList<RequestEntry>();
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("146.58.56.51");
		entries.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.0.1");
		entries.add(entry);
		
		req.setEntries(entries);
		Response resp = client.getRankedPeerList(req);
		assertNotNull(resp);
		assertNotNull(resp.getEntries());
	}
	
	/**
     * Tests for a simple Web Method request without ETM.
     */
	@Deprecated
	@Test
	public void simpleClientResponseTestCase() {
		ClientEndpointImpl client = new ClientEndpointImpl(new PeerRatingImpl());
		Request req = new Request();
		ArrayList<RequestEntry> entries = new ArrayList<RequestEntry>();
		RequestEntry entry = new RequestEntry();
		entry.setIpAddress("146.58.56.51");
		entries.add(entry);
		
		entry = new RequestEntry();
		entry.setIpAddress("192.10.0.2");
		entries.add(entry);
		
		req.setEntries(entries);
		Response resp = client.getSimpleRankedPeerList(req);
		assertNotNull(resp);
		assertNotNull(resp.getEntries());
	}
	
	/**
     * Tests for a Web Method request with no entries.
     */
	@Test
	public void clientResponseNullEntriesTestCase() {
		ClientEndpointImpl client = new ClientEndpointImpl(new PeerRatingImpl());
		Request req = new Request();
		
		Response resp = client.getRankedPeerList(req);
		assertNotNull(resp);
		
		resp = client.getSimpleRankedPeerList(req);
		assertNotNull(resp);
	}

}
