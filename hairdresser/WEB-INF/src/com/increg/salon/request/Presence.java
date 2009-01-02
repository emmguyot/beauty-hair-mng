/*
 * Présence du personnel
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

import java.math.BigDecimal;

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
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:42:57)
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getDebut() {
		return debut;
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:37:25)
	 * 
	 * @return java.util.Vector
	 */
	public BigDecimal[] getPointage() {
		return pointage;
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:37:25)
	 * 
	 * @return java.util.Vector
	 */
	public BigDecimal getPointage(int ind) {
		return pointage[ind];
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:36:53)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPRENOM() {
		return PRENOM;
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:42:57)
	 * 
	 * @param newDebut
	 *            java.util.Date
	 */
	public void setDebut(java.util.Date newDebut) {
		debut = newDebut;
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:37:25)
	 * 
	 * @param newPointage
	 *            java.util.Vector
	 */
	public void setPointage(BigDecimal[] newPointage) {
		pointage = newPointage;
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:37:25)
	 * 
	 * @param newPointage
	 *            java.util.Vector
	 */
	public void setPointage(int ind, BigDecimal newPointage) {
		pointage[ind] = newPointage;
	}

	/**
	 * Insert the method's description here. Creation date: (02/11/2001
	 * 19:36:53)
	 * 
	 * @param newPRENOM
	 *            java.lang.String
	 */
	public void setPRENOM(java.lang.String newPRENOM) {
		PRENOM = newPRENOM;
	}
}
