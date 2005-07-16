package com.increg.salon.bean.update;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;

/**
 * Passage à la version 3.1
 * Creation date : 18 sept. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV31 extends UpdateBeanV30 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV31(DBSession dbConnect, ResourceBundle rb) throws Exception {
        super(dbConnect, rb);
    }

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la colonne CPT_ABONNEMENT
        String sql = "select CPT_ABONNEMENT from PREST where 0 = 1";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            
            // Tout va bien : Pas d'erreur
            version = "3.1";
            rs.close();

            // TODO A supprimer
            /**
             * Mise à jour des TVA des toutes les factures déjà saisies
             */
//            String sqlTVA = "select * from FACT where FACT_HISTO = 'N' and PRX_TOT_TTC <> 0";
//            rs = dbConnect.doRequest(sqlTVA);
//            
//            while (rs.next()) {
//                FactBean aFact = new FactBean(rs);
//                
//                BigDecimal txTVA = aFact.getTVA().multiply(new BigDecimal(100)).divide(aFact.getPRX_TOT_HT(), 20, BigDecimal.ROUND_HALF_UP);
//                aFact.calculTotaux(dbConnect, txTVA);
//            }
//            rs.close();
            
        }
        catch (SQLException se) {
            // Erreur SQL : table inexistante
            // C'est donc une version antérieure
            super.deduitVersion(dbConnect);
        }
    }

    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("3.0")) {
            // Mise à jour de la base pour passer en 3.1
            // Requète Avant / Après
            String[][] reqStat = {
                };
            String[] sql = {
                "alter table PREST add CPT_ABONNEMENT numeric(4,0)",
                "alter table PREST add CD_PREST_ABONNEMENT numeric(5, 0)",
                "alter table PREST add INDIC_ABONNEMENT char",
                "update PREST set CPT_ABONNEMENT = 0, CD_PREST_ABONNEMENT = null",
                "drop index IDX_FACT_DT_PREST",
                "drop index IDX_FACT_PAIEMENT",
                "drop index IDX_HISTO_PREST_COLLAB",
                "drop index IDX_PREST_CATEG_PREST",
                "drop index IDX_PREST_MARQUE",
                "create table ABO_CLI (CD_CLI numeric(5,0) not null, CD_PREST numeric(5,0), CPT numeric(4,0) not null)",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'Disque amovible (ex. : A:/)', 'A:/')",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'URL des mises à jour du logiciel', 'http://www.increg.com/MiseAJour')",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'URL de lecture des sauvegardes', 'http://www.increg.com/Sauvegardes/')",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'URL de suppression des sauvegardes', 'http://www.increg.com/servlet/sauvegardeSup')",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'URL de creation de sauvegardes', 'http://www.increg.com/servlet/sauvegarde')",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'URL des messages d''info', 'http://beauty-hair-mng.sourceforge.net/')",
                "alter table HISTO_PREST add TVA numeric(8,2)",
                "alter table HISTO_PREST add PRX_TOT_TTC numeric(8,2)",
                "alter table HISTO_PREST add PRX_TOT_HT numeric(8,2)",
                "update HISTO_PREST set TVA = 0 from FACT where FACT.CD_FACT = HISTO_PREST.CD_FACT and FACT.PRX_TOT_TTC = 0"
                };
            String[] sqlAvecRes = {
                };

            for (int i = 0; i < reqStat.length; i++) {
                String[] aSql = new String[1];
                aSql[0] = "update STAT set REQ_SQL=" + DBSession.quoteWith(reqStat[i][1], '\'') 
                        + " where REQ_SQL=" + DBSession.quoteWith(reqStat[i][0], '\'');
                dbConnect.doExecuteSQL(aSql);
            }
                                    
            for (int i = 0; i < sql.length; i++) {
                String[] aSql = new String[1];
                aSql[0] = sql[i];
                dbConnect.doExecuteSQL(aSql);
            }

            for (int i = 0; i < sqlAvecRes.length; i++) {
                ResultSet rs = dbConnect.doRequest(sqlAvecRes[i]);
                rs.close();
            }

            /**
             * Mise à jour des TVA des toutes les factures déjà saisies
             */
            String sqlTVA = "select * from FACT where FACT_HISTO = 'N' and PRX_TOT_TTC <> 0";
            ResultSet rs = dbConnect.doRequest(sqlTVA);
            
            while (rs.next()) {
                FactBean aFact = new FactBean(rs, messages);
                
                BigDecimal txTVA = aFact.getTVA().multiply(new BigDecimal(100)).divide(aFact.getPRX_TOT_HT(), 20, BigDecimal.ROUND_HALF_UP);
                aFact.calculTotaux(dbConnect, txTVA);
            }
            rs.close();
            
            // On vient de passer en 3.1
            version = "3.1";
        }
    }
}
