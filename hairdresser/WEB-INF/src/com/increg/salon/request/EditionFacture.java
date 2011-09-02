/*
 * Bean de facture papier
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

import java.util.Vector;

import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.ReglementBean;

public class EditionFacture {
	PaiementBean myPaiement;

	FactBean myFact;
	
    Vector<ReglementBean> reglements;


	/**
	 * EditionFacture constructor comment.
	 */
	public EditionFacture() {
		super();
	}

	/**
	 * Insert the method's description here. Creation date: (04/11/2001
	 * 17:07:08)
	 * 
	 * @return com.increg.salon.bean.FactBean
	 */
	public com.increg.salon.bean.FactBean getMyFact() {
		return myFact;
	}

	/**
	 * Insert the method's description here. Creation date: (04/11/2001
	 * 16:44:20)
	 * 
	 * @return com.increg.salon.bean.PaiementBean
	 */
	public com.increg.salon.bean.PaiementBean getMyPaiement() {
		return myPaiement;
	}

	/**
	 * Insert the method's description here. Creation date: (04/11/2001
	 * 17:07:08)
	 * 
	 * @param newMyFact
	 *            com.increg.salon.bean.FactBean
	 */
	public void setMyFact(com.increg.salon.bean.FactBean newMyFact) {
		myFact = newMyFact;
	}

	/**
	 * Insert the method's description here. Creation date: (04/11/2001
	 * 16:44:20)
	 * 
	 * @param newMyPaiement
	 *            com.increg.salon.bean.PaiementBean
	 */
	public void setMyPaiement(com.increg.salon.bean.PaiementBean newMyPaiement) {
		myPaiement = newMyPaiement;
	}

	/**
	 * @return the reglements
	 */
	public Vector<ReglementBean> getReglements() {
		return reglements;
	}

	/**
	 * @param reglements the reglements to set
	 */
	public void setReglements(Vector<ReglementBean> reglements) {
		this.reglements = reglements;
	}
}
