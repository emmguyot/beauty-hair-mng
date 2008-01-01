/*
 * Gestion des paramètres
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

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSession;

public class FicParam extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_PARAM = request.getParameter("CD_PARAM");
	String LIB_PARAM = request.getParameter("LIB_PARAM");
	String VAL_PARAM = request.getParameter("VAL_PARAM");
    String VAL_PARAM2 = request.getParameter("VAL_PARAM2");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	ParamBean aParam = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aParam = new ParamBean();
		}
		else if ((Action.equals("Modification")) && (LIB_PARAM == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aParam = ParamBean.getParamBean(myDBSession, CD_PARAM);
            if (assertOrError((aParam != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aParam = ParamBean.getParamBean(myDBSession, CD_PARAM);
            if (assertOrError((aParam != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			aParam.setCD_PARAM(CD_PARAM);
			aParam.setLIB_PARAM(LIB_PARAM);
            if ((aParam.getCD_PARAM() != ParamBean.CD_OP_EXCEPTIONNEL) 
                    || ((VAL_PARAM2 != null) && (VAL_PARAM.equals(VAL_PARAM2)))) {
    			aParam.setVAL_PARAM(VAL_PARAM);
                
                try {
                    aParam.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
            }
            else {
                mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficIdent.motDePasseDifferent" + BasicSession.TAG_I18N);
            }
            request.setAttribute("Action", Action);

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

	request.setAttribute("ParamBean", aParam);
	
	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficParam.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
