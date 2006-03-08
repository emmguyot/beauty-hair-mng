package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.commun.exception.ReloadNeededException;

/**
 * Passage à la version 2.9 
 * Creation date : 15 févr. 03
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV29 extends UpdateBeanV28 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV29(DBSession dbConnect, ResourceBundle rb) throws Exception {
        super(dbConnect, rb);
    }

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la taille de la colonne cd_ident
        String sql = "select CD_PARAM from PARAM where CD_PARAM=6";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            if (rs.next()) {
                // Tout va bien : Le param est là
                version = "2.9";
            } else {
                super.deduitVersion(dbConnect);
            }
            rs.close();
        }
        catch (SQLException se) {
            // Erreur SQL : Champ inexistant
            // C'est donc une version antérieure
            super.deduitVersion(dbConnect);
        }
    }

    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("2.8")) {
            // Mise à jour de la base pour passer en 2.9
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (nextval('SEQ_PARAM'), 'Largeur d''impression des fiches (cm)', '8.5')",
                "create table DEVISE ("
                    + "CD_DEVISE numeric(2) not null,"
                    + "LIB_COURT_DEVISE varchar(10) not null,"
                    + "LIB_DEVISE varchar(30) not null,"
                    + "RATIO decimal(7,5),"
                    + "Constraint pk_devise Primary Key (CD_DEVISE))",
                "create sequence SEQ_DEVISE",
                "alter table DEVISE alter CD_DEVISE set default nextval('SEQ_DEVISE')",
                "insert into DEVISE (LIB_COURT_DEVISE, LIB_DEVISE, RATIO) values ('&euro;', 'Euros', 1)",
                "insert into DEVISE (LIB_COURT_DEVISE, LIB_DEVISE, RATIO) values ('F', 'Francs', 6.55957)",
                "alter table POINTAGE add constraint FK_POINTAGE_CORRESP_A_COLLAB foreign key (CD_COLLAB) references COLLAB (CD_COLLAB)",
                "create table STAT_HISTO ("
                    + "CD_STAT numeric(3) not null,"                    + "NUM_GRAPH numeric(1) not null,"
                    + "PARAM varchar(20) not null,"
                    + "VALUE varchar(80) not null,"
                    + "DT_CREAT timestamp with time zone NOT NULL,"
                    + "DT_MODIF timestamp with time zone DEFAULT now() NOT NULL,"
                    + "Constraint pk_stat_histo Primary Key (CD_STAT, NUM_GRAPH, PARAM))",
                "alter table STAT_HISTO add constraint FK_HISTO_CONCERNE_STAT foreign key (CD_STAT) references STAT (CD_STAT)",
                "alter table CLI add INDIC_VALID char(1) constraint CKC_INDIC_VALID check (INDIC_VALID is null or INDIC_VALID in ('O', 'N'))",
                "update CLI set INDIC_VALID='O'",
                "alter table COLLAB add INDIC_VALID char(1) constraint CKC_INDIC_VALID check (INDIC_VALID is null or INDIC_VALID in ('O', 'N'))",
                "update COLLAB set INDIC_VALID='O'"
                };
            String sqlAvecRes[] = {
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
            
            // On vient de passer en 2.9
            version = "2.9";
        }
    }
    /**
     * @see com.increg.salon.bean.update.UpdateBean#checkDatabase(DBSession)
     */
    public boolean checkDatabase(DBSession dbConnect) throws ReloadNeededException {

        // Liste des tables qui devraient être présentes, dans l'ordre alphabétique
        String lstTables[] = {
                            "ART",
                            "CAISSE",
                            "CAT_FOURN",
                            "CATEG_ART",
                            "CATEG_CLI",
                            "CATEG_PREST",
                            "CLI",
                            "COLLAB",
                            "DEVISE",
                            "FACT",
                            "FCT",
                            "FETE",
                            "FOURN",
                            "HISTO_PREST",
                            "IDENT",
                            "MARQUE",
                            "MOD_REGL",
                            "MVT_CAISSE",
                            "MVT_STK",
                            "ORIG",
                            "PAIEMENT",
                            "PARAM",
                            "POINTAGE",
                            "PREST",
                            "PROFIL",
                            "SOC",
                            "STAT",
                            "STAT_HISTO",
                            "TR_AGE",
                            "TYP_ART",
                            "TYP_CHEV",
                            "TYP_CONTR",
                            "TYP_MCA",
                            "TYP_MVT",
                            "TYP_POINTAGE",
                            "TYP_VENT",
                            "UNIT_MES"
                            };

        // liste des séquences qui devraient être présentes, dans l'ordre alphabétique
        String lstSequences[] = {
                            "SEQ_ART",
                            "SEQ_CATEG_ART",
                            "SEQ_CATEG_CLI",
                            "SEQ_CATEG_PREST",
                            "SEQ_CLI",
                            "SEQ_COLLAB",
                            "SEQ_DEVISE",
                            "SEQ_FACT",
                            "SEQ_FCT",
                            "SEQ_FETE",
                            "SEQ_FOURN",
                            "SEQ_IDENT",
                            "SEQ_MARQUE",
                            "SEQ_MOD_REGL",
                            "SEQ_ORIG",
                            "SEQ_PAIEMENT",
                            "SEQ_PARAM",
                            "SEQ_PREST",
                            "SEQ_PROFIL",
                            "SEQ_STAT",
                            "SEQ_TR_AGE",
                            "SEQ_TYP_ART",
                            "SEQ_TYP_CHEV",
                            "SEQ_TYP_CONTR",
                            "SEQ_TYP_MCA",
                            "SEQ_TYP_MVT",
                            "SEQ_TYP_POINTAGE",
                            "SEQ_TYP_VENT",
                            "SEQ_UNIT_MES"
                            };
        
        return checkDatabase(dbConnect, lstTables, lstSequences);
    }

}
