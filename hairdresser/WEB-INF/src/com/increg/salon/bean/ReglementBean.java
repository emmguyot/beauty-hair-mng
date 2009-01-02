/*
 * Gestion d'un réglement d'un paiement
 * Copyright (C) 2008-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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

package com.increg.salon.bean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.GenericBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;

/**
 * Gestion d'un réglement (d'un paiement)
 * Creation date: (17/08/2001 20:08:57)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ReglementBean extends GenericBean {
    /**
     * Code du réglement
     */
    protected long CD_REGLEMENT;
    /**
     * Code du paiement
     */
    protected long CD_PAIEMENT;

    /**
     * Montant total du réglement
     */
    protected java.math.BigDecimal MONTANT;

    /**
     * Montant total avant modification
     */
    protected java.math.BigDecimal MONTANT_INIT;

    /**
     * Mode de règlement
     */
    protected int CD_MOD_REGL;

    /**
     * ReglementBean constructor comment.
     * @param rb Messages à utiliser
     */
    public ReglementBean(ResourceBundle rb) {
        super(rb);
    }

    /**
     * ReglementBean constructor comment.
     * 
     * @param rs java.sql.ResultSet
     * @param rb Messages à utiliser
     */
    public ReglementBean(ResultSet rs, ResourceBundle rb) {
        super(rb);
        try {
            CD_REGLEMENT = rs.getLong("CD_REGLEMENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ReglementBean (RS) : "
                        + e.toString());
            }
        }
        try {
            CD_PAIEMENT = rs.getLong("CD_PAIEMENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ReglementBean (RS) : "
                        + e.toString());
            }
        }
        try {
            CD_MOD_REGL = rs.getInt("CD_MOD_REGL");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ReglementBean (RS) : "
                        + e.toString());
            }
        }
        try {
            MONTANT = rs.getBigDecimal("MONTANT", 2);
            MONTANT_INIT = MONTANT;
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ReglementBean (RS) : "
                        + e.toString());
            }
        }
    }

    /**
     * @throws FctlException 
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException, FctlException {

        StringBuffer req = new StringBuffer("insert into REGLEMENT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_REGLEMENT == 0) {
            /**
             * Numérotation automatique des codes reglement
             */
            String reqMax = "select nextval('SEQ_REGLEMENT')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_REGLEMENT = 1; // Par défaut

                while (aRS.next()) {
                	CD_REGLEMENT = aRS.getLong(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_REGLEMENT,");
        valeur.append(CD_REGLEMENT);
        valeur.append(",");

        if (CD_PAIEMENT != 0) {
            colonne.append("CD_PAIEMENT,");
            valeur.append(CD_PAIEMENT);
            valeur.append(",");
        }

        if (CD_MOD_REGL != 0) {
            colonne.append("CD_MOD_REGL,");
            valeur.append(CD_MOD_REGL);
            valeur.append(",");
        }

        if (MONTANT != null) {
            colonne.append("MONTANT,");
            valeur.append(MONTANT.toString());
            valeur.append(",");
        }

        // retire les dernières virgules
        colonne.setLength(colonne.length() - 1);
        valeur.setLength(valeur.length() - 1);

        // Constitue la requete finale
        req.append(colonne);
        req.append(")");
        req.append(valeur);
        req.append(")");

        // Fait les mouvements de caisse
        mouvemente(dbConnect, false);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws SQLException, FctlException {

        /**
         * Suppression effective
         */
        StringBuffer req = new StringBuffer("delete from REGLEMENT ");
        StringBuffer where = new StringBuffer(" where CD_REGLEMENT="
                + CD_REGLEMENT);

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Fait les mouvements de caisse
        mouvemente(dbConnect, true);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
        }

        // Fin de la transaction
        dbConnect.endTransaction();

    }

    /**
     * Suppression d'un paiement et reconstitution des mouvements
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @throws SQLException
     *             En cas d'erreur durant la suppression
     */
    public void deletePur(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG(
        		"dd/MM/yyyy HH:mm:ss");

        /**
         * Suppression effective
         */
        StringBuffer req = new StringBuffer("delete from REGLEMENT ");
        StringBuffer where = new StringBuffer(" where CD_REGLEMENT="
                + CD_REGLEMENT);

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Suppression des mouvements de caisse
        Vector listeMvtPaiement = MvtCaisseBean.getMvtCaisseBean(dbConnect,
                Long.toString(CD_REGLEMENT));
        Calendar dateMin = Calendar.getInstance();
        for (int i = 0; i < listeMvtPaiement.size(); i++) {
            MvtCaisseBean aMvt = (MvtCaisseBean) listeMvtPaiement.get(i);
            if (aMvt.getDT_MVT().before(dateMin)) {
                dateMin = aMvt.getDT_MVT();
            }
            aMvt.delete(dbConnect);
        }

        // Execute la suppression
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
        }

        try {
	        // Mise à jour des soldes de caisse à partir de la date min
	        MvtCaisseBean aMvt = MvtCaisseBean.getLastMvtCaisseBean(dbConnect,
	                Integer.toString(CD_MOD_REGL), formatDate.formatEG(dateMin
	                        .getTime()), Locale.FRENCH);
	        MvtCaisseBean.checkAndFix(dbConnect, Integer.toString(CD_MOD_REGL),
	                formatDate.formatEG(aMvt.getDT_MVT().getTime()), Locale.FRENCH);
        }
        catch (SQLException e) {
        	// Propage l'exception
			throw e;
		}
        catch (Exception e) {
			// Problème de conversion de la date
        	System.out.println("Erreur de conversion de la date");
        	e.printStackTrace();
		}
        
        // Fin de la transaction
        dbConnect.endTransaction();

    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:19:23)
     * 
     * @return long
     */
    public long getCD_PAIEMENT() {
        return CD_PAIEMENT;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:27:33)
     * 
     * @return int
     */
    public int getCD_MOD_REGL() {
        return CD_MOD_REGL;
    }

    /**
     * Création d'un Bean Règlement à partir de sa clé 
     * Creation date: (18/08/2001 17:05:45)
     * 
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_REGLEMENT java.lang.String
     * @param rb Messages à utiliser
     * @return Réglement correspondant au code
     */
    public static ReglementBean getReglementBean(DBSession dbConnect, String CD_REGLEMENT, ResourceBundle rb) {
        String reqSQL = "select * from REGLEMENT where CD_REGLEMENT="
                + CD_REGLEMENT;
        ReglementBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new ReglementBean(aRS, rb);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : "
                    + e.toString());
        }
        return res;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:51)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getMONTANT() {
        return MONTANT;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getMONTANT_Franc() {
        if (MONTANT != null) {
            return MONTANT.multiply(new BigDecimal(6.55957)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
        } else {
            return null;
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException, FctlException {

        StringBuffer req = new StringBuffer("update REGLEMENT set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_REGLEMENT="
                + CD_REGLEMENT);

        colonne.append("CD_PAIEMENT=");
        if (CD_PAIEMENT != 0) {
            colonne.append(CD_PAIEMENT);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_MOD_REGL=");
        if (CD_MOD_REGL != 0) {
            colonne.append(CD_MOD_REGL);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("MONTANT=");
        if (MONTANT != null) {
            colonne.append(MONTANT.toString());
        } else {
            colonne.append("NULL");
        }

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Fait les mouvements de stock
        mouvemente(dbConnect, false);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.enregistrementKo" + BasicSession.TAG_I18N));
        }

        // Fin de la transaction
        dbConnect.endTransaction();

    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:19:23)
     * 
     * @param newCD_PAIEMENT
     *            long
     */
    public void setCD_PAIEMENT(long newCD_PAIEMENT) {
        CD_PAIEMENT = newCD_PAIEMENT;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:19:23)
     * 
     * @param newCD_PAIEMENT
     *            String
     */
    public void setCD_PAIEMENT(String newCD_PAIEMENT) {

        if ((newCD_PAIEMENT != null) && (newCD_PAIEMENT.length() != 0)) {
            CD_PAIEMENT = Long.parseLong(newCD_PAIEMENT);
        } else {
            CD_PAIEMENT = 0;
        }
    }


	/**
	 * @return the cD_REGLEMENT
	 */
	public long getCD_REGLEMENT() {
		return CD_REGLEMENT;
	}

	/**
	 * @param cd_reglement the cD_REGLEMENT to set
	 */
	public void setCD_REGLEMENT(long cd_reglement) {
		CD_REGLEMENT = cd_reglement;
	}

    /**
	 * @param cd_reglement the cD_REGLEMENT to set
     */
    public void setCD_REGLEMENT(String cd_reglement) {

        if ((cd_reglement != null) && (cd_reglement.length() != 0)) {
        	CD_REGLEMENT = Long.parseLong(cd_reglement);
        } else {
        	CD_REGLEMENT = 0;
        }
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:27:33)
     * 
     * @param newCD_MOD_REGL
     *            int
     */
    public void setCD_MOD_REGL(int newCD_MOD_REGL) {
        CD_MOD_REGL = newCD_MOD_REGL;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:27:33)
     * 
     * @param newCD_MOD_REGL
     *            String
     */
    public void setCD_MOD_REGL(String newCD_MOD_REGL) {
        if ((newCD_MOD_REGL != null) && (newCD_MOD_REGL.length() != 0)) {
            CD_MOD_REGL = Integer.parseInt(newCD_MOD_REGL);
        } else {
            CD_MOD_REGL = 0;
        }
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @param newMONTANT
     *            String
     */
    public void setMONTANT(String newMONTANT) {

        if ((newMONTANT != null) && (newMONTANT.length() != 0)) {
            MONTANT = new BigDecimal(newMONTANT);
        } else {
            MONTANT = null;
        }
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @param newMONTANT
     *            java.math.BigDecimal
     */
    public void setMONTANT(java.math.BigDecimal newMONTANT) {
        MONTANT = newMONTANT;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Effectue les mouvements de caisse correspondant à ce paiement Creation
     * date: (24/09/2001 09:21:28)
     * 
     * @param dbConnect
     *            com.increg.salon.bean.DBSession
     * @param delete
     *            Il s'agit d'une suppression de paiement
     * @throws SQLException
     *             Si erreur de programme
     * @throws FctlException
     *             Si erreur d'initialisation
     */
    protected void mouvemente(DBSession dbConnect, boolean delete)
            throws SQLException, FctlException {

        if (MONTANT == null) {
            MONTANT = new BigDecimal(0);
            MONTANT.setScale(2);
        }
        if (MONTANT_INIT == null) {
            MONTANT_INIT = new BigDecimal(0);
            MONTANT_INIT.setScale(2);
        }

        Vector lstFact = PaiementBean.getPaiementBean(dbConnect, Long.toString(CD_PAIEMENT), message).getFact(dbConnect);
        boolean mvtCree = false;

        if (delete || (MONTANT.compareTo(MONTANT_INIT) != 0)) {
            // Annulation du précédent
            if (MONTANT_INIT.compareTo(new BigDecimal(0)) != 0) {
                CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect, Integer.toString(CD_MOD_REGL));
                if (aCaisse == null) {
                    throw new FctlException(BasicSession.TAG_I18N + "reglementBean.caissesKo" + BasicSession.TAG_I18N);
                }
                MvtCaisseBean aMvt = new MvtCaisseBean();

                aMvt.setCD_MOD_REGL(CD_MOD_REGL);
                aMvt.setCD_REGLEMENT(CD_REGLEMENT);
                aMvt.setCD_TYP_MCA(TypMcaBean.ENCAISSEMENT);
                String Comm = "Règlement annulé";
                aMvt.setCOMM(Comm);
                aMvt.setDT_MVT(Calendar.getInstance());
                aMvt.setMONTANT(MONTANT_INIT.negate());
                aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());

                aMvt.create(dbConnect);
                mvtCree = true;
            }
        }
        if (!delete) {
            if (((MONTANT.compareTo(MONTANT_INIT) != 0))
                    && (MONTANT.compareTo(new BigDecimal(0)) != 0)) {
                CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect,
                        Integer.toString(CD_MOD_REGL));
                if (aCaisse == null) {
                    throw new FctlException(BasicSession.TAG_I18N + "reglementBean.caissesKo" + BasicSession.TAG_I18N);
                }

                if (mvtCree) {
                    try {
                        // Temporise pour éviter que le temps et le reste soient
                        // identiques
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                        // Rien à faire
                        ignored.printStackTrace();
                    }
                }
                MvtCaisseBean aMvt = new MvtCaisseBean();

                aMvt.setCD_MOD_REGL(CD_MOD_REGL);
                aMvt.setCD_REGLEMENT(CD_REGLEMENT);
                aMvt.setCD_TYP_MCA(TypMcaBean.ENCAISSEMENT);
                String Comm = "";
                for (int i = 0; i < lstFact.size(); i++) {
                    if (i > 0) {
                        Comm = Comm + "\n";
                    }
					String msg = MessageFormat.format(message.getString("reglementBean.factureDe"), 
									new Object[] {
										ClientBean.getClientBean(
													dbConnect,
													Long.toString(((FactBean) lstFact.get(i))
															.getCD_CLI()), 
													message).toString()
									});
                    Comm = Comm + msg;
                }
                aMvt.setCOMM(Comm);
                aMvt.setDT_MVT(Calendar.getInstance());
                aMvt.setMONTANT(MONTANT);
                aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());

                aMvt.create(dbConnect);
            }
        }

        // Reset pour ne pas faire deux fois les mouvements
        MONTANT_INIT = MONTANT;
     }

    /**
     * Purge des paiements
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @param dateLimite
     *            Date limite de purge : Seront purgés les paiements non
     *            utilisés avant cette date
     * @exception FctlException
     *                En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite)
            throws FctlException {

        int nbEnreg = -1;

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG(
                "dd/MM/yyyy HH:mm:ss");

        String[] reqSQL = new String[1];
/* TODO : PURGE depuis paiement */
        reqSQL[0] = "delete from REGLEMENT where CD_PAIEMENT in " +
        		"(select CD_PAIEMENT from PAIEMENT where DT_PAIEMENT < "
                + DBSession.quoteWith(formatDate.format(dateLimite), '\'')
                + " and (select count(*) from FACT where CD_PAIEMENT = PAIEMENT.CD_PAIEMENT) = 0"
                + " and (select count(*) from MVT_CAISSE where CD_PAIEMENT = PAIEMENT.CD_PAIEMENT) = 0)";

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);

            nbEnreg = res[0];
        } catch (Exception e) {
            System.out.println("Erreur dans Purge des réglements : "
                    + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "reglementBean.purgeKo" + BasicSession.TAG_I18N);
        }

        // Fin de cette transaction
        dbConnect.endTransaction();

        return nbEnreg;
    }
}