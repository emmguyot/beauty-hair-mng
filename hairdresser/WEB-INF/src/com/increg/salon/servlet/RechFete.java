package com.increg.salon.servlet;

import com.increg.commun.*;
import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;
/**
 * Recherche/Liste de fêtes
 * Creation date: (17/07/2001 13:56:35)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechFete extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	// Récupération du paramètre
	String premLettre = request.getParameter("premLettre");
	if (premLettre == null) {
		premLettre = "A";
	}
	request.setAttribute("premLettre", premLettre);

	// Constitue la requete SQL
	String reqSQL = "select * from FETE where upper(ltrim(PRENOM)) like '" + premLettre + "%' order by PRENOM";

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			lstLignes.add(new FeteBean(aRS));
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstFete.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println ("Erreur dans performTask : " + e.toString());
		try {
			response.sendError(500);
		}
		catch (Exception e2) {
			System.out.println ("Erreur sur sendError : " + e2.toString());
		}
	}
 } 
}
