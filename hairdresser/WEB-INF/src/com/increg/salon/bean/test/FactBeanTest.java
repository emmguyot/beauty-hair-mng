/*
 * 
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
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.PrestBean;
import com.increg.salon.bean.TvaBean;
import com.increg.salon.bean.TypVentBean;
import com.increg.salon.request.TVA;

import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FactBeanTest extends TestCase {

    /**
      *  Connexion à la base de donnée  
      */
    private DBSession aDBSession = new DBSession("config");
    /**
     * Messages localisés
     */
	private ResourceBundle msg = ResourceBundle.getBundle("messages");

    /**
     * Code client à utiliser
     */
    private static final int CD_CLI = 195;
    /**
     * Code collab à utiliser
     */
    private static final int CD_COLLAB = 1;

    /**
     * Prestation de type Vente
     */
    private static final int CD_PREST1 = 75;
    /**
     * Prestation de type Salon homme
     */ 
    private static final int CD_PREST2 = 29;
    /**
     * Prestation de type Salon Dame
     */
    private static final int CD_PREST3 = 65;
    
    /**
     * Constructor for FactBeanTest.
     * @param arg0 .
     */
    public FactBeanTest(String arg0) {
        super(arg0);
    }

    /**
     * Vérification de la répartition de la TVA (unitaire)
     * @throws Exception .
     */
    public void testCalculTVARepartie1() throws Exception {
        
        FactBean aFact = new FactBean(msg);
        PaiementBean aPaiement = new PaiementBean(msg);
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            Calendar dateJUnit = Calendar.getInstance();
            dateJUnit.set(Calendar.YEAR, 1970);
        
            /**
             * Création d'une facture
             */
            aFact.setCD_CLI(CD_CLI);
            aFact.setCD_COLLAB(CD_COLLAB);
            aFact.setCD_TYP_VENT(1);
            aFact.setDT_PREST(df.format(dateJUnit.getTime()), Locale.getDefault());
            aFact.setFACT_HISTO("N");
            aFact.create(aDBSession);

            Vector lignes = new Vector();
            HistoPrestBean aLigne = new HistoPrestBean();
            aFact.setLignes(lignes);
            /**
             * Ligne 1
             */
            PrestBean aPrest = PrestBean.getPrestBean(aDBSession, Integer.toString(CD_PREST1));
            aLigne.setCD_FACT(aFact.getCD_FACT());
            aLigne.setCD_CLI(CD_CLI);
            aLigne.setCD_COLLAB(CD_COLLAB);
            aLigne.setCD_PREST(aPrest.getCD_PREST());
            aLigne.setDT_PREST(aFact.getDT_PREST());
            aLigne.setNUM_LIG_FACT(1);
            aLigne.setPRX_UNIT_TTC(aPrest.getPRX_UNIT_TTC());
            aLigne.setQTE("1.0");
            aLigne.create(aDBSession);
            lignes.add(aLigne);
            /**
             * Ligne 2
             */
            aLigne = new HistoPrestBean();
            aPrest = PrestBean.getPrestBean(aDBSession, Integer.toString(CD_PREST2));
            aLigne.setCD_FACT(aFact.getCD_FACT());
            aLigne.setCD_CLI(CD_CLI);
            aLigne.setCD_COLLAB(CD_COLLAB);
            aLigne.setCD_PREST(aPrest.getCD_PREST());
            aLigne.setDT_PREST(aFact.getDT_PREST());
            aLigne.setNUM_LIG_FACT(2);
            aLigne.setPRX_UNIT_TTC(aPrest.getPRX_UNIT_TTC());
            aLigne.setQTE("1.0");
            aLigne.create(aDBSession);
            lignes.add(aLigne);
            /**
             * Ligne 3
             */
            aLigne = new HistoPrestBean();
            aPrest = PrestBean.getPrestBean(aDBSession, Integer.toString(CD_PREST3));
            aLigne.setCD_FACT(aFact.getCD_FACT());
            aLigne.setCD_CLI(CD_CLI);
            aLigne.setCD_COLLAB(CD_COLLAB);
            aLigne.setCD_PREST(aPrest.getCD_PREST());
            aLigne.setDT_PREST(aFact.getDT_PREST());
            aLigne.setNUM_LIG_FACT(3);
            aLigne.setPRX_UNIT_TTC(aPrest.getPRX_UNIT_TTC());
            aLigne.setQTE("1.0");
            lignes.add(aLigne);
            aLigne.create(aDBSession);
        
            aPaiement.setCD_MOD_REGL(1);
            aPaiement.setDT_PAIEMENT(aFact.getDT_PREST());

            aPaiement.create(aDBSession);
            
            aFact.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
            aFact.calculTotaux(aDBSession);

            // Vérification par rapport au jour en cours
            /**
             * Hypothèse : Pas d'autres facture pour ce jour
             */        
            Vector listeTVA = FactBean.calculTVARepartie(aDBSession, df.format(dateJUnit.getTime()), df.format(dateJUnit.getTime()));
        
            // Vérifications....
            assertNotNull(listeTVA);
            assertEquals(3, listeTVA.size());
    
            // Vérification des chiffres
        
            String sql = "select TVA, prx_tot_ht, prx_tot_ttc from FACT where CD_FACT=" + aFact.getCD_FACT();
            ResultSet rs = aDBSession.doRequest(sql);
            assertTrue(rs.next());
            BigDecimal totTva = rs.getBigDecimal("TVA", 2);
            BigDecimal totHt = rs.getBigDecimal("PRX_TOT_HT", 2);
            BigDecimal totTtc = rs.getBigDecimal("PRX_TOT_TTC", 2);
        
            for (Iterator tvaIter = listeTVA.iterator(); tvaIter.hasNext();) {
                TVA aTVA = (TVA) tvaIter.next();
                totTva = totTva.subtract(aTVA.getTotal());
                totHt = totHt.subtract(aTVA.getTotalHT());
                totTtc = totTtc.subtract(aTVA.getTotalTTC());
            }
            // Comparaison au dixième de centime pour tenir compte des imprecisions
            assertTrue(almostEquals(new BigDecimal(0), totTva));
            assertTrue(almostEquals(new BigDecimal(0), totTtc));
            assertTrue(almostEquals(new BigDecimal(0), totHt));
        
            TypVentBean aTypVent = TypVentBean.getTypVentBean(aDBSession, Integer.toString(aPrest.getCD_TYP_VENT()));
            BigDecimal txTva = TvaBean.getTvaBean(aDBSession, Integer.toString(aTypVent.getCD_TVA())).getTX_TVA();
            for (Iterator tvaIter = listeTVA.iterator(); tvaIter.hasNext();) {
                TVA aTVA = (TVA) tvaIter.next();
                BigDecimal tva = aTVA.getTotal();
                BigDecimal ht = aTVA.getTotalHT();
                BigDecimal ttc = aTVA.getTotalTTC();
                assertTrue(almostEquals(ht.add(tva), ttc));
                assertTrue(almostEquals(ht.multiply(txTva.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP)), tva));
            }
        } finally {
        	// Attente pour éviter les duplicate keys
        	Thread.sleep(1000);
            aFact.delete(aDBSession);
            aPaiement.delete(aDBSession);
        }
    }

    /**
     * Vérification de la répartition de la TVA (globale)
     * TODO : Modification afin de tenir compte de la répartition si plusieurs taux sont utilisés
     * @throws Exception .
     */
    public void testCalculTVARepartie2() throws Exception {

        Vector listeTVA = FactBean.calculTVARepartie(aDBSession, "", "");
        
        // Vérifications....
        assertNotNull(listeTVA);
        assertTrue(listeTVA.size() > 0);

        boolean recalcul = false;
        for (Iterator tvaIter = listeTVA.iterator(); tvaIter.hasNext();) {
            TVA aTVA = (TVA) tvaIter.next();
            recalcul |= (aTVA.getTotalHT() == null);
            recalcul |= (aTVA.getTotal() == null);
        }
        
        if (recalcul) {
	    	// Recalcule les TVA (pour les vieilles bases)
	        String sqlTVA = "select * from FACT where FACT_HISTO = 'N' and PRX_TOT_TTC <> 0";
	        ResultSet rs = aDBSession.doRequest(sqlTVA);
	        
	        while (rs.next()) {
	            FactBean aFact = new FactBean(rs, msg);
	            
	            aFact.calculTotaux(aDBSession);
	        }
	        rs.close();
        }
        
        listeTVA = FactBean.calculTVARepartie(aDBSession, "", "");
        
        // Vérifications....
        assertNotNull(listeTVA);
        assertTrue(listeTVA.size() > 0);

        // Vérification des chiffres
        String sql = "select sum(TVA) as tva, sum(prx_tot_ht) as ht, sum(prx_tot_ttc) as ttc from FACT where CD_PAIEMENT is not null";
        ResultSet rs = aDBSession.doRequest(sql);
        assertTrue(rs.next());
        BigDecimal totTva = rs.getBigDecimal("tva", 2);
        BigDecimal totHt = rs.getBigDecimal("ht", 2);
        BigDecimal totTtc = rs.getBigDecimal("ttc", 2);
    
        for (Iterator tvaIter = listeTVA.iterator(); tvaIter.hasNext();) {
            TVA aTVA = (TVA) tvaIter.next();
            totTva = totTva.subtract(aTVA.getTotal());
            totHt = totHt.subtract(aTVA.getTotalHT());
            totTtc = totTtc.subtract(aTVA.getTotalTTC());
        }
        assertTrue(almostEquals(new BigDecimal(0), totTva));
        assertTrue(almostEquals(new BigDecimal(0), totTtc));
        assertTrue(almostEquals(new BigDecimal(0), totHt));
    
        for (Iterator tvaIter = listeTVA.iterator(); tvaIter.hasNext();) {
            TVA aTVA = (TVA) tvaIter.next();
            BigDecimal tva = aTVA.getTotal();
            BigDecimal ht = aTVA.getTotalHT();
            BigDecimal ttc = aTVA.getTotalTTC();
            assertTrue(almostEquals(ht.add(tva), ttc));
            // Le test suivant n'est plus vrai : Du fait des arrondis, les écarts peuvent être importants
            //assertTrue(almostEquals(ht.multiply(txTva.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP)), tva));
        }
    }

    /**
     * Comparaison de 2 montants au dixième de centime pret 
     * @param n1 premier chiffre
     * @param n2 second chiffre
     * @return True si égalité
     */
    static boolean almostEquals(BigDecimal n1, BigDecimal n2) {
        return (n1.subtract(n2).setScale(1, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(0)) == 0);
    }
}
