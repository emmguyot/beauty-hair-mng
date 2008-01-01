/*
 * Tag Affichant les infos de date d'un Objet Bean
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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.increg.commun.BasicSession;

public class TagMessage extends TagSupport {
    
    /**
     * Session contenant les messages
     */
    protected BasicSession salonSession = null;

    /**
     * TagInfo constructor comment.
     */
    public TagMessage() {
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
            boolean flagEcriture = false;
            
            if ((salonSession.getMessage("Info") != null) && (salonSession.getMessage("Info").length() > 0)) {
                // Ecrit le message d'information
                out.println("<p class=\"info\">" + salonSession.getMessage("Info") + "</p>");
                flagEcriture = true;
            }
            if ((salonSession.getMessage("Erreur") != null) && (salonSession.getMessage("Erreur").length() > 0)) {
                out.println("<p class=\"erreur\">" + salonSession.getMessage("Erreur") + "</p>");
                flagEcriture = true;
            }
            if (!flagEcriture) {
                // Ligne blanche pour éviter que l'écran se décale
                out.println("<p class=\"info\">&nbsp;</p>");
            }
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @return com.increg.salon.bean.SalonSession
     */
    public BasicSession getSalonSession() {
        return salonSession;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @param newSalonSession com.increg.salon.bean.SalonSession
     */
    public void setSalonSession(BasicSession newSalonSession) {
        salonSession = newSalonSession;
    }
    
    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        salonSession = null;
        return super.doEndTag();
    }

}
