/*
 * Type de vente vente / service
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
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.GenericBean;
/**
 * Type de vente vente / service
 * Creation date: (21/08/2001 21:54:12)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TypVentBean extends GenericBean {

    /**
     * CD_TYP_VENT Code numérique du type de vente
     */
    protected int CD_TYP_VENT;

    /**
     * CIVILITE Liste des civilités associées à ce type de vente
     * la liste est séparée par un | et peut être vide si pas de filtre
     */
    protected java.lang.String CIVILITE;

    /**
     * LIB_TYP_VENT Libellé du type de vente
     */
    protected java.lang.String LIB_TYP_VENT;

    /**
     * MARQUE Indicateur si le type de vente impose la présence de marque
     */
    protected java.lang.String MARQUE;

    /**
     * Code TVA associé au type de vente
     */
    protected int CD_TVA;

    /**
     * Code TVA supplémentaire associé au type de vente (optionnel)
     */
    protected int CD_TVA_SUPPL;
    
    /**
     * Indicateur si la TVA supplémentaire s'applique sur le montant HT (ou sur le montant après application de la première TVA) 
     */
    protected java.lang.String TVA_SUPPL_SUR_HT;

    /**
     * TypVentBean constructor comment.
     */
    public TypVentBean() {
        super();
    }

    /**
     * TypVentBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public TypVentBean(ResultSet rs) {
        super(rs);
        try {
            CD_TYP_VENT = rs.getInt("CD_TYP_VENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CIVILITE = rs.getString("CIVILITE");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }

        try {
            LIB_TYP_VENT = rs.getString("LIB_TYP_VENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }

        try {
            MARQUE = rs.getString("MARQUE");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TVA = rs.getInt("CD_TVA");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TVA_SUPPL = rs.getInt("CD_TVA_SUPPL");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            TVA_SUPPL_SUR_HT = rs.getString("TVA_SUPPL_SUR_HT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }

    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("insert into TYP_VENT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_TYP_VENT == 0) {
            /**
             * Numérotation automatique des prestations
             */
            String reqMax = "select nextval('seq_typ_vent')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_TYP_VENT = 1; // Par défaut

                while (aRS.next()) {
                    CD_TYP_VENT = aRS.getInt(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_TYP_VENT,");
        valeur.append(CD_TYP_VENT);
        valeur.append(",");

        if ((CIVILITE != null) && (CIVILITE.length() != 0)) {
            colonne.append("CIVILITE,");
            valeur.append(DBSession.quoteWith(CIVILITE, '\''));
            valeur.append(",");
        }

        if ((LIB_TYP_VENT != null) && (LIB_TYP_VENT.length() != 0)) {
            colonne.append("LIB_TYP_VENT,");
            valeur.append(DBSession.quoteWith(LIB_TYP_VENT, '\''));
            valeur.append(",");
        }

        if ((MARQUE != null) && (MARQUE.length() != 0)) {
            colonne.append("MARQUE,");
            valeur.append(DBSession.quoteWith(MARQUE, '\''));
            valeur.append(",");
        }

        if (CD_TVA != 0) {
            colonne.append("CD_TVA,");
            valeur.append(CD_TVA);
            valeur.append(",");
        }

        if (CD_TVA_SUPPL != 0) {
            colonne.append("CD_TVA_SUPPL,");
            valeur.append(CD_TVA_SUPPL);
            valeur.append(",");
        }

        if ((TVA_SUPPL_SUR_HT != null) && (TVA_SUPPL_SUR_HT.length() != 0)) {
            colonne.append("TVA_SUPPL_SUR_HT,");
            valeur.append(DBSession.quoteWith(TVA_SUPPL_SUR_HT, '\''));
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

        StringBuffer req = new StringBuffer("delete from TYP_VENT ");
        StringBuffer where = new StringBuffer(" where CD_TYP_VENT=" + CD_TYP_VENT);

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
    public void maj(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("update TYP_VENT set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_TYP_VENT=" + CD_TYP_VENT);

        colonne.append("CIVILITE=");
        if ((CIVILITE != null) && (CIVILITE.length() != 0)) {
            colonne.append(DBSession.quoteWith(CIVILITE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("LIB_TYP_VENT=");
        if ((LIB_TYP_VENT != null) && (LIB_TYP_VENT.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_TYP_VENT, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("MARQUE=");
        if ((MARQUE != null) && (MARQUE.length() != 0)) {
            colonne.append(DBSession.quoteWith(MARQUE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TVA=");
        if (CD_TVA != 0) {
            colonne.append(CD_TVA);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TVA_SUPPL=");
        if (CD_TVA_SUPPL != 0) {
            colonne.append(CD_TVA_SUPPL);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TVA_SUPPL_SUR_HT=");
        if ((TVA_SUPPL_SUR_HT != null) && (TVA_SUPPL_SUR_HT.length() != 0)) {
            colonne.append(DBSession.quoteWith(TVA_SUPPL_SUR_HT, '\''));
        } else {
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
     * @return int
     */
    public int getCD_TYP_VENT() {
        return CD_TYP_VENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:25:05)
     * @return java.lang.String
     */
    public java.lang.String getCIVILITE() {
        return CIVILITE;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_TYP_VENT() {
        return LIB_TYP_VENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 09:27:53)
     * @return java.lang.String
     */
    public java.lang.String getMARQUE() {
        return MARQUE;
    }
    
	/**
	 * @return the tVA_SUPPL_SUR_HT
	 */
	public java.lang.String getTVA_SUPPL_SUR_HT() {
		return TVA_SUPPL_SUR_HT;
	}

	/**
	 * @return whether tVA_SUPPL_SUR_HT
	 */
	public boolean isTVA_SUPPL_SUR_HT() {
		return (TVA_SUPPL_SUR_HT != null) && TVA_SUPPL_SUR_HT.equals("O");
	}

    /**
     * Création d'un Bean Type de Vente à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_TYP_VENT java.lang.String
     * @return TypVentBean
     */
    public static TypVentBean getTypVentBean(DBSession dbConnect, String CD_TYP_VENT) {
        String reqSQL = "select * from TYP_VENT where CD_TYP_VENT=" + CD_TYP_VENT;
        TypVentBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new TypVentBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    
    /**
     * Création d'une liste des Beans Type de Vente ayant une marque (un article associé)
     * Creation date: 19 janv. 2003
     * @param dbConnect com.increg.salon.bean.DBSession
     * @return Vector
     */
    public static Vector getLstTypVentMarque(DBSession dbConnect) {
        String reqSQL = "select * from TYP_VENT where MARQUE='O'";

        Vector liste = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                TypVentBean res = null;
                res = new TypVentBean(aRS);
                liste.add(res);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return liste;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_TYP_VENT int
     */
    public void setCD_TYP_VENT(int newCD_TYP_VENT) {
        CD_TYP_VENT = newCD_TYP_VENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_TYP_VENT String
     */
    public void setCD_TYP_VENT(String newCD_TYP_VENT) {
        if ((newCD_TYP_VENT != null) && (newCD_TYP_VENT.length() != 0)) {
            CD_TYP_VENT = Integer.parseInt(newCD_TYP_VENT);
        } else {
            CD_TYP_VENT = 0;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:25:05)
     * @param newCIVILITE java.lang.String
     */
    public void setCIVILITE(java.lang.String newCIVILITE) {
        CIVILITE = newCIVILITE;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_TYP_VENT java.lang.String
     */
    public void setLIB_TYP_VENT(java.lang.String newLIB_TYP_VENT) {
        LIB_TYP_VENT = newLIB_TYP_VENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 09:27:53)
     * @param newMARQUE java.lang.String
     */
    public void setMARQUE(java.lang.String newMARQUE) {
        MARQUE = newMARQUE;
    }
    
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getLIB_TYP_VENT();
    }
    /**
     * @return Returns the cD_TVA.
     */
    public int getCD_TVA() {
        return CD_TVA;
    }
    /**
     * @param cd_tva The cD_TVA to set.
     */
    public void setCD_TVA(int cd_tva) {
        CD_TVA = cd_tva;
    }
    /**
     * @param cd_tva The cD_TVA to set.
     */
    public void setCD_TVA(String cd_tva) {
        if ((cd_tva != null) && (cd_tva.length() != 0)) {
            CD_TVA = Integer.parseInt(cd_tva);
        } else {
            CD_TVA = 0;
        }
    }

    /**
     * @return Returns the CD_TVA_SUPPL.
     */
    public int getCD_TVA_SUPPL() {
        return CD_TVA_SUPPL;
    }
    /**
     * @param cd_tva The cD_TVA_SUPLL to set.
     */
    public void setCD_TVA_SUPPL(int cd_tva) {
        CD_TVA_SUPPL = cd_tva;
    }

    /**
     * @param cd_tva The CD_TVA_SUPPL to set.
     */
    public void setCD_TVA_SUPPL(String cd_tva) {
        if ((cd_tva != null) && (cd_tva.length() != 0)) {
            CD_TVA_SUPPL = Integer.parseInt(cd_tva);
        } else {
            CD_TVA_SUPPL = 0;
        }
    }

	/**
	 * @param tva_suppl_sur_ht the tVA_SUPPL_SUR_HT to set
	 */
	public void setTVA_SUPPL_SUR_HT(java.lang.String tva_suppl_sur_ht) {
		TVA_SUPPL_SUR_HT = tva_suppl_sur_ht;
	}
    
}
