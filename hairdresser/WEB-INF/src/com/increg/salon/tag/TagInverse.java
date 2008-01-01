/*
 * Tag Affichant un montant à l'envers
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import java.math.BigDecimal;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.increg.commun.BasicSession;

public class TagInverse extends TagSupport {
    
    /**
     * Montant à afficher
     */
    protected BigDecimal montant = null;

    /**
     * TagInfo constructor comment.
     */
    public TagInverse() {
        super();
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doStartTag() {

        JspWriter out = pageContext.getOut();

        try {
            if (montant != null) {
                // Ecrit le message de remerciement
        	    if ((pageContext == null) 
        	    		|| (pageContext.getSession() == null) 
        	    		|| (pageContext.getSession().getAttribute("SalonSession") == null)) {
        	    }
        	    else {
        		    HttpSession mySession = pageContext.getSession();
        			BasicSession myBasicSession = (BasicSession) mySession.getAttribute("SalonSession");
                    out.println("<img src=\"images/" + myBasicSession.getLangue().getLanguage() + "/Merci.gif\" align=\"top\"/>&nbsp;&nbsp;&nbsp;");
        	    }
                // Ecrit le symbole de monnaie
                // out.println("<img src=\"images/EuroInverse.gif\" align=\"top\">");
                // Ecrit le montant
                String chaine = montant.toString();
                for (int i = (chaine.length() - 1); i >= 0; i--) {
                    if (chaine.charAt(i) == '.') {
                        out.println("<img src=\"images/point.gif\" align=\"top\"/>");
                    } else if (Character.isDigit(chaine.charAt(i))) {
                        out.println("<img src=\"images/" + chaine.charAt(i) + ".gif\"/>");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 20:57:09)
     * @return BigDecimal
     */
    public BigDecimal getMontant() {
        return montant;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 20:57:09)
     * @param newMontant BigDecimal
     */
    public void setMontant(String newMontant) {
        montant = new BigDecimal(newMontant);
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 20:57:09)
     * @param newMontant BigDecimal
     */
    public void setMontant(BigDecimal newMontant) {
        montant = newMontant;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        montant = null;
        return super.doEndTag();
    }

}
