package com.increg.salon.servlet;

import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;/**
 * Liste des tranches d'âge
 * Creation date: (18/11/2001 22:09:25)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechTrAge extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	// Constitue la requete SQL
	String reqSQL = "select * from TR_AGE order by AGE_MIN";

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			lstLignes.add(new TrAgeBean(aRS));
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstTrAge.jsp").forward(request, response);

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
