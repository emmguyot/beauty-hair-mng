package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;
import com.increg.commun.exception.ReloadNeededException;

/**
 * Passage à la version 2.7
 * Creation date : 3 nov. 2002
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV27 extends UpdateBeanV26 {

	/**
	 * Constructor for UpdateBeanVxx.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV27(DBSession dbConnect) throws Exception {
		super(dbConnect);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la taille de la colonne cd_ident
        String sql = "select CD_CMD_FOURN from MVT_STK where CD_ART=-1";
        try {
            ResultSet res = dbConnect.doRequest(sql);
            // Tout va bien : le champ existe
            version = "2.7";
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
        if (version.equals("2.6")) {
            // Mise à jour de la base pour passer en 2.7
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                  "alter table MVT_STK add CD_CMD_FOURN varchar (10) null",
                  "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                  + "11, 'Nombre de prestations par catégorie et par personne', "
                  + "'select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), sum(HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST, COLLAB "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST "
                  + "and HISTO_PREST.CD_COLLAB = COLLAB.CD_COLLAB and COLLAB.CD_COLLAB = $CD_COLLAB$ and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ "
                  + "and FACT.DT_PREST between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)',"
                  + "'Période', 'Nombre', now(), now())",
                  "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                  + "12, 'Chiffre d''affaires par personne et par catégorie', "
                  + "'select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST "
                  + "and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ "
                  + "and FACT.DT_PREST between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)',"
                  + "'Période', 'Montant TTC', now(), now())",
                  "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                  + "13, 'Clients actifs par genre', "
                  + "'select date_trunc(''$PeriodeTemps$'', FACT.DT_PREST), count(DISTINCT FACT.CD_CLI) from CLI, FACT, PAIEMENT "
                  + "where FACT.CD_CLI = CLI.CD_CLI and FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT "
                  + "and ((''$Genre$'' = ''M'' and CIVILITE=''M.'') or (''$Genre$'' = ''F'' and CIVILITE in (''Mle'', ''Mme''))) "
                  + "and FACT.DT_PREST between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', FACT.DT_PREST)',"
                  + "'Période', 'Nombre', now(), now())",
                  "insert into STAT (CD_STAT,  LIB_STAT, REQ_SQL, LABEL_X, LABEL_Y, DT_CREAT, DT_MODIF) values ("
                  + "14, 'Clients actifs par trimestre par genre', "
                  + "'select to_char(extract(YEAR from DT_PAIEMENT), ''9999'') || to_char(extract(QUARTER from DT_PAIEMENT), '' T9''), count(DISTINCT FACT.CD_CLI) from CLI, FACT, PAIEMENT "
                  + "where FACT.CD_CLI = CLI.CD_CLI and FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT "
                  + "and ((''$Genre$'' = ''M'' and CIVILITE=''M.'') or (''$Genre$'' = ''F'' and CIVILITE in (''Mle'', ''Mme''))) "
                  + "and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by to_char(extract(YEAR from DT_PAIEMENT), ''9999'') || to_char(extract(QUARTER from DT_PAIEMENT), '' T9'')',"
                  + "'Trimestre', 'Nombre', now(), now())",
                  // Statistiques par période de temps
                  // Mise à jour de la requête, du libellé et du l'absisse
                  "update STAT set LIB_STAT='Nombre de prestations par catégorie', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', FACT.DT_PREST), sum(HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST "
                  + "and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', FACT.DT_PREST)', "
                  + "LABEL_X='Période' where CD_STAT=1",
                  "update STAT set LIB_STAT='Clients actifs', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', FACT.DT_PREST), count(DISTINCT FACT.CD_CLI) from FACT, PAIEMENT "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and FACT.DT_PREST between ''$DateDebut$'' and ''$DateFin$'' "
                  + "group by date_trunc(''$PeriodeTemps$'', FACT.DT_PREST)', "
                  + "LABEL_X='Période' where CD_STAT=2",
                  "update STAT set "
                  + "REQ_SQL='select to_char(extract(YEAR from DT_PAIEMENT), ''9999'') || to_char(extract(QUARTER from DT_PAIEMENT), '' T9''), "
                  + "count(DISTINCT FACT.CD_CLI) from FACT, PAIEMENT where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT "
                  + "and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by to_char(extract(YEAR from DT_PAIEMENT), ''9999'') || to_char(extract(QUARTER from DT_PAIEMENT), '' T9'')' "
                  + "where CD_STAT=3",
                  "update STAT set LIB_STAT='Chiffre d''affaires par catégorie de client', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, CLI "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_CLI = CLI.CD_CLI "
                  + "and CLI.CD_CATEG_CLI = $CD_CATEG_CLI$ and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)', "
                  + "LABEL_X='Période' where CD_STAT=4",
                  "update STAT set LIB_STAT='Chiffre d''affaires', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(FACT.PRX_TOT_TTC) from FACT, PAIEMENT "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' "
                  + "group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)', "
                  + "LABEL_X='Période' where CD_STAT=5",
                  "update STAT set LIB_STAT='Chiffre d''affaires par catégorie', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST "
                  + "and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)', "
                  + "LABEL_X='Période' where CD_STAT=6",
                  "update STAT set LIB_STAT='Chiffre d''affaires par type de prestations', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_TYP_VENT = $CD_TYP_VENT$ "
                  + "and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)', "
                  + "LABEL_X='Période' where CD_STAT=7",
                  "update STAT set LIB_STAT='Chiffre d''affaires par personne', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ "
                  + "and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)', "
                  + "LABEL_X='Période' where CD_STAT=8",
                  "update STAT set LIB_STAT='Chiffre d''affaires par personne et par type de prestations', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST "
                  + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST "
                  + "and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_TYP_VENT = $CD_TYP_VENT$ "
                  + "and DT_PAIEMENT between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_PAIEMENT)', "
                  + "LABEL_X='Période' where CD_STAT=9",
                  "update STAT set LIB_STAT='Consommation valorisée par catégorie de produits', "
                  + "REQ_SQL='select date_trunc(''$PeriodeTemps$'', DT_MVT), sum(MVT_STK.VAL_MVT_HT*MVT_STK.QTE) from MVT_STK, ART "
                  + "where MVT_STK.CD_ART = ART.CD_ART and MVT_STK.CD_TYP_MVT = $CD_TYP_MVT$ and ART.CD_CATEG_ART = $CD_CATEG_ART$ "
                  + "and MVT_STK.DT_MVT between ''$DateDebut$'' and ''$DateFin$'' group by date_trunc(''$PeriodeTemps$'', DT_MVT)', "
                  + "LABEL_X='Période' where CD_STAT=10"
                };
            String sqlAvecRes[] = {
                // Mise à jour de la sequence de statistique
                "select setval ('seq_stat', max (CD_STAT), true) from STAT"
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
            
            // On vient de passer en 2.7
            version = "2.7";
        }
    }
    /**
     * @see com.increg.salon.bean.update.UpdateBean#checkDatabase(DBSession)
     */
    public boolean checkDatabase(DBSession dbConnect) throws ReloadNeededException {

        // Liste des tables qui devraient être présentes, dans l'ordre alphabétique
        String lstTables[] = {
                            "ART",
                            "CAISSE",
                            "CAT_FOURN",
                            "CATEG_ART",
                            "CATEG_CLI",
                            "CATEG_PREST",
                            "CLI",
                            "COLLAB",
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
                            "SOC",
                            "STAT",
                            "TR_AGE",
                            "TYP_ART",
                            "TYP_CHEV",
                            "TYP_CONTR",
                            "TYP_MCA",
                            "TYP_MVT",
                            "TYP_POINTAGE",
                            "TYP_VENT",
                            "UNIT_MES"
                            };

        // liste des séquences qui devraient être présentes, dans l'ordre alphabétique
        String lstSequences[] = {
                            "SEQ_ART",
                            "SEQ_CATEG_ART",
                            "SEQ_CATEG_CLI",
                            "SEQ_CATEG_PREST",
                            "SEQ_CLI",
                            "SEQ_COLLAB",
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
                            "SEQ_TYP_ART",
                            "SEQ_TYP_CHEV",
                            "SEQ_TYP_CONTR",
                            "SEQ_TYP_MCA",
                            "SEQ_TYP_MVT",
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
                                throw new ReloadNeededException("Séquence " + lstSequences[iSequence] + " n'est pas bien valorisée : Max=" + valeur + " Sequence=" + res3.getLong(1));
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
