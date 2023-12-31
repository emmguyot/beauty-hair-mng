/*
 * R��dition de factures
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

import java.sql.ResultSet;
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
import com.increg.commun.exception.NoImplementationException;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.EditionFacture;
import com.increg.util.ServletUtil;
/**
 * R��dition de factures
 * Creation date: (03/11/2001 22:57:04)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ReFact extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {
	
	Log log = LogFactory.getLog(this.getClass());

	// R�cup�re les param�tres
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String format = request.getParameter("format");

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));
    DateFormat formatDateDB = myDBSession.getFormatDate();

	if (format == null) {
		// Initialise les valeurs par d�faut
		// D�but de mois
		Calendar J7 = Calendar.getInstance();
		J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
	    Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, J7);
	    Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, Calendar.getInstance());
	    if (dtFin.before(dtDebut)) {
	        dtFin = dtDebut;
	    }
	    request.setAttribute("DT_DEBUT", dtDebut);
	    request.setAttribute("DT_FIN", dtFin);
	    DT_DEBUT = formatDateDB.format(dtDebut.getTime());
	    DT_FIN = formatDateDB.format(dtFin.getTime());
	}
	else if ((format.equals("L")) || (format.equals("F"))) {
		// Recherche des factures concern�es
		String reqSQL = "select * from PAIEMENT where 1=1 ";

		Vector liste = new Vector();
		try {
			if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
				reqSQL = reqSQL + " and DT_PAIEMENT >= '" + formatDateDB.format(formatDate.parse(DT_DEBUT)) + "'";
			}
			if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
				reqSQL = reqSQL + " and DT_PAIEMENT < '" + formatDateDB.format(formatDate.parse(DT_FIN)) + "'::date + 1";
			}
			reqSQL = reqSQL + " order by DT_PAIEMENT, DT_CREAT";

			ResultSet aRS = myDBSession.doRequest(reqSQL);

	        while (aRS.next()) {
		        PaiementBean aPaiement = new PaiementBean(aRS, mySalon.getMessagesBundle());
	        
				// Recherche les factures associ�es
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
				
				// Cr�ation du bean d'�dition pour ce paiement
                if (aFact != null) {
                    EditionFacture aEditFact = new EditionFacture();
                    aEditFact.setMyPaiement(aPaiement);
                    aEditFact.setMyFact(aFact);
                    aEditFact.setReglements(aPaiement.getReglement(myDBSession));

                    liste.add(aEditFact);
                }
	        }
	        aRS.close();
		}
		catch (Exception e) {
			mySalon.setMessage("Erreur", e.toString());
	        log.error("Erreur g�n�rale : ", e);
		}

		request.setAttribute("listeEdition", liste);
	}
	else {
		mySalon.setMessage("Erreur", new NoImplementationException());
	}

	try {
		// Passe la main � la fiche
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
		log.error("Erreur � la redirection : ", e);
	}
}
}
