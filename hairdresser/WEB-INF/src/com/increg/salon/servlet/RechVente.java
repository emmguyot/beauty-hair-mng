/*
 * Recherche/Liste des ventes
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

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.ServletUtil;

public class RechVente extends ConnectedServlet {

    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // Récupération des paramètres
        String DT_DEBUT = request.getParameter("DT_DEBUT");
        String DT_FIN = request.getParameter("DT_FIN");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

        // Valeurs par défaut
        // Début de mois
        Calendar J7 = Calendar.getInstance();
        J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
        Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, J7);
        Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, Calendar.getInstance());
        if (dtFin.before(dtDebut)) {
            dtFin = dtDebut;
        }
        request.setAttribute("DT_DEBUT", dtDebut);
        request.setAttribute("DT_FIN", dtFin);
        DT_DEBUT = myDBSession.getFormatDate().format(dtDebut.getTime());
        DT_FIN = myDBSession.getFormatDate().format(dtFin.getTime());

        Vector lstLignes = new Vector();

        try {
            lstLignes = FactBean.calculVente(myDBSession, DT_DEBUT, DT_FIN);
        }
        catch (Exception e) {
            log.error("Erreur dans performTask : ", e);
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return;
            }
            catch (Exception e2) {
                log.error("Erreur sur sendError : ", e2);
            }
        }

        try {
            // Stocke le Vector pour le JSP
            request.setAttribute("Liste", lstLignes);

            // Passe la main
            getServletConfig().getServletContext().getRequestDispatcher("/lstVente.jsp").forward(request, response);

        }
        catch (Exception e) {
            log.error("Erreur dans performTask : ", e);
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
            catch (Exception e2) {
                log.error("Erreur sur sendError : ", e2);
            }
        }
    }

}
