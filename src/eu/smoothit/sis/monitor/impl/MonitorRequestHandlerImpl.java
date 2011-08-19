package eu.smoothit.sis.monitor.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IPeerStatusDAO;
import eu.smoothit.sis.db.impl.entities.ConnectedPeerStatusEntry;
import eu.smoothit.sis.db.impl.entities.PeerStatusEntry;
import eu.smoothit.sis.monitor.MonitorRequestHandler;
import eu.smoothit.sis.monitor.impl.xml.Item;
import eu.smoothit.sis.monitor.impl.xml.Key;
import eu.smoothit.sis.monitor.impl.xml.Map;

/**
 * The Monitor request handler implemented as an EJB3 session bean;
 * decodes client requests and stores the info in the DB.
 * 
 * @author Michael Makidis, Intracom Telecom 
 */
@Stateless
public class MonitorRequestHandlerImpl implements  MonitorRequestHandler{
	
	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(MonitorRequestHandlerImpl.class.getName());

	/**
     * Handles the client's requests;
     * decodes the request and stores it in the DB.
     * 
     * @param is The InputStream with the base-64 encoded XML file
     * @throws JAXBException Thrown if there is an XML parsing error
     * @throws IOException Thrown if there is an error reading from the input stream
     */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void handleRequest(InputStream is, String ip) throws IOException, JAXBException {

		InputStream bis = new Base64.InputStream(is);
		//GZIPInputStream gis = new GZIPInputStream(bis);
		
		/* ByteArrayOutputStream baous = new ByteArrayOutputStream();
		int c;
		while ((c=bis.read()) != -1)
			baous.write(c);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baous.toByteArray());
		
		java.util.Date now = new java.util.Date();
		
		File newFile = new File("report" + ((Long) now.getTime()).toString()  + ".xml");
		FileOutputStream fos = new FileOutputStream(newFile);
		int data;
		while ((data = bais.read()) != -1){
			char ch = (char) data;
			fos.write(ch);
		}
		fos.flush();
		fos.close();
		
		ByteArrayInputStream bais2 = new ByteArrayInputStream(baous.toByteArray());
		*/
				
		JAXBContext context = JAXBContext.newInstance(Map.class);
		Unmarshaller u = context.createUnmarshaller();
		Map input = (Map)u.unmarshal(bis);
		//Map input = (Map)u.unmarshal(bais2);
		
		is.close();
		
		log.debug("Client (IP: " + ip + ") status update unmarshalled");
		
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IPeerStatusDAO dao = factory.createPeerStatusDAO();
		PeerStatusEntry status = new PeerStatusEntry();
		
		// Entering IP address acquired from client call.
		status.setIp_address(ip);
		
		for(Key key : input.getKey())
			storeKey(status, key);
		dao.persist(status);
		log.info("Client (IP: " + ip + ", ID: " + status.getPeer_id() + ") status update stored");
	}

