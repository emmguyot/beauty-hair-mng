package com.increg.salon.servlet;

import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.PrestBean;
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
        String NbPrest = request.getParameter("NbPrest");
        String Action = request.getParameter("Action");

        // Valeurs par défaut
        if (NbPrest == null) {
            NbPrest = "10";
            request.setAttribute("NbPrest", NbPrest);
        }

        if ((Action != null) && (Action.equals("Complet"))) {
            // Affichage de la liste complete de prestations
            NbPrest = Long.toString(Long.MAX_VALUE);
        }

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        DBSession myDBSession =
            ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
        ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);

        // Stocke le client
        request.setAttribute("ClientBean", aCli);

        /**
         * Recherche les prestations abonnements associées au client
         */
        Vector listePrest = new Vector();
        if ((CD_CLI != null) && (CD_CLI.length() > 0)) {
            String reqSQL = "select * from FACT where CD_CLI=" + CD_CLI + " order by DT_PREST desc";
            long nbPrest = Long.parseLong(NbPrest);
            long compteur = 0;

            // Interroge la Base
            try {
                ResultSet aRS = myDBSession.doRequest(reqSQL);

                while ((aRS.next()) && (compteur < nbPrest)) {
                    /**
                     * Création du bean de consultation
                     */
                    FactBean aFact = new FactBean(aRS);
                    Vector lignes = aFact.getLignes(myDBSession);

                    /**
                     * Boucle sur chaque ligne
                     */
                    for (int i = 0; (i < lignes.size()) && (compteur < nbPrest); i++) {
                        HistoPrestBean aHistoPrest = (HistoPrestBean) lignes.get(i);

                        // Filtrage sur abonnement
                        PrestBean aPrest = PrestBean.getPrestBean(myDBSession, Long.toString(aHistoPrest.getCD_PREST()));

                        if (aPrest.isConsommationAbonnement() || aPrest.isAbonnement()) {
                            listePrest.add(aHistoPrest);
                            compteur++;
                        }
                    }
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans requète sur clé : " + e.toString());
                try {
                    response.sendError(500);
                } catch (Exception e2) {
                    System.out.println("Erreur sur sendError : " + e2.toString());
                }
            }
            request.setAttribute("NbPrest", Long.toString(compteur));
        }

        request.setAttribute("listePrest", listePrest);

        try {
            // Passe la main à l'affichage
            getServletConfig()
                .getServletContext()
                .getRequestDispatcher("/lstAbonnement.jsp")
                .forward(request, response);

        } catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
            } catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }
}