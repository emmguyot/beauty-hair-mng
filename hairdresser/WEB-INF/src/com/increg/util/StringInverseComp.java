/*
 * Comparateur classant dans un ordre inverse les chaînes
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.util;

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
