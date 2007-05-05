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
import com.increg.salon.bean.MvtStkBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.ServletUtil;

/**
 * Recherche/Liste de mouvements de stock
 * Creation date: (23/09/2001 22:42:51)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechMvt extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération du paramètre
	String CD_ART = request.getParameter("CD_ART");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateDefaut"));
    DateFormat formatDateDB = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

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
	String reqSQL = "select * from MVT_STK ";
	reqSQL = reqSQL + " where 1=1";
	if ((CD_ART != null) && (CD_ART.length() > 0)) {
		reqSQL = reqSQL + " and CD_ART=" + CD_ART;
	}
	if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
		reqSQL = reqSQL + " and DT_MVT >= " + DBSession.quoteWith(DT_DEBUT, '\'');
	}
	if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
		reqSQL = reqSQL + " and DT_MVT <= " + DBSession.quoteWith(DT_FIN, '\'');
	}
	if ((CD_TYP_MVT != null) && (CD_TYP_MVT.length() > 0)) {
		reqSQL = reqSQL + " and CD_TYP_MVT=" + CD_TYP_MVT;
	}
	
	reqSQL = reqSQL + " order by DT_MVT desc, CD_ART";

	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();

		while (aRS.next()) {
			MvtStkBean aMvtStk = new MvtStkBean(aRS, mySalon.getMessagesBundle());
			lstLignes.add(aMvtStk);
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstMvtStk.jsp").forward(request, response);

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
