/*
 * Gestion des types de mouvement de caisse
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
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TypMcaBean;

public class FicTypMca extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");
	String LIB_TYP_MCA = request.getParameter("LIB_TYP_MCA");
	String SENS_MCA = request.getParameter("SENS_MCA");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	TypMcaBean aTypMca = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aTypMca = new TypMcaBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aTypMca = new TypMcaBean();
			aTypMca.setCD_TYP_MCA(CD_TYP_MCA);
			aTypMca.setLIB_TYP_MCA(LIB_TYP_MCA);
			aTypMca.setSENS_MCA(SENS_MCA);

			try {
	            aTypMca.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_TYP_MCA == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aTypMca = TypMcaBean.getTypMcaBean(myDBSession, CD_TYP_MCA);
            if (assertOrError((aTypMca != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aTypMca = TypMcaBean.getTypMcaBean(myDBSession, CD_TYP_MCA);
            if (assertOrError((aTypMca != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			aTypMca.setCD_TYP_MCA(CD_TYP_MCA);
			aTypMca.setLIB_TYP_MCA(LIB_TYP_MCA);
            if (!aTypMca.getSENS_MCA().equals(SENS_MCA)) {
                // Modification du sens : Vérification si possible
                if (aTypMca.sensIsModifiable(myDBSession)) {
                    aTypMca.setSENS_MCA(SENS_MCA);
                }
                else {
                    mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficTypMvt.typeUtilise" + BasicSession.TAG_I18N);
                }
            }

			try {
	            aTypMca.maj(myDBSession);
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
			aTypMca = TypMcaBean.getTypMcaBean(myDBSession, CD_TYP_MCA);
            if (assertOrError((aTypMca != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aTypMca.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aTypMca = new TypMcaBean();
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
			aTypMca = new TypMcaBean();
			aTypMca.setLIB_TYP_MCA(LIB_TYP_MCA);
			aTypMca.setSENS_MCA(SENS_MCA);

			try {
	            aTypMca.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
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

	request.setAttribute("TypMcaBean", aTypMca);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficTypMca.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
