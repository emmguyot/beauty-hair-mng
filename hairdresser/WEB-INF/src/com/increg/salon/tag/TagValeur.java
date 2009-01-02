/*
 * Tag g�rant l'affichage d'un champs
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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.increg.commun.BasicSession;
import com.increg.util.ServletUtil;
import com.increg.util.SimpleDateFormatEG;

public class TagValeur extends BodyTagSupport {

    /**
     * valeur � afficher (si ce n'est pas une date)
     */
    protected java.lang.String valeur = "";
    /**
     * valeur inhibant l'affichage
     */
    protected java.lang.String valeurNulle = "";
    /**
     * Indicateur si traitement des retours chariots pour affichage html
     */
    protected boolean expand = false;
    /**
     * Indicateur si traitement des puces pour affichage html
     */
    protected boolean puces = false;
    /**
     * Format d'affichage des dates
     */
    protected String format = null;
    /**
     * Timezone de r�f�rence pour les dates/heures
     */
    protected boolean timezone = false;
    /**
     * Valeur � afficher si date
     */
    protected Calendar valeurDate = null;
    /**
     * Indicateur si heure d�cimale
     */
    protected boolean heureDec = false;
    /**
     * Indicateur si html dans le texte
     */
    protected boolean includeHTML = false;

    /**
     * Indicateur si les informations � afficher font partie d'une URL
     */
    protected boolean url = false;
    
    public TagValeur() {
		super();
		// Initialise les attributs par d�faut
		try {
			doEndTag();
		} catch (JspException ignored) {
			// RAS
		}
	}
	/**
     * Effet de bord : Positionne l'attribut Longueur � la longueure de la chaine affich�e
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doAfterBody() {

        BodyContent body = getBodyContent();
        
        if (format == null) {
    		// Par d�faut
    		format = "dd/MM/yyyy HH:mm:ss";
    		if (pageContext != null) {
    			HttpSession mySession = pageContext.getSession();
    			if (mySession != null) {
    				BasicSession myBasicSession = (BasicSession) mySession.getAttribute("SalonSession");
    				if (myBasicSession != null) {
    					format = myBasicSession.getMessagesBundle().getString("format.dateDefaut");
    				}
    			}
    		}
        }

        JspWriter out = body.getEnclosingWriter();
        String texte = body.getString();
        String chaineCherchee = "%%";
        int oldPos = 0;
        int pos = texte.indexOf(chaineCherchee);
        String retour = "<br/>";

        try {
            pageContext.getRequest().setAttribute("Longueur", new Integer(0));
            while (pos != -1) {
                // Ecrit la partie avant
                out.print(texte.substring(oldPos, pos));

                if (valeurDate != null) {
                    // Formatte maintenant la date
                    if (timezone) {
                        SimpleDateFormatEG formatDate = new SimpleDateFormatEG(format);
                        formatDate.setTimeZone(valeurDate.getTimeZone());
                        valeur = formatDate.formatEG(valeurDate.getTime());
                    } else {
                        SimpleDateFormat formatDate = new SimpleDateFormat(format);
                        formatDate.setTimeZone(valeurDate.getTimeZone());
                        valeur = formatDate.format(valeurDate.getTime());
                    }

                    // suppression des heures si � 0
                    int posDate = valeur.indexOf("00:00:00");
                    if (posDate != -1) {
                        // pos - 1 pour supprimer le blanc d'avant
                        valeur = valeur.substring(0, posDate - 1);
                    }
                }

                if (!(((valeur == null) && (valeurNulle == null)) 
                    || ((valeur != null) && (valeur.equals(valeurNulle))))) {

                    String aAfficher = null;
                    if (heureDec) {
                        try {
                            double valeurDec = Double.parseDouble(valeur);
                            aAfficher = Long.toString((long) valeurDec) + ":";
                            long minute = (long) (((valeurDec * 100) % 100) * 60 / 100);
                            if (minute > 10) {
                                aAfficher += Long.toString(minute);
                            } else {
                                aAfficher += "0" + Long.toString(minute);
                            }
                        } catch (NumberFormatException e) {
                            aAfficher = "###";
                        }
                    } else if (includeHTML) {
                        aAfficher = valeur;
                    }
                    else if (url) {
                    	aAfficher = URLEncoder.encode(valeur);
                    }
                    else {
                        aAfficher = ServletUtil.htmlEncode(valeur);
                    }
                    if (expand) {
                        // remplace les retours chariots
                        for (int i = aAfficher.indexOf("\n"); i != -1; i = aAfficher.indexOf("\n")) {
                            aAfficher = aAfficher.substring(0, i) + "<br/>" + aAfficher.substring(i + 1);
                        }
                    }
                    if (puces) {
                        boolean debutPuces = true;
                        // remplace les retours chariots
                        for (int i = aAfficher.indexOf(retour + "-"); i != -1; i = aAfficher.indexOf(retour + "-")) {
                            if (debutPuces) {
                                aAfficher = aAfficher.substring(0, i) + "</p><ul><li>" + aAfficher.substring(i + retour.length() + 1);
                                debutPuces = false;
                            } else {
                                aAfficher = aAfficher.substring(0, i) + "</li><li>" + aAfficher.substring(i + retour.length() + 1);
                            }
                        }
                        if (!debutPuces) {
                            // Il faut terminer le ul
                            int lastLI = aAfficher.lastIndexOf("<li>");
                            int nextBR = aAfficher.indexOf("<br/>", lastLI);
                            if (nextBR > 0) {
                                aAfficher = aAfficher.substring(0, nextBR) + "</li></ul><p>" + aAfficher.substring(nextBR + retour.length());
                            } else {
                                aAfficher = aAfficher + "</li></ul><p>";
                            }
                        }
                    }
                    out.print(aAfficher);
                    pageContext.getRequest().setAttribute("Longueur", new Integer(valeur.length()));
                }

                oldPos = pos + chaineCherchee.length();
                pos = texte.indexOf(chaineCherchee, oldPos);
            }

            out.print(texte.substring(oldPos));
        } catch (IOException e) {
            System.out.println("doAfterBody : " + e.toString());
        }
        return SKIP_BODY;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 15:50:06)
     * @return java.lang.String
     */
    public java.lang.String getFormat() {
        return format;
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
     * Creation date: (02/11/2001 16:00:26)
     * @return java.util.Calendar
     */
    public java.util.Calendar getValeurDate() {
        return valeurDate;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @return java.lang.String
     */
    protected java.lang.String getValeurNulle() {
        return valeurNulle;
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 16:17:48)
     * @return boolean
     */
    public boolean isExpand() {
        return expand;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:13:43)
     * @return boolean
     */
    public boolean isTimezone() {
        return timezone;
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 16:17:48)
     * @param newExpand String
     */
    public void setExpand(String newExpand) {
        if (newExpand != null) {
            expand = newExpand.equals("true");
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 16:17:48)
     * @param newExpand boolean
     */
    public void setExpand(boolean newExpand) {
        expand = newExpand;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 15:50:06)
     * @param newFormat java.lang.String
     */
    public void setFormat(java.lang.String newFormat) {
        format = newFormat;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:13:43)
     * @param newTimezone String
     */
    public void setTimezone(String newTimezone) {
        if (newTimezone != null) {
            timezone = newTimezone.equals("true");
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:13:43)
     * @param newTimezone boolean
     */
    public void setTimezone(boolean newTimezone) {
        timezone = newTimezone;
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
     * @param newValeur Integer
     */
    public void setValeur(Integer newValeur) {
        if (newValeur == null) {
            valeur = "0";
        } else {
            valeur = newValeur.toString();
        }
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
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur java.math.BigDecimal
     */
    public void setValeur(java.math.BigDecimal newValeur) {
        if (newValeur == null) {
            valeur = "0";
        } else {
            valeur = newValeur.toString();
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur java.util.Calendar
     */
    public void setValeur(java.util.Calendar newValeur) {
        valeurDate = newValeur;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur java.util.Calendar
     */
    public void setValeur(Date newValeur) {
        if (newValeur == null) {
            valeur = null;
        } else {
            valeurDate = Calendar.getInstance();
            valeurDate.setTime(newValeur);
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:00:26)
     * @param newValeurDate java.util.Calendar
     */
    public void setValeurDate(java.util.Calendar newValeurDate) {
        valeurDate = newValeurDate;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeurNulle java.lang.String
     */
    public void setValeurNulle(java.lang.String newValeurNulle) {
        if (newValeurNulle.equals("null")) {
            valeurNulle = null;
        } else {
            valeurNulle = newValeurNulle;
        }
    }
    /**
     * Gets the heureDec.
     * @return Returns a boolean
     */
    public boolean getHeureDec() {
        return heureDec;
    }

    /**
     * Sets the heureDec.
     * @param heureDec The heureDec to set
     */
    public void setHeureDec(boolean heureDec) {
        this.heureDec = heureDec;
    }

    /**
     * Returns the heureDec.
     * @return boolean
     */
    public boolean isHeureDec() {
        return heureDec;
    }

    /**
     * Returns the puces.
     * @return boolean
     */
    public boolean isPuces() {
        return puces;
    }

    /**
     * Sets the puces.
     * @param puces The puces to set
     */
    public void setPuces(boolean puces) {
        this.puces = puces;
    }

    /**
     * Sets the puces.
     * @param puces The puces to set
     */
    public void setPuces(String puces) {
        if (puces != null) {
            this.puces = puces.equals("true");
        }
    }

    /**
     * @return indicateur si le continu est de HTML
     */
    public boolean isIncludeHTML() {
        return includeHTML;
    }

    /**
     * @param b indicateur si le continu est de HTML
     */
    public void setIncludeHTML(boolean b) {
        includeHTML = b;
    }

    /**
     * @param s indicateur si le continu est de HTML
     */
    public void setIncludeHTML(String s) {
        if (s != null) {
            this.includeHTML = s.equals("true");
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        valeur = "";
        valeurNulle = "";
        expand = false;
        puces = false;
        timezone = false;
        valeurDate = null;
        heureDec = false;
        format = null;
        url = false;
        return super.doEndTag();
    }
	/**
	 * @return Returns the url.
	 */
	public boolean isUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(boolean url) {
		this.url = url;
	}

    /**
     * @param s The url to set.
     */
    public void setUrl(String s) {
        if (s != null) {
            this.url = s.equals("true");
        }
    }

}
