/*
 * Recherche/Liste de prestation 
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

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.PrestBean;
import com.increg.salon.bean.SalonSession;

public class RechPrest extends ConnectedServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 3772902617467403694L;

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
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector<PrestBean> lstLignes = new Vector<PrestBean>();

		while (aRS.next()) {
			lstLignes.add(new PrestBean(aRS));
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
        // Mémorise la liste pour la pagination
        mySalon.setListePrestation(lstLignes);
		
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
