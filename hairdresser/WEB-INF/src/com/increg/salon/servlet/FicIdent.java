/*
 * Création d'un client
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import com.increg.salon.bean.IdentBean;
import com.increg.salon.bean.SalonSession;

public class FicIdent extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_IDENT = request.getParameter("CD_IDENT");
	String LIB_IDENT = request.getParameter("LIB_IDENT");
	String MOT_PASSE = request.getParameter("MOT_PASSE");
    String MOT_PASSE2 = request.getParameter("MOT_PASSE2");
	String CD_PROFIL = request.getParameter("CD_PROFIL");
	String ETAT_CPT = request.getParameter("ETAT_CPT");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

    IdentBean aIdent = null;
    
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
            aIdent = new IdentBean();
			request.setAttribute("IdentBean", aIdent);
		}
		else if (Action.equals("Creation")) {
			// Crée réellement le Ident

			/**
			 * Création du bean et enregistrement
			 */
			aIdent = new IdentBean();
			aIdent.setCD_IDENT(CD_IDENT);
			aIdent.setLIB_IDENT(LIB_IDENT);
			aIdent.setMOT_PASSE(MOT_PASSE);
			aIdent.setCD_PROFIL(CD_PROFIL);
			aIdent.setETAT_CPT(ETAT_CPT);

            if ((MOT_PASSE2 != null) && (MOT_PASSE != null) && (MOT_PASSE.equals(MOT_PASSE2))) {
    			try {
    				aIdent.create(myDBSession);
    	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
    	            request.setAttribute("Action", "Modification");
    			}
    			catch (Exception e) {
    	            mySalon.setMessage("Erreur", e.toString());
    				request.setAttribute("Action", Action);
    			}
            }
            else {
                mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficIdent.motDePasseDifferent" + BasicSession.TAG_I18N);
                request.setAttribute("Action", Action);
            }
            request.setAttribute("IdentBean", aIdent);

		}
		else if ((Action.equals("Modification")) && (LIB_IDENT == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aIdent = IdentBean.getIdentBean(myDBSession, CD_IDENT);
            if (assertOrError((aIdent != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
			request.setAttribute("IdentBean", aIdent);

		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aIdent = IdentBean.getIdentBean(myDBSession, CD_IDENT);
            if (assertOrError((aIdent != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

            aIdent.setLIB_IDENT(LIB_IDENT);
            aIdent.setMOT_PASSE(MOT_PASSE);
            aIdent.setCD_PROFIL(CD_PROFIL);
            aIdent.setETAT_CPT(ETAT_CPT);

            if ((MOT_PASSE2 != null) && (MOT_PASSE != null) && (MOT_PASSE.equals(MOT_PASSE2))) {
    			try {
    	            aIdent.maj(myDBSession);
    	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
    	            request.setAttribute("Action", "Modification");
    			}
    			catch (Exception e) {
    	            mySalon.setMessage("Erreur", e.toString());
    				request.setAttribute("Action", Action);
    			}
            }
            else {
                mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficIdent.motDePasseDifferent" + BasicSession.TAG_I18N);
                request.setAttribute("Action", Action);
            }
			request.setAttribute("IdentBean", aIdent);
		}
		else if (Action.equals("Suppression")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aIdent = IdentBean.getIdentBean(myDBSession, CD_IDENT);
            if (assertOrError((aIdent != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aIdent.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aIdent = new IdentBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
			}
			request.setAttribute("IdentBean", aIdent);
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

	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficIdent.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
