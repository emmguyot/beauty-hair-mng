package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;
/**
 * Gestion des devises
 * Creation date: 26 févr. 03
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicDevise extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_DEVISE = request.getParameter("CD_DEVISE");
	String LIB_COURT_DEVISE = request.getParameter("LIB_COURT_DEVISE");
	String LIB_DEVISE = request.getParameter("LIB_DEVISE");
    String RATIO = request.getParameter("RATIO");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	DeviseBean aDevise = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aDevise = new DeviseBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aDevise = new DeviseBean();
			aDevise.setCD_DEVISE(CD_DEVISE);
			aDevise.setLIB_COURT_DEVISE(LIB_COURT_DEVISE);
			aDevise.setLIB_DEVISE(LIB_DEVISE);
            aDevise.setRATIO(RATIO);

			try {
	            aDevise.create(myDBSession);
                mySalon.resetDevise();
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_COURT_DEVISE == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aDevise = DeviseBean.getDeviseBean(myDBSession, CD_DEVISE);
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aDevise = DeviseBean.getDeviseBean(myDBSession, CD_DEVISE);

			aDevise.setCD_DEVISE(CD_DEVISE);
			aDevise.setLIB_COURT_DEVISE(LIB_COURT_DEVISE);
			aDevise.setLIB_DEVISE(LIB_DEVISE);
            aDevise.setRATIO(RATIO);

			try {
	            aDevise.maj(myDBSession);
                mySalon.resetDevise();
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
			aDevise = DeviseBean.getDeviseBean(myDBSession, CD_DEVISE);

			try {
	            aDevise.delete(myDBSession);
                mySalon.resetDevise();
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aDevise = new DeviseBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
			}
		}
		else if (Action.equals("Duplication")) {
			// Duplique la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aDevise = new DeviseBean();
			aDevise.setLIB_COURT_DEVISE(LIB_COURT_DEVISE);
			aDevise.setLIB_DEVISE(LIB_DEVISE);
            aDevise.setRATIO(RATIO);

			try {
	            aDevise.create(myDBSession);
                mySalon.resetDevise();
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
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

	request.setAttribute("DeviseBean", aDevise);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficDevise.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicDevise::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
