package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;
/**
 * Gestion des type de ventes
 * Creation date: 18 janv. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicTypVent extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
	String LIB_TYP_VENT = request.getParameter("LIB_TYP_VENT");
	String tCIVILITE[] = request.getParameterValues("CIVILITE");
    String CIVILITE = new String();
    if (tCIVILITE != null) {
        for (int i = 0; i < tCIVILITE.length; i++) {
            if (CIVILITE.length() > 0) {
                CIVILITE += "|";
            }
            CIVILITE += tCIVILITE[i];
        }
    }
    
    String MARQUE = request.getParameter("MARQUE");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	TypVentBean aTypVent = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aTypVent = new TypVentBean();
		}
        else if (Action.equals("Creation")) {
            // Modification effective de la fiche

            /**
             * Création du bean et enregistrement
             */
            aTypVent = new TypVentBean();

            try {
                aTypVent.setCD_TYP_VENT(CD_TYP_VENT);
                aTypVent.setLIB_TYP_VENT(LIB_TYP_VENT);
                aTypVent.setCIVILITE(CIVILITE);
                aTypVent.setMARQUE(MARQUE);

                aTypVent.create(myDBSession);
                mySalon.setMessage("Info", "Création effectuée.");
                request.setAttribute("Action", "Modification");
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
                request.setAttribute("Action", Action);
            }
        }
		else if ((Action.equals("Modification")) && (LIB_TYP_VENT == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

            aTypVent = TypVentBean.getTypVentBean(myDBSession, CD_TYP_VENT);
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
            aTypVent = TypVentBean.getTypVentBean(myDBSession, CD_TYP_VENT);

			aTypVent.setCD_TYP_VENT(CD_TYP_VENT);
			aTypVent.setLIB_TYP_VENT(LIB_TYP_VENT);
   			aTypVent.setCIVILITE(CIVILITE);
            aTypVent.setMARQUE(MARQUE);
            
            try {
                aTypVent.maj(myDBSession);
                mySalon.setMessage("Info", "Enregistrement effectué.");
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
            }
            request.setAttribute("Action", Action);

		}
        else if (Action.equals("Suppression")) {
            // Modification effective de la fiche

            /**
             * Création du bean et enregistrement
             */
            aTypVent = TypVentBean.getTypVentBean(myDBSession, CD_TYP_VENT);

            try {
                aTypVent.delete(myDBSession);
                mySalon.setMessage("Info", "Suppression effectuée.");
                // Un bean vide
                aTypVent = new TypVentBean();
                request.setAttribute("Action", "Creation");
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
            }
            request.setAttribute("Action", Action);

        }
        else if (Action.equals("Duplication")) {
            // Duplique la prestation

            /**
             * Création du bean et enregistrement
             */
            aTypVent = new TypVentBean();

            try {
                aTypVent.setLIB_TYP_VENT(LIB_TYP_VENT);
                aTypVent.setCIVILITE(CIVILITE);
                aTypVent.setMARQUE(MARQUE);

                aTypVent.create(myDBSession);
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

	request.setAttribute("TypVentBean", aTypVent);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficTypVent.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicTypVent::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
