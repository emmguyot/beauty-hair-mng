/*
 * Tag gérant l'affichage d'une selection à partir d'une base de données
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
package com.increg.salon.tag;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;

public class TagDBSelection extends BodyTagSupport {

    /**
     * Requete SQL pour la liste des Codes / libellés
     */
    protected java.lang.String sql = null;
    /**
     * Valeur sélectionnée (Code)
     */
    protected java.lang.String valeur = null;
    /**
     * Liste des valeurs sélectionnées quand plusieurs
     * Format Texte séparé par ,
     */
    protected java.lang.String listeValeur;
    /**
     * Connexion à la base de données à utiliser
     */
    protected DBSession myDBSession = null;
    /**
     * Tableau des valeurs sélectionnées
     */
    protected java.lang.String[] tabValeur;
    /**
     * Indicateur de message "données manquantes"
     */
    protected Boolean msgManquant = false;
    
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doAfterBody() {

    	SalonSession mySalon = null;
    	
        if (myDBSession == null) {
            // Récupére la connexion base de données
            javax.servlet.http.HttpSession mySession = pageContext.getSession();
            if ((mySession == null) || (mySession.getAttribute("SalonSession") == null) || (((SalonSession) mySession.getAttribute("SalonSession")).getMyIdent() == null)) {
                // Perte de la connexion : Rien à faire
                return SKIP_BODY;
            }

            mySalon = (SalonSession) mySession.getAttribute("SalonSession");
            myDBSession = mySalon.getMyDBSession();
        }

        BodyContent body = getBodyContent();

        JspWriter out = body.getEnclosingWriter();
        String texte = body.getString();
        String chaineCherchee = "%%";
        int pos = texte.indexOf(chaineCherchee);
        // Ne marche pas bien avec Tomcat !!!
        // pageContext.getRequest().setAttribute("Premier", (String) null);
        pageContext.getRequest().removeAttribute("Premier");

        try {
            if (pos != -1) {
                // Ecrit le début
                out.print(texte.substring(0, pos));

                try {
                    ResultSet aRS = myDBSession.doRequest(sql);
                    boolean premier = true;

                    while (aRS.next()) {
                        if (premier) {
                            // Stocke le premier item pour la selection par défaut
                            pageContext.getRequest().setAttribute("Premier", aRS.getString(1));
                            premier = false;
                        }
                        printOne(out, aRS.getString(1), aRS.getString(2));
                    }

                    aRS.close();
                    
                    if (premier && msgManquant) {
                    	out.print("<script language=\"JavaScript\">alert(\"" + mySalon.internationaliseMessage(BasicSession.TAG_I18N + "message.donneesManquantes" + BasicSession.TAG_I18N) + "\");</script>");
                    }
                } catch (java.sql.SQLException sqlErr) {
                    System.out.println("Erreur SQL sur requète >" + sql + "< : " + sqlErr.toString());
                }

                out.print(texte.substring(pos + chaineCherchee.length()));
            } else {
                out.print(texte);
            }
        } catch (IOException e) {
            System.out.println("doAfterBody : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/07/2001 14:23:02)
     * @return java.lang.String
     */
    protected java.lang.String getSql() {
        return sql;
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @return java.lang.String
     */
    protected java.lang.String getValeur() {
        return valeur;
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/07/2001 14:23:02)
     * @param newSql java.lang.String
     */
    public void setSql(java.lang.String newSql) {
        sql = newSql;
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur long
     */
    public void setValeur(long newValeur) {
        valeur = Long.toString(newValeur);
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur java.lang.String
     */
    public void setValeur(java.lang.String newValeur) {
        if ((newValeur == null) || (newValeur.equals("null"))) {
            valeur = null;
        } else {
            valeur = newValeur;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/01/2002 19:13:02)
     * @return com.increg.commun.DBSession
     */
    public com.increg.commun.DBSession getMyDBSession() {
        return myDBSession;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (15/09/2001 09:41:19)
     * @return java.lang.String[]
     */
    public java.lang.String[] getTabvaleur() {
        return tabValeur;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (16/08/2001 19:03:18)
     * @param out Printer de sortie pour la génération du code
     * @param aValeur java.lang.String
     * @param libelle Libellé correspondant au code
     * @exception java.io.IOException The exception description.
     */
    protected void printOne(JspWriter out, String aValeur, String libelle) throws java.io.IOException {
        if (aValeur != null) {
            out.print("<option value=\"" + aValeur + "\"");
            if (aValeur.equals(valeur)) {
                out.print(" selected=\"selected\"");
            } else if (tabValeur != null) {
                // Vérification par rapport au tableau
                for (int i = 0; i < tabValeur.length; i++) {
                    if (aValeur.equals(tabValeur[i])) {
                        out.print(" selected=\"selected\"");
                    }
                }
            }
            out.println(">" + libelle + "</option>");
        }

    }
    
    /**
     * Insert the method's description here.
     * Creation date: (15/09/2001 09:41:19)
     * @param newTabValeur java.lang.String[]
     */
    public void setListeValeur(java.lang.String newTabValeur) {

        listeValeur = newTabValeur;

        Vector n = new Vector();
        StringTokenizer tok = new StringTokenizer(newTabValeur, ",");
        while (tok.hasMoreTokens()) {
            n.addElement(tok.nextToken());
        }
        tabValeur = new String[n.size()];
        for (int i = 0; i < tabValeur.length; i++) {
            tabValeur[i] = (String) n.elementAt(i);
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (19/01/2002 19:13:02)
     * @param newMyDBSession com.increg.commun.DBSession
     */
    public void setMyDBSession(com.increg.commun.DBSession newMyDBSession) {
        myDBSession = newMyDBSession;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (15/09/2001 09:41:19)
     * @param newTabValeur java.lang.String[]
     */
    public void setTabValeur(java.lang.String[] newTabValeur) {
        tabValeur = newTabValeur;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur java.lang.Integer
     */
    public void setValeur(java.lang.Integer newValeur) {
        if (newValeur == null) {
            valeur = null;
        } else {
            valeur = newValeur.toString();
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        sql = null;
        valeur = null;
        listeValeur = null;
        myDBSession = null;
        tabValeur = null;
        return super.doEndTag();
    }

	/**
	 * @return the msgManquant
	 */
	public Boolean getMsgManquant() {
		return msgManquant;
	}

	/**
	 * @param msgManquant the msgManquant to set
	 */
	public void setMsgManquant(Boolean msgManquant) {
		this.msgManquant = msgManquant;
	}

}
