package com.increg.salon.request;

import java.math.*;

/**
 * Récap des mouvements de stock
 * Creation date: (03/11/2001 21:47:30)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class Recap {
    /**
     * Code de l'article
     */
    protected int CD_ART = 0;
    /**
     * Libellé de l'article
     */
	protected String LIB_ART;
    /**
     * Tableau des mouvements (quantité)
     */
	protected BigDecimal MVT[];
    /**
     * Tableau des valeurs des mouvements
     */
    protected BigDecimal MVT_VAL[];
    /**
     * Taille max des tableaux
     */
	public static final int NB_MVT_MAX = 100;

/**
 * Recap constructor comment.
 */
public Recap() {
	super();
	MVT = new BigDecimal[NB_MVT_MAX + 1];
    MVT_VAL = new BigDecimal[NB_MVT_MAX + 1];
}
/**
 * Insert the method's description here.
 * Creation date: (03/11/2001 22:18:44)
 * @return int
 */
public int getCD_ART() {
	return CD_ART;
}
/**
 * Insert the method's description here.
 * Creation date: (03/11/2001 21:48:48)
 * @return java.lang.String
 */
public java.lang.String getLIB_ART() {
	return LIB_ART;
}
/**
 * Insert the method's description here.
 * Creation date: (03/11/2001 21:48:48)
 * @return java.math.BigDecimal[]
 */
public java.math.BigDecimal[] getMVT() {
	return MVT;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:37:25)
 * @param ind Indice du mouvement
 * @return java.util.Vector
 */
public BigDecimal getMVT(int ind) {
	return MVT[ind];
}
/**
 * Insert the method's description here.
 * Creation date: (03/11/2001 22:18:44)
 * @param newCD_ART int
 */
public void setCD_ART(int newCD_ART) {
	CD_ART = newCD_ART;
}
/**
 * Insert the method's description here.
 * Creation date: (03/11/2001 21:48:48)
 * @param newLIB_ART java.lang.String
 */
public void setLIB_ART(java.lang.String newLIB_ART) {
	LIB_ART = newLIB_ART;
}
/**
 * Insert the method's description here.
 * Creation date: (03/11/2001 21:48:48)
 * @param newMVT java.math.BigDecimal[]
 */
public void setMVT(java.math.BigDecimal[] newMVT) {
	MVT = newMVT;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2001 19:37:25)
 * @param ind indice du code mouvement
 * @param newMVT quantité du mouvement
 * @param newMVT_VAL valeur du mouvement
 */
public void setMVT(int ind, BigDecimal newMVT, BigDecimal newMVT_VAL) {
	MVT[ind] = newMVT;
    MVT_VAL[ind] = newMVT_VAL;
}
    /**
     * Returns the mVT_VAL.
     * @return BigDecimal[]
     */
    public BigDecimal[] getMVT_VAL() {
        return MVT_VAL;
    }

    /**
     * Returns the mVT_VAL.
     * @param ind indice du type de mouvement
     * @return BigDecimal
     */
    public BigDecimal getMVT_VAL(int ind) {
        return MVT_VAL[ind];
    }

}
