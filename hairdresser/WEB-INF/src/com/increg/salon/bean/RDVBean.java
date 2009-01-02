/*
 * Gestion d'un rendez-vous de client
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;

/**
 * Rendez-vous
 * Creation date: 11 févr. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RDVBean extends TimeStampBean {
    /**
     * Attribut statique représentant la TimeZone à utiliser pour la manipulation des RDV
     */    
    protected static TimeZone theTimeZone = null;;
    /** 
     * Code du client venant
     */
    protected long CD_CLI;
    /**
     * Collaborateur suivant le RDV
     */
    protected long CD_COLLAB;
    /**
     * Commentaire du mouvement
     */
    protected java.lang.String COMM;
    /**
     * Date du RDV
     */
    protected Calendar DT_DEBUT;
    /**
     * Durée en minutes
     */
    protected int DUREE;


    /**
     * RDVBean constructor comment.
     */
    public RDVBean() {
        super();
        DT_DEBUT = Calendar.getInstance();
        DUREE = 15;
    }
    /**
     * RDVBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public RDVBean(ResultSet rs) {
        super(rs);
        try {
            CD_CLI = rs.getLong("CD_CLI");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans RDVBean (RS) : " + e.toString());
            }
        }
        try {
            CD_COLLAB = rs.getLong("CD_COLLAB");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans RDVBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans RDVBean (RS) : " + e.toString());
            }
        }
        try {
            DT_DEBUT = Calendar.getInstance();
            DT_DEBUT.setTime(rs.getTimestamp("DT_DEBUT"));
            DT_DEBUT.setTimeZone(getTimeZone());
            DT_DEBUT.setTime(DT_DEBUT.getTime());
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans RDVBean (RS) : " + e.toString());
            }
        }
        try {
            String aDuree = rs.getString("DUREE");
            DateFormat formatDate = DateFormat.getTimeInstance(DateFormat.SHORT);
            Calendar aTime = Calendar.getInstance();
            aTime.setTime(formatDate.parse(aDuree));
            DUREE = aTime.get(Calendar.HOUR) * 60 + aTime.get(Calendar.MINUTE);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans RDVBean (RS) : " + e.toString());
            }
        }
        catch (ParseException e) {
            System.out.println("Erreur dans RDVBean (RS) : " + e.toString());
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException, com.increg.commun.exception.FctlException {

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatDate.setTimeZone(getTimeZone());

        StringBuffer req = new StringBuffer("insert into RDV ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

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

        if (DT_DEBUT != null) {
            colonne.append("DT_DEBUT,");
            // Force à GMT
            valeur.append(DBSession.quoteWith(formatDate.format(DT_DEBUT.getTime()) + "+0", '\''));
            valeur.append(",");
        }

        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append("COMM,");
            valeur.append(DBSession.quoteWith(COMM, '\''));
            valeur.append(",");
        }

        colonne.append("DUREE,");
        valeur.append(DBSession.quoteWith(DUREE + " minutes", '\''));
        valeur.append(",");

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(DBSession.quoteWith(formatDate.format(DT_CREAT.getTime()), '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(DBSession.quoteWith(formatDate.format(DT_MODIF.getTime()), '\''));
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
    public void maj(DBSession dbConnect) throws SQLException, FctlException {

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatDate.setTimeZone(getTimeZone());

        StringBuffer req = new StringBuffer("update RDV set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_CLI=" + CD_CLI 
            + " and DT_DEBUT=" + DBSession.quoteWith(formatDate.format(DT_DEBUT.getTime()) + "+0", '\''));

        colonne.append("CD_COLLAB=");
        colonne.append(CD_COLLAB);
        colonne.append(",");

        colonne.append("COMM=");
        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append(DBSession.quoteWith(COMM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DUREE=");
        colonne.append(DBSession.quoteWith(DUREE + " minutes", '\''));
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(
            DBSession.quoteWith(formatDate.format(DT_MODIF.getTime()), '\''));

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
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws SQLException, FctlException {

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatDate.setTimeZone(getTimeZone());

        StringBuffer req = new StringBuffer("delete from RDV ");
        StringBuffer where = new StringBuffer(" where CD_CLI=" + CD_CLI + " and DT_DEBUT=" + DBSession.quoteWith(formatDate.format(DT_DEBUT.getTime()) + "+0", '\''));

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

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
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @return long
     */
    public long getCD_CLI() {
        return CD_CLI;
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
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_CLI long
     */
    public void setCD_CLI(long newCD_CLI) {
        CD_CLI = newCD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
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
     * Creation date: (18/08/2001 13:25:05)
     * @param newCOMM java.lang.String
     */
    public void setCOMM(java.lang.String newCOMM) {
        COMM = newCOMM;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/09/2001 15:57:21)
     * @return long
     */
    public long getCD_COLLAB() {
        return CD_COLLAB;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_DEBUT() {
        return DT_DEBUT;
    }
    /**
     * Retourne la date calculée de fin du RDV 
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_FIN() {

        if (DT_DEBUT == null) {
            return null;
        }
        Calendar fin = (Calendar) DT_DEBUT.clone();
        fin.add(Calendar.MINUTE, DUREE);
        return fin;
    }

    /**
     * Création d'un Bean RDV à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_CLI java.lang.String
     * @param DT_DEBUT Date du mouvement
     * @param aLocale Configuration pour parser la date
     * @throws Exception Si le format est incorrect
     * @return RDV correspondant à la clé
     */
    public static RDVBean getRDVBean(DBSession dbConnect, String CD_CLI, String DT_DEBUT, Locale aLocale) throws Exception {

        java.text.DateFormat formatDate =
            java.text.DateFormat.getDateTimeInstance(
                java.text.DateFormat.SHORT,
                java.text.DateFormat.SHORT, aLocale);
        Date dtDebut = null;
        try {
            dtDebut = formatDate.parse(DT_DEBUT);
        }
        catch (ParseException e) {
            System.out.println("Erreur de conversion : " + e.toString());
            throw (new Exception(BasicSession.TAG_I18N + "rdvBean.formatDate" + BasicSession.TAG_I18N));
		}

        return getRDVBean(dbConnect, CD_CLI, dtDebut);
    }

    /**
     * Création d'un Bean RDV à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_CLI java.lang.String
     * @param DT_DEBUT Date du mouvement
     * @return RDV correspondant à la clé
     */
    public static RDVBean getRDVBean(DBSession dbConnect, String CD_CLI, Date DT_DEBUT) {

        // Passage en GMT +0
        java.text.DateFormat formatDateStd = dbConnect.getFormatDateTimeSansTZ();
        formatDateStd.setTimeZone(RDVBean.getTimeZone());
        String dtDebut = formatDateStd.format(DT_DEBUT);
        
        String reqSQL = "select * from RDV where CD_CLI=" + CD_CLI + " and DT_DEBUT='" + dtDebut + "+0'";
        RDVBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new RDVBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Création d'un Bean RDV à partir de sa clé
     * Creation date: 3 nov. 2002
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_COLLAB Collab dont on veut la liste des RDV
     * @return liste des RDV du collab
     */
    public static Vector getRDVBeanFromCollab(DBSession dbConnect, String CD_COLLAB) {
        String reqSQL = "select * from RDV where CD_COLLAB=" + CD_COLLAB + " order by DT_DEBUT";
        Vector lstMvt = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                RDVBean res = new RDVBean(aRS);
                lstMvt.add(res);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans recherche par collab : " + e.toString());
        }
        return lstMvt;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @return Valeur en minutes
     */
    public int getDUREE() {
        return DUREE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/09/2001 15:57:21)
     * @param newCD_COLLAB long
     */
    public void setCD_COLLAB(long newCD_COLLAB) {
        CD_COLLAB = newCD_COLLAB;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/09/2001 15:57:21)
     * @param newCD_COLLAB String
     */
    public void setCD_COLLAB(String newCD_COLLAB) {
        if ((newCD_COLLAB != null) && (newCD_COLLAB.length() != 0)) {
            CD_COLLAB = Long.parseLong(newCD_COLLAB);
        }
        else {
            CD_COLLAB = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newDT_DEBUT String
     * @param aLocale Configuration pour parser la date
     * @exception Exception en cas d'erreur de format de date
     */
    public void setDT_DEBUT(String newDT_DEBUT, Locale aLocale) throws Exception {

        if ((newDT_DEBUT != null) && (newDT_DEBUT.length() != 0)) {
            DT_DEBUT = Calendar.getInstance();
            DT_DEBUT.setTimeZone(getTimeZone());

            DateFormat formatDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, aLocale);
            formatDate.setTimeZone(getTimeZone());
            try {
                DT_DEBUT.setTime(formatDate.parse(newDT_DEBUT));
                DT_DEBUT.setTimeZone(getTimeZone());
                DT_DEBUT.setTime(DT_DEBUT.getTime());
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_DEBUT = null;
                throw (new Exception(BasicSession.TAG_I18N + "rdvBean.formatDate" + BasicSession.TAG_I18N));
            }
        }
        else {
            DT_DEBUT = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newDT_DEBUT java.util.Calendar
     */
    public void setDT_DEBUT(java.util.Calendar newDT_DEBUT) {
        DT_DEBUT = newDT_DEBUT;
        DT_DEBUT.clear(Calendar.MILLISECOND);
    }
    /**
     * Insert the method's description here.
     * Creation date: (01/09/2001 14:59:04)
     * @param newDUREE String
     */
    public void setDUREE(String newDUREE) {

        if ((newDUREE != null) && (newDUREE.length() != 0)) {
            DUREE = Integer.parseInt(newDUREE);
        }
        else {
            DUREE = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newDUREE java.math.BigDecimal
     */
    public void setDUREE(int newDUREE) {
        DUREE = newDUREE;
    }

    /**
     * Purge des rdv
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les rdv avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {
        
        int nbEnreg = -1;
        
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String reqSQL[] = new String[1];
         
        reqSQL[0] = "delete from RDV where DT_DEBUT < " + DBSession.quoteWith(formatDate.format(dateLimite) + "+0", '\'');
        
        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);
            
            nbEnreg = res[0];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des RDV : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "rdvBean.purgeKo" + BasicSession.TAG_I18N);
        }
        
        // Fin de cette transaction
        dbConnect.endTransaction();
        
        return nbEnreg;
    }
    
    /**
     * Contrôle de chevauchement de deux RDV pour un même collab
     * Creation date: (04/10/2001 21:28:44)
     * @return boolean true si pas de conflit
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param exist boolean indiquant si l'objet en cours existe déjà en base
     */
    public boolean verifChevauchement(DBSession dbConnect, boolean exist) {

        SimpleDateFormat formatDate2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        formatDate2.setTimeZone(getTimeZone());
        Calendar DT_FIN = (Calendar) DT_DEBUT.clone();
        DT_FIN.add(Calendar.MINUTE, DUREE);

        String reqSQL =
            "select count(*) as nb from RDV where CD_COLLAB="
                + CD_COLLAB
                + " and ((DT_DEBUT < "
                + DBSession.quoteWith(formatDate2.format(DT_DEBUT.getTime()) + "+0", '\'')
                + " and "
                + DBSession.quoteWith(formatDate2.format(DT_DEBUT.getTime()) + "+0", '\'')
                + " < (DT_DEBUT + DUREE))";
        reqSQL = reqSQL
                + " or (DT_DEBUT < "
                + DBSession.quoteWith(formatDate2.format(DT_FIN.getTime()) + "+0", '\'')
                + " and "
                + DBSession.quoteWith(formatDate2.format(DT_FIN.getTime()) + "+0", '\'')
                + " < (DT_DEBUT + DUREE))"
                + " or (DT_DEBUT > "
                + DBSession.quoteWith(formatDate2.format(DT_DEBUT.getTime()) + "+0", '\'')
                + " and "
                + DBSession.quoteWith(formatDate2.format(DT_FIN.getTime()) + "+0", '\'')
                + " > DT_DEBUT)"
                + " or (DT_DEBUT = "
                + DBSession.quoteWith(formatDate2.format(DT_DEBUT.getTime()) + "+0", '\'')
                + " and "
                + DBSession.quoteWith(formatDate2.format(DT_FIN.getTime()) + "+0", '\'')
                + " = (DT_DEBUT + DUREE)))";

        boolean ok = true;

        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);
            while (aRS.next()) {
                int nb = aRS.getInt("nb");
                if ((exist && (nb > 1)) || (!exist && (nb > 0))) {
                    ok = false;
                }
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("verifChevauchement : Erreur " + e.toString());
        }

        return ok;
    }

    /**
     * @return TimeZone à utiliser pour les RDV
     */
    public static TimeZone getTimeZone() {
        if (theTimeZone == null) {
            theTimeZone = new SimpleTimeZone(0, "GMT");
        }
        return theTimeZone;
    }

} //Fin de la class RDVBean
