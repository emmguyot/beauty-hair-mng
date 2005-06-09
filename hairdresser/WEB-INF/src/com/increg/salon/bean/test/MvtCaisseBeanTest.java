/*
 * Test des mouvements de caisse 
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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import com.increg.commun.DBSession;
import com.increg.salon.bean.CaisseBean;
import com.increg.salon.bean.ModReglBean;
import com.increg.salon.bean.MvtCaisseBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.util.SimpleDateFormatEG;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * Test des mouvements de caisse
 * 
 */
public class MvtCaisseBeanTest extends TestCase {

    /**
      *  Connexion à la base de donnée  
      */
    private DBSession aDBSession = new DBSession("config");


    /**
     * Constructor for MvtCaisseBeanTest.
     * @param arg0 .
     */
    public MvtCaisseBeanTest(String arg0) {
        super(arg0);
    }

    /**
     * Test for Vector getMvtCaisseBean(DBSession, String)
     * @throws Exception en cas d'erreur non prévue
     */
    public void testGetMvtCaisseBeanDBSessionString() throws Exception {
       
        long cdPaiement = 999999;

        PaiementBean aPaiement = new PaiementBean(ResourceBundle.getBundle("messages"));
        MvtCaisseBean aMvt1 = new MvtCaisseBean();        
        MvtCaisseBean aMvt2 = new MvtCaisseBean();        
        MvtCaisseBean aMvt3 = new MvtCaisseBean();        
        try {         
            // Creation du Paiement
            aPaiement.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
            aPaiement.setCD_PAIEMENT(cdPaiement);
            aPaiement.setDT_PAIEMENT("06/07/2002", Locale.getDefault());
            aPaiement.setPRX_TOT_TTC("1001.02");
            aPaiement.create(aDBSession);
            
            aMvt1.setDT_MVT("06/07/2002 12:12:12", Locale.getDefault());
            aMvt1.setCD_PAIEMENT(cdPaiement);
            aMvt1.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
            aMvt1.setCD_TYP_MCA(1); // Encaissement
            aMvt1.setMONTANT("10.00");
            aMvt1.setSOLDE_AVANT("1000.00");
            aMvt1.setCOMM("Blabla");
            aMvt1.create(aDBSession);
    
            aMvt2.setDT_MVT("01/07/2002 12:12:13", Locale.getDefault());
            aMvt2.setCD_PAIEMENT(cdPaiement);
            aMvt2.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
            aMvt2.setCD_TYP_MCA(1); // Encaissement
            aMvt2.setMONTANT("-10.00");
            aMvt2.setSOLDE_AVANT("1000.00");
            aMvt2.setCOMM("Blabla");
            aMvt2.create(aDBSession);
    
            aMvt3.setDT_MVT("01/07/2002 12:12:12", Locale.getDefault());
            aMvt3.setCD_PAIEMENT(cdPaiement);
            aMvt3.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
            aMvt3.setCD_TYP_MCA(1); // Encaissement
            aMvt3.setMONTANT("10.00");
            aMvt3.setSOLDE_AVANT("1000.00");
            aMvt3.setCOMM("Blabla");
            aMvt3.create(aDBSession);
                    
            Vector res = MvtCaisseBean.getMvtCaisseBean(aDBSession, Long.toString(cdPaiement));
            
            Assert.assertNotNull(res);
            Assert.assertEquals(res.size(), 3);
            for (int i = 0; i < res.size(); i++) {
                MvtCaisseBean aMvt = (MvtCaisseBean) res.get(i);
                Assert.assertTrue((aMvt.getDT_MVT().equals(aMvt1.getDT_MVT()))
                                || (aMvt.getDT_MVT().equals(aMvt2.getDT_MVT()))
                                || (aMvt.getDT_MVT().equals(aMvt3.getDT_MVT())));
                Assert.assertTrue((aMvt.getMONTANT().equals(aMvt1.getMONTANT()))
                                || (aMvt.getMONTANT().equals(aMvt2.getMONTANT()))
                                || (aMvt.getMONTANT().equals(aMvt3.getMONTANT())));
                Assert.assertTrue((aMvt.getCD_MOD_REGL() == aMvt1.getCD_MOD_REGL())
                                || (aMvt.getCD_MOD_REGL() == aMvt2.getCD_MOD_REGL())
                                || (aMvt.getCD_MOD_REGL() == aMvt3.getCD_MOD_REGL()));
            }

                    
        }
        catch (Exception e) {
            throw new Exception("Erreur :" + e.toString());
        }
        finally {
            // Nettoyage
            aPaiement.delete(aDBSession);
            aMvt1.delete(aDBSession);
            aMvt2.delete(aDBSession);
            aMvt3.delete(aDBSession);
        }
    }

