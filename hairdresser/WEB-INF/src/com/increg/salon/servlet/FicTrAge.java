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

	// R�cup�ration des param�tres
	String Action = request.getParameter("Action");
	String CD_TR_AGE = request.getParameter("CD_TR_AGE");
	String LIB_TR_AGE = request.getParameter("LIB_TR_AGE");
	String AGE_MIN = request.getParameter("AGE_MIN");
	String AGE_MAX = request.getParameter("AGE_MAX");

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	TrAgeBean aTrAge = null;
	
	try {
		if (Action == null) {
			// Premi�re phase de cr�ation
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aTrAge = new TrAgeBean();
		}
		else if (Action.equals("Creation")) {
			// Cr�e r�ellement la tranche d'�ge

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aTrAge = new TrAgeBean();

			try {
	            aTrAge.setCD_TR_AGE(CD_TR_AGE);
	            aTrAge.setLIB_TR_AGE(LIB_TR_AGE);
	            aTrAge.setAGE_MIN(AGE_MIN);
	            aTrAge.setAGE_MAX(AGE_MAX);

	            aTrAge.create(myDBSession);

		        mySalon.setMessage("Info", "Cr�ation effectu�e.");
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
			 * Cr�ation du bean et enregistrement
			 */
			aTrAge = TrAgeBean.getTrAgeBean(myDBSession, CD_TR_AGE);

			aTrAge.setCD_TR_AGE(CD_TR_AGE);
			aTrAge.setLIB_TR_AGE(LIB_TR_AGE);
			aTrAge.setAGE_MIN(AGE_MIN);
			aTrAge.setAGE_MAX(AGE_MAX);

			try {
	            aTrAge.maj(myDBSession);
	            mySalon.setMessage("Info", "Enregistrement effectu�.");
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
			aTrAge = TrAgeBean.getTrAgeBean(myDBSession, CD_TR_AGE);

			try {
	            aTrAge.delete(myDBSession);
	            mySalon.setMessage("Info", "Suppression effectu�e.");
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
             * Cr�ation du bean et enregistrement
             */
            aTrAge = new TrAgeBean();

            try {
                aTrAge.setLIB_TR_AGE(LIB_TR_AGE);
                aTrAge.setAGE_MIN(AGE_MIN);
                aTrAge.setAGE_MAX(AGE_MAX);

                aTrAge.create(myDBSession);

                mySalon.setMessage("Info", "Duplication effectu�e. Vous travaillez maintenant sur la copie.");
                request.setAttribute("Action", "Modification");
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
                request.setAttribute("Action", Action);
            }
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

	request.setAttribute("TrAgeBean", aTrAge);
	
	try {
		// Passe la main � la fiche de cr�ation
		getServletConfig().getServletContext().getRequestDispatcher("/ficTrAge.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicParam::performTask : Erreur � la redirection : " + e.toString());
	}
}
}
