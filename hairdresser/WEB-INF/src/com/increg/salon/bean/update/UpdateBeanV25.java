/*
 * Upgrade de la base pour passer en 2.5
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;

/**
 * Passage à la version 2.5
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV25 extends UpdateBeanV24 {

	/**
	 * Constructor for UpdateBeanV24.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV25(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la contrainte des mouvements vers la facture
        String sql = "select * from PARAM where CD_PARAM=" + Integer.toString(ParamBean.CD_REPART_REMISE);
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            if (rs.next()) {
                // Tout va bien : Le param est là
                version = "2.5";
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
        if (version.equals("2.4")) {
            // Mise à jour de la base pour passer en 2.5
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                "insert into PARAM (CD_PARAM, LIB_PARAM, VAL_PARAM) values (" + Integer.toString(ParamBean.CD_REPART_REMISE)
                    + ", "
            		+ DBSession.quoteWith(messages.getString("label.paramRepartitionRemise"), '\'') + ","
                    + "'P')"
                };
            String sqlAvecRes[] = {
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
            
            /**
             * Mise à jour pour nouvelle plate-forme
             */
            String tomcatHome = System.getProperty("tomcat.home");
            if (tomcatHome == null) {
                tomcatHome = System.getProperty("catalina.home");
            }
            if (tomcatHome == null) {
                // Pb
                tomcatHome = System.getProperty("java.home");
            }
            if (tomcatHome == null) {
                tomcatHome = System.getProperty("increg");
            }
            if (tomcatHome == null) {
                tomcatHome = ".";
            }
            
            // Mise à jour de la configuration Apache pour désactiver GZIP
            File fichierHttpd = new File(tomcatHome + "/../Apache/conf/httpd.conf");
            // Sécurité pour nouveau socle et ancienne sauvegarde
            if (fichierHttpd.exists()) {
	            File fichierHttpdNew = new File(tomcatHome + "/../Apache/conf/httpd.conf.new");
	            BufferedReader brHttpd = new BufferedReader(new FileReader(fichierHttpd));
	            BufferedWriter bwHttpd = new BufferedWriter(new FileWriter(fichierHttpdNew));
	            String ligne = null;
	            
	            while ((ligne = brHttpd.readLine()) != null) {
	                if (ligne.indexOf("mod_gzip_on") != -1) {
	                    ligne = "mod_gzip_on No";
	                }
	                bwHttpd.write(ligne + "\r\n");
	            }
	            
	            bwHttpd.close();
	            brHttpd.close();
	            
	            fichierHttpd.delete();
	            fichierHttpdNew.renameTo(fichierHttpd);
            }
            
            // On vient de passer en 2.5
            version = "2.5";
        }
    }
}
