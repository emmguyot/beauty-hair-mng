/*
 * Tests unitaires pour la class MvtStkBeanTest
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
package com.increg.salon.bean.test;

import junit.framework.TestCase;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ArtBean;
import com.increg.salon.bean.MvtStkBean;
import junit.framework.Assert;

import java.math.BigDecimal;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Tests unitaires pour la class MvtStkBeanTest
 * @author Alexandre Guyot <alexandre.guyot@laposte.net>
 */
public class MvtStkBeanTest extends TestCase {

    /**
     * Constructor for MvtStkBeanTest.
     * @param arg0 s   
     */
    public MvtStkBeanTest(String arg0) {
        super(arg0);
    }

    //Quelques valeurs utiles
    /**
     * Qunize en BigDecimal
     */
    public static final BigDecimal QUINZE = new BigDecimal(15);

    /**
     * Trente en BigDecimal
     */
    public static final BigDecimal TRENTE = new BigDecimal(30);

    /**
     * Cinq en BigDecimal
     */
    public static final BigDecimal CINQ = new BigDecimal(5);

    /**
     * Dix en BigDecimal
     */
    public static final BigDecimal DIX = new BigDecimal(10).setScale(2);

    /**
     * Zero en BigDecimal
     */
    public static final BigDecimal ZERO = new BigDecimal(0);

    /**
     * Vingt en BigDecimal
     */
    public static final BigDecimal VINGT = new BigDecimal(20);

    /**
     * 25 en BigDecimal
     */
    public static final BigDecimal VINGT_CINQ = new BigDecimal(25).setScale(2);

    /**
      *  Connexion à la base de donnée	
      */
    private DBSession aDBSession = new DBSession("config");

    /**
     * Sets up the environment
     * @exception Exception jetée quand le set up echoue
     */
    protected void setUp() throws Exception {
        super.setUp();

    }

    /**	
     * Teste le calcul du stock en cas d'entrée
     * @exception Exception jetée quand l'assertion echoue
     */
    public void testEntree() throws Exception {
        Assert.assertEquals(MvtStkBean.calculStock(CINQ, ZERO, DIX, ZERO, "E"), new MvtStkBean.Stock(CINQ, DIX));
        Assert.assertEquals(MvtStkBean.calculStock(CINQ, QUINZE, DIX, TRENTE, "E"), new MvtStkBean.Stock(VINGT, VINGT_CINQ));
    }

    /**	
     * Teste le calcul du stock en cas de sortie
     * @exception Exception jetée quand l'assertion echoue
     */
    public void testSortie() throws Exception {
        Assert.assertEquals(MvtStkBean.calculStock(CINQ, ZERO, DIX, ZERO, "S"), new MvtStkBean.Stock(CINQ.negate(), ZERO));
        Assert.assertEquals(MvtStkBean.calculStock(CINQ, QUINZE, DIX, TRENTE, "S"), new MvtStkBean.Stock(DIX.setScale(0), TRENTE));

    }

    /**	
     * Teste le calcul du stock en cas d'inventaire
     * @exception Exception jetée quand l'assertion echoue
     */
    public void testInventaire() throws Exception {
        Assert.assertEquals(MvtStkBean.calculStock(CINQ, ZERO, DIX, ZERO, "I"), new MvtStkBean.Stock(CINQ, DIX));
        Assert.assertEquals(MvtStkBean.calculStock(CINQ, QUINZE, DIX, TRENTE, "I"), new MvtStkBean.Stock(CINQ, DIX));

    }

