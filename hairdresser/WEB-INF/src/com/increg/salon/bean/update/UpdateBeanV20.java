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
 */package com.increg.salon.bean.update;

import com.increg.commun.DBSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Bean de mise à jour de la base à partir d'une version 1.7 vers 2.0
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV20 extends UpdateBeanV17 {

	/**
	 * @see UpdateBeanV17
	 */
	public UpdateBeanV20(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence du responsable dans les identifications
        String sql = "select IMP_CHEQUE from MOD_REGL";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            if (rs.next()) {
                // Tout va bien 
                version = "2.0";
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
        if (version.equals("1.7")) {
            // Mise à jour de la base pour passer en 2.0
            String sql[] = {
                "insert into PROFIL (CD_PROFIL, LIB_PROFIL) values (nextval('seq_profil'), "
            		+ DBSession.quoteWith(messages.getString("label.responsable"), '\'')
            		+ ")",
                "insert into PROFIL (CD_PROFIL, LIB_PROFIL) values (nextval('seq_profil'), "
            		+ DBSession.quoteWith(messages.getString("label.coiffeur"), '\'')
            		+ ")",
                "alter table MOD_REGL add IMP_CHEQUE char(1)",
                "alter table MOD_REGL add RENDU_MONNAIE char(1)",
                "update MOD_REGL set IMP_CHEQUE='N', RENDU_MONNAIE='N'"};
                
            for (int i = 0; i < sql.length; i++) {
                String aSql[] = new String[1];
                aSql[0] = sql[i];
                dbConnect.doExecuteSQL(aSql);
            }
            // On vient de passer en 2.0
            version = "2.0";
        }
    }


}
