package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Gestion des types de mouvement
 * Creation date: (19/09/2001 13:34:23)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicTypMvt extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
	String LIB_TYP_MVT = request.getParameter("LIB_TYP_MVT");
	String SENS_MVT = request.getParameter("SENS_MVT");
    String TRANSF_MIXTE = request.getParameter("TRANSF_MIXTE");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	TypMvtBean aTypMvt = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aTypMvt = new TypMvtBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aTypMvt = new TypMvtBean();
			aTypMvt.setCD_TYP_MVT(CD_TYP_MVT);
			aTypMvt.setLIB_TYP_MVT(LIB_TYP_MVT);
			aTypMvt.setSENS_MVT(SENS_MVT);
            aTypMvt.setTRANSF_MIXTE(TRANSF_MIXTE);

			try {
	            aTypMvt.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_TYP_MVT == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aTypMvt = TypMvtBean.getTypMvtBean(myDBSession, CD_TYP_MVT);
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aTypMvt = TypMvtBean.getTypMvtBean(myDBSession, CD_TYP_MVT);

			aTypMvt.setCD_TYP_MVT(CD_TYP_MVT);
			aTypMvt.setLIB_TYP_MVT(LIB_TYP_MVT);
            aTypMvt.setTRANSF_MIXTE(TRANSF_MIXTE);
            
            if (!aTypMvt.getSENS_MVT().equals(SENS_MVT)) {
                // Modification du sens : Vérification si possible
                if (aTypMvt.sensIsModifiable(myDBSession)) {
                    aTypMvt.setSENS_MVT(SENS_MVT);
                }
                else {
                    mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficTypMvt.typeUtilise" + BasicSession.TAG_I18N);
                }
            }

			try {
	            aTypMvt.maj(myDBSession);
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
			aTypMvt = TypMvtBean.getTypMvtBean(myDBSession, CD_TYP_MVT);

			try {
	            aTypMvt.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aTypMvt = new TypMvtBean();
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
			aTypMvt = new TypMvtBean();
			aTypMvt.setLIB_TYP_MVT(LIB_TYP_MVT);
			aTypMvt.setSENS_MVT(SENS_MVT);
            aTypMvt.setTRANSF_MIXTE(TRANSF_MIXTE);

			try {
	            aTypMvt.create(myDBSession);
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

	request.setAttribute("TypMvtBean", aTypMvt);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficTypMvt.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicTypMvt::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
