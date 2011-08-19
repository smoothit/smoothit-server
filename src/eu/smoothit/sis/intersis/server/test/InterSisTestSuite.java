package eu.smoothit.sis.intersis.server.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.smoothit.sis.controller.peerrating.Request;
import eu.smoothit.sis.intersis.server.InterSisEndpoint;
import eu.smoothit.sis.intersis.server.impl.InterSisServiceImpl;

public class InterSisTestSuite {
	
	@Test
	public void authTestCase()
	{
		InterSisEndpoint sis = new InterSisServiceImpl();
		assertFalse(sis.authorizeServer("bla"));
	}
	
	@Test
	public void peerListTestCase()
	{
		InterSisEndpoint sis = new InterSisServiceImpl();
		assertTrue(sis.getRankedPeerList(new Request()) != null);
	}
}
