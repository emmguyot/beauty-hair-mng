/*
 * Tag Affichant une information de provenance de l'état (InCrEG) pour publicité
 * Copyright (C) 2002-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.increg.commun.BasicSession;

public class TagMadeBy extends TagSupport {

    /**
     * TagInfo constructor comment.
     */
    public TagMadeBy() {
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
    	    if ((pageContext == null) 
    	    		|| (pageContext.getSession() == null) 
    	    		|| (pageContext.getSession().getAttribute("SalonSession") == null)) {
    	        // Perte de la connexion : valeur par défaut
                out.println("<p style=\"{font-size:7pt; color: #888888}\">Etat réalisé par InCrEG LibertyLook.</p>");
    	    }
    	    else {
    		    HttpSession mySession = pageContext.getSession();
    			BasicSession myBasicSession = (BasicSession) mySession.getAttribute("SalonSession");
                out.println("<p style=\"{font-size:7pt; color: #888888}\">" + myBasicSession.getMessagesBundle().getString("label.madeBy") + "</p>");
    	    }
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }
}
