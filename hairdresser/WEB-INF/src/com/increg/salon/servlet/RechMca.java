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
import com.increg.salon.bean.MvtCaisseBean;
import com.increg.salon.bean.SalonSession;

/**
 * Recherche/Liste de mouvements de caisse
 * Creation date: (25/09/2001 22:02:35)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechMca extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	// Récupération du paramètre
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");

	DateFormat formatDate  = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

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
		DT_FIN = formatDate.format(Calendar.getInstance().getTime());
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
