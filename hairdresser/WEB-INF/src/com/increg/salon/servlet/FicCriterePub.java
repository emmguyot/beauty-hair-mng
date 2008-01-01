/*
 * Création d'un critère de publipostage
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;
import com.increg.salon.bean.CriterePubBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.ServletUtil;

public class FicCriterePub extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

    	// Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_CRITERE_PUB = request.getParameter("CD_CRITERE_PUB");
        String LIB_CRITERE_PUB = request.getParameter("LIB_CRITERE_PUB");
        String CLAUSE = request.getParameter("CLAUSE");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

        // Resultat
        CriterePubBean aCriterePub = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aCriterePub = new CriterePubBean(mySalon.getMessagesBundle());
            }
            else if (Action.equals("Creation")) {
                // Crée réellement le critère

                /**
                 * Création du bean et enregistrement
                 */
                aCriterePub = new CriterePubBean(mySalon.getMessagesBundle());
                aCriterePub.setLIB_CRITERE_PUB(LIB_CRITERE_PUB);
                aCriterePub.setCLAUSE(CLAUSE);

                try {
                    aCriterePub.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if ((Action.equals("Modification")) && (LIB_CRITERE_PUB == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aCriterePub = CriterePubBean.getCriterePubBean(myDBSession, CD_CRITERE_PUB, mySalon.getMessagesBundle());
                if (assertOrError((aCriterePub != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
            }
            else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aCriterePub = CriterePubBean.getCriterePubBean(myDBSession, CD_CRITERE_PUB, mySalon.getMessagesBundle());
                if (assertOrError((aCriterePub != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                aCriterePub.setCD_CRITERE_PUB(CD_CRITERE_PUB);
                aCriterePub.setLIB_CRITERE_PUB(LIB_CRITERE_PUB);
                aCriterePub.setCLAUSE(CLAUSE);

                try {
                    aCriterePub.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Duplication")) {
                // Duplication de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aCriterePub = new CriterePubBean(mySalon.getMessagesBundle());

                aCriterePub.setLIB_CRITERE_PUB(LIB_CRITERE_PUB);
                aCriterePub.setCLAUSE(CLAUSE);

                try {
                    aCriterePub.create(myDBSession);

                    mySalon.setMessage(
                        "Info",
                        BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
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
                aCriterePub = CriterePubBean.getCriterePubBean(myDBSession, CD_CRITERE_PUB, mySalon.getMessagesBundle());
                if (assertOrError((aCriterePub != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                try {
                    // Suppression de la stat
                    aCriterePub.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aCriterePub = new CriterePubBean(mySalon.getMessagesBundle());
                    request.setAttribute("Action", "Creation");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
            }
            else if (Action.equals("Construction")) {
                // Définition du graphe de stat et de ses paramètres
                aCriterePub = CriterePubBean.getCriterePubBean(myDBSession, CD_CRITERE_PUB, mySalon.getMessagesBundle());
                if (assertOrError((aCriterePub != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                // Construit la liste des paramètres à valoriser pour afficher la statistique
                Vector listeParam = aCriterePub.getParameters();

                request.setAttribute("listeParam", listeParam);
            }
            else if (Action.equals("Extraction")) {
                // Affichage du graphe
                aCriterePub = CriterePubBean.getCriterePubBean(myDBSession, CD_CRITERE_PUB, mySalon.getMessagesBundle());
                if (assertOrError((aCriterePub != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                // Constitue la liste des paramètres pour utilisation
                Map paramMap = new HashMap();
                for (Enumeration i = request.getParameterNames();
                        i.hasMoreElements();) {
                    String paramName = (String) i.nextElement();
                    if (request.getParameter(paramName).length() > 0) {
                    	String paramValue = request.getParameter(paramName);
                    	if (paramName.indexOf("Date") != -1) {
                    		// C'est une date : Convertion de la date dans le format BD
                            Calendar dt = ServletUtil.interpreteDate(paramValue, formatDate, Calendar.getInstance());
                            paramValue = myDBSession.getFormatDate().format(dt.getTime());
                    	}
                        paramMap.put(paramName,
                                    paramValue);
                    }
                }

                try {
                    Vector listeValeurs = aCriterePub.getData(myDBSession, paramMap);
                    request.setAttribute("Liste", listeValeurs);
                }
                catch (FctlException e) {
                    // Message à afficher graphiquement
                    request.setAttribute("Erreur", e.toString());
                    log.error("Erreur sur getData", e);
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

        request.setAttribute("CriterePubBean", aCriterePub);

        try {
            if ((Action != null) && (Action.equals("Construction"))) {
                // Passe la main à la fiche de critère
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/ficPub.jsp")
                    .forward(request, response);
            }
            else if ((Action != null) && (Action.equals("Extraction"))) {
                // Passe la main à la fiche création des données
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/ficPubCSV.jsp")
                    .forward(request, response);
            }
            else {
                // Passe la main à la fiche de création d'un critère
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/ficCriterePub.jsp")
                    .forward(request, response);
            }
        }
        catch (Exception e) {
            log.error("Erreur à la redirection : ", e);
        }
    }
}