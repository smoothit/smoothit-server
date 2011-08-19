package eu.smoothit.sis.db.impl.utils;

import java.util.List;

import eu.smoothit.sis.db.impl.entities.IPRangeConfigEntry;

/**
 * Help Methods for all DAOs
 * 
 * @author Jonas Flick, KOM, TU Darmstadt
 * 
 */
public class DAOUtils {

	public static boolean withinRange(String IP, String prefix, int prefix_len) {
		// Check if IP starts with prefix and ends within range of length
		// 1. extract IP_prefix and IP_suffix
		int indexOfLastDot = findIndexOfLastDot(IP);
		String IP_prefix = IP.substring(0, indexOfLastDot); // Exclusive the
		// last dot
		String IP_suffix = IP.substring(indexOfLastDot + 1, IP.length()); // Also
		// Exclusive the dot!

		// 2. check for right prefix
		if (!prefix.equalsIgnoreCase(IP_prefix)) {
			return false; // Failed the test
		}

		// 3. check for valid range
		int r = new Integer(IP_suffix);
		if (r >= 0 && r < prefix_len)
			return true; // Passed both tests
		else
			return false; // Final test failed, therefore it's not in range
	}

	private static int findIndexOfLastDot(String str) {
		int indexOfFirstDot = str.indexOf(".");

		int nextIndex;
		if (indexOfFirstDot != -1
				&& (nextIndex = findIndexOfLastDot(str
						.substring(indexOfFirstDot + 1))) != -1)
			return indexOfFirstDot + nextIndex + 1;

		return indexOfFirstDot;
	}

	public static boolean equalRange(IPRangeConfigEntry range, String prefix, int len) {
		return (prefix.equals(range.getPrefix()) && len == range
				.getPrefix_len().intValue());
	}

	public static boolean equalRange(IPRangeConfigEntry range1, IPRangeConfigEntry range2) {
		return (range2.getPrefix().equals(range1.getPrefix()) && range2
				.getPrefix_len().intValue() == range1.getPrefix_len()
				.intValue());
	}

	public static boolean contains(List<IPRangeConfigEntry> ranges, IPRangeConfigEntry aRange) {
		for (IPRangeConfigEntry r : ranges) {
			if (equalRange(aRange, r))
				return true;
		}
		return false;
	}

}
