package com.increg.salon.servlet;

import com.increg.commun.*;
import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;
/**
 * Recherche/Liste d'articles 
 * Creation date: (24/08/2001 07:40:38)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechArt extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	// Récupération du paramètre
	String premLettre = request.getParameter("premLettre");
	if (premLettre == null) {
		premLettre = "A";
		request.setAttribute("premLettre", premLettre);
	}
	request.setAttribute("premLettre", premLettre);
	String CD_TYP_ART = request.getParameter("CD_TYP_ART");
	String CD_CATEG_ART = request.getParameter("CD_CATEG_ART");
	String RUPTURE = request.getParameter("RUPTURE");
    String PERIME = request.getParameter("PERIME");

	// Constitue la requete SQL
	String reqSQL = "select * from ART ";
	if (premLettre.charAt(0) != ' ') {
		reqSQL = reqSQL + " where upper(ltrim(ART.LIB_ART)) like '" + premLettre + "%'";
	}
	else {
		reqSQL = reqSQL + " where 1=1";
	}
	if ((CD_TYP_ART != null) && (CD_TYP_ART.length() > 0)) {
		reqSQL = reqSQL + " and CD_TYP_ART=" + CD_TYP_ART;
	}
	if ((CD_CATEG_ART != null) && (CD_CATEG_ART.length() > 0)) {
		reqSQL = reqSQL + " and CD_CATEG_ART=" + CD_CATEG_ART;
	}
	if ((RUPTURE != null) && (RUPTURE.equals("on"))) {
		reqSQL = reqSQL + " and QTE_STK <= QTE_STK_MIN";
	}
    if ((PERIME == null) || (!PERIME.equals("on"))) {
        reqSQL = reqSQL + " and INDIC_PERIM='N'";
    }
	
	reqSQL = reqSQL + " order by LIB_ART, CD_TYP_ART, CD_CATEG_ART";

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();
		Vector lstMvt = new Vector();

		while (aRS.next()) {
			ArtBean aArt = new ArtBean(aRS);
			lstLignes.add(aArt);
			// Dernier mouvement
			String reqSQL2 = "select * from MVT_STK where CD_ART=" + aArt.getCD_ART() + " order by DT_MVT desc limit 1";
			ResultSet aRS2 = myDBSession.doRequest(reqSQL2);

			if (aRS2.next()) {
				lstMvt.add(new MvtStkBean(aRS2));
			}
			else {
				lstMvt.add(new MvtStkBean());
			}
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		request.setAttribute("ListeMvt", lstMvt);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstArt.jsp").forward(request, response);

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
