/*
 * Cr�ation d'un fournisseur
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
import com.increg.salon.bean.FournBean;
import com.increg.salon.bean.SalonSession;

public class FicFourn extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// R�cup�ration des param�tres
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

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	FournBean aFourn = null;
	try {
		if (Action == null) {
			// Premi�re phase de cr�ation
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aFourn = new FournBean();
		}
		else if (Action.equals("Creation")) {
			// Cr�e r�ellement le client

			/**
			 * Cr�ation du bean et enregistrement
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
			 * Cr�ation du bean et enregistrement
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
			 * Cr�ation du bean et enregistrement
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

	request.setAttribute("FournBean", aFourn);
	
	try {
		// Passe la main � la fiche de cr�ation
		getServletConfig().getServletContext().getRequestDispatcher("/ficFourn.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur � la redirection : ", e);
	}
}
}
