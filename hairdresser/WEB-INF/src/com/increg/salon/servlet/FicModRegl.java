/*
 * Gestion des modes de règlement
 * Copyright (C) 2002-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import com.increg.salon.bean.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.*;

public class FicModRegl extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

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
            if (assertOrError((aModRegl != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aModRegl = ModReglBean.getModReglBean(myDBSession, CD_MOD_REGL);
            if (assertOrError((aModRegl != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
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
            if (assertOrError((aModRegl != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
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

	request.setAttribute("ModReglBean", aModRegl);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficModRegl.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
