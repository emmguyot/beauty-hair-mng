/*
 * Upgrade de la base pour passe en 2.8
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

/**
 * Passage à la version 2.8
 * Creation date : 28 déc. 2002
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV28 extends UpdateBeanV27 {

	/**
	 * Constructor for UpdateBeanVxx.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV28(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la taille de la colonne cd_ident
        String sql = "select INDIC_PERIM from ART where CD_ART=-1";
        try {
            ResultSet res = dbConnect.doRequest(sql);
            // Tout va bien : le champ existe
            version = "2.8";
            res.close();
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
        if (version.equals("2.7")) {
            // Mise à jour de la base pour passer en 2.8
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                "alter table ART add INDIC_PERIM char(1) constraint CKC_INDIC_PERIM check (INDIC_PERIM is null or INDIC_PERIM in ('O', 'N'))",
                "update ART set INDIC_PERIM='N'",
                "alter table PREST add INDIC_PERIM char(1) constraint CKC_INDIC_PERIM2 check (INDIC_PERIM is null or INDIC_PERIM in ('O', 'N'))",
                "update PREST set INDIC_PERIM='N'",
                "insert into FETE (CD_FETE, PRENOM, DT_FETE) values (nextval('SEQ_FETE'), 'Joshua', '01/09/1970')",
                // Reset des catégories effacées
                "update PREST set CD_CATEG_PREST=(select min(CD_CATEG_PREST) from CATEG_PREST) where CD_CATEG_PREST not in (select CD_CATEG_PREST from CATEG_PREST)"
                };
            String sqlAvecRes[] = {
                // Mise à jour de la sequence des paramètres suite à bug précédent
                "select setval ('seq_param', max (CD_PARAM)::bigint, true) from PARAM"
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
            
            // On vient de passer en 2.8
            version = "2.8";
        }
    }
    /**
     * @see com.increg.salon.bean.update.UpdateBean#checkDatabase(DBSession)
     */
    public boolean checkDatabase(DBSession dbConnect) throws ReloadNeededException {

        // Pas de changement depuis la version précédente
        return super.checkDatabase(dbConnect);
    }

}
