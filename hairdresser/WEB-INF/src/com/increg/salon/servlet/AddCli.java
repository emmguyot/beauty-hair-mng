/*
 * Ajout d'un client dans l'encours
 * Copyright (C) 2001-2007 Emmanuel Guyot <See emmguyot on SourceForge> 
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

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

	if ((CD_CLI != null) && (CD_FACT != null)) {
        //On veut une facture bien précise
        mySalon.addClientAvecFacture(CD_CLI, CD_FACT);
    } else if (Vide == null) {
        //On ajoute un client avec sa dernière facture comportant une prestation
        //Ou une facture vide
        mySalon.addClient(CD_CLI);        
    } else if (Vide.equals("1") == true) {
        //On veut une facture vide pour ce client
	    mySalon.addEmptyFact(CD_CLI);
	}        
	
	try {
		// Passe la main à la fiche de création
		// redirect pour éviter de reproduire l'action au rafraichissement
		response.sendRedirect("Menu.jsp");
	}
	catch (Exception e) {
		System.out.println("AddCli::performTask : Erreur à la redirection : " + e.toString());
		log.error("Erreurà la redirection", e);
	}
}
}
