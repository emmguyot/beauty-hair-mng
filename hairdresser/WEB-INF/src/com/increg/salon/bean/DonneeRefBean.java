package com.increg.salon.bean;

import java.sql.*;
import java.util.*;
import com.increg.commun.*;/**
 * Bean générique permettant la manipulation d'un object ayant un code et un libellé
 * Creation date: (24/08/2001 20:52:01)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class DonneeRefBean extends GenericBean {
	protected int CD;
	protected java.lang.String LIB;
	protected java.lang.String nomTable;
/**
 * DonneeRefBean constructor comment.
 */
public DonneeRefBean() {
	System.out.println("Appel de DonneeRefBean() interdit");
}
/**
 * DonneeRefBean constructor comment.
 */
public DonneeRefBean(String nomTable) {
	super();
	this.nomTable = nomTable;
}
/**
 * DonneeRefBean constructor comment.
 * @param rs java.sql.ResultSet
 */
public DonneeRefBean(ResultSet rs) {
	System.out.println("Appel de DonneeRefBean(rs) interdit");
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void create(DBSession dbConnect) throws SQLException {

	StringBuffer req = new StringBuffer("insert into " + nomTable );
	StringBuffer colonne = new StringBuffer(" (");
	StringBuffer valeur = new StringBuffer(" values ( ");
	
	if (CD == 0) {
		/**
		 * Numérotation automatique des prestations
		 */
		String reqMax = "select nextval('SEQ_" + nomTable + "')";
		try {
			ResultSet aRS = dbConnect.doRequest(reqMax);
			CD = 1; // Par défaut

			while (aRS.next()) {
				CD = aRS.getInt(1);
			}
			aRS.close();
		}
		catch (Exception e) {
			System.out.println("Erreur dans reqSeq : " + e.toString());
		}
	}
	colonne.append("CD_" + nomTable + ",");
	valeur.append(CD);
	valeur.append(",");

	if ((LIB != null) && (LIB.length() != 0)) {
		colonne.append("LIB_" + nomTable + ",");
		valeur.append(DBSession.quoteWith(LIB, '\''));
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
		throw (new SQLException("Création non effectuée"));
	}	

}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void delete(DBSession dbConnect) throws SQLException {

	StringBuffer req = new StringBuffer("delete from " + nomTable);
	StringBuffer where = new StringBuffer(" where CD_" + nomTable + "=" + CD);
	
	// Constitue la requete finale
	req.append(where);

	// Execute la création
	String[] reqs = new String[1];
	reqs[0] = req.toString();
	int[] nb= new int[1];
	nb = dbConnect.doExecuteSQL(reqs);

	if (nb[0] != 1) {
		throw (new SQLException("Suppression non effectuée"));
	}	
}

/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:55:16)
 * @return int
 */
public int getCD() {
	return CD;
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:52:52)
 * @return java.lang.String
 */
public java.lang.String getLIB() {
	return LIB;
}
/**
 * Insert the method's description here.
 * Creation date: (02/09/2001 00:13:51)
 * @return java.lang.String
 */
public java.lang.String getNomTable() {
	return nomTable;
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void maj(DBSession dbConnect) throws SQLException {

	StringBuffer req = new StringBuffer("update " + nomTable + " set ");
	StringBuffer colonne = new StringBuffer("");
	StringBuffer where = new StringBuffer(" where CD_" + nomTable + "=" + CD);

	colonne.append("LIB_" + nomTable + "=");
	if ((LIB != null) && (LIB.length() != 0)) {
		colonne.append(DBSession.quoteWith(LIB, '\''));
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
		throw (new SQLException("Mise à jour non effectuée"));
	}

}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:55:16)
 * @param newCD_CATEG_ART int
 */
public void setCD(int newCD) {
	CD = newCD;
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:55:16)
 * @param newCD String
 */
public void setCD(String newCD) {
	if ((newCD != null) && (newCD.length() != 0)) {
		CD = Integer.parseInt(newCD);	}
	else {
		CD = 0;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (19/08/2001 20:52:52)
 * @param newLIB java.lang.String
 */
public void setLIB(java.lang.String newLIB) {
	LIB = newLIB;
}
/**
 * Insert the method's description here.
 * Creation date: (02/09/2001 00:13:51)
 * @param newNomTable java.lang.String
 */
public void setNomTable(java.lang.String newNomTable) {
	nomTable = newNomTable;
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public java.lang.String toString() {
	return getLIB();
}
/**
 * DonneeRefBean constructor comment.
 * @param rs java.sql.ResultSet
 */
public DonneeRefBean(String nomTable, ResultSet rs) {
	super(rs);
	this.nomTable = nomTable;
	try {
		CD = rs.getInt("CD_" + nomTable);
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans DonneeRefBean (RS) : " + e.toString());
		}
	}
	try {
		LIB = rs.getString("LIB_" + nomTable);
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans DonneeRefBean (RS) : " + e.toString());
		}
	}
}/**
 * Création d'un Bean Type de Vente à partir de sa clé
 * Creation date: (19/08/2001 21:14:20)
 * @param dbConnect com.increg.salon.bean.DBSession
 * @param CD java.lang.String
 */
public static DonneeRefBean getDonneeRefBean(DBSession dbConnect, String nomTable, String CD) {
	String reqSQL = "select * from " + nomTable + " where CD_" + nomTable + "=" + CD;
	DonneeRefBean res = null;

	// Interroge la Base
	try {
		ResultSet aRS = dbConnect.doRequest(reqSQL);

		while (aRS.next()) {
			res = new DonneeRefBean(nomTable, aRS);
		}
		aRS.close();
	}
	catch (Exception e) {
		System.out.println("Erreur dans constructeur sur clé : " + e.toString());
	}
	return res;
}}
