/*
 * Brouillard comptable
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
import java.util.SortedMap;
import java.util.TreeMap;

public class Brouillard {
	/**
	 * Date de la ligne de Brouillard
	 */
	protected java.util.Calendar DT_PAIEMENT;
	/**
	 * Entrée : Indicée sur le code type de prestation Représente la première
	 * partie du Brouillard => Les factures
	 */
	protected SortedMap<Integer, BigDecimal> ENTREE;
	/**
	 * Sortie : Indicée sur le code mode de règlement Représente la seconde
	 * partie du Brouillard => les encaissements
	 */
	protected SortedMap<Integer, BigDecimal> SORTIE;
	/**
	 * Remise : Indicée sur le code type de prestation Répartition en fonction
	 * du paramétrage
	 */
	protected SortedMap<Integer, BigDecimal> REMISE;

	/**
	 * Brouillard constructor comment.
	 */
	public Brouillard() {
		super();

		ENTREE = new TreeMap<Integer, BigDecimal>();
		REMISE = new TreeMap<Integer, BigDecimal>();
		SORTIE = new TreeMap<Integer, BigDecimal>();
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
	public SortedMap<Integer, BigDecimal> getENTREE() {
		return ENTREE;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            Indice demandé
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getENTREE(int ind) {
		return ENTREE.get(ind);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:32:29)
	 * 
	 * @return java.math.BigDecimal[]
	 */
	public SortedMap<Integer, BigDecimal> getREMISE() {
		return REMISE;
	}

	/**
	 * Insert the method's description here. Creation date: 31 août 02
	 * 
	 * @param ind
	 *            Indice demandé
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getREMISE(int ind) {
		return REMISE.get(ind);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @return java.math.BigDecimal[]
	 */
	public SortedMap<Integer, BigDecimal> getSORTIE() {
		return SORTIE;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            Indice demandé
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSORTIE(int ind) {
		return SORTIE.get(ind);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotalENTREE() {

		BigDecimal total = new BigDecimal("0.00");

		for (BigDecimal valeur : ENTREE.values()) {
			total = total.add(valeur);
		}
		return total;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotalSORTIE() {

		BigDecimal total = new BigDecimal("0.00");

		for (BigDecimal valeur : SORTIE.values()) {
			total = total.add(valeur);
		}
		return total;
	}

	/**
	 * Calcul le total des remises Creation date: (15/10/2001 21:30:19)
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getTotalREMISE() {

		BigDecimal total = new BigDecimal("0.00");

		for (BigDecimal valeur : REMISE.values()) {
			total = total.add(valeur);
		}
		return total;
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
	 * @param newENTREE
	 *            java.math.BigDecimal[]
	 */
	public void setENTREE(SortedMap<Integer, BigDecimal> newENTREE) {
		ENTREE = newENTREE;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:30:19)
	 * 
	 * @param ind
	 *            int
	 * @param newENTREE
	 *            java.math.BigDecimal
	 */
	public void setENTREE(int ind, java.math.BigDecimal newENTREE) {
		ENTREE.put(ind, newENTREE);
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:32:29)
	 * 
	 * @param newREMISE
	 *            java.math.BigDecimal[]
	 */
	public void setREMISE(SortedMap<Integer, BigDecimal> newREMISE) {
		REMISE = newREMISE;
	}

	/**
	 * Insert the method's description here. Creation date: (15/10/2001
	 * 21:32:29)
	 * 
	 * @param ind
	 *            Indice
	 * @param newREMISE
	 *            java.math.BigDecimal
	 */
	public void setREMISE(int ind, java.math.BigDecimal newREMISE) {
		REMISE.put(ind, newREMISE);
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
}
