/*
 * Bean gérant les différentes fêtes des prénoms
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.GenericBean;
import com.increg.util.SimpleDateFormatEG;

/**
 * Bean de gestion des paramètres de l'application
 * Creation date: (18/11/2001 21:58:58)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FeteBean extends GenericBean {
    /**
     * Code de la fête (identifiant)
     */
    protected int CD_FETE;
    /**
     * Date de la fête (année non significative)
     */
    protected Calendar DT_FETE;
    /**
     * Prénom concerné
     */
    protected java.lang.String PRENOM;
    /**
     * FeteBean constructor comment.
     */
    public FeteBean() {

    }
    /**
     * FeteBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public FeteBean(ResultSet rs) {
        super(rs);
        try {
            CD_FETE = rs.getInt("CD_FETE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FeteBean (RS) : " + e.toString());
            }
        }
        try {
            java.util.Date DtFete = rs.getDate("DT_FETE");
            if (DtFete != null) {
                DT_FETE = Calendar.getInstance();
                DT_FETE.setTime(DtFete);
            }
            else {
                DT_FETE = null;
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FeteBean (RS) : " + e.toString());
            }
        }
        try {
            PRENOM = rs.getString("PRENOM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FeteBean (RS) : " + e.toString());
            }
        }
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into FETE");
        StringBuffer colonne = new StringBuffer(" (");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_FETE == 0) {
            /**
             * Numérotation automatique des prestations
             */
            String reqMax = "select nextval('SEQ_FETE')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_FETE = 1; // Par défaut

                while (aRS.next()) {
                    CD_FETE = aRS.getInt(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_FETE,");
        valeur.append(CD_FETE);
        valeur.append(",");

        if (DT_FETE != null) {
            colonne.append("DT_FETE,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_FETE.getTime()), '\''));
            valeur.append(",");
        }

        if ((PRENOM != null) && (PRENOM.length() != 0)) {
            colonne.append("PRENOM,");
            valeur.append(DBSession.quoteWith(PRENOM, '\''));
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

        StringBuffer req = new StringBuffer("delete from FETE");
        StringBuffer where = new StringBuffer(" where CD_FETE =" + CD_FETE);

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
     * Creation date: (19/08/2001 20:55:16)
     * @return int
     */
    public int getCD_FETE() {
        return CD_FETE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public Calendar getDT_FETE() {
        return DT_FETE;
    }
    /**
     * Création d'un Bean Fête à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_FETE_PRENOM java.lang.String
     * @return Fête correspondate
     */
    public static FeteBean getFeteBean(DBSession dbConnect, String CD_FETE_PRENOM) {

        // Par défaut : recherche sur le prénom
        String reqSQL = "select * from FETE where PRENOM=" + DBSession.quoteWith(CD_FETE_PRENOM, '\'');
        try {
            Integer.parseInt(CD_FETE_PRENOM);
            // Si pas d'erreur de conversion : C'est un code
            reqSQL = "select * from FETE where CD_FETE=" + CD_FETE_PRENOM;
        }
        catch (Exception e) {
            // Ignore l'erreur
        }

        FeteBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new FeteBean(aRS);
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
     * Creation date: (02/09/2001 00:13:51)
     * @return java.lang.String
     */
    public java.lang.String getPRENOM() {
        return PRENOM;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update FETE set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_FETE=" + CD_FETE);

        colonne.append("DT_FETE=");
        if (DT_FETE != null) {
            colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_FETE.getTime()), '\''));
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
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_FETE int
     */
    public void setCD_FETE(int newCD_FETE) {
        CD_FETE = newCD_FETE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_FETE String
     */
    public void setCD_FETE(String newCD_FETE) {
        if ((newCD_FETE != null) && (newCD_FETE.length() != 0)) {
            CD_FETE = Integer.parseInt(newCD_FETE);
        }
        else {
            CD_FETE = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newDT_FETE java.lang.String
     */
    public void setDT_FETE(Calendar newDT_FETE) {
        DT_FETE = newDT_FETE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/09/2001 00:13:51)
     * @param newPRENOM java.lang.String
     */
    public void setPRENOM(java.lang.String newPRENOM) {
        PRENOM = newPRENOM;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM");

        if (getDT_FETE() != null) {
            return formatDate.format(getDT_FETE().getTime());
        }
        else {
            return "";
        }
    }
    /**
     * Création d'un Bean Fête à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @return Liste des fêtes du jour
     */
    public static Vector getFeteBean(DBSession dbConnect) {

        Vector res = new Vector();

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");

        // Par défaut : recherche sur le prénom
        String reqSQL = "select * from FETE where DT_FETE='" + formatDate.format(new java.util.Date()) + "/2004'";

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res.add(new FeteBean(aRS));
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
     * Creation date: (19/08/2001 20:52:52)
     * @param newDT_FETE java.lang.String
     * @param aLocale
     * @throws Exception Si format erroné
     */
    public void setDT_FETE(String newDT_FETE, Locale aLocale) throws Exception {
        if (newDT_FETE.length() != 0) {
            DT_FETE = Calendar.getInstance();

            DateFormat formatDate = DateFormat.getDateInstance(java.text.DateFormat.SHORT, aLocale);

            try {
                DT_FETE.setTime(formatDate.parse(newDT_FETE + "/2004"));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_FETE = null;
                throw (new Exception(BasicSession.TAG_I18N + "feteBean.formatDateFete" + BasicSession.TAG_I18N));
            }
        }
        else {
            DT_FETE = null;
        }
    }
}
