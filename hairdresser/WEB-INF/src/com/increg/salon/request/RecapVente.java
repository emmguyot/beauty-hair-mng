/*
 * Récap des ventes
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

import com.increg.salon.bean.PrestBean;

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
