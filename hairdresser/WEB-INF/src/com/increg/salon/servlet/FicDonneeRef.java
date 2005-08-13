package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Gestion des données de référence
 * Creation date: (02/09/2001 09:33:55)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicDonneeRef extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String nomTable = request.getParameter("nomTable");
	String CD = request.getParameter("CD");
	String LIB = request.getParameter("LIB");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	DonneeRefBean aDonneeRef = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aDonneeRef = new DonneeRefBean(nomTable);
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aDonneeRef = new DonneeRefBean(nomTable);
			aDonneeRef.setCD(CD);
			aDonneeRef.setLIB(LIB);

			try {
	            aDonneeRef.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aDonneeRef = DonneeRefBean.getDonneeRefBean(myDBSession, nomTable, CD);
            if (assert((aDonneeRef != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aDonneeRef = DonneeRefBean.getDonneeRefBean(myDBSession, nomTable, CD);
            if (assert((aDonneeRef != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			aDonneeRef.setCD(CD);
			aDonneeRef.setLIB(LIB);

			try {
	            aDonneeRef.maj(myDBSession);
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
			aDonneeRef = DonneeRefBean.getDonneeRefBean(myDBSession, nomTable, CD);
            if (assert((aDonneeRef != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aDonneeRef.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aDonneeRef = new DonneeRefBean(nomTable);
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
			}
		}
		else if (Action.equals("Duplication")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aDonneeRef = new DonneeRefBean(nomTable);
			aDonneeRef.setLIB(LIB);

			try {
	            aDonneeRef.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
			}
			request.setAttribute("Action", "Modification");
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

	request.setAttribute("DonneeRefBean", aDonneeRef);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficDonneeRef.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicDonneeRef::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
