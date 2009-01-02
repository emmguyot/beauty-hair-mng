/*
 * Servlet de vérification de l'identification au logiciel
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
package com.increg.salon.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.salon.bean.IdentBean;
import com.increg.salon.bean.SalonSession;


public class Ident extends HttpServlet {
/**
 * Process incoming HTTP GET requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 * @throws IOException .
 * @throws ServletException .
 */
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	performTask(request, response);

}
/**
 * Process incoming HTTP POST requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 * @throws IOException .
 * @throws ServletException .
 */
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	performTask(request, response);

}
/**
 * @return the servlet info string.
 */
public String getServletInfo() {

	return super.getServletInfo();

}
/**
 * Initializes the servlet.
 */
public void init() {
	// insert code to initialize the servlet here

}
/**
 * Process incoming requests for information
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Vérification de la présence du Bean Session	    
	HttpSession mySession = request.getSession(true);

	// Récupération du Bean Session
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

	// Déjà perdu ?, on le recrée
	if (mySalon == null) {
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/").forward(request, response);
        }
        catch (Exception e) {
            try {
                response.sendError(500);
            }
            catch (Exception e2) {
                // Ignore : Il n'y a plus rien à faire
            }
        }
        return;
	}

	// Récupère le paramètre
	String pwd = request.getParameter("MOT_PASSE");

	try {
	    IdentBean aIdent = new IdentBean (mySalon.getMyDBSession(), pwd);

	    if (aIdent.getETAT_CPT().equals(IdentBean.ETAT_ACTIF)) {
			// Identification correcte : On sauvegarde et enchaine
			mySalon.setMyIdent(aIdent);
   			mySalon.setMessage ("Erreur", (String) null);
	        getServletConfig().getServletContext().getRequestDispatcher("/main.srv").forward(request, response);
	    }
	    else {
			mySalon.setMessage ("Erreur", BasicSession.TAG_I18N + "ident.compteBloque" + BasicSession.TAG_I18N);
			// Retour au portail
		    getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp").forward(request, response);
	    }
	}
	catch (Exception e) {
		mySalon.setMessage ("Erreur", BasicSession.TAG_I18N + "ident.motDePasseKo" + BasicSession.TAG_I18N);
		try {
			// Retour au portail
		    if (getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp") == null) {
				log.error("NULL!!!!!");
		    }
			getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp").forward(request, response);			    
		}
		catch (Exception e2) {
			log.error("Exception sur retour Portail : ", e2);
            try {
                response.sendError(500);
            }
            catch (Exception e3) {
                // Ignore : Il n'y a plus rien à faire
            }
            return;
		}
	}
}
}