	/**
     * Converts from a Key XML element to a PeerStatusEntry object for
     * storage in the DB.
     * 
     * @param status The PeerStatusEntry object
     * @param key The Key XML element
     */
	protected void storeKey(PeerStatusEntry status, Key key) {
		if(key.getContent().isEmpty())
			return;
		String value = "";
		if(key.getContent().get(0) instanceof String)
			value = (String)key.getContent().get(0);
		if(key.getName().equalsIgnoreCase("down_total"))
		{
			status.setDown_total(Float.parseFloat(value));
		}
		else if(key.getName().equalsIgnoreCase("down_rate"))
		{
			status.setDown_rate(Float.parseFloat(value));
		}
		else if(key.getName().equalsIgnoreCase("iop_flag"))
		{
			if (value.equalsIgnoreCase("0"))
				status.setIop_flag(false);
			else 
				status.setIop_flag(true);
		}
		else if(key.getName().equalsIgnoreCase("filename"))
		{
			status.setFilename(value);
		}
		else if(key.getName().equalsIgnoreCase("infohash"))
		{
			String infohash = null;
			//try {
				//log.info("received infohash = " + value);
				//String tmp = StringEscapeUtils.unescapeXml(value.replaceAll("[']",""));
				//infohash = new String(Base64.decode(value));
			infohash = value;
			//} catch (IOException e) {
			//	e.printStackTrace();
			//}
			status.setInfohash(infohash);
		}
		else if(key.getName().equalsIgnoreCase("listenport"))
		{
			status.setListenport(Integer.parseInt(value));
		}
		else if(key.getName().equalsIgnoreCase("live"))
		{
			status.setLive(value.equalsIgnoreCase("true"));
		}
		else if(key.getName().equalsIgnoreCase("p_dropped"))
		{
			status.setP_dropped(Integer.parseInt(value));
		}
		else if(key.getName().equalsIgnoreCase("p_late"))
		{
			status.setP_late(Integer.parseInt(value));
		}
		else if(key.getName().equalsIgnoreCase("p_played"))
		{
			status.setP_played(Integer.parseInt(value));
		}
		else if(key.getName().equalsIgnoreCase("peerid"))
		{
			status.setPeer_id(value);
		}
		else if(key.getName().equalsIgnoreCase("progress"))
		{
			status.setProgress(Float.parseFloat(value));
		}
		else if(key.getName().equalsIgnoreCase("status"))
		{
			status.setStatus(value);
		}
		else if(key.getName().equalsIgnoreCase("t_prebuf"))
		{
			status.setT_prebuf(Integer.parseInt(value));
		}
		else if(key.getName().equalsIgnoreCase("t_stall"))
		{
			status.setT_stalled(Integer.parseInt(value));
		}
		else if(key.getName().equalsIgnoreCase("timestamp"))
		{
			// by-passing type inconsistency in between DB and client
			//status.setTimestamp(((Float)Float.parseFloat(value)).longValue());
			//status.setTimestamp(Long.parseLong(value));
			
			// resolve any possible de-synchronization between client and server by
			// considering only server timestamps.
			java.util.Date now = new java.util.Date();
			status.setTimestamp(now.getTime());
			
		}
		else if(key.getName().equalsIgnoreCase("up_rate"))
		{
			status.setUp_rate(Float.parseFloat(value));
		}
		else if(key.getName().equalsIgnoreCase("up_total"))
		{
			status.setUp_total(Float.parseFloat(value));
		}
		else if(key.getName().equalsIgnoreCase("validrange"))
		{
			status.setValidRange(value);
		}
		else if(key.getName().equalsIgnoreCase("peers"))
		{
			parseConnectedPeers(status, key);
		}
		else if(key.getName().equalsIgnoreCase("blockstats"))
		{
			parseBlockStats(key);
		}
	}
	
	private void parseBlockStats(Key key){
		
		Object oo = key.getContent();
		if(!(oo instanceof ArrayList<?>)) {
			if (oo == null)
				log.debug("Reason: Null value!");
			else
				log.debug("Reason: Other type of object: " + oo.getClass().getName());
			
			return;
		}
		
		for (Object ooo : (ArrayList<?>)oo){
			if(!(ooo instanceof eu.smoothit.sis.monitor.impl.xml.List)) {
				if (ooo == null)
					log.debug("Reason: Null value!");
				else
					log.debug("Reason: Other type of object: " + ooo.getClass().getName());
				
				return;
			}
			
			eu.smoothit.sis.monitor.impl.xml.List p = (eu.smoothit.sis.monitor.impl.xml.List) ooo;
			
			for(Item o : p.getItem()){
				if(!(o instanceof Item)) {
					if (o == null)
						log.debug("Reason: Null value!");
					else
						log.debug("Reason: Other type of object: " + o.getClass().getName());
					
					continue;
				}
				
				eu.smoothit.sis.monitor.impl.xml.Tuple t = o.getTuple();
				
				if(!(t instanceof eu.smoothit.sis.monitor.impl.xml.Tuple)) {
					if (t == null)
						log.debug("Reason: Null value!");
					else
						log.debug("Reason: Other type of object: " + o.getClass().getName());
					
					continue;
				}
				
				parseTuple(t);
				
			}
			
		}		
		
	}
	
