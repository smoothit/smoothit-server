package eu.smoothit.sis.controller.iop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.smoothit.sis.controller.iop.ConfigParams;
import eu.smoothit.sis.controller.iop.IoPConfig;
import eu.smoothit.sis.controller.iop.impl.IoPConfigImpl;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.api.daos.IPRangeDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.init.web.SisWebInitializer;


/**
 * Tests the IoP Configuration Service methods
 * 
 * @author Sergios Soursos, Intracom Telecom
 */
public class IoPConfigTestSuite {
	
	private static IoPConfig configurator;
	
	@BeforeClass
	public static void init()
	{
		configurator = new IoPConfigImpl();
	}
	
	@AfterClass
	public static void deleteTestData()
	{
		IComponentConfigDAO dao = SisDAOFactory.getFactory().createComponentConfigDAO();
		dao.removeAll();
		
		IPRangeDAO ipdao = SisDAOFactory.getFactory().createIPRangeDAO();
		ipdao.removeAll();
	}
	
	/*
	 * Checks whether the method react normally if no configuration
	 * parameter is stored for IoP functionality.
	 * 
	 */
	@Test
	public void getIoPConfigParamsNullTestCase() {
		
		assertNull(configurator.getIoPConfigParams("10.0.0.1"));
		
	}
	
	/*
	 * Checks the functionality when PLAIN operation mode is selected.
	 * (same stands for COMBO mode, but it's not tested)
	 * 
	 */
	@Test
	public void getIoPConfigParamsSimple1TestCase() {
		
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		
		ComponentConfigEntry conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_OPERATION_MODE);
		conf.setValue(SisWebInitializer.VALUE_IOP_OPERATION_MODE_PLAIN);
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_LOCAL_IP_RANGES);
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_REMOTE_CONNECTION_FLAG);
		conf.setValue("true");
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_UNCHOKING_SLOTS);
		conf.setValue("10");
		dao.persist(conf);
		
		List<String> ranges = new ArrayList<String>();
		
		IPRangeDAO ipdao = SisDAOFactory.getFactory().createIPRangeDAO();
		IPRangeConfigEntry iprange = new IPRangeConfigEntry();
		iprange.setLocal(true);
		iprange.setPrefix("10.0.0.0");
		ranges.add("10.0.0.0");
		iprange.setPrefix_len(8);
		ipdao.persist(iprange);
		
		iprange = new IPRangeConfigEntry();
		iprange.setLocal(true);
		iprange.setPrefix("20.0.0.0");
		ranges.add("20.0.0.0");
		iprange.setPrefix_len(16);
		ipdao.persist(iprange);
		
		iprange = new IPRangeConfigEntry();
		iprange.setLocal(false);
		iprange.setPrefix("30.0.0.0");
		iprange.setPrefix_len(24);
		ipdao.persist(iprange);
		
		ConfigParams params = configurator.getIoPConfigParams("10.0.1.5");
		assertNotNull(params);
		
		assertEquals(SisWebInitializer.VALUE_IOP_OPERATION_MODE_PLAIN, params.getMode());
		assertEquals(true, params.isRemotes());
		assertEquals(params.getSlots(), 10, 0.0001);
		
		String[] list = params.getLocalIPRanges();
		assertNotNull(list);
		assertEquals(2, list.length);
		for(String r: list){
			assertNotNull(r);
			if ((r.substring(0, r.indexOf("/")-1).equals("10.0.0.0")))
					assertEquals(r.substring(r.indexOf("/"),r.length()-1),"8");
			else if ((r.substring(0, r.indexOf("/")-1).equals("20.0.0.0")))
					assertEquals(r.substring(r.indexOf("/"),r.length()-1),"16");
		}
		
		assertEquals(0, params.getD(), 0.0001);
		assertEquals(0, params.getDlow(), 0.0001);
		assertEquals(0, params.getT(), 0.0001);
		assertEquals(0, params.getU(), 0.001);
		assertEquals(0, params.getUlow(), 0.0001);
		assertEquals(0, params.getX(), 0.0001);
	}
	
	/*
	 * Checks the functionality when COLLAB operation mode is selected.
	 * (uses data from previous test)
	 * 
	 */
	@Test
	public void getIoPConfigParamsSimple2TestCase() {
		
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO dao = factory.createComponentConfigDAO();
		
		ComponentConfigEntry conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_OPERATION_MODE);
		conf.setValue(SisWebInitializer.VALUE_IOP_OPERATION_MODE_COLLAB);
		dao.update(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_T);
		conf.setValue("1800000");
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_ULOW);
		conf.setValue("256");
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_DLOW);
		conf.setValue("2024");
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_U);
		conf.setValue("512");
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_D);
		conf.setValue("4048");
		dao.persist(conf);
		
		conf = new ComponentConfigEntry();
		conf.setComponent(SisWebInitializer.COMPONENT_NAME_CONTR_IOP);
		conf.setPropName(SisWebInitializer.PARAM_IOP_SIS_SWARM_SELECTION_X);
		conf.setValue("90");
		dao.persist(conf);

		
		ConfigParams params = configurator.getIoPConfigParams("10.0.1.5");
		assertNotNull(params);
		
		assertEquals(SisWebInitializer.VALUE_IOP_OPERATION_MODE_COLLAB, params.getMode());
		assertEquals(true, params.isRemotes());
		assertEquals(params.getSlots(), 10, 0.0001);
		
		String[] list = params.getLocalIPRanges();
		assertNotNull(list);
		assertEquals(2, list.length);
		for(String r: list){
			assertNotNull(r);
			if ((r.substring(0, r.indexOf("/")-1).equals("10.0.0.0")))
				assertEquals(r.substring(r.indexOf("/"),r.length()-1),"8");
			else if ((r.substring(0, r.indexOf("/")-1).equals("20.0.0.0")))
				assertEquals(r.substring(r.indexOf("/"),r.length()-1),"16");
		}
		
		assertEquals(4048, params.getD(), 0.0001);
		assertEquals(2024, params.getDlow(), 0.0001);
		assertEquals(1800000, params.getT(), 0.0001);
		assertEquals(512, params.getU(), 0.001);
		assertEquals(256, params.getUlow(), 0.0001);
		assertEquals(90, params.getX(), 0.0001);
	}

}
