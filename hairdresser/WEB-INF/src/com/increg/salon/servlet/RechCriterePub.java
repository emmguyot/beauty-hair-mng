/*
 * Liste des données de référence
 * Copyright (C) 2003-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import com.increg.salon.bean.CriterePubBean;
import com.increg.salon.bean.SalonSession;

public class RechCriterePub extends ConnectedServlet {

/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Constitue la requete SQL
	String reqSQL = "select * from CRITERE_PUB order by LIB_CRITERE_PUB";

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();
	
	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			lstLignes.add(new CriterePubBean(aRS, mySalon.getMessagesBundle()));
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstCriterePub.jsp").forward(request, response);

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
