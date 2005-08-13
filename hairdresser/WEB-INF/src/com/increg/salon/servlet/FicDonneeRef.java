package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Gestion des donn�es de r�f�rence
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

	// R�cup�ration des param�tres
	String Action = request.getParameter("Action");
	String nomTable = request.getParameter("nomTable");
	String CD = request.getParameter("CD");
	String LIB = request.getParameter("LIB");

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	DonneeRefBean aDonneeRef = null;
	
	try {
		if (Action == null) {
			// Premi�re phase de cr�ation
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aDonneeRef = new DonneeRefBean(nomTable);
		}
		else if (Action.equals("Creation")) {
			// Cr�e r�ellement la prestation

			/**
			 * Cr�ation du bean et enregistrement
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
			 * Cr�ation du bean et enregistrement
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
			 * Cr�ation du bean et enregistrement
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
			// Cr�e r�ellement la prestation

			/**
			 * Cr�ation du bean et enregistrement
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
			System.out.println ("Action non cod�e : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		System.out.println("Note : " + e.toString());
	}

    /**
     * Reset de la transaction pour la recherche des informations compl�mentaires
     */
    myDBSession.cleanTransaction();

	request.setAttribute("DonneeRefBean", aDonneeRef);
	
	try {
		// Passe la main � la fiche de cr�ation
		getServletConfig().getServletContext().getRequestDispatcher("/ficDonneeRef.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicDonneeRef::performTask : Erreur � la redirection : " + e.toString());
	}
}
}
