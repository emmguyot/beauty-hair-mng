/*
 * Upgrade de la base pour passer en 3.3
 * Copyright (C) 2001-2007 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import com.increg.salon.bean.ParamBean;

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
    public UpdateBeanV33(DBSession dbConnect, ResourceBundle rb) throws Exception {
        super(dbConnect, rb);
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
                + "nextval('seq_stat'), "
        		+ DBSession.quoteWith(messages.getString("label.statCreationClientOrigine"), '\'') + ","
                + "'select date_trunc(''$PeriodeTemps$'', CLI.DT_CREAT), count(DISTINCT CLI.CD_CLI) from CLI "
                + "where CLI.CD_ORIG = $CD_ORIG$ and CLI.DT_CREAT between ''$DateDebut$'' and ''$DateFin$'' "
                + "group by date_trunc(''$PeriodeTemps$'', CLI.DT_CREAT)',"
        		+ DBSession.quoteWith(messages.getString("label.periode"), '\'') + ","
        		+ DBSession.quoteWith(messages.getString("label.nombre"), '\'') + ","
                + "now(), now())",
                "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                + "nextval('seq_stat'), "
        		+ DBSession.quoteWith(messages.getString("label.statClientOrigine"), '\'') + ","
                + "'select LIB_ORIG, count(DISTINCT CLI.CD_CLI) from CLI, ORIG "
                + "where CLI.CD_ORIG = ORIG.CD_ORIG and CLI.DT_CREAT between ''$DateDebut$'' and ''$DateFin$'' group by LIB_ORIG',"
        		+ DBSession.quoteWith(messages.getString("label.origine"), '\'') + ","
        		+ DBSession.quoteWith(messages.getString("label.nombre"), '\'') + ","
                + "now(), now())",
                "alter table SOC add FLG_INSTITUT char(1)",
                "alter table SOC add FLG_SALON char(1)",
                "create table TYP_PEAU ("
                + "      CD_TYP_PEAU     numeric(2)     not null,"
                + "      LIB_TYP_PEAU    varchar(80)    not null,"
                + "         constraint PK_TYP_PEAU primary key (CD_TYP_PEAU) "
                + ")",
                "create sequence SEQ_TYP_PEAU",
                "alter table TYP_PEAU alter CD_TYP_PEAU set default nextval('SEQ_TYP_PEAU')",
                "insert into TYP_PEAU (CD_TYP_PEAU, LIB_TYP_PEAU) select CD_TYP_CHEV, LIB_TYP_CHEV from TYP_CHEV",
                "alter table CLI add CD_TYP_PEAU numeric(2)",
                "alter table CLI add constraint FK_APOUR_PEAU foreign key (CD_TYP_PEAU) references TYP_PEAU (CD_TYP_PEAU)",
                "update CLI set CD_TYP_PEAU = CD_TYP_CHEV",
                "create table TVA ("
                + "     CD_TVA numeric(2) not null,"
                + "     LIB_TVA varchar(80) not null,"
                + "     TX_TVA numeric(5,2) not null,"
                + "     constraint PK_TVA primary key (CD_TVA)"
                + ")",
                "create sequence SEQ_TVA",
                "alter table TVA alter CD_TVA set default nextval('SEQ_TVA')",
                "insert into TVA (LIB_TVA, TX_TVA) select 'TVA Normale', to_number(VAL_PARAM, '99.99') from PARAM where CD_PARAM=" + ParamBean.CD_TVA,
                "alter table TYP_VENT add CD_TVA numeric(2)",
                "update TYP_VENT set CD_TVA = 1",
                "alter table TYP_VENT add constraint FK_APOUR_TVA foreign key (CD_TVA) references TVA (CD_TVA)",
                "delete from PARAM where CD_PARAM=" + ParamBean.CD_TVA,
                "alter table DEVISE drop constraint PK_DEVISE",
                "alter table DEVISE rename to DEVISE_OLD",
                "create table DEVISE ("
                + "CD_DEVISE numeric(2) not null default nextval('SEQ_DEVISE'),"
                + "LIB_COURT_DEVISE varchar(10) not null,"
                + "LIB_DEVISE varchar(30) not null,"
                + "RATIO decimal(9,5),"
                + "Constraint PK_DEVISE Primary Key (CD_DEVISE))",
                "insert into DEVISE (CD_DEVISE, LIB_COURT_DEVISE, LIB_DEVISE, RATIO) select CD_DEVISE, LIB_COURT_DEVISE, LIB_DEVISE, RATIO from DEVISE_OLD",
                "drop table DEVISE_OLD"
                };
            String[] sqlAvecRes = {
                "select setval('seq_typ_peau', max(CD_TYP_PEAU)::bigint, true) from TYP_PEAU"
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

            // Mise à jour des flag société
            if (dbConnect.getBaseName().indexOf("institut") != -1) {
                String[] aSql = new String[1];
                aSql[0] = "update SOC set FLG_INSTITUT='O'";
                dbConnect.doExecuteSQL(aSql);
            } else {
                String[] aSql = new String[1];
                aSql[0] = "update SOC set FLG_INSTITUT='N'";
                dbConnect.doExecuteSQL(aSql);
            }
            if (dbConnect.getBaseName().indexOf("salon") != -1) {
                String[] aSql = new String[1];
                aSql[0] = "update SOC set FLG_SALON='O'";
                dbConnect.doExecuteSQL(aSql);
            } else {
                String[] aSql = new String[1];
                aSql[0] = "update SOC set FLG_SALON='N'";
                dbConnect.doExecuteSQL(aSql);
            }
            // On vient de passer en 3.3
            version = "3.3";
        }
    }
    /**
     * @see com.increg.salon.bean.update.UpdateBean#checkDatabase(DBSession)
     */
    public boolean checkDatabase(DBSession dbConnect) throws ReloadNeededException {

        // Liste des tables qui devraient être présentes, dans l'ordre alphabétique
        String[] lstTables = {
                            "ABO_CLI",
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
                            "TVA",
                            "TYP_ART",
                            "TYP_CHEV",
                            "TYP_CONTR",
                            "TYP_MCA",
                            "TYP_MVT",
                            "TYP_PEAU",
                            "TYP_POINTAGE",
                            "TYP_VENT",
                            "UNIT_MES",
                            "VERSION"
                            };

        // liste des séquences qui devraient être présentes, dans l'ordre alphabétique
        String[] lstSequences = {
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
                            "SEQ_TVA",
                            "SEQ_TYP_ART",
                            "SEQ_TYP_CHEV",
                            "SEQ_TYP_CONTR",
                            "SEQ_TYP_MCA",
                            "SEQ_TYP_MVT",
                            "SEQ_TYP_PEAU",
                            "SEQ_TYP_POINTAGE",
                            "SEQ_TYP_VENT",
                            "SEQ_UNIT_MES"
                            };
        
        return checkDatabase(dbConnect, lstTables, lstSequences);
    }
}
