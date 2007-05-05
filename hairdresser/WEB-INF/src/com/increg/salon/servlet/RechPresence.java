package com.increg.salon.servlet;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.Presence;
import com.increg.util.ServletUtil;

/**
 * Recherche/Liste de Présences
 * Creation date: (02/11/2001 17:28:45)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechPresence extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String CD_COLLAB = request.getParameter("CD_COLLAB");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

	// Valeurs par défaut
	// Début de mois
	Calendar J7 = Calendar.getInstance();
	J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
    Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, J7);
    Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, Calendar.getInstance());
    if (dtFin.before(dtDebut)) {
        dtFin = dtDebut;
    }
    request.setAttribute("DT_DEBUT", dtDebut);
    request.setAttribute("DT_FIN", dtFin);
    DT_DEBUT = myDBSession.getFormatDate().format(dtDebut.getTime());
    DT_FIN = myDBSession.getFormatDate().format(dtFin.getTime());

	// Constitue les requetes SQL
	String reqSQL = "select to_char(date_part('year', DT_DEBUT), '9999') || to_char(date_part('week', DT_DEBUT), '99') || ' ' || to_char(COLLAB.CD_COLLAB, '9999') as SEM_COLLAB, min(DT_DEBUT) as DEBUT, "
				+ "COLLAB.PRENOM, POINTAGE.CD_TYP_POINTAGE, "
				+ "LIB_TYP_POINTAGE, "
				+ "sum(date_part('hour', DT_FIN-DT_DEBUT) + date_part('day', DT_FIN-DT_DEBUT)*24 + date_part('minute',DT_FIN-DT_DEBUT)/60.00) as TOTAL_HEURE "
				+ "from POINTAGE, COLLAB, TYP_POINTAGE "
				+ "where POINTAGE.CD_COLLAB = COLLAB.CD_COLLAB "
				+ "and TYP_POINTAGE.CD_TYP_POINTAGE = POINTAGE.CD_TYP_POINTAGE ";

	if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
		reqSQL = reqSQL + " and POINTAGE.DT_DEBUT >= '" + DT_DEBUT + "'";
	}
	if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
		reqSQL = reqSQL + " and POINTAGE.DT_FIN < '" + DT_FIN + "'::date + 1";
	}
	if ((CD_COLLAB != null) && (CD_COLLAB.length() > 0)) {
		reqSQL = reqSQL + " and POINTAGE.CD_COLLAB=" + CD_COLLAB;
	}
	reqSQL = reqSQL + " group by SEM_COLLAB, COLLAB.PRENOM, POINTAGE.CD_TYP_POINTAGE, LIB_TYP_POINTAGE";

	TreeMap lstLignes = new TreeMap();
	TreeMap lstType = new TreeMap();

	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);

		while (aRS.next()) {
			// Récupère l'élément de la liste
			String semaine = aRS.getString("SEM_COLLAB");

			Presence aPresence = (Presence) lstLignes.get(semaine);

			if (aPresence == null) {
				aPresence = new Presence();
				aPresence.setDebut(aRS.getTimestamp("DEBUT"));
				aPresence.setPRENOM(aRS.getString("PRENOM"));
			}

            // Assure que la date affichée est toujours la plus petite
            if (aPresence.getDebut().after(aRS.getTimestamp("DEBUT"))) {
                aPresence.setDebut(aRS.getTimestamp("DEBUT"));
            }
                
			// Positionne les infos
			int cd = aRS.getInt("CD_TYP_POINTAGE");
			BigDecimal totalHeure = new BigDecimal(aRS.getDouble("TOTAL_HEURE"));
			totalHeure = totalHeure.setScale(1, BigDecimal.ROUND_HALF_UP);
			
			aPresence.setPointage(cd, totalHeure);

			// Ajoute l'element modifié
			lstLignes.put(semaine, aPresence);

			lstType.put(new Integer(aRS.getInt("CD_TYP_POINTAGE")), aRS.getString("LIB_TYP_POINTAGE"));
		}
		aRS.close();
	}
	catch (Exception e) {
		log.error("Erreur dans performTask : ", e);
		try {
			response.sendError(500);
			return;
		}
		catch (Exception e2) {
			log.error("Erreur sur sendError : ", e2);
		}
	}
		
	try {
		// Stocke les Map pour le JSP
		request.setAttribute("Liste", lstLignes);
		request.setAttribute("ListeType", lstType);

		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstPresence.jsp").forward(request, response);

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
