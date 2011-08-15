/*
 * Servlet de base vérifiant la connexion
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ISalonListeReset;
import com.increg.salon.bean.SalonSession;

public abstract class ConnectedServlet extends javax.servlet.http.HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5508278263770389616L;

	/**
	 * RAZ des points pouvant bloquer (Transaction d'une session, ...)
	 * Creation date: (20/09/2001 21:04:54)
	 */
	protected void cleanUp(HttpServletRequest request) {
		
		HttpSession mySession = request.getSession(false);
		SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
		DBSession myDBSession = mySalon.getMyDBSession();
	
		myDBSession.cleanTransaction();
	}
	/**
	 * Process incoming HTTP GET requests 
	 * 
	 * @param request Object that encapsulates the request to the servlet 
	 * @param response Object that encapsulates the response from the servlet
	 */
	public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
	
		if (verifCnx(request, response)) {
			cleanUp(request);
			performTask(request, response);
		}
	
	}
	/**
	 * Process incoming HTTP POST requests 
	 * 
	 * @param request Object that encapsulates the request to the servlet 
	 * @param response Object that encapsulates the response from the servlet
	 */
	public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
		throws javax.servlet.ServletException, java.io.IOException {
	
		if (verifCnx(request, response)) {
			cleanUp(request);
			performTask(request, response);
	
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/07/2001 14:24:43)
	 * @param urlDest String Adresse destination du forward
	 */
	public void forward(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, String urlDest) throws IOException, ServletException
	{
		getServletConfig().getServletContext().getRequestDispatcher(urlDest).forward(request, response);
	}
	/**
	 * Returns the servlet info string.
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
	public abstract void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response);
	/**
	 * Vérifie la connexion
	 * Creation date: (08/07/2001 12:50:37)
	 * @return boolean
	 */
	protected boolean verifCnx(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
	
		HttpSession mySession = request.getSession(false);
		if ((mySession == null) || (mySession.getAttribute("SalonSession") == null) || (((SalonSession) mySession.getAttribute("SalonSession")).getMyIdent() == null)){
			GoHome(request, response);
			return false;
		}
		else {
			// RAZ des messages
			((SalonSession) mySession.getAttribute("SalonSession")).setMessage("Erreur", (String) null);
			((SalonSession) mySession.getAttribute("SalonSession")).setMessage("Info", (String) null);
		}
		return true;
	}
	
	/**
	 * Vérifie une condition de bon fonctionnement
	 * @param condition Condition à respecter
	 * @param messageErreur Message d'erreur
	 * @param request Requete HTTP
	 * @param response Réponse HTTP
	 * @return true si erreur
	 */
	protected boolean assertOrError(boolean condition, String messageErreur, HttpServletRequest request, HttpServletResponse response) {
		if (!condition) {
	    	Log log = LogFactory.getLog(this.getClass());

	    	HttpSession mySession = request.getSession(false);
	        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	        mySalon.setMessage("Erreur", messageErreur);
	        try {
				forward(request, response, "/Erreur.jsp");
			} catch (IOException e) {
				log.error("Erreur à la redirection", e);
			} catch (ServletException e) {
				log.error("Erreur à la redirection", e);
			}
		}
		return !condition;
	}

	/**
	 * Recherche dans la précédente liste d'une entité l'entité actuelle pour indiquer si il y a un précédent
	 * @param lst
	 * @param CD_ACTUEL
	 * @param resetter
	 * @return CD du précédent ou Null
	 */
	protected Long precedentPossible(Vector<Long> lst, Long CD_ACTUEL, ISalonListeReset resetter) {
		
		Long precedent = null;
	
		if ((lst == null)) {
			return precedent;
		}
		
		for (Long CD : lst) {
			
			if (CD.equals(CD_ACTUEL)) {
				return precedent;
			}
			precedent = CD;
		}
		// Pas trouvé
		resetter.reset();
		return null;
	}
	/**
	 * Recherche dans la précédente liste d'une entité l'entité actuelle pour indiquer si il y a un suivant
	 * @param lst
	 * @param CD_ACTUEL
	 * @param resetter
	 * @return CD du suivant ou Null
	 */
	protected Long suivantPossible(Vector<Long> lst, Long CD_ACTUEL, ISalonListeReset resetter) {
		Long suivant = null;
	
		if ((lst == null)) {
			return suivant;
		}
		
		Boolean nextIsGood = false;
		for (Long CD : lst) {
			
			suivant = CD;
			if (nextIsGood) {
				return suivant;
			}
			if (CD.equals(CD_ACTUEL)) {
				nextIsGood = true;
			}
		}
		// Pas trouvé
		if (!nextIsGood) {
			resetter.reset();
		}
		return null;
	}
	
	public static SalonSession CheckOrGoHome(javax.servlet.http.HttpServletRequest request, HttpServletResponse response) {

		HttpSession mySession = request.getSession(false);
		SalonSession mySalon = null;
		if ((mySession == null) 
				|| ((mySalon = (SalonSession) mySession.getAttribute("SalonSession")) == null)) {
			GoHome(request, response);
	    }
	    return mySalon;
		
	}
	
	protected static void GoHome(javax.servlet.http.HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.sendRedirect(request.getContextPath() + "/reconnect.html");
		} catch (IOException e) {
			Log log = LogFactory.getLog(ConnectedServlet.class);
			log.error("Erreur à la redirection", e);
		}
	}
}
