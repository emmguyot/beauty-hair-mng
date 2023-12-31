/*
 * Gestion des taux de TVA
 * Copyright (C) 2003-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TvaBean;

public class FicTxTVA extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // R�cup�ration des param�tres
        String Action = request.getParameter("Action");
        String CD_TVA = request.getParameter("CD_TVA");
        String LIB_TVA = request.getParameter("LIB_TVA");
        String TX_TVA = request.getParameter("TX_TVA");

        // R�cup�re la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession
                .getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        TvaBean aTva = null;

        try {
            if (Action == null) {
                // Premi�re phase de cr�ation
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aTva = new TvaBean();
            } else if (Action.equals("Creation")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTva = new TvaBean();

                try {
                    aTva.setCD_TVA(CD_TVA);
                    aTva.setLIB_TVA(LIB_TVA);
                    aTva.setTX_TVA(TX_TVA);

                    aTva.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else if ((Action.equals("Modification"))
                    && (LIB_TVA == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aTva = TvaBean.getTvaBean(myDBSession, CD_TVA);
                if (assertOrError((aTva != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
            } else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTva = TvaBean.getTvaBean(myDBSession, CD_TVA);
                if (assertOrError((aTva != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                aTva.setCD_TVA(CD_TVA);
                aTva.setLIB_TVA(LIB_TVA);
                aTva.setTX_TVA(TX_TVA);

                try {
                    aTva.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", Action);

            } else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTva = TvaBean.getTvaBean(myDBSession, CD_TVA);
                if (assertOrError((aTva != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                try {
                    aTva.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aTva = new TvaBean();
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", Action);

            } else if (Action.equals("Duplication")) {
                // Duplique la prestation

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTva = new TvaBean();

                try {
                    aTva.setLIB_TVA(LIB_TVA);
                    aTva.setTX_TVA(TX_TVA);

                    aTva.create(myDBSession);
                    mySalon
                            .setMessage("Info",
                                    BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else {
                log.error("Action non cod�e : " + Action);
            }
        } catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur g�n�rale : ", e);
        }

        /**
         * Reset de la transaction pour la recherche des informations
         * compl�mentaires
         */
        myDBSession.cleanTransaction();

        request.setAttribute("TvaBean", aTva);

        try {
            // Passe la main � la fiche de cr�ation
            getServletConfig().getServletContext().getRequestDispatcher(
                    "/ficTxTVA.jsp").forward(request, response);

        } catch (Exception e) {
            log.error("Erreur � la redirection : ", e);
        }
    }
}
