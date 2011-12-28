/*
 * Tag gérant l'affichage d'une selection simple
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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;


import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class TagSelection extends BodyTagSupport {

    /**
     * Valeur sélectionnée
     */
    protected java.lang.String valeur = null;
    /**
     * Libellés de la liste séparés par |
     */
    protected java.lang.String libelle = null;
    /**
     * Valeurs correspondantes aux libellés séparés par |
     */
    protected java.lang.String valeurs = null;

    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doAfterBody() {
        BodyContent body = getBodyContent();

        JspWriter out = body.getEnclosingWriter();
        String texte = body.getString();
        String chaineCherchee = "%%";
        int pos = texte.indexOf(chaineCherchee);

        try {
            if (pos != -1) {
                // Ecrit le début
                out.print(texte.substring(0, pos));

                //on est dans le cas standard, valeurs contient
                //les valeurs separees par |
                int lastPos = 0;
                int newPos = 0;
                int lastPosLib = 0;
                int newPosLib = 0;
                String val = null;
                String lib = null;
                while (lastPos != -1) {
                    newPos = valeurs.indexOf("|", lastPos);
                    if (newPos != -1) {
                        val = valeurs.substring(lastPos, newPos);
                        lastPos = newPos + 1;
                    }
                    else {
                        val = valeurs.substring(lastPos);
                        lastPos = newPos;
                    }
                    if (libelle != null) {
                        newPosLib = libelle.indexOf("|", lastPosLib);
                        if (newPosLib != -1) {
                            lib = libelle.substring(lastPosLib, newPosLib);
                            lastPosLib = newPosLib + 1;
                        }
                        else {
                            lib = libelle.substring(lastPosLib);
                            lastPosLib = newPosLib;
                        }
                    }
                    printOne(out, val, lib);
                }

                out.print(texte.substring(pos + chaineCherchee.length()));
            }
            else {
                out.print(texte);
            }
        }
        catch (IOException e) {
            System.out.println("doAfterBody : " + e.toString());
        }
        return SKIP_BODY;
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
        }
        else {
            valeur = newValeur;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/09/2001 22:38:25)
     * @return java.lang.String
     */
    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/07/2001 14:23:02)
     * @return java.lang.String
     */
    protected java.lang.String getValeurs() {
        return valeurs;
    } 
    
    /**
     * Insert the method's description here.
     * Creation date: (16/08/2001 19:03:18)
     * @param out Printer de sortie
     * @param aValeur java.lang.String
     * @param aLibelle Libellé correspondant à la valeur
     * @exception java.io.IOException The exception description.
     */
    protected void printOne(JspWriter out, String aValeur, String aLibelle)
        throws java.io.IOException {
        if (aValeur != null) {
            out.print("<option value=\"" + aValeur + "\"");
            if (aValeur.equals(valeur)) {
                out.print(" selected=\"selected\"");
            }
            if (aLibelle == null) {
                out.print(">" + aValeur + "</option>");
            }
            else {
                out.print(">" + aLibelle + "</option>");
            }
        }

    } 
    
    /**
     * Insert the method's description here.
     * Creation date: (18/09/2001 22:38:25)
     * @param newLibelle java.lang.String
     */
    public void setLibelle(java.lang.String newLibelle) {
        libelle = newLibelle;
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/07/2001 14:23:02)
     * @param newValeurs java.lang.String
     */
    public void setValeurs(java.lang.String newValeurs) {
        valeurs = newValeurs;
    }

    /**
     * Met a jour le champ valeurs à partir de la map
     * ainsi que le champs libelle.<br>
     * Le principe ici est que les clefs de la map correspondent aux valeurs
     * et que les veleurs associees aux clefs sont les libelles associés.
     * @param newValeurs java.lang.String
     */
    public void setValeurs(Map newValeurs) {
        StringBuffer tempVal = new StringBuffer();
        StringBuffer tempLib = new StringBuffer();
        boolean premier = true;

        //On doit afficher les valeurs de la hashmap
        Iterator valueIter = newValeurs.keySet().iterator();

        //On parcourt les clefs, on recupere le libelle et on affiche
        while (valueIter.hasNext()) {
            Integer value = (Integer) valueIter.next();
            String label = (String) newValeurs.get(value);
            if (!premier) {
                //On peut afficher le |
                tempVal.append("|");
                tempLib.append("|");
            }
            else {
                premier = false;
            }

            //Ajoute la valeur
            tempVal.append(value.toString());
            //Ajoute le libelle
            tempLib.append(label);
        }

        this.libelle = tempLib.toString();
        this.valeurs = tempVal.toString();
    }

    /**
     * Met a jour le champ valeurs à partir de la liste
     * ainsi que le champs libelle.<br>
     * Le principe ici est que le premier element de la liste est la valeur
     * le deuxième étant le libelle
     * @param newValeurs java.lang.String
     */
    public void setValeurs(List newValeurs) {
        StringBuffer tempVal = new StringBuffer();
        StringBuffer tempLib = new StringBuffer();
        boolean premier = true;
        boolean valeur = true;

        //On doit afficher les valeurs de la hashmap
        Iterator valueIter = newValeurs.iterator();

        //On parcourt la liste pour mettre a jour valeur et libelle
        while (valueIter.hasNext()) {
            Object value = valueIter.next();

			if (valeur == true) {
                //Ajoute la valeur
                if (!premier) {
                    //On peut afficher le |
                     tempVal.append("|");
                }
                tempVal.append(value.toString());
                valeur = false;
			}
            else {
                //Ajoute le libelle
                if (!premier) {
                    //On peut afficher le |
                     tempLib.append("|");
                }
                
                tempLib.append(value.toString());
                valeur = true;
                // Théoriquement, on peut mettre les | maitenant
                // car on a traite une valeur et un libellé.
                premier = false;                
            }

        }

        this.libelle = tempLib.toString();
        this.valeurs = tempVal.toString();
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        valeur = null;
        libelle = null;
        valeurs = null;
        return super.doEndTag();
    }

}