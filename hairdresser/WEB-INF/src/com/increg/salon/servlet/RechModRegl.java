package com.increg.salon.servlet;

import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;/**
 * Liste des modes de réglement
 * Creation date: (02/09/2001 09:31:35)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechModRegl extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	// Constitue la requete SQL
	String reqSQL = "select * from MOD_REGL order by LIB_MOD_REGL";

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			lstLignes.add(new ModReglBean(aRS));
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstModRegl.jsp").forward(request, response);

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
