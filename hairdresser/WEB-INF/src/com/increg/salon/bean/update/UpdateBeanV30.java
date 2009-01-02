/*
 * Mise à jour de la base de données
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.commun.exception.ReloadNeededException;

/**
 * Passage à la version 3.0 
 * Creation date : 11 févr. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV30 extends UpdateBeanV211 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV30(DBSession dbConnect, ResourceBundle rb) throws Exception {
        super(dbConnect, rb);
    }

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de l'existence de la table RDV
        String sql = "select CD_COLLAB from RDV where 0=1";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            // Tout va bien : Pas d'erreur
            version = "3.0";
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
        if (version.equals("2.11")) {
            // Mise à jour de la base pour passer en 3.0
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                "create table RDV ("
                    + "CD_CLI numeric(5) not null,"
                    + "DT_DEBUT timestamp with time zone not null,"
                    + "CD_COLLAB numeric(3) not null,"
                    + "DUREE interval not null,"
                    + "COMM text,"
                    + "DT_CREAT timestamp with time zone NOT NULL,"
                    + "DT_MODIF timestamp with time zone DEFAULT now() NOT NULL,"
                    + "Constraint pk_rdv Primary Key (DT_DEBUT, CD_CLI))",
                "alter table RDV add constraint FK_ESTPOUR_COLLAB foreign key (CD_COLLAB) references COLLAB (CD_COLLAB)",
                "alter table RDV add constraint FK_ESTPOUR_CLIENT foreign key (CD_CLI) references CLI (CD_CLI)",
                "update FETE set DT_FETE=(date_part('day',DT_FETE) || '/' || date_part('month', DT_FETE) || '/2004')::date",
                "insert into FETE (CD_FETE, PRENOM, DT_FETE) values (nextval('SEQ_FETE'), 'Auguste', '29/02/2004')",
                "create index IDX_ART_TYP_ART on ART (CD_TYP_ART)",
                "create index IDX_ART_CATEG_ART on ART (CD_CATEG_ART)",
                "create index IDX_CAT_FOURN_FOURN on CAT_FOURN (CD_FOURN)",
                "create index IDX_CAT_FOURN_ART on CAT_FOURN (CD_ART)",
                "create index IDX_CLI_NOM on CLI (NOM)",
                "create index IDX_FACT_CLI on FACT (CD_CLI)",
                "create index IDX_FACT_PAIEMENT on FACT (CD_PAIEMENT)",
                "create index IDX_FACT_DT_PREST on FACT (DT_PREST)",
                "create index IDX_HISTO_PREST_CLI on HISTO_PREST (CD_CLI)",
                "create index IDX_HISTO_PREST_COLLAB on HISTO_PREST (CD_COLLAB)",
                "create index IDX_MVT_STK_ART on MVT_STK (CD_ART)",
                "create index IDX_MVT_STK_CMD_FOURN on MVT_STK (CD_CMD_FOURN)",
                "create index IDX_POINTAGE_TYP_POINTAGE on POINTAGE (CD_TYP_POINTAGE)",
                "create index IDX_PREST_TYP_VENT on PREST (CD_TYP_VENT)",
                "create index IDX_PREST_CATEG_PREST on PREST (CD_CATEG_PREST)",
                "create index IDX_PREST_MARQUE on PREST (CD_MARQUE)"
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

            // On vient de passer en 3.0
            version = "3.0";
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
                            "RDV",
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
