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
import com.increg.commun.exception.BadPlatformException;
import com.increg.commun.exception.ReloadNeededException;

/**
 * Passage à la version 4.1
 * Creation date : 10 juil. 08 00:08:20
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV41 extends UpdateBeanV40 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV41(DBSession dbConnect, ResourceBundle rb) throws Exception {
        super(dbConnect, rb);
    }

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la table de Version
        String sql = "select VERSION.VERSION from VERSION";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            
            if (rs.next()) {
            	version = rs.getString("VERSION");
            	
            	// Vérification de la version du socle via la version de la base
            	sql = "select version()";
                ResultSet rsPostgre = dbConnect.doRequest(sql);
                if (rsPostgre.next()) {
                	if (rsPostgre.getString(1).compareToIgnoreCase("postgresql 8.2") < 0) {
                    	throw new BadPlatformException();
                	}
                }
                else {
                	throw new BadPlatformException();
                }
                rsPostgre.close();
            }
            else {
                super.deduitVersion(dbConnect);
            }
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
        if (version.equals("4.0")) {
            // Mise à jour de la base pour passer en 4.1
            // Requète Avant / Après
            String[][] reqStat = {
                };
            String[] sql = {
                "update VERSION set VERSION='4.1'",
                "alter table pointage alter column dt_debut type timestamp without time zone",
                "alter table pointage alter column dt_fin type timestamp without time zone",
                "alter table mvt_caisse alter column dt_mvt type timestamp without time zone",
                "alter table mvt_stk alter column dt_mvt type timestamp without time zone",
                "alter table rdv alter column dt_debut type timestamp without time zone"
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

            // On vient de passer en 4.1
            version = "4.1";
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
