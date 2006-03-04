/*
 * Bean de gestion des taux de TVA
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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.GenericBean;

/**
 * Bean de gestion des taux de TVA
 * Creation date: 26 janv. 2005
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TvaBean extends GenericBean {
    /**
     * Code du taux
     */
    protected int CD_TVA;
    /**
     * Libellé du taux
     */
    protected java.lang.String LIB_TVA;
    /**
     * Valeur du taux (19.6 par exemple)
     */
    protected BigDecimal TX_TVA;

    /**
     * ParamBean constructor comment.
     */
    public TvaBean() {

    }

    /**
     * ParamBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public TvaBean(ResultSet rs) {
        super(rs);
        try {
            CD_TVA = rs.getInt("CD_TVA");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TvaBean (RS) : " + e.toString());
            }
        }
        try {
            LIB_TVA = rs.getString("LIB_TVA");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TvaBean (RS) : " + e.toString());
            }
        }
        try {
            TX_TVA = rs.getBigDecimal("TX_TVA", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans TvaBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("insert into TVA");
        StringBuffer colonne = new StringBuffer(" (");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_TVA == 0) {
            /**
             * Numérotation automatique des prestations
             */
            String reqMax = "select nextval('SEQ_TVA')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_TVA = 1; // Par défaut

                while (aRS.next()) {
                    CD_TVA = aRS.getInt(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_TVA,");
        valeur.append(CD_TVA);
        valeur.append(",");

        if ((LIB_TVA != null) && (LIB_TVA.length() != 0)) {
            colonne.append("LIB_TVA,");
            valeur.append(DBSession.quoteWith(LIB_TVA, '\''));
            valeur.append(",");
        }

        if (TX_TVA != null) {
            colonne.append("TX_TVA,");
            valeur.append(TX_TVA.toString());
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

        StringBuffer req = new StringBuffer("delete from TVA");
        StringBuffer where = new StringBuffer(" where CD_TVA=" + CD_TVA);

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
    public int getCD_TVA() {
        return CD_TVA;
    }
	/**
     * Création d'un Bean Type de Vente à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect Connexion à la base à utiliser
     * @param CD_TVA Code à rechercher
     * @return Bean correspondant au code
     */
    public static TvaBean getTvaBean(DBSession dbConnect, String CD_TVA) {
        String reqSQL = "select * from TVA where CD_TVA=" + CD_TVA;
        TvaBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new TvaBean(aRS);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_TVA() {
        return LIB_TVA;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/09/2001 00:13:51)
     * @return Valeur du taux
     */
    public BigDecimal getTX_TVA() {
        return TX_TVA;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("update TVA set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_TVA=" + CD_TVA);

        colonne.append("LIB_TVA=");
        if ((LIB_TVA != null) && (LIB_TVA.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_TVA, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TX_TVA=");
        if (TX_TVA != null) {
            colonne.append(TX_TVA.toString());
        } else {
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
     * @param newCD_TVA int
     */
    public void setCD_TVA(int newCD_TVA) {
        CD_TVA = newCD_TVA;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_TVA String
     */
    public void setCD_TVA(String newCD_TVA) {
        if ((newCD_TVA != null) && (newCD_TVA.length() != 0)) {
            CD_TVA = Integer.parseInt(newCD_TVA);
        } else {
            CD_TVA = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_TVA java.lang.String
     */
    public void setLIB_TVA(java.lang.String newLIB_TVA) {
        LIB_TVA = newLIB_TVA;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/09/2001 00:13:51)
     * @param newTX_TVA java.lang.String
     */
    public void setTX_TVA(String newTX_TVA) {
        if ((newTX_TVA != null) && (newTX_TVA.length() != 0)) {
            TX_TVA = new BigDecimal(newTX_TVA);
        } else {
            TX_TVA = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/09/2001 00:13:51)
     * @param newTX_TVA java.lang.String
     */
    public void setTX_TVA(BigDecimal newTX_TVA) {
        TX_TVA = newTX_TVA;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getLIB_TVA();
    }

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getCD_TVA();
	}
    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof TvaBean) {
			TvaBean tva2 = (TvaBean) obj;
			return (getCD_TVA() == tva2.getCD_TVA());
		}
		else {
			return false;
		}
	}

}
