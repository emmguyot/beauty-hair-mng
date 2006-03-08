/*
 * Gestion d'une ligne de facture
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
import java.util.Calendar;
import java.util.Locale;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;

/**
 * Gestion d'une ligne de facture
 * Creation date: (17/08/2001 20:08:57)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class HistoPrestBean extends TimeStampBean {
    /**
     * Numéro de facture
     */
    protected long CD_FACT;
    /**
     * Numéro du client
     */
    protected long CD_CLI;
    /**
     * Date de la facture
     */
    protected java.util.Calendar DT_PREST = null;
    /**
     * Collaborateur ayant exécuté la prestation
     */
    protected int CD_COLLAB;
    /**
     * Prestation effectuée
     */
    protected long CD_PREST;
    /**
     * Commentaire / fiche tech
     */
    protected java.lang.String COMM;
    /**
     * Niveau de satisfaction
     */
    protected int NIV_SATISF;
    /**
     * Numéro de ligne dans la facture
     */
    protected int NUM_LIG_FACT;
    /**
     * Prix unitaire
     */
    protected java.math.BigDecimal PRX_UNIT_TTC;
    /**
     * Quantité
     */
    protected java.math.BigDecimal QTE;
    /**
     * TVA de la ligne
     */
    protected java.math.BigDecimal TVA;
    /**
     * TTC de la ligne
     */
    protected java.math.BigDecimal PRX_TOT_TTC;
    /**
     * HT de la ligne
     */
    protected java.math.BigDecimal PRX_TOT_HT;
    /**
     * FactBean constructor comment.
     */
    public HistoPrestBean() {
        super();
    }
    /**
     * HistoPrestBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public HistoPrestBean(ResultSet rs) {
        super(rs);
        try {
            CD_CLI = rs.getLong("CD_CLI");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            NUM_LIG_FACT = rs.getInt("NUM_LIG_FACT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_COLLAB = rs.getInt("CD_COLLAB");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_FACT = rs.getLong("CD_FACT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_PREST = rs.getLong("CD_PREST");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            java.util.Date DtPrest = rs.getDate("DT_PREST");
            if (DtPrest != null) {
                DT_PREST = Calendar.getInstance();
                DT_PREST.setTime(DtPrest);
            }
            else {
                DT_PREST = null;
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            NIV_SATISF = rs.getInt("NIV_SATISF");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            PRX_UNIT_TTC = rs.getBigDecimal("PRX_UNIT_TTC", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            QTE = rs.getBigDecimal("QTE", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            TVA = rs.getBigDecimal("TVA", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            PRX_TOT_TTC = rs.getBigDecimal("PRX_TOT_TTC", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
        try {
            PRX_TOT_HT = rs.getBigDecimal("PRX_TOT_HT", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans HistoPrestBean (RS) : " + e.toString());
            }
        }
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into HISTO_PREST ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_FACT != 0) {
            colonne.append("CD_FACT,");
            valeur.append(CD_FACT);
            valeur.append(",");
        }

        if (NUM_LIG_FACT == 0) {
            /**
             * Numérotation automatique des lignes de facture
             */
            String reqMax = "select max(NUM_LIG_FACT) from HISTO_PREST where CD_FACT=" + CD_FACT;
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                NUM_LIG_FACT = 1; // Par défaut

                while (aRS.next()) {
                    NUM_LIG_FACT = aRS.getInt(1) + 1;
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("Erreur dans reqMax : " + e.toString());
            }
        }
        colonne.append("NUM_LIG_FACT,");
        valeur.append(NUM_LIG_FACT);
        valeur.append(",");

        if (CD_CLI != 0) {
            colonne.append("CD_CLI,");
            valeur.append(CD_CLI);
            valeur.append(",");
        }

        if (CD_COLLAB != 0) {
            colonne.append("CD_COLLAB,");
            valeur.append(CD_COLLAB);
            valeur.append(",");
        }

        if (CD_PREST != 0) {
            colonne.append("CD_PREST,");
            valeur.append(CD_PREST);
            valeur.append(",");
        }

        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append("COMM,");
            valeur.append(DBSession.quoteWith(COMM, '\''));
            valeur.append(",");
        }

        if (DT_PREST != null) {
            colonne.append("DT_PREST,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_PREST.getTime()), '\''));
            valeur.append(",");
        }

        if (NIV_SATISF != 0) {
            colonne.append("NIV_SATISF,");
            valeur.append(NIV_SATISF);
            valeur.append(",");
        }

        if (PRX_UNIT_TTC != null) {
            colonne.append("PRX_UNIT_TTC,");
            valeur.append(PRX_UNIT_TTC.toString());
            valeur.append(",");
        }

        if (QTE != null) {
            colonne.append("QTE,");
            valeur.append(QTE.toString());
            valeur.append(",");
        }

        if (TVA != null) {
            colonne.append("TVA,");
            valeur.append(TVA.toString());
            valeur.append(",");
        }

        if (PRX_TOT_TTC != null) {
            colonne.append("PRX_TOT_TTC,");
            valeur.append(PRX_UNIT_TTC.toString());
            valeur.append(",");
        }

        if (PRX_TOT_HT != null) {
            colonne.append("PRX_TOT_HT,");
            valeur.append(PRX_UNIT_TTC.toString());
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
    public void delete(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("delete from HISTO_PREST ");
        StringBuffer where = new StringBuffer(" where CD_FACT=" + CD_FACT + " and NUM_LIG_FACT=" + NUM_LIG_FACT);

        // Constitue la requete finale
        req.append(where);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:20:23)
     * @return long
     */
    public long getCD_CLI() {
        return CD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:42)
     * @return int
     */
    public int getCD_COLLAB() {
        return CD_COLLAB;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:19:23)
     * @return long
     */
    public long getCD_FACT() {
        return CD_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:11)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_PREST() {
        return DT_PREST;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update HISTO_PREST set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_FACT=" + CD_FACT + " and NUM_LIG_FACT=" + NUM_LIG_FACT);

        colonne.append("CD_CLI=");
        if (CD_CLI != 0) {
            colonne.append(CD_CLI);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_COLLAB=");
        if (CD_COLLAB != 0) {
            colonne.append(CD_COLLAB);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_PREST=");
        if (CD_PREST != 0) {
            colonne.append(CD_PREST);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("COMM=");
        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append(DBSession.quoteWith(COMM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_PREST=");
        if (DT_PREST != null) {
            colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_PREST.getTime()), '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("NIV_SATISF=");
        if (CD_PREST != 0) {
            colonne.append(NIV_SATISF);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_UNIT_TTC=");
        if (PRX_UNIT_TTC != null) {
            colonne.append(PRX_UNIT_TTC.toString());
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("QTE=");
        if (QTE != null) {
            colonne.append(QTE.toString());
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TVA=");
        if (TVA != null) {
            colonne.append(TVA.toString());
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_TOT_TTC=");
        if (PRX_TOT_TTC != null) {
            colonne.append(PRX_TOT_TTC.toString());
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_TOT_HT=");
        if (PRX_TOT_HT != null) {
            colonne.append(PRX_TOT_HT.toString());
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

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
     * Purge de l'historique de prestation des clients
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les historiques avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {

        int nbEnreg = -1;

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String[] reqSQL = new String[1];

        reqSQL[0] = "delete from HISTO_PREST where DT_PREST < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'');

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);

            nbEnreg = res[0];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des historiques de prestations : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "histoPrestBean.purgeKo" + BasicSession.TAG_I18N);
        }

        // Fin de cette transaction
        dbConnect.endTransaction();

        return nbEnreg;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:20:23)
     * @param newCD_CLI long
     */
    public void setCD_CLI(long newCD_CLI) {
        CD_CLI = newCD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:42)
     * @param newCD_COLLAB int
     */
    public void setCD_COLLAB(int newCD_COLLAB) {
        CD_COLLAB = newCD_COLLAB;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:19:23)
     * @param newCD_FACT long
     */
    public void setCD_FACT(long newCD_FACT) {
        CD_FACT = newCD_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:11)
     * @param newDT_PREST java.util.Calendar
     */
    public void setDT_PREST(java.util.Calendar newDT_PREST) {
        DT_PREST = newDT_PREST;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:22)
     * @return long
     */
    public long getCD_PREST() {
        return CD_PREST;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:25:05)
     * @return java.lang.String
     */
    public java.lang.String getCOMM() {
        return COMM;
    }
    /**
     * Création d'une ligne de Facture à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @return HistoPrestBean
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_FACT java.lang.String
     * @param NUM_LIG_FACT java.lang.String
     */
    public static HistoPrestBean getHistoPrestBean(DBSession dbConnect, String CD_FACT, String NUM_LIG_FACT) {
        String reqSQL = "select * from HISTO_PREST where CD_FACT=" + CD_FACT + " and NUM_LIG_FACT=" + NUM_LIG_FACT;
        HistoPrestBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new HistoPrestBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    /**
     * Création d'une ligne de Facture à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @return HistoPrestBean
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_CLI java.lang.String
     * @param CD_MARQUE java.lang.String
     * @param CD_CATEG java.lang.String
     * @param CD_PREST java.lang.String
     */
    public static HistoPrestBean getLastHistoPrest(DBSession dbConnect, String CD_CLI, String CD_MARQUE, String CD_CATEG, String CD_PREST) {

        String reqSQL = "select * from HISTO_PREST, PREST where HISTO_PREST.CD_PREST = PREST.CD_PREST and CD_CLI=" + CD_CLI;
        if (CD_MARQUE != null) {
            reqSQL = reqSQL + " and CD_MARQUE=" + CD_MARQUE;
        }
        if (CD_CATEG != null) {
            reqSQL = reqSQL + " and CD_CATEG_PREST=" + CD_CATEG;
        }
        if (CD_PREST != null) {
            reqSQL = reqSQL + " and PREST.CD_PREST=" + CD_PREST;
        }
        reqSQL = reqSQL + " order by DT_PREST desc limit 1";
        HistoPrestBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new HistoPrestBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:48)
     * @return int
     */
    public int getNIV_SATISF() {
        return NIV_SATISF;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:54:00)
     * @return int
     */
    public int getNUM_LIG_FACT() {
        return NUM_LIG_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_UNIT_TTC() {
        return PRX_UNIT_TTC;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getQTE() {
        return QTE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:20:23)
     * @param newCD_CLI String
     */
    public void setCD_CLI(String newCD_CLI) {

        if ((newCD_CLI != null) && (newCD_CLI.length() != 0)) {
            CD_CLI = Long.parseLong(newCD_CLI);
        }
        else {
            CD_CLI = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:42)
     * @param newCD_COLLAB String
     */
    public void setCD_COLLAB(String newCD_COLLAB) {
        if ((newCD_COLLAB != null) && (newCD_COLLAB.length() != 0)) {
            CD_COLLAB = Integer.parseInt(newCD_COLLAB);
        }
        else {
            CD_COLLAB = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:19:23)
     * @param newCD_FACT String
     */
    public void setCD_FACT(String newCD_FACT) {

        if ((newCD_FACT != null) && (newCD_FACT.length() != 0)) {
            CD_FACT = Long.parseLong(newCD_FACT);
        }
        else {
            CD_FACT = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:22)
     * @param newCD_PREST long
     */
    public void setCD_PREST(long newCD_PREST) {
        CD_PREST = newCD_PREST;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:22)
     * @param newCD_PREST String
     */
    public void setCD_PREST(String newCD_PREST) {
        if ((newCD_PREST != null) && (newCD_PREST.length() != 0)) {
            CD_PREST = Long.parseLong(newCD_PREST);
        }
        else {
            CD_PREST = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:25:05)
     * @param newCOMM java.lang.String
     */
    public void setCOMM(java.lang.String newCOMM) {
        COMM = newCOMM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:11)
     * @param newDT_PREST String
     * @param aLocale Configuration pour parser la date
     * @throws Exception si erreur de conversion
     */
    public void setDT_PREST(String newDT_PREST, Locale aLocale) throws Exception {

        if (newDT_PREST.length() != 0) {
            DT_PREST = Calendar.getInstance();

            java.text.DateFormat formatDate = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, aLocale);
            try {
                DT_PREST.setTime(formatDate.parse(newDT_PREST));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_PREST = null;
                throw (new Exception(BasicSession.TAG_I18N + "histoPrestBean.formatDatePrestation" + BasicSession.TAG_I18N));
            }
        }
        else {
            DT_PREST = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:48)
     * @param newNIV_SATISF int
     */
    public void setNIV_SATISF(int newNIV_SATISF) {
        NIV_SATISF = newNIV_SATISF;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:48)
     * @param newNIV_SATISF String
     */
    public void setNIV_SATISF(String newNIV_SATISF) {
        if ((newNIV_SATISF != null) && (newNIV_SATISF.length() != 0)) {
            NIV_SATISF = Integer.parseInt(newNIV_SATISF);
        }
        else {
            NIV_SATISF = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:54:00)
     * @param newNUM_LIG_FACT int
     */
    public void setNUM_LIG_FACT(int newNUM_LIG_FACT) {
        NUM_LIG_FACT = newNUM_LIG_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:54:00)
     * @param newNUM_LIG_FACT String
     */
    public void setNUM_LIG_FACT(String newNUM_LIG_FACT) {
        if ((newNUM_LIG_FACT != null) && (newNUM_LIG_FACT.length() != 0)) {
            NUM_LIG_FACT = Integer.parseInt(newNUM_LIG_FACT);
        }
        else {
            NUM_LIG_FACT = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @param newPRX_UNIT_TTC String
     */
    public void setPRX_UNIT_TTC(String newPRX_UNIT_TTC) {

        if ((newPRX_UNIT_TTC != null) && (newPRX_UNIT_TTC.length() != 0)) {
            PRX_UNIT_TTC = new BigDecimal(newPRX_UNIT_TTC);
        }
        else {
            PRX_UNIT_TTC = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @param newPRX_UNIT_TTC java.math.BigDecimal
     */
    public void setPRX_UNIT_TTC(java.math.BigDecimal newPRX_UNIT_TTC) {
        PRX_UNIT_TTC = newPRX_UNIT_TTC;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @param newQTE String
     */
    public void setQTE(String newQTE) {
        if ((newQTE != null) && (newQTE.length() != 0)) {
            QTE = new BigDecimal(newQTE);
        }
        else {
            QTE = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @param newQTE java.math.BigDecimal
     */
    public void setQTE(java.math.BigDecimal newQTE) {
        QTE = newQTE;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Indique si cette Historique implique un mouvement de stock
     * @param dbConnect Connexion à la base à utiliser
     * @return true si cette historique génère ou a généré un mouvement de stock
     */
    public boolean hasMvtStk(DBSession dbConnect) {
        PrestBean aPrest = PrestBean.getPrestBean(dbConnect, Long.toString(getCD_PREST()));
        TypVentBean aTypVent = TypVentBean.getTypVentBean(dbConnect, Integer.toString(aPrest.getCD_TYP_VENT()));

        return aTypVent.getMARQUE().equals("O");
    }

    /**
     * @return TVA de la ligne
     */
    public java.math.BigDecimal getTVA() {
        return TVA;
    }

    /**
     * @param decimal TVA de la ligne
     */
    public void setTVA(java.math.BigDecimal decimal) {
        TVA = decimal;
    }

    /**
     * @return HT de la ligne
     */
    public java.math.BigDecimal getPRX_TOT_HT() {
        return PRX_TOT_HT;
    }

    /**
     * @return TTC de la ligne
     */
    public java.math.BigDecimal getPRX_TOT_TTC() {
        return PRX_TOT_TTC;
    }

    /**
     * @param decimal HT de la ligne
     */
    public void setPRX_TOT_HT(java.math.BigDecimal decimal) {
        PRX_TOT_HT = decimal;
    }

    /**
     * @param decimal TTC de la ligne
     */
    public void setPRX_TOT_TTC(java.math.BigDecimal decimal) {
        PRX_TOT_TTC = decimal;
    }

}
