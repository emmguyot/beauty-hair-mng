/*
 * Bean assurant la gestion d'un collaborateur (Personne assurant les prestations)
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
package com.increg.salon.bean;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;

/**
 * Bean de gestion de collaborateur
 * Creation date: (22/08/2001 13:17:38)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CollabBean extends TimeStampBean {
    /**
     * Civilité du collab M. Mme, Mle
     */
    protected String CIVILITE;
    /**
     * Nom du collab
     */
    protected String NOM;
    /**
     * Prénom du collab
     */
    protected String PRENOM;
    /**
     * Adresse du collab
     */
    protected String RUE;
    /**
     * Ville du collab
     */
    protected String VILLE;
    /**
     * Code postal du collab
     */
    protected String CD_POSTAL;
    
    /**
     * Téléphone du collab
     */
    protected String TEL;
    /**
     * Portable du collab
     */
    protected String PORTABLE;
    /**
     * Email du collab
     */
    protected String EMAIL;
    /**
     * Catégorie du collab
     */
    protected String CATEG;
    /**
     * Identifiant du collab
     */
    protected int CD_COLLAB;
    /**
     * Code fonction
     */
    protected int CD_FCT;
    /**
     * Type de contrat CDI, ...
     */
    protected int CD_TYP_CONTR;
    /**
     * Coefficient
     */
    protected String COEF;
    /**
     * Date de naissance
     */
    protected Calendar DT_NAIS;
    /**
     * Echelon
     */
    protected String ECHELON;
    /**
     * Numéro de sécurité social
     */
    protected String NUM_SECU;
    /**
     * Nombre d'heures prévues par jour
     */
    protected BigDecimal QUOTA_HEURE;
    /**
     * Collaborateur valide ?
     */
    protected String INDIC_VALID;
    
    /**
     * CollabBean constructor comment.
     */
    public CollabBean() {
        super();
        QUOTA_HEURE = new BigDecimal("7.00");
    }
    /**
     * CollabBean à partir d'un RecordSet.
     * @param rs Jeu d'enregistrements contenant les infos
     */
    public CollabBean(ResultSet rs) {
        super(rs);
        try {
            CATEG = rs.getString("CATEG");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            CD_COLLAB = rs.getInt("CD_COLLAB");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            CD_FCT = rs.getInt("CD_FCT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            CD_POSTAL = rs.getString("CD_POSTAL");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_CONTR = rs.getInt("CD_TYP_CONTR");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            CIVILITE = rs.getString("CIVILITE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            COEF = rs.getString("COEF");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            Date DtNais = rs.getDate("DT_NAIS");
            if (DtNais != null) {
                DT_NAIS = Calendar.getInstance();
                DT_NAIS.setTime(DtNais);
            }
            else {
                DT_NAIS = null;
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            ECHELON = rs.getString("ECHELON");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            EMAIL = rs.getString("EMAIL");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            NOM = rs.getString("NOM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            NUM_SECU = rs.getString("NUM_SECU");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            PORTABLE = rs.getString("PORTABLE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            PRENOM = rs.getString("PRENOM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            RUE = rs.getString("RUE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            TEL = rs.getString("TEL");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            VILLE = rs.getString("VILLE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            QUOTA_HEURE = rs.getBigDecimal("QUOTA_HEURE", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
        try {
            INDIC_VALID = rs.getString("INDIC_VALID");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans CollabBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws java.sql.SQLException {

        SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into COLLAB ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_COLLAB == 0) {
            /**
             * Numérotation automatique des codes clients
             */
            String reqMax = "select nextval('seq_collab')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_COLLAB = 1; // Par défaut

                while (aRS.next()) {
                    CD_COLLAB = aRS.getInt(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_COLLAB,");
        valeur.append(CD_COLLAB);
        valeur.append(",");

        if ((CIVILITE != null) && (CIVILITE.length() != 0)) {
            colonne.append("CIVILITE,");
            valeur.append(DBSession.quoteWith(CIVILITE, '\''));
            valeur.append(",");
        }

        if ((NOM != null) && (NOM.length() != 0)) {
            colonne.append("NOM,");
            valeur.append(DBSession.quoteWith(NOM, '\''));
            valeur.append(",");
        }

        if ((PRENOM != null) && (PRENOM.length() != 0)) {
            colonne.append("PRENOM,");
            valeur.append(DBSession.quoteWith(PRENOM, '\''));
            valeur.append(",");
        }

        if ((RUE != null) && (RUE.length() != 0)) {
            colonne.append("RUE,");
            valeur.append(DBSession.quoteWith(RUE, '\''));
            valeur.append(",");
        }

        if ((VILLE != null) && (VILLE.length() != 0)) {
            colonne.append("VILLE,");
            valeur.append(DBSession.quoteWith(VILLE, '\''));
            valeur.append(",");
        }

        if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
            colonne.append("CD_POSTAL,");
            valeur.append(DBSession.quoteWith(CD_POSTAL, '\''));
            valeur.append(",");
        }

        if ((TEL != null) && (TEL.length() != 0)) {
            colonne.append("TEL,");
            valeur.append(DBSession.quoteWith(TEL, '\''));
            valeur.append(",");
        }

        if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
            colonne.append("PORTABLE,");
            valeur.append(DBSession.quoteWith(PORTABLE, '\''));
            valeur.append(",");
        }

        if ((EMAIL != null) && (EMAIL.length() != 0)) {
            colonne.append("EMAIL,");
            valeur.append(DBSession.quoteWith(EMAIL, '\''));
            valeur.append(",");
        }

        if (DT_NAIS != null) {
            colonne.append("DT_NAIS,");
            valeur.append(
                DBSession.quoteWith(formatDate.formatEG(DT_NAIS.getTime()), '\''));
            valeur.append(",");
        }

        if ((NUM_SECU != null) && (NUM_SECU.length() != 0)) {
            colonne.append("NUM_SECU,");
            valeur.append(DBSession.quoteWith(NUM_SECU, '\''));
            valeur.append(",");
        }

        if (CD_FCT != 0) {
            colonne.append("CD_FCT,");
            valeur.append(CD_FCT);
            valeur.append(",");
        }

        if (CD_TYP_CONTR != 0) {
            colonne.append("CD_TYP_CONTR,");
            valeur.append(CD_TYP_CONTR);
            valeur.append(",");
        }

        if ((CATEG != null) && (CATEG.length() != 0)) {
            colonne.append("CATEG,");
            valeur.append(DBSession.quoteWith(CATEG, '\''));
            valeur.append(",");
        }

        if ((ECHELON != null) && (ECHELON.length() != 0)) {
            colonne.append("ECHELON,");
            valeur.append(DBSession.quoteWith(ECHELON, '\''));
            valeur.append(",");
        }

        if ((COEF != null) && (COEF.length() != 0)) {
            colonne.append("COEF,");
            valeur.append(DBSession.quoteWith(COEF, '\''));
            valeur.append(",");
        }

        if (QUOTA_HEURE != null) {
            colonne.append("QUOTA_HEURE,");
            valeur.append(QUOTA_HEURE);
            valeur.append(",");
        }

        if ((INDIC_VALID != null) && (INDIC_VALID.length() != 0)) {
            colonne.append("INDIC_VALID,");
            valeur.append(DBSession.quoteWith(INDIC_VALID, '\''));
            valeur.append(",");
        }

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(
                DBSession.quoteWith(formatDate.formatEG(DT_CREAT.getTime()), '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(
                DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));
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
    public void delete(DBSession dbConnect) throws java.sql.SQLException {

        StringBuffer req = new StringBuffer("delete from COLLAB ");
        StringBuffer where = new StringBuffer(" where CD_COLLAB=" + CD_COLLAB);

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
     * Creation date: (18/07/2001 22:48:09)
     * @return java.lang.String
     */
    public java.lang.String getCD_POSTAL() {
        if (CD_POSTAL == null) {
            return "";
        }
        else {
            return CD_POSTAL;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:26)
     * @return java.lang.String
     */
    public java.lang.String getCIVILITE() {
        if (CIVILITE == null) {
            return "";
        }
        else {
            return CIVILITE;
        }
    }

    /**
     * Création d'un Bean collab à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_COLLAB java.lang.String
     * @return Bean créé
     */
    public static CollabBean getCollabBean(DBSession dbConnect, String CD_COLLAB) {
        String reqSQL = "select * from COLLAB where CD_COLLAB=" + CD_COLLAB;
        CollabBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new CollabBean(aRS);
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
     * Creation date: (18/07/2001 22:48:46)
     * @return java.lang.String
     */
    public java.lang.String getEMAIL() {
        if (EMAIL == null) {
            return "";
        }
        else {
            return EMAIL;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:51)
     * @return java.lang.String
     */
    public java.lang.String getNOM() {

        if (NOM == null) {
            return "";
        }
        else {
            return NOM;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:37)
     * @return java.lang.String
     */
    public java.lang.String getPORTABLE() {

        if (PORTABLE == null) {
            return "";
        }
        else {
            return PORTABLE;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:09)
     * @return java.lang.String
     */
    public java.lang.String getPRENOM() {

        if (PRENOM == null) {
            return "";
        }
        else {
            return PRENOM;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:24)
     * @return java.lang.String
     */
    public java.lang.String getRUE() {

        if (RUE == null) {
            return "";
        }
        else {
            return RUE;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:21)
     * @return java.lang.String
     */
    public java.lang.String getTEL() {

        if (TEL == null) {
            return "";
        }
        else {
            return TEL;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:47)
     * @return java.lang.String
     */
    public java.lang.String getVILLE() {

        if (VILLE == null) {
            return "";
        }
        else {
            return VILLE;
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws java.sql.SQLException {

        SimpleDateFormatEG formatDate =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update COLLAB set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_COLLAB=" + CD_COLLAB);

        colonne.append("CIVILITE=");
        if ((CIVILITE != null) && (CIVILITE.length() != 0)) {
            colonne.append(DBSession.quoteWith(CIVILITE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("NOM=");
        if ((NOM != null) && (NOM.length() != 0)) {
            colonne.append(DBSession.quoteWith(NOM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRENOM=");
        if ((PRENOM != null) && (PRENOM.length() != 0)) {
            colonne.append(DBSession.quoteWith(PRENOM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("RUE=");
        if ((RUE != null) && (RUE.length() != 0)) {
            colonne.append(DBSession.quoteWith(RUE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("VILLE=");
        if ((VILLE != null) && (VILLE.length() != 0)) {
            colonne.append(DBSession.quoteWith(VILLE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_POSTAL=");
        if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
            colonne.append(DBSession.quoteWith(CD_POSTAL, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TEL=");
        if ((TEL != null) && (TEL.length() != 0)) {
            colonne.append(DBSession.quoteWith(TEL, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PORTABLE=");
        if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
            colonne.append(DBSession.quoteWith(PORTABLE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("EMAIL=");
        if ((EMAIL != null) && (EMAIL.length() != 0)) {
            colonne.append(DBSession.quoteWith(EMAIL, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_NAIS=");
        if (DT_NAIS != null) {
            colonne.append(
                DBSession.quoteWith(formatDate.formatEG(DT_NAIS.getTime()), '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("NUM_SECU=");
        if ((NUM_SECU != null) && (NUM_SECU.length() != 0)) {
            colonne.append(DBSession.quoteWith(NUM_SECU, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_FCT=");
        if (CD_FCT != 0) {
            colonne.append(CD_FCT);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TYP_CONTR=");
        if (CD_TYP_CONTR != 0) {
            colonne.append(CD_TYP_CONTR);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CATEG=");
        if ((CATEG != null) && (CATEG.length() != 0)) {
            colonne.append(DBSession.quoteWith(CATEG, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("ECHELON=");
        if ((ECHELON != null) && (ECHELON.length() != 0)) {
            colonne.append(DBSession.quoteWith(ECHELON, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("COEF=");
        if ((COEF != null) && (COEF.length() != 0)) {
            colonne.append(DBSession.quoteWith(COEF, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("QUOTA_HEURE=");
        if (QUOTA_HEURE != null) {
            colonne.append(QUOTA_HEURE);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("INDIC_VALID=");
        if ((INDIC_VALID != null) && (INDIC_VALID.length() != 0)) {
            colonne.append(DBSession.quoteWith(INDIC_VALID, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(
            DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));

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
     * Purge des collaborateurs
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les collaborateurs n'ayant plus d'historiques et de pointages et créé avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, Date dateLimite) throws FctlException {
        
        int nbEnreg = -1;
        
        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String reqSQL[] = new String[1];
         
        reqSQL[0] = "delete from COLLAB where DT_CREAT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'')
                + " and (select count(*) from POINTAGE where CD_COLLAB = COLLAB.CD_COLLAB) = 0"
                + " and (select count(*) from HISTO_PREST where CD_COLLAB = COLLAB.CD_COLLAB) = 0"
                + " and (select count(*) from FACT where CD_COLLAB = COLLAB.CD_COLLAB) = 0"
                + " and (select count(*) from RDV where CD_COLLAB = COLLAB.CD_COLLAB) = 0";
        
        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);
            
            nbEnreg = res[0];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des articles: " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "collabBean.purgeKo" + BasicSession.TAG_I18N);
        }
        
        // Fin de cette transaction
        dbConnect.endTransaction();
        
        return nbEnreg;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:09)
     * @param newCD_POSTAL java.lang.String
     */
    public void setCD_POSTAL(java.lang.String newCD_POSTAL) {
        CD_POSTAL = newCD_POSTAL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:26)
     * @param newCIVILITE java.lang.String
     */
    public void setCIVILITE(java.lang.String newCIVILITE) {
        CIVILITE = newCIVILITE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:46)
     * @param newEMAIL java.lang.String
     */
    public void setEMAIL(java.lang.String newEMAIL) {
        EMAIL = newEMAIL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:51)
     * @param newNOM java.lang.String
     */
    public void setNOM(java.lang.String newNOM) {
        NOM = newNOM;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:37)
     * @param newPORTABLE java.lang.String
     */
    public void setPORTABLE(java.lang.String newPORTABLE) {
        PORTABLE = newPORTABLE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:09)
     * @param newPRENOM java.lang.String
     */
    public void setPRENOM(java.lang.String newPRENOM) {
        PRENOM = newPRENOM;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:24)
     * @param newRUE java.lang.String
     */
    public void setRUE(java.lang.String newRUE) {
        RUE = newRUE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:21)
     * @param newTEL java.lang.String
     */
    public void setTEL(java.lang.String newTEL) {
        TEL = newTEL;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:47)
     * @param newVILLE java.lang.String
     */
    public void setVILLE(java.lang.String newVILLE) {
        VILLE = newVILLE;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getPRENOM();
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return java.lang.String
     */
    public java.lang.String getCATEG() {
        return CATEG;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return int
     */
    public int getCD_COLLAB() {
        return CD_COLLAB;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return int
     */
    public int getCD_FCT() {
        return CD_FCT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return int
     */
    public int getCD_TYP_CONTR() {
        return CD_TYP_CONTR;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return java.lang.String
     */
    public java.lang.String getCOEF() {
        return COEF;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_NAIS() {
        return DT_NAIS;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return java.lang.String
     */
    public java.lang.String getECHELON() {
        return ECHELON;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @return java.lang.String
     */
    public java.lang.String getNUM_SECU() {
        return NUM_SECU;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/12/2001 22:58:39)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getQUOTA_HEURE() {
        return QUOTA_HEURE;
    }

    /**
     * Retourne tous les collaborateurs du salon.
     * Note: ne pas oublier de fermer le resultset retourné apres utilisation
     * @param myDBSession La session utilisée pour récuperer les données de la base
     * @return tous les collaborateurs du salon.
     */
    public static ResultSet getAllCollabs(DBSession myDBSession) {
        ResultSet rSet = null;
        try {

            String reqSQL = "select * from COLLAB where INDIC_VALID='O' order by PRENOM, NOM";
            rSet = myDBSession.doRequest(reqSQL);
        }
        catch (Exception e) {
            System.out.println(
                "Erreur à la recupération des collaborateurs dans la base : " + e);
        }
        return rSet;
    }

    /**
     * Retourne tous les collaborateurs du salon sous forme d'une liste de collabs.
     * @param myDBSession La session utilisée pour récuperer les données de la base
     * @return tous les collaborateurs du salon.
     */
    public static List getAllCollabsAsList(DBSession myDBSession) {

        List collabsList = new LinkedList();
        try {
            ResultSet rSet = getAllCollabs(myDBSession);
            while (rSet.next()) {
                CollabBean aCollab = new CollabBean(rSet);
                collabsList.add(aCollab);
            }
            rSet.close();
        }
        catch (Exception e) {
            System.out.println(
                "Erreur a la constitution de la liste des collaborateurs : " + e);
        }
        return collabsList;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCATEG java.lang.String
     */
    public void setCATEG(java.lang.String newCATEG) {
        CATEG = newCATEG;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCD_COLLAB int
     */
    public void setCD_COLLAB(int newCD_COLLAB) {
        CD_COLLAB = newCD_COLLAB;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCD_COLLAB String
     */
    public void setCD_COLLAB(String newCD_COLLAB) {

        if (newCD_COLLAB.length() != 0) {
            CD_COLLAB = Integer.parseInt(newCD_COLLAB);
        }
        else {
            CD_COLLAB = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCD_FCT int
     */
    public void setCD_FCT(int newCD_FCT) {
        CD_FCT = newCD_FCT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCD_FCT String
     */
    public void setCD_FCT(String newCD_FCT) {
        if ((newCD_FCT != null) && (newCD_FCT.length() != 0)) {
            CD_FCT = Integer.parseInt(newCD_FCT);
        }
        else {
            CD_FCT = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCD_TYP_CONTR int
     */
    public void setCD_TYP_CONTR(int newCD_TYP_CONTR) {
        CD_TYP_CONTR = newCD_TYP_CONTR;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCD_TYP_CONTR String
     */
    public void setCD_TYP_CONTR(String newCD_TYP_CONTR) {
        if ((newCD_TYP_CONTR != null) && (newCD_TYP_CONTR.length() != 0)) {
            CD_TYP_CONTR = Integer.parseInt(newCD_TYP_CONTR);
        }
        else {
            CD_TYP_CONTR = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newCOEF java.lang.String
     */
    public void setCOEF(java.lang.String newCOEF) {
        COEF = newCOEF;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newDT_NAIS String
     * @param aLocale Configuration pour parser la date
     * @throws Exception En cas d'erreur de conversion
     */
    public void setDT_NAIS(String newDT_NAIS, Locale aLocale) throws Exception {
        if ((newDT_NAIS != null) && (newDT_NAIS.length() != 0)) {
            DT_NAIS = Calendar.getInstance();

            DateFormat formatDate = DateFormat.getDateInstance(DateFormat.SHORT, aLocale);
            try {
                DT_NAIS.setTime(formatDate.parse(newDT_NAIS));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_NAIS = null;
                throw (new Exception(BasicSession.TAG_I18N + "clientBean.formatDateAnniversaire" + BasicSession.TAG_I18N));
            }
        }
        else {
            DT_NAIS = null;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newDT_NAIS java.util.Calendar
     */
    public void setDT_NAIS(java.util.Calendar newDT_NAIS) {
        DT_NAIS = newDT_NAIS;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newECHELON java.lang.String
     */
    public void setECHELON(java.lang.String newECHELON) {
        ECHELON = newECHELON;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 13:23:17)
     * @param newNUM_SECU java.lang.String
     */
    public void setNUM_SECU(java.lang.String newNUM_SECU) {
        NUM_SECU = newNUM_SECU;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/12/2001 22:58:39)
     * @param newQUOTA_HEURE String
     */
    public void setQUOTA_HEURE(String newQUOTA_HEURE) {

        if ((newQUOTA_HEURE != null) && (newQUOTA_HEURE.length() != 0)) {
            QUOTA_HEURE = new BigDecimal(newQUOTA_HEURE);
        }
        else {
            QUOTA_HEURE = null;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/12/2001 22:58:39)
     * @param newQUOTA_HEURE java.math.BigDecimal
     */
    public void setQUOTA_HEURE(java.math.BigDecimal newQUOTA_HEURE) {
        QUOTA_HEURE = newQUOTA_HEURE;
    }
    /**
     * @return Collaborateur valide ?
     */
    public String getINDIC_VALID() {
        return INDIC_VALID;
    }

    /**
     * @param string Collaborateur valide ?
     */
    public void setINDIC_VALID(String string) {
        INDIC_VALID = string;
    }
    
    /**
     * Verifie que la liste ne contient pas le collaborateur et le met dans la liste
     * @param aCollab le collaborateur a ajouter
     * @param collabs la liste de collab auquel il faut ajouter le collaborateur
     */
    public static void verifieEtAjoute(CollabBean aCollab, List collabs) {
        if ((aCollab != null)
            && (collabs.contains(new Integer(aCollab.getCD_COLLAB())) == false)) {
                ajouteEtTrie(aCollab, collabs);
        }
        else {
            //On n'a pas besoin de l'ajouter
        }
    }
    
    /**
     * Verifie que la liste ne contient pas le collaborateur identifié par CD_COLLAB.
     * Si c'est le cas, récupère le collaborateur de la base et le met dans la liste
     * @param CD_COLLAB l'identifiant du collaborateur a ajouter
     * @param collabs la liste de collab auquel il faut ajouter le collaborateur
     * @param myDBSession la session utilisée pour récuperer le collaborateur si besoin est
     */
    public static void verifieEtAjoute(
        int CD_COLLAB,
        List collabs,
        DBSession myDBSession) {
        if (collabs.contains(new Integer(CD_COLLAB)) == false) {
            //Le collaborateur n'est pas dans la liste, il faut le recuperer et l'ajouter
            CollabBean aCollab =
                CollabBean.getCollabBean(myDBSession, Integer.toString(CD_COLLAB));
            if (aCollab != null) {
                ajouteEtTrie(aCollab, collabs);
            }
        }
        else {
            //On n'a pas besoin de l'ajouter
        }
    }

    /**
     * Ajoute le collab en évitant les doublons
     * @param aCollab : Collaborateur à ajouter
     * @param collabs : Liste des collab dans laquelle il faut faire l'ajout
     */
    public static void ajouteEtTrie(CollabBean aCollab, List collabs) {
        ListIterator iter = collabs.listIterator();
        boolean isPrenom = false;
        boolean trouve = false;
        int index = 0;
        String prenomCollab = aCollab.getPRENOM();
        //On parcourt la liste. On prend un element sur deux
        //qui doit etre le nom du collaborateur.
        //On compare et si necessaire, on l'insère
        while (iter.hasNext() && !trouve) {
            index++;
            if (isPrenom) {
                String prenom = (String) iter.next();
                if (prenom.compareTo(prenomCollab) > 0) {
                    //On doit l'insérer avant
                    trouve = true;
                }
                isPrenom = false;
            } else {
                //C'est le CD_COLAB, on l'ignore
                iter.next();
                isPrenom = true;   
            }
        }
        
        //Il faut faire un decallage pour l'index car on a l'index du prenom -> -2
        int veritableIndex = 0;
        if (trouve) {
            //on doit insérer la valeur avant l'enregistrement courant.
            veritableIndex = (index == 0 ? 0 : index - 2);
        } else {
            //On insere a la fin, donc a la position courante d'index
            veritableIndex = index;
        }
        collabs.add(veritableIndex, new Integer(aCollab.getCD_COLLAB()));
        collabs.add(veritableIndex + 1, prenomCollab);
        
    }

    /**
     * Constitue la liste des collab (présents ou tous) 
     * @param myDBSession Connexion à la base à utiliser
     * @param filtrePresent Indicateur s'il faut prendre tous les collaborateurs
     * @return Liste avec les collab
     */
    public static List getListeCollab(DBSession myDBSession, boolean filtrePresent) {
        List collabs;
        collabs = new LinkedList();
        List collabsList = CollabBean.getAllCollabsAsList(myDBSession);
        Iterator collabIter = collabsList.iterator();
        
        while (collabIter.hasNext()) {
            CollabBean aCollab = (CollabBean) collabIter.next();
            if (filtrePresent) {
                //On modifie une facture
                //on ne met que les presents
        
                // Recherche le dernier pointage
                PointageBean aPointage =
                    PointageBean.getPointageBean(
                        myDBSession,
                        Integer.toString(aCollab.getCD_COLLAB()),
                        new Date());
        
                if ((aPointage != null)
                    && (aPointage.getDT_DEBUT() != null)
                    && (aPointage.getDT_FIN() == null)
                    && (aPointage.getCD_TYP_POINTAGE() == PointageBean.TYP_PRESENCE)) {
                    //Le collaborateur est présent
                    //On l'ajoute a la liste car il doit etre affiché
                    verifieEtAjoute(aCollab, collabs);
        
                }
                else {
                    //On ne fait rien
                }
            }
            else {
                //On est en train d'imprimer, créer, supprimer ou modifier
                //un facture historique, on ajoute tout le monde
                verifieEtAjoute(aCollab, collabs);
            }
        
        }
        return collabs;
    }


}