package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.PointageBean;
import com.increg.salon.bean.SalonSession;

/**
 * Recherche/Liste de pointage
 * Creation date: (29/09/2001 16:35:14)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechPointage extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	// Récupération du paramètre
	String CD_COLLAB = request.getParameter("CD_COLLAB");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String CD_TYP_POINTAGE = request.getParameter("CD_TYP_POINTAGE");

	DateFormat formatDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();

	// Valeurs par défaut
	if (DT_DEBUT == null) {
		// J-7
		Calendar J7 = Calendar.getInstance();
		J7.add(Calendar.DAY_OF_YEAR, -7);
		DT_DEBUT = formatDate.format(J7.getTime());
	}
	if (DT_FIN == null) {
		// J+7
		Calendar J7 = Calendar.getInstance();
		J7.add(Calendar.DAY_OF_YEAR, +7);
		DT_FIN = formatDate.format(J7.getTime());
	}
    try {
        if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
            request.setAttribute("DT_DEBUT", formatDate.parse(DT_DEBUT));
        }
        if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
            request.setAttribute("DT_FIN", formatDate.parse(DT_FIN));
        }
    }
    catch (ParseException e) {
        mySalon.setMessage("Erreur", e.toString());
        e.printStackTrace();
    }
	
	// Constitue la requete SQL
	String reqSQL = "select * from POINTAGE ";
	reqSQL = reqSQL + " where 1=1";
	if ((CD_COLLAB != null) && (CD_COLLAB.length() > 0)) {
		reqSQL = reqSQL + " and CD_COLLAB=" + CD_COLLAB;
	}
	if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
		reqSQL = reqSQL + " and DT_DEBUT >= " + DBSession.quoteWith(DT_DEBUT, '\'');
	}
	if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
		reqSQL = reqSQL + " and DT_DEBUT <= " + DBSession.quoteWith(DT_FIN, '\'');
	}
	if ((CD_TYP_POINTAGE != null) && (CD_TYP_POINTAGE.length() > 0)) {
		reqSQL = reqSQL + " and CD_TYP_POINTAGE=" + CD_TYP_POINTAGE;
	}
	
	reqSQL = reqSQL + " order by DT_DEBUT desc, CD_COLLAB";

	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			PointageBean aPointage = new PointageBean(aRS);
			lstLignes.add(aPointage);
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstPointage.jsp").forward(request, response);

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
