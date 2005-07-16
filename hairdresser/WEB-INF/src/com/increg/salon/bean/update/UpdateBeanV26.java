package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;

/**
 * Passage à la version 2.6
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV26 extends UpdateBeanV25 {

	/**
	 * Constructor for UpdateBeanV24.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV26(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la taille de la colonne cd_ident
        String[] sql = new String[1];
        sql[0] = "insert into ident (cd_ident, lib_ident, mot_passe, cd_profil, etat_cpt, dt_creat, dt_modif)"
                +   " values (9999, 'Test Version', 'jqsdfjqsdfinndiixcj', 1, 'B', now(), now())";
        try {
            int[] res = new int[1];
            res = dbConnect.doExecuteSQL(sql);
            // Tout va bien : l'insert est passé
            version = "2.6";
            // Nettoyage
            sql[0] = "delete from ident where cd_ident=9999";
            res = dbConnect.doExecuteSQL(sql);
        }
        catch (SQLException se) {
            // Erreur SQL : insert en erreur
            // C'est donc une version antérieure
            super.deduitVersion(dbConnect);
        }
    }

    /**
     * @see UpdateBean#majVersion()
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
        super.majVersion(dbConnect);
        if (version.equals("2.5")) {
            // Mise à jour de la base pour passer en 2.6
            // Requète Avant / Après
            String reqStat[][] = {
                };
            String sql[] = {
                  "alter table IDENT rename to IDENT_OLD",
                  "drop index pk_ident",
                  "CREATE TABLE IDENT ( "
                    + "CD_IDENT numeric(6,0) DEFAULT nextval('SEQ_IDENT'::text) NOT NULL,"
                    + "LIB_IDENT character varying(80) NOT NULL,"
                    + "MOT_PASSE character varying(20) NOT NULL,"
                    + "CD_PROFIL numeric(2,0) NOT NULL,"
                    + "ETAT_CPT character(1) DEFAULT 'A' NOT NULL"
                    + "  constraint CKC_ETAT_CPT check (ETAT_CPT in ('A','B') ),"
                    + "DT_CREAT timestamp with time zone NOT NULL,"
                    + "DT_MODIF timestamp with time zone DEFAULT now() NOT NULL,"
                    + "  Constraint pk_ident Primary Key (cd_ident))",
                  "insert into IDENT (CD_IDENT, LIB_IDENT, MOT_PASSE, CD_PROFIL, ETAT_CPT, DT_CREAT, DT_MODIF)"
                    + " select CD_IDENT, LIB_IDENT, MOT_PASSE, CD_PROFIL, ETAT_CPT, DT_CREAT, DT_MODIF from IDENT_OLD",
                  "drop table IDENT_OLD",
                  "alter table IDENT add constraint FK_ESTDE_PROFIL foreign key (CD_PROFIL) references PROFIL (CD_PROFIL)"
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
            
            // On vient de passer en 2.6
            version = "2.6";
        }
    }
}
