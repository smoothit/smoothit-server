package eu.smoothit.sis.db.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;
import eu.smoothit.sis.db.impl.utils.DAOUtils;


/**
 * Test class for utility methods
 * 
 * @author Jonas
 * 
 */
public class UtilitiesTestCases  extends TestCase {

	
	
	protected Logger logger = Logger
	.getLogger(UtilitiesTestCases.class);
	
	
	public UtilitiesTestCases() {
	}

	/**
	 * Tests if the withinRange Method functions correctly
	 */
	@Test
	public void testIPRangeCheck() {

		String prefix = "192.168.0";
		int prefix_len = 226;
		String IP1 = "192.168.0.1";// In
		String IP2 = "192.168.0.225";// Out
		boolean check1 = DAOUtils.withinRange(IP1, prefix, prefix_len);
		boolean check2 = DAOUtils.withinRange(IP2, prefix, prefix_len);
		assertTrue(check1);
		assertTrue(check2);
	}

	@Test
	public void testContains() {
		IPRangeConfigEntry aRange = new IPRangeConfigEntry();
		aRange.setPrefix("1.2.3.4");
		aRange.setPrefix_len(5);
		List<IPRangeConfigEntry> ranges = new ArrayList<IPRangeConfigEntry>();
		ranges.add(aRange);
		assertTrue(DAOUtils.contains(ranges, aRange));
		ranges = new ArrayList<IPRangeConfigEntry>();
		assertTrue(!DAOUtils.contains(ranges, aRange));
	}

	@Test
	public void testEqualRange() {
		IPRangeConfigEntry aRange =  new IPRangeConfigEntry();
		aRange.setPrefix("1.2.3.4");
		aRange.setPrefix_len(5);
		IPRangeConfigEntry bRange =  new IPRangeConfigEntry();
		bRange.setPrefix("1.2.3.4");
		bRange.setPrefix_len(5);

		assertTrue(DAOUtils.equalRange(aRange, bRange));
		assertTrue(DAOUtils.equalRange(aRange, bRange.getPrefix(), bRange
				.getPrefix_len()));
	}

}
