/*
 * Bean de gestion d'une caisse (alias un mode de paiement et son solde)
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
import com.increg.util.*;
/**
 * Gestion d'une facture (entête et pied)
 * Creation date: (17/08/2001 20:08:57)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CaisseBean extends TimeStampBean {
    /**
     * Solde de début de caisse
     */
	protected BigDecimal SOLDE_DEBUT;
    /**
     * Solde actuel
     */
	protected java.math.BigDecimal SOLDE;
    /**
     * Caisse correspondante au mode de règlement ...
     */
	protected int CD_MOD_REGL;
	/**
	 * CaisseBean constructor comment.
	 */
	public CaisseBean() {
		super();
	}
	/**
	 * CaisseBean constructor comment.
	 * @param rs java.sql.ResultSet
	 */
	public CaisseBean(ResultSet rs) {
		super(rs);
		try {
			SOLDE_DEBUT = rs.getBigDecimal("SOLDE_DEBUT", 2);
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CaisseBean (RS) : " + e.toString());
			}
		}
		try {
			CD_MOD_REGL = rs.getInt("CD_MOD_REGL");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CaisseBean (RS) : " + e.toString());
			}
		}
		try {
			SOLDE = rs.getBigDecimal("SOLDE", 2);
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CaisseBean (RS) : " + e.toString());
			}
		}
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void create(DBSession dbConnect) throws SQLException {
	 
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("insert into CAISSE ");
		StringBuffer colonne = new StringBuffer("(");
		StringBuffer valeur = new StringBuffer(" values ( ");
		
		if (CD_MOD_REGL != 0) {
			colonne.append("CD_MOD_REGL,");
			valeur.append(CD_MOD_REGL);
			valeur.append(",");
		}
	
		if (SOLDE != null) {
			colonne.append("SOLDE,");
			valeur.append(SOLDE.toString());
			valeur.append(",");
		}
	
		if (SOLDE_DEBUT != null) {
			colonne.append("SOLDE_DEBUT,");
			valeur.append(SOLDE_DEBUT);
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
	
	
		/**
		 * Vérification de la possibilité de supprimer cette facture
		 * Elle ne doit pas avoir été réglée
		 */
		//if (getCD_MOD_REGL() != 0) {
			//throw (new SQLException("Suppression non effectuée : Cette facture a été payée."));
		//}
		 
		/**
		 * Suppression effective
		 */
		StringBuffer req = new StringBuffer("delete from CAISSE ");
		StringBuffer where = new StringBuffer(" where CD_MOD_REGL=" + CD_MOD_REGL);
		
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
	 * Creation date: (17/08/2001 21:27:33)
	 * @return int
	 */
	public int getCD_MOD_REGL() {
		return CD_MOD_REGL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:19:23)
	 * @return BigDecimal 
	 */
	public BigDecimal getSOLDE_DEBUT() {
		return SOLDE_DEBUT;
	}
	/**
	 * Création d'un Bean Facture à partir de sa clé
	 * Creation date: (18/08/2001 17:05:45)
	 * @param dbConnect com.increg.salon.bean.DBSession
	 * @param CD_MOD_REGL java.lang.String
     * @return Bean correspondant
	 */
	public static CaisseBean getCaisseBean(DBSession dbConnect, String CD_MOD_REGL) {
	
		if (Integer.parseInt(CD_MOD_REGL) == ModReglBean.MOD_REGL_CHQ_FRF) {
			// Caisse du chèque
			CD_MOD_REGL = Integer.toString(ModReglBean.MOD_REGL_CHQ);
		}
		if (Integer.parseInt(CD_MOD_REGL) == ModReglBean.MOD_REGL_ESP_FRF) {
			// Caisse de l'espèce
			CD_MOD_REGL = Integer.toString(ModReglBean.MOD_REGL_ESP);
		}
		
		String reqSQL = "select * from CAISSE where CD_MOD_REGL=" + CD_MOD_REGL;
		CaisseBean res = null;
	
		// Interroge la Base
		try {
			ResultSet aRS = dbConnect.doRequest(reqSQL);
	
			while (aRS.next()) {
				res = new CaisseBean(aRS);
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
	 * Creation date: (17/08/2001 21:26:51)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSOLDE() {
		return SOLDE;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:26:51)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSOLDE_Franc() {
		if (SOLDE != null) {
			return SOLDE.multiply(new BigDecimal(6.55957)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		else {
			return null;
		}
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void maj(DBSession dbConnect) throws SQLException {
	
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("update CAISSE set ");
		StringBuffer colonne = new StringBuffer("");
		StringBuffer where = new StringBuffer(" where CD_MOD_REGL=" + CD_MOD_REGL);
	
		colonne.append("SOLDE=");
		if (SOLDE != null) {
			colonne.append(SOLDE.toString());
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("SOLDE_DEBUT=");
		if (SOLDE_DEBUT != null) {
			colonne.append(SOLDE_DEBUT.toString());
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
	 * Creation date: (17/08/2001 21:27:33)
	 * @param newCD_MOD_REGL int
	 */
	public void setCD_MOD_REGL(int newCD_MOD_REGL) {
		CD_MOD_REGL = newCD_MOD_REGL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:27:33)
	 * @param newCD_MOD_REGL String
	 */
	public void setCD_MOD_REGL(String newCD_MOD_REGL) {
		if ((newCD_MOD_REGL != null) && (newCD_MOD_REGL.length() != 0)) {
			CD_MOD_REGL = Integer.parseInt(newCD_MOD_REGL);
		}
		else {
			CD_MOD_REGL = 0;
		}
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:19:23)
	 * @param newSOLDE_DEBUT String
	 */
	public void setSOLDE_DEBUT(String newSOLDE_DEBUT) {
	
		if ((newSOLDE_DEBUT != null) && (newSOLDE_DEBUT.length() != 0)) {
			SOLDE_DEBUT = new BigDecimal(newSOLDE_DEBUT);
		}
		else {
			SOLDE_DEBUT = null;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:26:51)
	 * @param newSOLDE String
	 */
	public void setSOLDE(String newSOLDE) {
		
		if ((newSOLDE != null) && (newSOLDE.length() != 0)) {
			SOLDE = new BigDecimal(newSOLDE);
		}
		else {
			SOLDE = null;
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:26:51)
	 * @param newSOLDE java.math.BigDecimal
	 */
	public void setSOLDE(java.math.BigDecimal newSOLDE) {
		SOLDE = newSOLDE;
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public java.lang.String toString() {
		return SOLDE.toString();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (17/08/2001 21:19:23)
	 * @param newSOLDE_DEBUT BigDecimal
	 */
	public void setSOLDE_DEBUT(BigDecimal newSOLDE_DEBUT) {
		SOLDE_DEBUT = newSOLDE_DEBUT;
    }
}
