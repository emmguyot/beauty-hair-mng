/*
 * Bean de gestion de statistiques génériques 
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
import com.increg.commun.exception.FctlException;
import com.increg.commun.exception.NoImplementationException;
import com.increg.util.*;
import com.increg.commun.*;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

/**
 * Bean de gestion de statistiques génériques
 * Creation date: (24/03/2002 10:35:20)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class StatBean extends TimeStampBean {
    /**
     * Code de la statistique
     */
	protected long CD_STAT;
    /**
     * Libellé de la statistique
     */
	protected java.lang.String LIB_STAT;
    /**
     * Requête SQL de la statistique avec les variables entre $
     */
	protected java.lang.String REQ_SQL;
    /**
     * Légende de l'axe X
     */
	protected java.lang.String LABEL_X;
    /**
     * Légende de l'axe Y
     */
	protected java.lang.String LABEL_Y;
    /**
     * Séparateur de variable au sein des requêtes SQL
     */
	public static final String SEP = "$";

/**
 * ClientBean constructor comment.
 */
public StatBean(ResourceBundle rb) {
	super(rb);
}

/**
 * ClientBean à partir d'un RecordSet.
 * @param rs RecordSet dans lequel les données seront lues
 */
public StatBean(ResultSet rs, ResourceBundle rb) {
	super(rs, rb);
	try {
		CD_STAT = rs.getLong("CD_STAT");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
		}
	}
	try {
		LIB_STAT = rs.getString("LIB_STAT");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
		}
	}
	try {
		REQ_SQL = rs.getString("REQ_SQL");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
		}
	}
	try {
		LABEL_X = rs.getString("LABEL_X");
	}
	catch (SQLException e) {
		if (e.getErrorCode() != 1) {
			System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
		}
	}
	try {
		LABEL_Y = rs.getString("LABEL_Y");
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
public void create(DBSession dbConnect) throws java.sql.SQLException {

	com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

	StringBuffer req = new StringBuffer("insert into STAT ");
	StringBuffer colonne = new StringBuffer("(");
	StringBuffer valeur = new StringBuffer(" values ( ");
	
	if (CD_STAT == 0) {
		/**
		 * Numérotation automatique des codes clients
		 */
		String reqMax = "select nextval('SEQ_STAT')";
		try {
			ResultSet aRS = dbConnect.doRequest(reqMax);
			CD_STAT = 1; // Par défaut

			while (aRS.next()) {
				CD_STAT = aRS.getLong(1);
			}
			aRS.close();
		}
		catch (Exception e) {
			System.out.println("Erreur dans reqSeq : " + e.toString());
		}
	}
	colonne.append("CD_STAT,");
	valeur.append(CD_STAT);
	valeur.append(",");
	
	if ((LIB_STAT != null) && (LIB_STAT.length() != 0)) {
		colonne.append("LIB_STAT,");
		valeur.append(DBSession.quoteWith(LIB_STAT, '\''));
		valeur.append(",");
	}

	if ((REQ_SQL != null) && (REQ_SQL.length() != 0)) {
		colonne.append("REQ_SQL,");
		valeur.append(DBSession.quoteWith(REQ_SQL, '\''));
		valeur.append(",");
	}

	if ((LABEL_X != null) && (LABEL_X.length() != 0)) {
		colonne.append("LABEL_X,");
		valeur.append(DBSession.quoteWith(LABEL_X, '\''));
		valeur.append(",");
	}

	if ((LABEL_Y != null) && (LABEL_Y.length() != 0)) {
		colonne.append("LABEL_Y,");
		valeur.append(DBSession.quoteWith(LABEL_Y, '\''));
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
public void delete(DBSession dbConnect) throws java.sql.SQLException {

	StringBuffer req = new StringBuffer("delete from STAT ");
	StringBuffer where = new StringBuffer(" where CD_STAT=" + CD_STAT);
	
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
 * Creation date: (18/07/2001 22:45:39)
 * @return long
 */
public long getCD_STAT() {
	return CD_STAT;
}

/**
 * Execute la requète de stat avece les paramètres et retourne les données
 * Creation date: (30/03/2002 23:15:09)
 * @return java.util.TreeMap
 * @param dbConnect DBSession
 * @param paramReq java.util.Map
 * @exception FctlException The exception description.
 */
public TreeMap getData(DBSession dbConnect, Map paramReq) throws FctlException {

	/**
	 * Substitution des paramètres dans la requète
	 * Les paramètres sont entre deux signes $
	 */
	String reqSubst = REQ_SQL;

	while (reqSubst.indexOf(SEP) != -1) {
		int deb = reqSubst.indexOf(SEP);
		int fin = reqSubst.indexOf(SEP, deb + 1);

		String motCle = reqSubst.substring(deb + 1, fin);
		reqSubst = reqSubst.substring(0, deb) + (String) paramReq.get(motCle) + reqSubst.substring(fin + 1);
	}

	/**
	 * Execute la requète substituée
	 */
	TreeMap res = new TreeMap();
	try {
		ResultSet aRS = dbConnect.doRequest(reqSubst);
		while (aRS.next()) {
	        res.put(aRS.getObject(1), new Double(aRS.getDouble(2)));
		}
		aRS.close();
	}
	catch (Exception e) {
		throw (new FctlException("Erreur dans la requète SQL : " + e.toString() + ". La requète était : " + reqSubst));
	}
	
	
	return res;
}


/**
 * Regroupe des jeux de données afin de les afficher sur un même écran
 * @param lstJeuValeur Liste des jeux de valeur à regrouper
 * @param periode Période de traitement si présent
 * @return Liste des jeux modifiés
 * @throws NoImplementationException Si regroupement non codé
 */
public Vector regroupeData (Vector lstJeuValeur, String periode) throws NoImplementationException {

    Vector newLstJeuValeur = new Vector();
        
    int nbJeux = lstJeuValeur.size();
    
    // En fonction du type des X
    // Prend la première valeur comme référence
    TreeMap data = null;
    for (int i = 0; (i < nbJeux) && ((data == null) || (data.size() == 0)); i++) {
        data = (TreeMap) lstJeuValeur.get(i);
    }
    if (data.size() != 0) {
        Object anX = data.firstKey();
        
        if (anX.getClass().getName().equals("java.lang.Integer")) {
            // Regroupement par égalité
            // Rien à faire
            newLstJeuValeur = lstJeuValeur;
        } else if (anX.getClass().getName().equals("java.lang.Double")) {
            // Regroupement par égalité
            // Rien à faire
            newLstJeuValeur = lstJeuValeur;

        } else if (anX.getClass().getName().equals("java.util.Date")) {
            SimpleDateFormat formatDate = null;
            if ((periode == null) || (recouvrementData(lstJeuValeur))) {
                // Regroupement par égalité
                // Rien à faire
                newLstJeuValeur = lstJeuValeur;
            }
            else if ((periode.equals("day") && (plusieursMois(lstJeuValeur)))) {
                formatDate = new SimpleDateFormat("dd/MM");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        java.util.Date aDate = (java.util.Date) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
            else if (periode.equals("day")) {
                formatDate = new SimpleDateFormat("dd");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        java.util.Date aDate = (java.util.Date) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
            else if (periode.equals("month")) {
                formatDate = new SimpleDateFormat("MM");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        java.util.Date aDate = (java.util.Date) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
        } else if (anX.getClass().getName().equals("java.util.Calendar")) {
            SimpleDateFormat formatDate = null;
            if ((periode == null) || (recouvrementData(lstJeuValeur))) {
                // Regroupement par égalité
                // Rien à faire
                newLstJeuValeur = lstJeuValeur;
            }
            else if ((periode.equals("day") && (plusieursMois(lstJeuValeur)))) {
                formatDate = new SimpleDateFormat("dd/MM");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        Calendar aDate = (Calendar) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate.getTime()), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
            else if (periode.equals("day")) {
                formatDate = new SimpleDateFormat("dd");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        Calendar aDate = (Calendar) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate.getTime()), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
            else if (periode.equals("month")) {
                formatDate = new SimpleDateFormat("MM");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        Calendar aDate = (Calendar) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate.getTime()), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
        } else if (anX.getClass().getName().equals("java.sql.Timestamp")) {
            SimpleDateFormat formatDate = null;
            if ((periode == null) || (recouvrementData(lstJeuValeur))) {
                // Regroupement par égalité
                // Rien à faire
                newLstJeuValeur = lstJeuValeur;
            }
            else if ((periode.equals("day") && (plusieursMois(lstJeuValeur)))) {
                formatDate = new SimpleDateFormat("dd/MM");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        java.sql.Timestamp aDate = (java.sql.Timestamp) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
            else if (periode.equals("day")) {
                formatDate = new SimpleDateFormat("dd");
                
                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        java.sql.Timestamp aDate = (java.sql.Timestamp) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate), value);
                    }
                    newLstJeuValeur.add(newData);
                }
            }
            else if (periode.equals("month")) {
                formatDate = new SimpleDateFormat("MM");

                for (int i = 0; i < nbJeux; i++) {
                    data = (TreeMap) lstJeuValeur.get(i);
                    
                    TreeMap newData = new TreeMap();
                    for (Iterator d = data.keySet().iterator(); d.hasNext();) {
                        java.sql.Timestamp aDate = (java.sql.Timestamp) d.next();
                        Object value = data.get(aDate);
                        // Annule et remplace
                        newData.put(formatDate.format(aDate), value);
                    }
                    
                    newLstJeuValeur.add(newData);
                }
            }
        } else if (anX.getClass().getName().equals("java.lang.String")) {
            // Regroupement par égalité
            // Rien à faire
            newLstJeuValeur = lstJeuValeur;
        } else {

			String msg = MessageFormat.format(message.getString("statBean.regroupementNoImplementation"), 
					new Object[] { anX.getClass().getName() });
        	
            throw new NoImplementationException(msg);
        }
        
    }
    // Sinon aucune donnée : Rien à faire
    
    return newLstJeuValeur;
    
}


/**
 * Indique si les jeux de valeur se recouvre ou pas
 * @param lstJeuValeur Ensemble des jeux de valeur
 * @return True si recouvrement effectif
 */
protected boolean recouvrementData(Vector lstJeuValeur) {
    
    boolean res = false; // Jusqu'à preuve du contraire
    
    for (int i = 0; (i < lstJeuValeur.size()) && !res; i++) {
        TreeMap data = (TreeMap) lstJeuValeur.get(i);
        
        // Borne du jeu en cours            
        Comparable maxX = null;
        Comparable minX = null;
        
        for (Iterator d = data.keySet().iterator(); d.hasNext();) {
            Comparable anX = (Comparable) d.next();
            if ((maxX == null) || (anX.compareTo(maxX) > 0)) {
                maxX = anX;
            }
            if ((minX == null) || (anX.compareTo(minX) < 0)) {
                minX = anX;
            }
        }


        if ((maxX != null) && (minX != null)) {
	        // Compare ces bornes aux autres
	        for (int j = 0; (j < lstJeuValeur.size()) && !res; j++) {
	            if (i != j) {
	                TreeMap data2 = (TreeMap) lstJeuValeur.get(j);
	                
	                for (Iterator d = data2.keySet().iterator(); d.hasNext() && !res;) {
	                    Comparable anX = (Comparable) d.next();
	                    if ((anX.compareTo(maxX) <= 0) && (anX.compareTo(minX) >= 0)) {
	                        // La valeur est entre les bornes : Il y a recouvrement
	                        res = true;
	                    }
	                }
	            }
	        }
        }
                
    }
    
    return res;
}

/**
 * Indique si un des jeux de valeur porte sur plusieurs mois
 * @param lstJeuValeur Ensemble des jeux de valeur
 * @return True si au moins un des jeux porte sur plusieurs mois
 */
protected boolean plusieursMois(Vector lstJeuValeur) {
    
    boolean res = false; // Jusqu'à preuve du contraire
    
    for (int i = 0; (i < lstJeuValeur.size()) && !res; i++) {
        TreeMap data = (TreeMap) lstJeuValeur.get(i);
        
        // Borne du jeu en cours            
        Comparable maxX = null;
        Comparable minX = null;
        
        for (Iterator d = data.keySet().iterator(); d.hasNext();) {
            Comparable anX = (Comparable) d.next();
            if ((maxX == null) || (anX.compareTo(maxX) > 0)) {
                maxX = anX;
            }
            if ((minX == null) || (anX.compareTo(minX) < 0)) {
                minX = anX;
            }
        }
        
        SimpleDateFormat formatDate = new SimpleDateFormat("MM");

        if (maxX.getClass().getName().equals("java.util.Date")) {
            res = !formatDate.format((java.util.Date) maxX).equals(formatDate.format((java.util.Date) minX));
        } else if (maxX.getClass().getName().equals("java.util.Calendar")) {
            res = !formatDate.format(((Calendar) maxX).getTime()).equals(formatDate.format(((Calendar) minX).getTime()));
        } else if (maxX.getClass().getName().equals("java.sql.Timestamp")) {
            res = !formatDate.format((java.util.Date) maxX).equals(formatDate.format((java.util.Date) minX));
        }
    }
    
    return res;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:09)
 * @return java.lang.String
 */
public java.lang.String getLABEL_X() {

	if (LABEL_X == null) {
		return "";
	}
	else {
		return LABEL_X;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:24)
 * @return java.lang.String
 */
public java.lang.String getLABEL_Y() {

	if (LABEL_Y == null) {
		return "";
	}
	else {
		return LABEL_Y;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:46:26)
 * @return java.lang.String
 */
public java.lang.String getLIB_STAT() {
	if (LIB_STAT == null) {
		return "";
	}
	else {
		return LIB_STAT;
	}
}

/**
 * Donne la liste des paramètres de la requète
 * Creation date: (31/03/2002 22:17:47)
 * @return java.util.Vector
 */
public Vector getParameters() {

	Vector res = new Vector();

	int deb = 0;
	while (REQ_SQL.indexOf(SEP, deb) != -1) {
		deb = REQ_SQL.indexOf(SEP, deb);
		int fin = REQ_SQL.indexOf(SEP, deb + 1);

		String motCle = REQ_SQL.substring(deb + 1, fin);
		res.add(motCle);

		deb = fin + 1;
	}

	return res;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:46:51)
 * @return java.lang.String
 */
public java.lang.String getREQ_SQL() {

	if (REQ_SQL == null) {
		return "";
	}
	else {
		return REQ_SQL;
	}
}

/**
 * Création d'un Bean Statistique à partir de sa clé
 * Creation date: (18/08/2001 17:05:45)
 * @param dbConnect com.increg.salon.bean.DBSession
 * @param CD_STAT java.lang.String
 * @param rb Messages localisés
 * @return Bean créé
 */
public static StatBean getStatBean(DBSession dbConnect, String CD_STAT, ResourceBundle rb) {
	String reqSQL = "select * from STAT where CD_STAT=" + CD_STAT;
	StatBean res = null;

	// Interroge la Base
	try {
		ResultSet aRS = dbConnect.doRequest(reqSQL);

		while (aRS.next()) {
			res = new StatBean(aRS, rb);
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
public void maj(DBSession dbConnect) throws java.sql.SQLException {

	com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

	StringBuffer req = new StringBuffer("update STAT set ");
	StringBuffer colonne = new StringBuffer("");
	StringBuffer where = new StringBuffer(" where CD_STAT=" + CD_STAT);

	colonne.append("LIB_STAT=");
	if ((LIB_STAT != null) && (LIB_STAT.length() != 0)) {
		colonne.append(DBSession.quoteWith(LIB_STAT, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("REQ_SQL=");
	if ((REQ_SQL != null) && (REQ_SQL.length() != 0)) {
		colonne.append(DBSession.quoteWith(REQ_SQL, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("LABEL_X=");
	if ((LABEL_X != null) && (LABEL_X.length() != 0)) {
		colonne.append(DBSession.quoteWith(LABEL_X, '\''));
	}
	else {
	    colonne.append("NULL");
	}
	colonne.append(",");

	colonne.append("LABEL_Y=");
	if ((LABEL_Y != null) && (LABEL_Y.length() != 0)) {
		colonne.append(DBSession.quoteWith(LABEL_Y, '\''));
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
 * Creation date: (18/07/2001 22:45:39)
 * @param newCD_STAT long
 */
public void setCD_STAT(long newCD_STAT) {
	CD_STAT = newCD_STAT;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:45:39)
 * @param newCD_STAT long
 */
public void setCD_STAT(String newCD_STAT) {

	if (newCD_STAT.length() != 0) {
		CD_STAT = Long.parseLong(newCD_STAT);
	}
	else {
		CD_STAT = 0;
	}
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:09)
 * @param newLABEL_X java.lang.String
 */
public void setLABEL_X(java.lang.String newLABEL_X) {
	LABEL_X = newLABEL_X;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:47:24)
 * @param newLABEL_Y java.lang.String
 */
public void setLABEL_Y(java.lang.String newLABEL_Y) {
	LABEL_Y = newLABEL_Y;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:46:26)
 * @param newLIB_STAT java.lang.String
 */
public void setLIB_STAT(java.lang.String newLIB_STAT) {
	LIB_STAT = newLIB_STAT;
}

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:46:51)
 * @param newREQ_SQL java.lang.String
 */
public void setREQ_SQL(java.lang.String newREQ_SQL) {
	REQ_SQL = newREQ_SQL;
}

/**
 * @see com.increg.salon.bean.TimeStampBean
 */
public java.lang.String toString() {
	return getLIB_STAT();
}

}