    /**
     * Test la passage à mixte d'un article Vente
     */
    public void testMixteVente() {

        /**
         * Création de l'article de type vente
         */
        ArtBean aArt = new ArtBean(ResourceBundle.getBundle("messages"));
        aArt.setLIB_ART("JUnit Test article");
        aArt.setQTE_STK("1");
        aArt.setVAL_STK_HT("10");
        aArt.setCD_TYP_ART(1);
        aArt.setCD_CATEG_ART(2);
        try {
            aArt.create(aDBSession);
        }
        catch (SQLException e) {
            Assert.fail(e.toString());
        }

        ArtBean aArt2 = ArtBean.getArtBean(aDBSession, Long.toString(aArt.getCD_ART()));
        Assert.assertEquals(aArt2.getINDIC_MIXTE(), "N");

        /**
         * Vente de l'article
         */
        MvtStkBean aMvt = new MvtStkBean();
        aMvt.setCD_ART(aArt.getCD_ART());
        aMvt.setCD_TYP_MVT(1); // Vente
        aMvt.setQTE("1");
        aMvt.setVAL_MVT_HT("10");
        aMvt.setSTK_AVANT(aArt.getQTE_STK());
        aMvt.setVAL_STK_AVANT(aArt.getVAL_STK_HT());
        aMvt.setDT_MVT(Calendar.getInstance());
        try {
            aMvt.create(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        aArt2 = ArtBean.getArtBean(aDBSession, Long.toString(aArt.getCD_ART()));
        Assert.assertEquals(aArt2.getINDIC_MIXTE(), "N");

        try {
            aMvt.delete(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        /**
         * Utilisation de l'article
         */
        aMvt = new MvtStkBean();
        aMvt.setCD_ART(aArt.getCD_ART());
        aMvt.setCD_TYP_MVT(3); // Utilisation
        aMvt.setQTE("1");
        aMvt.setVAL_MVT_HT("10");
        aMvt.setSTK_AVANT(aArt.getQTE_STK());
        aMvt.setVAL_STK_AVANT(aArt.getVAL_STK_HT());
        aMvt.setDT_MVT(Calendar.getInstance());
        try {
            aMvt.create(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        aArt2 = ArtBean.getArtBean(aDBSession, Long.toString(aArt.getCD_ART()));
        Assert.assertEquals(aArt2.getINDIC_MIXTE(), "O");

        try {
            aMvt.delete(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        try {
            aArt.delete(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    /**
     * Test la passage à mixte d'un article Utilisation
     */
    public void testMixteUtilisation() {

        /**
         * Création de l'article de type vente
         */
        ArtBean aArt = new ArtBean(ResourceBundle.getBundle("messages"));
        aArt.setLIB_ART("JUnit Test article");
        aArt.setQTE_STK("1");
        aArt.setVAL_STK_HT("10");
        aArt.setCD_TYP_ART(2);
        aArt.setCD_CATEG_ART(2);
        try {
            aArt.create(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        ArtBean aArt2 = ArtBean.getArtBean(aDBSession, Long.toString(aArt.getCD_ART()));
        Assert.assertEquals(aArt2.getINDIC_MIXTE(), "N");

        /**
         * Vente de l'article
         */
        MvtStkBean aMvt = new MvtStkBean();
        aMvt.setCD_ART(aArt.getCD_ART());
        aMvt.setCD_TYP_MVT(1); // Vente
        aMvt.setQTE("1");
        aMvt.setVAL_MVT_HT("10");
        aMvt.setSTK_AVANT(aArt.getQTE_STK());
        aMvt.setVAL_STK_AVANT(aArt.getVAL_STK_HT());
        aMvt.setDT_MVT(Calendar.getInstance());
        try {
            aMvt.create(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        aArt2 = ArtBean.getArtBean(aDBSession, Long.toString(aArt.getCD_ART()));
        Assert.assertEquals(aArt2.getINDIC_MIXTE(), "N");

        try {
            aMvt.delete(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        /**
         * Utilisation de l'article
         */
        aMvt = new MvtStkBean();
        aMvt.setCD_ART(aArt.getCD_ART());
        aMvt.setCD_TYP_MVT(3); // Utilisation
        aMvt.setQTE("1");
        aMvt.setVAL_MVT_HT("10");
        aMvt.setSTK_AVANT(aArt.getQTE_STK());
        aMvt.setVAL_STK_AVANT(aArt.getVAL_STK_HT());
        aMvt.setDT_MVT(Calendar.getInstance());
        try {
            aMvt.create(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        aArt2 = ArtBean.getArtBean(aDBSession, Long.toString(aArt.getCD_ART()));
        Assert.assertEquals(aArt2.getINDIC_MIXTE(), "N");

        try {
            aMvt.delete(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }

        try {
            aArt.delete(aDBSession);
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

}
