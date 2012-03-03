/*
 * Journal comptable
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
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class Journal {
	/**
	 * Date de la ligne du journal
	 */
	protected java.util.Calendar DT_PAIEMENT;

	/**
	 * Solde de début de période
	 */
	protected java.math.BigDecimal SOLDE_INIT;

	/**
	 * Solde de fin de période
	 */
	protected java.math.BigDecimal SOLDE_FINAL;

	/**
	 * Sens des types mouvements
	 */
	protected SortedMap<Integer, String> SENS;

	/**
	 * Total des sorties par type de mouvements
	 */
	protected SortedMap<Integer, BigDecimal> SORTIE;

	/**
	 * Brouillard constructor comment.
	 */
	public Journal() {
		super();

		SORTIE = new TreeMap<Integer, BigDecimal>();
		SENS = new TreeMap<Integer, String>();
		SOLDE_FINAL = new BigDecimal("0.00");
		SOLDE_FINAL.setScale(2);
		SOLDE_INIT = new BigDecimal("0.00");
		SOLDE_INIT.setScale(2);

	}

	/**
	 * Insert the method's description here. Creation date: (18/10/2001
	 * 08:45:00)
	 */
	public void calculFinal() {

		// Calcul le final
		BigDecimal total = SOLDE_INIT;

		for (Integer cd : SORTIE.keySet()) {
			if (SORTIE.get(cd) != null) {
				if ((SENS.get(cd) != null) && (SENS.get(cd).equals("S"))) {
					total = total.subtract(SORTIE.get(cd));
				} else if ((SENS.get(cd) != null) && (SENS.get(cd).equals("E"))) {
					total = total.add(SORTIE.get(cd));
				}
			}
		}
		SOLDE_FINAL = total;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:28:49)
	 * 
	 * @return java.util.Calendar
	 */
	public java.util.Calendar getDT_PAIEMENT() {
		return DT_PAIEMENT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @return java.math.BigDecimal[]
	 */
	public SortedMap<Integer, String> getSENS() {
		return SENS;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            Indice pour Type de mouvement
	 * @return java.math.BigDecimal
	 */
	public String getSENS(int ind) {
		return SENS.get(ind);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:29:33)
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSOLDE_FINAL() {
		return SOLDE_FINAL;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:29:13)
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSOLDE_INIT() {
		return SOLDE_INIT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @return java.math.BigDecimal[]
	 */
	public Collection<BigDecimal> getSORTIE() {
		return SORTIE.values();
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            Indice pour Type de mouvement
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSORTIE(int ind) {
		return SORTIE.get(ind);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:28:49)
	 * 
	 * @param newDT_PAIEMENT
	 *            java.util.Calendar
	 */
	public void setDT_PAIEMENT(java.util.Calendar newDT_PAIEMENT) {
		DT_PAIEMENT = newDT_PAIEMENT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param newSENS
	 *            java.math.BigDecimal[]
	 */
	public void setSENS(SortedMap<Integer, String> newSENS) {
		SENS = newSENS;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            int
	 * @param newSENS
	 *            java.math.BigDecimal
	 */
	public void setSENS(int ind, String newSENS) {
		SENS.put(ind, newSENS);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:29:33)
	 * 
	 * @param newSOLDE_FINAL
	 *            java.math.BigDecimal
	 */
	public void setSOLDE_FINAL(java.math.BigDecimal newSOLDE_FINAL) {
		SOLDE_FINAL = newSOLDE_FINAL;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:29:13)
	 * 
	 * @param newSOLDE_INIT
	 *            java.math.BigDecimal
	 */
	public void setSOLDE_INIT(java.math.BigDecimal newSOLDE_INIT) {
		SOLDE_INIT = newSOLDE_INIT;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param newSORTIE
	 *            java.math.BigDecimal[]
	 */
	public void setSORTIE(SortedMap<Integer, BigDecimal> newSORTIE) {
		SORTIE = newSORTIE;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            int
	 * @param newSORTIE
	 *            java.math.BigDecimal
	 */
	public void setSORTIE(int ind, java.math.BigDecimal newSORTIE) {
		SORTIE.put(ind, newSORTIE);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            int
	 * @param newSORTIE
	 *            java.math.BigDecimal
	 */
	public void addSORTIE(int ind, java.math.BigDecimal newSORTIE) {
		if (!SORTIE.containsKey(ind)) {
			SORTIE.put(ind, newSORTIE);
		} else {
			SORTIE.put(ind, SORTIE.get(ind).add(newSORTIE));
		}
	}
}
