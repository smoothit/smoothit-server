package eu.smoothit.sis.admin.util;

import java.io.Serializable;
import java.util.Comparator;

public class ColumnValueSorter implements Comparator<Object>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sortColumnName;
	private boolean ascending;

	public ColumnValueSorter(String sortColumnName, boolean ascending) {
		sortColumnName = "get" + sortColumnName.substring(0, 1).toUpperCase()
				+ sortColumnName.substring(1);
		this.sortColumnName = sortColumnName;
//		System.err.println("sortColumnName,ascending:"+sortColumnName+ascending);
		this.ascending = ascending;
	}

	@SuppressWarnings("unchecked")
	public int compare(Object o1, Object o2) {
		if (sortColumnName == null) {
			return 0;
		}
		try {
			o1 = o1.getClass().getMethod(sortColumnName, new Class[0]).invoke(
					o1, new Object[0]);
			o2 = o2.getClass().getMethod(sortColumnName, new Class[0]).invoke(
					o2, new Object[0]);

		} catch (Exception e) {
			// If this exception occurs, then it is usually a fault of the DTO
			// developer.
			throw new RuntimeException("Cannot compare " + o1 + " with " + o2
					+ " on " + sortColumnName, e);
		}
		if (ascending) {
			return (o1 == null) ? -1 : ((o2 == null) ? 1
					: ((Comparable<Object>) o1).compareTo(o2));
		} else {
			return (o1 == null) ? 1 : ((o2 == null) ? -1
					: ((Comparable<Object>) o2).compareTo(o1));
		}
	}
}