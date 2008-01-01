/*
 * Mise à jour de la base de données
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.increg.commun.DBSession;
import com.increg.salon.bean.TypMvtBean;

/**
 * .................
 * @version 	1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBeanV21 extends UpdateBeanV20 {

	/**
	 * Constructor for UpdateBeanV21.
	 * @param dbConnect .
	 * @throws Exception .
	 */
	public UpdateBeanV21(DBSession dbConnect, ResourceBundle rb) throws Exception {
		super(dbConnect, rb);
	}

    /**
     * @see UpdateBean#deduitVersion()
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {

        // Vérification de la présence de la colonne Mixte sur l'article
        String sql = "select INDIC_MIXTE from ART limit 1";
        try {
            ResultSet rs = dbConnect.doRequest(sql);
            if (rs.next()) {
                // Tout va bien 
                version = "2.1";
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
        if (version.equals("2.0")) {
            // Mise à jour de la base pour passer en 2.0
            // Requète Avant / Après
            String reqStat[][] = {
                {
                "select date_trunc('month', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)'",
                "select date_trunc('month', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_CATEG_PREST = $CD_CATEG_PREST$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)'"
                },
                {
                "select date_trunc('month', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, CLI where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_CLI = CLI.CD_CLI and CLI.CD_CATEG_CLI = $CD_CATEG_CLI$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)",
                "select date_trunc('month', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, CLI where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_CLI = CLI.CD_CLI and CLI.CD_CATEG_CLI = $CD_CATEG_CLI$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)"
                },
                {
                "select date_trunc('month', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)",
                "select date_trunc('month', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)"
                },
                {
                "select date_trunc('month', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)",
                "select date_trunc('month', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and HISTO_PREST.CD_COLLAB = $CD_COLLAB$ and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)"
                },
                {
                "select date_trunc('month', DT_PAIEMENT), SUM(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)",
                "select date_trunc('month', DT_PAIEMENT), SUM(round(HISTO_PREST.PRX_UNIT_TTC*HISTO_PREST.QTE,2)) from FACT, PAIEMENT, HISTO_PREST, PREST where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT and HISTO_PREST.CD_FACT = FACT.CD_FACT and HISTO_PREST.CD_PREST = PREST.CD_PREST and PREST.CD_TYP_VENT = $CD_TYP_VENT$ and FACT.DT_PREST between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_PAIEMENT)"
                },
                {
                "select date_trunc('month', DT_MVT), sum(MVT_STK.VAL_MVT_HT*MVT_STK.QTE)\nfrom MVT_STK, ART\nwhere MVT_STK.CD_ART = ART.CD_ART\nand ART.CD_CATEG_ART = $CD_CATEG_ART$\nand MVT_STK.DT_MVT between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_MVT)",
                "select date_trunc('month', DT_MVT), sum(round(MVT_STK.VAL_MVT_HT*MVT_STK.QTE,2))\nfrom MVT_STK, ART\nwhere MVT_STK.CD_ART = ART.CD_ART\nand ART.CD_CATEG_ART = $CD_CATEG_ART$\nand MVT_STK.DT_MVT between '$DateDebut$' and '$DateFin$' group by date_trunc('month', DT_MVT)"
                }
                };
            String sql[] = {
                "alter table ART add INDIC_MIXTE char(1) constraint CKC_INDIC_MIXTE check (INDIC_MIXTE is null or INDIC_MIXTE in ('O', 'N'))",
                "alter table TYP_MVT add TRANSF_MIXTE char(1) null constraint CKC_TRANSF_MIXTE check (TRANSF_MIXTE is null or TRANSF_MIXTE in ('O', 'N'))",
                "update TYP_MVT set TRANSF_MIXTE='N'",
                "update TYP_MVT set TRANSF_MIXTE='O' where CD_TYP_MVT="+Integer.toString(TypMvtBean.UTILISATION),
                
                "update ART set INDIC_MIXTE='N'",
                "update ART set INDIC_MIXTE='O' where CD_TYP_ART=1 and exists (select * from MVT_STK, TYP_MVT where MVT_STK.CD_ART=ART.CD_ART and MVT_STK.CD_TYP_MVT=TYP_MVT.CD_TYP_MVT and TRANSF_MIXTE='O')"
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
            // On vient de passer en 2.1
            version = "2.1";
        }
    }
}
