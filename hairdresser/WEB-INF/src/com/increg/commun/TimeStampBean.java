package com.increg.commun;

import com.increg.commun.*;
import java.sql.*;
import java.util.*;
/**
 * Bean gérant les dates de créations / Modification
 * Creation date: (08/07/2001 17:17:53)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class TimeStampBean extends GenericBean {

	protected Calendar	DT_CREAT;
	protected Calendar	DT_MODIF;
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:56:22)
 */
public TimeStampBean() {
	super();
	DT_CREAT = Calendar.getInstance();
	DT_MODIF = Calendar.getInstance();	
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:56:22)
 */
public TimeStampBean(ResultSet rs) {

	super(rs);
	DT_CREAT = Calendar.getInstance();
	DT_MODIF = Calendar.getInstance();	
	try {
		DT_CREAT.setTime(rs.getTimestamp("DT_CREAT"));
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TimeStampBean (RS) : " + e.toString());
		}
	}
	try {
		DT_MODIF.setTime(rs.getTimestamp("DT_MODIF"));
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TimeStampBean (RS) : " + e.toString());
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:54:15)
 * @return java.util.Calendar
 */
public java.util.Calendar getDT_CREAT() {
	return DT_CREAT;
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:54:15)
 * @return java.util.Calendar
 */
public java.util.Calendar getDT_MODIF() {
	return DT_MODIF;
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:54:15)
 * @param newDT_CREAT java.util.Calendar
 */
public void setDT_CREAT(java.util.Calendar newDT_CREAT) {
	DT_CREAT = newDT_CREAT;
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:54:15)
 * @param newDT_MODIF java.util.Calendar
 */
public void setDT_MODIF(java.util.Calendar newDT_MODIF) {
	DT_MODIF = newDT_MODIF;
}
}
