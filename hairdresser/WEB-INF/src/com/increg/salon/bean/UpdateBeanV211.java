package com.increg.salon.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;
import com.increg.commun.exception.ReloadNeededException;

/**
 * Passage à la version 2.11 
 * Creation date : 27 oct. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV211 extends UpdateBeanV210 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV211(DBSession dbConnect) throws Exception {
        super(dbConnect);
    }

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de l'existence de la table CRITERE_PUB
        String sql = "select CD_CRITERE_PUB from CRITERE_PUB where 0=1";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            // Tout va bien : Pas d'erreur
            version = "2.11";
            rs.close();
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
        if (version.equals("2.10")) {
            // Mise à jour de la base pour passer en 2.11
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                "create sequence SEQ_CRITERE_PUB",
                "create table CRITERE_PUB ("
                    + "CD_CRITERE_PUB numeric(3) not null default nextval('SEQ_CRITERE_PUB'),"                    + "LIB_CRITERE_PUB varchar(80) not null,"
                    + "CLAUSE text not null,"
                    + "DT_CREAT timestamp with time zone NOT NULL,"
                    + "DT_MODIF timestamp with time zone DEFAULT now() NOT NULL,"
                    + "Constraint pk_critere_pub Primary Key (CD_CRITERE_PUB))",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Nouveaux clients',"                    + "'from CLI where DT_CREAT > ''$DateDebut$''', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Clients d''une ville',"
                    + "'from CLI where VILLE=''$VILLE$''', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Clients en fonction de leur genre',"
                    + "'from CLI where case when CIVILITE = ''M.'' then ''M'' when CIVILITE in (''Mle'', ''Mme'') then ''F'' else null end = ''$Genre$''', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Anniversaire dans les n prochains jours',"
                    + "'from CLI where (date_part(''day'', DT_ANNIV) || ''/'' || date_part(''month'', DT_ANNIV) || ''/'' || date_part(''year'', now()))::date between now() and now() + interval ''$Nombre$ days''', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Clients dans la tranche d''âge',"
                    + "'from CLI where CD_TR_AGE = $CD_TR_AGE$', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Clients dans la catégorie de prestations',"
                    + "'from CLI where CD_CLI in (select CD_CLI from HISTO_PREST,PREST where HISTO_PREST.CD_PREST = PREST.CD_PREST and CD_CATEG_PREST = $CD_CATEG_PREST$ and DT_PREST > ''$DateDebut$'')', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Clients ayant acheté un produit',"
                    + "'from CLI where CD_CLI in (select CD_CLI from HISTO_PREST,PREST where HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_ART = $CD_ART$ and DT_PREST > ''$DateDebut$'')', now())",
                "insert into CRITERE_PUB (LIB_CRITERE_PUB, CLAUSE, dt_creat) values ('Clients inactifs depuis le...',"
                    + "'from CLI where CD_CLI not in (select distinct CD_CLI from FACT where DT_PREST > ''$DateDebut$'')', now())",
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

            // On vient de passer en 2.11
            version = "2.11";
        }
    }
    /**
     * @see com.increg.salon.bean.UpdateBean#checkDatabase(DBSession)
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
                            "CRITERE_PUB",
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
                            "SEQ_CRITERE_PUB",
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
