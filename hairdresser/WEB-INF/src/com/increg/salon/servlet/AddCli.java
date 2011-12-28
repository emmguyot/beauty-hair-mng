/*
 * Ajout d'un client dans l'encours
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.exception.FctlException;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.PointageBean;
import com.increg.salon.bean.SalonSession;
/**
 * Ajout d'un client aux encours
 * Creation date: (09/09/2001 22:00:00)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class AddCli extends ConnectedServlet {
	protected Log log = LogFactory.getLog(this.getClass());

/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
    //Le principe ici, c'est : 
    // - soit on passe un CD_CLI tout seul pour récupérer sa dernière facture
    // - soit on passe un CD_CLI et Vide à "1" pour récupérer une facture vide (accueil simple)
    // - soit on passe un CD_CLI, un CD_FACT et peu importe Vide pour récupérer la facture voulue.
	String CD_CLI = request.getParameter("CD_CLI");
	String Vide = request.getParameter("Vide");
    String CD_FACT = request.getParameter("CD_FACT");    
    String CD_COLLAB = request.getParameter("CD_COLLAB");    
    String action = request.getParameter("Action");    

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	boolean nouveauCollab = false; 
	
	if (action == null) {
		if ((CD_CLI != null) && (CD_FACT != null)) {
	        //On veut une facture bien précise
	        nouveauCollab = mySalon.addClientAvecFacture(CD_CLI, CD_FACT);
	    } else if (Vide == null) {
	        //On ajoute un client avec sa dernière facture comportant une prestation
	        //Ou une facture vide
	    	nouveauCollab = mySalon.addClient(CD_CLI);        
	    } else if (Vide.equals("1") == true) {
	        //On veut une facture vide pour ce client
		    mySalon.addEmptyFact(CD_CLI);
		    nouveauCollab = false;
		}        
	}
	else if (action.equals("Modification")){
		// Cas de la modification
		FactBean fact = FactBean.getFactBean(mySalon.getMyDBSession(), CD_FACT, mySalon.getMessagesBundle());
		fact.setCD_COLLAB(CD_COLLAB);
		
		Vector<HistoPrestBean> lignes = fact.getLignes(mySalon.getMyDBSession());
		for (int i=0; i < lignes.size(); i++) {
			lignes.get(i).setCD_COLLAB(CD_COLLAB);
		}
		
		try {
			fact.maj(mySalon.getMyDBSession());
			for (int i=0; i < lignes.size(); i++) {
				HistoPrestBean aHisto = lignes.get(i); 
				aHisto.setCD_COLLAB(CD_COLLAB);
				aHisto.maj(mySalon.getMyDBSession());
			}
		} catch (SQLException e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur générale : ", e);
		} catch (FctlException e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur générale : ", e);
		}
	}
	
	try {
		if (nouveauCollab) {
			request.setAttribute("lstCollab", CollabBean.getListeCollab(mySalon.getMyDBSession(), true));
			forward(request, response, "/selectCollab.jsp");
		}
		else {
			response.sendRedirect("refreshMenu.js");
		}
	}
	catch (Exception e) {
		log.error("Erreur à la redirection", e);
	}
}
}
