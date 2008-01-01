/*
 * Tag affichant le TimeStamp d'un Bean
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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.increg.commun.BasicSession;

import java.io.*;
import java.text.MessageFormat;
/**
 * Tag Affichant les infos de date d'un Objet Bean
 * Prérequis : Une session est créée (Salon), la locale est positionnée et un bundle contient le message
 * Creation date: (22/08/2001 22:00:08)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagTimeStamp extends TagSupport {
    /**
     * bean : Object dont l'information de temps doit être affichée
     */
    protected com.increg.commun.TimeStampBean bean = null;

    /**
     * TagInfo constructor comment.
     */
    public TagTimeStamp() {
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
	        HttpSession mySession = pageContext.getSession();
			BasicSession myBasicSession = (BasicSession) mySession.getAttribute("SalonSession");

			// Ecrit le début
			String messageTimeStamp = myBasicSession.getMessagesBundle().getString("message.timestamp");
			MessageFormat msgFormat = new MessageFormat(messageTimeStamp);
			msgFormat.setLocale(myBasicSession.getLangue());
			
            out.print("\"" + msgFormat.format(new Object[] {
					bean.getDT_CREAT().getTime(), 
					bean.getDT_MODIF().getTime()}) + "\"");
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 22:01:32)
     * @return com.increg.salon.bean.TimeStampBean
     */
    public com.increg.commun.TimeStampBean getBean() {
        return bean;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 22:01:32)
     * @param newBean com.increg.salon.bean.TimeStampBean
     */
    public void setBean(com.increg.commun.TimeStampBean newBean) {
        bean = newBean;
    }
    
    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        bean = null;
        return super.doEndTag();
    }

}
