/*
 * Bean gérant les devises
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
import java.sql.*;
import java.util.Vector;

import com.increg.commun.*;

/**
 * Bean des devises
 * Creation date: 26 févr. 03
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class DeviseBean extends GenericBean {

    /**
     * Code de la devise
     */
    protected int CD_DEVISE;
    /**
     * Libellé court de la devise (HTML)
     */
    protected String LIB_COURT_DEVISE;
    /**
     * Libellé de la devise
     */
    protected String LIB_DEVISE;
    /**
     * Rapport à la devise principale (1 pour la principale)
     */
    protected BigDecimal RATIO;

    /**
     * Constructeur comment.
     */
    public DeviseBean() {

    }

    /**
     * Constructeur à partie de la base
     * @param rs java.sql.ResultSet
     */
    public DeviseBean(ResultSet rs) {
        super(rs);
        try {
            CD_DEVISE = rs.getInt("CD_DEVISE");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans DeviseBean (RS) : " + e.toString());
            }
        }
        try {
            LIB_COURT_DEVISE = rs.getString("LIB_COURT_DEVISE");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans DeviseBean (RS) : " + e.toString());
            }
        }
        try {
            LIB_DEVISE = rs.getString("LIB_DEVISE");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans DeviseBean (RS) : " + e.toString());
            }
        }
        try {
            RATIO = rs.getBigDecimal("RATIO",5);
            RATIO.setScale(5);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans DeviseBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("insert into DEVISE");
        StringBuffer colonne = new StringBuffer(" (");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_DEVISE == 0) {
            /**
             * Numérotation automatique
             */
            String reqMax = "select nextval('SEQ_DEVISE')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_DEVISE = 1; // Par défaut

                while (aRS.next()) {
                    CD_DEVISE = aRS.getInt(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_DEVISE,");
        valeur.append(CD_DEVISE);
        valeur.append(",");

        if ((LIB_COURT_DEVISE != null) && (LIB_COURT_DEVISE.length() != 0)) {
            colonne.append("LIB_COURT_DEVISE,");
            valeur.append(DBSession.quoteWith(LIB_COURT_DEVISE, '\''));
            valeur.append(",");
        }

        if ((LIB_DEVISE != null) && (LIB_DEVISE.length() != 0)) {
            colonne.append("LIB_DEVISE,");
            valeur.append(DBSession.quoteWith(LIB_DEVISE, '\''));
            valeur.append(",");
        }

        if (RATIO != null) {
            colonne.append("RATIO,");
            valeur.append(RATIO.toString());
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

        StringBuffer req = new StringBuffer("delete from DEVISE");
        StringBuffer where = new StringBuffer(" where CD_DEVISE =" + CD_DEVISE);

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
    public int getCD_DEVISE() {
        return CD_DEVISE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_COURT_DEVISE() {
        return LIB_COURT_DEVISE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_DEVISE() {
        return LIB_DEVISE;
    }

    /**
     * Création d'un Bean Type de Vente à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_DEVISE java.lang.String
     * @return Devise correspondante
     */
    public static DeviseBean getDeviseBean(DBSession dbConnect, String CD_DEVISE) {
        String reqSQL = "select * from DEVISE where CD_DEVISE=" + CD_DEVISE;
        DeviseBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new DeviseBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Création d'un Bean Type de Vente à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @return Devise correspondante
     */
    public static DeviseBean getMainDeviseBean(DBSession dbConnect) {
        String reqSQL = "select * from DEVISE where RATIO=1";
        DeviseBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new DeviseBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Création d'un Bean Type de Vente à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @return Liste des autres devises
     */
    public static Vector getOtherDeviseBean(DBSession dbConnect) {
        String reqSQL = "select * from DEVISE where RATIO<>1";
        Vector res = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res.add(new DeviseBean(aRS));
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("update DEVISE set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_DEVISE=" + CD_DEVISE);

        colonne.append("LIB_COURT_DEVISE=");
        if ((LIB_COURT_DEVISE != null) && (LIB_COURT_DEVISE.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_COURT_DEVISE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("LIB_DEVISE=");
        if ((LIB_DEVISE != null) && (LIB_DEVISE.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_DEVISE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");
        
        colonne.append("RATIO=");
        colonne.append(RATIO.toString());

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
     * @param newCD_DEVISE int
     */
    public void setCD_DEVISE(int newCD_DEVISE) {
        CD_DEVISE = newCD_DEVISE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_DEVISE String
     */
    public void setCD_DEVISE(String newCD_DEVISE) {
        if ((newCD_DEVISE != null) && (newCD_DEVISE.length() != 0)) {
            CD_DEVISE = Integer.parseInt(newCD_DEVISE);
        } else {
            CD_DEVISE = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_COURT_DEVISE java.lang.String
     */
    public void setLIB_COURT_DEVISE(java.lang.String newLIB_COURT_DEVISE) {
        LIB_COURT_DEVISE = newLIB_COURT_DEVISE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_DEVISE java.lang.String
     */
    public void setLIB_DEVISE(java.lang.String newLIB_DEVISE) {
        LIB_DEVISE = newLIB_DEVISE;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getLIB_COURT_DEVISE();
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (16/12/2001 21:05:01)
     * @return BigDecimal
     */
    public BigDecimal getRATIO() {
        return RATIO;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (16/12/2001 21:05:01)
     * @param newRATIO BigDecimal
     */
    public void setRATIO(BigDecimal newRATIO) {
        RATIO = newRATIO;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/12/2001 21:05:01)
     * @param newRATIO String
     */
    public void setRATIO(String newRATIO) {
        if ((newRATIO != null) && (newRATIO.length() != 0)) {
            RATIO = new BigDecimal(newRATIO);
        } else {
            RATIO = new BigDecimal(0);
        }
    }

    /**
     * Converti dans cette devise un montant
     * @param montant montant à convertir
     * @return montant converti et arrondi
     */
    public BigDecimal convertiMontant(BigDecimal montant) {
        if (montant == null) {
            return null;
        }
        return montant.multiply(RATIO).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
