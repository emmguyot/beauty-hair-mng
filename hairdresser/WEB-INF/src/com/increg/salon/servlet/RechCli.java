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

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        DBSession myDBSession =
            ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();

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

            // Passe la main
            getServletConfig()
                .getServletContext()
                .getRequestDispatcher("/lstCli.jsp")
                .forward(request, response);

        }
        catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(500);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }
}