/*
 * Recherche/Liste de clients
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.SalonSession;

public class RechCli extends ConnectedServlet {

	protected Log log = LogFactory.getLog(this.getClass());

	/**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération du paramètre
        String type = request.getParameter("type");
        String action = request.getParameter("Action");
        boolean advancedQuery = ((type != null) && (type.equals("advanced")));
        
        Vector lstLignes;
        if (advancedQuery) {
        	lstLignes = getListQueryAdvanced(request);
        } else if (StringUtils.equals(action, "Groupe")) {
        	// Regroupement des clients sélectionnés
        	regroupeDoublon(request);
        	lstLignes = getListQueryDoublon(request);
        } else if (StringUtils.equals(action, "Doublon")) {
        	lstLignes = getListQueryDoublon(request);
        } else {
        	lstLignes = getListQuerySimple(request);
        }

        // Stocke le Vector pour le JSP
        request.setAttribute("Liste", lstLignes);

        // Interroge la Base
        try {
            // Affichage de la vue correspondante
            if (advancedQuery) {
                // Passe la main
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/lstCli_Advanced.jsp")
                    .forward(request, response);
            } else if (StringUtils.equals(action, "Doublon") 
            		|| StringUtils.equals(action, "Groupe")) {
                // Passe la main
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/lstCli_Doublon.jsp")
                    .forward(request, response);
            } else {
                // Passe la main
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/lstCli.jsp")
                    .forward(request, response);
            }

        } catch (Exception e) {
            log.error("Erreur dans performTask : ", e);
            try {
                response.sendError(500);
            } catch (Exception e2) {
                log.error("Erreur sur sendError : ", e2);
            }
        }
    }

    /**
     * Constitue la requete de recherche de client pour la recherche simple
     * @param request requete avec les paramétres 
     * @return résultat de la requete
     */
    private Vector getListQuerySimple(HttpServletRequest request) {
        String premLettre = request.getParameter("premLettre");
        String INDIC_VALID = request.getParameter("INDIC_VALID");
        if (premLettre == null) {
            premLettre = "A";
        }
        request.setAttribute("premLettre", premLettre);
        request.setAttribute("INDIC_VALID", INDIC_VALID);
        // Constitue la requete SQL
        String reqSQL =
            "select * from CLI where upper(ltrim(CLI.NOM)) like '"
                + premLettre
                + "%'";
        if ((INDIC_VALID == null) || (!INDIC_VALID.equals("on"))) {
            reqSQL = reqSQL + " and INDIC_VALID='O'";
        }
        reqSQL += " order by NOM, PRENOM, CD_CLI";
        return executeQuery(request, reqSQL);
    }

    /**
     * Constitue la requete de recherche de client pour la recherche avancée
     * @param request requete avec les paramétres 
     * @return résultat de la requete
     */
    private Vector getListQueryAdvanced(HttpServletRequest request) {
        String nom = request.getParameter("NOM");
        String prenom = request.getParameter("PRENOM");
        String civilite = request.getParameter("CIVILITE");
        String sexe = request.getParameter("sexe");
        String ville = request.getParameter("VILLE");
        String abonnement = request.getParameter("CD_PREST");
        String INDIC_VALID = request.getParameter("INDIC_VALID");
        String critereGlobal = request.getParameter("critereGlobal");

        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

        // Constitue la requete SQL
        StringBuffer reqSQL = new StringBuffer("select * from CLI");
        boolean where = false;
        
        if (nom == null) {
            // Paramètre non passé : Initialisation de la fiche, donc pas de recherce
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("0 = 1");
            mySalon.setMessage("Info", BasicSession.TAG_I18N + "rechCli.criteres" + BasicSession.TAG_I18N);
        }
        if ((nom != null) && (nom.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("NOM ilike ").append(DBSession.quoteWith("%" + nom + "%", '\''));
        }
        if ((prenom != null) && (prenom.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("PRENOM ilike ").append(DBSession.quoteWith("%" + prenom + "%", '\''));
        }
        if ((civilite != null) && (civilite.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("CIVILITE = ").append(DBSession.quoteWith(civilite, '\''));
        }
        if ((sexe != null) && (sexe.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            if (sexe.equals("H")) {
                reqSQL.append("CIVILITE = 'M. '");
            } else {
                reqSQL.append("CIVILITE in ('Mle', 'Mme')");
            }
        }
        if ((ville != null) && (ville.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("VILLE ilike ").append(DBSession.quoteWith("%" + ville + "%", '\''));
        }
        if ((abonnement != null) && (abonnement.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            if (abonnement.equals("*")) {
                reqSQL.append("CD_CLI in (select CD_CLI from ABO_CLI where CPT > 0)");
            }
            else {
                reqSQL.append("CD_CLI in (select CD_CLI from ABO_CLI where CPT > 0 and CD_PREST=").append(abonnement).append(')');
            }
        }
        if ((INDIC_VALID == null) || (!INDIC_VALID.equals("on"))) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("INDIC_VALID='O'");
        }
        if (StringUtils.isNotEmpty(critereGlobal)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("coalesce(COMM,'')||coalesce(EMAIL,'')||coalesce(NOM,'')||coalesce(PRENOM,'')")
            	.append("||coalesce(PORTABLE,'')||coalesce(TEL,'')||coalesce(RUE,'')||coalesce(VILLE,'') ilike ")
            	.append(DBSession.quoteWith("%" + critereGlobal + "%", '\''));
        }
        reqSQL.append(" order by NOM, PRENOM");
        return executeQuery(request, reqSQL.toString());
    }
    
    /**
     * Constitue la requete de recherche de client en doublon pour le client en paramètre
     * @param request requete avec les paramétres 
     * @return résultat de la requete
     */
    private Vector getListQueryDoublon(HttpServletRequest request) {
        String CD_CLI = request.getParameter("CD_CLI");
        request.setAttribute("CD_CLI", CD_CLI);
        // Charge le client source
        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        ClientBean client = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
        if (client == null) {
            return null;
        }
        request.setAttribute("Action", "Doublon");
        // Rechercher les doublons
        return new Vector(ClientBean.getDoubleClientBeans(myDBSession, client, mySalon.getMessagesBundle()));
    }

    /**
     * Regroupe les clients sélectionnés
     * @param request requete avec les paramétres
     */
    private void regroupeDoublon(HttpServletRequest request) {
        String CD_CLI = request.getParameter("CD_CLI");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (StringUtils.contains(paramName, "DOUBLON")) {
				String cdCli = paramName.substring(8);
				if (!cdCli.equals(CD_CLI)) {
					try {
	                    myDBSession.setDansTransactions(true);
						ClientBean.joinDouble(myDBSession, CD_CLI, cdCli, mySalon.getMessagesBundle());
						myDBSession.endTransaction();
					}
					catch (SQLException e) {
	                    mySalon.setMessage("Erreur", e);
	                    myDBSession.cleanTransaction();
					}
				}
			}
		}
    }
    
    /**
     * Execute une requete de recherche de client
     * @param request requete HTTP pour accès aux infos 
     * @param reqSQL requete SQL à passer
     * @return Liste des clients de la requete
     */
    private Vector executeQuery(HttpServletRequest request, String reqSQL) {
        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        Vector lstLignes = null;

        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            lstLignes = new Vector();

            while (aRS.next()) {
                lstLignes.add(new ClientBean(aRS, mySalon.getMessagesBundle()));
            }
            aRS.close();
        } catch (Exception e) {
            log.error("Erreur dans performTask : ", e);
            lstLignes = null;
        }
        
        return lstLignes;
    }
}