    /**
     * Verification de la fonction de calcul du solde
     */
    public void testCalculSolde() {
        
        BigDecimal soldeAvant = new BigDecimal("1000.02");
        BigDecimal qte = new BigDecimal("1.03");
        MvtCaisseBean aMvt = new MvtCaisseBean();
        aMvt.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
        aMvt.setCD_TYP_MCA(1); // Encaissement
        aMvt.setMONTANT(qte);
        aMvt.setSOLDE_AVANT(soldeAvant);
        aMvt.setCOMM("Blabla");
        
        Assert.assertEquals(MvtCaisseBean.calculSolde(aDBSession, aMvt.getCD_TYP_MCA(), soldeAvant, qte), new BigDecimal("1001.05"));
        Assert.assertEquals(MvtCaisseBean.calculSolde(aDBSession, aMvt.getCD_TYP_MCA(), soldeAvant, qte.negate()), new BigDecimal("998.99"));

        /******************/
        aMvt = new MvtCaisseBean();
        aMvt.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
        aMvt.setCD_TYP_MCA(2); // Dépot banque
        aMvt.setMONTANT(qte);
        aMvt.setSOLDE_AVANT(soldeAvant);
        aMvt.setCOMM("Blabla");

        Assert.assertEquals(MvtCaisseBean.calculSolde(aDBSession, aMvt.getCD_TYP_MCA(), soldeAvant, qte), new BigDecimal("998.99"));
        Assert.assertEquals(MvtCaisseBean.calculSolde(aDBSession, aMvt.getCD_TYP_MCA(), soldeAvant, qte.negate()), new BigDecimal("1001.05"));

        /******************/
        aMvt = new MvtCaisseBean();
        aMvt.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
        aMvt.setCD_TYP_MCA(4); // Inventaire
        aMvt.setMONTANT(qte);
        aMvt.setSOLDE_AVANT(soldeAvant);
        aMvt.setCOMM("Blabla");

        Assert.assertEquals(MvtCaisseBean.calculSolde(aDBSession, aMvt.getCD_TYP_MCA(), soldeAvant, qte), new BigDecimal("1.03"));
        Assert.assertEquals(MvtCaisseBean.calculSolde(aDBSession, aMvt.getCD_TYP_MCA(), soldeAvant, qte.negate()), new BigDecimal("-1.03"));
    }

