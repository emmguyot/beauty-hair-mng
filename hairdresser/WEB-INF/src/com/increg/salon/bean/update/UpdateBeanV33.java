package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;

/**
 * Passage à la version 3.3
 * Creation date : 30 déc. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV33 extends UpdateBeanV31 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV33(DBSession dbConnect) throws Exception {
        super(dbConnect);
    }

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la table de Version
        String sql = "select VERSION from VERSION where 0 = 1";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            
            // Tout va bien : Pas d'erreur
            version = "3.3";
            rs.close();
        } catch (SQLException se) {
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
        if (version.equals("3.1")) {
            // Mise à jour de la base pour passer en 3.3
            // Requète Avant / Après
            String[][] reqStat = {
                };
            String[] sql = {
                "create table VERSION (VERSION varchar(10) not null)",
                "insert into VERSION values ('3.3')",
                "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                + "nextval('seq_stat'), 'Création de clients par origine', "
                + "'select date_trunc(''$PeriodeTemps$'', CLI.DT_CREAT), count(DISTINCT CLI.CD_CLI) from CLI "
                + "where CLI.CD_ORIG = $CD_ORIG$ and CLI.DT_CREAT between ''$DateDebut$'' and ''$DateFin$'' "
                + "group by date_trunc(''$PeriodeTemps$'', CLI.DT_CREAT)',"
                + "'Période', 'Nombre', now(), now())",
                "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                + "nextval('seq_stat'), 'Clients par origine', "
                + "'select LIB_ORIG, count(DISTINCT CLI.CD_CLI) from CLI, ORIG "
                + "where CLI.CD_ORIG = ORIG.CD_ORIG and CLI.DT_CREAT between ''$DateDebut$'' and ''$DateFin$'' group by LIB_ORIG',"
                + "'Origine', 'Nombre', now(), now())"
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

            // On vient de passer en 3.3
            version = "3.3";
        }
    }
}
