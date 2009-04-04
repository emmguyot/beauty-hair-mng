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
 */
package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;
import com.increg.commun.exception.ReloadNeededException;
import com.increg.salon.bean.IdentBean;

/**
 * Passage � la version 4.2
 * Creation date : 30 d�c. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV43 extends UpdateBeanV42 {

    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV43(DBSession dbConnect, ResourceBundle rb, boolean forceLesSequences) throws Exception {
        super(dbConnect, rb, forceLesSequences);
    }

    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("4.2")) {
            // Mise � jour de la base pour passer en 4.3
            // Reset des trigger pour passer en FK
        	String[] tableFK = {
        		};
        	// Requ�te Avant / Apr�s
            String[][] reqStat = {
                {
                    "select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(FACT.PRX_TOT_TTC) from FACT, PAIEMENT where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                    "select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(FACT.PRX_TOT_TTC) from FACT, PAIEMENT where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, CLI where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_CLI = CLI.CD_CLI and CLI.CD_CATEG_CLI = $CD_CATEG_CLI$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, CLI where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_CLI = CLI.CD_CLI and CLI.CD_CATEG_CLI = $CD_CATEG_CLI$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and DT_PAIEMENT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', FACT.DT_PREST), count(DISTINCT FACT.CD_CLI) from FACT, PAIEMENT where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', FACT.DT_PREST)",
                	"select extract($PeriodeTemps$ from FACT.DT_PREST), count(DISTINCT FACT.CD_CLI) from FACT, PAIEMENT where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from FACT.DT_PREST)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', FACT.DT_PREST), count(DISTINCT FACT.CD_CLI) from CLI, FACT, PAIEMENT where FACT.CD_CLI = CLI.CD_CLI and FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and (('$Genre$' = 'M' and CIVILITE='M.') or ('$Genre$' = 'F' and CIVILITE in ('Mle', 'Mme'))) and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', FACT.DT_PREST)",
                	"select extract($PeriodeTemps$ from FACT.DT_PREST), count(DISTINCT FACT.CD_CLI) from CLI, FACT, PAIEMENT where FACT.CD_CLI = CLI.CD_CLI and FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and (('$Genre$' = 'M' and CIVILITE='M.') or ('$Genre$' = 'F' and CIVILITE in ('Mle', 'Mme'))) and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from FACT.DT_PREST)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_MVT), sum(MVT_STK.VAL_MVT_HT*MVT_STK.QTE) from MVT_STK, ART where MVT_STK.CD_ART = ART.CD_ART and MVT_STK.CD_TYP_MVT = $CD_TYP_MVT$ and ART.CD_CATEG_ART = $CD_CATEG_ART$ and MVT_STK.DT_MVT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_MVT)",
                	"select extract($PeriodeTemps$ from DT_MVT), sum(MVT_STK.VAL_MVT_HT*MVT_STK.QTE) from MVT_STK, ART where MVT_STK.CD_ART = ART.CD_ART and MVT_STK.CD_TYP_MVT = $CD_TYP_MVT$ and ART.CD_CATEG_ART = $CD_CATEG_ART$ and MVT_STK.DT_MVT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_MVT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', CLI.DT_CREAT), count(DISTINCT CLI.CD_CLI) from CLI where CLI.CD_ORIG = $CD_ORIG$ and CLI.DT_CREAT between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', CLI.DT_CREAT)",
                	"select extract($PeriodeTemps$ from CLI.DT_CREAT), count(DISTINCT CLI.CD_CLI) from CLI where CLI.CD_ORIG = $CD_ORIG$ and CLI.DT_CREAT between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from CLI.DT_CREAT)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', FACT.DT_PREST), sum(HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', FACT.DT_PREST)",
                	"select extract($PeriodeTemps$ from FACT.DT_PREST), sum(HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from FACT.DT_PREST)"
                },
                {
                	"select date_trunc('$PeriodeTemps$', DT_PAIEMENT), sum(HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST, COLLAB where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = COLLAB.CD_COLLAB and COLLAB.CD_COLLAB = $CD_COLLAB$ and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('$PeriodeTemps$', DT_PAIEMENT)",
                	"select extract($PeriodeTemps$ from DT_PAIEMENT), sum(HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST, COLLAB where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = COLLAB.CD_COLLAB and COLLAB.CD_COLLAB = $CD_COLLAB$ and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by extract($PeriodeTemps$ from DT_PAIEMENT)"
                }
                };
            String[] sql = {
            	"update VERSION set VERSION='4.3'"};
            String[] sqlAvecRes = {
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

            // On vient de passer en 4.3
            version = "4.3";
        }
    }

    /**
     * @see com.increg.salon.bean.update.UpdateBean#checkDatabase(DBSession, String[], String[])
     */
    public boolean checkDatabase(DBSession dbConnect, String[] lstTables, String[] lstSequences) throws ReloadNeededException {

        // Valeur retourn�e
        boolean etat = true;
                
        try {        

            /**
             * <b>Partie 1 :</b>
             * Pour chaque table, v�rifie si pr�sent dans la table 
             * et incr�mente le compteur ==> V�rification du nombre de table
             */
            String sql = "select tablename from pg_tables where tableowner='" + dbConnect.getUser() + "' order by tablename";

            ResultSet res = dbConnect.doRequest(sql);

            int iTables = 0;
           
            while (res.next() && (iTables < lstTables.length)) {
                if (res.getString("tablename").equalsIgnoreCase(lstTables[iTables])) {
                    // Table trouv�e
                    iTables++;
                };
            }
            res.close();
            
            if (iTables < lstTables.length) {
                // Pas bon
                throw new ReloadNeededException("Table " + lstTables[iTables] + " manquante dans la base!!");
            }

            /**
             * <b>Partie 2 :</b>
             * Pour chaque s�quence, v�rifie si pr�sent dans la liste 
             * et incr�mente le compteur ==> V�rification du nombre de s�quences
             */
            sql = "select relname from pg_class, pg_user where relkind='S' "
                        + "and pg_class.relowner=pg_user.usesysid "
                        + "and usename='" + dbConnect.getUser() + "' order by relname";

            res = dbConnect.doRequest(sql);

            int iSequence = 0;
           
            while (res.next() && (iSequence < lstSequences.length)) {
                if (res.getString("relname").equalsIgnoreCase(lstSequences[iSequence])) {
                    // S�quence trouv�e
                    /**
                     * <b>Partie 3 :</b>
                     * Pour chaque s�quence, v�rifie que la valeur est au dela du max de la table
                     */
                    String table = lstSequences[iSequence].substring(4);
                    String sql2 = "select max(CD_" + table + ") from " + table;
                    ResultSet res2 = dbConnect.doRequest(sql2);
                    if (res2.next()) {
                        long valeur = res2.getLong(1);

                        // Recherche la valeur de la sequence
                        String sql3 = "select last_value from " + lstSequences[iSequence];
                        ResultSet res3 = dbConnect.doRequest(sql3);
                        if (res3.next()) {
                            if (valeur > res3.getLong(1)) {
                                // Pas bon
                            	if (forceSequence) {
                            		// Maj de la s�quence
                            		log.info("Mise � jour de la s�quence " + lstSequences[iSequence] + " n'est pas bien valoris�e : Max=" + valeur + " Sequence=" + res3.getLong(1));
                                    String sql4 = 
                                    	"select setval('" + lstSequences[iSequence] + "', " + valeur + ", true)";
                                    ResultSet rs = dbConnect.doRequest(sql4);
                                    rs.close();
                            	}
                            	else {
                            		throw new ReloadNeededException("S�quence " + lstSequences[iSequence] + " n'est pas bien valoris�e : Max=" + valeur + " Sequence=" + res3.getLong(1));
                            	}
                            }
                        }
                        res3.close();
                    }
                    res2.close();
                    iSequence++;
                };
            }
            res.close();
            
            if (iSequence < lstSequences.length) {
                // Pas bon
                throw new ReloadNeededException("S�quence " + lstSequences[iSequence] + " manquante dans la base!!");
            }
            
            try {
            	IdentBean.verificationSuper(dbConnect);
            }
            catch (FctlException e) {
				// Pas de super : Cr�ation du compte en auto
        		log.info("Cr�ation d'un compte super");
                IdentBean ident = new IdentBean();
                ident.setCD_PROFIL(IdentBean.PROFIL_SUPER);
                ident.setETAT_CPT(IdentBean.ETAT_ACTIF);
                ident.setLIB_IDENT("Profil super");
                ident.setMOT_PASSE("MDPRECUP");
                ident.create(dbConnect);
			}
        }
        catch (SQLException e) {
            // Erreur SQL
            System.out.println ("checkDatabase:: Erreur :");
            e.printStackTrace();
            throw new ReloadNeededException("Erreur � la v�rification de la base : " + e.toString());
        }
        
        return etat;
    }
}
