package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Gestion des modes de règlement
 * Creation date: (20/01/2002 19:53:14)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicModRegl extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
	String LIB_MOD_REGL = request.getParameter("LIB_MOD_REGL");
	String UTILISABLE = request.getParameter("UTILISABLE");
    String IMP_CHEQUE = request.getParameter("IMP_CHEQUE");
    String RENDU_MONNAIE = request.getParameter("RENDU_MONNAIE");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	ModReglBean aModRegl = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aModRegl = new ModReglBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aModRegl = new ModReglBean();
			aModRegl.setCD_MOD_REGL(CD_MOD_REGL);
			aModRegl.setLIB_MOD_REGL(LIB_MOD_REGL);
			aModRegl.setUTILISABLE(UTILISABLE);
            aModRegl.setIMP_CHEQUE(IMP_CHEQUE);
            aModRegl.setRENDU_MONNAIE(RENDU_MONNAIE);

			try {
	            aModRegl.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_MOD_REGL == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aModRegl = ModReglBean.getModReglBean(myDBSession, CD_MOD_REGL);
            if (assert((aModRegl != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aModRegl = ModReglBean.getModReglBean(myDBSession, CD_MOD_REGL);
            if (assert((aModRegl != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			aModRegl.setCD_MOD_REGL(CD_MOD_REGL);
			aModRegl.setLIB_MOD_REGL(LIB_MOD_REGL);
			aModRegl.setUTILISABLE(UTILISABLE);
            aModRegl.setIMP_CHEQUE(IMP_CHEQUE);
            aModRegl.setRENDU_MONNAIE(RENDU_MONNAIE);

			try {
	            aModRegl.maj(myDBSession);
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
			aModRegl = ModReglBean.getModReglBean(myDBSession, CD_MOD_REGL);
            if (assert((aModRegl != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aModRegl.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aModRegl = new ModReglBean();
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
			aModRegl = new ModReglBean();
			aModRegl.setLIB_MOD_REGL(LIB_MOD_REGL);
			aModRegl.setUTILISABLE(UTILISABLE);
            aModRegl.setIMP_CHEQUE(IMP_CHEQUE);
            aModRegl.setRENDU_MONNAIE(RENDU_MONNAIE);

			try {
	            aModRegl.create(myDBSession);
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

	request.setAttribute("ModReglBean", aModRegl);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficModRegl.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicModRegl::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
