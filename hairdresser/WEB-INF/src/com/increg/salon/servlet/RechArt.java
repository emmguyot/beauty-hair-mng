/*
 * Recherche/Liste d'articles
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import com.increg.salon.bean.ArtBean;
import com.increg.salon.bean.MvtStkBean;
import com.increg.salon.bean.SalonSession;

public class RechArt extends ConnectedServlet {
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
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();
		Vector lstMvt = new Vector();

		while (aRS.next()) {
			ArtBean aArt = new ArtBean(aRS, mySalon.getMessagesBundle());
			lstLignes.add(aArt);
			// Dernier mouvement
			String reqSQL2 = "select * from MVT_STK where CD_ART=" + aArt.getCD_ART() + " order by DT_MVT desc limit 1";
			ResultSet aRS2 = myDBSession.doRequest(reqSQL2);

			if (aRS2.next()) {
				lstMvt.add(new MvtStkBean(aRS2, mySalon.getMessagesBundle()));
			}
			else {
				lstMvt.add(new MvtStkBean(mySalon.getMessagesBundle()));
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
