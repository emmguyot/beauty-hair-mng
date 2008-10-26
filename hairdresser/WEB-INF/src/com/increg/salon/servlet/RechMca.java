/*
 * Recherche/Liste de mouvements de caisse
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.MvtCaisseBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.ServletUtil;

public class RechMca extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération du paramètre
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");

	
    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateDefaut"));
    DateFormat formatDateDB = myDBSession.getFormatDateTimeSansTZ();
	
    
	// Valeurs par défaut
	// J-7
	Calendar J7 = Calendar.getInstance();
	J7.add(Calendar.DAY_OF_YEAR, -7);
    Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, J7);
    Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, Calendar.getInstance());
    if (dtFin.before(dtDebut)) {
        dtFin = dtDebut;
    }
    request.setAttribute("DT_DEBUT", dtDebut);
    request.setAttribute("DT_FIN", dtFin);
    DT_DEBUT = formatDateDB.format(dtDebut.getTime());
    DT_FIN = formatDateDB.format(dtFin.getTime());
	
	// Constitue la requete SQL
	String reqSQL = "select * from MVT_CAISSE ";
	reqSQL = reqSQL + " where 1=1";
	if ((CD_MOD_REGL != null) && (CD_MOD_REGL.length() > 0)) {
		reqSQL = reqSQL + " and CD_MOD_REGL=" + CD_MOD_REGL;
	}
	if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
		reqSQL = reqSQL + " and DT_MVT >= " + DBSession.quoteWith(DT_DEBUT, '\'');
	}
	if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
		reqSQL = reqSQL + " and DT_MVT <= " + DBSession.quoteWith(DT_FIN, '\'');
	}
	if ((CD_TYP_MCA != null) && (CD_TYP_MCA.length() > 0)) {
		reqSQL = reqSQL + " and CD_TYP_MCA=" + CD_TYP_MCA;
	}
	
	reqSQL = reqSQL + " order by DT_MVT desc, CD_MOD_REGL";

	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			MvtCaisseBean aMvtCaisse = new MvtCaisseBean(aRS);
			lstLignes.add(aMvtCaisse);
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstMvtCaisse.jsp").forward(request, response);

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
