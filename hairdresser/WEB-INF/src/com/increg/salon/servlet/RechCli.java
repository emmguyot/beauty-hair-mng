package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.SalonSession;

/**
 * Recherche/Liste de clients
 * Creation date: (17/07/2001 13:56:35)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechCli extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération du paramètre
        String type = request.getParameter("type");
        boolean advancedQuery = ((type != null) && (type.equals("advanced")));
        
        String reqSQL;
        if (advancedQuery) {
            reqSQL = getQueryAdvanced(request);
        } else {
            reqSQL = getQuerySimple(request);
        }

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();

        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            Vector lstLignes = new Vector();

            while (aRS.next()) {
                lstLignes.add(new ClientBean(aRS));
            }
            aRS.close();

            // Stocke le Vector pour le JSP
            request.setAttribute("Liste", lstLignes);

            // Affichage de la vue correspondante
            if (advancedQuery) {
                // Passe la main
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/lstCli_Advanced.jsp")
                    .forward(request, response);
            } else {
                // Passe la main
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/lstCli.jsp")
                    .forward(request, response);
            }

        } catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(500);
            } catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }

    /**
     * Constitue la requete de recherche de client pour la recherche simple
     * @param request requete avec les paramétres 
     * @return requete SQL à executer
     */
    private String getQuerySimple(HttpServletRequest request) {
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
        reqSQL += " order by NOM, PRENOM";
        return reqSQL;
    }

    /**
     * Constitue la requete de recherche de client pour la recherche avancée
     * @param request requete avec les paramétres 
     * @return requete SQL à executer
     */
    private String getQueryAdvanced(HttpServletRequest request) {
        String nom = request.getParameter("NOM");
        String prenom = request.getParameter("PRENOM");
        String civilite = request.getParameter("CIVILITE");
        String sexe = request.getParameter("sexe");
        String ville = request.getParameter("VILLE");
        String abonnement = request.getParameter("CD_PREST");
        String INDIC_VALID = request.getParameter("INDIC_VALID");

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
            mySalon.setMessage("Info", "Veuillez saisir vos critères.");
        }
        if ((nom != null) && (nom.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("NOM ilike ").append(DBSession.quoteWith(nom, '\''));
        }
        if ((prenom != null) && (prenom.length() > 0)) {
            if (!where) {
                reqSQL.append(" where ");
                where = true;
            } else {
                reqSQL.append(" and ");
            }
            reqSQL.append("PRENOM ilike ").append(DBSession.quoteWith(prenom, '\''));
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
            reqSQL.append("VILLE ilike ").append(DBSession.quoteWith(ville, '\''));
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
        reqSQL.append(" order by NOM, PRENOM");
        return reqSQL.toString();
    }
}