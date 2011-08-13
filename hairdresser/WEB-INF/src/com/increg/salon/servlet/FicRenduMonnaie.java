/*
 * Calcul le rendu d'espèces
 * Copyright (C) 2002-2011 Alexandre GUYOT <alexandre.guyot@laposte.net>
 * Emmanuel Guyot <See emmguyot on SourceForge> 
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

import com.increg.commun.BasicSession;
import com.increg.salon.bean.*;
import com.increg.util.Montant;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;

public class FicRenduMonnaie extends ConnectedServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3782294754248932354L;

	/**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // Récupération des paramètres
        String montant = request.getParameter("montant");
        String montantRegle = request.getParameter("montantRegle");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

        BigDecimal aRendre = new BigDecimal(0);
        try {
            aRendre = new Montant(montantRegle).subtract(new Montant(montant));
            if (aRendre.signum() < 0) { 
                // Montant trop faible
                mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficRenduMonnaie.montantFaible" + BasicSession.TAG_I18N);
            }
        }
        catch (Throwable t) {
            mySalon.setMessage("Erreur", t.toString());
            log.error("Erreur générale : ", t);
        }

        request.setAttribute("montant", montant);
        request.setAttribute("montantRegle", montantRegle);
        request.setAttribute("aRendre", aRendre);

        try {
            // Passe la main à la fiche d'affichage
            getServletConfig().getServletContext().getRequestDispatcher("/ficRenduMonnaie.jsp").forward(request, response);

        }
        catch (Exception e) {
            log.error("Erreur à la redirection : ", e);
        }
    }
}
