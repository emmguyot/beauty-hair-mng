/*
 * Mise à jour de la base de données
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
 */package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;

/**
 * Passage à la version 2.4
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV24 extends UpdateBeanV22 {

	/**
	 * Constructor for UpdateBeanV22.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV24(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la contrainte des mouvements vers la facture
        String sql = "select * from pg_trigger where tgconstrname='fk_concerne_fact'";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            if (rs.next()) {
                super.deduitVersion(dbConnect);
            } else {
                // Tout va bien : Elle n'y est plus
                version = "2.4";
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
        if (version.equals("2.2")) {
            // Mise à jour de la base pour passer en 2.4
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
//                "alter table MVT_STK drop constraint fk_concerne_fact"
                  "alter table MVT_STK rename to MVT_STK_OLD",
                  "CREATE TABLE MVT_STK ( " +
                        "CD_ART numeric(5,0) NOT NULL," +
                        "DT_MVT timestamp with time zone DEFAULT now() NOT NULL," +
                        "CD_FACT numeric(6,0)," +
                        "CD_TYP_MVT numeric(2,0) NOT NULL," +
                        "QTE numeric(5,2) NOT NULL," +
                        "VAL_MVT_HT numeric(8,2) NOT NULL," +
                        "STK_AVANT numeric(5,2) NOT NULL," +
                        "COMM text," +
                        "DT_CREAT timestamp with time zone NOT NULL," +
                        "DT_MODIF timestamp with time zone DEFAULT now() NOT NULL," +
                        "VAL_STK_AVANT numeric(8,2) NOT NULL" +
                        ")",
                  "insert into MVT_STK (CD_ART, DT_MVT, CD_FACT, CD_TYP_MVT, QTE, VAL_MVT_HT, STK_AVANT, COMM," +
                        "DT_CREAT, DT_MODIF, VAL_STK_AVANT) select CD_ART, DT_MVT, CD_FACT, CD_TYP_MVT, QTE, VAL_MVT_HT, STK_AVANT, COMM," +
                        "DT_CREAT, DT_MODIF, VAL_STK_AVANT from MVT_STK_OLD",
                  "drop table MVT_STK_OLD",
                  "CREATE UNIQUE INDEX UK_MVT_STK on MVT_STK (CD_ART, DT_MVT, CD_FACT)",
                  "alter table MVT_STK add constraint FK_CONCERNE_ART foreign key (CD_ART) references ART (CD_ART)",
                  "alter table MVT_STK add constraint FK_APOUR_TYP_MVT foreign key (CD_TYP_MVT) references TYP_MVT (CD_TYP_MVT)"
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
            // On vient de passer en 2.4
            version = "2.4";
        }
    }
}
