/*
 * Bean gérant les modes de réglement
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
 * Bean gérant les modes de réglement
 * Creation date: (25/08/2001 21:57:06)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ModReglBean extends GenericBean {
    /**
     * Clé : Code numérique
     */
	protected int CD_MOD_REGL;
    /**
     * Libellé
     */
	protected java.lang.String LIB_MOD_REGL;
    /**
     * Indicateur si le mode en encore utilisable
     */
    protected java.lang.String UTILISABLE;
    /**
     * Indicateur si ce mode de règlement peut s'imprimer sous forme de chèque
     */
    protected java.lang.String IMP_CHEQUE;
    /**
     * Indicateur si ce mode de règlement peut nécessiter un rendu de monnaie
     */
    protected java.lang.String RENDU_MONNAIE;
    
    /**
     * Mode de réglement Carte Bancaire
     */
    public static final int MOD_REGL_CB = 4;    
    /**
     * Mode de règlement Chèque
     */
    public static final int MOD_REGL_CHQ = 3;   
    /**
     * Mode de règlement Cheque Franc
     */
    public static final int MOD_REGL_CHQ_FRF = 5;   
    /**
     * Mode de règlement Espèce
     */
    public static final int MOD_REGL_ESP = 1;   
    /**
     * Mode de règlement Francs
     */
    public static final int MOD_REGL_ESP_FRF = 2;   

	/**
	 * ModReglBean constructor comment.
	 */
	public ModReglBean() {
		super();
	}
	/**
	 * ModReglBean constructor comment.
	 * @param rs java.sql.ResultSet
	 */
	public ModReglBean(ResultSet rs) {
		super(rs);
		try {
			CD_MOD_REGL = rs.getInt("CD_MOD_REGL");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ModReglBean (RS) : " + e.toString());
			}
		}
		try {
			LIB_MOD_REGL = rs.getString("LIB_MOD_REGL");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ModReglBean (RS) : " + e.toString());
			}
		}
		try {
			UTILISABLE = rs.getString("UTILISABLE");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans ModReglBean (RS) : " + e.toString());
			}
		}
        try {
            IMP_CHEQUE = rs.getString("IMP_CHEQUE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ModReglBean (RS) : " + e.toString());
            }
        }
        try {
            RENDU_MONNAIE = rs.getString("RENDU_MONNAIE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ModReglBean (RS) : " + e.toString());
            }
        }
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void create(DBSession dbConnect) throws SQLException {
	
		StringBuffer req = new StringBuffer("insert into MOD_REGL ");
		StringBuffer colonne = new StringBuffer("(");
		StringBuffer valeur = new StringBuffer(" values ( ");
		
		if (CD_MOD_REGL == 0) {
			/**
			 * Numérotation automatique des prestations
			 */
			String reqMax = "select nextval('SEQ_MOD_REGL')";
			try {
				ResultSet aRS = dbConnect.doRequest(reqMax);
				CD_MOD_REGL = 1; // Par défaut
	
				while (aRS.next()) {
					CD_MOD_REGL = aRS.getInt(1);
				}
				aRS.close();
			}
			catch (Exception e) {
				System.out.println("Erreur dans reqSeq : " + e.toString());
			}
		}
		colonne.append("CD_MOD_REGL,");
		valeur.append(CD_MOD_REGL);
		valeur.append(",");
	
		if ((LIB_MOD_REGL != null) && (LIB_MOD_REGL.length() != 0)) {
			colonne.append("LIB_MOD_REGL,");
			valeur.append(DBSession.quoteWith(LIB_MOD_REGL, '\''));
			valeur.append(",");
		}
	
		if ((UTILISABLE != null) && (UTILISABLE.length() != 0)) {
			colonne.append("UTILISABLE,");
			valeur.append(DBSession.quoteWith(UTILISABLE, '\''));
			valeur.append(",");
		}
	
        if ((IMP_CHEQUE != null) && (IMP_CHEQUE.length() != 0)) {
            colonne.append("IMP_CHEQUE,");
            valeur.append(DBSession.quoteWith(IMP_CHEQUE, '\''));
            valeur.append(",");
        }
    
        if ((RENDU_MONNAIE != null) && (RENDU_MONNAIE.length() != 0)) {
            colonne.append("RENDU_MONNAIE,");
            valeur.append(DBSession.quoteWith(RENDU_MONNAIE, '\''));
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

	    // Debut de la transaction
	    dbConnect.setDansTransactions(true);
	
		// Execute la création
		String[] reqs = new String[1];
		reqs[0] = req.toString();
		int[] nb = new int[1];
		nb = dbConnect.doExecuteSQL(reqs);
	
		if (nb[0] != 1) {
			throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
		}
        
        // Création de la caisse correspondante
	    CaisseBean aCaisse = new CaisseBean();
        aCaisse.setCD_MOD_REGL(CD_MOD_REGL);
        aCaisse.setSOLDE("0");
        aCaisse.setSOLDE_DEBUT("0");
        aCaisse.create(dbConnect);
        
	    // Fin de la transaction
	    dbConnect.endTransaction();
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void delete(DBSession dbConnect) throws SQLException {
	
		StringBuffer req = new StringBuffer("delete from MOD_REGL ");
		StringBuffer where = new StringBuffer(" where CD_MOD_REGL=" + CD_MOD_REGL);
		
		// Constitue la requete finale
		req.append(where);
	
        // Debut de la transaction
        dbConnect.setDansTransactions(true);
    
        CaisseBean aCaisse = CaisseBean.getCaisseBean(dbConnect, Integer.toString(CD_MOD_REGL));
        if ((aCaisse != null) && (aCaisse.getCD_MOD_REGL() == CD_MOD_REGL)) {
            aCaisse.delete(dbConnect);
        }
        
		// Execute la création
		String[] reqs = new String[1];
		reqs[0] = req.toString();
		int[] nb = new int[1];
		nb = dbConnect.doExecuteSQL(reqs);
	
		if (nb[0] != 1) {
			throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
		}	

        // Fin de la transaction
        dbConnect.endTransaction();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
	 * @return int
	 */
	public int getCD_MOD_REGL() {
		return CD_MOD_REGL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:52:52)
	 * @return java.lang.String
	 */
	public java.lang.String getLIB_MOD_REGL() {
		return LIB_MOD_REGL;
	}
	/**
	 * Création d'un Bean Type de Vente à partir de sa clé
	 * Creation date: (19/08/2001 21:14:20)
	 * @param dbConnect com.increg.salon.bean.DBSession
	 * @param CD_MOD_REGL java.lang.String
     * @return Bean correspondant
	 */
	public static ModReglBean getModReglBean(DBSession dbConnect, String CD_MOD_REGL) {
		String reqSQL = "select * from MOD_REGL where CD_MOD_REGL=" + CD_MOD_REGL;
		ModReglBean res = null;
	
		// Interroge la Base
		try {
			ResultSet aRS = dbConnect.doRequest(reqSQL);
	
			while (aRS.next()) {
				res = new ModReglBean(aRS);
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
	
		StringBuffer req = new StringBuffer("update MOD_REGL set ");
		StringBuffer colonne = new StringBuffer("");
		StringBuffer where = new StringBuffer(" where CD_MOD_REGL=" + CD_MOD_REGL);
	
		colonne.append("LIB_MOD_REGL=");
		if ((LIB_MOD_REGL != null) && (LIB_MOD_REGL.length() != 0)) {
			colonne.append(DBSession.quoteWith(LIB_MOD_REGL, '\''));
		}
		else {
		    colonne.append("NULL");
		}
	
		colonne.append(",UTILISABLE=");
		if ((UTILISABLE != null) && (UTILISABLE.length() != 0)) {
			colonne.append(DBSession.quoteWith(UTILISABLE, '\''));
		}
		else {
		    colonne.append("NULL");
		}
	
        colonne.append(",IMP_CHEQUE=");
        if ((IMP_CHEQUE != null) && (IMP_CHEQUE.length() != 0)) {
            colonne.append(DBSession.quoteWith(IMP_CHEQUE, '\''));
        }
        else {
            colonne.append("NULL");
        }
    
        colonne.append(",RENDU_MONNAIE=");
        if ((RENDU_MONNAIE != null) && (RENDU_MONNAIE.length() != 0)) {
            colonne.append(DBSession.quoteWith(RENDU_MONNAIE, '\''));
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
	 * @param newCD_MOD_REGL int
	 */
	public void setCD_MOD_REGL(int newCD_MOD_REGL) {
		CD_MOD_REGL = newCD_MOD_REGL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (19/08/2001 20:55:16)
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
	 * Creation date: (19/08/2001 20:52:52)
	 * @param newLIB_MOD_REGL java.lang.String
	 */
	public void setLIB_MOD_REGL(java.lang.String newLIB_MOD_REGL) {
		LIB_MOD_REGL = newLIB_MOD_REGL;
	}
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public java.lang.String toString() {
		return getLIB_MOD_REGL();
	}

    /**
	 * Insert the method's description here.
	 * Creation date: (20/01/2002 19:33:24)
	 * @return java.lang.String
	 */
	public java.lang.String getUTILISABLE() {
		return UTILISABLE;
	}
    /**
	 * Insert the method's description here.
	 * Creation date: (20/01/2002 19:33:24)
	 * @param newUTILISABLE java.lang.String
	 */
	public void setUTILISABLE(java.lang.String newUTILISABLE) {
		UTILISABLE = newUTILISABLE;
	}
	/**
	 * Gets the iMP_CHEQUE.
	 * @return Returns a java.lang.String
	 */
	public java.lang.String getIMP_CHEQUE() {
		return IMP_CHEQUE;
	}

	/**
	 * Sets the iMP_CHEQUE.
	 * @param iMP_CHEQUE The iMP_CHEQUE to set
	 */
	public void setIMP_CHEQUE(java.lang.String iMP_CHEQUE) {
		IMP_CHEQUE = iMP_CHEQUE;
	}

	/**
	 * Gets the rENDU_MONNAIE.
	 * @return Returns a java.lang.String
	 */
	public java.lang.String getRENDU_MONNAIE() {
		return RENDU_MONNAIE;
	}

	/**
	 * Sets the rENDU_MONNAIE.
	 * @param rENDU_MONNAIE The rENDU_MONNAIE to set
	 */
	public void setRENDU_MONNAIE(java.lang.String rENDU_MONNAIE) {
		RENDU_MONNAIE = rENDU_MONNAIE;
	}

}
