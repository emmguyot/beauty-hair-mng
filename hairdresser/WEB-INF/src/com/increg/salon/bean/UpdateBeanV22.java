package com.increg.salon.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;

/**
 * Passage à la version 2.2
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV22 extends UpdateBeanV21 {

	/**
	 * Constructor for UpdateBeanV22.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV22(DBSession dbConnect) throws Exception {
		super(dbConnect);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence du paramètre Affichage du prix en bas de facture
        String sql = "select * from PARAM where CD_PARAM=4";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            if (rs.next()) {
                // Tout va bien 
                version = "2.2";
            } else {
                super.deduitVersion(dbConnect);
            }
            rs.close();
        }
        catch (SQLException se) {
            // Erreur SQL : Colonne introuvable
            // C'est donc une version antérieure
            super.deduitVersion(dbConnect);
        }
    }

    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("2.1")) {
            // Mise à jour de la base pour passer en 2.2
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                "update PARAM set LIB_PARAM='Mot de passe Opérations Exceptionnelles' where CD_PARAM=1",
                "update PARAM set VAL_PARAM=(select MOT_PASSE from IDENT where CD_IDENT=1) where CD_PARAM=1",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (4, 'Affichage prix en bas de facture', 'O')"
                };
            String sqlAvecRes[] = {
                "select setval ('seq_param', 4, true)"
                };
                
            for (int i = 0; i < reqStat.length; i++) {
                String aSql[] = new String[1];
                aSql[0] = "update STAT set REQ_SQL=" + DBSession.quoteWith(reqStat[i][1], '\'') 
                        + " where REQ_SQL=" + DBSession.quoteWith(reqStat[i][0], '\'');
                dbConnect.doExecuteSQL(aSql);
            }
                                    
            for (int i = 0; i < sql.length; i++) {
                String aSql[] = new String[1];
                aSql[0] = sql[i];
                dbConnect.doExecuteSQL(aSql);
            }

            for (int i = 0; i < sqlAvecRes.length; i++) {
                ResultSet rs = dbConnect.doRequest(sqlAvecRes[i]);
                rs.close();
            }
            // On vient de passer en 2.2
            version = "2.2";
        }
    }
}
