package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TrAgeBean;

/**
 * Gestion des tranches d'age
 * Creation date: (18/11/2001 22:24:09)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicTrAge extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	HttpServletRequest request,
	HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_TR_AGE = request.getParameter("CD_TR_AGE");
	String LIB_TR_AGE = request.getParameter("LIB_TR_AGE");
	String AGE_MIN = request.getParameter("AGE_MIN");
	String AGE_MAX = request.getParameter("AGE_MAX");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	TrAgeBean aTrAge = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aTrAge = new TrAgeBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la tranche d'âge

			/**
			 * Création du bean et enregistrement
			 */
			aTrAge = new TrAgeBean();

			try {
	            aTrAge.setCD_TR_AGE(CD_TR_AGE);
	            aTrAge.setLIB_TR_AGE(LIB_TR_AGE);
	            aTrAge.setAGE_MIN(AGE_MIN);
	            aTrAge.setAGE_MAX(AGE_MAX);

	            aTrAge.create(myDBSession);

		        mySalon.setMessage("Info", "Création effectuée.");
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_TR_AGE == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aTrAge = TrAgeBean.getTrAgeBean(myDBSession, CD_TR_AGE);
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aTrAge = TrAgeBean.getTrAgeBean(myDBSession, CD_TR_AGE);

			aTrAge.setCD_TR_AGE(CD_TR_AGE);
			aTrAge.setLIB_TR_AGE(LIB_TR_AGE);
			aTrAge.setAGE_MIN(AGE_MIN);
			aTrAge.setAGE_MAX(AGE_MAX);

			try {
	            aTrAge.maj(myDBSession);
	            mySalon.setMessage("Info", "Enregistrement effectué.");
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
			aTrAge = TrAgeBean.getTrAgeBean(myDBSession, CD_TR_AGE);

			try {
	            aTrAge.delete(myDBSession);
	            mySalon.setMessage("Info", "Suppression effectuée.");
	            // Un bean vide
	            aTrAge = new TrAgeBean();
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
            aTrAge = new TrAgeBean();

            try {
                aTrAge.setLIB_TR_AGE(LIB_TR_AGE);
                aTrAge.setAGE_MIN(AGE_MIN);
                aTrAge.setAGE_MAX(AGE_MAX);

                aTrAge.create(myDBSession);

                mySalon.setMessage("Info", "Duplication effectuée. Vous travaillez maintenant sur la copie.");
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

	request.setAttribute("TrAgeBean", aTrAge);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficTrAge.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicParam::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
