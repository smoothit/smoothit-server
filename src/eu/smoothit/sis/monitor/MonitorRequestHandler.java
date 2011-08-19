package eu.smoothit.sis.monitor;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.Local;
import javax.xml.bind.JAXBException;

/**
 * Local (EJB) interface to the Monitor request handler, which is
 * implemented as an EJB3 session bean.
 * 
 * @author Michael Makidis, Intracom Telecom 
 */
@Local
public interface MonitorRequestHandler {
	
	/**
     * Handles the client's requests;
     * decodes the request and stores it in the DB.
     * 
     * @param is The InputStream with the base-64 encoded XML file
     * @param ip The IP address of the client
     * @throws JAXBException Thrown if there is an XML parsing error
     * @throws IOException Thrown if there is an error reading from the input stream
     */
	void handleRequest(InputStream is, String ip) throws IOException,
			JAXBException;

}
