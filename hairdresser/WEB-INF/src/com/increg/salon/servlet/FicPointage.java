package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Création d'un pointage
 * Creation date: (07/09/2001 13:38:32)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicPointage extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_COLLAB = request.getParameter("CD_COLLAB");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String HR_DEBUT = request.getParameter("HR_DEBUT");
	String HR_FIN = request.getParameter("HR_FIN");
	String CD_TYP_POINTAGE = request.getParameter("CD_TYP_POINTAGE");
	String COMM = request.getParameter("COMM");

	// Concatene les heures pour former un timestamp
	if ((DT_DEBUT != null) && (HR_DEBUT != null)) {
	    DT_DEBUT = DT_DEBUT + " " + HR_DEBUT;
	    DT_DEBUT = DT_DEBUT.trim();
	}
	if ((DT_FIN != null) && (HR_FIN != null)) {
	    DT_FIN = DT_FIN + " " + HR_FIN;
	    DT_FIN = DT_FIN.trim();
	}

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	PointageBean aPointage = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aPointage = new PointageBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aPointage = new PointageBean();

			try {
	            aPointage.setCD_COLLAB(CD_COLLAB);
	            aPointage.setDT_FIN(DT_FIN, mySalon.getLangue());
	            aPointage.setDT_DEBUT(DT_DEBUT, mySalon.getLangue());
	            aPointage.setCD_TYP_POINTAGE(CD_TYP_POINTAGE);
	            aPointage.setCOMM(COMM);

	            aPointage.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (CD_TYP_POINTAGE == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aPointage = PointageBean.getPointageBean(myDBSession, CD_COLLAB, DT_DEBUT, mySalon.getLangue());
			request.setAttribute("PointageBean", aPointage);

		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aPointage = PointageBean.getPointageBean(myDBSession, CD_COLLAB, DT_DEBUT, mySalon.getLangue());

			try {
	            //aPointage.setCD_COLLAB(CD_COLLAB);
	            aPointage.setDT_FIN(DT_FIN, mySalon.getLangue());
	            //aPointage.setDT_DEBUT(DT_DEBUT);
	            aPointage.setCD_TYP_POINTAGE(CD_TYP_POINTAGE);
	            aPointage.setCOMM(COMM);

	            aPointage.maj(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if (Action.equals("Suppression")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aPointage = PointageBean.getPointageBean(myDBSession, CD_COLLAB, DT_DEBUT, mySalon.getLangue());

			try {
	            aPointage.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aPointage = new PointageBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
			}
		}
		else {
			System.out.println ("Action non codée : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		System.out.println("Note : " + e.toString());
	}

    /**
     * Reset de la transaction pour la recherche des informations complémentaires
     */
    myDBSession.cleanTransaction();

	request.setAttribute("PointageBean", aPointage);

	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficPointage.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicPointage::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
