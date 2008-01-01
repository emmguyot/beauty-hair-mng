/*
 * Gestion des devises
 * Copyright (C) 2003-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.salon.servlet;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.DeviseBean;
import com.increg.salon.bean.SalonSession;

public class FicDevise extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

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
            if (assertOrError((aDevise != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aDevise = DeviseBean.getDeviseBean(myDBSession, CD_DEVISE);
            if (assertOrError((aDevise != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

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
            if (assertOrError((aDevise != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

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
				log.error("Erreur à la création", e);
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

	request.setAttribute("DeviseBean", aDevise);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficDevise.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
