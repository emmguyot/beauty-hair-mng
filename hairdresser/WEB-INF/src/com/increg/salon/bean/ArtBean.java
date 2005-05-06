/*
 * Bean de gestion des articles
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
import java.util.*;
import java.math.*;
import com.increg.commun.*;
import com.increg.commun.exception.FctlException;
import com.increg.util.*;
/**
 * Article en stock
 * Creation date: (19/08/2001 20:51:29)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ArtBean extends TimeStampBean {
    /**
     * Code de l'article
     */
	protected long CD_ART;
    /**
     * Référence externe de l'article
     */
	protected java.lang.String REF_ART;
    /**
     * Libellé de l'article
     */
	protected java.lang.String LIB_ART;
    /**
     * Libellé de l'article au chargement de l'article
     */
    protected java.lang.String LIB_ART_orig;
    /**
     * Unité de mesure
     */
	protected int CD_UNIT_MES;
    /**
     * Catégorie de l'article
     */
	protected int CD_CATEG_ART;
    /**
     * Quantité de rupture de stock
     */
	protected java.math.BigDecimal QTE_STK_MIN;
    /**
     * Quantité en stock à cet instant
     */
	protected java.math.BigDecimal QTE_STK;
    /**
     * Val unitaire en stock
     */
	protected java.math.BigDecimal VAL_STK_HT;
    /**
     * Commentaire sur l'article
     */
	protected java.lang.String COMM;
    /**
     * Indicateur si l'article est mixte
     */
    protected String INDIC_MIXTE;
    /**
     * Indicateur si l'article est périmé (O/N)
     */
    protected String INDIC_PERIM;
    /**
     * Type de l'article
     */
    protected int CD_TYP_ART;
    /**
     * Type d'article vente
     */
    public static final int TYP_ART_VENT_DETAIL = 1;
    
	/**
	 * ArtBean constructor comment.
	 */
	public ArtBean() {
		super();
        INDIC_MIXTE = "N";
        INDIC_PERIM = "N";
        LIB_ART_orig = "";
	}
	/**
	 * ArtBean constructor comment.
	 * @param rs java.sql.ResultSet
	 */
	public ArtBean(ResultSet rs) {
		super(rs);
		try {
			CD_ART = rs.getLong("CD_ART");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
		try {
			CD_CATEG_ART = rs.getInt("CD_CATEG_ART");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
		try {
			CD_UNIT_MES = rs.getInt("CD_UNIT_MES");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
		try {
			CD_TYP_ART = rs.getInt("CD_TYP_ART");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
		try {
			COMM = rs.getString("COMM");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
	
        try {
            INDIC_MIXTE = rs.getString("INDIC_MIXTE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
            }
        }
        try {
            INDIC_PERIM = rs.getString("INDIC_PERIM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
            }
        }
		try {
			LIB_ART = rs.getString("LIB_ART");
            LIB_ART_orig = LIB_ART;
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
		try {
			QTE_STK = rs.getBigDecimal("QTE_STK", 2);
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
			}
		}
		try {
			QTE_STK_MIN = rs.getBigDecimal("QTE_STK_MIN", 2);
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
			}
		}
		try {
			REF_ART = rs.getString("REF_ART");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ArtBean (RS) : " + e.toString());
			}
		}
		try {
			VAL_STK_HT = rs.getBigDecimal("VAL_STK_HT", 2);
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
			}
		}
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void create(DBSession dbConnect) throws SQLException {
	
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("insert into ART ");
		StringBuffer colonne = new StringBuffer("(");
		StringBuffer valeur = new StringBuffer(" values ( ");
		
		if (CD_ART == 0) {
			/**
			 * Numérotation automatique des prestations
			 */
			String reqMax = "select nextval('SEQ_ART')";
			try {
				ResultSet aRS = dbConnect.doRequest(reqMax);
				CD_ART = 1; // Par défaut
	
				while (aRS.next()) {
					CD_ART = aRS.getLong(1);
				}
				aRS.close();
			}
			catch (Exception e) {
				System.out.println("Erreur dans reqSeq : " + e.toString());
			}
		}
		colonne.append("CD_ART,");
		valeur.append(CD_ART);
		valeur.append(",");
	
		if (CD_CATEG_ART != 0) {
			colonne.append("CD_CATEG_ART,");
			valeur.append(CD_CATEG_ART);
			valeur.append(",");
		}
		
		if (CD_UNIT_MES != 0) {
			colonne.append("CD_UNIT_MES,");
			valeur.append(CD_UNIT_MES);
			valeur.append(",");
		}
	
		if (CD_TYP_ART != 0) {
			colonne.append("CD_TYP_ART,");
			valeur.append(CD_TYP_ART);
			valeur.append(",");
		}
	
		if ((COMM != null) && (COMM.length() != 0)) {
			colonne.append("COMM,");
			valeur.append(DBSession.quoteWith(COMM, '\''));
			valeur.append(",");
		}
	
        if ((INDIC_MIXTE != null) && (INDIC_MIXTE.length() != 0)) {
            colonne.append("INDIC_MIXTE,");
            valeur.append(DBSession.quoteWith(INDIC_MIXTE, '\''));
            valeur.append(",");
        }
    
        if ((INDIC_PERIM != null) && (INDIC_PERIM.length() != 0)) {
            colonne.append("INDIC_PERIM,");
            valeur.append(DBSession.quoteWith(INDIC_PERIM, '\''));
            valeur.append(",");
        }
    
		if ((LIB_ART != null) && (LIB_ART.length() != 0)) {
			colonne.append("LIB_ART,");
			valeur.append(DBSession.quoteWith(LIB_ART, '\''));
			valeur.append(",");
		}
	
		if (QTE_STK != null) {
			colonne.append("QTE_STK,");
			valeur.append(QTE_STK.toString());
			valeur.append(",");
		}
	
		if (QTE_STK_MIN != null) {
			colonne.append("QTE_STK_MIN,");
			valeur.append(QTE_STK_MIN.toString());
			valeur.append(",");
		}
	
		if ((REF_ART != null) && (REF_ART.length() != 0)) {
			colonne.append("REF_ART,");
			valeur.append(DBSession.quoteWith(REF_ART, '\''));
			valeur.append(",");
		}
	
		if (VAL_STK_HT != null) {
			colonne.append("VAL_STK_HT,");
			valeur.append(VAL_STK_HT.toString());
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
	
		StringBuffer req = new StringBuffer("delete from ART ");
		StringBuffer where = new StringBuffer(" where CD_ART=" + CD_ART);
		
		// Constitue la requete finale
		req.append(where);
	
		StringBuffer reqFourn = new StringBuffer("delete from CAT_FOURN ");
		
		// Constitue la requete finale
		reqFourn.append(where);
	
		// Execute la création
		String[] reqs = new String[2];
		reqs[0] = reqFourn.toString();
		reqs[1] = req.toString();
		int[] nb = new int[2];
		nb = dbConnect.doExecuteSQL(reqs);
	
		if (nb[1] != 1) {
			throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
		}	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @return long
	 */
	public long getCD_ART() {
		return CD_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @return int
	 */
	public int getCD_CATEG_ART() {
		return CD_CATEG_ART;
	}
	
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @return int
	 */
	public int getCD_UNIT_MES() {
		return CD_UNIT_MES;
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
	 * Création d'un Bean Article à partir de sa clé
	 * Creation date: (19/08/2001 21:14:20)
	 * @param dbConnect com.increg.salon.bean.DBSession
	 * @param CD_ART java.lang.String
	 * @return Article Correspondant
	 */
	public static ArtBean getArtBean(DBSession dbConnect, String CD_ART) {
		String reqSQL = "select * from ART where CD_ART=" + CD_ART;
		ArtBean res = null;
	
		// Interroge la Base
		try {
			ResultSet aRS = dbConnect.doRequest(reqSQL);
	
			while (aRS.next()) {
				res = new ArtBean(aRS);
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
	
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("update ART set ");
		StringBuffer colonne = new StringBuffer("");
		StringBuffer where = new StringBuffer(" where CD_ART=" + CD_ART);
	
		colonne.append("CD_CATEG_ART=");
		if (CD_CATEG_ART != 0) {
			colonne.append(CD_CATEG_ART);
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("CD_UNIT_MES=");
		if (CD_UNIT_MES != 0) {
			colonne.append(CD_UNIT_MES);
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("CD_TYP_ART=");
		if (CD_TYP_ART != 0) {
			colonne.append(CD_TYP_ART);
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
	
        colonne.append("INDIC_MIXTE=");
        if ((INDIC_MIXTE != null) && (INDIC_MIXTE.length() != 0)) {
            colonne.append(DBSession.quoteWith(INDIC_MIXTE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");
    
        colonne.append("INDIC_PERIM=");
        if ((INDIC_PERIM != null) && (INDIC_PERIM.length() != 0)) {
            colonne.append(DBSession.quoteWith(INDIC_PERIM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");
    
		colonne.append("LIB_ART=");
		if ((LIB_ART != null) && (LIB_ART.length() != 0)) {
			colonne.append(DBSession.quoteWith(LIB_ART, '\''));
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("QTE_STK=");
		if (QTE_STK != null) {
			colonne.append(QTE_STK.toString());
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("QTE_STK_MIN=");
		if (QTE_STK_MIN != null) {
			colonne.append(QTE_STK_MIN.toString());
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("REF_ART=");
		if ((REF_ART != null) && (REF_ART.length() != 0)) {
			colonne.append(DBSession.quoteWith(REF_ART, '\''));
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("VAL_STK_HT=");
		if (VAL_STK_HT != null) {
			colonne.append(VAL_STK_HT.toString());
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
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_ART long
	 */
	public void setCD_ART(long newCD_ART) {
		CD_ART = newCD_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_ART String
	 */
	public void setCD_ART(String newCD_ART) {
		if ((newCD_ART != null) && (newCD_ART.length() != 0)) {
			CD_ART = Long.parseLong(newCD_ART);
		}
		else {
			CD_ART = 0;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_CATEG_ART int
	 */
	public void setCD_CATEG_ART(int newCD_CATEG_ART) {
		CD_CATEG_ART = newCD_CATEG_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_CATEG_ART String
	 */
	public void setCD_CATEG_ART(String newCD_CATEG_ART) {
		if ((newCD_CATEG_ART != null) && (newCD_CATEG_ART.length() != 0)) {
			CD_CATEG_ART = Integer.parseInt(newCD_CATEG_ART);
		}
		else {
			CD_CATEG_ART = 0;
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_UNIT_MES int
	 */
	public void setCD_UNIT_MES(int newCD_UNIT_MES) {
		CD_UNIT_MES = newCD_UNIT_MES;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_UNIT_MES String
	 */
	public void setCD_UNIT_MES(String newCD_UNIT_MES) {
		if ((newCD_UNIT_MES != null) && (newCD_UNIT_MES.length() != 0)) {
			CD_UNIT_MES = Integer.parseInt(newCD_UNIT_MES);
		}
		else {
			CD_UNIT_MES = 0;
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
		return getLIB_ART();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 15:36:14)
	 * @return int
	 */
	public int getCD_TYP_ART() {
		return CD_TYP_ART;
    }
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @return java.lang.String
	 */
	public java.lang.String getLIB_ART() {
	    return LIB_ART;
    }
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getQTE_STK() {
		return QTE_STK;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getQTE_STK_MIN() {
		return QTE_STK_MIN;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @return java.lang.String
	 */
	public java.lang.String getREF_ART() {
	    return REF_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getVAL_STK_HT() {
		return VAL_STK_HT;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 15:36:14)
	 * @param newCD_TYP_ART int
	 */
	public void setCD_TYP_ART(int newCD_TYP_ART) {
		CD_TYP_ART = newCD_TYP_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 15:36:14)
	 * @param newCD_TYP_ART String
	 */
	public void setCD_TYP_ART(String newCD_TYP_ART) {
		if ((newCD_TYP_ART != null) && (newCD_TYP_ART.length() != 0)) {
			CD_TYP_ART = Integer.parseInt(newCD_TYP_ART);
		}
		else {
			CD_TYP_ART = 0;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newLIB_ART java.lang.String
	 */
	public void setLIB_ART(java.lang.String newLIB_ART) {
		LIB_ART = newLIB_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newQTE_STK String
	 */
	public void setQTE_STK(String newQTE_STK) {
	
		if ((newQTE_STK != null) && (newQTE_STK.length() != 0)) {
			QTE_STK = new BigDecimal(newQTE_STK);
		}
		else {
			QTE_STK = null;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newQTE_STK java.math.BigDecimal
	 */
	public void setQTE_STK(java.math.BigDecimal newQTE_STK) {
		QTE_STK = newQTE_STK;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newQTE_STK_MIN String
	 */
	public void setQTE_STK_MIN(String newQTE_STK_MIN) {
	
		if ((newQTE_STK_MIN != null) && (newQTE_STK_MIN.length() != 0)) {
			QTE_STK_MIN = new BigDecimal(newQTE_STK_MIN);
		}
		else {
			QTE_STK_MIN = null;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newQTE_STK_MIN java.math.BigDecimal
	 */
	public void setQTE_STK_MIN(java.math.BigDecimal newQTE_STK_MIN) {
		QTE_STK_MIN = newQTE_STK_MIN;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newREF_ART java.lang.String
	 */
	public void setREF_ART(java.lang.String newREF_ART) {
		REF_ART = newREF_ART;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newVAL_STK_HT String
	 */
	public void setVAL_STK_HT(String newVAL_STK_HT) {
	
		if ((newVAL_STK_HT != null) && (newVAL_STK_HT.length() != 0)) {
			VAL_STK_HT = new BigDecimal(newVAL_STK_HT);
		}
		else {
			VAL_STK_HT = null;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newVAL_STK_HT java.math.BigDecimal
	 */
	public void setVAL_STK_HT(java.math.BigDecimal newVAL_STK_HT) {
		VAL_STK_HT = newVAL_STK_HT;
	}

	/**
	 * Gets the iNDIC_MIXTE.
	 * @return Returns a String
	 */
	public String getINDIC_MIXTE() {
		return INDIC_MIXTE;
	}

	/**
	 * Sets the iNDIC_MIXTE.
	 * @param iNDIC_MIXTE The iNDIC_MIXTE to set
	 */
	public void setINDIC_MIXTE(String iNDIC_MIXTE) {
        // La mixité n'a de sens que pour les articles ventes
        if (CD_TYP_ART == TYP_ART_VENT_DETAIL) {
    		INDIC_MIXTE = iNDIC_MIXTE;
        }
	}
    
    /**
     * Gets the iNDIC_PERIM.
     * @return Returns a String
     */
    public String getINDIC_PERIM() {
        return INDIC_PERIM;
    }

    /**
     * Sets the iNDIC_PERIM.
     * @param iNDIC_PERIM The iNDIC_PERIM to set
     */
    public void setINDIC_PERIM(String iNDIC_PERIM) {
        INDIC_PERIM = iNDIC_PERIM;
    }
    
    /**
     * Création automatique de la prestation
     * Creation date: (14/10/2001 22:05:39)
     * @param myDBSession com.increg.salon.bean.DBSession
     * @exception java.lang.Exception The exception description.
     */
    public void creationPrestation(DBSession myDBSession) throws java.lang.Exception {
        PrestBean aPrest = new PrestBean();
    
        aPrest.setLIB_PREST(LIB_ART);
        aPrest.setCD_TYP_VENT(PrestBean.TYP_VENT_DETAIL);
        aPrest.setPRX_UNIT_TTC("0");
        aPrest.setCD_UNIT_MES(CD_UNIT_MES);
        aPrest.setCD_ART(CD_ART);
        aPrest.setINDIC_PERIM(INDIC_PERIM);
        aPrest.setCOMM(BasicSession.TAG_I18N + "artBean.commentaireDuplication" + BasicSession.TAG_I18N);
    
        aPrest.create(myDBSession);
    }
    
    /**
     * Synchronise la prestation avec l'article au niveau de l'indicateur de péremption
     * et éventuellement du libellé
     * @param myDBSession Connexion à la base à utiliser
     * @throws SQLException En cas d'erreur de mise à jour de la prestation
     */
    public void syncWithPrest (DBSession myDBSession) throws SQLException {
        
        PrestBean aPrest = PrestBean.getPrestBeanFromArt(myDBSession, Long.toString(CD_ART));
        
        if (aPrest != null) {
            // Mets à jour la prestation
            aPrest.setINDIC_PERIM(INDIC_PERIM);
            if (aPrest.getLIB_PREST().equals(LIB_ART_orig)) {
                // Il faut mettre à jour le libellé aussi
                aPrest.setLIB_PREST(LIB_ART);
                aPrest.setCD_UNIT_MES(CD_UNIT_MES);
            }
            aPrest.maj(myDBSession);
        }
    }

    /**
     * Purge des articles
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les articles n'ayant plus de mouvement et créé avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {
        
        int nbEnreg = -1;
        
        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String reqSQL[] = new String[2];
         
        reqSQL[0] = "delete from CAT_FOURN where CD_ART in "
                + " (select CD_ART from ART where DT_CREAT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'')
                + " and (select count(*) from MVT_STK where CD_ART = ART.CD_ART) = 0"
                + " and (select count(*) from PREST where CD_ART = ART.CD_ART) = 0)";
        
        reqSQL[1] = "delete from ART where DT_CREAT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'')
                + " and (select count(*) from MVT_STK where CD_ART = ART.CD_ART) = 0"
                + " and (select count(*) from PREST where CD_ART = ART.CD_ART) = 0";

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);
            
            nbEnreg = res[1];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des articles: " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "artBean.purgeKo" + BasicSession.TAG_I18N);
        }
        
        // Fin de cette transaction
        dbConnect.endTransaction();
        
        return nbEnreg;
    }
    
    
}
