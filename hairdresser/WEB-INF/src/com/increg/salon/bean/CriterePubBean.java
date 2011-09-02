/*
 * Bean gérant les critères de publipostage
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
import com.increg.util.*;
import com.increg.commun.*;

import java.util.*;
import java.sql.*;

/**
 * Bean de gestion des critères de publipostage
 * Creation date: 27 oct. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class CriterePubBean extends TimeStampBean {
    /**
     * Code du critère
     */
	protected long CD_CRITERE_PUB;
    /**
     * Libellé du critère
     */
	protected String LIB_CRITERE_PUB;
    /**
     * Clause Where de la requète avec les variables entre $
     */
	protected String CLAUSE;
    /**
     * Séparateur de variable au sein des requêtes SQL
     */
	public static final String SEP = "$";

    /**
     * ClientBean constructor comment.
     * @param rb Messages à utiliser
     */
    public CriterePubBean(ResourceBundle rb) {
    	super(rb);
    }

	/**
	 * ClientBean à partir d'un RecordSet.
	 * @param rs ResultSet dans lequel les données seront lues
     * @param rb Messages à utiliser
	 */
	public CriterePubBean(ResultSet rs, ResourceBundle rb) {
		super(rs, rb);
		try {
			CD_CRITERE_PUB = rs.getLong("CD_CRITERE_PUB");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CriterePubBean (RS) : " + e.toString());
			}
		}
		try {
			LIB_CRITERE_PUB = rs.getString("LIB_CRITERE_PUB");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CriterePubBean (RS) : " + e.toString());
			}
		}
		try {
			CLAUSE = rs.getString("CLAUSE");
		}
		catch (SQLException e) {
			if (e.getErrorCode() != 1) {
				System.out.println("Erreur dans CriterePubBean (RS) : " + e.toString());
			}
		}
	}
	
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public void create(DBSession dbConnect) throws java.sql.SQLException {
	
		com.increg.util.SimpleDateFormatEG formatDate  = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
	
		StringBuffer req = new StringBuffer("insert into CRITERE_PUB ");
		StringBuffer colonne = new StringBuffer("(");
		StringBuffer valeur = new StringBuffer(" values ( ");
		
		if (CD_CRITERE_PUB == 0) {
			/**
			 * Numérotation automatique des codes
			 */
			String reqMax = "select nextval('SEQ_CRITERE_PUB')";
			try {
				ResultSet aRS = dbConnect.doRequest(reqMax);
				CD_CRITERE_PUB = 1; // Par défaut
	
				while (aRS.next()) {
					CD_CRITERE_PUB = aRS.getLong(1);
				}
				aRS.close();
			}
			catch (Exception e) {
				System.out.println("Erreur dans reqSeq : " + e.toString());
			}
		}
		colonne.append("CD_CRITERE_PUB,");
		valeur.append(CD_CRITERE_PUB);
		valeur.append(",");
		
		if ((LIB_CRITERE_PUB != null) && (LIB_CRITERE_PUB.length() != 0)) {
			colonne.append("LIB_CRITERE_PUB,");
			valeur.append(DBSession.quoteWith(LIB_CRITERE_PUB, '\''));
			valeur.append(",");
		}
	
		if ((CLAUSE != null) && (CLAUSE.length() != 0)) {
			colonne.append("CLAUSE,");
			valeur.append(DBSession.quoteWith(CLAUSE, '\''));
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
	
		StringBuffer req = new StringBuffer("delete from CRITERE_PUB ");
		StringBuffer where = new StringBuffer(" where CD_CRITERE_PUB=" + CD_CRITERE_PUB);
		
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
	public long getCD_CRITERE_PUB() {
		return CD_CRITERE_PUB;
	}
	
	/**
	 * Execute la requète de stat avece les paramètres et retourne les données
	 * Creation date: (30/03/2002 23:15:09)
	 * @return Liste des clients
	 * @param dbConnect DBSession
	 * @param paramReq java.util.Map
	 * @exception FctlException The exception description.
	 */
	public Vector getData(DBSession dbConnect, Map paramReq) throws FctlException {
	
		/**
		 * Substitution des paramètres dans la requète
		 * Les paramètres sont entre deux signes $
		 */
		String reqSubst = "select * " + CLAUSE;
	
		while (reqSubst.indexOf(SEP) != -1) {
			int deb = reqSubst.indexOf(SEP);
			int fin = reqSubst.indexOf(SEP, deb + 1);
	
			String motCle = reqSubst.substring(deb + 1, fin);
			reqSubst = reqSubst.substring(0, deb) + (String) paramReq.get(motCle) + reqSubst.substring(fin + 1);
		}
	
		/**
		 * Execute la requète substituée
		 */
		Vector res = new Vector();
		try {
			ResultSet aRS = dbConnect.doRequest(reqSubst);
			while (aRS.next()) {
		        res.add(new ClientBean(aRS, message));
			}
			aRS.close();
		}
		catch (Exception e) {
			throw (new FctlException(BasicSession.TAG_I18N + "criterePubBean.erreurSQL" + BasicSession.TAG_I18N,
									new Object[] { e.toString(), reqSubst }));
		}
		
		return res;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (18/07/2001 22:46:26)
	 * @return java.lang.String
	 */
	public java.lang.String getLIB_CRITERE_PUB() {
		if (LIB_CRITERE_PUB == null) {
			return "";
		}
		else {
			return LIB_CRITERE_PUB;
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
		while (CLAUSE.indexOf(SEP, deb) != -1) {
			deb = CLAUSE.indexOf(SEP, deb);
			int fin = CLAUSE.indexOf(SEP, deb + 1);
	
			String motCle = CLAUSE.substring(deb + 1, fin);
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
	public java.lang.String getCLAUSE() {
	
		if (CLAUSE == null) {
			return "";
		}
		else {
			return CLAUSE;
		}
	}
	
	/**
	 * Création d'un Bean critère de publipostage à partir de sa clé
	 * Creation date: (18/08/2001 17:05:45)
	 * @param dbConnect com.increg.salon.bean.DBSession
	 * @param CD_CRITERE_PUB java.lang.String
     * @param rb Messages à utiliser
	 * @return Bean créé
	 */
	public static CriterePubBean getCriterePubBean(DBSession dbConnect, String CD_CRITERE_PUB, ResourceBundle rb) {
		String reqSQL = "select * from CRITERE_PUB where CD_CRITERE_PUB=" + CD_CRITERE_PUB;
		CriterePubBean res = null;
	
		// Interroge la Base
		try {
			ResultSet aRS = dbConnect.doRequest(reqSQL);
	
			while (aRS.next()) {
				res = new CriterePubBean(aRS, rb);
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
	
		StringBuffer req = new StringBuffer("update CRITERE_PUB set ");
		StringBuffer colonne = new StringBuffer("");
		StringBuffer where = new StringBuffer(" where CD_CRITERE_PUB=" + CD_CRITERE_PUB);
	
		colonne.append("LIB_CRITERE_PUB=");
		if ((LIB_CRITERE_PUB != null) && (LIB_CRITERE_PUB.length() != 0)) {
			colonne.append(DBSession.quoteWith(LIB_CRITERE_PUB, '\''));
		}
		else {
		    colonne.append("NULL");
		}
		colonne.append(",");
	
		colonne.append("CLAUSE=");
		if ((CLAUSE != null) && (CLAUSE.length() != 0)) {
			colonne.append(DBSession.quoteWith(CLAUSE, '\''));
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
	 * @param newCD_CRITERE_PUB long
	 */
	public void setCD_CRITERE_PUB(long newCD_CRITERE_PUB) {
		CD_CRITERE_PUB = newCD_CRITERE_PUB;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (18/07/2001 22:45:39)
	 * @param newCD_CRITERE_PUB long
	 */
	public void setCD_CRITERE_PUB(String newCD_CRITERE_PUB) {
	
		if (newCD_CRITERE_PUB.length() != 0) {
			CD_CRITERE_PUB = Long.parseLong(newCD_CRITERE_PUB);
		}
		else {
			CD_CRITERE_PUB = 0;
		}
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (18/07/2001 22:46:26)
	 * @param newLIB_CRITERE_PUB java.lang.String
	 */
	public void setLIB_CRITERE_PUB(java.lang.String newLIB_CRITERE_PUB) {
		LIB_CRITERE_PUB = newLIB_CRITERE_PUB;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (18/07/2001 22:46:51)
	 * @param newCLAUSE java.lang.String
	 */
	public void setCLAUSE(java.lang.String newCLAUSE) {
		CLAUSE = newCLAUSE;
	}
	
	/**
	 * @see com.increg.salon.bean.TimeStampBean
	 */
	public java.lang.String toString() {
		return getLIB_CRITERE_PUB();
	}

}