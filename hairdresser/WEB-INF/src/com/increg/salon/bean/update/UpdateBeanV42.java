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
 * Passage à la version 4.2
 * Creation date : 30 déc. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV42 extends UpdateBeanV41 {

	protected boolean forceSequence;
	
    /**
     * Constructor for UpdateBeanVxx.
     * @param dbConnect .
     * @throws Exception .
     */
    public UpdateBeanV42(DBSession dbConnect, ResourceBundle rb, boolean forceLesSequences) throws Exception {
        super(dbConnect, rb);
        forceSequence = forceLesSequences;
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
        if (version.equals("4.1")) {
            // Mise à jour de la base pour passer en 4.2
            // Reset des trigger pour passer en FK
        	String[] tableFK = {
        			"paiement"
        		};
        	// Requète Avant / Après
            String[][] reqStat = {
                };
            String[] sql = {
                "update VERSION set VERSION='4.2'",
                "create table REGLEMENT ( " +
                	"CD_REGLEMENT numeric(6) not null, " +
                	"CD_PAIEMENT numeric(6) not null, " +
                	"MONTANT decimal(8,2) not null, " +
                	"CD_MOD_REGL numeric(2) not null )",
                "create sequence SEQ_REGLEMENT",
                "alter table REGLEMENT alter CD_REGLEMENT set default nextval('SEQ_REGLEMENT')",
                "alter table REGLEMENT add constraint PK_REGLEMENT PRIMARY KEY (CD_REGLEMENT)",
                "alter table REGLEMENT add constraint FK_REGLEMENT_ESTREGLEPAR_MOD_REGL foreign key (CD_MOD_REGL) references MOD_REGL (CD_MOD_REGL)",
                "alter table REGLEMENT add constraint FK_REGLE_PAIEMENT foreign key (CD_PAIEMENT) references PAIEMENT (CD_PAIEMENT)",
                "insert into REGLEMENT (CD_REGLEMENT, CD_PAIEMENT, MONTANT, CD_MOD_REGL) " +
                	"select CD_PAIEMENT, CD_PAIEMENT, PRX_TOT_TTC, CD_MOD_REGL from PAIEMENT where PRX_TOT_TTC is not null",
/*                "create table PAIEMENT_new (" +
                	"CD_PAIEMENT numeric(6) not null, " +
                	"DT_PAIEMENT date, " +
                	"DT_CREAT timestamp without time zone not null, " +
                	"DT_MODIF timestamp without time zone not null default now())",
                "insert into PAIEMENT_new (CD_PAIEMENT, DT_PAIEMENT, DT_CREAT, DT_MODIF) " +
                	"select CD_PAIEMENT, DT_PAIEMENT, DT_CREAT, DT_MODIF from PAIEMENT",
                "drop table PAIEMENT",
                "alter table PAIEMENT alter CD_PAIEMENT set default nextval('SEQ_PAIEMENT')",
                "alter table PAIEMENT add constraint PK_PAIEMENT PRIMARY KEY (CD_PAIEMENT)",
*/
                "alter table FACT add constraint FK_ESTPAYE_PAR foreign key (CD_PAIEMENT) references PAIEMENT (CD_PAIEMENT)",
                "alter table PAIEMENT drop column CD_MOD_REGL",
                "alter table PAIEMENT drop column PRX_TOT_TTC",
                "alter table MVT_CAISSE rename column CD_PAIEMENT to CD_REGLEMENT",
                "alter table ABO_CLI add constraint FK_APOUR_CLIENT_ABO foreign key (CD_CLI) references CLI (CD_CLI)",
                "alter table ABO_CLI add constraint FK_APOUR_PREST_ABO foreign key (CD_PREST) references PREST (CD_PREST)",
                };
            String[] sqlAvecRes = {
            	"select setval('seq_reglement', max(CD_REGLEMENT)::bigint, true) from REGLEMENT"
            		};

            for (int i = 0; i < tableFK.length; i++) {
                String[] aSql = new String[1];

                // Suppression des FK via des triggers (issue des vieilles versions de PostgreSQL)
                ResultSet rs = dbConnect.doRequest("select tgname, relname from pg_trigger, pg_class where pg_trigger.tgrelid=pg_class.oid and relname='" + tableFK[i] + "'");
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
                
                // Suppression des FK classiques
                rs = dbConnect.doRequest("select conname, table1.relname, table2.relname from pg_constraint, pg_class table1, pg_class table2 " +
                		"where pg_constraint.conrelid=table1.oid and pg_constraint.confrelid=table2.oid " +
                		"and (table1.relname='" + tableFK[i] + "' or table2.relname='" + tableFK[i] + "') " +
                		"and contype='f'");
        		while (rs.next()) {
        			aSql[0] = "alter table " + rs.getString(2) + " drop constraint " + rs.getString(1);
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

            // On vient de passer en 4.2
            version = "4.2";
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
                            "REGLEMENT",
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
                            "SEQ_REGLEMENT",
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
    
    /**
     * @see com.increg.salon.bean.update.UpdateBean#checkDatabase(DBSession, String[], String[])
     */
    public boolean checkDatabase(DBSession dbConnect, String[] lstTables, String[] lstSequences) throws ReloadNeededException {

        // Valeur retournée
        boolean etat = true;
                
        try {        

            /**
             * <b>Partie 1 :</b>
             * Pour chaque table, vérifie si présent dans la table 
             * et incrémente le compteur ==> Vérification du nombre de table
             */
            String sql = "select tablename from pg_tables where tableowner='" + dbConnect.getUser() + "' order by tablename";

            ResultSet res = dbConnect.doRequest(sql);

            int iTables = 0;
           
            while (res.next() && (iTables < lstTables.length)) {
                if (res.getString("tablename").equalsIgnoreCase(lstTables[iTables])) {
                    // Table trouvée
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
             * Pour chaque séquence, vérifie si présent dans la liste 
             * et incrémente le compteur ==> Vérification du nombre de séquences
             */
            sql = "select relname from pg_class, pg_user where relkind='S' "
                        + "and pg_class.relowner=pg_user.usesysid "
                        + "and usename='" + dbConnect.getUser() + "' order by relname";

            res = dbConnect.doRequest(sql);

            int iSequence = 0;
           
            while (res.next() && (iSequence < lstSequences.length)) {
                if (res.getString("relname").equalsIgnoreCase(lstSequences[iSequence])) {
                    // Séquence trouvée
                    /**
                     * <b>Partie 3 :</b>
                     * Pour chaque séquence, vérifie que la valeur est au dela du max de la table
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
                            		// Maj de la séquence
                            		log.info("Mise à jour de la séquence " + lstSequences[iSequence] + " n'est pas bien valorisée : Max=" + valeur + " Sequence=" + res3.getLong(1));
                                    String sql4 = 
                                    	"select setval('" + lstSequences[iSequence] + "', " + valeur + ", true)";
                                    ResultSet rs = dbConnect.doRequest(sql4);
                                    rs.close();
                            	}
                            	else {
                            		throw new ReloadNeededException("Séquence " + lstSequences[iSequence] + " n'est pas bien valorisée : Max=" + valeur + " Sequence=" + res3.getLong(1));
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
                throw new ReloadNeededException("Séquence " + lstSequences[iSequence] + " manquante dans la base!!");
            }
        }
        catch (SQLException e) {
            // Erreur SQL
            System.out.println ("checkDatabase:: Erreur :");
            e.printStackTrace();
            throw new ReloadNeededException("Erreur à la vérification de la base : " + e.toString());
        }
        
        return etat;
    }
}
