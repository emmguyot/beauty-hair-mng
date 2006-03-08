/*
 * Bean de gestion tranches d'ages
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
import com.increg.commun.*;
/**
 * Bean de gestion tranches d'ages
 * Creation date: (16/12/2001 21:02:31)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TrAgeBean extends GenericBean {
	protected int CD_TR_AGE;
	protected java.lang.String LIB_TR_AGE;

/**
 * TrAgeBean constructor comment.
 */
public TrAgeBean() {
	
}
/**
 * TrAgeBean constructor comment.
 * @param rs java.sql.ResultSet
 */
public TrAgeBean(ResultSet rs) {
	super(rs);
	try {
		CD_TR_AGE = rs.getInt("CD_TR_AGE");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TrAgeBean (RS) : " + e.toString());
		}
	}
	try {
		LIB_TR_AGE = rs.getString("LIB_TR_AGE");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TrAgeBean (RS) : " + e.toString());
		}
	}
	try {
		AGE_MIN = rs.getInt("AGE_MIN");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TrAgeBean (RS) : " + e.toString());
		}
	}
	try {
		AGE_MAX = rs.getInt("AGE_MAX");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TrAgeBean (RS) : " + e.toString());
		}
	}
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void create(DBSession dbConnect) throws SQLException {

	StringBuffer req = new StringBuffer("insert into TR_AGE");
	StringBuffer colonne = new StringBuffer(" (");
	StringBuffer valeur = new StringBuffer(" values ( ");
	
	if (CD_TR_AGE == 0) {
		/**
		 * Numérotation automatique des prestations
		 */
		String reqMax = "select nextval('SEQ_TR_AGE')";
		try {
			ResultSet aRS = dbConnect.doRequest(reqMax);
			CD_TR_AGE = 1; // Par défaut

			while (aRS.next()) {
				CD_TR_AGE = aRS.getInt(1);
			}
			aRS.close();
		}
		catch (Exception e) {
			System.out.println("Erreur dans reqSeq : " + e.toString());
		}
	}
	colonne.append("CD_TR_AGE,");
	valeur.append(CD_TR_AGE);
	valeur.append(",");

	if ((LIB_TR_AGE != null) && (LIB_TR_AGE.length() != 0)) {
		colonne.append("LIB_TR_AGE,");
		valeur.append(DBSession.quoteWith(LIB_TR_AGE, '\''));
		valeur.append(",");
	}

	colonne.append("AGE_MIN,");
	valeur.append(AGE_MIN);
	valeur.append(",");

	colonne.append("AGE_MAX,");
	valeur.append(AGE_MAX);
	valeur.append(",");

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

	StringBuffer req = new StringBuffer("delete from TR_AGE");
	StringBuffer where = new StringBuffer(" where CD_TR_AGE =" + CD_TR_AGE);
	
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
public int getCD_TR_AGE() {
	return CD_TR_AGE;
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:52:52)
 * @return java.lang.String
 */
public java.lang.String getLIB_TR_AGE() {
	return LIB_TR_AGE;
}
/**
 * Création d'un Bean Type de Vente à partir de sa clé
 * Creation date: (19/08/2001 21:14:20)
 * @param dbConnect com.increg.salon.bean.DBSession
 * @param CD_TR_AGE java.lang.String
 */
public static TrAgeBean getTrAgeBean(DBSession dbConnect, String CD_TR_AGE) {
	String reqSQL = "select * from TR_AGE where CD_TR_AGE=" + CD_TR_AGE;
	TrAgeBean res = null;

	// Interroge la Base
	try {
		ResultSet aRS = dbConnect.doRequest(reqSQL);

		while (aRS.next()) {
			res = new TrAgeBean(aRS);
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

	StringBuffer req = new StringBuffer("update TR_AGE set ");
	StringBuffer colonne = new StringBuffer("");
	StringBuffer where = new StringBuffer(" where CD_TR_AGE=" + CD_TR_AGE);

	colonne.append("LIB_TR_AGE=");
	if ((LIB_TR_AGE != null) && (LIB_TR_AGE.length() != 0)) {
		colonne.append(DBSession.quoteWith(LIB_TR_AGE, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");
	
	colonne.append("AGE_MIN=");
	colonne.append(AGE_MIN);
	colonne.append(",");
	
	colonne.append("AGE_MAX=");
	colonne.append(AGE_MAX);

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
 * @param newCD_TR_AGE_CATEG_ART int
 */
public void setCD_TR_AGE(int newCD_TR_AGE) {
	CD_TR_AGE = newCD_TR_AGE;
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:55:16)
 * @param newCD_TR_AGE String
 */
public void setCD_TR_AGE(String newCD_TR_AGE) {
	if ((newCD_TR_AGE != null) && (newCD_TR_AGE.length() != 0)) {
		CD_TR_AGE = Integer.parseInt(newCD_TR_AGE);	}
	else {
		CD_TR_AGE = 0;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:52:52)
 * @param newLIB_TR_AGE java.lang.String
 */
public void setLIB_TR_AGE(java.lang.String newLIB_TR_AGE) {
	LIB_TR_AGE = newLIB_TR_AGE;
}

/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public java.lang.String toString() {
	return getLIB_TR_AGE();
}
	protected int AGE_MAX;	protected int AGE_MIN;/**
 * Insert the method's description here.
 * Creation date: (16/12/2001 21:05:01)
 * @return int
 */
public int getAGE_MAX() {
	return AGE_MAX;
}/**
 * Insert the method's description here.
 * Creation date: (16/12/2001 21:04:48)
 * @return int
 */
public int getAGE_MIN() {
	return AGE_MIN;
}/**
 * Création d'un Bean Type de Vente à partir de sa clé
 * Creation date: (19/08/2001 21:14:20)
 * @param dbConnect com.increg.salon.bean.DBSession
 * @param CD_TR_AGE java.lang.String
 */
public static TrAgeBean getTrAgeBean(DBSession dbConnect, Calendar dtNais) {

	// Calcule l'age
	Calendar aujourdhui = Calendar.getInstance();
	long age = (aujourdhui.getTime().getTime() - dtNais.getTime().getTime())/(1000l*60l*60l*24l*365l);
	
	String reqSQL = "select * from TR_AGE where AGE_MIN<=" + Long.toString(age) + " and AGE_MAX>=" + Long.toString(age);
	TrAgeBean res = null;

	// Interroge la Base
	try {
		ResultSet aRS = dbConnect.doRequest(reqSQL);

		while (aRS.next()) {
			res = new TrAgeBean(aRS);
		}
		aRS.close();
	}
	catch (Exception e) {
		System.out.println("Erreur dans constructeur sur clé (recherche par age) : " + e.toString());
	}
	return res;
}/**
 * Insert the method's description here.
 * Creation date: (16/12/2001 21:05:01)
 * @param newAGE_MAX int
 */
public void setAGE_MAX(int newAGE_MAX) {
	AGE_MAX = newAGE_MAX;
}/**
 * Insert the method's description here.
 * Creation date: (16/12/2001 21:05:01)
 * @param newAGE_MAX String
 */
public void setAGE_MAX(String newAGE_MAX) {
	if ((newAGE_MAX != null) && (newAGE_MAX.length() != 0)) {
		AGE_MAX = Integer.parseInt(newAGE_MAX);	}
	else {
		AGE_MAX = 0;
	}
}/**
 * Insert the method's description here.
 * Creation date: (16/12/2001 21:04:48)
 * @param newAGE_MIN int
 */
public void setAGE_MIN(int newAGE_MIN) {
	AGE_MIN = newAGE_MIN;
}/**
 * Insert the method's description here.
 * Creation date: (16/12/2001 21:04:48)
 * @param newAGE_MIN String
 */
public void setAGE_MIN(String newAGE_MIN) {
	if ((newAGE_MIN != null) && (newAGE_MIN.length() != 0)) {
		AGE_MIN = Integer.parseInt(newAGE_MIN);	}
	else {
		AGE_MIN = 0;
	}
}}
