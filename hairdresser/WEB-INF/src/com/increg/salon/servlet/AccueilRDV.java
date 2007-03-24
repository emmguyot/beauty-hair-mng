/*
 * Page d'accueil avec carnet de rendez-vous
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

import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.SalonSession;

/**
 * Affiche la liste des clients répondant aux critères pour la création d'un RDV
 * Creation date: Aug 5, 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class AccueilRDV extends ConnectedServlet {

	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération du paramètre (Un seul saisi parmi l'ensemble des champs
        String critere = null;
        String nomParam = null;
        
        Enumeration paramEnum = request.getParameterNames();
        while (paramEnum.hasMoreElements()) {
            String param = (String) paramEnum.nextElement();
            
            if ((request.getParameter(param) != null) 
                    && (request.getParameter(param).length() > 0) 
                    && (param.charAt(0) == 'C')) {
                critere = request.getParameter(param);
                nomParam = param;
            }
        }

        HashMap attributes = new HashMap();
        String DT_DEBUT = request.getParameter("DT_DEBUT");

        if (nomParam != null) {
            attributes.put("CD_COLLAB", nomParam.substring(1, nomParam.indexOf('h')));
            attributes.put("DT_DEBUT", DT_DEBUT);
            String HR_DEBUT = nomParam.substring(nomParam.indexOf('h') + 1, nomParam.indexOf('m'));
            HR_DEBUT += ":" + nomParam.substring(nomParam.indexOf('m') + 1);
            attributes.put("HR_DEBUT", HR_DEBUT);
        }
        String nom = null; 
        String prenom = null;
        int posBlanc = critere.indexOf(' ');
        if (posBlanc > 0) {
            // 2 critères : Nom puis prénom
            nom = critere.substring(0, posBlanc);
            prenom = critere.substring(posBlanc + 1); 
            attributes.put("NOM", nom);
            attributes.put("PRENOM", prenom);
        }
        else {
            attributes.put("NOM", critere);
        }

        // Constitue la requete SQL
        String reqSQL =
            "select * from CLI where (ltrim(CLI.NOM) ilike '"
                + critere
                + "%'";
        if (nom != null) {
            reqSQL += "or (ltrim(CLI.NOM) ilike '" + nom + "%' and ltrim(CLI.PRENOM) ilike '" + prenom + "%')"; 
        }
        reqSQL += ") and INDIC_VALID='O'";
        reqSQL += " order by NOM, PRENOM";

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            Vector lstLignes = new Vector();

            while (aRS.next()) {
                lstLignes.add(new ClientBean(aRS, mySalon.getMessagesBundle()));
            }
            aRS.close();

            // Stocke le Vector pour le JSP
            attributes.put("Liste", lstLignes);

            // Stocke temporairement la requete dans la session pour le découpage en frame
            mySession.setAttribute("attributes", attributes);
            
            // Passe la main
            forward(request, response, "/ListeCliRDV.jsp");
        }
        catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            log.error("Erreur générale", e);
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
                log.error("Erreur sur redirection", e);
            }
        }
    }
}