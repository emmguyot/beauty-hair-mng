package com.increg.salon.servlet;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.SalonSession;

/**
 * Recherche/Liste des abonnement d'un client
 * Creation date: (17/07/2001 13:56:35)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechAbonnement extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération du paramètre
        String CD_CLI = request.getParameter("CD_CLI");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        DBSession myDBSession =
            ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
        ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);

        // Interroge la Base
        try {
            // Stocke le Vector pour le JSP
            request.setAttribute("ClientBean", aCli);

            // Passe la main
            getServletConfig()
                .getServletContext()
                .getRequestDispatcher("/lstAbonnement.jsp")
                .forward(request, response);

        }
        catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }
}