    /**
     * Test de la vérification d'une caisse
     * @throws Exception En cas d'erreur programme
     */
    public void testCheckAndFix() throws Exception {
        
        // Par défaut une caisse est bonne
        Assert.assertTrue(MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_ESP), "01/01/2002 12:12:12"));
        
        // Insertion d'une incohérence dans la caisse : Le solde final est bon, mais des mises à jour sont nécessaires
        MvtCaisseBean aMvt = new MvtCaisseBean();
        aMvt.setDT_MVT(Calendar.getInstance());
        aMvt.setCD_PAIEMENT(0);
        aMvt.setDEVISE("EUR");
        aMvt.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
        aMvt.setCD_TYP_MCA(2); // Dépot banque
        aMvt.setMONTANT("10");
        aMvt.setSOLDE_AVANT("0"); // Mauvais solde avant pour erreur
        aMvt.setCOMM("Incohérence ajoutée pour test");
        aMvt.create(aDBSession);
        
        Assert.assertTrue(MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_ESP), "01/01/2002 12:12:12"));

        aMvt.delete(aDBSession);

        // Insertion d'une incohérence dans la caisse : Le solde final n'est pas bon et des mises à jour sont nécessaires
        aMvt = new MvtCaisseBean();
        aMvt.setDT_MVT(Calendar.getInstance());
        aMvt.setCD_PAIEMENT(0);
        aMvt.setDEVISE("EUR");
        aMvt.setCD_MOD_REGL(ModReglBean.MOD_REGL_ESP);
        aMvt.setCD_TYP_MCA(2); // Dépot banque
        aMvt.setMONTANT("10");
        aMvt.setSOLDE_AVANT("0"); // Mauvais solde avant pour erreur
        aMvt.setCOMM("Incohérence ajoutée pour test");
        aMvt.create(aDBSession);
        
        aMvt.setMONTANT("20");
        aMvt.maj(aDBSession);  // Mise à jour : Ne modifie pas la caisse => Incohérence finale
        
        Assert.assertTrue(!MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_ESP), "01/01/2002 12:12:12"));

        aMvt.delete(aDBSession);
    }

    /**
     * Test de la purge
     * @throws Exception En cas d'erreur programme
     */
    public void testPurge() throws Exception {

        CaisseBean aCaisse = null;;
        MvtCaisseBean aMvt = null;
        ModReglBean aModRegl = null;
        try {
            SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy");
            Date limite = formatDate.parse("01/01/1999");
            
            // Utilisation d'une caisse spécifique
            aCaisse = CaisseBean.getCaisseBean(aDBSession, "10");
            
            if (aCaisse == null) {
                // Il faut créer la caisse et le mode de réglement
                aModRegl = new ModReglBean();
                aModRegl.setCD_MOD_REGL(10);
                aModRegl.setIMP_CHEQUE("N");
                aModRegl.setLIB_MOD_REGL("Mode de règlement pour tests unitaires");
                aModRegl.setRENDU_MONNAIE("N");
                aModRegl.setUTILISABLE("N");
                aModRegl.create(aDBSession);
                 
                aCaisse = CaisseBean.getCaisseBean(aDBSession, "10");
            }
            
            // Purge à une date lointaine : 01/01/1999
            // Rien à purger théoriquement
            Assert.assertEquals(MvtCaisseBean.purge(aDBSession, limite), 0);
            
            // Création d'un vieux mouvement
            aMvt = new MvtCaisseBean();
            aMvt.setDT_MVT("01/01/1998 00:00:00", Locale.getDefault());
            aMvt.setCD_PAIEMENT(0);
            aMvt.setDEVISE("EUR");
            aMvt.setCD_MOD_REGL(10);
            aMvt.setCD_TYP_MCA(2); // Dépot banque
            aMvt.setMONTANT("10");
            aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());
            aMvt.setCOMM("Mouvement historique pour test purge");
            aMvt.create(aDBSession);
            
            Assert.assertEquals(MvtCaisseBean.purge(aDBSession, limite), 1);
            
            CaisseBean caisseApres = CaisseBean.getCaisseBean(aDBSession, "10");
            
            Assert.assertEquals(aCaisse.getSOLDE_DEBUT().add(aMvt.getMONTANT().negate()), caisseApres.getSOLDE_DEBUT());
            Assert.assertEquals(aCaisse.getSOLDE().add(aMvt.getMONTANT().negate()), caisseApres.getSOLDE());
        }
        finally {
            if (aCaisse != null) {
                aCaisse.delete(aDBSession);
            }
            if (aModRegl != null) {
                aModRegl.delete(aDBSession);
            }
        }
    }

}
