/*
 * Gestion des f�te
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.FeteBean;
import com.increg.salon.bean.SalonSession;

public class FicFete extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	HttpServletRequest request,
	HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// R�cup�ration des param�tres
	String Action = request.getParameter("Action");
	String CD_FETE = request.getParameter("CD_FETE");
	String PRENOM = request.getParameter("PRENOM");
	String DT_FETE = request.getParameter("DT_FETE");

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	FeteBean aFete = null;
	
	try {
		if (Action == null) {
			// Premi�re phase de cr�ation
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aFete = new FeteBean();
		}
		else if (Action.equals("Creation")) {
			// Modification effective de la fiche

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aFete = new FeteBean();

			try {
	            aFete.setCD_FETE(CD_FETE);
	            aFete.setPRENOM(PRENOM);
	            aFete.setDT_FETE(DT_FETE, mySalon.getLangue());

	            aFete.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
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
            if (assertOrError((aFete != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aFete = FeteBean.getFeteBean(myDBSession, CD_FETE);
            if (assertOrError((aFete != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aFete.setCD_FETE(CD_FETE);
	            aFete.setPRENOM(PRENOM);
	            aFete.setDT_FETE(DT_FETE, mySalon.getLangue());

	            aFete.maj(myDBSession);
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
			 * Cr�ation du bean et enregistrement
			 */
			aFete = FeteBean.getFeteBean(myDBSession, CD_FETE);
            if (assertOrError((aFete != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aFete.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
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
             * Cr�ation du bean et enregistrement
             */
            aFete = new FeteBean();

            try {
                aFete.setPRENOM(PRENOM);
	            aFete.setDT_FETE(DT_FETE, mySalon.getLangue());

                aFete.create(myDBSession);
                mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
                request.setAttribute("Action", "Modification");
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
                request.setAttribute("Action", Action);
            }
        }
		else {
			log.error("Action non cod�e : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		log.error("Erreur g�n�rale : ", e);
	}

    /**
     * Reset de la transaction pour la recherche des informations compl�mentaires
     */
    myDBSession.cleanTransaction();

	request.setAttribute("FeteBean", aFete);
	
	try {
		// Passe la main � la fiche de cr�ation
		getServletConfig().getServletContext().getRequestDispatcher("/ficFete.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur � la redirection : ", e);
	}
}
}
