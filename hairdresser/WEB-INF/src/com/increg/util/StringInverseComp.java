package com.increg.util;

/**
 * Comparateur classant dans un ordre inverse les chaînes
 * Creation date: (10/11/2001 09:42:14)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class StringInverseComp implements java.util.Comparator {
/**
 * StringInverseComp constructor comment.
 */
public StringInverseComp() {
	super();
}
/**
 * @see java.util.Comparator
 */
public int compare(Object o1, Object o2) {
	return - ((Comparable) o1).compareTo(o2);
}
}
