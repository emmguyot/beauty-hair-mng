/*
 * Bean gérant les dates de créations / Modification
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.commun;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Bean gérant les dates de créations / Modification
 * Creation date: (08/07/2001 17:17:53)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class TimeStampBean extends GenericBean {

	/**
	 * Date de création du bean
	 */
	protected Calendar	DT_CREAT;
	/**
	 * Date de modification du bean
	 */
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
	 * Constructeur par défaut à utiliser pour tout contexte international 
	 * @param rb Messages à utiliser
	 */
	public TimeStampBean(ResourceBundle rb) {
		super(rb);
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
	 * Creation date: (18/07/2001 22:56:22)
	 */
	public TimeStampBean(ResultSet rs, ResourceBundle rb) {
		this(rs);
		message = rb;
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
