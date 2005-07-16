/*
 * Réédition de factures
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.EditionFacture;
/**
 * Réédition de factures
 * Creation date: (03/11/2001 22:57:04)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ReFact extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {
	
	// Récupère les paramètres
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String format = request.getParameter("format");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	if (format == null) {
		// Initialise les valeurs par défaut
		DateFormat formatDate  = DateFormat.getDateInstance(DateFormat.SHORT);

		if (DT_DEBUT == null) {
			// Début de mois
			Calendar J7 = Calendar.getInstance();
			J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
			DT_DEBUT = formatDate.format(J7.getTime());
		}
		if (DT_FIN == null) {
			DT_FIN = formatDate.format(Calendar.getInstance().getTime());
		}
        try {
            if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
                request.setAttribute("DT_DEBUT", formatDate.parse(DT_DEBUT));
            }
            if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
                request.setAttribute("DT_FIN", formatDate.parse(DT_FIN));
            }
        }
        catch (ParseException e) {
            mySalon.setMessage("Erreur", e.toString());
            e.printStackTrace();
        }
	}
	else if ((format.equals("L")) || (format.equals("F"))) {
		// Recherche des factures concernées
		String reqSQL = "select * from PAIEMENT where 1=1 ";

		if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
			reqSQL = reqSQL + " and DT_PAIEMENT >= '" + DT_DEBUT + "'";
		}
		if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
			reqSQL = reqSQL + " and DT_PAIEMENT < '" + DT_FIN + "'::date + 1";
		}
		reqSQL = reqSQL + " order by DT_PAIEMENT, DT_CREAT";

		Vector liste = new Vector();
		try {
	        ResultSet aRS = myDBSession.doRequest(reqSQL);

	        while (aRS.next()) {
		        PaiementBean aPaiement = new PaiementBean(aRS, mySalon.getMessagesBundle());
	        
				// Recherche les factures associées
		        Vector listeFact = aPaiement.getFact(myDBSession);
				FactBean aFact = null;

				for (int i = 0; i < listeFact.size(); i++) {
					if (aFact == null) {
						aFact = (FactBean) listeFact.get(i);
						aFact.getLignes(myDBSession);
						aFact.getTotPrest(myDBSession);
					}
					else {
						aFact.merge (myDBSession, Long.toString(((FactBean) listeFact.get(i)).getCD_FACT()));
					}
				}
				
				// Création du bean d'édition pour ce paiement
                if (aFact != null) {
                    EditionFacture aEditFact = new EditionFacture();
                    aEditFact.setMyPaiement(aPaiement);
                    aEditFact.setMyFact(aFact);

                    liste.add(aEditFact);
                }
	        }
	        aRS.close();
		}
		catch (Exception e) {
			mySalon.setMessage("Erreur", e.toString());
	        System.out.println("Note : " + e.toString());
		}

		request.setAttribute("listeEdition", liste);
	}
	else {
		mySalon.setMessage("Erreur", "Format non implémenté.");
	}

	try {
		// Passe la main à la fiche
		if ((format == null) || (mySalon.getMessage("Erreur") != null)) {
	        getServletConfig().getServletContext().getRequestDispatcher("/ficReFact.jsp").forward(request, response);
		}
		else if (format.equals("L")) {
	        getServletConfig().getServletContext().getRequestDispatcher("/ficReFactListe.jsp").forward(request, response);
		}
		else if (format.equals("F")) {
	        getServletConfig().getServletContext().getRequestDispatcher("/ficFactImpr.jsp").forward(request, response);
		}

	}
	catch (Exception e) {
		System.out.println("ReFact::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
