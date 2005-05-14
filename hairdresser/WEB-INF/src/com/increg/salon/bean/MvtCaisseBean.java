/*
 * Bean gérant un mouvement de caisse
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;

/**
 * Mouvement de caisse 
 * Creation date: (25/09/2001 21:09:29)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class MvtCaisseBean extends TimeStampBean {
    /**
     * Code du mode de réglement et donc de la caisse associée
     */
    protected int CD_MOD_REGL;

    /**
     * Date du mouvement
     */
    protected Calendar DT_MVT;

    /**
     * Type du mouvement
     */
    protected int CD_TYP_MCA;

    /**
     * Montant du mouvement
     */
    protected java.math.BigDecimal MONTANT;

    /**
     * Solde de la caisse avant le mouvement
     */
    protected java.math.BigDecimal SOLDE_AVANT;

    /**
     * Commentaire
     */
    protected java.lang.String COMM;

    /**
     * Devise du mouvement
     */
    protected java.lang.String DEVISE;

    /**
     * Paiement associé (si existe)
     */
    protected long CD_PAIEMENT;

    /**
     * FactBean constructor comment.
     */
    public MvtCaisseBean() {
        super();
    }

    /**
     * MvtCaisseBean constructor comment.
     * 
     * @param rs
     *            java.sql.ResultSet
     */
    public MvtCaisseBean(ResultSet rs) {
        super(rs);
        try {
            CD_MOD_REGL = rs.getInt("CD_MOD_REGL");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
        try {
            CD_PAIEMENT = rs.getLong("CD_PAIEMENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_MCA = rs.getInt("CD_TYP_MCA");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }

        try {
            DT_MVT = Calendar.getInstance();
            DT_MVT.setTime(rs.getTimestamp("DT_MVT"));
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
        try {
            MONTANT = rs.getBigDecimal("MONTANT", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
        try {
            SOLDE_AVANT = rs.getBigDecimal("SOLDE_AVANT", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
        try {
            DEVISE = rs.getString("DEVISE");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtCaisseBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into MVT_CAISSE ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_MOD_REGL != 0) {
            colonne.append("CD_MOD_REGL,");
            valeur.append(CD_MOD_REGL);
            valeur.append(",");
        }

        if (CD_PAIEMENT != 0) {
            colonne.append("CD_PAIEMENT,");
            valeur.append(CD_PAIEMENT);
            valeur.append(",");
        }

        if (DT_MVT != null) {
            colonne.append("DT_MVT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_MVT.getTime()), '\''));
            valeur.append(",");
        }

        if (CD_TYP_MCA != 0) {
            colonne.append("CD_TYP_MCA,");
            valeur.append(CD_TYP_MCA);
            valeur.append(",");
        }

        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append("COMM,");
            valeur.append(DBSession.quoteWith(COMM, '\''));
            valeur.append(",");
        }

        if (MONTANT != null) {
            colonne.append("MONTANT,");
            valeur.append(MONTANT.toString());
            valeur.append(",");
        }

        if (SOLDE_AVANT != null) {
            colonne.append("SOLDE_AVANT,");
            valeur.append(SOLDE_AVANT.toString());
            valeur.append(",");
        }

        if (DEVISE != null) {
            colonne.append("DEVISE,");
            valeur.append(DBSession.quoteWith(DEVISE, '\''));
            valeur.append(",");
        }

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_CREAT.getTime()), '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));
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

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        majCaisse(dbConnect, MONTANT);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[0];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
        }

        // Fin de la transaction
        dbConnect.endTransaction();
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("delete from MVT_CAISSE ");
        StringBuffer where = new StringBuffer(" where CD_MOD_REGL=" + CD_MOD_REGL + " and DT_MVT=" + DBSession.quoteWith(formatDate.formatEG(DT_MVT.getTime()), '\''));
        if (CD_PAIEMENT == 0) {
            where.append(" and CD_PAIEMENT is null");
        } else {
            where.append(" and CD_PAIEMENT=" + CD_PAIEMENT);
        }

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // mise à jour de la caisse dans l'autre sens
        majCaisse(dbConnect, MONTANT.negate());

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
     * Insert the method's description here. Creation date: (24/09/2001
     * 15:57:21)
     * 
     * @return long
     */
    public long getCD_PAIEMENT() {
        return CD_PAIEMENT;
    }

    /**
     * Insert the method's description here. Creation date: (19/08/2001
     * 20:55:16)
     * 
     * @return long
     */
    public int getCD_MOD_REGL() {
        return CD_MOD_REGL;
    }

    /**
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:26)
     * 
     * @return int
     */
    public int getCD_TYP_MCA() {
        return CD_TYP_MCA;
    }

    /**
     * Insert the method's description here. Creation date: (18/08/2001
     * 13:25:05)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCOMM() {
        return COMM;
    }

    /**
     * Insert the method's description here. Creation date: (25/09/2001
     * 21:19:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDEVISE() {
        return DEVISE;
    }

    /**
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_MVT() {
        return DT_MVT;
    }

    /**
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getMONTANT() {
        return MONTANT;
    }

    /**
     * Création d'un Bean Mouvement de caisse à partir de sa clé Creation date:
     * (19/08/2001 21:14:20)
     * 
     * @param dbConnect
     *            com.increg.salon.bean.DBSession
     * @param CD_MOD_REGL
     *            java.lang.String
     * @param CD_PAIEMENT
     *            Paiement associé
     * @param DT_MVT
     *            Date du mouvement
     * @return Mouvement associé
     */
    public static MvtCaisseBean getMvtCaisseBean(DBSession dbConnect, String CD_MOD_REGL, String DT_MVT, String CD_PAIEMENT) {
        String reqSQL = "select * from MVT_CAISSE where CD_MOD_REGL=" + CD_MOD_REGL + " and DT_MVT='" + DT_MVT + "'";
        if ((CD_PAIEMENT == null) || (CD_PAIEMENT.equals("")) || (CD_PAIEMENT.equals("0"))) {
            reqSQL = reqSQL + " and CD_PAIEMENT is null";
        } else {
            reqSQL = reqSQL + " and CD_PAIEMENT=" + CD_PAIEMENT;
        }
        MvtCaisseBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new MvtCaisseBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Création d'un Bean Mouvement de caisse à partir d'une date : Le dernier
     * avant la date Creation date: 8 juil. 02
     * 
     * @param dbConnect
     *            com.increg.salon.bean.DBSession
     * @param CD_MOD_REGL
     *            java.lang.String
     * @param DT_MVT
     *            Date du mouvement
     * @return Mouvement associé
     */
    public static MvtCaisseBean getLastMvtCaisseBean(DBSession dbConnect, String CD_MOD_REGL, String DT_MVT) {
        String reqSQL = "select * from MVT_CAISSE where CD_MOD_REGL=" + CD_MOD_REGL + " and DT_MVT <= '" + DT_MVT + "' order by DT_MVT desc LIMIT 1";
        MvtCaisseBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new MvtCaisseBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Obtention des mouvements de caisse lié à un paiement Creation date: 6
     * juil. 02
     * 
     * @param dbConnect
     *            com.increg.salon.bean.DBSession
     * @param CD_PAIEMENT
     *            Paiement associé
     * @return Liste des Mouvements associés
     */
    public static Vector getMvtCaisseBean(DBSession dbConnect, String CD_PAIEMENT) {

        String reqSQL = "select * from MVT_CAISSE where";
        if ((CD_PAIEMENT == null) || (CD_PAIEMENT.equals("")) || (CD_PAIEMENT.equals("0"))) {
            reqSQL = reqSQL + " CD_PAIEMENT is null";
        } else {
            reqSQL = reqSQL + " CD_PAIEMENT=" + CD_PAIEMENT;
        }
        Vector res = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                MvtCaisseBean aMvt = new MvtCaisseBean(aRS);
                res.add(aMvt);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans recherche sur paiement : " + e.toString());
        }
        return res;
    }

    /**
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getSOLDE_AVANT() {
        return SOLDE_AVANT;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update MVT_CAISSE set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_MOD_REGL=" + Integer.toString(CD_MOD_REGL) + " and DT_MVT=" + DBSession.quoteWith(formatDate.formatEG(DT_MVT.getTime()), '\''));

        if (CD_PAIEMENT == 0) {
            where.append(" and CD_PAIEMENT is null");
        } else {
            where.append(" and CD_PAIEMENT = " + Long.toString(CD_PAIEMENT));
        }

        colonne.append("CD_TYP_MCA=");
        if (CD_TYP_MCA != 0) {
            colonne.append(CD_TYP_MCA);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("COMM=");
        if (COMM != null) {
            colonne.append(DBSession.quoteWith(COMM, '\''));
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
        colonne.append(",");

        colonne.append("SOLDE_AVANT=");
        if (SOLDE_AVANT != null) {
            colonne.append(SOLDE_AVANT.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DEVISE=");
        if (DEVISE != null) {
            colonne.append(DBSession.quoteWith(DEVISE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        /**
         * Pas de mise à jour de la caisse !!! Doit être fait si besoin en
         * dehors
         */

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.enregistrementKo" + BasicSession.TAG_I18N));
        }
    }

    /**
     * Calcul le solde de caisse à l'issue du mouvement en fonction du solde de
     * départ et de la quantité
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @param CD_TYP_MCA
     *            Code de la caisse
     * @param soldeAvant
     *            Solde avant le mouvement
     * @param qte
     *            Quantité mouvementée
     * @return Solde final
     */
    public static BigDecimal calculSolde(DBSession dbConnect, int CD_TYP_MCA, BigDecimal soldeAvant, BigDecimal qte) {

        BigDecimal solde = null;

        // Tiens compte du sens du mouvement
        TypMcaBean myTypMca = TypMcaBean.getTypMcaBean(dbConnect, Integer.toString(CD_TYP_MCA));
        if (myTypMca.getSENS_MCA().equals(TypMcaBean.SENS_SORTIE)) {
            qte = qte.negate();
            // Mise à jour du solde
            solde = soldeAvant.add(qte);
        } else if (myTypMca.getSENS_MCA().equals(TypMcaBean.SENS_ENTREE)) {
            // Mise à jour du solde
            solde = soldeAvant.add(qte);
        } else if (myTypMca.getSENS_MCA().equals(TypMcaBean.SENS_INVENTAIRE)) {
            // Mise à jour du solde
            solde = qte;
        } else {
            System.out.println("Type de mouvement non géré. La caisse n'est pas calculée");
        }

        return solde;
    }

    /**
     * Construit la requete de maj des données articles Creation date:
     * (17/09/2001 22:19:13)
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @param qte
     *            java.math.BigDecimal
     * @throws SQLException
     *             En cas d'erreur durant la mise à jour
     */
    protected void majCaisse(DBSession dbConnect, BigDecimal qte) throws SQLException {

        CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect, Integer.toString(CD_MOD_REGL));

        aCaisse.setSOLDE(calculSolde(dbConnect, getCD_TYP_MCA(), aCaisse.getSOLDE(), qte));

        aCaisse.maj(dbConnect);
    }

    /**
     * Insert the method's description here. Creation date: (24/09/2001
     * 15:57:21)
     * 
     * @param newCD_PAIEMENT
     *            long
     */
    public void setCD_PAIEMENT(long newCD_PAIEMENT) {
        CD_PAIEMENT = newCD_PAIEMENT;
    }

    /**
     * Insert the method's description here. Creation date: (24/09/2001
     * 15:57:21)
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
     * Insert the method's description here. Creation date: (19/08/2001
     * 20:55:16)
     * 
     * @param newCD_MOD_REGL
     *            int
     */
    public void setCD_MOD_REGL(int newCD_MOD_REGL) {
        CD_MOD_REGL = newCD_MOD_REGL;
    }

    /**
     * Insert the method's description here. Creation date: (19/08/2001
     * 20:55:16)
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
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @param newCD_TYP_MCA
     *            int
     */
    public void setCD_TYP_MCA(int newCD_TYP_MCA) {
        CD_TYP_MCA = newCD_TYP_MCA;
    }

    /**
     * Insert the method's description here. Creation date: (01/09/2001
     * 15:36:14)
     * 
     * @param newCD_TYP_MCA
     *            String
     */
    public void setCD_TYP_MCA(String newCD_TYP_MCA) {
        if ((newCD_TYP_MCA != null) && (newCD_TYP_MCA.length() != 0)) {
            CD_TYP_MCA = Integer.parseInt(newCD_TYP_MCA);
        } else {
            CD_TYP_MCA = 0;
        }
    }

    /**
     * Insert the method's description here. Creation date: (18/08/2001
     * 13:25:05)
     * 
     * @param newCOMM
     *            java.lang.String
     */
    public void setCOMM(java.lang.String newCOMM) {
        COMM = newCOMM;
    }

    /**
     * Insert the method's description here. Creation date: (25/09/2001
     * 21:19:11)
     * 
     * @param newDEVISE
     *            java.lang.String
     */
    public void setDEVISE(java.lang.String newDEVISE) {
        DEVISE = newDEVISE;
    }

    /**
     * Insert the method's description here. 
     * Creation date: (16/09/2001 19:31:27)
     * @param newDT_MVT String
     * @param aLocale Configuration pour parser la date
     * @throws Exception En cas d'erreur de format
     */
    public void setDT_MVT(String newDT_MVT, Locale aLocale) throws Exception {

        if ((newDT_MVT != null) && (newDT_MVT.length() != 0)) {
            DT_MVT = Calendar.getInstance();

            java.text.DateFormat formatDate = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.MEDIUM, aLocale);
            try {
                DT_MVT.setTime(formatDate.parse(newDT_MVT));
            } catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_MVT = null;
                throw (new Exception(BasicSession.TAG_I18N + "mvtCaisseBean.formatDateMvt" + BasicSession.TAG_I18N));
            }
        } else {
            DT_MVT = null;
        }
    }

    /**
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @param newDT_MVT
     *            java.util.Calendar
     */
    public void setDT_MVT(java.util.Calendar newDT_MVT) {
        DT_MVT = newDT_MVT;
    }

    /**
     * Insert the method's description here. Creation date: (01/09/2001
     * 14:59:04)
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
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @param newMONTANT
     *            java.math.BigDecimal
     */
    public void setMONTANT(java.math.BigDecimal newMONTANT) {
        MONTANT = newMONTANT;
    }

    /**
     * Insert the method's description here. Creation date: (01/09/2001
     * 14:59:04)
     * 
     * @param newSOLDE_AVANT
     *            String
     */
    public void setSOLDE_AVANT(String newSOLDE_AVANT) {

        if ((newSOLDE_AVANT != null) && (newSOLDE_AVANT.length() != 0)) {
            SOLDE_AVANT = new BigDecimal(newSOLDE_AVANT);
        } else {
            SOLDE_AVANT = null;
        }
    }

    /**
     * Insert the method's description here. Creation date: (16/09/2001
     * 19:31:27)
     * 
     * @param newSOLDE_AVANT
     *            java.math.BigDecimal
     */
    public void setSOLDE_AVANT(java.math.BigDecimal newSOLDE_AVANT) {
        SOLDE_AVANT = newSOLDE_AVANT;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Mise à jour des soldes avant et vérification du solde final d'une caisse
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @param CD_MOD_REGL
     *            Mode de règlement indiquant la caisse concernée
     * @param DT_DEBUT
     *            Date de début de vérification
     * @return Indicateur si la caisse était correcte ou pas
     * @throws SQLException
     *             En cas d'erreur à la mise à jour
     */
    public static boolean checkAndFix(DBSession dbConnect, String CD_MOD_REGL, String DT_DEBUT) throws SQLException {

        String modReglSup = "";

        if (Long.parseLong(CD_MOD_REGL) == ModReglBean.MOD_REGL_ESP) {
            modReglSup = "," + Long.toString(ModReglBean.MOD_REGL_ESP_FRF);
        } else if (Long.parseLong(CD_MOD_REGL) == ModReglBean.MOD_REGL_ESP_FRF) {
            modReglSup = "," + Long.toString(ModReglBean.MOD_REGL_ESP);
        } else if (Long.parseLong(CD_MOD_REGL) == ModReglBean.MOD_REGL_CHQ) {
            modReglSup = "," + Long.toString(ModReglBean.MOD_REGL_CHQ_FRF);
        } else if (Long.parseLong(CD_MOD_REGL) == ModReglBean.MOD_REGL_CHQ_FRF) {
            modReglSup = "," + Long.toString(ModReglBean.MOD_REGL_CHQ);
        }

        // Recherche tous les mouvements depuis la date
        String reqSQL = "select * from MVT_CAISSE where CD_MOD_REGL in (" + CD_MOD_REGL + modReglSup + ")" + " and DT_MVT >= " + DBSession.quoteWith(DT_DEBUT, '\'') + " order by DT_MVT ASC";

        BigDecimal solde = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                MvtCaisseBean aMvt = new MvtCaisseBean(aRS);

                if (solde == null) {
                    // Premier mouvement : Init
                    solde = aMvt.getSOLDE_AVANT();
                } else if (!solde.equals(aMvt.getSOLDE_AVANT())) {
                    // C'est pas le bon solde, on le met à jour
                    aMvt.setSOLDE_AVANT(solde);
                    aMvt.maj(dbConnect);
                }

                // Calcul le solde après le mouvement
                solde = calculSolde(dbConnect, aMvt.getCD_TYP_MCA(), solde, aMvt.getMONTANT());

            }
            aRS.close();
        } catch (SQLException e) {
            System.out.println("Erreur dans checkAndFix : " + e.toString());
            throw e;
        }

        CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect, CD_MOD_REGL);
        if (aCaisse.getSOLDE().equals(solde)) {
            return true;
        } else {
            aCaisse.setSOLDE(solde);
            aCaisse.maj(dbConnect);
            return false;
        }
    }

    /**
     * Purge des mouvements de caisse
     * 
     * @param dbConnect
     *            Connexion à la base à utiliser
     * @param dateLimite
     *            Date limite de purge : Seront purgés les mouvements avant
     *            cette date
     * @exception FctlException
     *                En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {

        int nbEnreg = -1;

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        // Calcul de solde de début
        String reqSQLCaisse = "select * from CAISSE";

        try {
            ResultSet rs = dbConnect.doRequest(reqSQLCaisse);

            while (rs.next()) {
                CaisseBean aCaisse = new CaisseBean(rs);

                String reqSQLSolde = "select * from MVT_CAISSE where DT_MVT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'') + " and CD_MOD_REGL = " + aCaisse.getCD_MOD_REGL()
                        + " order by DT_MVT desc" + " limit 1";

                ResultSet rsMvt = dbConnect.doRequest(reqSQLSolde);

                if (rsMvt.next()) {
                    // Des mouvements de cette caisse vont être purgés
                    // Il faut mettre à jour le solde de la caisse
                    MvtCaisseBean lastMvt = new MvtCaisseBean(rsMvt);

                    aCaisse.setSOLDE_DEBUT(calculSolde(dbConnect, lastMvt.getCD_TYP_MCA(), lastMvt.getSOLDE_AVANT(), lastMvt.getMONTANT()));
                    aCaisse.maj(dbConnect);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans mise à jour solde initial de la caisse : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "mvtCaisseBean.purgeKo" + BasicSession.TAG_I18N);
        }

        String[] reqSQL = new String[1];

        reqSQL[0] = "delete from MVT_CAISSE where DT_MVT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'');

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);

            nbEnreg = res[0];
        } catch (Exception e) {
            System.out.println("Erreur dans Purge des mouvements de caisse : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "mvtCaisseBean.purgeKo" + BasicSession.TAG_I18N);
        }

        // Fin de cette transaction
        dbConnect.endTransaction();

        return nbEnreg;
    }

}