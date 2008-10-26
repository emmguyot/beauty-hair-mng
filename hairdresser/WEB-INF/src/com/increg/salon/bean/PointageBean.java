/*
 * Bean de gestion de collaborateur 
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
 */
package com.increg.salon.bean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

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
public class PointageBean extends TimeStampBean {

    /**
     * Type de pointage : Présence
     */
    public static final int TYP_PRESENCE = 1;

    /**
     * Date et heure du début de pointage
     */
    protected java.util.Calendar DT_DEBUT;
    /**
     * Date et heure de fin du pointage
     */
    protected java.util.Calendar DT_FIN;
    /**
     * Collaborateur concerné
     */
    protected int CD_COLLAB;
    /**
     * Type du pointage
     */
    protected int CD_TYP_POINTAGE;
    /**
     * Commentaire pour le pointage
     */
    protected java.lang.String COMM;

    /**
     * PointageBean constructor comment.
     */
    public PointageBean() {
        super();
    }

    /**
     * PointageBean à partir d'un RecordSet.
     * @param rs Record set dans lequel lire le pointage
     */
    public PointageBean(ResultSet rs) {
        super(rs);
        try {
            CD_COLLAB = rs.getInt("CD_COLLAB");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PointageBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_POINTAGE = rs.getInt("CD_TYP_POINTAGE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PointageBean (RS) : " + e.toString());
            }
        }
        try {
            DT_DEBUT = Calendar.getInstance();
            java.util.Date DtDebut = rs.getTimestamp("DT_DEBUT", DT_DEBUT);
            if (DtDebut != null) {
                DT_DEBUT.setTime(DtDebut);
            }
            else {
                DT_DEBUT = null;
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PointageBean (RS) : " + e.toString());
            }
        }
        try {
            DT_FIN = Calendar.getInstance();
            java.util.Date DtFin = rs.getTimestamp("DT_FIN", DT_FIN);
            if (DtFin != null) {
                DT_FIN.setTime(DtFin);
            }
            else {
                DT_FIN = null;
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PointageBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PointageBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect)
        throws java.sql.SQLException, com.increg.commun.exception.FctlException {

        SimpleDateFormat formatDateSplit = new SimpleDateFormat("dd/MM/yyyy");

        if ((DT_DEBUT != null)
            && (DT_FIN != null)
            && (!formatDateSplit
                .format(DT_DEBUT.getTime())
                .equals(formatDateSplit.format(DT_FIN.getTime())))) {
            // Pointage sur plusieurs jours : Il faut éclater
            Vector listePointage = eclate(dbConnect);

            for (int i = 0; i < listePointage.size(); i++) {
                PointageBean aPointage = (PointageBean) listePointage.get(i);

                aPointage.create(dbConnect);
            }
        }
        else {

            com.increg.util.SimpleDateFormatEG formatDate =
                new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
            com.increg.util.SimpleDateFormatEG formatDate2 =
                new SimpleDateFormatEG("dd/MM/yyyy HH:mm");
            //	java.text.DateFormat formatDate  = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT,java.text.DateFormat.MEDIUM);
            //	java.text.DateFormat formatDate2  = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT,java.text.DateFormat.SHORT);

            StringBuffer req = new StringBuffer("insert into POINTAGE ");
            StringBuffer colonne = new StringBuffer("(");
            StringBuffer valeur = new StringBuffer(" values ( ");

            if (CD_COLLAB != 0) {
                colonne.append("CD_COLLAB,");
                valeur.append(CD_COLLAB);
                valeur.append(",");
            }

            if ((COMM != null) && (COMM.length() != 0)) {
                colonne.append("COMM,");
                valeur.append(DBSession.quoteWith(COMM, '\''));
                valeur.append(",");
            }

            if (DT_DEBUT != null) {
                colonne.append("DT_DEBUT,");
                valeur.append(
                    DBSession.quoteWith(formatDate2.formatEG(DT_DEBUT.getTime()), '\''));
                valeur.append(",");
            }

            if (DT_FIN != null) {
                colonne.append("DT_FIN,");
                valeur.append(
                    DBSession.quoteWith(formatDate2.formatEG(DT_FIN.getTime()), '\''));
                valeur.append(",");
            }

            if (CD_TYP_POINTAGE != 0) {
                colonne.append("CD_TYP_POINTAGE,");
                valeur.append(CD_TYP_POINTAGE);
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

            if (verifChevauchement(dbConnect, false)) {
                // Execute la création
                String[] reqs = new String[1];
                reqs[0] = req.toString();
                int[] nb = new int[1];
                nb = dbConnect.doExecuteSQL(reqs);

                if (nb[0] != 1) {
                    throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
                }
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws java.sql.SQLException {

        com.increg.util.SimpleDateFormatEG formatDate =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm");

        StringBuffer req = new StringBuffer("delete from POINTAGE ");
        StringBuffer where =
            new StringBuffer(
                " where CD_COLLAB="
                    + CD_COLLAB
                    + " and DT_DEBUT="
                    + DBSession.quoteWith(formatDate.formatEG(DT_DEBUT.getTime()), '\''));

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
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect)
        throws java.sql.SQLException, com.increg.commun.exception.FctlException {

        SimpleDateFormat formatDateSplit = new SimpleDateFormat("dd/MM/yyyy");

        if ((DT_DEBUT != null)
            && (DT_FIN != null)
            && (!formatDateSplit
                .format(DT_DEBUT.getTime())
                .equals(formatDateSplit.format(DT_FIN.getTime())))) {
            // Pointage sur plusieurs jours : Il faut éclater
            Vector listePointage = eclate(dbConnect);

            for (int i = 0; i < listePointage.size(); i++) {
                PointageBean aPointage = (PointageBean) listePointage.get(i);

                if (i == 0) {
                    aPointage.maj(dbConnect);
                }
                else {
                    aPointage.create(dbConnect);
                }
            }
        }
        else {
            com.increg.util.SimpleDateFormatEG formatDate =
                new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
            com.increg.util.SimpleDateFormatEG formatDate2 =
                new SimpleDateFormatEG("dd/MM/yyyy HH:mm");

            StringBuffer req = new StringBuffer("update POINTAGE set ");
            StringBuffer colonne = new StringBuffer("");
            StringBuffer where =
                new StringBuffer(
                    " where CD_COLLAB="
                        + CD_COLLAB
                        + " and DT_DEBUT="
                        + DBSession.quoteWith(formatDate2.formatEG(DT_DEBUT.getTime()), '\''));

            colonne.append("COMM=");
            if ((COMM != null) && (COMM.length() != 0)) {
                colonne.append(DBSession.quoteWith(COMM, '\''));
            }
            else {
                colonne.append("NULL");
            }
            colonne.append(",");

            colonne.append("DT_DEBUT=");
            if (DT_DEBUT != null) {
                colonne.append(
                    DBSession.quoteWith(formatDate2.formatEG(DT_DEBUT.getTime()), '\''));
            }
            else {
                colonne.append("NULL");
            }
            colonne.append(",");

            colonne.append("DT_FIN=");
            if (DT_FIN != null) {
                colonne.append(
                    DBSession.quoteWith(formatDate2.formatEG(DT_FIN.getTime()), '\''));
            }
            else {
                colonne.append("NULL");
            }
            colonne.append(",");

            colonne.append("CD_TYP_POINTAGE=");
            if (CD_TYP_POINTAGE != 0) {
                colonne.append(CD_TYP_POINTAGE);
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

            if (verifChevauchement(dbConnect, true)) {
                // Execute la création
                String[] reqs = new String[1];
                reqs[0] = req.toString();
                int[] nb = new int[1];
                nb = dbConnect.doExecuteSQL(reqs);

                if (nb[0] != 1) {
                    throw (new SQLException(BasicSession.TAG_I18N + "message.enregistrementKo" + BasicSession.TAG_I18N));
                }
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @return int
     */
    public int getCD_TYP_POINTAGE() {
        return CD_TYP_POINTAGE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @return java.lang.String
     */
    public java.lang.String getCOMM() {

        if (COMM == null) {
            return "";
        }
        else {
            return COMM;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_DEBUT() {
        return DT_DEBUT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_FIN() {
        return DT_FIN;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @param newCD_TYP_POINTAGE int
     */
    public void setCD_TYP_POINTAGE(int newCD_TYP_POINTAGE) {
        CD_TYP_POINTAGE = newCD_TYP_POINTAGE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @param newCOMM java.lang.String
     */
    public void setCOMM(java.lang.String newCOMM) {
        COMM = newCOMM;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @param newDT_DEBUT java.util.Calendar
     * @throws FctlException En cas d'erreur de conversion
     */
    public void setDT_DEBUT(java.util.Calendar newDT_DEBUT) throws FctlException {
        DT_DEBUT = newDT_DEBUT;
        if ((DT_DEBUT != null) && (DT_FIN != null) && (!DT_DEBUT.before(DT_FIN))) {
            throw new FctlException("La date de fin doit être après la date de début.");
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @param newDT_FIN java.util.Calendar
     * @throws FctlException En cas d'erreur de conversion
     */
    public void setDT_FIN(java.util.Calendar newDT_FIN) throws FctlException {
        DT_FIN = newDT_FIN;
        if ((DT_DEBUT != null) && (DT_FIN != null) && (!DT_DEBUT.before(DT_FIN))) {
            throw new FctlException("La date de fin doit être après la date de début.");
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (10/12/2001 22:22:43)
     * @return com.increg.salon.bean.PointageBean
     */
    public Object clone() {
        PointageBean aPointage = new PointageBean();

        aPointage.setCD_COLLAB(getCD_COLLAB());
        aPointage.setCD_TYP_POINTAGE(getCD_TYP_POINTAGE());
        aPointage.setCOMM(getCOMM());
        aPointage.setDT_CREAT(getDT_CREAT());
        try {
            aPointage.setDT_DEBUT(getDT_DEBUT());
            aPointage.setDT_FIN(getDT_FIN());
        }
        catch (Exception e) {
            System.out.println("Erreur dans clone PointageBean");
        }
        aPointage.setDT_MODIF(getDT_MODIF());

        return aPointage;
    }

    /**
     * Eclate un pointage en plusieurs en tenant compte de la durée du collab
     * Creation date: (01/12/2001 22:56:00)
     * @return java.util.Vector
     * @param dbConnect com.increg.salon.bean.DBSession
     * @exception java.sql.SQLException The exception description.
     */
    protected Vector eclate(DBSession dbConnect) throws java.sql.SQLException {

        Vector res = new Vector();

        // Recherche si le nombre d'heure du collab est défini
        CollabBean aCollab =
            CollabBean.getCollabBean(dbConnect, Integer.toString(CD_COLLAB));

        if ((aCollab.getQUOTA_HEURE() != null)
            && (aCollab.getQUOTA_HEURE().compareTo(new BigDecimal(0)) > 0)) {
            SimpleDateFormat formatDateSplit = new SimpleDateFormat("dd/MM/yyyy");
            Calendar dtEnCours = (Calendar) DT_DEBUT.clone();
            boolean premierTour = true;

            while (dtEnCours.before(DT_FIN)) {

                // Si il y a déjà des pointages : Tiens en compte
                String reqSQL =
                    "select sum(date_part('hour', DT_FIN-DT_DEBUT)*60 + date_part('day', DT_FIN-DT_DEBUT)*24*60 "
                        + "+ date_part('minute',DT_FIN-DT_DEBUT)) as TOTAL_HEURE from POINTAGE where CD_COLLAB="
                        + CD_COLLAB
                        + " and DT_DEBUT >= '"
                        + formatDateSplit.format(dtEnCours.getTime())
                        + "'"
                        + " and DT_DEBUT < '"
                        + formatDateSplit.format(dtEnCours.getTime())
                        + "'::date + 1"
                        + " and DT_FIN is not null";
                BigDecimal tot = null;

                // Interroge la Base
                try {
                    ResultSet aRS = dbConnect.doRequest(reqSQL);

                    while (aRS.next()) {
                        tot = new BigDecimal(aRS.getDouble("TOTAL_HEURE"));
                    }
                    aRS.close();
                }
                catch (Exception e) {
                    System.out.println("Erreur dans recherche pour éclatement : " + e.toString());
                }

                if ((tot == null)
                    || (tot.compareTo(aCollab.getQUOTA_HEURE().multiply(new BigDecimal(60))) < 0)) {
                    try {
                        // Pointage à ajouter
                        PointageBean aPointage = (PointageBean) this.clone();
                        aPointage.setDT_DEBUT((Calendar) dtEnCours.clone());
                        Calendar dtFin = (Calendar) dtEnCours.clone();
                        if (tot == null) {
                            dtFin.add(
                                Calendar.MINUTE,
                                aCollab.getQUOTA_HEURE().multiply(new BigDecimal(60)).intValue());
                        }
                        else {
                            dtFin.add(
                                Calendar.MINUTE,
                                aCollab.getQUOTA_HEURE().multiply(new BigDecimal(60)).subtract(tot).intValue());
                        }

                        // Vérifie que le pointage ne fini pas trop tard
                        // Pas au dela de la fin de journée
                        if (!premierTour) {
                            Calendar dtLimite = (Calendar) dtEnCours.clone();
                            dtLimite.set(Calendar.HOUR_OF_DAY, 19);
                            dtLimite.set(Calendar.MINUTE, 0);
                            while (!dtFin.before(dtLimite)) {
                                // Décalage
                                dtFin.add(Calendar.HOUR_OF_DAY, -1);
                                aPointage.getDT_DEBUT().add(Calendar.HOUR_OF_DAY, -1);
                            }
                        }
                        else {
                            Calendar dtLimite = (Calendar) dtEnCours.clone();
                            dtLimite.set(Calendar.HOUR_OF_DAY, 23);
                            dtLimite.set(Calendar.MINUTE, 59);
                            if (!dtFin.before(dtLimite)) {
                                // Impossible de trouver le bon compte sans toucher aux pointages en base : On s'arrête à 23H59
                                dtFin = dtLimite;
                            }
                        }
                        // Par au delà du moment indiqué
                        if (DT_FIN.before(dtFin)) {
                            // Décalle d'autant la date de début
                            dtFin = (Calendar) DT_FIN.clone();
                            if (tot == null) {
                                aPointage.getDT_DEBUT().add(
                                    Calendar.MINUTE,
                                    -aCollab.getQUOTA_HEURE().multiply(new BigDecimal(60)).intValue());
                            }
                            else {
                                aPointage.getDT_DEBUT().add(
                                    Calendar.MINUTE,
                                    -aCollab.getQUOTA_HEURE().multiply(new BigDecimal(60)).subtract(tot).intValue());
                            }
                        }

                        aPointage.setDT_FIN(dtFin);
                        aPointage.setDT_CREAT(Calendar.getInstance());
                        aPointage.setDT_MODIF(Calendar.getInstance());

                        res.add(aPointage);
                    }
                    catch (Exception e) {
                        System.out.println("Erreur dans création pointage éclaté : " + e.toString());
                    }
                }

                // Au suivant
                dtEnCours.add(Calendar.DAY_OF_MONTH, 1);

                // Saute les dimanches et lundi
                if (dtEnCours.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    dtEnCours.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (dtEnCours.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    dtEnCours.add(Calendar.DAY_OF_MONTH, 1);
                }
                premierTour = false;
            }
        }

        return res;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:05:01)
     * @return int
     */
    public int getCD_COLLAB() {
        return CD_COLLAB;
    }

    /**
     * Création d'un Bean collab à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_COLLAB java.lang.String
     * @param DT_DEBUT java.lang.String
     * @param aLocale Configuration pour parser la date
     * @throws Exception Probleme de décodage
     * @return Pointage obtenu ou null
     */
    public static PointageBean getPointageBean(DBSession dbConnect, String CD_COLLAB, String DT_DEBUT, Locale aLocale) throws Exception {
    	
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
            throw (new Exception(BasicSession.TAG_I18N + "pointageBean.formatDateFin" + BasicSession.TAG_I18N));
		}
        
        return getPointageBean(dbConnect, CD_COLLAB, dtDebut);
    }

    /**
     * Création d'un Bean collab à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_COLLAB java.lang.String
     * @param DT_DEBUT Date de début
     * @return Pointage obtenu ou null
     */
    public static PointageBean getPointageBean(DBSession dbConnect, String CD_COLLAB, Date DT_DEBUT) {
    	
        // Passage en GMT +0
        java.text.DateFormat formatDateStd = dbConnect.getFormatDateTimeSansTZ(2);
        String dtDebut = formatDateStd.format(DT_DEBUT);
        
        String reqSQL =
            "select * from POINTAGE where CD_COLLAB="
                + CD_COLLAB
                + " and DT_DEBUT<='"
                + dtDebut
                + "' order by DT_DEBUT desc limit 1";
        PointageBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new PointageBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Retourne la liste des collaborateurs presents au
     * moment de l'appel de cette methode.
     * @param dbConnect la connection a la base
     * @return la liste des collaborateurs presents
     */
    public static List getPresentCollabs(DBSession dbConnect) {
        List presentCollabs = new LinkedList();
        //Recupere tous les collaborateurs
        List collabsList = CollabBean.getAllCollabsAsList(dbConnect);
        Iterator collabIter = collabsList.iterator();

        //Pour chaque collaborateur, on recupere le dernier pointage
        //Si le collaborateur est present, on l'ajoute a la liste
        while (collabIter.hasNext()) {
            CollabBean aCollab = (CollabBean) collabIter.next();
            // Recherche le dernier pointage
            PointageBean aPointage =
                PointageBean.getPointageBean(
                    dbConnect,
                    Integer.toString(aCollab.getCD_COLLAB()),
                    new Date());

            if ((aPointage != null)
                && (aPointage.getDT_DEBUT() != null)
                && (aPointage.getDT_FIN() == null)
                && (aPointage.getCD_TYP_POINTAGE() == PointageBean.TYP_PRESENCE)) {
                //Le collaborateur est présent
                presentCollabs.add(aCollab);
            }
            else {
                //On ne fait rien
            }

        }
        return presentCollabs;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:05:01)
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
     * @param newCD_TYP_POINTAGE String
     */
    public void setCD_TYP_POINTAGE(String newCD_TYP_POINTAGE) {
        if ((newCD_TYP_POINTAGE != null) && (newCD_TYP_POINTAGE.length() != 0)) {
            CD_TYP_POINTAGE = Integer.parseInt(newCD_TYP_POINTAGE);
        }
        else {
            CD_TYP_POINTAGE = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @param newDT_DEBUT String
     * @param aLocale Configuration pour parser la date
     * @throws Exception En cas d'erreur de conversion
     */
    public void setDT_DEBUT(String newDT_DEBUT, Locale aLocale) throws Exception {
        if ((newDT_DEBUT != null) && (newDT_DEBUT.length() != 0)) {
            DT_DEBUT = Calendar.getInstance();

            // Format : Ignore les secondes
            java.text.DateFormat formatDate =
                java.text.DateFormat.getDateTimeInstance(
                    java.text.DateFormat.SHORT,
                    java.text.DateFormat.SHORT, aLocale);
            try {
                DT_DEBUT.setTime(formatDate.parse(newDT_DEBUT));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_DEBUT = null;
                throw (new Exception(BasicSession.TAG_I18N + "pointageBean.formatDateDebut" + BasicSession.TAG_I18N));
            }
            if ((DT_DEBUT != null) && (DT_FIN != null) && (!DT_DEBUT.before(DT_FIN))) {
                throw new FctlException(BasicSession.TAG_I18N + "pointageBean.chronoDebutFin" + BasicSession.TAG_I18N);
            }
        }
        else {
            DT_DEBUT = null;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/09/2001 09:04:21)
     * @param newDT_FIN String
     * @param aLocale Configuration pour parser la date
     * @throws Exception En cas d'erreur de conversion
     */
    public void setDT_FIN(String newDT_FIN, Locale aLocale) throws Exception {
        if ((newDT_FIN != null) && (newDT_FIN.length() != 0)) {
            DT_FIN = Calendar.getInstance();

            // Format : Ignore les secondes
            java.text.DateFormat formatDate =
                java.text.DateFormat.getDateTimeInstance(
                    java.text.DateFormat.SHORT,
                    java.text.DateFormat.SHORT, aLocale);
            try {
                DT_FIN.setTime(formatDate.parse(newDT_FIN));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_FIN = null;
                throw (new Exception(BasicSession.TAG_I18N + "pointageBean.formatDateFin" + BasicSession.TAG_I18N));
            }
            if ((DT_DEBUT != null) && (DT_FIN != null) && (!DT_DEBUT.before(DT_FIN))) {
                throw new FctlException(BasicSession.TAG_I18N + "pointageBean.chronoDebutFin" + BasicSession.TAG_I18N);
            }
        }
        else {
            DT_FIN = null;
        }
    }

    /**
     * Contrôle de chevauchement de deux pointages
     * Creation date: (04/10/2001 21:28:44)
     * @return boolean
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param exist boolean
     * @exception FctlException En cas de chevauchement
     */
    protected boolean verifChevauchement(DBSession dbConnect, boolean exist)
        throws FctlException {

        com.increg.util.SimpleDateFormatEG formatDate2 =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm");

        String reqSQL =
            "select count(*) as nb from POINTAGE where CD_COLLAB="
                + CD_COLLAB
                + " and DT_FIN is not null "
                + " and ((DT_DEBUT <= "
                + DBSession.quoteWith(formatDate2.formatEG(DT_DEBUT.getTime()), '\'')
                + " and "
                + DBSession.quoteWith(formatDate2.formatEG(DT_DEBUT.getTime()), '\'')
                + " < DT_FIN)";
        if (DT_FIN == null) {
            reqSQL = reqSQL + ")";
        }
        else {
            reqSQL =
                reqSQL
                    + " or (DT_DEBUT <= "
                    + DBSession.quoteWith(formatDate2.formatEG(DT_FIN.getTime()), '\'')
                    + " and "
                    + DBSession.quoteWith(formatDate2.formatEG(DT_FIN.getTime()), '\'')
                    + " < DT_FIN)"
                    + " or (DT_DEBUT >= "
                    + DBSession.quoteWith(formatDate2.formatEG(DT_DEBUT.getTime()), '\'')
                    + " and "
                    + DBSession.quoteWith(formatDate2.formatEG(DT_FIN.getTime()), '\'')
                    + " > DT_DEBUT))";
        }

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

        if (!ok) {
            throw (
                new com.increg.commun.exception.FctlException(
                    BasicSession.TAG_I18N + "pointageBean.recouvrement" + BasicSession.TAG_I18N));
        }

        return ok;
    }
    
    /**
     * Purge des pointages
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les pointages clos avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {
        
        int nbEnreg = -1;
        
        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String reqSQL[] = new String[1];
         
        reqSQL[0] = "delete from POINTAGE where DT_FIN < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'');
        
        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);
            
            nbEnreg = res[0];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des pointages : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "pointageBean.purgeKo" + BasicSession.TAG_I18N);
        }
        
        // Fin de cette transaction
        dbConnect.endTransaction();
        
        return nbEnreg;
    }
    

}