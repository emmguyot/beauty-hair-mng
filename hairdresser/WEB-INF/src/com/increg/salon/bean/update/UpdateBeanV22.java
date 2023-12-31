/*
 * Mise � jour de la base de donn�es
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
 */package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;

/**
 * Passage � la version 2.2
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV22 extends UpdateBeanV21 {

	/**
	 * Constructor for UpdateBeanV22.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV22(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // V�rification de la pr�sence du param�tre Affichage du prix en bas de facture
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
            // C'est donc une version ant�rieure
            super.deduitVersion(dbConnect);
        }
    }

    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("2.1")) {
            // Mise � jour de la base pour passer en 2.2
            // Requ�te Avant / Apr�s
            String reqStat[][] = {
                };
            String sql[] = {
                "update PARAM set LIB_PARAM="
            		+ DBSession.quoteWith(messages.getString("label.paramMDPOpExcep"), '\'')
            		+ " where CD_PARAM=1",
                "update PARAM set VAL_PARAM=(select MOT_PASSE from IDENT where CD_IDENT=1) where CD_PARAM=1",
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (4, "
                	+ DBSession.quoteWith(messages.getString("label.paramAffPrixFacture"), '\'') + ","
                	+ "'O')"
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
