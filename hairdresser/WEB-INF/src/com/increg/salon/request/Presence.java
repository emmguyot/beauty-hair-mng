package com.increg.salon.request;

import java.util.*;
import java.math.*;
/**
 * Insert the type's description here.
 * Creation date: (02/11/2001 19:31:45)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class Presence {
	protected java.lang.String PRENOM = "";
	protected BigDecimal pointage[];
	protected java.util.Date debut;
/**
 * Presence constructor comment.
 */
public Presence() {
	super();
	pointage = new BigDecimal[20];
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:42:57)
 * @return java.util.Date
 */
public java.util.Date getDebut() {
	return debut;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:37:25)
 * @return java.util.Vector
 */
public BigDecimal[] getPointage() {
	return pointage;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:37:25)
 * @return java.util.Vector
 */
public BigDecimal getPointage(int ind) {
	return pointage[ind];
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:36:53)
 * @return java.lang.String
 */
public java.lang.String getPRENOM() {
	return PRENOM;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:42:57)
 * @param newDebut java.util.Date
 */
public void setDebut(java.util.Date newDebut) {
	debut = newDebut;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:37:25)
 * @param newPointage java.util.Vector
 */
public void setPointage(BigDecimal[] newPointage) {
	pointage = newPointage;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:37:25)
 * @param newPointage java.util.Vector
 */
public void setPointage(int ind, BigDecimal newPointage) {
	pointage[ind] = newPointage;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:36:53)
 * @param newPRENOM java.lang.String
 */
public void setPRENOM(java.lang.String newPRENOM) {
	PRENOM = newPRENOM;
}
}
