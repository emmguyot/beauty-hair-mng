/*
 * Recherche/Liste des informations de synthèse de la journée
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

import java.util.Calendar;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.Brouillard;

public class RechFinJournee extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();

	// Date du jour
	Calendar dtJour = Calendar.getInstance(); 
	request.setAttribute("DT_JOUR", dtJour);
	String DT_JOUR  = myDBSession.getFormatDate().format(dtJour.getTime());

    TreeMap lstLignesB = new TreeMap();
    TreeMap lstTypeB = new TreeMap();
    TreeMap lstTypeRemB = new TreeMap();
    TreeMap lstTypeMcaB = new TreeMap();
    Brouillard brouillardTotal = new Brouillard();

    Vector lstLignesC = new Vector();
    TreeSet lstTypeC = new TreeSet();

    Vector listeCaisse = new Vector();
    
    try {
        // Calcule le brouillard pour avoir la répartition pour tout le salon
        RechBrouillard.rechercheBrouillard(myDBSession, DT_JOUR, DT_JOUR, lstLignesB, lstTypeB, lstTypeRemB, lstTypeMcaB, brouillardTotal);
        
        // Calcule la ventilation par personne
        RechCA.rechercheCA(myDBSession, null, DT_JOUR, DT_JOUR, lstLignesC, lstTypeC);
        
        // Recherche les soldes de caisse pour aider à recompter
        FicMvtCaisse.rechercheTouteCaisse(myDBSession, "O", listeCaisse);
        
    }
    catch (Exception e) {
        try {
            response.sendError(500);
            return;
        }
        catch (Exception e2) {
            log.error("Erreur sur sendError : ", e2);
        }
    }
    
    try {
        // Stocke les Map pour le JSP
        request.setAttribute("ListeB", lstLignesB);
        request.setAttribute("ListeTypeB", lstTypeB);
        request.setAttribute("ListeTypeRemB", lstTypeRemB);
        request.setAttribute("ListeTypeMcaB", lstTypeMcaB);
        request.setAttribute("TotalB", brouillardTotal);

        // Stocke le Vector pour le JSP
        request.setAttribute("ListeC", lstLignesC);
        request.setAttribute("ListeTypeC", new Vector(lstTypeC));
        
        request.setAttribute("ListeCaisse", new Vector(listeCaisse));
        
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstFinJournee.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur dans performTask : ", e);
		try {
			response.sendError(500);
		}
		catch (Exception e2) {
			log.error("Erreur sur sendError : ", e2);
		}
	}
}
}
