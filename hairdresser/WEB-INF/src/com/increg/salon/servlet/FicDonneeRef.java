/*
 * Gestion des données de référence
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
import com.increg.salon.bean.DonneeRefBean;
import com.increg.salon.bean.SalonSession;

public class FicDonneeRef extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String nomTable = request.getParameter("nomTable");
	String CD = request.getParameter("CD");
	String LIB = request.getParameter("LIB");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	DonneeRefBean aDonneeRef = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aDonneeRef = new DonneeRefBean(nomTable);
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
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
            if (assertOrError((aDonneeRef != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aDonneeRef = DonneeRefBean.getDonneeRefBean(myDBSession, nomTable, CD);
            if (assertOrError((aDonneeRef != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
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
			 * Création du bean et enregistrement
			 */
			aDonneeRef = DonneeRefBean.getDonneeRefBean(myDBSession, nomTable, CD);
            if (assertOrError((aDonneeRef != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
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
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
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

	request.setAttribute("DonneeRefBean", aDonneeRef);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficDonneeRef.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
