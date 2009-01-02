/*
 * Consultation fiche technique d'un client
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
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.SalonSession;

public class FicTech extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_CLI = request.getParameter("CD_CLI");
	String CD_CATEG_PREST[] = request.getParameterValues("CD_CATEG_PREST");
	String NbComment = request.getParameter("NbComment");

	if (NbComment == null) {
	    NbComment = "2";
	}

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
	Vector listeComment = new Vector();
	boolean all = false;//Permet de savoir si il faut cocher toutes les cases ou non
	
	try {
		String SQL = "select HISTO_PREST.*, PREST.CD_CATEG_PREST from HISTO_PREST, PREST, CATEG_PREST where CD_CLI=" + CD_CLI 
					+ " and HISTO_PREST.CD_PREST=PREST.CD_PREST and CATEG_PREST.CD_CATEG_PREST = PREST.CD_CATEG_PREST"
					+ " and HISTO_PREST.COMM is not null ";
		if (CD_CATEG_PREST != null) {
			SQL = SQL + "and PREST.CD_CATEG_PREST in (";
	        for (int i = 0; i < CD_CATEG_PREST.length; i++) {
	            SQL = SQL + CD_CATEG_PREST[i] + ",";
	        }
	        SQL = SQL.substring(0, SQL.length() - 1) + ")";
		}
		SQL = SQL + " order by CATEG_PREST.LIB_CATEG_PREST, DT_PREST DESC";

		long CD_CATEG_PREST_PREC = 0;
		long nbPrest = Long.parseLong(NbComment);
		long compteur = 0;
        String lastComment = "";

		// Interroge la Base
		ResultSet aRS = myDBSession.doRequest(SQL);

		while (aRS.next()) {
			/**
			 * Création du bean de consultation
			 */
			HistoPrestBean aHisto = new HistoPrestBean(aRS);
		   	int CD_CATEG_PREST_LU = aRS.getInt("CD_CATEG_PREST");

			if (CD_CATEG_PREST_PREC != CD_CATEG_PREST_LU) {
	            compteur = 0;
	            CD_CATEG_PREST_PREC = CD_CATEG_PREST_LU;
			}

			if (compteur < nbPrest) {
	            listeComment.add(aHisto);
			}
            
            // Ne compte que les commentaires différents
            if (!lastComment.equals(aHisto.getCOMM())) {
                lastComment = aHisto.getCOMM();
                compteur++;
            }

		}
		aRS.close();

	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		log.error("Erreur générale : ", e);
	}

    if (Action == null) {
    	//On vient d'arriver sur la page, il faut tout cocher
    	all = true;	
    } else {
        //il faut cocher que celles effectivement cochées par l'utilisateur
        all = false;
    }

    //Positionne les attributes de la session
	request.setAttribute("ClientBean", aCli);
	request.setAttribute("listeComment", listeComment);
	request.setAttribute("NbComment", NbComment);
	request.setAttribute("cocheTout", new Boolean(all));

	try {
		if ((Action == null) || (Action.equals(""))) {
			// Simple affichage
	        // Passe la main à la fiche de création
	        getServletConfig().getServletContext().getRequestDispatcher("/ficTech.jsp").forward(request, response);
		}
		else if (Action.equals("Impression")) {
			// Crée réellement la prestation
	        // Passe la main à la fiche d'impression
	        getServletConfig().getServletContext().getRequestDispatcher("/ficTechImpr.jsp").forward(request, response);
		}
		else {
			log.error("Action non codée : " + Action);
		}
	}
	catch (Exception e) {
		log.error("Erreur à la redirection : ", e);
	}
}
}
