/*
 * Création d'un pointage
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.PointageBean;
import com.increg.salon.bean.SalonSession;

public class FicPointage extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_COLLAB = request.getParameter("CD_COLLAB");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String HR_DEBUT = request.getParameter("HR_DEBUT");
	String HR_FIN = request.getParameter("HR_FIN");
	String CD_TYP_POINTAGE = request.getParameter("CD_TYP_POINTAGE");
	String COMM = request.getParameter("COMM");

	// Concatene les heures pour former un timestamp
	if ((DT_DEBUT != null) && (HR_DEBUT != null)) {
	    DT_DEBUT = DT_DEBUT + " " + HR_DEBUT;
	    DT_DEBUT = DT_DEBUT.trim();
	}
	if ((DT_FIN != null) && (HR_FIN != null)) {
	    DT_FIN = DT_FIN + " " + HR_FIN;
	    DT_FIN = DT_FIN.trim();
	}

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateHeureSimpleSansSeconde"));

	PointageBean aPointage = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aPointage = new PointageBean();
		}
		else if (Action.equals("Creation")) {
			// Crée réellement la prestation

			/**
			 * Création du bean et enregistrement
			 */
			aPointage = new PointageBean();

			try {
	            aPointage.setCD_COLLAB(CD_COLLAB);
                Calendar dtFin = Calendar.getInstance();
                dtFin.clear();
                dtFin.setTime(formatDate.parse(DT_FIN));
	            aPointage.setDT_FIN(dtFin);
                Calendar dtDebut = Calendar.getInstance();
                dtDebut.clear();
                dtDebut.setTime(formatDate.parse(DT_DEBUT));
	            aPointage.setDT_DEBUT(dtDebut);
	            aPointage.setCD_TYP_POINTAGE(CD_TYP_POINTAGE);
	            aPointage.setCOMM(COMM);

	            aPointage.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (CD_TYP_POINTAGE == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

            Calendar dtDebut = Calendar.getInstance();
            dtDebut.clear();
            dtDebut.setTime(formatDate.parse(DT_DEBUT));
			aPointage = PointageBean.getPointageBean(myDBSession, CD_COLLAB, dtDebut.getTime());
            if (assertOrError((aPointage != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
			request.setAttribute("PointageBean", aPointage);

		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
            Calendar dtDebut = Calendar.getInstance();
            dtDebut.clear();
            dtDebut.setTime(formatDate.parse(DT_DEBUT));
			aPointage = PointageBean.getPointageBean(myDBSession, CD_COLLAB, dtDebut.getTime());
            if (assertOrError((aPointage != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            //aPointage.setCD_COLLAB(CD_COLLAB);
                Calendar dtFin = Calendar.getInstance();
                dtFin.clear();
                dtFin.setTime(formatDate.parse(DT_FIN));
	            aPointage.setDT_FIN(dtFin);
	            //aPointage.setDT_DEBUT(DT_DEBUT);
	            aPointage.setCD_TYP_POINTAGE(CD_TYP_POINTAGE);
	            aPointage.setCOMM(COMM);

	            aPointage.maj(myDBSession);
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
            Calendar dtDebut = Calendar.getInstance();
            dtDebut.clear();
            dtDebut.setTime(formatDate.parse(DT_DEBUT));
			aPointage = PointageBean.getPointageBean(myDBSession, CD_COLLAB, dtDebut.getTime());
            if (assertOrError((aPointage != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
	            aPointage.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aPointage = new PointageBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
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

	request.setAttribute("PointageBean", aPointage);

	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficPointage.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
