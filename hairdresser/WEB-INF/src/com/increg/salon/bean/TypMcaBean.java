/*
 * Type de mouvements de caisse
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
import com.increg.commun.*;

/**
 * Type de mouvements de caisse
 * Creation date: (25/09/2001 13:24:28)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TypMcaBean extends GenericBean {
    /**
     * Type du mouvement de caisse
     */
	protected int CD_TYP_MCA;
    /**
     * Libellé du mouvement de caisse
     */
	protected java.lang.String LIB_TYP_MCA;
    /**
     * Sens du mouvement de caisse
     */
	protected java.lang.String SENS_MCA;
    /**
     * Sens de sortie de la caisse
     */
	public static final String SENS_SORTIE = "S";
    /**
     * Sens d'entrée dans la caisse
     */
	public static final String SENS_ENTREE = "E";
    /**
     * Sens Inventaire de la caisse
     */
	public static final String SENS_INVENTAIRE = "I";
    /**
     * Code du mouvement de caisse ENCAISSEMENT
     */
	public static final int ENCAISSEMENT = 1;
    
/**
 * TypMcaBean constructor comment.
 */
public TypMcaBean() {
	super();
}
/**
 * TypMcaBean constructor comment.
 * @param rs java.sql.ResultSet
 */
public TypMcaBean(ResultSet rs) {
	super(rs);
	try {
		CD_TYP_MCA = rs.getInt("CD_TYP_MCA");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TypMcaBean (RS) : " + e.toString());
		}
	}
	try {
		LIB_TYP_MCA = rs.getString("LIB_TYP_MCA");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TypMcaBean (RS) : " + e.toString());
		}
	}

	try {
		SENS_MCA = rs.getString("SENS_MCA");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans TypMcaBean (RS) : " + e.toString());
		}
	}
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void create(DBSession dbConnect) throws SQLException {

	StringBuffer req = new StringBuffer("insert into TYP_MCA ");
	StringBuffer colonne = new StringBuffer("(");
	StringBuffer valeur = new StringBuffer(" values ( ");
	
	if (CD_TYP_MCA == 0) {
		/**
		 * Numérotation automatique des prestations
		 */
		String reqMax = "select nextval('SEQ_TYP_MCA')";
		try {
			ResultSet aRS = dbConnect.doRequest(reqMax);
			CD_TYP_MCA = 1; // Par défaut

			while (aRS.next()) {
				CD_TYP_MCA = aRS.getInt(1);
			}
			aRS.close();
		}
		catch (Exception e) {
			System.out.println("Erreur dans reqSeq : " + e.toString());
		}
	}
	colonne.append("CD_TYP_MCA,");
	valeur.append(CD_TYP_MCA);
	valeur.append(",");

	if ((LIB_TYP_MCA != null) && (LIB_TYP_MCA.length() != 0)) {
		colonne.append("LIB_TYP_MCA,");
		valeur.append(DBSession.quoteWith(LIB_TYP_MCA, '\''));
		valeur.append(",");
	}

	if ((SENS_MCA != null) && (SENS_MCA.length() != 0)) {
		colonne.append("SENS_MCA,");
		valeur.append(DBSession.quoteWith(SENS_MCA, '\''));
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

	StringBuffer req = new StringBuffer("delete from TYP_MCA ");
	StringBuffer where = new StringBuffer(" where CD_TYP_MCA=" + CD_TYP_MCA);
	
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
public int getCD_TYP_MCA() {
	return CD_TYP_MCA;
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:52:52)
 * @return java.lang.String
 */
public java.lang.String getLIB_TYP_MCA() {
	return LIB_TYP_MCA;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2001 09:27:53)
 * @return java.lang.String
 */
public java.lang.String getSENS_MCA() {
	return SENS_MCA;
}
/**
 * Création d'un Bean Type de Vente à partir de sa clé
 * Creation date: (19/08/2001 21:14:20)
 * @param dbConnect com.increg.salon.bean.DBSession
 * @param CD_TYP_MCA java.lang.String
 * @return TypMcaBean
 */
public static TypMcaBean getTypMcaBean(DBSession dbConnect, String CD_TYP_MCA) {
	String reqSQL = "select * from TYP_MCA where CD_TYP_MCA=" + CD_TYP_MCA;
	TypMcaBean res = null;

	// Interroge la Base
	try {
		ResultSet aRS = dbConnect.doRequest(reqSQL);

		while (aRS.next()) {
			res = new TypMcaBean(aRS);
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

	StringBuffer req = new StringBuffer("update TYP_MCA set ");
	StringBuffer colonne = new StringBuffer("");
	StringBuffer where = new StringBuffer(" where CD_TYP_MCA=" + CD_TYP_MCA);

	colonne.append("LIB_TYP_MCA=");
	if ((LIB_TYP_MCA != null) && (LIB_TYP_MCA.length() != 0)) {
		colonne.append(DBSession.quoteWith(LIB_TYP_MCA, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("SENS_MCA=");
	if ((SENS_MCA != null) && (SENS_MCA.length() != 0)) {
		colonne.append(DBSession.quoteWith(SENS_MCA, '\''));
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
 * @param newCD_TYP_MCA int
 */
public void setCD_TYP_MCA(int newCD_TYP_MCA) {
	CD_TYP_MCA = newCD_TYP_MCA;
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:55:16)
 * @param newCD_TYP_MCA String
 */
public void setCD_TYP_MCA(String newCD_TYP_MCA) {
	if ((newCD_TYP_MCA != null) && (newCD_TYP_MCA.length() != 0)) {
		CD_TYP_MCA = Integer.parseInt(newCD_TYP_MCA);
	}
	else {
		CD_TYP_MCA = 0;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:52:52)
 * @param newLIB_TYP_MCA java.lang.String
 */
public void setLIB_TYP_MCA(java.lang.String newLIB_TYP_MCA) {
	LIB_TYP_MCA = newLIB_TYP_MCA;
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2001 09:27:53)
 * @param newSENS_MCA java.lang.String
 */
public void setSENS_MCA(java.lang.String newSENS_MCA) {
	SENS_MCA = newSENS_MCA;
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public java.lang.String toString() {
	return getLIB_TYP_MCA();
}

/**
 * Vérification que le sens du type de mouvement est modifiable
 * Il ne l'est plus dès que le type à été utilisé, sinon l'intégrité est mise en cause
 * @param dbConnect Connexion à la base à utiliser
 * @return true si le sens de ce mouvement est modifiable
 */
public boolean sensIsModifiable(DBSession dbConnect) {

    String reqSQL = "select count(*) as compte from MVT_CAISSE where CD_TYP_MCA=" + Integer.toString(CD_TYP_MCA);

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