	protected void parseTuple(eu.smoothit.sis.monitor.impl.xml.Tuple t){
		List<eu.smoothit.sis.monitor.impl.xml.Value> lst = t.getValue();
		for (eu.smoothit.sis.monitor.impl.xml.Value v : lst){
			
			String value = "";
			if(v.getValue() instanceof String)
				value = (String)v.getValue();
			
			if (v.getAt().intValue() == 0){
				// piece_id
				//log.info("XML value @ position 0 = " + value);
			} else if (v.getAt().intValue() == 1){
				// block_offset_in_piece
				//log.info("XML value @ position 1 = " + value);
			} else if (v.getAt().intValue() == 2){
				// block_length
				//log.info("XML value @ position 2 = " + value);
			} else if (v.getAt().intValue() == 3){
				// timestamp (seconds)
				//log.info("XML value @ position 3 = " + value);
			} else if (v.getAt().intValue() == 4){
				// sender_ip
				//log.info("XML value @ position 4 = " + value);
			} else if (v.getAt().intValue() == 5){
				// sender_port
				//log.info("XML value @ position 5 = " + value);
			} else if (v.getAt().intValue() == 6){
				// sender_id
				//log.info("XML value @ position 6 = " + value);
			}
			
		}
	}

	/**
     * Enumerates the connected peers part of a Key XML object
     * that is part of the current peer monitor information. The data
     * are stored in a PeerStatusEntry (in ConnectedPeerStatus entities
     * that have a foreign key to the PeerStatus).
     * 
     * @param status The PeerStatusEntry object
     * @param key The Key XML element
     */
	protected void parseConnectedPeers(PeerStatusEntry status, Key key) {
		List<ConnectedPeerStatusEntry> list = new ArrayList<ConnectedPeerStatusEntry>();
		
		Object oo = key.getContent();
		if(!(oo instanceof ArrayList<?>)) {
			if (oo == null)
				log.debug("Reason: Null value!");
			else
				log.debug("Reason: Other type of object: " + oo.getClass().getName());
			
			return;
		}
		
		Object ooo = ((ArrayList<?>)oo).get(0);
		if(!(ooo instanceof eu.smoothit.sis.monitor.impl.xml.List)) {
			if (ooo == null)
				log.debug("Reason: Null value!");
			else
				log.debug("Reason: Other type of object: " + ooo.getClass().getName());
			
			return;
		}
		
		eu.smoothit.sis.monitor.impl.xml.List p = (eu.smoothit.sis.monitor.impl.xml.List) ooo;
		
		for(Item o : p.getItem())
		{
			if(!(o instanceof Item)) {
				if (o == null)
					log.debug("Reason: Null value!");
				else
					log.debug("Reason: Other type of object: " + o.getClass().getName());
				
				continue;
			}
			Item item = (Item) o;
			ConnectedPeerStatusEntry cStatus = new ConnectedPeerStatusEntry();
			
			cStatus.setStatus(status);
			storeItem(cStatus, item);
			list.add(cStatus);
			//dao.addConnectedPeerStatusEntry(cStatus);
		}
		status.setList(list);
	}

	/**
     * Converts between an Item XML element and a ConnectedPeerStatus
     * entity bean for storage in the DB
     * 
     * @param cStatus The ConnectedPeerStatus entity bean
     * @param item The Item XML element
     */
	protected void storeItem(ConnectedPeerStatusEntry cStatus, Item item) {
		Map map = item.getMap();
		for(Key key: map.getKey())
		{
			String value = "";
			if(key.getContent().get(0) instanceof String)
				value = (String)key.getContent().get(0);
			
			if(key.getName().equalsIgnoreCase("addr"))
			{
				cStatus.setAddr(value);
			}
			else if(key.getName().equalsIgnoreCase("down_rate"))
			{
				cStatus.setDown_rate(Float.parseFloat(value));
			}
			else if(key.getName().equalsIgnoreCase("down_str"))
			{
				cStatus.setDown_str(value);
			}
			else if(key.getName().equalsIgnoreCase("down_total"))
			{
				cStatus.setDown_total(Float.parseFloat(value));
			}
			else if(key.getName().equalsIgnoreCase("g2g"))
			{
				cStatus.setG2g(value);
			}
			else if(key.getName().equalsIgnoreCase("g2g_score"))
			{
				cStatus.setG2g_score(value);
			}
			else if(key.getName().equalsIgnoreCase("id"))
			{
				cStatus.setPeer_id(value);
			}
			else if(key.getName().equalsIgnoreCase("up_rate"))
			{
				cStatus.setUp_rate(Float.parseFloat(value));
			}
			else if(key.getName().equalsIgnoreCase("up_str"))
			{
				cStatus.setUp_str(value);
			}
			else if(key.getName().equalsIgnoreCase("up_total"))
			{
				cStatus.setUp_total(Float.parseFloat(value));
			}
		}
	}

}
