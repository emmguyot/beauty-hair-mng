package com.increg.salon.request;

import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (15/10/2001 14:27:38)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CA {
	protected java.lang.String PRENOM;
	protected java.util.Calendar DT_PREST;
	protected java.lang.String LIB_TYP_VENT;
	protected java.math.BigDecimal MONTANT;
/**
 * CA constructor comment.
 */
public CA() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:38:39)
 * @return java.util.Calendar
 */
public java.util.Calendar getDT_PREST() {
	return DT_PREST;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:39:19)
 * @return java.lang.String
 */
public java.lang.String getLIB_TYP_VENT() {
	return LIB_TYP_VENT;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:39:54)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getMONTANT() {
	return MONTANT;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:28:18)
 * @return java.lang.String
 */
public java.lang.String getPRENOM() {
	return PRENOM;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:38:39)
 * @param newDT_PREST java.util.Date
 */
public void setDT_PREST(java.util.Calendar newDT_PREST) {
	DT_PREST = newDT_PREST;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:39:19)
 * @param newLIB_TYP_VENT java.lang.String
 */
public void setLIB_TYP_VENT(java.lang.String newLIB_TYP_VENT) {
	LIB_TYP_VENT = newLIB_TYP_VENT;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:39:54)
 * @param newMONTANT java.math.BigDecimal
 */
public void setMONTANT(java.math.BigDecimal newMONTANT) {
	MONTANT = newMONTANT;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 14:28:18)
 * @param newPRENOM java.lang.String
 */
public void setPRENOM(java.lang.String newPRENOM) {
	PRENOM = newPRENOM;
}
}
