/*
 * Tag Affichant les infos de date d'un Objet Bean
 * Copyright (C) 2001-2022 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.CharSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSessionImpl;

public class TagInclude extends TagSupport {

	// Logger par défaut
	protected Log log = LogFactory.getLog(this.getClass());
	
    /**
     * Cache des fichiers à charger 
     */
    protected static HashMap cache = new HashMap();

    /**
     * Date de dernière mise à jour du cache
     */
    protected static Calendar dateRefresh = Calendar.getInstance();
    
    /**
     * fichier à récupérer
     */
    protected String file = null;
 
    /**
     * Taille du tampon
     */
    protected static final int CHUNK_SIZE = 4096;
    
    /**
     * TagInfo constructor comment.
     */
    public TagInclude() {
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
            // Teste la connection
            SalonSessionImpl mySalon = (SalonSessionImpl) pageContext.getAttribute("SalonSession", PageContext.SESSION_SCOPE);
            DBSession myDBSession = mySalon.getMyDBSession();

            URL curURL = null;
            ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_INFO));
            if (valUrl != null) {
                curURL = new URL(valUrl.getVAL_PARAM() + file);
            }
            
            StringBuffer pageBuf = new StringBuffer();

            // Vérification du cache
            Calendar dateLimite = Calendar.getInstance();
            dateLimite.add(Calendar.HOUR, -5);
            if ((dateRefresh.after(dateLimite)) && (cache.get(curURL.toString()) != null)) {
            	// Utilisation du cache
            	pageBuf.append(cache.get(curURL.toString()));
            }
            else {
	            HttpURLConnection aCon = null;
	            aCon = (HttpURLConnection) curURL.openConnection();
	
	            // Si ok, inclut le fichier
	            aCon.setUseCaches(true);
	            BufferedInputStream pageStream = new BufferedInputStream(aCon.getInputStream());
	            // Lecture
	            byte dataBytes[] = new byte[CHUNK_SIZE];
	            int byteRead = 0;
	            while (byteRead != -1) {
	                byteRead = pageStream.read(dataBytes, 0, CHUNK_SIZE);
	
	                // Stocke dans la chaine
	                if (byteRead != -1) {
	                    pageBuf.append(new String(dataBytes, 0, byteRead, "ISO8859_15"));
	                }
	            }
	            
	            // Stocke dans le cache
	            cache.put(curURL.toString(), pageBuf);
	            dateRefresh = Calendar.getInstance();
            }
            out.println(pageBuf.toString());
        } catch (UnknownHostException e) {
            // Ignore cette exception
            // C'est le rôle de ce tag
        } catch (IOException e) {
        	log.error("Erreur dans TagInclude", e);
            System.out.println("TagInclude : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @return String
     */
    public String getFile() {
        return file;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @param newFile Fichier à inclure
     */
    public void setFile(String newFile) {
        file = newFile;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        file = null;
        return super.doEndTag();
    }

}
