package eu.smoothit.sis.controller.iop.impl;

import java.util.List;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import eu.smoothit.sis.controller.iop.ConfigParams;
import eu.smoothit.sis.controller.iop.IoPConfig;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;


/**
 * The implementation of the IoP configuration web service.
 * 
 * @author Sergios Soursos, Intracom Telecom
 * @version 1.0
 * 
 */
@Stateless
public class IoPConfigImpl implements IoPConfig {

	/**
	 * The logger.
	 */
	private static org.apache.log4j.Logger log = Logger
			.getLogger(IoPConfigImpl.class.getName());
	
	/**
	 * Collects the respective IoP configuration info from the DB 
	 * and passes it to the Client.  
	 * 
	 * @param ipAddress The IP address of the requesting IoP (currently not used)
	 * @return The configuration parameters of the IoP. 
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ConfigParams getIoPConfigParams(String ipAddress){

		ConfigParams res = new ConfigParams();
	
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		
		List<ComponentConfigEntry> configList = dao.findByComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		
		boolean flag = false;
		
		if (configList != null && configList.size() > 0){
			for (ComponentConfigEntry entry: configList){
				log.debug("Processing param with name: " + entry.getPropName());
				if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_OPERATION_MODE)){
					res.setMode((String) entry.getValue());
					if (((String) entry.getValue()).equalsIgnoreCase(SisWebInitializer.VALUE_IOP_OPERATION_MODE_COLLAB))
						flag=true;
				}
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_LOCAL_IP_RANGES)){
					//List<String> tmplist = new ArrayList<String>();
					String[] tmplist;
					
					IPRangeDAO ipdao = SisDAOFactory.getFactory().createIPRangeDAO();
					IPRangeConfigEntry iprange = new IPRangeConfigEntry();
					iprange.setLocal(true);
					List<IPRangeConfigEntry> localIPs = ipdao.get(iprange);
					if (localIPs != null && localIPs.size() > 0){ 
						tmplist = new String[localIPs.size()];
					
						for(int j=0;j<localIPs.size();j++) {
							IPRangeConfigEntry i = localIPs.get(j);
							String tmp = "";
							tmp = tmp.concat(i.getPrefix());
							tmp = tmp.concat("/");
							tmp = tmp.concat(i.getPrefix_len().toString());
							tmplist[j]= tmp;
							//tmplist.add(tmp);
						}
					} else
						tmplist = new String[0];
						
					
					res.setLocalIPRanges(tmplist);
				}
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_REMOTE_CONNECTION_FLAG))
					res.setRemotes(Boolean.valueOf((String) entry.getValue()).booleanValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_UNCHOKING_SLOTS))
					res.setSlots(Integer.valueOf((String) entry.getValue()).intValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_T))
						//&& (flag == true))
					res.setT(Integer.valueOf((String) entry.getValue()).intValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_ULOW))
						//&& (flag == true))
					res.setUlow(Float.valueOf((String)entry.getValue()).floatValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_DLOW))
						//&& (flag == true))
					res.setDlow(Float.valueOf((String)entry.getValue()).floatValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_U))
						//&& (flag == true))
					res.setU(Float.valueOf((String)entry.getValue()).floatValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_D))
						//&& (flag == true))
					res.setD(Float.valueOf((String)entry.getValue()).floatValue());
				else if (entry.getPropName().equalsIgnoreCase(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_X))
						//&& (flag == true))
					res.setX(Float.valueOf((String)entry.getValue()).floatValue());
			}
			
			// Temporarily fixed a bug of Admin (not storing a config param for IP ranges)
			//List<String> tmplist = new ArrayList<String>();
			String[] tmplist;
			
			IPRangeDAO ipdao = SisDAOFactory.getFactory().createIPRangeDAO();
			IPRangeConfigEntry iprange = new IPRangeConfigEntry();
			iprange.setLocal(true);
			List<IPRangeConfigEntry> localIPs = ipdao.get(iprange);
			if (localIPs != null && localIPs.size() > 0){ 
				tmplist = new String[localIPs.size()];
			
				for(int j=0;j<localIPs.size();j++) {
					IPRangeConfigEntry i = localIPs.get(j);
					String tmp = i.getPrefix();
					tmp = tmp.concat("/");
					tmp = tmp.concat(i.getPrefix_len().toString());
					tmplist[j]= tmp;
					//tmplist.add(tmp);
				}
			} else
				tmplist = new String[0];
			
			res.setLocalIPRanges(tmplist);

			return res;
		} else {
			log.warn("No IoP configuration found!");
			return null;
		}
		
	}
	

}
