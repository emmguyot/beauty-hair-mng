package com.increg.salon.bean;

import java.util.*;
import java.sql.*;

import com.increg.util.*;import com.increg.commun.*;/**
 * Bean de gestion de collaborateur
 * Creation date: (22/08/2001 13:17:38)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FournBean extends TimeStampBean {
	protected int CD_FOURN;
	protected java.lang.String RAIS_SOC;
	protected java.lang.String CIVILITE_CONT;
	protected java.lang.String NOM_CONT;
	protected java.lang.String PRENOM_CONT;
	protected java.lang.String RUE;
	protected java.lang.String VILLE;
	protected java.lang.String CD_POSTAL;
	protected java.lang.String TEL;
	protected java.lang.String PORTABLE;
	protected java.lang.String EMAIL;
	protected int CD_MOD_REGL;
	protected java.lang.String COMM;
/**
 * FournBean constructor comment.
 */
public FournBean() {
	super();
}
/**
 * FournBean à partir d'un RecordSet.
 */
public FournBean(ResultSet rs) {
	super(rs);
	try {
		CD_FOURN = rs.getInt("CD_FOURN");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		CD_MOD_REGL = rs.getInt("CD_MOD_REGL");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		CD_POSTAL = rs.getString("CD_POSTAL");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		CIVILITE_CONT = rs.getString("CIVILITE_CONT");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		COMM = rs.getString("COMM");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		EMAIL = rs.getString("EMAIL");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		NOM_CONT = rs.getString("NOM_CONT");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		PORTABLE = rs.getString("PORTABLE");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		PRENOM_CONT = rs.getString("PRENOM_CONT");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		RAIS_SOC = rs.getString("RAIS_SOC");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		RUE = rs.getString("RUE");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		TEL = rs.getString("TEL");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		FAX = rs.getString("FAX");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
	try {
		VILLE = rs.getString("VILLE");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans FournBean (RS) : " + e.toString());
		}
	}
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void create(DBSession dbConnect) throws java.sql.SQLException {

	com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

	StringBuffer req = new StringBuffer("insert into FOURN ");
	StringBuffer colonne = new StringBuffer("(");
	StringBuffer valeur = new StringBuffer(" values ( ");
	
	if (CD_FOURN == 0) {
		/**
		 * Numérotation automatique des codes clients
		 */
		String reqMax = "select nextval('SEQ_FOURN')";
		try {
			ResultSet aRS = dbConnect.doRequest(reqMax);
			CD_FOURN = 1; // Par défaut

			while (aRS.next()) {
				CD_FOURN = aRS.getInt(1);
			}
			aRS.close();
		}
		catch (Exception e) {
			System.out.println("Erreur dans reqSeq : " + e.toString());
		}
	}
	colonne.append("CD_FOURN,");
	valeur.append(CD_FOURN);
	valeur.append(",");
	
	if (CD_MOD_REGL != 0) {
		colonne.append("CD_MOD_REGL,");
		valeur.append(CD_MOD_REGL);
		valeur.append(",");
	}

	if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
		colonne.append("CD_POSTAL,");
		valeur.append(DBSession.quoteWith(CD_POSTAL, '\''));
		valeur.append(",");
	}

	if ((CIVILITE_CONT != null) && (CIVILITE_CONT.length() != 0)) {
		colonne.append("CIVILITE_CONT,");
		valeur.append(DBSession.quoteWith(CIVILITE_CONT, '\''));
		valeur.append(",");
	}

	if ((COMM != null) && (COMM.length() != 0)) {
		colonne.append("COMM,");
		valeur.append(DBSession.quoteWith(COMM, '\''));
		valeur.append(",");
	}

	if ((EMAIL != null) && (EMAIL.length() != 0)) {
		colonne.append("EMAIL,");
		valeur.append(DBSession.quoteWith(EMAIL, '\''));
		valeur.append(",");
	}
	
	if ((NOM_CONT != null) && (NOM_CONT.length() != 0)) {
		colonne.append("NOM_CONT,");
		valeur.append(DBSession.quoteWith(NOM_CONT, '\''));
		valeur.append(",");
	}

	if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
		colonne.append("PORTABLE,");
		valeur.append(DBSession.quoteWith(PORTABLE, '\''));
		valeur.append(",");
	}
	
	if ((PRENOM_CONT != null) && (PRENOM_CONT.length() != 0)) {
		colonne.append("PRENOM_CONT,");
		valeur.append(DBSession.quoteWith(PRENOM_CONT, '\''));
		valeur.append(",");
	}

	if ((RAIS_SOC != null) && (RAIS_SOC.length() != 0)) {
		colonne.append("RAIS_SOC,");
		valeur.append(DBSession.quoteWith(RAIS_SOC, '\''));
		valeur.append(",");
	}

	if ((RUE != null) && (RUE.length() != 0)) {
		colonne.append("RUE,");
		valeur.append(DBSession.quoteWith(RUE, '\''));
		valeur.append(",");
	}

	if ((VILLE != null) && (VILLE.length() != 0)) {
		colonne.append("VILLE,");
		valeur.append(DBSession.quoteWith(VILLE, '\''));
		valeur.append(",");
	}
	
	if ((TEL != null) && (TEL.length() != 0)) {
		colonne.append("TEL,");
		valeur.append(DBSession.quoteWith(TEL, '\''));
		valeur.append(",");
	}
	
	if ((FAX != null) && (FAX.length() != 0)) {
		colonne.append("FAX,");
		valeur.append(DBSession.quoteWith(FAX, '\''));
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
		throw (new SQLException("Création non effectuée"));
	}	
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void delete(DBSession dbConnect) throws java.sql.SQLException {

	StringBuffer req = new StringBuffer("delete from FOURN ");
	StringBuffer where = new StringBuffer(" where CD_FOURN=" + CD_FOURN);
	
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
 * Creation date: (03/09/2001 13:27:52)
 * @return int
 */
public int getCD_FOURN() {
	return CD_FOURN;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:09)
 * @return java.lang.String
 */
public java.lang.String getCD_POSTAL() {
	if (CD_POSTAL == null) {
		return "";
	}
	else {
		return CD_POSTAL;
	}
}



/**
 * Création d'un Bean collab à partir de sa clé
 * Creation date: (18/08/2001 17:05:45)
 * @param dbConnect com.increg.salon.bean.DBSession
 * @param CD_FOURN java.lang.String
 */
public static FournBean getFournBean(DBSession dbConnect, String CD_FOURN) {
	String reqSQL = "select * from FOURN where CD_FOURN=" + CD_FOURN;
	FournBean res = null;

	// Interroge la Base
	try {
		ResultSet aRS = dbConnect.doRequest(reqSQL);

		while (aRS.next()) {
			res = new FournBean(aRS);
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
 * Creation date: (18/07/2001 22:48:46)
 * @return java.lang.String
 */
public java.lang.String getEMAIL() {
	if (EMAIL == null) {
		return "";
	}
	else {
		return EMAIL;
	}
}


/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:37)
 * @return java.lang.String
 */
public java.lang.String getPORTABLE() {

	if (PORTABLE == null) {
		return "";
	}
	else {
		return PORTABLE;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:24)
 * @return java.lang.String
 */
public java.lang.String getRUE() {

	if (RUE == null) {
		return "";
	}
	else {
		return RUE;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:21)
 * @return java.lang.String
 */
public java.lang.String getTEL() {

	if (TEL == null) {
		return "";
	}
	else {
		return TEL;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:47)
 * @return java.lang.String
 */
public java.lang.String getVILLE() {

	if (VILLE == null) {
		return "";
	}
	else {
		return VILLE;
	}
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public void maj(DBSession dbConnect) throws java.sql.SQLException {

	com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

	StringBuffer req = new StringBuffer("update FOURN set ");
	StringBuffer colonne = new StringBuffer("");
	StringBuffer where = new StringBuffer(" where CD_FOURN=" + CD_FOURN);

	colonne.append("CD_MOD_REGL=");
	if (CD_MOD_REGL != 0) {
		colonne.append(CD_MOD_REGL);
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("CD_POSTAL=");
	if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
		colonne.append(DBSession.quoteWith(CD_POSTAL, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("CIVILITE_CONT=");
	if ((CIVILITE_CONT != null) && (CIVILITE_CONT.length() != 0)) {
		colonne.append(DBSession.quoteWith(CIVILITE_CONT, '\''));
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

	colonne.append("EMAIL=");
	if ((EMAIL != null) && (EMAIL.length() != 0)) {
		colonne.append(DBSession.quoteWith(EMAIL, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("NOM_CONT=");
	if ((NOM_CONT != null) && (NOM_CONT.length() != 0)) {
		colonne.append(DBSession.quoteWith(NOM_CONT, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("PORTABLE=");
	if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
		colonne.append(DBSession.quoteWith(PORTABLE, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("PRENOM_CONT=");
	if ((PRENOM_CONT != null) && (PRENOM_CONT.length() != 0)) {
		colonne.append(DBSession.quoteWith(PRENOM_CONT, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("RAIS_SOC=");
	if ((RAIS_SOC != null) && (RAIS_SOC.length() != 0)) {
		colonne.append(DBSession.quoteWith(RAIS_SOC, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("RUE=");
	if ((RUE != null) && (RUE.length() != 0)) {
		colonne.append(DBSession.quoteWith(RUE, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("VILLE=");
	if ((VILLE != null) && (VILLE.length() != 0)) {
		colonne.append(DBSession.quoteWith(VILLE, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("TEL=");
	if ((TEL != null) && (TEL.length() != 0)) {
		colonne.append(DBSession.quoteWith(TEL, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("FAX=");
	if ((FAX != null) && (FAX.length() != 0)) {
		colonne.append(DBSession.quoteWith(FAX, '\''));
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
		throw (new SQLException("Mise à jour non effectuée"));
	}

}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newCD_FOURN int
 */
public void setCD_FOURN(int newCD_FOURN) {
	CD_FOURN = newCD_FOURN;
}
/**
 * Insert the method's description here.
 * Creation date: (22/08/2001 13:23:17)
 * @param newCD_FOURN String
 */
public void setCD_FOURN(String newCD_FOURN) {

	if (newCD_FOURN.length() != 0) {
		CD_FOURN = Integer.parseInt(newCD_FOURN);
	}
	else {
		CD_FOURN = 0;
	}
}


/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:09)
 * @param newCD_POSTAL java.lang.String
 */
public void setCD_POSTAL(java.lang.String newCD_POSTAL) {
	CD_POSTAL = newCD_POSTAL;
}







/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:46)
 * @param newEMAIL java.lang.String
 */
public void setEMAIL(java.lang.String newEMAIL) {
	EMAIL = newEMAIL;
}


/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:37)
 * @param newPORTABLE java.lang.String
 */
public void setPORTABLE(java.lang.String newPORTABLE) {
	PORTABLE = newPORTABLE;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:24)
 * @param newRUE java.lang.String
 */
public void setRUE(java.lang.String newRUE) {
	RUE = newRUE;
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:48:21)
 * @param newTEL java.lang.String
 */
public void setTEL(java.lang.String newTEL) {
	TEL = newTEL;
}
/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:47)
 * @param newVILLE java.lang.String
 */
public void setVILLE(java.lang.String newVILLE) {
	VILLE = newVILLE;
}
/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public java.lang.String toString() {
	return getRAIS_SOC();
}



/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @return int
 */
public int getCD_MOD_REGL() {
	return CD_MOD_REGL;
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @return java.lang.String
 */
public java.lang.String getCIVILITE_CONT() {
	if (CIVILITE_CONT == null) {
		return "";
	}
	else {
		return CIVILITE_CONT;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
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
 * Creation date: (03/09/2001 13:27:52)
 * @return java.lang.String
 */
public java.lang.String getNOM_CONT() {
	if (NOM_CONT == null) {
		return "";
	}
	else {
		return NOM_CONT;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @return java.lang.String
 */
public java.lang.String getPRENOM_CONT() {
	if (PRENOM_CONT == null) {
		return "";
	}
	else {
		return PRENOM_CONT;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @return java.lang.String
 */
public java.lang.String getRAIS_SOC() {
	if (RAIS_SOC == null) {
		return "";
	}
	else {
		return RAIS_SOC;
	}
}



/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newCD_MOD_REGL int
 */
public void setCD_MOD_REGL(int newCD_MOD_REGL) {
	CD_MOD_REGL = newCD_MOD_REGL;
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newCIVILITE_CONT java.lang.String
 */
public void setCIVILITE_CONT(java.lang.String newCIVILITE_CONT) {
	CIVILITE_CONT = newCIVILITE_CONT;
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newCOMM java.lang.String
 */
public void setCOMM(java.lang.String newCOMM) {
	COMM = newCOMM;
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newNOM_CONT java.lang.String
 */
public void setNOM_CONT(java.lang.String newNOM_CONT) {
	NOM_CONT = newNOM_CONT;
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newPRENOM_CONT java.lang.String
 */
public void setPRENOM_CONT(java.lang.String newPRENOM_CONT) {
	PRENOM_CONT = newPRENOM_CONT;
}

/**
 * Insert the method's description here.
 * Creation date: (03/09/2001 13:27:52)
 * @param newRAIS_SOC java.lang.String
 */
public void setRAIS_SOC(java.lang.String newRAIS_SOC) {
	RAIS_SOC = newRAIS_SOC;
}
	protected java.lang.String FAX;/**
 * Insert the method's description here.
 * Creation date: (24/09/2001 22:40:46)
 * @return java.lang.String
 */
public java.lang.String getFAX() {

	if (FAX == null) {
		return "";
	}
	else {
		return FAX;
	}
}/**
 * Insert the method's description here.
 * Creation date: (22/08/2001 13:23:17)
 * @param newCD_MOD_REGL String
 */
public void setCD_MOD_REGL(String newCD_MOD_REGL) {
	if (newCD_MOD_REGL.length() != 0) {
		CD_MOD_REGL = Integer.parseInt(newCD_MOD_REGL);
	}
	else {
		CD_MOD_REGL = 0;
	}
}/**
 * Insert the method's description here.
 * Creation date: (24/09/2001 22:40:46)
 * @param newFAX java.lang.String
 */
public void setFAX(java.lang.String newFAX) {
	FAX = newFAX;
}}
