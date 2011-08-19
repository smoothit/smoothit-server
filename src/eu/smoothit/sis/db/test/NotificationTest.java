/**
 * 
 */
package eu.smoothit.sis.db.test;

import java.util.List;
import java.util.Observable;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.smoothit.sis.db.SISDBObserver;
import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IComponentConfigDAO;
import eu.smoothit.sis.db.impl.entities.ComponentConfigEntry;
import eu.smoothit.sis.db.impl.utils.DBNotifyMessage;
import eu.smoothit.sis.db.impl.utils.SISDBNotifier;

/**
 * @author Christian Gross
 * 
 */
public class NotificationTest extends TestCase implements
		SISDBObserver {

	protected Logger logger = Logger.getLogger(NotificationTest.class);

	private DBNotifyMessage result;


	public NotificationTest() {
	}


	public void testIndividualFunctionality() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		SISDBNotifier notifier = factory.createSISDBNotifier();
		notifier.addObserver(this);
		assertTrue(notifier.countObservers() > 0);

		IComponentConfigDAO cdao = factory.createComponentConfigDAO();
		cdao.removeAll();
		ComponentConfigEntry config = new ComponentConfigEntry();
		config.setComponent("test");
		config.setPropName("test2");
		config.setValue("12345");
		cdao.persist(config);
		assertTrue(cdao.countAll() == 1);
		assertTrue(this.result != null);
		assertTrue(result.getAction().equals(DBNotifyMessage.ACTION.PERSIST));

		ComponentConfigEntry search = new ComponentConfigEntry();
		search.setComponent("test");
		search.setPropName("test2");
		List<ComponentConfigEntry> list = cdao.get(search);
		ComponentConfigEntry entry = list.get(0);
		entry.setValue("999");
		cdao.update(entry);
		assertTrue(result.getAction().equals(DBNotifyMessage.ACTION.UPDATE));

		cdao.remove(search);
		assertTrue(result.getAction().equals(DBNotifyMessage.ACTION.REMOVE));

		// clean the database
		cdao.removeAll();

		// fill the database
		createSomeEntries();
		search = new ComponentConfigEntry();
		search.setComponent("2");

		list = cdao.get(search);
		for (ComponentConfigEntry propsConfigEntry : list) {
			cdao.remove(propsConfigEntry);
		}
		assertTrue(cdao.countAll() == 6);
		assertTrue(result.getAction().equals(DBNotifyMessage.ACTION.REMOVE));

		notifier.deleteObserver(this);
		assertTrue(notifier.countObservers() == 0);

	}

	// ComponentConfigDAO cdao;
	//
	// public NotificationTest() {
	//
	// }
	//
	// @BeforeClass
	// public static void setupLogger() {
	// PropertyConfigurator.configure("log4j.properties");
	// }
	//
	// @Test
	// public void testNotification() {
	//

	// }

	@Override
	public void update(Observable arg0, Object arg1) {
		logger.debug(arg1);
		this.result = (DBNotifyMessage) arg1;
	}

	//
	private void createSomeEntries() {
		SisDAOFactory factory = SisDAOFactory.getFactory();
		IComponentConfigDAO cdao = factory.createComponentConfigDAO();

		ComponentConfigEntry a1 = new ComponentConfigEntry();
		a1.setComponent("1");
		a1.setPropName("1");
		a1.setValue("11");
		cdao.persist(a1);
		
		ComponentConfigEntry a2 = new ComponentConfigEntry();
		a2.setComponent("1");
		a2.setPropName("2");
		a2.setValue("12");
		cdao.persist(a2);
		
		ComponentConfigEntry a3 = new ComponentConfigEntry();
		a3.setComponent("1");
		a3.setPropName("3");
		a3.setValue("13");
		cdao.persist(a3);
		
		ComponentConfigEntry a4 = new ComponentConfigEntry();
		a4.setComponent("2");
		a4.setPropName("1");
		a4.setValue("21");
		cdao.persist(a4);
		
		ComponentConfigEntry a5 = new ComponentConfigEntry();
		a5.setComponent("2");
		a5.setPropName("2");
		a5.setValue("22");
		cdao.persist(a5);
		
		ComponentConfigEntry a6 = new ComponentConfigEntry();
		a6.setComponent("2");
		a6.setPropName("3");
		a6.setValue("23");
		cdao.persist(a6);
		
		ComponentConfigEntry a7 = new ComponentConfigEntry();
		a7.setComponent("3");
		a7.setPropName("1");
		a7.setValue("31");
		cdao.persist(a7);
		
		ComponentConfigEntry a8 = new ComponentConfigEntry();
		a8.setComponent("3");
		a8.setPropName("2");
		a8.setValue("32");
		cdao.persist(a8);
		
		ComponentConfigEntry a9 = new ComponentConfigEntry();
		a9.setComponent("3");
		a9.setPropName("3");
		a9.setValue("33");
		cdao.persist(a9);
	}

}
