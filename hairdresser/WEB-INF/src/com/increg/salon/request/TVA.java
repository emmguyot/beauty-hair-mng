package com.increg.salon.request;

import java.math.BigDecimal;

/**
 * Insert the type's description here.
 * Creation date: (28/12/2001 20:43:26)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TVA {
    /**
     * Type de prestation
     */
    protected int CD_TYP_VENT;
    /**
     * Total de la TVA
     */
    protected BigDecimal total;
    /**
     * Total du HT
     */
    protected BigDecimal totalHT;
    /**
     * Total du TTC
     */
    protected BigDecimal totalTTC;

    /**
     * TVA constructor comment.
     */
    public TVA() {
        super();
        total = new BigDecimal(0);
        totalHT = new BigDecimal(0);
        totalTTC = new BigDecimal(0);
    }
    /**
     * Insert the method's description here.
     * Creation date: (28/12/2001 20:43:48)
     * @return int
     */
    public int getCD_TYP_VENT() {
        return CD_TYP_VENT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (28/12/2001 20:44:28)
     * @return java.math.BigDecimal
     */
    public BigDecimal getTotal() {
        return total;
    }
    /**
     * Insert the method's description here.
     * Creation date: (28/12/2001 20:43:48)
     * @param newCD_TYP_VENT int
     */
    public void setCD_TYP_VENT(int newCD_TYP_VENT) {
        CD_TYP_VENT = newCD_TYP_VENT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (28/12/2001 20:44:28)
     * @param newTotal java.math.BigDecimal
     */
    public void setTotal(BigDecimal newTotal) {
        total = newTotal;
    }
    /**
     * @return Total du HT
     */
    public BigDecimal getTotalHT() {
        return totalHT;
    }

    /**
     * @return Total du TTC
     */
    public BigDecimal getTotalTTC() {
        return totalTTC;
    }

    /**
     * @param decimal Total du HT
     */
    public void setTotalHT(BigDecimal decimal) {
        totalHT = decimal;
    }

    /**
     * @param decimal Total du TTC
     */
    public void setTotalTTC(BigDecimal decimal) {
        totalTTC = decimal;
    }

}
