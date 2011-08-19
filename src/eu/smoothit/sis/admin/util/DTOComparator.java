package eu.smoothit.sis.admin.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Sorts a collection of data transfer objects (DTO's) based on the name of the getter of the field
 * to sort on. For example: the DTO MyDto has three fields ID, Name and Value, all with appropriate
 * getters and setters. If you want to sort on the field Name, which has a getter called "getName",
 * then invoke the sorting
 */
public class DTOComparator implements Comparator<Object>,Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> getters;
    private boolean ascending;

//constructors
//-------------------------------------------------------------------
    /**
     * @param getter The name of the getter of the field to sort on.
     * @param ascending The sort order: true = ascending, false = descending.
     */
    public DTOComparator(String getter, boolean ascending) {
        this.getters = new ArrayList<String>();
        for (String name : getter.split("\\.")) {
            if (!name.startsWith("get")) {
                name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            this.getters.add(name);
        }
        this.ascending = ascending;
    }

    /**
     * @param getter The name of the getter of the field to sort on.
     */
    public DTOComparator(String getter) {
        this(getter, true);
    }

    // Actions 
    //------------------------------------------------------------------------------------
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        try {
            Iterator<String> iter = getters.iterator();
            while (o1 != null && o2 != null && iter.hasNext()) {
                String getter = iter.next();
                o1 = o1.getClass().getMethod(getter, new Class[0]).invoke(o1, new Object[0]);
                o2 = o2.getClass().getMethod(getter, new Class[0]).invoke(o2, new Object[0]);
            }
        } catch (Exception e) {
            // If this exception occurs, then it is usually a fault of the DTO developer.
            throw new RuntimeException("Cannot compare " + o1 + " with " + o2 + " on " + getters, e);
        }
        if (ascending) {
            return (o1 == null) ? -1 : ((o2 == null) ? 1 : ((Comparable<Object>) o1).compareTo(o2));
        } else {
            return (o1 == null) ? 1 : ((o2 == null) ? -1 : ((Comparable<Object>) o2).compareTo(o1));
        }
    }

}