package eu.smoothit.sis.monitor.test;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import com.bm.testsuite.BaseSessionBeanFixture;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.monitor.MonitorRequestHandler;
import eu.smoothit.sis.monitor.impl.Base64;

/**
 * Tests the Monitor component.
 * 
 * @author Michael Makidis, Intracom Telecom 
 */
public class MonitorTestSuite 
extends BaseSessionBeanFixture<MonitorRequestHandler> {
	
	/**
     * Beans used for this test
     */
	private static final Class[] usedBeans = { };

	/**
     * Initialization of the EJB3Unit container
     */
	public MonitorTestSuite() {
		super(MonitorRequestHandler.class, usedBeans);
	}

	/**
     * Test file name.
     */
	public static final String filename="/eu/smoothit/sis/monitor/test/reporting-example.xml";
	
	/**
     * Tests the Monitor Request Handler
     */
	public void testMonitor() throws IOException, JAXBException
	{
		InputStream instream = getClass().getResourceAsStream(filename);
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int c;
		while ((c = instream.read()) != -1) {
			baos.write((char) c);
		}
		instream.close();
		
		ByteArrayOutputStream gzipOutput = new ByteArrayOutputStream();
		GZIPOutputStream gout = new GZIPOutputStream(gzipOutput);
		gout.write(baos.toByteArray());
		gout.finish();
		gout.close();
		
		InputStream bytein = new ByteArrayInputStream(gzipOutput.toByteArray());*/
		
		MonitorRequestHandler handler = this.getBeanToTest();
		handler.handleRequest(new Base64.InputStream(instream, Base64.ENCODE), "TEST");
		
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IPeerStatusDAO dao = factory.createPeerStatusDAO();
		dao.removeAll();
	}

}
