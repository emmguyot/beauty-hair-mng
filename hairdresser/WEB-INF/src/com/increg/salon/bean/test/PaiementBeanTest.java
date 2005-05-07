package com.increg.salon.bean.test;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Locale;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.PaiementBean;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PaiementBeanTest extends TestCase {

    /**
     * Connexion à la base de donnée  
     */
    private DBSession aDBSession = new DBSession("config");

    /**
     * Constructor for PaiementBeanTest.
     * @param arg0 .
     */
    public PaiementBeanTest(String arg0) {
        super(arg0);
    }

    /**
     * Vérification de la suppression exceptionnelle de paiement
     * Cas sans mouvement de caisse après
     * @throws Exception en cas d'erreur programme
     */
    public void testDeletePur1() throws Exception {

        long numFact = 999999;
        long numPaiement = 999998;
        FactBean aFact = null;
        HistoPrestBean aHisto1 = null; 
        HistoPrestBean aHisto2 = null; 
        PaiementBean aPaiement = null;
        
        try {        
            // Recherche un client au hazard
            String reqCli = "select min(CD_CLI) from CLI";
            ResultSet rs = aDBSession.doRequest(reqCli);
            long cdCli = 0;
            if (rs.next()) { cdCli = rs.getLong(1); }
            rs.close();
            
            // Compte les mouvements avant
            String reqSQL = "select count(*) from MVT_CAISSE";
            rs = aDBSession.doRequest(reqSQL);
            long nbMvt = 0;
            if (rs.next()) { nbMvt = rs.getLong(1); }
            rs.close();
            
            // Date max de mouvements
            String reqSQL2 = "select max(DT_MVT) from MVT_CAISSE";            
            rs = aDBSession.doRequest(reqSQL2);
            Calendar lastDateAvant = Calendar.getInstance();
            if (rs.next()) { 
                java.util.Date dtLast = rs.getTimestamp(1);
                lastDateAvant.setTime(dtLast);
            }
            rs.close();

            aFact = new FactBean();
            aFact.setCD_FACT(numFact);
            aFact.setCD_CLI(cdCli);
            aFact.setCD_COLLAB(1);
            aFact.setCD_TYP_VENT(1);
            aFact.setDT_PREST("08/07/2002", Locale.FRENCH);
            aFact.setFACT_HISTO("N");
            aFact.create(aDBSession);
            
            aHisto1 = new HistoPrestBean();
            aHisto1.setCD_CLI(aFact.getCD_CLI());
            aHisto1.setCD_COLLAB(aFact.getCD_COLLAB());
            aHisto1.setCD_FACT(aFact.getCD_FACT());
            aHisto1.setCD_PREST(1);
            aHisto1.setNUM_LIG_FACT(1);
            aHisto1.setPRX_UNIT_TTC("10.10");
            aHisto1.setQTE("1");
            aHisto1.create(aDBSession);

            aHisto2 = new HistoPrestBean();
            aHisto2.setCD_CLI(aFact.getCD_CLI());
            aHisto2.setCD_COLLAB(aFact.getCD_COLLAB());
            aHisto2.setCD_FACT(aFact.getCD_FACT());
            aHisto2.setCD_PREST(3);
            aHisto2.setNUM_LIG_FACT(3);
            aHisto2.setPRX_UNIT_TTC("15.10");
            aHisto2.setQTE("1");
            aHisto2.create(aDBSession);
            
            aFact.calculTotaux(aDBSession);
            
            // Paiement
            aPaiement = new PaiementBean();
            aPaiement.setCD_MOD_REGL(1);
            aPaiement.setCD_PAIEMENT(numPaiement);
            aPaiement.setDT_PAIEMENT("08/07/2002");
            aPaiement.setPRX_TOT_TTC(aFact.getTotPrest());
            aPaiement.create(aDBSession);
            
            // Effectue les mouvements
            aFact.setCD_PAIEMENT(numPaiement);
            aFact.maj(aDBSession);
            aPaiement.maj(aDBSession);
            
            // Compte les mouvements après
            rs = aDBSession.doRequest(reqSQL);
            long nbMvt2 = 0;
            if (rs.next()) { nbMvt2 = rs.getLong(1); }
            rs.close();

            Assert.assertEquals(nbMvt, nbMvt2 - 1);
            
            // Suppression du paiement
            aFact.setCD_PAIEMENT(0);
            aFact.maj(aDBSession);
            aPaiement.deletePur(aDBSession);
            aPaiement = null;
            
            // Compte les mouvements après
            rs = aDBSession.doRequest(reqSQL);
            if (rs.next()) { nbMvt2 = rs.getLong(1); }
            rs.close();

            Assert.assertEquals(nbMvt, nbMvt2);
            
            // Test sur dernière date de mouvement
            rs = aDBSession.doRequest(reqSQL2);
            Calendar lastDate = Calendar.getInstance();
            if (rs.next()) { 
                java.util.Date dtLast = rs.getTimestamp(1);
                lastDate.setTime(dtLast);
            }
            rs.close();
            
            Assert.assertEquals(lastDate, lastDateAvant);
        } catch (Exception e) {
            System.out.println("testDeletePur1 : " + e.toString());
            throw (e);
        } finally {
            if (aHisto1 != null) { aHisto1.delete(aDBSession); }
            if (aHisto2 != null) { aHisto2.delete(aDBSession); }
            if (aFact != null) { aFact.delete(aDBSession); }
            if (aPaiement != null) { aPaiement.delete(aDBSession); }
        }
        
        
    }

}
