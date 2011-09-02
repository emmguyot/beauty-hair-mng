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

public class Brouillard {
    /**
     * Date de la ligne de Brouillard
     */
	protected java.util.Calendar DT_PAIEMENT;
    /**
     * Entrée : Indicée sur le code type de prestation
     * Représente la première partie du Brouillard => Les factures
     */
	protected java.math.BigDecimal[] ENTREE;
    /**
     * Sortie : Indicée sur le code mode de règlement
     * Représente la seconde partie du Brouillard => les encaissements
     */
	protected java.math.BigDecimal[] SORTIE;
    /**
     * Remise : Indicée sur le code type de prestation
     * Répartition en fonction du paramétrage
     */
	protected java.math.BigDecimal[] REMISE;

/**
 * Brouillard constructor comment.
 */
public Brouillard() {
	super();

	ENTREE = new BigDecimal[10];
	REMISE = new BigDecimal[10];
	SORTIE = new BigDecimal[10];
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:28:49)
 * @return java.util.Calendar
 */
public java.util.Calendar getDT_PAIEMENT() {
	return DT_PAIEMENT;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @return java.math.BigDecimal[]
 */
public java.math.BigDecimal[] getENTREE() {
	return ENTREE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind Indice demandé
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getENTREE(int ind) {
	return ENTREE[ind];
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:32:29)
 * @return java.math.BigDecimal[]
 */
public java.math.BigDecimal[] getREMISE() {
	return REMISE;
}
/**
 * Insert the method's description here.
 * Creation date: 31 août 02
 * @param ind Indice demandé
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getREMISE(int ind) {
    return REMISE[ind];
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @return java.math.BigDecimal[]
 */
public java.math.BigDecimal[] getSORTIE() {
	return SORTIE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind Indice demandé
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getSORTIE(int ind) {
	return SORTIE[ind];
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotalENTREE() {

	BigDecimal total = new BigDecimal("0.00");
	
	for (int i = 0; i < ENTREE.length; i++) {
		if (ENTREE[i] != null) {
			total = total.add(ENTREE[i]);
		}
	}
	return total;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotalSORTIE() {

	BigDecimal total = new BigDecimal("0.00");
	
	for (int i = 0; i < SORTIE.length; i++) {
		if (SORTIE[i] != null) {
			total = total.add(SORTIE[i]);
		}
	}
	return total;
}
/**
 * Calcul le total des remises
 * Creation date: (15/10/2001 21:30:19)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotalREMISE() {

    BigDecimal total = new BigDecimal("0.00");
    
    for (int i = 0; i < REMISE.length; i++) {
        if (REMISE[i] != null) {
            total = total.add(REMISE[i]);
        }
    }
    return total;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:28:49)
 * @param newDT_PAIEMENT java.util.Calendar
 */
public void setDT_PAIEMENT(java.util.Calendar newDT_PAIEMENT) {
	DT_PAIEMENT = newDT_PAIEMENT;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param newENTREE java.math.BigDecimal[]
 */
public void setENTREE(java.math.BigDecimal[] newENTREE) {
	ENTREE = newENTREE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind int
 * @param newENTREE java.math.BigDecimal
 */
public void setENTREE(int ind, java.math.BigDecimal newENTREE) {
    // Extension du tableau si besoin
    if (ind > ENTREE.length) {
        BigDecimal[] futurENTREE = new BigDecimal[ind + 1];
        
        for (int i = 0; i < ENTREE.length; i++) {
            futurENTREE[i] = ENTREE[i];
        }
        ENTREE = futurENTREE;
    }
	ENTREE[ind] = newENTREE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:32:29)
 * @param newREMISE java.math.BigDecimal[]
 */
public void setREMISE(java.math.BigDecimal[] newREMISE) {
	REMISE = newREMISE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:32:29)
 * @param ind Indice
 * @param newREMISE java.math.BigDecimal
 */
public void setREMISE(int ind, java.math.BigDecimal newREMISE) {
    // Extension du tableau si besoin
    if (ind > REMISE.length) {
        BigDecimal[] futurREMISE = new BigDecimal[ind + 1];
        
        for (int i = 0; i < REMISE.length; i++) {
            futurREMISE[i] = REMISE[i];
        }
        REMISE = futurREMISE;
    }
    REMISE[ind] = newREMISE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param newSORTIE java.math.BigDecimal[]
 */
public void setSORTIE(java.math.BigDecimal[] newSORTIE) {
	SORTIE = newSORTIE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind int
 * @param newSORTIE java.math.BigDecimal
 */
public void setSORTIE(int ind, java.math.BigDecimal newSORTIE) {
    // Extension du tableau si besoin
    if (ind > SORTIE.length) {
        BigDecimal[] futurSORTIE = new BigDecimal[ind + 1];
        
        for (int i = 0; i < SORTIE.length; i++) {
            futurSORTIE[i] = SORTIE[i];
        }
        SORTIE = futurSORTIE;
    }
	SORTIE[ind] = newSORTIE;
}
}
