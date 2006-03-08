/*
 * Gestion d'un paiement (d'une ou plusieurs factures)
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;

/**
 * Gestion d'un paiement (d'une ou plusieurs factures)
 * Creation date: (17/08/2001 20:08:57)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class PaiementBean extends TimeStampBean {
    /**
     * Code du paiement
     */
    protected long CD_PAIEMENT;

    /**
     * Montant total du paiement
     */
    protected java.math.BigDecimal PRX_TOT_TTC;

    /**
     * Montant total avant modification
     */
    protected java.math.BigDecimal PRX_TOT_TTC_INIT;

    /**
     * Mode de règlement
     */
    protected int CD_MOD_REGL;

    /**
     * Mode de règlement avant changement
     */
    protected int CD_MOD_REGL_INIT;

    /**
     * Date du paiement
     */
    protected java.util.Calendar DT_PAIEMENT;

    /**
     * PaiementBean constructor comment.
     * @param rb Messages à utiliser
     */
    public PaiementBean(ResourceBundle rb) {
        super(rb);
    }

    /**
     * PaiementBean constructor comment.
     * 
     * @param rs java.sql.ResultSet
     * @param rb Messages à utiliser
     */
    public PaiementBean(ResultSet rs, ResourceBundle rb) {
        super(rs, rb);
        try {
            CD_PAIEMENT = rs.getLong("CD_PAIEMENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PaiementBean (RS) : "
                        + e.toString());
            }
        }
        try {
            CD_MOD_REGL = rs.getInt("CD_MOD_REGL");
            CD_MOD_REGL_INIT = CD_MOD_REGL;
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PaiementBean (RS) : "
                        + e.toString());
            }
        }
        try {
            java.util.Date DtPaiement = rs.getDate("DT_PAIEMENT");
            if (DtPaiement != null) {
                DT_PAIEMENT = Calendar.getInstance();
                DT_PAIEMENT.setTime(DtPaiement);
            } else {
                DT_PAIEMENT = null;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PaiementBean (RS) : "
                        + e.toString());
            }
        }
        try {
            PRX_TOT_TTC = rs.getBigDecimal("PRX_TOT_TTC", 2);
            PRX_TOT_TTC_INIT = PRX_TOT_TTC;
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PaiementBean (RS) : "
                        + e.toString());
            }
        }
    }

    /**
     * Calcul le total du paiement compte tenu des factures en base Creation
     * date: (20/08/2001 21:25:47)
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @exception Exception
     *                En cas d'erreur de mise à jour
     */
    public void calculTotaux(DBSession dbConnect) throws Exception {

        PRX_TOT_TTC = null;

        // Calcul en direct
        String reqSQL = "select sum(PRX_TOT_TTC) from FACT where CD_PAIEMENT="
                + CD_PAIEMENT;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);
            PRX_TOT_TTC = new BigDecimal(0);
            PRX_TOT_TTC.setScale(2);

            while (aRS.next()) {
                BigDecimal tempTot = aRS.getBigDecimal(1, 2);
                if (tempTot != null) {
                    PRX_TOT_TTC = tempTot;
                }
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans calculTotaux (PaiementBean) : "
                    + e.toString());
        }

        if (PRX_TOT_TTC == null) {
            PRX_TOT_TTC = new BigDecimal(0);
            PRX_TOT_TTC.setScale(2);
        }

        try {
            // Sauvegarde en base
            maj(dbConnect);
        } catch (Exception e) {
            System.out.println("PaiementBean==>Erreur dans calculTotaux : "
                    + e.toString());
            throw (e);
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG(
                "dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into PAIEMENT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_PAIEMENT == 0) {
            /**
             * Numérotation automatique des codes factures
             */
            String reqMax = "select nextval('SEQ_PAIEMENT')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_PAIEMENT = 1; // Par défaut

                while (aRS.next()) {
                    CD_PAIEMENT = aRS.getLong(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_PAIEMENT,");
        valeur.append(CD_PAIEMENT);
        valeur.append(",");

        if (CD_MOD_REGL != 0) {
            colonne.append("CD_MOD_REGL,");
            valeur.append(CD_MOD_REGL);
            valeur.append(",");
        }

        if (DT_PAIEMENT != null) {
            colonne.append("DT_PAIEMENT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_PAIEMENT
                    .getTime()), '\''));
            valeur.append(",");
        }

        if (PRX_TOT_TTC != null) {
            colonne.append("PRX_TOT_TTC,");
            valeur.append(PRX_TOT_TTC.toString());
            valeur.append(",");
        }

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_CREAT
                    .getTime()), '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF
                    .getTime()), '\''));
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

        /**
         * Ne mouvemente pas, car il est impossible que des factures soient déjà
         * ratachées (FK)
         */

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
        StringBuffer req = new StringBuffer("delete from PAIEMENT ");
        StringBuffer where = new StringBuffer(" where CD_PAIEMENT="
                + CD_PAIEMENT);

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Fait les mouvements de stock
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
        StringBuffer req = new StringBuffer("delete from PAIEMENT ");
        StringBuffer where = new StringBuffer(" where CD_PAIEMENT="
                + CD_PAIEMENT);

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Suppression des mouvements de caisse
        Vector listeMvtPaiement = MvtCaisseBean.getMvtCaisseBean(dbConnect,
                Long.toString(CD_PAIEMENT));
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
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:27:59)
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_PAIEMENT() {
        return DT_PAIEMENT;
    }

    /**
     * Donne la valeur par défaut de la date de Paiement Creation date:
     * (17/08/2001 21:27:59)
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_PAIEMENT_defaut() {
        Calendar jour = Calendar.getInstance();
        jour.clear(Calendar.HOUR_OF_DAY);
        jour.clear(Calendar.HOUR);
        jour.clear(Calendar.MINUTE);
        jour.clear(Calendar.SECOND);
        jour.clear(Calendar.MILLISECOND);
        return jour;
    }

    /**
     * Création d'un Bean Facture à partir de sa clé Creation date: (18/08/2001
     * 17:05:45)
     * 
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_PAIEMENT java.lang.String
     * @param rb Messages à utiliser
     * @return Paiement correspondant au code
     */
    public static PaiementBean getPaiementBean(DBSession dbConnect, String CD_PAIEMENT, ResourceBundle rb) {
        String reqSQL = "select * from PAIEMENT where CD_PAIEMENT="
                + CD_PAIEMENT;
        PaiementBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new PaiementBean(aRS, rb);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : "
                    + e.toString());
        }
        return res;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_TOT_TTC() {
        return PRX_TOT_TTC;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_TOT_TTC_Franc() {
        if (PRX_TOT_TTC != null) {
            return PRX_TOT_TTC.multiply(new BigDecimal(6.55957)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
        } else {
            return null;
        }
    }

    /**
     * Insert the method's description here. Creation date: (20/08/2001
     * 21:42:17)
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotPrest(DBSession dbConnect) {

        BigDecimal totPrest = new BigDecimal(0);
        totPrest.setScale(2);
        // Calcul en direct
        String reqSQL = "select sum(QTE*PRX_UNIT_TTC) from HISTO_PREST, FACT where HISTO_PREST.CD_FACT = FACT.CD_FACT and FACT.CD_PAIEMENT="
                + CD_PAIEMENT;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                totPrest = aRS.getBigDecimal(1, 2);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans getTotPrest : " + e.toString());
        }
        return totPrest;

    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException, FctlException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG(
                "dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update PAIEMENT set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_PAIEMENT="
                + CD_PAIEMENT);

        colonne.append("CD_MOD_REGL=");
        if (CD_MOD_REGL != 0) {
            colonne.append(CD_MOD_REGL);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_PAIEMENT=");
        if (DT_PAIEMENT != null) {
            colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_PAIEMENT
                    .getTime()), '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_TOT_TTC=");
        if (PRX_TOT_TTC != null) {
            colonne.append(PRX_TOT_TTC.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF
                .getTime()), '\''));

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
     * Insert the method's description here. 
     * Creation date: (17/08/2001 21:27:59)
     * 
     * @param newDT_PAIEMENT String
     * @param aLocale locale à utiliser pour le format de la date 
     * @throws Exception Format de date erroné
     */
    public void setDT_PAIEMENT(String newDT_PAIEMENT, Locale aLocale) throws Exception {

        if ((newDT_PAIEMENT != null) && (newDT_PAIEMENT.length() != 0)) {
            DT_PAIEMENT = Calendar.getInstance();

            java.text.DateFormat formatDate = java.text.DateFormat
                    .getDateInstance(java.text.DateFormat.SHORT, aLocale);
            try {
                DT_PAIEMENT.setTime(formatDate.parse(newDT_PAIEMENT));
            } catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_PAIEMENT = null;
                throw (new Exception(BasicSession.TAG_I18N + "paiementBean.formatDatePaiement" + BasicSession.TAG_I18N));
            }
        } else {
            DT_PAIEMENT = null;
        }
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:27:59)
     * 
     * @param newDT_PAIEMENT
     *            java.util.Calendar
     */
    public void setDT_PAIEMENT(java.util.Calendar newDT_PAIEMENT) {
        DT_PAIEMENT = newDT_PAIEMENT;
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @param newPRX_TOT_TTC
     *            String
     */
    public void setPRX_TOT_TTC(String newPRX_TOT_TTC) {

        if ((newPRX_TOT_TTC != null) && (newPRX_TOT_TTC.length() != 0)) {
            PRX_TOT_TTC = new BigDecimal(newPRX_TOT_TTC);
        } else {
            PRX_TOT_TTC = null;
        }
    }

    /**
     * Insert the method's description here. Creation date: (17/08/2001
     * 21:26:51)
     * 
     * @param newPRX_TOT_TTC
     *            java.math.BigDecimal
     */
    public void setPRX_TOT_TTC(java.math.BigDecimal newPRX_TOT_TTC) {
        PRX_TOT_TTC = newPRX_TOT_TTC;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Donne les factures concernées par ce paiement Creation date: (18/08/2001
     * 15:24:27)
     * 
     * @return java.util.Vector
     * @param dbConnect
     *            DBSession
     */
    public java.util.Vector getFact(DBSession dbConnect) {

        Vector lignes = new Vector();
        /**
         * Chargement des lignes de facture
         */
        String reqSQL = "select * from FACT where CD_PAIEMENT=" + CD_PAIEMENT;

        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                lignes.add(new FactBean(aRS, message));
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans getLignes : " + e.toString());
        }

        return lignes;
    }

    /**
     * Calcul le montant réel en caisse (cas du franc qui n'a pas de centimes =>
     * Arrondi) Creation date: (30/09/2001 10:28:50)
     * 
     * @param CD_MOD_REGL
     *            int
     * @param montantEuro
     *            BigDecimal
     * @return Montant total en euro du paiement
     */
    public BigDecimal getMontantReel(int CD_MOD_REGL, BigDecimal montantEuro) {

        // Applique la conversion si nécessaire
        if (CD_MOD_REGL == ModReglBean.MOD_REGL_CHQ_FRF) {
            // Conversion légale en Francs puis en Euro
            // Théoriquement : pas d'erreur de conversion
            return montantEuro.multiply(new BigDecimal(6.55957)).setScale(2,
                    BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(6.55957),
                    2, BigDecimal.ROUND_HALF_UP);
        } else if (CD_MOD_REGL == ModReglBean.MOD_REGL_ESP_FRF) {
            // Arrondi aux 5 centimes
            BigDecimal montantFrf = montantEuro.multiply(
                    new BigDecimal(6.55957)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            /**
             * Pour arrondir à 5 centimes : Multiplication par 2 Arrondi à 1
             * chiffre après la virgule Division par 2
             */
            montantFrf = montantFrf.multiply(new BigDecimal("2")).setScale(1,
                    BigDecimal.ROUND_HALF_UP);
            montantFrf = montantFrf.divide(new BigDecimal("2"), 2,
                    BigDecimal.ROUND_HALF_UP);
            return montantFrf.divide(new BigDecimal(6.55957), 2,
                    BigDecimal.ROUND_HALF_UP);
        } else {
            // Montant en Euro
            return montantEuro;
        }
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

        if (PRX_TOT_TTC == null) {
            PRX_TOT_TTC = new BigDecimal(0);
            PRX_TOT_TTC.setScale(2);
        }
        if (PRX_TOT_TTC_INIT == null) {
            PRX_TOT_TTC_INIT = new BigDecimal(0);
            PRX_TOT_TTC_INIT.setScale(2);
        }

        Vector lstFact = getFact(dbConnect);
        boolean mvtCree = false;

        if (delete || (PRX_TOT_TTC.compareTo(PRX_TOT_TTC_INIT) != 0)
                || (CD_MOD_REGL_INIT != CD_MOD_REGL)) {
            // Annulation du précédent
            if (PRX_TOT_TTC_INIT.compareTo(new BigDecimal(0)) != 0) {
                CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect,
                        Integer.toString(CD_MOD_REGL_INIT));
                if (aCaisse == null) {
                    throw new FctlException(BasicSession.TAG_I18N + "paiementBean.caissesKo" + BasicSession.TAG_I18N);
                }
                MvtCaisseBean aMvt = new MvtCaisseBean();

                aMvt.setCD_MOD_REGL(CD_MOD_REGL_INIT);
                aMvt.setCD_PAIEMENT(CD_PAIEMENT);
                aMvt.setCD_TYP_MCA(TypMcaBean.ENCAISSEMENT);
                String Comm = "Paiement annulé";
                aMvt.setCOMM(Comm);
                aMvt.setDT_MVT(Calendar.getInstance());
                aMvt.setMONTANT(getMontantReel(CD_MOD_REGL_INIT,
                        PRX_TOT_TTC_INIT).negate());
                aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());

                aMvt.create(dbConnect);
                mvtCree = true;
            }
        }
        if (!delete) {
            if (((PRX_TOT_TTC.compareTo(PRX_TOT_TTC_INIT) != 0) || (CD_MOD_REGL_INIT != CD_MOD_REGL))
                    && (PRX_TOT_TTC.compareTo(new BigDecimal(0)) != 0)) {
                CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect,
                        Integer.toString(CD_MOD_REGL));
                if (aCaisse == null) {
                    throw new FctlException(BasicSession.TAG_I18N + "paiementBean.caissesKo" + BasicSession.TAG_I18N);
                }

                if (mvtCree && (CD_MOD_REGL == CD_MOD_REGL_INIT)) {
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
                aMvt.setCD_PAIEMENT(CD_PAIEMENT);
                aMvt.setCD_TYP_MCA(TypMcaBean.ENCAISSEMENT);
                String Comm = "";
                for (int i = 0; i < lstFact.size(); i++) {
                    if (i > 0) {
                        Comm = Comm + "\n";
                    }
					String msg = MessageFormat.format(message.getString("paiementBean.factureDe"), 
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
                aMvt.setMONTANT(getMontantReel(CD_MOD_REGL, PRX_TOT_TTC));
                aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());

                aMvt.create(dbConnect);
            }
        }

        // Reset pour ne pas faire deux fois les mouvements
        CD_MOD_REGL_INIT = CD_MOD_REGL;
        PRX_TOT_TTC_INIT = PRX_TOT_TTC;
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

        reqSQL[0] = "delete from PAIEMENT where DT_PAIEMENT < "
                + DBSession.quoteWith(formatDate.format(dateLimite), '\'')
                + " and (select count(*) from FACT where CD_PAIEMENT = PAIEMENT.CD_PAIEMENT) = 0"
                + " and (select count(*) from MVT_CAISSE where CD_PAIEMENT = PAIEMENT.CD_PAIEMENT) = 0";

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);

            nbEnreg = res[0];
        } catch (Exception e) {
            System.out.println("Erreur dans Purge des paiements : "
                    + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "paiementBean.purgeKo" + BasicSession.TAG_I18N);
        }

        // Fin de cette transaction
        dbConnect.endTransaction();

        return nbEnreg;
    }

}