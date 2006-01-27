/*
 * Bean assurant la gestion du catalogue fournisseur (articles fournis par un fournisseur)
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

import java.sql.*;
import java.util.*;
import java.math.*;
import com.increg.commun.*;
import com.increg.util.*;
/**
 * Catalogue d'un fournisseur
 * Creation date: (03/09/2001 12:43:32)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CatFournBean extends TimeStampBean {
	protected int CD_FOURN;
	protected long CD_ART;
	protected java.lang.String REF_ART;
	protected java.lang.String LIB_ART;
	protected int CD_UNIT_MES;
	protected java.math.BigDecimal QTE_CMD_MIN;
	protected java.math.BigDecimal PRX_UNIT_HT;
	protected java.lang.String COMM;
	protected String FOURN_PRINC;

	/**
	 * CatFournBean constructor comment.
	 */
	public CatFournBean() {
		super();
	}
	/**
	 * CatFournBean constructor comment.
	 * @param rs java.sql.ResultSet
	 */
	public CatFournBean(ResultSet rs) {
		super(rs);
		try {
			CD_ART = rs.getLong("CD_ART");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
		try {
			CD_FOURN = rs.getInt("CD_FOURN");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
		try {
			CD_UNIT_MES = rs.getInt("CD_UNIT_MES");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
		try {
			COMM = rs.getString("COMM");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
	
		try {
			FOURN_PRINC = rs.getString("FOURN_PRINC");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
		try {
			LIB_ART = rs.getString("LIB_ART");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
		try {
			PRX_UNIT_HT = rs.getBigDecimal("PRX_UNIT_HT", 2);
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
			}
		}
		try {
			QTE_CMD_MIN = rs.getBigDecimal("QTE_CMD_MIN", 2);
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
				System.out.println("Erreur dans CatFournBean (RS) : " + e.toString());
			}
		}
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void create(DBSession dbConnect) throws SQLException {
	
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("insert into CAT_FOURN ");
		StringBuffer colonne = new StringBuffer("(");
		StringBuffer valeur = new StringBuffer(" values ( ");
		
		if (CD_ART != 0) {
			colonne.append("CD_ART,");
			valeur.append(CD_ART);
			valeur.append(",");
		}
		
		if (CD_FOURN != 0) {
			colonne.append("CD_FOURN,");
			valeur.append(CD_FOURN);
			valeur.append(",");
		}
		
		if (CD_UNIT_MES != 0) {
			colonne.append("CD_UNIT_MES,");
			valeur.append(CD_UNIT_MES);
			valeur.append(",");
		}
	
		if ((COMM != null) && (COMM.length() != 0)) {
			colonne.append("COMM,");
			valeur.append(DBSession.quoteWith(COMM, '\''));
			valeur.append(",");
		}
	
		if ((FOURN_PRINC != null) && (FOURN_PRINC.length() != 0)) {
			colonne.append("FOURN_PRINC,");
			valeur.append(DBSession.quoteWith(FOURN_PRINC, '\''));
			valeur.append(",");
		}
	
		if ((LIB_ART != null) && (LIB_ART.length() != 0)) {
			colonne.append("LIB_ART,");
			valeur.append(DBSession.quoteWith(LIB_ART, '\''));
			valeur.append(",");
		}
	
		if (PRX_UNIT_HT != null) {
			colonne.append("PRX_UNIT_HT,");
			valeur.append(PRX_UNIT_HT.toString());
			valeur.append(",");
		}
	
		if (QTE_CMD_MIN != null) {
			colonne.append("QTE_CMD_MIN,");
			valeur.append(QTE_CMD_MIN.toString());
			valeur.append(",");
		}
	
		if ((REF_ART != null) && (REF_ART.length() != 0)) {
			colonne.append("REF_ART,");
			valeur.append(DBSession.quoteWith(REF_ART, '\''));
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
		colonne.setLength(colonne.length()-1);
		valeur.setLength(valeur.length()-1);
	
		// Constitue la requete finale
		req.append(colonne);
		req.append(")");
		req.append(valeur);
		req.append(")");
	
		// Execute la création
		String[] reqs = new String[1];
		reqs[0] = req.toString();
		int[] nb= new int[1];
		nb = dbConnect.doExecuteSQL(reqs);
	
		if (nb[0] != 1) {
			throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
		}	
	
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void delete(DBSession dbConnect) throws SQLException {
	
		StringBuffer req = new StringBuffer("delete from CAT_FOURN ");
		StringBuffer where = new StringBuffer(" where CD_ART=" + CD_ART + " and CD_FOURN=" + CD_FOURN + " and QTE_CMD_MIN=" + QTE_CMD_MIN + "::numeric(5,2)");
		
		// Constitue la requete finale
		req.append(where);
	
		// Execute la création
		String[] reqs = new String[1];
		reqs[0] = req.toString();
		int[] nb= new int[1];
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
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void maj(DBSession dbConnect) throws SQLException {
	
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("update CAT_FOURN set ");
		StringBuffer colonne = new StringBuffer("");
		StringBuffer where = new StringBuffer(" where CD_ART=" + CD_ART + " and CD_FOURN=" + CD_FOURN + " and QTE_CMD_MIN=" + QTE_CMD_MIN + "::numeric(5,2)");
	
		colonne.append("CD_UNIT_MES=");
		if (CD_UNIT_MES != 0) {
			colonne.append(CD_UNIT_MES);
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
	
		colonne.append("FOURN_PRINC=");
		if ((FOURN_PRINC != null) && (FOURN_PRINC.length() != 0)) {
			colonne.append(DBSession.quoteWith(FOURN_PRINC, '\''));
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
	
		colonne.append("PRX_UNIT_HT=");
		if (PRX_UNIT_HT != null) {
			colonne.append(PRX_UNIT_HT.toString());
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("QTE_CMD_MIN=");
		if (QTE_CMD_MIN != null) {
			colonne.append(QTE_CMD_MIN.toString());
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
		return "";
	}
	
	/**
	 * Création d'un Bean Article à partir de sa clé
	 * Creation date: (19/08/2001 21:14:20)
	 * @param dbConnect com.increg.salon.bean.DBSession
	 * @param CD_ART java.lang.String
	 * @param CD_FOURN java.lang.String
	 * @param QTE_CMD_MIN java.lang.String
	 */
	public static CatFournBean getCatFournBean(DBSession dbConnect, String CD_ART, String CD_FOURN, String QTE_CMD_MIN) {
		String reqSQL = "select * from CAT_FOURN where CD_ART=" + CD_ART + " and CD_FOURN=" + CD_FOURN + " and QTE_CMD_MIN=" + QTE_CMD_MIN + "::numeric(5,2)";
		CatFournBean res = null;
	
		// Interroge la Base
		try {
			ResultSet aRS = dbConnect.doRequest(reqSQL);
	
			while (aRS.next()) {
				res = new CatFournBean(aRS);
			}
			aRS.close();
		}
		catch (Exception e) {
			System.out.println("Erreur dans constructeur sur clé : " + e.toString());
		}
		return res;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @return long
	 */
	public long getCD_ART() {
		return CD_ART;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @return int
	 */
	public int getCD_FOURN() {
		return CD_FOURN;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 13:01:39)
	 * @return java.lang.String
	 */
	public java.lang.String getFOURN_PRINC() {
		return FOURN_PRINC;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @return java.lang.String
	 */
	public java.lang.String getLIB_ART() {
		return LIB_ART;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getPRX_UNIT_HT() {
		return PRX_UNIT_HT;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getQTE_CMD_MIN() {
		return QTE_CMD_MIN;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @return java.lang.String
	 */
	public java.lang.String getREF_ART() {
		return REF_ART;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @param newCD_ART long
	 */
	public void setCD_ART(long newCD_ART) {
		CD_ART = newCD_ART;
	}/**
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
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @param newCD_FOURN int
	 */
	public void setCD_FOURN(int newCD_FOURN) {
		CD_FOURN = newCD_FOURN;
	}/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @param newCD_FOURN String
	 */
	public void setCD_FOURN(String newCD_FOURN) {
		if ((newCD_FOURN != null) && (newCD_FOURN.length() != 0)) {
			CD_FOURN = Integer.parseInt(newCD_FOURN);
		}
		else {
			CD_FOURN = 0;
		}
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 13:01:39)
	 * @param newFOURN_PRINC java.lang.String
	 */
	public void setFOURN_PRINC(java.lang.String newFOURN_PRINC) {
		FOURN_PRINC = newFOURN_PRINC;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @param newLIB_ART java.lang.String
	 */
	public void setLIB_ART(java.lang.String newLIB_ART) {
		LIB_ART = newLIB_ART;
	}/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newPRX_UNIT_HT String
	 */
	public void setPRX_UNIT_HT(String newPRX_UNIT_HT) {
	
		if ((newPRX_UNIT_HT != null) && (newPRX_UNIT_HT.length() != 0)) {
			PRX_UNIT_HT = new BigDecimal(newPRX_UNIT_HT);
		}
		else {
			PRX_UNIT_HT = null;
		}
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @param newPRX_UNIT_HT java.math.BigDecimal
	 */
	public void setPRX_UNIT_HT(java.math.BigDecimal newPRX_UNIT_HT) {
		PRX_UNIT_HT = newPRX_UNIT_HT;
	}/**
	 * Insert the method's description here.
	 * Creation date: (01/09/2001 14:59:04)
	 * @param newQTE_CMD_MIN String
	 */
	public void setQTE_CMD_MIN(String newQTE_CMD_MIN) {
	
		if ((newQTE_CMD_MIN != null) && (newQTE_CMD_MIN.length() != 0)) {
			QTE_CMD_MIN = new BigDecimal(newQTE_CMD_MIN);
		}
		else {
			QTE_CMD_MIN = null;
		}
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @param newQTE_CMD_MIN java.math.BigDecimal
	 */
	public void setQTE_CMD_MIN(java.math.BigDecimal newQTE_CMD_MIN) {
		QTE_CMD_MIN = newQTE_CMD_MIN;
	}/**
	 * Insert the method's description here.
	 * Creation date: (03/09/2001 12:59:59)
	 * @param newREF_ART java.lang.String
	 */
	public void setREF_ART(java.lang.String newREF_ART) {
		REF_ART = newREF_ART;
	}
}
