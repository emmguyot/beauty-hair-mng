package com.increg.salon.app;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ModReglBean;
import com.increg.salon.bean.MvtCaisseBean;

/**
 * @author Manu
 *
 * Test des mouvements de caisse
 * 
 */
public class MvtCaisseFix {

    /**
      *  Connexion à la base de donnée  
      */
    private DBSession aDBSession = new DBSession();


    /**
     * Constructor for MvtCaisseBeanTest.
     */
    public MvtCaisseFix() {
    }

    public static void main (java.lang.String[] args) {
        MvtCaisseFix aMvtCaisseFix = new MvtCaisseFix();
        try {
            aMvtCaisseFix.testCheckAndFix();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Test de la vérification d'une caisse
     * @throws Exception En cas d'erreur programme
     */
    public void testCheckAndFix() throws Exception {
        
        // Par défaut une caisse est bonne
        MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_ESP), "01/01/2001 10:10:10");
        MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_CHQ), "01/01/2001 10:10:10");
        MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_CB), "01/01/2001 10:10:10");
        
    }

}
