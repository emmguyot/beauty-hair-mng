package com.increg.salon.servlet;

import com.increg.commun.*;
import com.increg.salon.bean.*;

import javax.servlet.*;

import java.io.*;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Insert the type's description here.
 * Creation date: (08/07/2001 12:48:48)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class ConnectedServlet extends javax.servlet.http.HttpServlet {
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
	 * V�rifie la connexion
	 * Creation date: (08/07/2001 12:50:37)
	 * @return boolean
	 */
	protected boolean verifCnx(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
	
		HttpSession mySession = request.getSession(false);
		if ((mySession == null) || (mySession.getAttribute("SalonSession") == null) || (((SalonSession) mySession.getAttribute("SalonSession")).getMyIdent() == null)){
			try {
				forward (request, response, "/reconnect.html");
			}
			catch (Exception e) {
				log("Erreur � la v�rification de connexion : " + e.toString());
				try {
					response.sendError(500);
				}
				catch (Exception e2) {
					log ("Erreur au sendError : " + e2.toString());
				}
			}
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
	 * V�rifie une condition de bon fonctionnement
	 * @param condition Condition � respecter
	 * @param messageErreur Message d'erreur
	 * @param request Requete HTTP
	 * @param response R�ponse HTTP
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
				log.error("Erreur � la redirection", e);
			} catch (ServletException e) {
				log.error("Erreur � la redirection", e);
			}
		}
		return !condition;
	}
}
