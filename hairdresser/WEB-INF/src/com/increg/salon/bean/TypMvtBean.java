/*
 * Type de mouvements de stock
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

import java.sql.*;
import com.increg.commun.*;

/**
 * Type de mouvement de stock
 * Creation date: (21/08/2001 21:54:12)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TypMvtBean extends GenericBean {
    /**
     * SENS_ENTREE  Type de mouvements d'entrée
     */
    public static final java.lang.String SENS_ENTREE = "E";
    /**
     * SENS_INVENTAIRE Type de mouvements d'inventaire
     */
    public static final java.lang.String SENS_INVENTAIRE = "I";
    /**
     * SENS_SORTIE Type de mouvements de sortie
     */
    public static final java.lang.String SENS_SORTIE = "S";
    /**
     * VENTE Code du mouvement correspondant à une vente de produit
     */
    public static final int VENTE = 1;
    /**
     * UTILISATION Code du mouvement correspondant à une utilisation de produit
     */
    public static final int UTILISATION = 3;

    /**
     * CD_TYP_MVT Code numérique du mouvement
     */
    protected int CD_TYP_MVT;
    /**
     * LIB_TYP_MVT Libellé du type de mouvement
     */
    protected java.lang.String LIB_TYP_MVT;
    /**
     * SENS_MVT Sens du mouvement (E/S/I)
     */
    protected java.lang.String SENS_MVT;
    /**
     * TRANSF_MIXTE Indique si ce mouvement rend un article mixte
     */
    protected String TRANSF_MIXTE;

    /**
     * TypMvtBean constructor comment.
     */
    public TypMvtBean() {
        super();
        TRANSF_MIXTE = "N";
    }
    /**
     * TypMvtBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public TypMvtBean(ResultSet rs) {
        super(rs);
        try {
            CD_TYP_MVT = rs.getInt("CD_TYP_MVT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TypMvtBean (RS) : " + e.toString());
            }
        }
        try {
            LIB_TYP_MVT = rs.getString("LIB_TYP_MVT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TypMvtBean (RS) : " + e.toString());
            }
        }

        try {
            SENS_MVT = rs.getString("SENS_MVT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TypMvtBean (RS) : " + e.toString());
            }
        }
        try {
            TRANSF_MIXTE = rs.getString("TRANSF_MIXTE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TypMvtBean (RS) : " + e.toString());
            }
        }
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("insert into TYP_MVT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_TYP_MVT == 0) {
            /**
             * Numérotation automatique des prestations
             */
            String reqMax = "select nextval('SEQ_TYP_MVT')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_TYP_MVT = 1; // Par défaut

                while (aRS.next()) {
                    CD_TYP_MVT = aRS.getInt(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_TYP_MVT,");
        valeur.append(CD_TYP_MVT);
        valeur.append(",");

        if ((LIB_TYP_MVT != null) && (LIB_TYP_MVT.length() != 0)) {
            colonne.append("LIB_TYP_MVT,");
            valeur.append(DBSession.quoteWith(LIB_TYP_MVT, '\''));
            valeur.append(",");
        }

        if ((SENS_MVT != null) && (SENS_MVT.length() != 0)) {
            colonne.append("SENS_MVT,");
            valeur.append(DBSession.quoteWith(SENS_MVT, '\''));
            valeur.append(",");
        }

        if ((TRANSF_MIXTE != null) && (TRANSF_MIXTE.length() != 0)) {
            colonne.append("TRANSF_MIXTE,");
            valeur.append(DBSession.quoteWith(TRANSF_MIXTE, '\''));
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

        StringBuffer req = new StringBuffer("delete from TYP_MVT ");
        StringBuffer where = new StringBuffer(" where CD_TYP_MVT=" + CD_TYP_MVT);

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
    public int getCD_TYP_MVT() {
        return CD_TYP_MVT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_TYP_MVT() {
        return LIB_TYP_MVT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 09:27:53)
     * @return java.lang.String
     */
    public java.lang.String getSENS_MVT() {
        return SENS_MVT;
    }
    /**
     * Création d'un Bean Type de Vente à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_TYP_MVT java.lang.String
     * @return TypMvtBean 
     */
    public static TypMvtBean getTypMvtBean(DBSession dbConnect, String CD_TYP_MVT) {
        String reqSQL = "select * from TYP_MVT where CD_TYP_MVT=" + CD_TYP_MVT;
        TypMvtBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new TypMvtBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("update TYP_MVT set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_TYP_MVT=" + CD_TYP_MVT);

        colonne.append("LIB_TYP_MVT=");
        if ((LIB_TYP_MVT != null) && (LIB_TYP_MVT.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_TYP_MVT, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("SENS_MVT=");
        if ((SENS_MVT != null) && (SENS_MVT.length() != 0)) {
            colonne.append(DBSession.quoteWith(SENS_MVT, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TRANSF_MIXTE=");
        if ((TRANSF_MIXTE != null) && (TRANSF_MIXTE.length() != 0)) {
            colonne.append(DBSession.quoteWith(TRANSF_MIXTE, '\''));
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
     * @param newCD_TYP_MVT int
     */
    public void setCD_TYP_MVT(int newCD_TYP_MVT) {
        CD_TYP_MVT = newCD_TYP_MVT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_TYP_MVT String
     */
    public void setCD_TYP_MVT(String newCD_TYP_MVT) {
        if ((newCD_TYP_MVT != null) && (newCD_TYP_MVT.length() != 0)) {
            CD_TYP_MVT = Integer.parseInt(newCD_TYP_MVT);
        }
        else {
            CD_TYP_MVT = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_TYP_MVT java.lang.String
     */
    public void setLIB_TYP_MVT(java.lang.String newLIB_TYP_MVT) {
        LIB_TYP_MVT = newLIB_TYP_MVT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 09:27:53)
     * @param newSENS_MVT java.lang.String
     */
    public void setSENS_MVT(java.lang.String newSENS_MVT) {
        SENS_MVT = newSENS_MVT;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getLIB_TYP_MVT();
    }
    /**
     * Gets the tRANSF_MIXTE.
     * @return Returns a String
     */
    public String getTRANSF_MIXTE() {
        return TRANSF_MIXTE;
    }

    /**
     * Sets the tRANSF_MIXTE.
     * @param tRANSF_MIXTE The tRANSF_MIXTE to set
     */
    public void setTRANSF_MIXTE(String tRANSF_MIXTE) {
        TRANSF_MIXTE = tRANSF_MIXTE;
    }

    /**
     * Vérification que le sens du type de mouvement est modifiable
     * Il ne l'est plus dès que le type à été utilisé, sinon l'intégrité est mise en cause
     * @param dbConnect Connexion à la base à utiliser
     * @return true si le sens de ce mouvement est modifiable
     */
    public boolean sensIsModifiable(DBSession dbConnect) {
    
        String reqSQL = "select count(*) as compte from MVT_STK where CD_TYP_MVT=" + Integer.toString(CD_TYP_MVT);
    
        boolean res = true;
    
        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);
    
            while (aRS.next()) {
                if (aRS.getLong("compte") > 0) {
                    // Il y a des mouvements : Pas possible de modifier le type
                    res = false;
                }
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans sensIsModifiable : " + e.toString());
        }
        
        return res;
    }
}
