package com.increg.salon.bean.update;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;

/**
 * Bean de mise à jour de la base à partir d'une version 1.5 vers 1.7
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV17 extends UpdateBean {

	/**
	 * Constructor for UpdateBeanV15.
	 * @param dbConnect Connexion à la base
	 * @throws Exception En cas de pepin
	 */
	public UpdateBeanV17(DBSession dbConnect) throws Exception {
		super(dbConnect);
	}


	/**
	 * @see UpdateBean#deduitVersion()
	 */
	protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la table STAT
        String sql = "select CD_STAT from STAT";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            // Tout va bien 
            version = "1.7";
            rs.close();
        }
        catch (SQLException se) {
            // Erreur SQL : Table introuvable
            // C'est donc une version antérieure
            super.deduitVersion(dbConnect);
        }
	}

	/**
	 * @see UpdateBean#majVersion()
	 */
	protected void majVersion(DBSession dbConnect) throws Exception {
		super.majVersion(dbConnect);
        if (version.equals("1.5")) {
            // Mise à jour de la base pour passer en 1.7
            String sql[] = {
                    "create table STAT (" + 
                    "      CD_STAT     numeric(3)     not null," +
                    "      LIB_STAT    varchar(80)    not null," +
                    "      REQ_SQL     text       not null," +
                    "      LABEL_X     varchar(80)    null," +
                    "      LABEL_Y     varchar(80)    null," +
                    "      DT_CREAT    timestamp      not null," +
                    "      DT_MODIF    timestamp      not null default now()," +
                    "         constraint PK_STAT primary key (CD_STAT) " +
                    ")",
                    "create sequence SEQ_STAT",
                    "alter table STAT alter CD_STAT set default nextval('SEQ_STAT')"};
            
            for (int i = 0; i < sql.length; i++) {
                String aSql[] = new String[1];
                aSql[0] = sql[i];
                dbConnect.doExecuteSQL(aSql);
            }
            // On vient de passer en 1.7
            version = "1.7";
        }
	}

}