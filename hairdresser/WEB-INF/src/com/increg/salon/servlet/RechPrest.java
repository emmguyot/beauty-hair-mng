package com.increg.salon.servlet;

import com.increg.commun.*;
import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Recherche/Liste de prestation 
 * Creation date: (24/08/2001 07:40:38)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechPrest extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération du paramètre
	String premLettre = request.getParameter("premLettre");
	if (premLettre == null) {
		premLettre = "A";
		request.setAttribute("premLettre", premLettre);
	}
	request.setAttribute("premLettre", premLettre);
	String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
	String CD_CATEG_PREST = request.getParameter("CD_CATEG_PREST");
	String CD_MARQUE = request.getParameter("CD_MARQUE");
    String PERIME = request.getParameter("PERIME");

	// Constitue la requete SQL
	String reqSQL = "select * from PREST ";
	if (premLettre.charAt(0) != ' ') {
		reqSQL = reqSQL + " where upper(ltrim(PREST.LIB_PREST)) like '" + premLettre + "%'";
	}
	else {
		reqSQL = reqSQL + " where 1=1";
	}
	if ((CD_TYP_VENT != null) && (CD_TYP_VENT.length() > 0)) {
		reqSQL = reqSQL + " and CD_TYP_VENT=" + CD_TYP_VENT;
	}
	if ((CD_CATEG_PREST != null) && (CD_CATEG_PREST.length() > 0)) {
		reqSQL = reqSQL + " and CD_CATEG_PREST=" + CD_CATEG_PREST;
	}
	if ((CD_MARQUE != null) && (CD_MARQUE.length() > 0)) {
		reqSQL = reqSQL + " and CD_MARQUE=" + CD_MARQUE;
	}
    if ((PERIME == null) || (!PERIME.equals("on"))) {
        reqSQL = reqSQL + " and INDIC_PERIM='N'";
    }
	reqSQL = reqSQL + " order by LIB_PREST, CD_TYP_VENT, CD_CATEG_PREST";

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			lstLignes.add(new PrestBean(aRS));
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstPrest.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur dans performTask : ", e);
		try {
			response.sendError(500);
		}
		catch (Exception e2) {
			log.error("Erreur sur sendError : ", e2);
		}
	}
 } 
}
