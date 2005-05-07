package com.increg.salon.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.salon.bean.IdentBean;
import com.increg.salon.bean.SalonSession;


/**
 * Servlet de v�rification de l'identification au logiciel
 * Creation date: (07/07/2001 20:46:00)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
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

	// V�rification de la pr�sence du Bean Session	    
	HttpSession mySession = request.getSession(true);

	// R�cup�ration du Bean Session
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

	// D�j� perdu ?, on le recr�e
	if (mySalon == null) {
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/").forward(request, response);
        }
        catch (Exception e) {
            try {
                response.sendError(500);
            }
            catch (Exception e2) {
                // Ignore : Il n'y a plus rien � faire
            }
            return;
        }
	}

	// R�cup�re le param�tre
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
			mySalon.setMessage ("Erreur", "Ce compte est bloqu�. Vous ne pouvez plus utiliser l'application. Si cela vous semble anormal, contacter le salon.");
			// Retour au portail
		    getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp").forward(request, response);
	    }
	}
	catch (Exception e) {
		mySalon.setMessage ("Erreur", "Votre mot de passe est erron�.");
		try {
			// Retour au portail
		    if (getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp") == null) {
				System.out.println ("NULL!!!!!");
		    }
			getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp").forward(request, response);			    
		}
		catch (Exception e2) {
			System.out.println ("Exception sur retour Portail : " + e2.toString());
            try {
                response.sendError(500);
            }
            catch (Exception e3) {
                // Ignore : Il n'y a plus rien � faire
            }
            return;
		}
	}
}
}
