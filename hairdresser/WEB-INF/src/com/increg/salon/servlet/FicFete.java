package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FeteBean;
import com.increg.salon.bean.SalonSession;

/**
 * Gestion des fête
 * Creation date: (22/12/2001 22:33:25)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicFete extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	HttpServletRequest request,
	HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_FETE = request.getParameter("CD_FETE");
	String PRENOM = request.getParameter("PRENOM");
	String DT_FETE = request.getParameter("DT_FETE");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	FeteBean aFete = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aFete = new FeteBean();
		}
		else if (Action.equals("Creation")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aFete = new FeteBean();

			try {
	            aFete.setCD_FETE(CD_FETE);
	            aFete.setPRENOM(PRENOM);
	            aFete.setDT_FETE(DT_FETE);

	            aFete.create(myDBSession);
	            mySalon.setMessage("Info", "Création effectuée.");
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (PRENOM == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aFete = FeteBean.getFeteBean(myDBSession, CD_FETE);
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aFete = FeteBean.getFeteBean(myDBSession, CD_FETE);

			try {
	            aFete.setCD_FETE(CD_FETE);
	            aFete.setPRENOM(PRENOM);
	            aFete.setDT_FETE(DT_FETE);

	            aFete.maj(myDBSession);
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
			aFete = FeteBean.getFeteBean(myDBSession, CD_FETE);

			try {
	            aFete.delete(myDBSession);
	            mySalon.setMessage("Info", "Suppression effectuée.");
	            // Un bean vide
	            aFete = new FeteBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
        else if (Action.equals("Duplication")) {
            // Duplique la prestation

            /**
             * Création du bean et enregistrement
             */
            aFete = new FeteBean();

            try {
                aFete.setPRENOM(PRENOM);
                aFete.setDT_FETE(DT_FETE);

                aFete.create(myDBSession);
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

	request.setAttribute("FeteBean", aFete);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficFete.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicParam::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
