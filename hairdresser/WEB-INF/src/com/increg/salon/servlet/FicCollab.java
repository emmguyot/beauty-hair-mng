/*
 * Fiche de création / modification d'un collab
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.SalonSession;

/**
 * Création d'un collaborateur
 * Creation date: (07/09/2001 13:38:32)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicCollab extends ConnectedServlet {

	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_COLLAB = request.getParameter("CD_COLLAB");
        String CIVILITE = request.getParameter("CIVILITE");
        String NOM = request.getParameter("NOM");
        String PRENOM = request.getParameter("PRENOM");
        String RUE = request.getParameter("RUE");
        String VILLE = request.getParameter("VILLE");
        String CD_POSTAL = request.getParameter("CD_POSTAL");
        String TEL = request.getParameter("TEL");
        String PORTABLE = request.getParameter("PORTABLE");
        String EMAIL = request.getParameter("EMAIL");
        String DT_NAIS = request.getParameter("DT_NAIS");
        String NUM_SECU = request.getParameter("NUM_SECU");
        String CD_FCT = request.getParameter("CD_FCT");
        String CD_TYP_CONTR = request.getParameter("CD_TYP_CONTR");
        String CATEG = request.getParameter("CATEG");
        String ECHELON = request.getParameter("ECHELON");
        String COEF = request.getParameter("COEF");
        String QUOTA_HEURE = request.getParameter("QUOTA_HEURE");
        String INDIC_VALID = request.getParameter("INDIC_VALID");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        CollabBean aCollab = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aCollab = new CollabBean();
            }
            else if (Action.equals("Creation")) {
                // Crée réellement la prestation

                /**
                 * Création du bean et enregistrement
                 */
                aCollab = new CollabBean();

                try {
                    aCollab.setCD_COLLAB(CD_COLLAB);
                    aCollab.setNOM(NOM);
                    aCollab.setCIVILITE(CIVILITE);
                    aCollab.setPRENOM(PRENOM);
                    aCollab.setRUE(RUE);
                    aCollab.setVILLE(VILLE);
                    aCollab.setCD_POSTAL(CD_POSTAL);
                    aCollab.setTEL(TEL);
                    aCollab.setPORTABLE(PORTABLE);
                    aCollab.setEMAIL(EMAIL);
                    aCollab.setDT_NAIS(DT_NAIS, mySalon.getLangue());
                    aCollab.setNUM_SECU(NUM_SECU);
                    aCollab.setCD_FCT(CD_FCT);
                    aCollab.setCD_TYP_CONTR(CD_TYP_CONTR);
                    aCollab.setCATEG(CATEG);
                    aCollab.setECHELON(ECHELON);
                    aCollab.setCOEF(COEF);
                    aCollab.setQUOTA_HEURE(QUOTA_HEURE);
                    aCollab.setINDIC_VALID(INDIC_VALID);

                    aCollab.create(myDBSession);

                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    log.error("Erreur à la création du collab", e);
                    request.setAttribute("Action", Action);
                }
            }
            else if ((Action.equals("Modification")) && (NOM == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aCollab = CollabBean.getCollabBean(myDBSession, CD_COLLAB);
                if (assertOrError((aCollab != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
                request.setAttribute("CollabBean", aCollab);

            }
            else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aCollab = CollabBean.getCollabBean(myDBSession, CD_COLLAB);
                if (assertOrError((aCollab != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                try {
                    aCollab.setCD_COLLAB(CD_COLLAB);
                    aCollab.setNOM(NOM);
                    aCollab.setCIVILITE(CIVILITE);
                    aCollab.setPRENOM(PRENOM);
                    aCollab.setRUE(RUE);
                    aCollab.setVILLE(VILLE);
                    aCollab.setCD_POSTAL(CD_POSTAL);
                    aCollab.setTEL(TEL);
                    aCollab.setPORTABLE(PORTABLE);
                    aCollab.setEMAIL(EMAIL);
                    aCollab.setDT_NAIS(DT_NAIS, mySalon.getLangue());
                    aCollab.setNUM_SECU(NUM_SECU);
                    aCollab.setCD_FCT(CD_FCT);
                    aCollab.setCD_TYP_CONTR(CD_TYP_CONTR);
                    aCollab.setCATEG(CATEG);
                    aCollab.setECHELON(ECHELON);
                    aCollab.setCOEF(COEF);
                    aCollab.setQUOTA_HEURE(QUOTA_HEURE);
                    aCollab.setINDIC_VALID(INDIC_VALID);

                    aCollab.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    log.error("Erreur à la mise à jour du collab", e);
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aCollab = CollabBean.getCollabBean(myDBSession, CD_COLLAB);
                if (assertOrError((aCollab != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                try {
                    aCollab.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aCollab = new CollabBean();
                    request.setAttribute("Action", "Creation");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    log.error("Erreur à la suppression du collab", e);
                    request.setAttribute("Action", "Modification");
                }
            }
            else {
                log.error("Action non codée : " + Action);
            }
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur générale", e);
        }

        /**
         * Reset de la transaction pour la recherche des informations complémentaires
         */
        myDBSession.cleanTransaction();

        request.setAttribute("CollabBean", aCollab);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficCollab.jsp").forward(request, response);

        }
        catch (Exception e) {
            log.error("Erreur à la redirection", e);
        }
    }
}
