package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.*;

/**
 * Création d'un fournisseur
 * Creation date: (03/09/2001 21:57:30)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicFourn extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_FOURN = request.getParameter("CD_FOURN");
	String RAIS_SOC = request.getParameter("RAIS_SOC");
	String CIVILITE_CONT = request.getParameter("CIVILITE_CONT");
	String NOM_CONT = request.getParameter("NOM_CONT");
	String PRENOM_CONT = request.getParameter("PRENOM_CONT");
	String RUE = request.getParameter("RUE");
	String CD_POSTAL = request.getParameter("CD_POSTAL");
	String VILLE = request.getParameter("VILLE");
	String TEL = request.getParameter("TEL");
	String FAX = request.getParameter("FAX");
	String PORTABLE = request.getParameter("PORTABLE");
	String EMAIL = request.getParameter("EMAIL");
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
	String COMM = request.getParameter("COMM");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	FournBean aFourn = null;
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aFourn = new FournBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement le client

			/**
			 * Création du bean et enregistrement
			 */
			aFourn = new FournBean();
			aFourn.setCD_FOURN(CD_FOURN);
			aFourn.setRAIS_SOC(RAIS_SOC);
			aFourn.setCIVILITE_CONT(CIVILITE_CONT);
			aFourn.setNOM_CONT(NOM_CONT);
			aFourn.setPRENOM_CONT(PRENOM_CONT);
			aFourn.setRUE(RUE);
			aFourn.setCD_POSTAL(CD_POSTAL);
			aFourn.setVILLE(VILLE);
			aFourn.setTEL(TEL);
			aFourn.setFAX(FAX);
			aFourn.setPORTABLE(PORTABLE);
			aFourn.setEMAIL(EMAIL);
            aFourn.setCD_MOD_REGL(CD_MOD_REGL);
			aFourn.setCOMM(COMM);

			try {
	            aFourn.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (RAIS_SOC == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aFourn = FournBean.getFournBean(myDBSession, CD_FOURN);
            if (assertOrError((aFourn != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aFourn = FournBean.getFournBean(myDBSession, CD_FOURN);
            if (assertOrError((aFourn != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			aFourn.setCD_FOURN(CD_FOURN);
			aFourn.setRAIS_SOC(RAIS_SOC);
			aFourn.setCIVILITE_CONT(CIVILITE_CONT);
			aFourn.setNOM_CONT(NOM_CONT);
			aFourn.setPRENOM_CONT(PRENOM_CONT);
			aFourn.setRUE(RUE);
			aFourn.setCD_POSTAL(CD_POSTAL);
			aFourn.setVILLE(VILLE);
			aFourn.setTEL(TEL);
			aFourn.setFAX(FAX);
			aFourn.setPORTABLE(PORTABLE);
			aFourn.setEMAIL(EMAIL);
            aFourn.setCD_MOD_REGL(CD_MOD_REGL);
			aFourn.setCOMM(COMM);

			try {
	            aFourn.maj(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
			}
		}
		else if (Action.equals("Suppression")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aFourn = FournBean.getFournBean(myDBSession, CD_FOURN);
            if (assertOrError((aFourn != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aFourn.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aFourn = new FournBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else {
			log.error("Action non codée : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		log.error("Erreur générale : ", e);
	}

    /**
     * Reset de la transaction pour la recherche des informations complémentaires
     */
    myDBSession.cleanTransaction();

	request.setAttribute("FournBean", aFourn);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficFourn.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
