package com.increg.salon.request;

import java.math.*;

/**
 * Insert the type's description here.
 * Creation date: (15/10/2001 21:28:12)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
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
	protected String[] SENS;
    /**
     * Total des sorties par type de mouvements
     */
	protected java.math.BigDecimal[] SORTIE;
    
/**
 * Brouillard constructor comment.
 */
public Journal() {
	super();

	SORTIE = new BigDecimal[10];
	SENS = new String[10];
	SOLDE_FINAL = new BigDecimal("0.00");
	SOLDE_FINAL.setScale(2);
	SOLDE_INIT = new BigDecimal("0.00");
	SOLDE_INIT.setScale(2);
	
}
/**
 * Insert the method's description here.
 * Creation date: (18/10/2001 08:45:00)
 */
public void calculFinal() {

	// Calcul le final
	BigDecimal total = SOLDE_INIT;

	for (int i = 0; i < SORTIE.length; i++) {
		if (SORTIE[i] != null) {
	        if ((SENS[i] != null) && (SENS[i].equals("S"))) {
	            total = total.subtract(SORTIE[i]);
	        }
	        else if ((SENS[i] != null) && (SENS[i].equals("E"))) {
	            total = total.add(SORTIE[i]);
	        }
		}
	}
	SOLDE_FINAL = total;
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
public String[] getSENS() {
	return SENS;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind Indice pour Type de mouvement
 * @return java.math.BigDecimal
 */
public String getSENS(int ind) {
	return SENS[ind];
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:29:33)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getSOLDE_FINAL() {
	return SOLDE_FINAL;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:29:13)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getSOLDE_INIT() {
	return SOLDE_INIT;
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
 * @param ind Indice pour Type de mouvement
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getSORTIE(int ind) {
	return SORTIE[ind];
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
 * @param newSENS java.math.BigDecimal[]
 */
public void setSENS(String[] newSENS) {
	SENS = newSENS;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind int
 * @param newSENS java.math.BigDecimal
 */
public void setSENS(int ind, String newSENS) {
	SENS[ind] = newSENS;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:29:33)
 * @param newSOLDE_FINAL java.math.BigDecimal
 */
public void setSOLDE_FINAL(java.math.BigDecimal newSOLDE_FINAL) {
	SOLDE_FINAL = newSOLDE_FINAL;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:29:13)
 * @param newSOLDE_INIT java.math.BigDecimal
 */
public void setSOLDE_INIT(java.math.BigDecimal newSOLDE_INIT) {
	SOLDE_INIT = newSOLDE_INIT;
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
	SORTIE[ind] = newSORTIE;
}
/**
 * Insert the method's description here.
 * Creation date: (15/10/2001 21:30:19)
 * @param ind int
 * @param newSORTIE java.math.BigDecimal
 */
public void addSORTIE(int ind, java.math.BigDecimal newSORTIE) {
    if (SORTIE[ind] == null) {
        SORTIE[ind] = newSORTIE;
    }
    else {
        SORTIE[ind] = SORTIE[ind].add(newSORTIE);
    }
}
}
