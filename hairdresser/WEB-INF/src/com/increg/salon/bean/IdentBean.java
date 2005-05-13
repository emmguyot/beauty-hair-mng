/*
 * Bean gérant les informations d'identification 
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

import com.increg.commun.*;
import com.increg.util.*;
import java.sql.*;
import java.util.*;
/**
 * Bean gérant les informations d'identification
 * Creation date: (08/07/2001 17:17:53)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class IdentBean extends TimeStampBean {

    /**
     * Code de l'identification
     */
	protected	int CD_IDENT;
    /**
     * Libellé de l'identification
     */
	protected String LIB_IDENT;
    /**
     * Mot de passe en clair
     */
	protected String MOT_PASSE;
    /**
     * Profil Associé
     */
	protected int CD_PROFIL;
    /**
     * Etat de l'identification
     */
	protected String ETAT_CPT;
    /**
     * Constante pour l'état bloqué
     */
	public static final String ETAT_BLOQUE = "B";
    /**
     * Constante pour l'état actif
     */
	public static final String ETAT_ACTIF = "A";

    /**
     * Constante pour le profil SUPER : Tous les droits
     */
    public static final int PROFIL_SUPER = 1;
    /**
     * Constante pour le profil RESPONSABLE du salon
     */
    public static final int PROFIL_RESPONSABLE = 2;
    /**
     * Constante pour le profil COIFFEUR du salon
     */
    public static final int PROFIL_COIFFEUR = 3;
    /**
     * Constante pour le profil RESPONSABLE(Internet) du salon
     */
    public static final int PROFIL_RESPONSABLE_I = 4;
    
	/**
	 * IdentBean constructor comment.
	 */
	public IdentBean() {
		super();
		CD_IDENT = 0;
		CD_PROFIL = 0;
		ETAT_CPT = ETAT_ACTIF;
		LIB_IDENT = "";
		MOT_PASSE = "";
	}
	/**
	 * IdentBean constructor à partir de la base.
     * @param aDBSession : Connexion base à utiliser
     * @param MOT_PASSE : Mot de passe saisi
     * @throws Exception : Pas trouvé, pb base, ...
	 */
	public IdentBean(DBSession aDBSession, String MOT_PASSE) throws Exception {
		super();
		// Initialise le composant à partir de la base
		try {
			boolean found = false;
		    String req = "select CD_IDENT, CD_PROFIL, DT_CREAT, DT_MODIF, ETAT_CPT, LIB_IDENT from IDENT where MOT_PASSE=" + DBSession.quoteWith(MOT_PASSE, '\'');
			ResultSet rs = aDBSession.doRequest(req);
			while (rs.next()) {
		        CD_IDENT = rs.getInt ("CD_IDENT");
		        CD_PROFIL = rs.getInt ("CD_PROFIL");
		        DT_CREAT.setTime(rs.getTimestamp ("DT_CREAT"));
		        DT_MODIF.setTime(rs.getTimestamp ("DT_MODIF"));
		        ETAT_CPT = rs.getString("ETAT_CPT");
		        LIB_IDENT = rs.getString("LIB_IDENT");
		        this.MOT_PASSE = MOT_PASSE;
		        found = true;
			}
			rs.close();
			if (!found) {
		        throw (new Exception(BasicSession.TAG_I18N + "identBean.motDePasseKo" + BasicSession.TAG_I18N));
			}
		}
		catch (Exception e) {
		    System.out.println ("Erreur constructeur IdentBean : " + e.toString());
		    throw (e);
		}
	}
    
	/**
	 * IdentBean à partir d'un RecordSet.
     * @param rs RecordSet dans lequel lire les info
	 */
	public IdentBean(ResultSet rs) {
	    super(rs);
	    try {
	        CD_IDENT = rs.getInt("CD_IDENT");
	    }
	    catch (SQLException e) {
	        if (e.getErrorCode() != 1) {
	            System.out.println("Erreur dans IdentBean (RS) : " + e.toString());
	        }
	    }
	    try {
	        LIB_IDENT = rs.getString("LIB_IDENT");
	    }
	    catch (SQLException e) {
	        if (e.getErrorCode() != 1) {
	            System.out.println("Erreur dans IdentBean (RS) : " + e.toString());
	        }
	    }
	    try {
	        MOT_PASSE = rs.getString("MOT_PASSE");
	    }
	    catch (SQLException e) {
	        if (e.getErrorCode() != 1) {
	            System.out.println("Erreur dans IdentBean (RS) : " + e.toString());
	        }
	    }
	    try {
	        CD_PROFIL = rs.getInt("CD_PROFIL");
	    }
	    catch (SQLException e) {
	        if (e.getErrorCode() != 1) {
	            System.out.println("Erreur dans IdentBean (RS) : " + e.toString());
	        }
	    }
	    try {
	        ETAT_CPT = rs.getString("ETAT_CPT");
	    }
	    catch (SQLException e) {
	        if (e.getErrorCode() != 1) {
	            System.out.println("Erreur dans IdentBean (RS) : " + e.toString());
	        }
	    }
	}

    /**
	 * Création d'un Bean Identification à partir de sa clé
	 * Creation date: 30 mai 02 23:33:42
	 * @param dbConnect com.increg.salon.bean.DBSession
	 * @param CD_IDENT java.lang.String
     * @return IdentBean : Bean correspondant chargé
	 */
	public static IdentBean getIdentBean(DBSession dbConnect, String CD_IDENT) {
	    String reqSQL = "select * from IDENT where CD_IDENT=" + CD_IDENT;
	    IdentBean res = null;
	
	    // Interroge la Base
	    try {
	        ResultSet aRS = dbConnect.doRequest(reqSQL);
	
	        while (aRS.next()) {
	            res = new IdentBean(aRS);
	        }
	        aRS.close();
	    }
	    catch (Exception e) {
	        System.out.println("Erreur dans constructeur sur clé : " + e.toString());
	    }
	    return res;
	}
	    

    /**
     * Création d'un Bean à partir de l'identification
     * @param dbConnect Connexion base à utiliser
     * @param lib_ident Libellé de l'identification 
     * @return Bean chargé
     */
    public static IdentBean getIdentBeanFromUser(DBSession dbConnect, String lib_ident) {
        String reqSQL = "select * from IDENT where LIB_IDENT=" + DBSession.quoteWith(lib_ident, '\'');
        IdentBean res = null;
    
        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);
    
            while (aRS.next()) {
                res = new IdentBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur libellé : " + e.toString());
        }
        return res;
    }
    
    /**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void create(DBSession dbConnect) throws java.sql.SQLException {
        com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into IDENT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

	    if (CD_IDENT == 0) {
	        /**
	         * Numérotation automatique des codes clients
	         */
	        String reqMax = "select nextval('SEQ_IDENT')";
	        try {
	            ResultSet aRS = dbConnect.doRequest(reqMax);
	            CD_IDENT = 1; // Par défaut
	
	            while (aRS.next()) {
	                CD_IDENT = aRS.getInt(1);
	            }
	            aRS.close();
	        }
	        catch (Exception e) {
	            System.out.println("Erreur dans reqSeq : " + e.toString());
	        }
	    }
	    colonne.append("CD_IDENT,");
	    valeur.append(CD_IDENT);
	    valeur.append(",");
    
        if (CD_PROFIL != 0) {
	        colonne.append("CD_PROFIL,");
	        valeur.append(CD_PROFIL);
	        valeur.append(",");
	    }

	    if (LIB_IDENT != null) {
	        colonne.append("LIB_IDENT,");
	        valeur.append(DBSession.quoteWith(LIB_IDENT, '\''));
	        valeur.append(",");
	    }
	
        if (MOT_PASSE != null) {
            colonne.append("MOT_PASSE,");
            valeur.append(DBSession.quoteWith(MOT_PASSE, '\''));
            valeur.append(",");
        }
        
        if (ETAT_CPT != null) {
            colonne.append("ETAT_CPT,");
            valeur.append(DBSession.quoteWith(ETAT_CPT, '\''));
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
	    int[] nb = new int[0];
	    nb = dbConnect.doExecuteSQL(reqs);
	
	    if (nb[0] != 1) {
	        throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
	    }
	
	}
    
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void delete(DBSession dbConnect) throws java.sql.SQLException {
	    StringBuffer req = new StringBuffer("delete from IDENT ");
	    StringBuffer where = new StringBuffer(" where CD_IDENT=" + CD_IDENT);
	    
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
	 * Creation date: (08/07/2001 17:50:31)
	 * @return integer
	 */
	public int getCD_IDENT() {
		return CD_IDENT;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @return integer
	 */
	public int getCD_PROFIL() {
		return CD_PROFIL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @return String
	 */
	public String getETAT_CPT() {
		return ETAT_CPT;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @return java.lang.String
	 */
	public java.lang.String getLIB_IDENT() {
		return LIB_IDENT;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @return java.lang.String
	 */
	public java.lang.String getMOT_PASSE() {
		return MOT_PASSE;
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void maj(DBSession dbConnect) throws java.sql.SQLException {
	    com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
	    StringBuffer req = new StringBuffer("update IDENT set ");
	    StringBuffer colonne = new StringBuffer("");
	    StringBuffer where = new StringBuffer(" where CD_IDENT=" + CD_IDENT);
	
	    colonne.append("LIB_IDENT=");
	    if ((LIB_IDENT != null) && (LIB_IDENT.length() != 0)) {
	        colonne.append(DBSession.quoteWith(LIB_IDENT, '\''));
	    }
	    else {
	        colonne.append("NULL");
	    }
	    colonne.append(",");
	
        colonne.append("MOT_PASSE=");
        if ((MOT_PASSE != null) && (MOT_PASSE.length() != 0)) {
            colonne.append(DBSession.quoteWith(MOT_PASSE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");
    
	    colonne.append("ETAT_CPT=");
	    if ((ETAT_CPT != null) && (ETAT_CPT.length() != 0)) {
	        colonne.append(DBSession.quoteWith(ETAT_CPT, '\''));
	    }
	    else {
	        colonne.append("NULL");
	    }
	    colonne.append(",");
	
	    colonne.append("CD_PROFIL=");
	    if (CD_PROFIL != 0) {
	        colonne.append(CD_PROFIL);
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
	 * Sets the cD_IDENT.
	 * @param cD_IDENT The cD_IDENT to set
	 */
	public void setCD_IDENT(int cD_IDENT) {
		CD_IDENT = cD_IDENT;
	}

    /**
     * Sets the cD_IDENT.
     * @param cD_IDENT The cD_IDENT to set
     */
    public void setCD_IDENT(String cD_IDENT) {
        if ((cD_IDENT != null) && (cD_IDENT.length() != 0)) {
            CD_IDENT = Integer.parseInt(cD_IDENT);
        }
        else {
            CD_IDENT = 0;
        }
    }

	/**
	 * Sets the cD_PROFIL.
	 * @param cD_PROFIL The cD_PROFIL to set
	 */
	public void setCD_PROFIL(int cD_PROFIL) {
		CD_PROFIL = cD_PROFIL;
	}

    /**
     * Sets the cD_PROFIL.
     * @param cD_PROFIL The cD_PROFIL to set
     */
    public void setCD_PROFIL(String cD_PROFIL) {
	    if ((cD_PROFIL != null) && (cD_PROFIL.length() != 0)) {
	        CD_PROFIL = Integer.parseInt(cD_PROFIL);
	    }
	    else {
	        CD_PROFIL = 0;
	    }
    }

	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @param newETAT_CPT String
	 */
	public void setETAT_CPT(String newETAT_CPT) {
		ETAT_CPT = newETAT_CPT;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @param newLIB_IDENT java.lang.String
	 */
	public void setLIB_IDENT(java.lang.String newLIB_IDENT) {
		LIB_IDENT = newLIB_IDENT;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 17:50:31)
	 * @param newMOT_PASSE java.lang.String
	 */
	public void setMOT_PASSE(java.lang.String newMOT_PASSE) {
		MOT_PASSE = newMOT_PASSE;
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public java.lang.String toString() {
		return getLIB_IDENT();
	}
    
    /**
     * Indique le droit associé à l'utilisateur
     * @param entite Entité pour lequel le droit est demandé
     * @param action Action pour lequel le droit est demandé ("+" : Toute)
     * @return boolean Indicateur si l'utilisateur à le droit ou non
     */
    public boolean getDroit(String entite, String action) {
        
        if ((entite == null) || (action == null)) {
            // Mauvais paramètres
            return false;
        }
        
        if (entite.equals("Clients")) {
            return true;
        }
        else if (entite.equals("Prestations")) {
            return true;
        }
        else if (entite.equals("RDV")) {
            return true;
        }
        else if (entite.equals("Stock")) {
            return true;
        }
        else if (entite.equals("Comptabilité")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)
                || (CD_PROFIL == PROFIL_RESPONSABLE_I)) {
                return true;
            }
        }
        else if (entite.equals("Personnel")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)
                || (CD_PROFIL == PROFIL_RESPONSABLE_I)) {
                return true;
            }
        }
        else if (entite.equals("Statistiques")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)
                || (CD_PROFIL == PROFIL_RESPONSABLE_I)) {
                return true;
            }
        }
        else if (entite.equals("Publipostages")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)
                || (CD_PROFIL == PROFIL_RESPONSABLE_I)) {
                return true;
            }
        }
        else if (entite.equals("Administration")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)) {
                return true;
            }
        }
        else if (entite.equals("Sauvegarde")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)) {
                return true;
            }            
        }
        else if (entite.equals("Restauration")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)) {
                return true;
            }            
        }
        else if (entite.equals("MiseAJour")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)) {
                return true;
            }            
        }
        else if (entite.equals("GestionSauvegardes")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)) {
                return true;
            }            
        }
        else if (entite.equals("Parametrage")) {
            if (CD_PROFIL == PROFIL_SUPER) {
                return true;
            }            
        }
        else if (entite.equals("Facture") && action.equals("Forçage")) {
            if ((CD_PROFIL == PROFIL_SUPER) 
                || (CD_PROFIL == PROFIL_RESPONSABLE)) {
                return true;
            }            
        }
        else if (entite.equals("Statistique") && action.equals("Modification")) {
            if (CD_PROFIL == PROFIL_SUPER) {
                return true;
            }            
        }
        else if (entite.equals("Publipostage") && action.equals("Modification")) {
            if (CD_PROFIL == PROFIL_SUPER) {
                return true;
            }            
        }
        
        // Dans tous les autres cas : Pas le droit
        return false;
    }
}
