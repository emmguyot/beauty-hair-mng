/*
 * Mise à jour de la base de données
 * Copyright (C) 2001-2017 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;

/**
 * Passage à la version 4.9
 * Creation date : 25/06/2017
 * @author Emmanuel GUYOT <See emmguyot on SourceForge> 
 */
public class UpdateBeanV49 extends UpdateBeanV48 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV49(DBSession dbConnect, ResourceBundle rb, boolean forceLesSequences) throws Exception {
        super(dbConnect, rb, forceLesSequences);
    }
    
    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("4.8")) {
            // Mise à jour de la base pour passer en 4.9
            // Reset des trigger pour passer en FK
        	String[] tableFK = {
        		};
        	// Requête Avant / Après
            String[][] reqStat = {
                };
            String[] sql = {
            	"alter table CLI alter cd_postal type varchar(6)",
            	"update VERSION set VERSION='4.9'"
            	};
            String[] sqlAvecRes = {
                "select setval('seq_param', max(CD_PARAM)::bigint, true) from PARAM"
            	};

            for (int i = 0; i < tableFK.length; i++) {
                String[] aSql = new String[1];

                // Suppression des FK classiques
                ResultSet rs = dbConnect.doRequest("select conname, table1.relname, table2.relname from pg_constraint, pg_class table1, pg_class table2 " +
                		"where pg_constraint.conrelid=table1.oid and pg_constraint.confrelid=table2.oid " +
                		"and (table1.relname='" + tableFK[i] + "' or table2.relname='" + tableFK[i] + "') " +
                		"and contype='f'");
        		while (rs.next()) {
        			aSql[0] = "alter table " + rs.getString(2) + " drop constraint " + rs.getString(1);
        			dbConnect.doExecuteSQL(aSql);
        		}
                rs.close();

                // Suppression des FK via des triggers (issue des vieilles versions de PostgreSQL)
                rs = dbConnect.doRequest("select tgname, relname from pg_trigger, pg_class where pg_trigger.tgrelid=pg_class.oid and relname='" + tableFK[i] + "'");
        		while (rs.next()) {
        			aSql[0] = "drop trigger \"" + rs.getString(1) + "\" on " + rs.getString(2);
        			dbConnect.doExecuteSQL(aSql);
        		}
                rs.close();
                rs = dbConnect.doRequest("select tgname, tablePrincipale.relname from pg_trigger, pg_class tablePrincipale, pg_class tableSecondaire " +
                		"where pg_trigger.tgrelid=tablePrincipale.oid  and pg_trigger.tgconstrrelid=tableSecondaire.oid and tableSecondaire.relname='" + tableFK[i] + "'");
        		while (rs.next()) {
        			aSql[0] = "drop trigger \"" + rs.getString(1) + "\" on " + rs.getString(2);
        			dbConnect.doExecuteSQL(aSql);
        		}
                rs.close();
                
            }
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

            // On vient de passer en 4.9
            version = "4.9";
        }
    }
}
