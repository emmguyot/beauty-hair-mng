/*
 * Created on 9 oct. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.salon.request;

import java.math.BigDecimal;

import com.increg.salon.bean.PrestBean;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RecapVente {

    /**
     * Prestation concernée
     */
    protected PrestBean prest;
    
    /**
     * Total HT des ventes
     */
    protected BigDecimal HT;

    /**
     * Total TVA des ventes
     */
    protected BigDecimal TVA;

    /**
     * Total TTC des ventes
     */
    protected BigDecimal TTC;
    
    /**
     * Quantité vendu
     */
    protected BigDecimal qte;
    
    /**
     * Constructeur par défaut 
     */
    public RecapVente() {
    }

    /**
     * @return HT des ventes
     */
    public BigDecimal getHT() {
        return HT;
    }

    /**
     * @return Prestation concernée
     */
    public PrestBean getPrest() {
        return prest;
    }

    /**
     * @return TTC des ventes
     */
    public BigDecimal getTTC() {
        return TTC;
    }

    /**
     * @return TVA des ventes
     */
    public BigDecimal getTVA() {
        return TVA;
    }

    /**
     * @param decimal HT des ventes
     */
    public void setHT(BigDecimal decimal) {
        HT = decimal;
    }

    /**
     * @param bean Prestation concernée
     */
    public void setPrest(PrestBean bean) {
        prest = bean;
    }

    /**
     * @param decimal TTC des ventes
     */
    public void setTTC(BigDecimal decimal) {
        TTC = decimal;
    }

    /**
     * @param decimal TVA des ventes
     */
    public void setTVA(BigDecimal decimal) {
        TVA = decimal;
    }

    /**
     * @return Quantité vendu
     */
    public BigDecimal getQte() {
        return qte;
    }

    /**
     * @param decimal Quantité vendu
     */
    public void setQte(BigDecimal decimal) {
        qte = decimal;
    }

}
