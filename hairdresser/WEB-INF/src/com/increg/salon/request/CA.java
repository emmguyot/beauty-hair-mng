/*
 * Chiffre d'affaire
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.salon.request;

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
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:38:39)
	 * 
	 * @return java.util.Calendar
	 */
	public java.util.Calendar getDT_PREST() {
		return DT_PREST;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:39:19)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLIB_TYP_VENT() {
		return LIB_TYP_VENT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:39:54)
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getMONTANT() {
		return MONTANT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:28:18)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPRENOM() {
		return PRENOM;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:38:39)
	 * 
	 * @param newDT_PREST
	 *            java.util.Date
	 */
	public void setDT_PREST(java.util.Calendar newDT_PREST) {
		DT_PREST = newDT_PREST;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:39:19)
	 * 
	 * @param newLIB_TYP_VENT
	 *            java.lang.String
	 */
	public void setLIB_TYP_VENT(java.lang.String newLIB_TYP_VENT) {
		LIB_TYP_VENT = newLIB_TYP_VENT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:39:54)
	 * 
	 * @param newMONTANT
	 *            java.math.BigDecimal
	 */
	public void setMONTANT(java.math.BigDecimal newMONTANT) {
		MONTANT = newMONTANT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 14:28:18)
	 * 
	 * @param newPRENOM
	 *            java.lang.String
	 */
	public void setPRENOM(java.lang.String newPRENOM) {
		PRENOM = newPRENOM;
	}
}
